package SymbolTable;

public class Symbol {
    private Scope scope;
    private String name;
    private String type;

    public Symbol(String name) {
        this.name = name;
    }

    public Symbol(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public Scope getScope() {
        return this.scope;
    }

    public String getName() {
        return this.name;
    }

    public String getType() {
        return this.type;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public void setType(String type) {
        this.type = type;
    }
}
