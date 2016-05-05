import SymbolTable.SymbolTable;
import org.antlr.v4.runtime.ParserRuleContext;

import java.beans.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

class TypeChecker extends chawkBaseVisitor {
    SymbolTable symbolTable = new SymbolTable();

    @Override
    public Object visitProgram(chawkParser.ProgramContext ctx) {
        System.out.println("TYPE CHECKING");
        System.out.println("-------------");
        return visitChildren(ctx);
    }

    @Override
    public Object visitSetup(chawkParser.SetupContext ctx) {
        symbolTable.pushScope();
        Object children = visitChildren(ctx);
        symbolTable.popScope();
        return children;
    }

    @Override
    public Object visitRoute(chawkParser.RouteContext ctx) {
        symbolTable.pushScope();
        Object children = visitChildren(ctx);
        symbolTable.popScope();
        return children;
    }

    @Override
    public Object visitBody(chawkParser.BodyContext ctx) {
        for(chawkParser.Function_expressionContext fectx : ctx.function_expression()) {
            visit(fectx);
        }
        List<chawkParser.StatementContext> statements = ctx.statement();
        Object lastStatement = visit(statements.get(0));
        for (int i = 1; i < statements.size(); i++) {
            if (!lastStatement.equals(visit(statements.get(i)))) {
                throw new NumberFormatException("function has return statements of different types");
            }
            lastStatement = visit(statements.get(i));
        }
        return lastStatement;
    }

    //region Statements
    @Override
    public Object visitVariableStatement(chawkParser.VariableStatementContext ctx) {
        Object type = visit(ctx.expression());
        symbolTable.currentScope().define(ctx.IDENTIFIER().getText(), type.toString());
        return null;
    }

    @Override
    public Object visitArrayStatement(chawkParser.ArrayStatementContext ctx) {
        Object arrayType = visit(ctx.expression(0));
        for (chawkParser.ExpressionContext expression : ctx.expression()) {
            Object type = visit(expression);
            if (!type.equals(arrayType)) {
                throw new NumberFormatException("Elements in array are not all of same type");
            }
        }
        symbolTable.currentScope().define(ctx.IDENTIFIER().getText(), arrayType.toString());
        return null;
    }

    @Override
    public Object visitFunctionStatement(chawkParser.FunctionStatementContext ctx) {
        String type;
        chawkParser.BodyContext bodyContext = ctx.body();
        symbolTable.pushScope();
        if(bodyContext == null) {
            type = "Void";
        } else {
            Object body = visit(bodyContext);
            if(body == null) {
                type = "Void";
            } else {
                type = body.toString();
            }
        }
        symbolTable.popScope();
        symbolTable.currentScope().define(ctx.IDENTIFIER().getText(), type);
        return null;
    }

    @Override
    public Object visitReturn_statement(chawkParser.Return_statementContext ctx) {
        return visit(ctx.expression());
    }

    @Override
    public Object visitIfStatement(chawkParser.IfStatementContext ctx) {
        chawkParser.BodyContext body = ctx.body();
        if (body != null) {
            return visit(body);
        }
        return null;
    }

    @Override
    public Object visitIfElseStatement(chawkParser.IfElseStatementContext ctx) {
        chawkParser.BodyContext body = ctx.body(0);
        if (body != null) {
            return visit(body);
        }
        return null;
    }

    @Override
    public Object visitForStatement(chawkParser.ForStatementContext ctx) {
        chawkParser.BodyContext body = ctx.body();
        if (body != null) {
            return visit(body);
        }
        return null;
    }

    @Override
    public Object visitWhileStatement(chawkParser.WhileStatementContext ctx) {
        chawkParser.BodyContext body = ctx.body();
        if (body != null) {
            return visit(body);
        }
        return null;
    }
    //endregion

