package com.java.howtodoinjava;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RunnerHowtoDoInjava {
    public static void main(String[] args) {

        //----------Check ConcurrentModification exception------
        ArrayList<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        list.add("d");
        Iterator it = list.iterator();
        list.add("e");
        while(it.hasNext()){
            System.out.println("test");
            System.out.println(it.next());
            list.add("s");

        }
    }
}
