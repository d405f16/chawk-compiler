import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

class Visitor {
    public static void main(String[] args) {
        String smpl = "a=2.0 d=2.0 while(d<a){c=2}  setup = {} route = {}";

        chawkLexer lexer = new chawkLexer(new ANTLRInputStream(smpl));
        chawkParser parser = new chawkParser(new CommonTokenStream(lexer));


        ParseTree tree = parser.program();

        //new TypeChecker().visit(tree);
        //new SemanticChecker().visit(tree);
        new JBCodeGenerator().visit(tree);
    }
}
