package com.company;

import com.company.enums.BookOrderType;
import com.company.enums.MarketOrderType;

import java.util.Arrays;
import java.util.Optional;

public class OrderBookQueries {
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

    public static String executeInput(OrderBook orderBook) {
        StringBuilder stringBuilder = new StringBuilder();
        Arrays.stream(inputData.split("\n")).forEach(string -> {
            String[] operands = string.split(",");
            switch(operands[0]) {
                case "u" -> update(orderBook, operands);
                case "q" -> stringBuilder.append(query(orderBook, operands).orElseThrow()).append("\n");
                case "o" -> marketOrder(orderBook, operands);
            }
        });
        return stringBuilder.toString();
    }

    private static void update(OrderBook orderBook, String[] operands) {
        orderBook.update(Integer.parseInt(operands[1]),
                new Order(Integer.parseInt(operands[2]), BookOrderType.valueOf(operands[3].toUpperCase())));
    }

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
        }
        return Optional.empty();
    }

    private static void marketOrder(OrderBook orderBook, String[] operands) {
        orderBook.executeMarketOrder(Integer.parseInt(operands[2]), MarketOrderType.valueOf(operands[1].toUpperCase()));
    }
}