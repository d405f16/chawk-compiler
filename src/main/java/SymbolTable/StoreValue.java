package SymbolTable;

public class StoreValue {
    private String type;
    private Integer location;

    public StoreValue(String type, Integer location) {
        this.type = type;
        this.location = location;
    }

    public String getType() {
        if (this.type != null) return this.type;
        else return null;
    }

    public Integer getLocation() {
        if (this.location != null) return this.location;
        else return null;
    }
}
