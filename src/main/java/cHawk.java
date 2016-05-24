import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class cHawk {
    public static void main(String[] args) throws IOException {
        Path input = null;
        Path output = null;
        String code = "";
        switch (args.length) {
            case 1:
                input = Paths.get(args[0]);
                output = Paths.get(removeExtension(input.toString()) + ".js");
                break;
            case 2:
                input = Paths.get(args[0]);
                output = Paths.get(args[1]);
                break;
            default:
                throw new IllegalArgumentException("Invalid arguments");
        }

        ParseTree tree = parseFile(input);

        System.out.println("Checking types...");
        new TypeChecker().visit(tree);
        System.out.println(output.toString());
        if(getExtension(output.toString()).equals("class")){
            System.out.println("Generating bytecode...");
            code = (String) new JBCodeGenerator().visit(tree);
        }
        else {
            System.out.println("Generating code...");
            code = (String) new CodeGenerator().visit(tree);
        }
            System.out.println("Writing file...");
            renderFile(output, code);
    }
    private static String getExtension(String filename){

        int i = filename.lastIndexOf('.');
        if (i > 0) {
            return filename.substring(i+1);
        }
        else throw new IllegalArgumentException("The filepath needs an extension");
    }

    private static String removeExtension(String filename) {
        if (filename == null) return null;
        int position = filename.lastIndexOf(".");
        if (position == -1) return filename;
        return filename.substring(0, position);
    }

    private static ParseTree parseFile(Path path) throws IOException {
        cHawkLexer lexer = new cHawkLexer(new ANTLRInputStream(Files.newInputStream(path)));
        cHawkParser parser = new cHawkParser(new CommonTokenStream(lexer));
        return parser.program();
    }

    private static void renderFile(Path path, String content) throws IOException {
        if (path.getParent() != null) Files.createDirectories(path.getParent());
        Files.write(path, content.getBytes());
    }
}