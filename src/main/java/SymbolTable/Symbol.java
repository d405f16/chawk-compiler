package SymbolTable;

public class Symbol {
    private Scope scope;
    private String name;
    private String type;

    Symbol(String name, String type) {
        this.name = name;
        this.type = type;
    }

    String getName() {
        return this.name;
    }

    public String getType() {
        return this.type;
    }

    void setScope(Scope scope) {
        this.scope = scope;
    }
}