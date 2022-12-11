package com.company;

import com.company.enums.BookOrderType;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Order {
    @Setter private int size;
    private final BookOrderType type;

    public Order(int size, BookOrderType type) {
        this.size = size;
        this.type = type;
    }
}