import Exceptions.DeclarationException;
import SymbolTable.Symbol;
import SymbolTable.SymbolTable;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.IntArrayData;
import com.sun.xml.internal.bind.v2.schemagen.episode.Klass;
import com.sun.xml.internal.fastinfoset.algorithm.BooleanEncodingAlgorithm;
import org.antlr.v4.runtime.misc.IntegerList;
import org.antlr.v4.runtime.tree.TerminalNode;


import java.lang.reflect.Type;
import java.util.Arrays;
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

    @Override
    public Object visitMathematicalExpression(chawkParser.MathematicalExpressionContext ctx) {
        Object left = visit(ctx.left);
        Object right = visit(ctx.right);
        if (left instanceof Integer && right instanceof Integer) return (Integer)left + (Integer)right;
        return null;
////region Description
//        List<String> acceptedDatatypes = Arrays.asList(
//                "Integer",
//                "Float",
//                "MathematicalExpressionContext"
//        );
//
//        if (acceptedDatatypes.contains(left.getClass().getSimpleName())
//                && acceptedDatatypes.contains(right.getClass().getSimpleName())) {
//            return ctx;
//        }
//        else if(left.getClass().getSimpleName().contains("VariableExpressionContext") &&
//                right.getClass().getSimpleName().contains("VariableExpressionContext")){
//            if (symbolTable.currentScope().resolve(ctx.left.getText()) != null &&
//                    symbolTable.currentScope().resolve(ctx.right.getText()) != null) {
//                if(symbolTable.currentScope().resolve(ctx.left.getText()).getType().contains("MathematicalExpressionContext") &&
//                        symbolTable.currentScope().resolve(ctx.right.getText()).getType().contains("MathematicalExpressionContext")){
//                    return ctx;
//                }
//                throw new DeclarationException("The declared variables must be numbers");
//            }
//            else throw new DeclarationException("Variables must be declared before use");
//        }
//        else if(left.getClass().getSimpleName().contains("VariableExpressionContext") || right.getClass().getSimpleName().contains("VariableExpressionContext")){
//            if (symbolTable.currentScope().resolve(ctx.left.getText()) != null)
//            {
//                if(symbolTable.currentScope().resolve(ctx.left.getText()).getType().contains("MathematicalExpressionContext")){
//                    return ctx;
//                }
//            }
//            else if(symbolTable.currentScope().resolve(ctx.right.getText()) != null) {
//                if(symbolTable.currentScope().resolve(ctx.right.getText()).getType().contains("MathematicalExpressionContext")){
//                    return ctx;
//                }
//                throw new DeclarationException("The declared variables must be numbers");
//            }
//            else throw new DeclarationException("Variables must be declared before use");
//        }
//        throw new NumberFormatException("The operands must be numbers");
//        //endregion
    }



    @Override
    public Object visitLogicalExpression(chawkParser.LogicalExpressionContext ctx) {
        Object left = visit(ctx.left);
        Object right = visit(ctx.right);
return null;
    }



    // TODO kontrolstrukture

    @Override
    public Object visitValueExpression(chawkParser.ValueExpressionContext ctx) {
        Object type = null;
        String value = ctx.getText();

        if (value.equals("true")) {
            type = true;
        } else if (type.equals("false")) {
            type = false;
        } else {
            try {
                type = Integer.parseInt(value);
            } catch (NumberFormatException e) {
                try {
                    type = Float.parseFloat(value);
                } catch (NumberFormatException f) {
                    type = value;
                }
            }
        }
        return type;
    }

    @Override
    public Object visitId(chawkParser.IdContext ctx) {
        return symbolTable.currentScope().resolve(ctx.id.getText()).getType();
    }

    @Override
    public Object visitVarDcl(chawkParser.VarDclContext ctx) {
        Object type = visit(ctx.expr);
        Symbol s = new Symbol(ctx.id.getText(), type);
        symbolTable.currentScope().define(s);
        /*//region OldCode
        if (ctx.expr != null){
            visit(ctx.expr);
            if(ctx.expr.getClass().getSimpleName() == "VariableExpressionContext"){
                Symbol symbol = new Symbol(ctx.id.getText(),symbolTable.currentScope().resolve(ctx.getText()).getType());
                symbolTable.currentScope().define(symbol);
            }
            else{
                Symbol symbol = new Symbol(ctx.id.getText(),ctx.expr.getClass().getSimpleName());
                symbolTable.currentScope().define(symbol);
            }

        }
        return ctx;
        //endregion*/
        return null;
    }

    @Override
public Object visitVariableExpression(chawkParser.VariableExpressionContext ctx) {
    return ctx;
}
//    @Override
//    public Object visitVariable_expression(chawkParser.Variable_expressionContext ctx) {
//        visitChildren(ctx);
//        System.out.println("varaible expression");
//        System.out.println("ctx.id.getText()");
//         get variable from hashTable and return
//        return null;
//    }
}
