package SymbolTable;

public class Symbol {
    private Scope scope;
    private String name;
    private String type;
    private Integer size;

    public Symbol(String name) {
        this.name = name;
    }

    public Symbol(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public Symbol(String name, String type, Integer size) {
        this.name = name;
        this.type = type;
        this.size = size;
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

    public Integer getSize() {
        return this.size;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public void setType(String type) {
        this.type = type;
    }
}