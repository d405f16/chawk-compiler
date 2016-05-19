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

        switch (args.length) {
            case 1:
                input = Paths.get(args[0]);
                output = Paths.get(removeExtension(input.getFileName().toString()) + ".js");
                break;
            case 2:
                input = Paths.get(args[0]);
                output = Paths.get(args[1]);
                break;
        }

        ParseTree tree = parseFile(input);

        System.out.println("Checking types...");
        new TypeChecker().visit(tree);

        System.out.println("Generating code...");
        String code = (String) new CodeGenerator().visit(tree);

        System.out.println("Writing file...");
        renderFile(output, code);
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
