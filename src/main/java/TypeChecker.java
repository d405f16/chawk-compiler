import SymbolTable.Symbol;
import SymbolTable.SymbolTable;
import org.antlr.v4.runtime.tree.ParseTree;

class TypeChecker extends cHawkBaseVisitor {
    private SymbolTable symbolTable = new SymbolTable();

    @Override
    public Object visit(ParseTree tree) {
        if (tree == null) {
            return "";
        }
        return super.visit(tree);
    }

    @Override
    public Object visitSetup(cHawkParser.SetupContext ctx) {
        symbolTable.pushScope();
        super.visitSetup(ctx);
        symbolTable.popScope();
        return null;
    }

    @Override
    public Object visitRoute(cHawkParser.RouteContext ctx) {
        symbolTable.pushScope();
        super.visitRoute(ctx);
        symbolTable.popScope();
        return null;
    }

    @Override
    public Object visitBody(cHawkParser.BodyContext ctx) {
        return super.visitBody(ctx);
    }

    //region Statements
    @Override
    public Object visitVariableStatement(cHawkParser.VariableStatementContext ctx) {
        Object type = visit(ctx.expression());
        symbolTable.currentScope().define(ctx.IDENTIFIER().getText(), (String) type, null);
        return null;
    }

    @Override
    public Object visitArrayStatement(cHawkParser.ArrayStatementContext ctx) {
        Object arrayType = visit(ctx.expression(0));

        for (cHawkParser.ExpressionContext expression : ctx.expression()) {
            Object type = visit(expression);
            if (!type.equals(arrayType)) {
                throw new NumberFormatException("elements in array line " + ctx.getStart().getLine() + " are not all " +
                        "of same type");
            }
        }
        symbolTable.currentScope().define(ctx.IDENTIFIER().getText(), (String) arrayType, ctx.expression().size());
        return null;
    }

    @Override
    public Object visitFunctionStatement(cHawkParser.FunctionStatementContext ctx) {
        Object type = visit(ctx.body());
        if (type == null) {
            symbolTable.currentScope().define(ctx.IDENTIFIER().getText(), "Void", null);
        } else {
            symbolTable.currentScope().define(ctx.IDENTIFIER().getText(), (String) type, null);
        }
        return null;
    }

    @Override
    public Object visitIfStatement(cHawkParser.IfStatementContext ctx) {
        if (!visit(ctx.expression()).equals("Boolean")) {
            throw new NumberFormatException("the condition " + ctx.getStart().getLine() + " must be a boolean " +
                    "expression");
        }
        visit(ctx.body());
        return null;
    }

    @Override
    public Object visitIfElseStatement(cHawkParser.IfElseStatementContext ctx) {
        if (!visit(ctx.expression()).equals("Boolean")) {
            throw new NumberFormatException("the condition line " + ctx.getStart().getLine() + " must be a boolean " +
                    "expression");
        }
        visit(ctx.body(0));
        visit(ctx.body(1));
        return null;
    }

    @Override
    public Object visitForStatement(cHawkParser.ForStatementContext ctx) {
        for (cHawkParser.ExpressionContext expression : ctx.expression()) {
            if (!visit(expression).equals("Integer")) {
                throw new NumberFormatException("the assignment line " + ctx.getStart().getLine() + " must be of type" +
                        " integer");
            }
        }
        visit(ctx.body());
        return null;
    }

    @Override
    public Object visitWhileStatement(cHawkParser.WhileStatementContext ctx) {
        if (!visit(ctx.expression()).equals("Boolean")) {
            throw new NumberFormatException("the condition line " + ctx.getStart().getLine() + " must be a boolean " +
                    "expression");
        }
        visit(ctx.body());
        return null;
    }
    //endregion

    //region Expressions
    @Override
    public Object visitValueExpression(cHawkParser.ValueExpressionContext ctx) {
        String value = ctx.getText();
        if (value.equals("true") || value.equals("false")) {
            return "Boolean";
        } else {
            try {
                Integer.parseInt(value);
                return "Integer";
            } catch (NumberFormatException e) {
                try {
                    Float.parseFloat(value);
                    return "Float";
                } catch (NumberFormatException f) {
                    return "String";
                }
            }
        }
    }

    @Override
    public Object visitVariableExpression(cHawkParser.VariableExpressionContext ctx) {
        if (ctx.getParent().getClass().getSimpleName().equals("SystemVariableExpressionContext")) {
            return null; // TODO handle system variables
            // it is unknown what type the varaible will have. Maybe a
            // dictionary of all system variables with their return types
        }
        Symbol symbol = symbolTable.currentScope().resolve(ctx.IDENTIFIER().getText());
        if (symbol == null) {
            throw new NumberFormatException("variable '" + ctx.IDENTIFIER().getText() + "' line " + ctx.getStart()
                    .getLine() + " is undefined");
        }
        return symbol.getType();
    }

