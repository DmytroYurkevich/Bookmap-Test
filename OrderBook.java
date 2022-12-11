package com.company;

import com.company.enums.BookOrderType;
import com.company.enums.MarketOrderType;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class OrderBook {
    private final HashMap<Integer, Order> orderBook = new HashMap<>();

    public void update(int price, Order newOrder) {
        if (price < 1 || price > 1_000_000_000 || newOrder.getSize() < 0 || newOrder.getSize() > 100_000_000) {
            return;
        }

        if (newOrder.getSize() == 0) {
            orderBook.remove(price);
            return;
        }

        // todo put can throw exception if price can place before 
        orderBook.put(price, newOrder);
    }

    
    public int getSizeAtPrice(int price) {
        // todo operator ?
        if (orderBook.containsKey(price)) {
            return orderBook.get(price).getSize();
        }
        return 0;
    }

    // todo ussless method
    public Optional<Map.Entry<Integer, Order>> searchForBestOrder(BookOrderType type) {
        return orderBook.entrySet().stream()
                .filter(integerOrderEntry -> integerOrderEntry.getValue().getType() == type)
                // todo maybe need add static method into BookOrderType witch create Comparator from type or this method can throw exception ?
                // todo maybe it's not need, because in java it's not impossible 
                .max(new OrderEntryComparator(type));
    }

    public void executeMarketOrder(int sizeToExecute, MarketOrderType marketOrderType) {
        BookOrderType type = BookOrderType.values()[marketOrderType.ordinal()];

        while (sizeToExecute > 0) {
            // todo: orElseThrow ussless. null it's impossible. only empty map
            Map.Entry<Integer, Order> bestOrderEntry = searchForBestOrder(type).orElseThrow();

            if (bestOrderEntry.getValue().getSize() <= sizeToExecute) {
                sizeToExecute -= bestOrderEntry.getValue().getSize();
                orderBook.remove(bestOrderEntry.getKey());
                continue;
            }

            bestOrderEntry.getValue().setSize(bestOrderEntry.getValue().getSize() - sizeToExecute);
            return;
        }
    }

    // todo: if public add new file and move. java style
    public static class OrderEntryComparator implements Comparator<Map.Entry<Integer, Order>> {

        private final BookOrderType bookOrderTypeToBeCompared;

        public OrderEntryComparator(BookOrderType bookOrderTypeToBeCompared) {
            this.bookOrderTypeToBeCompared = bookOrderTypeToBeCompared;
        }

        // todo: entry1 and entury2 bad names. Better lsh and rsh -> left (right) side hand  
        @Override
        public int compare(Map.Entry<Integer, Order> entry1, Map.Entry<Integer, Order> entry2) {
            if(entry1.getKey().equals(entry2.getKey())) {
                return 0;
            }
            switch(bookOrderTypeToBeCompared) {
                case ASK -> {
                    return (entry1.getKey() < entry2.getKey()) ? 1 : -1;
                }
                case BID -> {
                    return (entry1.getKey() > entry2.getKey()) ? 1 : -1;
                }
                default -> {
                    return 0;
                }
            }
        }
    }
}