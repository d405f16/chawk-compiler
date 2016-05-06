import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.lang.reflect.Type;

class Visitor {
    public static void main(String[] args) {
        String smpl = "a.asd = 10 asd.ble = [123, 'asd', a.asd] asd.test = {speed = 10} foo(hej=10) setup = {} route = {}";

        chawkLexer lexer = new chawkLexer(new ANTLRInputStream(smpl));
        chawkParser parser = new chawkParser(new CommonTokenStream(lexer));


        ParseTree tree = parser.program();

        new CodeGenerator().visit(tree);
    }
}
