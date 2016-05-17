import org.antlr.v4.runtime.tree.ParseTree;
public class JBCodeGenerator extends chawkBaseVisitor {
    public int label=-1;
    @Override
    public Object visit(ParseTree tree) {
        if (tree == null) {
            return null;
        }
        return super.visit(tree);
    }

    @Override
    public Object visitProgram(chawkParser.ProgramContext ctx) {
        System.out.println("public class cHawk {");
        for (int i = 0; i < ctx.getChildCount(); i++) {
            Object child = visit(ctx.getChild(i));
            if (child != null)
                System.out.println(child);
        }
        System.out.println("}");
        return null;
    }

    @Override
    public Object visitWhileStatement(chawkParser.WhileStatementContext ctx) {

        return null;
    }

    @Override
    public Object visitValueExpression(chawkParser.ValueExpressionContext ctx) {
        String value = ctx.getText();

        if (value.equals("true") || value.equals("false")) {
            return Boolean.parseBoolean(value);
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
    public int labelinc() {
        label++;
        return label;
    }
    //
//    @Override
//    public Object visitArrayExpression(chawkParser.ArrayExpressionContext ctx) {
//        return ctx.IDENTIFIER()) + "[" + visit(ctx.expression()) + "]";
//    }
}
