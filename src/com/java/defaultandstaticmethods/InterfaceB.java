package com.java.defaultandstaticmethods;

public interface InterfaceB {
    default void defaultMethod(){
        System.out.println("Interface B default method");
    }

    static void staticMethod(){
        System.out.println("Interface B static method");
    }
}
