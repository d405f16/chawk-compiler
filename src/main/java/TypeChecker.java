public class TypeChecker extends chawkBaseVisitor {
    @Override
    public Object visitMathematicalExpression(chawkParser.MathematicalExpressionContext ctx) {
        Object left = visit(ctx.left);
        Object right = visit(ctx.right);

        if(left instanceof Number && right instanceof Number) {
            return ctx;
        }

        // TODO handle variables

        throw new NumberFormatException("The two operands must be numbers");
    }

    @Override
    public Object visitValueExpression(chawkParser.ValueExpressionContext ctx) {
        try {
            return Integer.parseInt(ctx.getText());
        } catch (NumberFormatException e) {
            try {
                return Float.parseFloat(ctx.getText());
            } catch (NumberFormatException f) {
                return ctx.getText();
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
