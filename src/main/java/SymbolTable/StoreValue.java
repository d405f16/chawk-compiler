package SymbolTable;

/**
 * Created by Mark on 18-05-2016.
 */
public class StoreValue {
    private String name;
    private String type;
    private Integer location;

    public StoreValue(String name, String type, Integer location) {
        this.name = name;
        this.type = type;
        this.location = location;
    }

    public String getName() {
        if(this.name != null) return this.name;
        else return null;
    }

    public String getType() {
        if (this.type != null) return this.type;
        else return null;
    }

    public Integer getLocation() {if(this.location != null) return this.location;
                                  else return null;}
}
