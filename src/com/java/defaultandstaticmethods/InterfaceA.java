package com.java.defaultandstaticmethods;

public interface InterfaceA {
    default void defaultMethod(){
        System.out.println("Interface A default method");
    }

    static void staticMethod(){
        System.out.println("Interface A static method");
    }
}
