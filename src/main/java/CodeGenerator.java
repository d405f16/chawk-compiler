import com.sun.deploy.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.jar.Attributes;

class CodeGenerator extends chawkBaseVisitor {

    @Override
    public Object visitProgram(chawkParser.ProgramContext ctx) {
        String line = "";
        for (int i = 0; i < ctx.getChildCount() - 1; i++) {
            line += visit(ctx.getChild(i));
        }
        System.out.println(line);
        write(line);
        return null;
    }

    @Override
    public Object visitBody(chawkParser.BodyContext ctx) {
        String line = "";
        for (int i = 0; i < ctx.getChildCount(); i++) {
            line += visit(ctx.getChild(i));
        }
        return line;
    }

    @Override
    public Object visitSetup(chawkParser.SetupContext ctx) {
        if (ctx.getChildCount() > 4) return "function setup(){" + visit(ctx.body()) + "}" + "setup();";
        else return "";
    }

    @Override
    public Object visitRoute(chawkParser.RouteContext ctx) {
        if (ctx.getChildCount() > 4) return "function route(){ while(true){" + visit(ctx.body()) + "}}" + "route();";
        else return "";
    }

    @Override
    public Object visitStatement(chawkParser.StatementContext ctx) {
        return visit(ctx.getChild(0)).toString();
    }

    //region variable_statement

    @Override
    public Object visitVariableStatement(chawkParser.VariableStatementContext ctx) {
        String line;
        String id = ctx.IDENTIFIER().getText().replace('.', '_');
        if (ctx.getParent().getClass().getSimpleName().contains("Named_parameterContext")) {
            line = id + " : " + visit(ctx.expression());
        } else {
            line = "var " + id + "=" + ctx.expression().getText() + ";";
        }

        return line;
    }

    @Override
    public Object visitArrayStatement(chawkParser.ArrayStatementContext ctx) {
        String array = "";
        Integer count = 0;
        for (chawkParser.ExpressionContext expression : ctx.expression()) {
            if (count == 0) {
                array += expression.getText();
            } else {
                array += "," + expression.getText();
            }
            count++;
        }
        return "var " + ctx.IDENTIFIER().getText().replace('.', '_') + "=[" + array + "];";
    }

    //endregion

    //region function_statement
    @Override
    public Object visitFunctionStatement(chawkParser.FunctionStatementContext ctx) {
        return "function " + ctx.IDENTIFIER().getText().replace('.', '_') + "(params)" + "{" + visit(ctx.body()).toString() + "}";
    }

    //endregion

    //region selection_statement
    @Override
    public Object visitIfStatement(chawkParser.IfStatementContext ctx) {
        return "if(" + visit(ctx.expression()) + "){" + visit(ctx.body()) + "}";
    }

    @Override
    public Object visitIfElseStatement(chawkParser.IfElseStatementContext ctx) {
        return "if(" + visit(ctx.expression()) + "){" + visit(ctx.body(0)) + "}else{" + visit(ctx.body(1)) + "}";
    }

    //endregion

    //region iteration_statement

    @Override
    public Object visitForStatement(chawkParser.ForStatementContext ctx) {
        return "for(var " + ctx.IDENTIFIER().getText() + "=" + visit(ctx.expression(0)) + ";" +
                ctx.IDENTIFIER().getText() + "<" + visit(ctx.expression(1)) + ";" +
                ctx.IDENTIFIER().getText() + "+=" + visit(ctx.expression(2)) + "){" + visit(ctx.body()) + "}";

    }

    @Override
    public Object visitWhileStatement(chawkParser.WhileStatementContext ctx) {
        return "while(" + visit(ctx.expression()) + "){" + visit(ctx.body()) + "}";
    }

    //endregion

    //region return_statement
    @Override
    public Object visitReturn_statement(chawkParser.Return_statementContext ctx) {
        return "return" + visit(ctx.expression()) + ";";
    }

    //endregion

    //region expression

    @Override
    public Object visitValueExpression(chawkParser.ValueExpressionContext ctx) {
        return ctx.getText();
    }

    @Override
    public Object visitParenthesisExpression(chawkParser.ParenthesisExpressionContext ctx) {
        return "(" + visit(ctx.expression()) + ")";
    }

    @Override
    public Object visitVariable_expression(chawkParser.Variable_expressionContext ctx) {
        return ctx.IDENTIFIER().getText().replace('.', '_');
    }

    @Override
    public Object visitFunction_expression(chawkParser.Function_expressionContext ctx) {
        String line = ctx.IDENTIFIER().getText().replace('.', '_') + "(";
        Integer i = 0;
        if (ctx.getChildCount() > 3) line += "{";
        for (chawkParser.Named_parameterContext param : ctx.named_parameter()) {
            if (i == 0) line += visit(param).toString();
            else line += " ," + visit(param).toString();
            i++;
        }
        if (ctx.getChildCount() > 3) line += "}";
        line += ")";
        return line;
    }

    @Override
    public Object visitMathematicalExpression(chawkParser.MathematicalExpressionContext ctx) {
        Object left = visit(ctx.expression(0));
        Object right = visit(ctx.expression(1));

        return left.toString() + ctx.op.getText() + right.toString();
    }

    @Override
    public Object visitRelationalExpression(chawkParser.RelationalExpressionContext ctx) {
        Object left = visit(ctx.expression(0));
        Object right = visit(ctx.expression(1));

        return left.toString() + ctx.op.getText() + right.toString();
    }

    @Override
    public Object visitEqualityExpression(chawkParser.EqualityExpressionContext ctx) {
        Object left = visit(ctx.expression(0));
        Object right = visit(ctx.expression(1));

        return left.toString() + ctx.op.getText() + right.toString();

    }

    @Override
    public Object visitLogicalExpression(chawkParser.LogicalExpressionContext ctx) {
        Object left = visit(ctx.expression(0));
        Object right = visit(ctx.expression(1));

        return left.toString() + ctx.getChild(1).getText() + right.toString();

    }

    //endregion

    //region named_parameter

    @Override
    public Object visitNamed_parameter(chawkParser.Named_parameterContext ctx) {
        return visit(ctx.getChild(0));
    }

    //endregion

    private void write(String line) {
        try {
            File file = new File("output\\program.js");
            file.getParentFile().mkdir();
            file.createNewFile();
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(line);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
