package com.company;

public class Order {
    public int price;
    public int size;
    public BookOrderType type;

    public Order(int price, Integer size, BookOrderType type) {
        this.price = price;
        this.size = size;
        this.type = type;
    }

    @Override
    public String toString() {
        return price + "," + size;
    }
}
