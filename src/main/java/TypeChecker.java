import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.Arrays;
import java.util.List;

class TypeChecker extends chawkBaseVisitor {
    @Override
    public Object visitMathematicalExpression(chawkParser.MathematicalExpressionContext ctx) {
        Object left = visit(ctx.left);
        Object right = visit(ctx.right);

        List<String> acceptedDatatypes = Arrays.asList(
                "Integer",
                "Float",
                "MathematicalExpressionContext",
                "Variable_expressionContext"
        );

        if (acceptedDatatypes.contains(left.getClass().getSimpleName())
                && acceptedDatatypes.contains(right.getClass().getSimpleName())) {
            return ctx;
        }

        // TODO handle variables

        throw new NumberFormatException("The operands must be numbers");
    }

    @Override
    public Object visitLogicalExpression(chawkParser.LogicalExpressionContext ctx) {
        Object left = visit(ctx.left);
        Object right = visit(ctx.right);

        System.out.println("left: " + left.getClass().getSimpleName());
        System.out.println("right: " + right.getClass().getSimpleName());
        System.out.println("--------------------------");

        if (left instanceof Number) {
            throw new NumberFormatException("The left operand cannot be boolean while the left is integer");
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

//    @Override
//    public Object visitVariable_expression(chawkParser.Variable_expressionContext ctx) {
//        visitChildren(ctx);
//        System.out.println("varaible expression");
//        System.out.println("ctx.id.getText()");
//         get variable from hashTable and return
//        return null;
//    }
}
