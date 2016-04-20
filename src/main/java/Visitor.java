import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.lang.reflect.Type;

class Visitor {
    public static void main(String[] args) {
        String cmplx = "for (i=1 to 10 by 3){i = print(string = \"hej\", speed=3) a=2} setup={b=3 d=3} print(string = \"hej\", speed=3) route={c=4}";
        //String smpl = "if (3 > 7 > 10) {} setup = {} route = {}";
        String smpl = "if (2>1 && 312 != 200 || 7 > 10) {} setup = {} route = {}";

        chawkLexer lexer1;
        lexer1 = new chawkLexer(new ANTLRInputStream(cmplx));
        chawkParser parser1 = new chawkParser(new CommonTokenStream(lexer1));

        chawkLexer lexer2 = new chawkLexer(new ANTLRInputStream(smpl));
        chawkParser parser2 = new chawkParser(new CommonTokenStream(lexer2));

        ParseTree tree1 = parser1.program();
        ParseTree tree2 = parser2.program();

        new TypeChecker().visit(tree2);

        //new PrettyPrinter().visit(tree1);
        //new PrettyPrinter().visit(tree2);
    }
}
