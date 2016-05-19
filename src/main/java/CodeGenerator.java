class CodeGenerator extends cHawkBaseVisitor{
    @Override
    public String visitProgram(cHawkParser.ProgramContext ctx) {
        // return all code generated as string
        return "asd";
    }

    @Override
    public Object visitBody(cHawkParser.BodyContext ctx) {
        return super.visitBody(ctx);
    }

    @Override
    public Object visitStatement(cHawkParser.StatementContext ctx) {
        return super.visitStatement(ctx);
    }

    @Override
    public Object visitFunctionExpression(cHawkParser.FunctionExpressionContext ctx) {
        return super.visitFunctionExpression(ctx);
    }
}
