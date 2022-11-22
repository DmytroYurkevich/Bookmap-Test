package com.company;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Main {
    static OrderBook orderBook = new OrderBook();

    public static void main(String[] args) throws Exception {
        executeInput("input.txt");
    }

    private static void executeInput(String fileName) throws Exception {
        //Input
        BufferedReader input = new BufferedReader(new FileReader(fileName));
        //Output
        FileWriter outputFile = new FileWriter("output.txt", StandardCharsets.UTF_8);

        boolean firstOutputLine = true;

        for(String line; (line = input.readLine()) != null; ) {
            String[] operands = line.split(",");
            switch(operands[0]) {
                case "u" -> orderBook.update(Integer.parseInt(operands[1]), Integer.parseInt(operands[2]), BookOrderType.valueOf(operands[3]));
                case "q" -> {
                    if (!firstOutputLine) {
                        outputFile.write("\n");
                    }
                    switch (operands[1]) {
                        case "best_bid" -> outputFile.write(orderBook.searchForBestOrder(BookOrderType.bid).toString());
                        case "best_ask" -> outputFile.write(orderBook.searchForBestOrder(BookOrderType.ask).toString());
                        case "size" -> outputFile.write(String.valueOf(orderBook.getSizeAtPrice(Integer.parseInt(operands[2]))));
                    }
                    firstOutputLine = false;
                    outputFile.flush();
                }
                case "o" -> orderBook.executeMarketOrder(Integer.parseInt(operands[2]), MarketOrderType.valueOf(operands[1]));
            }
        }
        input.close();
        outputFile.close();
    }
}