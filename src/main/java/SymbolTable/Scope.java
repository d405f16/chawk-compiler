package SymbolTable;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Scope {
    public final int level;
    public Scope enclosingScope;
    protected Map<String, Symbol> symbolMap = new LinkedHashMap<String, Symbol>();
    private List<String> keywords = Arrays.asList("drone.fly", "drone.takeoff", "drone.land", "param");

    public Scope(int level, Scope enclosingScope) {
        this.level = level;
        this.enclosingScope = enclosingScope;
    }

    public void define(String name, String type) {
        Symbol symbol = new Symbol(name, type);
        if (this.keywords.contains(symbol.getName())) {
            throw new NullPointerException("symbol name is reserved");
        }
        symbol.setScope(this);
        symbolMap.put(symbol.getName(), symbol);
    }

    public void defineKeyword(Symbol symbol) {
        symbol.setScope(this);
        symbolMap.put(symbol.getName(), symbol);
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
