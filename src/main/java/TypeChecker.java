import SymbolTable.Symbol;
import SymbolTable.SymbolTable;

import java.awt.font.NumericShaper;
import java.util.ArrayList;
import java.util.List;

class TypeChecker extends chawkBaseVisitor {
    SymbolTable symbolTable = new SymbolTable();

    @Override
    public Object visitProgram(chawkParser.ProgramContext ctx) {
        visitChildren(ctx);
        if (symbolTable.getScope(0).resolve("a").getType() instanceof Integer)
            System.out.println("Det var en Integer");
        return null;
    }


    //region Expressions
    @Override
    public Object visitMathematicalExpression(chawkParser.MathematicalExpressionContext ctx) {
        Object left = visit(ctx.left);
        Object right = visit(ctx.right);

        if (left instanceof Integer && right instanceof Integer) {
            return 1;
        }

        if (left instanceof Float && right instanceof Float) {
            return 1f;
        }

        // int + float

        throw new NumberFormatException("illegal datatype in expression line: " + ctx.start.getLine());
    }


    @Override
    public Object visitLogicalExpression(chawkParser.LogicalExpressionContext ctx) {
        Object left = visit(ctx.left);
        Object right = visit(ctx.right);
        return null;
    }

    @Override
    public Object visitParenthesisExpression(chawkParser.ParenthesisExpressionContext ctx) {
        return visit(ctx.expression());
    }

    @Override
    public Object visitValueExpression(chawkParser.ValueExpressionContext ctx) {
        String value = ctx.getText();

        if (value.equals("true")) {
            return true;
        } else if (value.equals("false")) {
            return false;
        } else {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                try {
                    return Float.parseFloat(value);
                } catch (NumberFormatException f) {
                    return value;
                }
            }
        }
    }
    //endregion


    @Override
    public Object visitId(chawkParser.IdContext ctx) {
        return symbolTable.currentScope().resolve(ctx.id.getText()).getType();
    }

    @Override
    public Object visitVarArrayDcl(chawkParser.VarArrayDclContext ctx) {
        List<Object> types = new ArrayList<Object>();
        for (chawkParser.ExpressionContext expr : ctx.expression()) {
            types.add(visit(expr));
        }

        Symbol symbol = new Symbol(ctx.id.getText(), types);
        symbolTable.currentScope().define(symbol);

        return null;
    }

    @Override
    public Object visitArray(chawkParser.ArrayContext ctx) {
        Symbol symbol = symbolTable.currentScope().resolve(ctx.id.getText());
        ArrayList types = (ArrayList) symbol.getType();

        Object expr = visit(ctx.expression());

        if (expr instanceof Integer) {
            return types.get((Integer) expr);
        }

        throw new NumberFormatException("Array index must be an integer");
    }


    @Override
    public Object visitVarDcl(chawkParser.VarDclContext ctx) {
        Object type = visit(ctx.expr);
        Symbol s = new Symbol(ctx.id.getText(), type);
        symbolTable.currentScope().define(s);
        return null;
    }
}