    //region Expressions
    @Override
    public Object visitValueExpression(chawkParser.ValueExpressionContext ctx) {
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
    public Object visitParenthesisExpression(chawkParser.ParenthesisExpressionContext ctx) {
        return visit(ctx.expression());
    }

    @Override
    public Object visitVariable_expression(chawkParser.Variable_expressionContext ctx) {
        //System.out.print(ctx.IDENTIFIER());

        return null;
    }

    @Override
    public Object visitFunction_expression(chawkParser.Function_expressionContext ctx) {
        return null;
    }

    @Override
    public Object visitMathematicalExpression(chawkParser.MathematicalExpressionContext ctx) {
        Object left = visit(ctx.expression(0));
        Object right = visit(ctx.expression(1));

        if (left.equals("Integer") && right.equals("Integer")) {
            return "Integer";
        }

        if (left.equals("Float") && right.equals("Float")) {
            return "Float";
        }

        if (left.equals("Integer") && right.equals("Float")) {
            return "Float";
        }

        if (left.equals("Float") && right.equals("Integer")) {
            return "Float";
        }

        throw new NumberFormatException("Illegal datatype in expression line: " + ctx.start.getLine());
    }

    @Override
    public Object visitRelationalExpression(chawkParser.RelationalExpressionContext ctx) {
        Object left = visit(ctx.expression(0));
        Object right = visit(ctx.expression(1));

        if (!left.equals("Integer") && !left.equals("Float") && !right.equals("Integer") && !right.equals("Float")) {
            throw new NumberFormatException("A relational comparison must be of the same type");
        }

        return "Boolean";
    }

    @Override
    public Object visitEqualityExpression(chawkParser.EqualityExpressionContext ctx) {
        Object left = visit(ctx.expression(0));
        Object right = visit(ctx.expression(1));

        if (!left.equals(right)) {
            throw new NumberFormatException("An equality comparison must be of numeric values");
        }

        return "Boolean";
    }

    @Override
    public Object visitLogicalExpression(chawkParser.LogicalExpressionContext ctx) {
        Object left = visit(ctx.expression(0));
        Object right = visit(ctx.expression(1));

        if (!left.equals("Boolean") && !right.equals("Boolean")) {
            throw new NumberFormatException("A logical comparison must be of the boolean");
        }

        return "Boolean";
    }
    //endregion

    //    @Override
//    public Object visitId(chawkParser.IdContext ctx) {
//        return symbolTable.currentScope().resolve(ctx.id.getText()).getType();
//    }
//
//    @Override
//    public Object visitArray(chawkParser.ArrayContext ctx) {
//        Symbol symbol = symbolTable.currentScope().resolve(ctx.id.getText());
//        ArrayList types = (ArrayList) symbol.getType();
//
//        Object expr = visit(ctx.expression());
//
//        if (expr instanceof Integer) {
//            return types.get((Integer) expr);
//        }
//
//        throw new NumberFormatException("Array index must be an integer");
//    }
//
//    //endregion
//
//    //region Statements
//


//
//    @Override
//    public Object visitFuncDcl(chawkParser.FuncDclContext ctx) {
//        symbolTable.pushScope();
//        visit(ctx.expr);
//
//        // TODO if type == null skal typen af Symbol s være en void agtig type
//        Object type = visit(ctx.return_statement());
//        Symbol s = new Symbol(ctx.id.getText(), type);
//        symbolTable.currentScope().define(s);
//
//        symbolTable.popScope();
//        return null;
//    }
//
//    @Override
//    public Object visitReturn_statement(chawkParser.Return_statementContext ctx) {
//        return visit(ctx.expr);
//    }
//
//    //endregion
//
//    //region Math
//    private Integer doIntMath(Integer i1, Integer i2, char op) {
//        switch (op) {
//            case '*':
//                return i1 * i2;
//            case '/':
//                return i1 / i2;
//            case '%':
//                return i1 % i2;
//            case '+':
//                return i1 + i2;
//            case '-':
//                return i1 - i2;
//            default:
//                return null;
//        }
//    }
//
//    private Float doFloatMath(Float f1, Float f2, char op) {
//        switch (op) {
//            case '*':
//                return f1 * f2;
//            case '/':
//                return f1 / f2;
//            case '%':
//                return f1 % f2;
//            case '+':
//                return f1 + f2;
//            case '-':
//                return f1 - f2;
//            default:
//                return null;
//        }
//    }
//
//    private Float doFloatMath(Integer i, Float f, char op) {
//        switch (op) {
//            case '*':
//                return i * f;
//            case '/':
//                return i / f;
//            case '%':
//                return i % f;
//            case '+':
//                return i + f;
//            case '-':
//                return i - f;
//            default:
//                return null;
//        }
//    }
//    //endregion


}
