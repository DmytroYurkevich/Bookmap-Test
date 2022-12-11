package com.company;

public class Main {
    public static void main(String[] args) {
        OrderBook orderBook = new OrderBook();
        System.out.println(OrderBookQueries.executeInput(orderBook));
    }
}