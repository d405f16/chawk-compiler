package SymbolTable;

import java.lang.reflect.Type;

public class Symbol {
    protected Scope _scope;
    protected String _name;
    protected String _type;

    public Symbol(String name){
        _name = name;
    }

    public Symbol(String name, String type){
        _name = name;
        _type = type;
    }

    public Scope getScope(){
        return _scope;
    }

    public String getName(){
        return _name;
    }
    public String getType(){
        return _type;
    }
    public void setScope(Scope scope){
        _scope = scope;
    }

    public void SetType(String type){
        _type = type;
    }



}
