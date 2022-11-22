package com.company;

import java.util.ArrayList;
import java.util.Comparator;

public class OrderBook {
    private final ArrayList<Order> orders = new ArrayList<>();
    private final OrdersCount ordersCount = new OrdersCount();
    private static class OrdersCount {
        private final int[] ordersCount = {0,0,0};
        public int get(BookOrderType type) {
            return ordersCount[type.ordinal()];
        }
        public void dec(BookOrderType type) {
            ordersCount[type.ordinal()]--;
        }
        public void inc(BookOrderType type) {
            ordersCount[type.ordinal()]++;
        }
    }

    public void update(int price, int size, BookOrderType type) {
        if(price < 1 || price > 1_000_000_000 || size < 0 || size > 100_000_000) return;

        int index = myBinarySearch(orders, price, type);
        if(size == 0) {
            if(index >= 0) {
                orders.remove(index);
                ordersCount.dec(type);
                return;
            }
            return;
        }
        if(size > 0) {
            if(index >= 0) {
                orders.get(index).size = size;
                return;
            }
            orders.add(new Order(price, size, type));
            ordersCount.inc(type);
            sortOrders(orders);
        }
    }

    public int getSizeAtPrice(int price) {
        int index = myBinarySearch(orders, price, BookOrderType.any);
        if(index < 0) return 0;
        return orders.get(index).size;
    }

    public Order searchForBestOrder(BookOrderType type) {
        switch(type) {
            case bid -> {
                return orders.get(ordersCount.get(BookOrderType.bid) - 1);
            }
            case ask -> {
                return orders.get(orders.size() - ordersCount.get(BookOrderType.ask));
            }
        }
        return null;
    }

    public void executeMarketOrder(int size, MarketOrderType marketOrderType) {
        BookOrderType type = null;
        switch(marketOrderType) {
            case sell -> type = BookOrderType.bid;
            case buy -> type = BookOrderType.ask;
        }

        while(size > 0) {
            Order bestOrder = searchForBestOrder(type);
            if(bestOrder.size <= size) {
                size -= bestOrder.size;
                orders.remove(bestOrder);
                ordersCount.dec(type);
                continue;
            }
            bestOrder.size -= size;
            return;
        }
    }

    private int myBinarySearch(ArrayList<Order> sortedOrders, int price, BookOrderType type) {
        int index = -1;
        int low = 0, high = orders.size() - 1;
        switch(type) {
            case ask -> {
                low = orders.size() - ordersCount.get(BookOrderType.ask);
                high = orders.size() - 1;
            }
            case bid -> {
                high = ordersCount.get(BookOrderType.bid) - 1;
            }
            case any -> {
                high = orders.size() - 1;
            }
        }

        while (low <= high) {
            int mid = low  + ((high - low) / 2);
            if (sortedOrders.get(mid).price < price) {
                low = mid + 1;
            } else if (sortedOrders.get(mid).price > price) {
                high = mid - 1;
            } else if (sortedOrders.get(mid).price == price) {
                index = mid;
                break;
            }
        }
        return index;
    }
    private void sortOrders(ArrayList<Order> orders) {
        orders.sort(Comparator.comparingInt(o -> o.price));
    }
}