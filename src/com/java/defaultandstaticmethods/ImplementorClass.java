package com.java.defaultandstaticmethods;

public class ImplementorClass extends AbstractClassC implements InterfaceA, InterfaceB {// both interface having same signature of default method
    @Override
    public void defaultMethod() { //If not overriden then: //inherit unrelated defaults for defaultMehod() from types...
        InterfaceA.super.defaultMethod();// Call interface A default method
        super.defaultMethod(); // Abstract class method
    }

    //In case extending abstract class and implementing interface, both having same method, then also need to override method


    public static void main(String[] args) {
        ImplementorClass implementorClass = new ImplementorClass();
        implementorClass.defaultMethod();
        InterfaceA.staticMethod();

    }
}
