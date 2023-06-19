package com.megateam.common.util;

import java.util.List;

/**
 * Application console printer implementation. Provides an ability to print some things into stout
 */
public class ConsolePrinter implements Printer {
    /**
     * This method provides an ability to print strings in the same line
     *
     * @param printable string to be printed
     */
    @Override
    public void print(String printable) {
        System.out.print(printable);
    }

    /**
     * This method provides an ability to print strings in the new line
     *
     * @param printable string to be printed
     */
    @Override
    public void println(String printable) {
        System.out.println(printable);
    }

    /**
     * This method provides an ability to print parametrized strings
     *
     * @param printable parametrized string to be printed
     * @param args string parameters
     */
    @Override
    public void printf(String printable, Object... args) {
        System.out.printf(printable, args);
    }

    /**
     * This method provides an ability to print a list of elements using toString() method for each
     * element
     *
     * @param printable a list of printable elements
     */
    @Override
    public void printList(List<?> printable) {
        if (printable.size() == 0) {
            println("Empty list");
            return;
        }

        printable.forEach((e) -> println(e.toString()));
    }
}
