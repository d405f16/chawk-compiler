package SymbolTable;

import java.util.LinkedHashMap;
import java.util.Map;

public class Scope {
    public final int level;
    public Scope enclosingScope;
    protected Map<String, Symbol> symbolMap = new LinkedHashMap<String, Symbol>();

    public Scope(int level, Scope enclosingScope) {
        this.level = level;
        this.enclosingScope = enclosingScope;
    }

    public void define(Symbol symbol) {
        symbol.setScope(this);
        symbolMap.put(symbol._name, symbol);
    }

    public Symbol resolve(String name) {
        Symbol symbol = symbolMap.get(name);
        if (symbol != null) return symbol;
        if (this.enclosingScope != null) return this.enclosingScope.resolve(name);
        return null;
    }

    public Scope enclosingScope() {
        return this.enclosingScope;
    }
}
