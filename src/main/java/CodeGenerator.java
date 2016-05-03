import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class CodeGenerator extends chawkBaseVisitor {
    //this class is super fun

    List<String> code = new ArrayList<String>();

    @Override
    public Object visitProgram(chawkParser.ProgramContext ctx) {
        visitChildren(ctx);
        for (String oline : code) {
            System.out.println(oline);
        }

        return super.visitProgram(ctx);
    }

    @Override
    public Object visitVarDcl(chawkParser.VarDclContext ctx) {
        String line = "var " + ctx.id.getText() + "=" + ctx.expression().getText()+";";
        append(line);

        return super.visitVarDcl(ctx);
    }

    @Override
    public Object visitArrayDcl(chawkParser.ArrayDclContext ctx) {
        String line = "var " + ctx.getText()+";";
        append(line);

        return super.visitArrayDcl(ctx);
    }

    @Override
    public Object visitFuncDcl(chawkParser.FuncDclContext ctx) {
        String line = "";

        return super.visitFuncDcl(ctx);
    }

    private void append(String input) {
        code.add(input);
    }

    private void write(String code) {
//        List<String> lines = Arrays.asList("The first line", "The second line");
//        Path file = Paths.get("the-file-name.txt");
//        Files.write(file, lines, Charset.forName("UTF-8"));
    }


}
