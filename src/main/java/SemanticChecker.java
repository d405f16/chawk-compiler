import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;

import java.util.ArrayList;

public class SemanticChecker extends chawkBaseVisitor {
    @Override
    public Object visit(ParseTree tree) {
        if (tree == null) {
            return "";
        }
        return super.visit(tree);
    }

    @Override
    public ArrayList<Object> visitChildren(RuleNode node) {
        ArrayList<Object> result = new ArrayList<Object>();
        for (int i = 0; i < node.getChildCount(); i++) {
            result.add(visit(node.getChild(i)));
        }
        return result;
    }

    @Override
    public Object visitSetup(chawkParser.SetupContext ctx) {
        checkForReturnStatement(visitChildren(ctx));
        return null;
    }

    @Override
    public Object visitRoute(chawkParser.RouteContext ctx) {
        checkForReturnStatement(visitChildren(ctx));
        return null;
    }

    @Override
    public Object visitReturn_statement(chawkParser.Return_statementContext ctx) {
        return ctx;
    }

    private void checkForReturnStatement(ArrayList array) {
        for (Object element : array) {
            if (element instanceof ArrayList) {
                checkForReturnStatement((ArrayList) element);
            } else {
                if (visit((ParseTree) element) instanceof chawkParser.Return_statementContext) {
                    throw new NumberFormatException("Return statement not allowed in setup or route");
                }
            }
        }
    }
}



