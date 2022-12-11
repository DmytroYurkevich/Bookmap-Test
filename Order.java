package com.company;

import com.company.enums.BookOrderType;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Order {
    // todo: jaava style ? agter annotation new line !!!
    // todo: better add method update, whitch return this and update size
    // todo: setter it's bad, because you can forgot set some field
    @Setter
    private int size;
    private final BookOrderType type;

    public Order(int size, BookOrderType type) {
        this.size = size;
        this.type = type;
    }

    // todo: add static method to create object, construct can be private
}