/* TODO fix errors caused from cfg changes
import SymbolTable.StoreValue;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class JBCodeGenerator extends cHawkBaseVisitor {
    private Map<String, StoreValue> variableMap = new LinkedHashMap<String, StoreValue>();
    private int label = -1;
    private int store = 0;
    private String functions = "";

    @Override
    public Object visit(ParseTree tree) {
        if (tree == null) {
            return "";
        }
        return super.visit(tree);
    }

    @Override
    public Object visitProgram(cHawkParser.ProgramContext ctx) {
        visitChildren(ctx);
//        String line = "";
//        line += ".class public cHawk\r\n";
//        line += ".super java/lang/Object\r\n";
//        line += ".method public <init>()V\r\n";
//        line += "aload_0\r\n" +
//                "invokenonvirtual ";
//        line += "java/lang/Object/<init>()V\r\n";
//        line += "return\r\n";
//        line += ".end method\r\n";
//        line += functions;
//        line += ".method public static main([Ljava/lang/String;)V\r\n" +
//                ".limit stack 400\r\n" +
//                ".limit locals 200\r\n";
//        for (int i = 0; i < ctx.getChildCount(); i++) {
//            Object child = visit(ctx.getChild(i));
//            if (child != null)
//                line += child;
//        }
//        line += "return\r\n.end method";
//        try {
//            File file = new File("output\\cHawk.j");
//            file.getParentFile().mkdir();
//            file.createNewFile();
//            FileWriter fw = new FileWriter(file);
//            fw.write(line);
//            fw.flush();
//            fw.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return null;
    }

    @Override
    public Object visitSetup(cHawkParser.SetupContext ctx) {
        String line = "";
        line += "invokestatic cHawk.setup()V\r\n";
        functions += ".method public static setup()V\r\n";
        functions += ".limit stack 20\r\n.limit locals 20\r\n";
        functions += visit(ctx.body());
        functions += "return" +
                "\r\n.end method\r\n";

        return line;
    }

    @Override
    public Object visitRoute(cHawkParser.RouteContext ctx) {
        String line = "";
        int gotolabel = label + 1;
        line += "invokestatic cHawk.route()V\r\n";
        functions += ".method public static route()V\r\n";
        functions += ".limit stack 20\r\n.limit locals 20\r\n";
        functions += "Label" + labelinc() + ":\r\n";
        functions += visit(ctx.body());
        functions += "goto " + "Label" + gotolabel + "\r\n";
        functions += "return" +
                "\r\n.end method\r\n";

        return line;
    }

    @Override
    public Object visitBody(cHawkParser.BodyContext ctx) {
        String line = "";
        for (int i = 0; i < ctx.getChildCount(); i++) {
            Object child = visit(ctx.getChild(i));
            if (child != null) line += child;
        }
        return line;
    }

    @Override
    public Object visitStatement(cHawkParser.StatementContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public Object visitIfStatement(cHawkParser.IfStatementContext ctx) {
        String line = "";
        line += visit(ctx.expression());

        line += visit(ctx.body());
        line += "Label" + label + ":\r\n";
        return line;
    }

    @Override
    public Object visitWhileStatement(cHawkParser.WhileStatementContext ctx) {
        String line = "";
        Integer gotolabel = label + 1;

        line += visit(ctx.expression());
        line += visit(ctx.body());
        line += "goto Label" + gotolabel + "\r\n";
        line += "Label" + label + ":\r\n";

        return line;
    }

    @Override
    public Object visitFunctionExpression(cHawkParser.FunctionExpressionContext ctx) {
        String line = "";
        String temp;

        if (ctx.getChild(0).getText().equals("print")) {
            line += "getstatic java/lang/System/out Ljava/io/PrintStream;\r\n";
            line += "ldc \"";
            for (int i = 2; i < ctx.getChild(2).getChild(0).getChildCount(); i++) {
                temp = ctx.getChild(2).getChild(0).getChild(i).getText();
                line += temp.replace("\"", "");
            }
            line += "\"\ninvokevirtual java/io/PrintStream/println(Ljava/lang/String;)V\r\n";
        }
        return line;
    }

    @Override
    public Object visitRelationalExpression(cHawkParser.RelationalExpressionContext ctx) {
        String line = "";
        StoreValue variable1 = variableMap.get(ctx.expression(0).getText());
        StoreValue variable2 = variableMap.get(ctx.expression(1).getText());
        String type1 = (variable1 != null) ? variable1.getType() : visit(ctx.expression(0)).getClass().getSimpleName();
        String type2 = (variable2 != null) ? variable2.getType() : visit(ctx.expression(1)).getClass().getSimpleName();

        line += "Label" + labelinc() + ":" + "\r\n";

        if ((type1.contains("Integer") && type2.contains("Integer")) || (type1.contains("Float") && type2.contains
                ("Float"))) {
            if (ctx.expression(0).getClass().getSimpleName().contains("Variable_expression_Context")) {
                StoreValue variable = variableMap.get(ctx.expression(0).getText());
                line += variableSwitch(variable);
            }
            if (ctx.expression(0).getClass().getSimpleName().contains("ValueExpressionContext")) {
                String type = visit(ctx.expression(0)).getClass().getSimpleName();
                line += constantSwitch(type, ctx.expression(0).getText());
            }
            if (ctx.expression(1).getClass().getSimpleName().contains("Variable_expression_Context")) {
                StoreValue variable = variableMap.get(ctx.expression(1).getText());
                line += variableSwitch(variable);
            }
            if (ctx.expression(1).getClass().getSimpleName().contains("ValueExpressionContext")) {
                String type = visit(ctx.expression(1)).getClass().getSimpleName();
                line += constantSwitch(type, ctx.expression(1).getText());
            }
        } else throw new ArithmeticException("Both numbers must be of same type");
        if (type1.equals("Integer")) {
            switch (ctx.getChild(1).getText()) {
                case "<":
                    line += "if_icmpge Label" + labelinc() + "\r\n";
                    break;
                case ">":
                    line += "if_icmple Label" + labelinc() + "\r\n";
                    break;
                case "<=":
                    line += "if_icmpgt Label" + labelinc() + "\r\n";
                    break;
                case ">=":
                    line += "if_icmplt Label" + labelinc() + "\r\n";
                default:
                    throw new ArithmeticException("< <= >= >  comparisons are only allowed");
            }
        } else {
            switch (ctx.getChild(1).getText()) {
                case "<":
                    line += "fcmpg\r\nifge Label" + labelinc() + "\r\n";
                    break;
                case ">":
                    line += "fcmpl\r\nifle Label" + labelinc() + "\r\n";
                    break;
                case "<=":
                    line += "fcmpg\r\nifgt Label" + labelinc() + "\r\n";
                    break;
                case ">=":
                    line += "fcmpl\r\niflt Label" + labelinc() + "\r\n";
                default:
                    throw new ArithmeticException("< <= >= >  comparisons are only allowed");
            }
        }

        return line;
    }

    private String constantSwitch(String type, String value) {
        switch (type) {
            case "Integer":
                return "bipush " + value + "\r\n";
            case "Float":
                return "ldc " + value + "\r\n";
            default:
                return null;
        }
    }

    private String variableSwitch(StoreValue variable) {
        switch (variable.getType()) {
            case "Integer":
                return "iload " + variable.getLocation() + "\r\n";
            case "Float":
                return "fload " + variable.getLocation() + "\r\n";
            case "String":
                return "aload " + variable.getLocation() + "\r\n";
            default:
                return null;
        }
    }

    @Override
    public Object visitVariableStatement(cHawkParser.VariableStatementContext ctx) {
        String type = visit(ctx.getChild(2)).getClass().getSimpleName();
        String name = ctx.getChild(0).getText();
        String line = "";
        switch (type) {
            case "Integer":
                line += "bipush " + visit(ctx.getChild(2)) + "\r\n" +
                        "istore " + storeinc() + "\r\n";
                break;
            case "Float":
                line += "ldc " + visit(ctx.getChild(2)) + "\r\n" +
                        "fstore " + storeinc() + "\r\n";
                break;
            case "String":
                line += "ldc " + visit(ctx.getChild(2)) + "\r\n" +
                        "astore " + storeinc() + "\r\n";
        }
        StoreValue variable = new StoreValue(name, type, store);
        variableMap.put(name, variable);
        return line;
    }

    @Override
    public Object visitValueExpression(cHawkParser.ValueExpressionContext ctx) {
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

    private int labelinc() {
        label++;
        return label;
    }

    private int storeinc() {
        store++;
        return store;
    }
}*/