    @Override
    public Object visitArrayExpression(cHawkParser.ArrayExpressionContext ctx) {
        Symbol symbol = symbolTable.currentScope().resolve(ctx.IDENTIFIER().getText());
        if (symbol == null) {
            throw new NumberFormatException("variable '" + ctx.IDENTIFIER().getText() + "' line " + ctx.getStart()
                    .getLine() + " is undefined");
        }
        // TODO handle out of bounds. All expressions return types, so imposible to solve expression
        return super.visitArrayExpression(ctx); // TODO
    }

    @Override
    public Object visitVariableParameterExpression(cHawkParser.VariableParameterExpressionContext ctx) {
        return super.visitVariableParameterExpression(ctx);  // TODO handle named paramter
    }

    @Override
    public Object visitFunctionExpression(cHawkParser.FunctionExpressionContext ctx) {
        if (ctx.getParent().getClass().getSimpleName().equals("SystemFunctionExpressionContext")) {
            return "Void";
        }
        Symbol symbol = symbolTable.currentScope().resolve(ctx.IDENTIFIER().getText());
        if (symbol == null) {
            throw new NumberFormatException("function '" + ctx.IDENTIFIER().getText() + "'line " + ctx.getStart()
                    .getLine() + " is undefined");
        }
        return symbol.getType();
    }

    @Override
    public Object visitFunctionParameterExpression(cHawkParser.FunctionParameterExpressionContext ctx) {
        return super.visitFunctionParameterExpression(ctx); // TODO handle named paramter
    }

    @Override
    public Object visitParenthesisExpression(cHawkParser.ParenthesisExpressionContext ctx) {
        return visit(ctx.expression());
    }


    @Override
    public Object visitMultiplicativeExpression(cHawkParser.MultiplicativeExpressionContext ctx) {
        Object left = visit(ctx.mathematical_expression());
        Object right = visit(ctx.expression());
        if (left.equals("Integer") && right.equals("Integer"))
            return "Integer";
        if (left.equals("Float") && right.equals("Float"))
            return "Float";
        if (left.equals("Integer") && right.equals("Float"))
            return "Float";
        if (left.equals("Float") && right.equals("Integer"))
            return "Float";
        throw new NumberFormatException("Illegal datatype in expression line: " + ctx.getStart().getLine());
    }

    @Override
    public Object visitAdditiveExpression(cHawkParser.AdditiveExpressionContext ctx) {
        Object left = visit(ctx.mathematical_expression());
        Object right = visit(ctx.expression());
        if (left.equals("Integer") && right.equals("Integer"))
            return "Integer";
        if (left.equals("Float") && right.equals("Float"))
            return "Float";
        if (left.equals("Integer") && right.equals("Float"))
            return "Float";
        if (left.equals("Float") && right.equals("Integer"))
            return "Float";
        throw new NumberFormatException("Illegal datatype in expression line: " + ctx.getStart().getLine());
    }

    @Override
    public Object visitRelationalExpression(cHawkParser.RelationalExpressionContext ctx) {
        Object left = visit(ctx.logical_expression());
        Object right = visit(ctx.expression());
        if (!left.equals("Integer") && !left.equals("Float") && !right.equals("Integer") && !right.equals("Float")) {
            throw new NumberFormatException("the relational comparison line " + ctx.getStart().getLine() + " must be " +
                    "of the same type");
        }
        return "Boolean";
    }

    @Override
    public Object visitEqualityExpression(cHawkParser.EqualityExpressionContext ctx) {
        Object left = visit(ctx.logical_expression());
        Object right = visit(ctx.expression());
        if (!left.equals(right)) {
            throw new NumberFormatException("the equality comparison line " + ctx.getStart().getLine() + " must be of" +
                    " numeric values");
        }
        return "Boolean";
    }

    @Override
    public Object visitAndExpression(cHawkParser.AndExpressionContext ctx) {
        Object left = visit(ctx.logical_expression());
        Object right = visit(ctx.expression());
        if (!left.equals("Boolean") && !right.equals("Boolean")) {
            throw new NumberFormatException("the logical comparison line " + ctx.getStart().getLine() + " must be of " +
                    "the boolean");
        }
        return "Boolean";
    }

    @Override
    public Object visitOrExpression(cHawkParser.OrExpressionContext ctx) {
        Object left = visit(ctx.logical_expression());
        Object right = visit(ctx.expression());
        if (!left.equals("Boolean") && !right.equals("Boolean")) {
            throw new NumberFormatException("the logical comparison line " + ctx.getStart().getLine() + " must be of " +
                    "the boolean");
        }
        return "Boolean";
    }
    //endregion
}