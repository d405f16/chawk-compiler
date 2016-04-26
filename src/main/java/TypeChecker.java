import Exceptions.DeclarationException;
import SymbolTable.Symbol;
import SymbolTable.SymbolTable;
import com.sun.xml.internal.bind.v2.schemagen.episode.Klass;
import org.antlr.v4.runtime.tree.TerminalNode;


import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

class TypeChecker extends chawkBaseVisitor {
    SymbolTable symbolTable = new SymbolTable();;

    @Override
    public Object visitMathematicalExpression(chawkParser.MathematicalExpressionContext ctx){
        Object left = visit(ctx.left);
        Object right = visit(ctx.right);
        List<String> acceptedDatatypes = Arrays.asList(
                "Integer",
                "Float",
                "MathematicalExpressionContext"
        );

        if (acceptedDatatypes.contains(left.getClass().getSimpleName())
                && acceptedDatatypes.contains(right.getClass().getSimpleName())) {
            return ctx;
        }
        else if(left.getClass().getSimpleName().contains("VariableExpressionContext") &&
                right.getClass().getSimpleName().contains("VariableExpressionContext")){
            if (symbolTable.currentScope().resolve(ctx.left.getText()) != null &&
                    symbolTable.currentScope().resolve(ctx.right.getText()) != null) {
                if(symbolTable.currentScope().resolve(ctx.left.getText()).getType().contains("MathematicalExpressionContext") &&
                        symbolTable.currentScope().resolve(ctx.right.getText()).getType().contains("MathematicalExpressionContext")){
                    return ctx;
                }
                throw new DeclarationException("The declared variables must be numbers");
            }
            else throw new DeclarationException("Variables must be declared before use");
        }
        else if(left.getClass().getSimpleName().contains("VariableExpressionContext") || right.getClass().getSimpleName().contains("VariableExpressionContext")){
            if (symbolTable.currentScope().resolve(ctx.left.getText()) != null)
            {
                if(symbolTable.currentScope().resolve(ctx.left.getText()).getType().contains("MathematicalExpressionContext")){
                    return ctx;
                }
            }
            else if(symbolTable.currentScope().resolve(ctx.right.getText()) != null) {
                if(symbolTable.currentScope().resolve(ctx.right.getText()).getType().contains("MathematicalExpressionContext")){
                    return ctx;
                }
                throw new DeclarationException("The declared variables must be numbers");
            }
            else throw new DeclarationException("Variables must be declared before use");
        }
        throw new NumberFormatException("The operands must be numbers");
    }


    @Override
    public Object visitLogicalExpression(chawkParser.LogicalExpressionContext ctx) {
        Object left = visit(ctx.left);
        Object right = visit(ctx.right);

        //System.out.println("left: " + left.getClass().getSimpleName());
        //System.out.println("right: " + right.getClass().getSimpleName());
        //System.out.println("--------------------------");

        System.out.println(left.getClass().getSimpleName() + " " + right.getClass().getSimpleName());
        if (left instanceof Number && right instanceof Number) {
            return ctx;
        }
        if(left.getClass().getSimpleName()!= null && right.getClass().getSimpleName()!=null){
            if(left.getClass().getSimpleName().contains("VariableExpressionContext")
                    && right.getClass().getSimpleName().contains("VariableExpressionContext")){
                Symbol leftSymbol = symbolTable.currentScope().resolve(ctx.left.getText());
                Symbol rightSymbol = symbolTable.currentScope().resolve(ctx.right.getText());

                if(leftSymbol != null
                        && rightSymbol != null){
                    if(leftSymbol.getType().contains("Number") || leftSymbol.getType().contains("Expression") &&
                            rightSymbol.getType().contains("Number") || rightSymbol.getType().contains("Expression")){
                        return ctx;
                    }
                }
            }
            else if(left.getClass().getSimpleName().contains("VariableExpressionContext")){
                Symbol leftSymbol = symbolTable.currentScope().resolve(ctx.left.getText());
                if(leftSymbol != null){
                    if(leftSymbol.getType().contains("Number") || leftSymbol.getType().contains("Expression")){
                        return ctx;
                    }
                }
            }
            else if(right.getClass().getSimpleName().contains("VariableExpressionContext")){

            }
            else if(left.getClass().getSimpleName().contains("Expression") && right.getClass().getSimpleName().contains("Expression")) {
                System.out.println("hej");
                return ctx;
            }
        }
        if(left instanceof Number || right instanceof Number) {
            if (left.getClass().getSimpleName() != null) {
                if (left.getClass().getSimpleName().contains("Expression")) {
                    System.out.println("1hej");
                    return ctx;
                }
            }
            if (right.getClass().getSimpleName() != null) {
                if (right.getClass().getSimpleName().contains("Expression")) {
                    System.out.println("hej2");
                    return ctx;
                }
            }
        }

        // number number
        // boolean boolean
        // logical logical
        // math math
        // math value
        // value math
        throw new NumberFormatException("You failed");
    }



    // TODO kontrolstrukture

    @Override
    public Object visitValueExpression(chawkParser.ValueExpressionContext ctx) {
        String text = ctx.getText();

        if (text.equals("true")) {
            return true;
        } else if (text.equals("false")) {
            return false;
        } else {
            try {
                return Integer.parseInt(text);
            } catch (NumberFormatException e) {
                try {
                    return Float.parseFloat(text);
                } catch (NumberFormatException f) {
                    return text;
                }
            }
        }
    }

    @Override
    public Object visitVarDcl(chawkParser.VarDclContext ctx) {
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
