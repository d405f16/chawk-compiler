class PrettyPrinter extends chawkBaseVisitor {
    private Integer indentation = 0;

    @Override
    public Object visitProgram(chawkParser.ProgramContext ctx) {
        System.out.println("PRETTY PRINTING");
        System.out.println("---------------");
        visitChildren(ctx);
        return null;
    }

    @Override
    public Object visitSetup(chawkParser.SetupContext ctx) {
        System.out.println("setup = {");
        indentation += 2;
        if (ctx.statement_expression() != null) {
            ctx.statement_expression().accept(this);
        }
        indentation -= 2;
        System.out.println("}");

        return null;
    }

    @Override
    public Object visitRoute(chawkParser.RouteContext ctx) {
        System.out.println("route = {");
        indentation += 2;
        if (ctx.statement_expression() != null) {
            ctx.statement_expression().accept(this);
        }
        indentation -= 2;
        System.out.println("}");

        return null;
    }

    @Override
    public Object visitVarDcl(chawkParser.VarDclContext ctx) {
        System.out.print(indent() + ctx.id.getText() + " = ");
        ctx.expr.accept(this);

        if (ctx.parent.getClass() == chawkParser.StatementContext.class)  System.out.println("");

        return null;
    }

    @Override
    public Object visitVarDotDcl(chawkParser.VarDotDclContext ctx) {
        System.out.print(indent() + ctx.id1.getText() + "." + ctx.id2.getText() + " = ");
        ctx.expr.accept(this);

        if (ctx.parent.getClass() == chawkParser.StatementContext.class)  System.out.println("");

        return null;
    }

    @Override
    public Object visitVarArrayDcl(chawkParser.VarArrayDclContext ctx) {
        Integer size = ctx.expression().size();

        System.out.print(indent() + ctx.id.getText() + " = [");
        for (Integer i = 0; i < size; i++){
            ctx.expression(i).accept(this);
            if (size > 1 && i != size-1)
                System.out.print(", ");
        }
        System.out.print("]");

        if (ctx.parent.getClass() == chawkParser.StatementContext.class)  System.out.println("");

        return null;
    }

    @Override
    public Object visitFuncDcl(chawkParser.FuncDclContext ctx) {
        String begin = ctx.parent.getClass() == chawkParser.Named_parameterContext.class ?
                ctx.id.getText() + " = {" : indent() + ctx.id.getText() + " = {";
        String end = ctx.parent.getClass() == chawkParser.Named_parameterContext.class ?
                "}" : indent() + "}";

        System.out.println(begin);
        indentation += 2;
        ctx.statement_expression().accept(this);
        indentation -= 2;
        System.out.println(end);

        return null;
    }

    //region Statement
    @Override
    public Object visitFuncDotDcl(chawkParser.FuncDotDclContext ctx) {
        String begin =
                ctx.parent.getClass() == chawkParser.Named_parameterContext.class ?
                           ctx.id1.getText() + "." + ctx.id2.getText() + " = {" :   // if node is param do not indent
                indent() + ctx.id1.getText() + "." + ctx.id2.getText() + " = {";    // else indent
        String end =
                ctx.parent.getClass() == chawkParser.Named_parameterContext.class ?
                           "}" :                                                    // if node is param do not indent
                indent() + "}" ;                                                    // else indent

        System.out.println(begin);
        indentation += 2;
        ctx.statement_expression().accept(this);
        indentation -= 2;
        System.out.println(end);

        return null;
    }

//    @Override
//    public Object visitIfStmt(chawkParser.IfStmtContext ctx) {
//        System.out.print(indent() + "if (");
//        ctx.expression().accept(this);
//        System.out.println("){");
//        indentation += 2;
//        ctx.statement_expression().accept(this);
//        indentation -= 2;
//        System.out.println(indent() + "}");
//
//        return null;
//    }
//
//    @Override
//    public Object visitIfElseStmt(chawkParser.IfElseStmtContext ctx) {
//        // if
//        System.out.print(indent() + "if (");
//        ctx.expression().accept(this);
//        System.out.println("){");
//        indentation += 2;
//        ctx.statement_expression(0).accept(this);
//        indentation -= 2;
//        System.out.println(indent() + "}");
//        // else
//        System.out.print(indent() + "else {");
//        indentation += 2;
//        ctx.statement_expression(1).accept(this);
//        indentation -= 2;
//        System.out.println(indent() + "}");
//
//        return null;
//    }

    @Override
    public Object visitForSmt(chawkParser.ForSmtContext ctx) {
        System.out.print(indent() + "for (");
        ctx.variable_statement().accept(this);
        System.out.println(" to " + ctx.num1.getText() + " by " + ctx.num2.getText() + "){");
        indentation += 2;
        ctx.statement_expression().accept(this);
        indentation -= 2;
        System.out.println(indent() + "}");

        return null;
    }

    @Override
    public Object visitWhileSmt(chawkParser.WhileSmtContext ctx) {
        // 'while' '(' expression ')' '{' statement_expression '}'
        System.out.print(indent() + "while (");
        ctx.expression().accept(this);
        System.out.println("){");
        indentation += 2;
        ctx.statement_expression().accept(this);
        indentation -= 2;
        System.out.println(indent() + "}");

        return null;
    }
    //endregions

    //region Expression
    @Override
    public Object visitValueExpression(chawkParser.ValueExpressionContext ctx) {
        ctx.value().accept(this);
        return null;
    }

    @Override
    public Object visitParenthesisExpression(chawkParser.ParenthesisExpressionContext ctx) {
        System.out.print("(");
        ctx.expression().accept(this);
        System.out.print(")");
        return null;
    }

    @Override
    public Object visitMathematicalExpression(chawkParser.MathematicalExpressionContext ctx) {
        ctx.left.accept(this);
        System.out.print(ctx.op.getText());
        ctx.right.accept(this);
        return null;
    }

    @Override
    public Object visitLogicalExpression(chawkParser.LogicalExpressionContext ctx) {
        ctx.left.accept(this);
        System.out.print(ctx.op.getText());
        ctx.right.accept(this);
        return null;
    }

    @Override
    public Object visitFunctionExpression(chawkParser.FunctionExpressionContext ctx) {
        ctx.function_expression().accept(this);
        return null;
    }

    @Override
    public Object visitVariableExpression(chawkParser.VariableExpressionContext ctx) {
        ctx.variable_expression().accept(this);
        return null;
    }

    @Override
    public Object visitId(chawkParser.IdContext ctx) {
        System.out.print(ctx.id.getText());

        return null;
    }

    @Override
    public Object visitDotId(chawkParser.DotIdContext ctx) {
        System.out.print(ctx.id.getText() + ".");
        ctx.variable_expression().accept(this);

        return null;
    }

    @Override
    public Object visitArray(chawkParser.ArrayContext ctx) {
        System.out.print(ctx.id.getText() + "[");
        ctx.expression().accept(this);
        System.out.print("]");

        return null;
    }

    @Override
    public Object visitFuncCall(chawkParser.FuncCallContext ctx) {
        Integer size = ctx.named_parameter().size();

        System.out.print(ctx.id.getText() + "(");

        for (Integer i = 0; i < size; i++){
            ctx.named_parameter(i).accept(this);
            if (size > 1 && i != size-1)
                System.out.print(", ");
        }

        System.out.print(")");

        if (ctx.parent.parent.getClass() != chawkParser.VarDclContext.class &&
            ctx.parent.parent.getClass() != chawkParser.VarDotDclContext.class) System.out.println("");

        return null;
    }

    @Override
    public Object visitFuncDotCall(chawkParser.FuncDotCallContext ctx) {
        System.out.print(ctx.id.getText() + ".");
        ctx.function_expression().accept(this);

        return null;
    }

    @Override
    public Object visitNamed_parameter(chawkParser.Named_parameterContext ctx) {
        Integer tmp = indentation;
        indentation = 0;
        visitChildren(ctx);
        indentation = tmp;
        return null;
    }
    //endregions

    @Override
    public Object visitValue(chawkParser.ValueContext ctx) {
        System.out.print(ctx.getText());
        return null;
    }

    private String indent(){
        String str = "";
        for (Integer i = 0; i < indentation; i++){
            str += (" ");
        }
        return str;
    }
}
