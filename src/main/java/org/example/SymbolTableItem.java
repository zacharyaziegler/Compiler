package org.example;


public class SymbolTableItem {
    String name;
    MyParser.TYPE type;

    public SymbolTableItem(String name, MyParser.TYPE type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public MyParser.TYPE getType() {
        return type;
    }

    @Override
    public String toString() {
        return "SymbolTableItem{name='" + name + "', type=" + type + "}";
    }

}

