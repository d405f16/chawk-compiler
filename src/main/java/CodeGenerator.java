import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

class CodeGenerator extends chawkBaseVisitor {
    //region Overrides
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
    //endregion

    //region Base
    @Override
    public Object visitProgram(chawkParser.ProgramContext ctx) {
        try {
            File file = new File("output\\program.js");
            file.getParentFile().mkdir();
            file.createNewFile();
            FileWriter fw = new FileWriter(file);
            fw.write(visitChildren(ctx));
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Object visitSetup(chawkParser.SetupContext ctx) {
        return "function setup(){" + visit(ctx.body()) + "}";
    }

    @Override
    public Object visitRoute(chawkParser.RouteContext ctx) {
        return "function route(){" + visit(ctx.body()) + "}";
    }
    //endregion

    //region Statements
    @Override
    public Object visitVariableStatement(chawkParser.VariableStatementContext ctx) {
        if (ctx.getParent().getClass().getSimpleName().equals("Named_parameterContext")) {
            return parseID(ctx.IDENTIFIER()) + ":" + visit(ctx.expression());
        }
        return "var " + parseID(ctx.IDENTIFIER()) + "=" + visit(ctx.expression()) + ";";
    }

    @Override
    public Object visitArrayStatement(chawkParser.ArrayStatementContext ctx) {
        String list = "";
        Integer count = 0;
        for (chawkParser.ExpressionContext expr : ctx.expression()) {
            if (count == 0) {
                list += expr.getText();
            } else {
                list += "," + expr.getText();
            }
            count++;
        }
        return "var " + parseID(ctx.IDENTIFIER()) + "=[" + list + "];";
    }

    @Override
    public Object visitFunctionStatement(chawkParser.FunctionStatementContext ctx) {
        return "function " + parseID(ctx.IDENTIFIER()) + "(params){" + visit(ctx.body()) + "}";
    }

    @Override
    public Object visitIfStatement(chawkParser.IfStatementContext ctx) {
        return "if(" + visit(ctx.expression()) + "){" + visit(ctx.body()) + "}";
    }

    @Override
    public Object visitIfElseStatement(chawkParser.IfElseStatementContext ctx) {
        return "if(" + visit(ctx.expression()) + "){" + visit(ctx.body(0)) + "}else{" + visit(ctx.body(1)) + "}";
    }

    @Override
    public Object visitForStatement(chawkParser.ForStatementContext ctx) {
        String id = parseID(ctx.IDENTIFIER());
        return "for(var " + id + "=" + visit(ctx.expression(0)) + ";" +
                id + "<" + visit(ctx.expression(1)) + ";" +
                id + "+=" + visit(ctx.expression(2)) + "){" + visit(ctx.body()) + "}";
    }

    @Override
    public Object visitWhileStatement(chawkParser.WhileStatementContext ctx) {
        return "while(" + visit(ctx.expression()) + "){" + visit(ctx.body()) + "}";
    }

    @Override
    public Object visitReturn_statement(chawkParser.Return_statementContext ctx) {
        return "return" + visit(ctx.expression()) + ";";
    }
    //endregion

    //region Expression
    @Override
    public Object visitValueExpression(chawkParser.ValueExpressionContext ctx) {
        return ctx.getText();
    }

    @Override
    public Object visitParenthesisExpression(chawkParser.ParenthesisExpressionContext ctx) {
        return "(" + visit(ctx.expression()) + ")";
    }

    @Override
    public Object visitVariableExpression(chawkParser.VariableExpressionContext ctx) {
        return parseID(ctx.IDENTIFIER());
    }

    @Override
    public Object visitArrayExpression(chawkParser.ArrayExpressionContext ctx) {
        return parseID(ctx.IDENTIFIER()) + "[" + visit(ctx.expression()) + "]";
    }

    @Override
    public Object visitFunctionExpression(chawkParser.FunctionExpressionContext ctx) {
        String parameters = "";
        Integer count = 0;
        for (chawkParser.Named_parameterContext param : ctx.named_parameter()) {
            if (count == 0) {
                parameters += visit(param);
            } else {
                parameters += "," + visit(param);
            }
            count++;
        }
        return parseID(ctx.IDENTIFIER()) + "({" + parameters + "})";
    }

    @Override
    public Object visitMathematicalExpression(chawkParser.MathematicalExpressionContext ctx) {
        return visit(ctx.expression(0)) + ctx.op.getText() + visit(ctx.expression(1));
    }

    @Override
    public Object visitRelationalExpression(chawkParser.RelationalExpressionContext ctx) {
        return visit(ctx.expression(0)) + ctx.op.getText() + visit(ctx.expression(1));
    }

    @Override
    public Object visitEqualityExpression(chawkParser.EqualityExpressionContext ctx) {
        return visit(ctx.expression(0)) + ctx.op.getText() + visit(ctx.expression(1));
    }

    @Override
    public Object visitLogicalExpression(chawkParser.LogicalExpressionContext ctx) {
        return visit(ctx.expression(0)) + ctx.op.getText() + visit(ctx.expression(1));
    }
    //endregion

    //region Helpers
    private String parseID(TerminalNode identifier) {
        return identifier.getText().replace('.', '_');
    }
    //endregion
}