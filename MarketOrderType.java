package com.company;

public enum MarketOrderType {
    sell("sell"),
    buy("sell");
    private String str;
    MarketOrderType(String str) {
        this.str = str;
    }
}