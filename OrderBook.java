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
        if(price < 1 || price > 1_000_000_000 || newOrder.getSize() < 0 || newOrder.getSize() > 100_000_000) {
            return;
        }

        if(newOrder.getSize() == 0) {
            orderBook.remove(price);
            return;
        }
        orderBook.put(price, newOrder);
    }

    public int getSizeAtPrice(int price) {
        if(orderBook.containsKey(price)) {
            return orderBook.get(price).getSize();
        }
        return 0;
    }

    public Optional<Map.Entry<Integer, Order>> searchForBestOrder(BookOrderType type) {
        return orderBook.entrySet().stream()
                .filter(integerOrderEntry -> integerOrderEntry.getValue().getType() == type)
                .max(new OrderEntryComparator(type));
    }

    public void executeMarketOrder(int sizeToExecute, MarketOrderType marketOrderType) {
        BookOrderType type = BookOrderType.values()[marketOrderType.ordinal()];

        while(sizeToExecute > 0) {
            Map.Entry<Integer, Order> bestOrderEntry = searchForBestOrder(type).orElseThrow();

            if(bestOrderEntry.getValue().getSize() <= sizeToExecute) {
                sizeToExecute -= bestOrderEntry.getValue().getSize();
                orderBook.remove(bestOrderEntry.getKey());
                continue;
            }
            bestOrderEntry.getValue().setSize(bestOrderEntry.getValue().getSize() - sizeToExecute);
            return;
        }
    }

    public static class OrderEntryComparator implements Comparator<Map.Entry<Integer, Order>> {

        private final BookOrderType bookOrderTypeToBeCompared;
        public OrderEntryComparator(BookOrderType bookOrderTypeToBeCompared) {
            this.bookOrderTypeToBeCompared = bookOrderTypeToBeCompared;
        }

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