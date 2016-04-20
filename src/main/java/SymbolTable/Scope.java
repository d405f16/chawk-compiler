package SymbolTable;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Scope {
    public final int _level;
    public Scope _enclosingScope;
    protected Map<String, Symbol> symbolMap = new LinkedHashMap<String, Symbol>();

    public Scope(int level, Scope enclosingScope){
        _level = level;
        _enclosingScope = enclosingScope;
    }
    public void define(Symbol symbol){
        symbol.setScope(this);
        symbolMap.put(symbol._name, symbol);
    }
    public Symbol resolve(String name){
        Symbol symbol = symbolMap.get(name);
        if (symbol!=null) return symbol;
        if  (_enclosingScope !=null) return _enclosingScope.resolve(name);
        return null;
    }
    public Scope enclosingScope(){
        return _enclosingScope;
    }
}
