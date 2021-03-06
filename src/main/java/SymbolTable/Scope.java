package SymbolTable;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Scope {
    private final int level;
    private Scope enclosingScope;
    private Map<String, Symbol> symbolMap = new LinkedHashMap<String, Symbol>();

    Scope(int level, Scope enclosingScope) {
        this.level = level;
        this.enclosingScope = enclosingScope;
    }

    public void define(String name, String type) {
        Symbol symbol = new Symbol(name, type);
        symbol.setScope(this);
        symbolMap.put(symbol.getName(), symbol);
    }

    public Symbol resolve(String name) {
        Symbol symbol = symbolMap.get(name);
        if (symbol != null) return symbol;
        if (this.enclosingScope != null) return this.enclosingScope.resolve(name);
        return null;
    }
}
