package com.company;

public enum BookOrderType {
    bid("bid"),
    ask("ask"),
    spread("spread"),
    any("any");
    private String str;
    BookOrderType(String str) {
        this.str = str;
    }
}
