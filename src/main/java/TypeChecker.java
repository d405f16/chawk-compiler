import SymbolTable.Symbol;
import SymbolTable.SymbolTable;
import SymbolTable.Scope;
import org.omg.CORBA.INTERNAL;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

class TypeChecker extends chawkBaseVisitor {
    SymbolTable symbolTable = new SymbolTable();

    @Override
    public Object visitProgram(chawkParser.ProgramContext ctx) {
        System.out.println("TYPE CHECKING");
        System.out.println("-------------");
        visitChildren(ctx);
        return null;
    }

    @Override
    public Object visitSetup(chawkParser.SetupContext ctx) {
        symbolTable.pushScope();
        visitChildren(ctx);
        symbolTable.popScope();

        return null;
    }

    @Override
    public Object visitRoute(chawkParser.RouteContext ctx) {
        symbolTable.pushScope();
        visitChildren(ctx);
        symbolTable.popScope();

        return null;
    }

    //region Expressions
    @Override
    public Object visitMathematicalExpression(chawkParser.MathematicalExpressionContext ctx) {
        Object left = visit(ctx.left);
        Object right = visit(ctx.right);

        if (left instanceof Integer && right instanceof Integer) {
            return doIntMath((Integer) left, (Integer) right, ctx.op.getText().charAt(0));
        }

        if (left instanceof Float && right instanceof Float) {
            return doFloatMath((Float) left, (Float) right, ctx.op.getText().charAt(0));
        }

        if (left instanceof Integer && right instanceof Float) {
            return doFloatMath((Integer) left, (Float) right, ctx.op.getText().charAt(0));
        }

        if (left instanceof Float && right instanceof Integer) {
            return doFloatMath((Integer) right, (Float) left, ctx.op.getText().charAt(0));
        }

        throw new NumberFormatException("Illegal datatype in expression line: " + ctx.start.getLine());
    }

    @Override
    public Object visitLogicalExpression(chawkParser.LogicalExpressionContext ctx) {
        Object left = visit(ctx.left);
        Object right = visit(ctx.right);

        if (left.getClass().equals(right.getClass())){
            return true;
        }
        else {
            try {
                throw new Exception("Operator " + ctx.op.getText() + " cannot be applied to " +
                        left.getClass().getSimpleName() + " and " + right.getClass().getSimpleName());
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public Object visitRelationalExpression(chawkParser.RelationalExpressionContext ctx) {
        Object left = visit(ctx.left);
        Object right = visit(ctx.right);

        if (left instanceof Number && right instanceof Number)
            return true;
        else {
            try {
                throw new Exception("Operator " + ctx.op.getText() + " cannot be applied to " +
                        left.getClass().getSimpleName() + " and " + right.getClass().getSimpleName());
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public Object visitAndOrExpression(chawkParser.AndOrExpressionContext ctx) {
        // && ||
        Object left = visit(ctx.left);
        Object right = visit(ctx.right);
        System.out.println(left.getClass().getSimpleName());
        System.out.println(right.getClass().getSimpleName());
        if (left instanceof Boolean && right instanceof Boolean){
            return true;
        }
        else {
            try {
                throw new Exception("Operator " + ctx.op.getText() + " cannot be applied to " +
                        left.getClass().getSimpleName() + " and " + right.getClass().getSimpleName());
            } catch (Exception e){
                e.printStackTrace();
            }
        }
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

    @Override
    public Object visitId(chawkParser.IdContext ctx) {
        return symbolTable.currentScope().resolve(ctx.id.getText()).getType();
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

    //endregion

    //region Statements

    @Override
    public Object visitVarDcl(chawkParser.VarDclContext ctx) {
        Object type = visit(ctx.expr);
        Symbol s = new Symbol(ctx.id.getText(), type);
        symbolTable.currentScope().define(s);
        return null;
    }

    @Override
    public Object visitArrayDcl(chawkParser.ArrayDclContext ctx) {
        List<Object> types = new ArrayList<Object>();
        for (chawkParser.ExpressionContext expr : ctx.expression()) {
            types.add(visit(expr));
        }

        Symbol symbol = new Symbol(ctx.id.getText(), types);
        symbolTable.currentScope().define(symbol);

        return null;
    }

    @Override
    public Object visitFuncDcl(chawkParser.FuncDclContext ctx) {
        symbolTable.pushScope();
        visit(ctx.expr);

        // TODO if type == null skal typen af Symbol s være en void agtig type
        Object type = visit(ctx.return_statement());
        Symbol s = new Symbol(ctx.id.getText(), type);
        symbolTable.currentScope().define(s);

        symbolTable.popScope();
        return null;
    }

    @Override
    public Object visitReturn_statement(chawkParser.Return_statementContext ctx) {
        return visit(ctx.expr);
    }

    //endregion

    //region Math
    private Integer doIntMath(Integer i1, Integer i2, char op) {
        switch (op) {
            case '*':
                return i1 * i2;
            case '/':
                return i1 / i2;
            case '%':
                return i1 % i2;
            case '+':
                return i1 + i2;
            case '-':
                return i1 - i2;
            default:
                return null;
        }
    }

    private Float doFloatMath(Float f1, Float f2, char op) {
        switch (op) {
            case '*':
                return f1 * f2;
            case '/':
                return f1 / f2;
            case '%':
                return f1 % f2;
            case '+':
                return f1 + f2;
            case '-':
                return f1 - f2;
            default:
                return null;
        }
    }

    private Float doFloatMath(Integer i, Float f, char op) {
        switch (op) {
            case '*':
                return i * f;
            case '/':
                return i / f;
            case '%':
                return i % f;
            case '+':
                return i + f;
            case '-':
                return i - f;
            default:
                return null;
        }
    }
    //endregion
}
