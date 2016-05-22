package SymbolTable;

import java.util.ArrayList;
import java.util.Stack;

public class SymbolTable {
    private Stack<Scope> scopeStack;
    private ArrayList<Scope> allScopes;
    private Integer level;

    public SymbolTable() {
        scopeStack = new Stack<Scope>();
        allScopes = new ArrayList<Scope>();
        level = 0;

        Scope scope = new Scope(0, null);

        scopeStack.push(scope);
        allScopes.add(scope);
    }

    public Scope pushScope() {
        Scope enclosingScope = scopeStack.peek();
        Scope scope = new Scope(++level, enclosingScope);
        scopeStack.push(scope);
        allScopes.add(scope);
        return scope;
    }

    public void popScope() {
        scopeStack.pop();
    }

    public Scope currentScope() {
        if (scopeStack.size() > 0) return scopeStack.peek();
        System.err.println("Unbalanced scope stack.");
        return allScopes.get(0);
    }

    public Scope getScope(int level) {
        for (Scope scope : scopeStack) {
            if (scope.level == level) return scope;
        }
        return null;
    }
}
