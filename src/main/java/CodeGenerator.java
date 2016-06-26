import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;

import java.util.ArrayList;

class CodeGenerator extends cHawkBaseVisitor {
    @Override
    public Object visit(ParseTree tree) {
        if (tree == null) {
            return "";
        }
        return super.visit(tree);
    }

    @Override
    public String visitChildren(RuleNode node) {
        String result = "";
        for (int i = 0; i < node.getChildCount(); i++) {
            result += visit(node.getChild(i));
        }
        return result;
    }

    //region Base
    @Override
    public Object visitProgram(cHawkParser.ProgramContext ctx) {
        return "module.exports = function (drone) {" + visitChildren(ctx) + "};";
    }

    @Override
    public Object visitBody(cHawkParser.BodyContext ctx) {
        String result = "";
        String scope = "";
        int scopeCount = 0;
        for (int i = 0; i < ctx.getChildCount(); i++) {
            ParseTree child = ctx.getChild(i);
            if (child.getClass().getSimpleName().equals("SystemFunctionExpressionContext")
                    || child.getClass().getSimpleName().equals("FunctionExpressionContext")) {
                scope += visit(child);
                result += scope;
                scope = "";
                scopeCount++;
            } else {
                scope += visit(child);
            }
        }

        String parentContext = ctx.getParent().getClass().getSimpleName();

        if (scopeCount > 0) {
            if (parentContext.equals("RouteContext")) {
                scope += "route();";
            }
            if (parentContext.equals("FunctionStatementContext"))
                scope += "callback();";
        }

        for (int i = 0; i < scopeCount; i++) {
            scope += "});";
        }
        return result + scope;
    }

    @Override
    public String visitSetup(cHawkParser.SetupContext ctx) {
        return "function setup() {" + visit(ctx.body()) + "}; this.setup = setup;";
    }

    @Override
    public String visitRoute(cHawkParser.RouteContext ctx) {
        return "function route() {" + visit(ctx.body()) + "}; this.route = route;";
    }
    //endregion

    //region Statements
    @Override
    public String visitVariableStatement(cHawkParser.VariableStatementContext ctx) {
        if (ctx.getParent().getClass().getSimpleName().equals("NamedVariableParameterContext")) {
            return ctx.IDENTIFIER() + ":" + visit(ctx.expression());
        }
        return "var " + ctx.IDENTIFIER() + " = " + visit(ctx.expression()) + ";";
    }

    @Override
    public Object visitArrayStatement(cHawkParser.ArrayStatementContext ctx) {
        ArrayList<String> array = new ArrayList<>();
        for (cHawkParser.ExpressionContext expr : ctx.expression()) {
            array.add((String) visit(expr));
        }
        if (ctx.getParent().getClass().getSimpleName().equals("NamedVariableParameterContext")) {
            return ctx.IDENTIFIER() + ":[" + String.join(",", array) + "]";
        }
        return "var " + ctx.IDENTIFIER() + " = [" + String.join(",", array) + "];";
    }

    @Override
    public Object visitFunctionStatement(cHawkParser.FunctionStatementContext ctx) {
        return "function " + ctx.IDENTIFIER() + "(param, callback){" + visit(ctx.body()) + visit(ctx.return_statement()) + "}";
    }

    @Override
    public Object visitIfStatement(cHawkParser.IfStatementContext ctx) {
        return "if(" + visit(ctx.expression()) + "){" + visit(ctx.body()) + "}";
    }

    @Override
    public Object visitIfElseStatement(cHawkParser.IfElseStatementContext ctx) {
        return "if(" + visit(ctx.expression()) + "){" + visit(ctx.body(0)) + "}else{" + visit(ctx.body(1)) + "}";
    }

    @Override
    public Object visitForStatement(cHawkParser.ForStatementContext ctx) {
        Object id = ctx.IDENTIFIER();
        return "for(var " + id + "=" + visit(ctx.expression(0)) + ";" +
                id + "<" + visit(ctx.expression(1)) + ";" +
                id + "+=" + visit(ctx.expression(2)) + "){" + visit(ctx.body()) + "}";
    }

    @Override
    public Object visitWhileStatement(cHawkParser.WhileStatementContext ctx) {
        return "while(" + visit(ctx.expression()) + "){" + visit(ctx.body()) + "}";
    }

    @Override
    public Object visitReturnStatement(cHawkParser.ReturnStatementContext ctx) {
        return "return" + visit(ctx.expression()) + ";";
    }

    @Override
    public Object visitPrintStatement(cHawkParser.PrintStatementContext ctx) {
        return "console.log(" + visit(ctx.expression()) + ");";
    }
    //endregion

    //region Expressions
    @Override
    public String visitValueExpression(cHawkParser.ValueExpressionContext ctx) {
        return ctx.getText();
    }

    @Override
    public String visitVariableExpression(cHawkParser.VariableExpressionContext ctx) {
        return ctx.IDENTIFIER().getText();
    }

    @Override
    public String visitArrayExpression(cHawkParser.ArrayExpressionContext ctx) {
        return ctx.IDENTIFIER() + "[" + visit(ctx.expression()) + "]";
    }

    @Override
    public String visitSystemVariableExpression(cHawkParser.SystemVariableExpressionContext ctx) {
        return "drone." + visit(ctx.variable_expression());
    }

    @Override
    public String visitVariableParameterExpression(cHawkParser.VariableParameterExpressionContext ctx) {
        return "param." + visit(ctx.variable_expression());
    }


    @Override
    public Object visitFunctionExpression(cHawkParser.FunctionExpressionContext ctx) {
        ArrayList<String> parameters = new ArrayList<>();
        for (cHawkParser.Named_parameterContext param : ctx.named_parameter()) {
            parameters.add(visit(param).toString());
        }
        return ctx.IDENTIFIER() + "({" + String.join(",", parameters) + "}, function() {";
    }

    @Override
    public Object visitSystemFunctionExpression(cHawkParser.SystemFunctionExpressionContext ctx) {
        return "drone." + visit(ctx.function_expression());
    }

    @Override
    public Object visitFunctionParameterExpression(cHawkParser.FunctionParameterExpressionContext ctx) {
        return "param." + visit(ctx.function_expression());
    }

    @Override
    public Object visitParenthesisExpression(cHawkParser.ParenthesisExpressionContext ctx) {
        return visit(ctx.expression());
    }

    @Override
    public Object visitMultiplicativeExpression(cHawkParser.MultiplicativeExpressionContext ctx) {
        return visit(ctx.mathematical_expression()) + ctx.op.getText() + visit(ctx.expression());
    }

    @Override
    public Object visitAdditiveExpression(cHawkParser.AdditiveExpressionContext ctx) {
        return visit(ctx.mathematical_expression()) + ctx.op.getText() + visit(ctx.expression());
    }

    @Override
    public Object visitRelationalExpression(cHawkParser.RelationalExpressionContext ctx) {
        return visit(ctx.logical_expression()) + ctx.op.getText() + visit(ctx.expression());
    }

    @Override
    public Object visitEqualityExpression(cHawkParser.EqualityExpressionContext ctx) {
        return visit(ctx.logical_expression()) + ctx.op.getText() + visit(ctx.expression());
    }

    @Override
    public Object visitAndExpression(cHawkParser.AndExpressionContext ctx) {
        return visit(ctx.logical_expression()) + ctx.op.getText() + visit(ctx.expression());
    }

    @Override
    public Object visitOrExpression(cHawkParser.OrExpressionContext ctx) {
        return visit(ctx.logical_expression()) + ctx.op.getText() + visit(ctx.expression());
    }
    //endregion
}