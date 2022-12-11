package com.company;

public class Main {
    public static void main(String[] args) {
        // todo move to method executeInput
        // todo add try catch orElseThrow can throw exception program failed in run time. it's bad style 
        OrderBook orderBook = new OrderBook();
        System.out.println(OrderBookQueries.executeInput(orderBook));
    }
}