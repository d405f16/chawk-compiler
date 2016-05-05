import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.lang.reflect.Type;

class Visitor {
    public static void main(String[] args) {
        String smpl = "e=a==b||c==d&&a==c setup = {} route = {}";

        chawkLexer lexer = new chawkLexer(new ANTLRInputStream(smpl));
        chawkParser parser = new chawkParser(new CommonTokenStream(lexer));


        ParseTree tree = parser.program();

        new CodeGenerator().visit(tree);
    }
}
