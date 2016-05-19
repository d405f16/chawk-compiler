import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

class Visitor {
    public static void main(String[] args) {
        String smpl = "a=2.0 d=1.0 while(a>d){b=3} setup = {} route = {if(1<2){print(hej=123)}}";

        chawkLexer lexer = new chawkLexer(new ANTLRInputStream(smpl));
        chawkParser parser = new chawkParser(new CommonTokenStream(lexer));


        ParseTree tree = parser.program();

        //new TypeChecker().visit(tree);
        //new SemanticChecker().visit(tree);
        new JBCodeGenerator().visit(tree);
    }
}
