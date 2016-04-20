package SymbolTable;

import java.lang.reflect.Type;

public class Symbol {
    protected Scope _scope;
    protected String _name;
    protected Type _type;

    public Symbol(String name){
        _name = name;
    }

    public Symbol(String name, Type type){
        _name = name;
        _type = type;
    }

    public Scope getScope(){
        return _scope;
    }

    public String getName(){
        return _name;
    }
    public Type getType(){
        return _type;
    }
    public void setScope(Scope scope){
        _scope = scope;
    }

    public void SetType(Type type){
        _type = type;
    }



}
