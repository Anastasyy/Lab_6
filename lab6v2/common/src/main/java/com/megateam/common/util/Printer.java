package com.megateam.common.util;

import java.util.List;

/** Application printer abstraction */
public interface Printer {
    /**
     * An abstraction of method that provides an ability to print strings in the same line
     *
     * @param printable string to be printed
     */
    void print(String printable);

    /**
     * An abstraction of method that provides an ability to print strings in the new line
     *
     * @param printable string to be printed
     */
    void println(String printable);

    /**
     * An abstraction of method that provides an ability to print parametrized strings
     *
     * @param printable parametrized string to be printed
     * @param args string parameters
     */
    void printf(String printable, Object... args);

    /**
     * An abstraction of method that provides an ability to print a list of elements using
     * toString() method for each element
     *
     * @param printable a list of printable elements
     */
    void printList(List<?> printable);
}
