package com.company;

import com.company.enums.BookOrderType;
import com.company.enums.MarketOrderType;

import java.util.Arrays;
import java.util.Optional;

public class OrderBookQueries {
    // todo better array. split expensive operation s
    private static String inputData =
            "u,9,1,bid\n" +
            "u,11,5,ask\n" +
            "q,best_bid\n" +
            "u,10,2,bid\n" +
            "q,best_bid\n" +
            "o,sell,1\n" +
            "q,size,10\n" +
            "u,9,0,bid\n" +
            "u,11,0,ask";

    // todo it's so bad method, if operands[0] will "u" or "o" will be empty string
    // 
    public static String executeInput(OrderBook orderBook) {
        StringBuilder stringBuilder = new StringBuilder();
        Arrays.stream(inputData.split("\n")).forEach(string -> {
            String[] operands = string.split(",");
            switch(operands[0]) {
                case "u" -> update(orderBook, operands);
                // todo for "q" add try catch because use orElseThrow 
                case "q" -> stringBuilder.append(query(orderBook, operands).orElseThrow()).append("\n");
                case "o" -> marketOrder(orderBook, operands);
            }
        });
        return stringBuilder.toString();
    }

    // todo better third params to operands [2] and operands[3], it's better and code more readably
    private static void update(OrderBook orderBook, String[] operands) {
        // todo add static method into order. params: operands[2], operands[3] and try catch into static method
        orderBook.update(Integer.parseInt(operands[1]),
                new Order(Integer.parseInt(operands[2]), BookOrderType.valueOf(operands[3].toUpperCase())));
    }

    // todo operands it's bad, pass param string with actual name
    // todo  this method can't return null why use Optional ?
    private static Optional<String> query(OrderBook orderBook, String[] operands) {
        switch (operands[1]) {
            case "best_bid" -> {
                int orderKey = orderBook.searchForBestOrder(BookOrderType.BID).orElseThrow().getKey();
                return Optional.of(orderKey + "," + orderBook.getSizeAtPrice(orderKey));
            }
            case "best_ask" -> {
                int orderKey = orderBook.searchForBestOrder(BookOrderType.ASK).orElseThrow().getKey();
                return Optional.of(orderKey + "," + orderBook.getSizeAtPrice(orderKey));
            }
            case "size" -> {
                return Optional.of(String.valueOf(orderBook.getSizeAtPrice(Integer.parseInt(operands[2]))));
            }
            // todo default way ?  
        }
        return Optional.empty();
    }

    private static void marketOrder(OrderBook orderBook, String[] operands) {
        // todo: add try catch. Integer.parseInt(...) or MarketOrderType.valueOf(...) can not parse string to int or not find enum
        // , will be exception
        // todo: add local variable, simple debug code 
        // todo: "Integer.parseInt(operands[2]), MarketOrderType.valueOf(operands[1].toUpperCase())" move to excute method 
        // and add try catch 
        orderBook.executeMarketOrder(Integer.parseInt(operands[2]), MarketOrderType.valueOf(operands[1].toUpperCase()));
    }
}