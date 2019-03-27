package com.java.otherexample;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class frominteviewbook {
    public static void main(String[] args) {
        //-------Sorting
        String[] names = {"One", "Two", "Three", "Four", "Five", "Six"};
        List<String> sorted = Arrays.stream(names)
                .sorted(String::compareToIgnoreCase)
                .collect(Collectors.toList());

        System.out.println(sorted);

        //.sorted((o1, o2) -> Integer.compare(o1.length(), o2.length()))
        //.sorted((o1, o2) -> o1.getHireDate().compareTo(o2.getHireDate())) //Employee sorting
        //.sorted(Comparator.comparing(Employee::getHireDate))

        /*Comparator<Employee> byFirstName = (e1, e2) -> e1.getFirstName().compareTo(e2.getFirstName());
        Comparator<Employee> byLastName = (e1, e2) -> e1.getLastName().compareTo(e2.getLastName());
        employees.stream()
                .sorted(byFirstName.thenComparing(byLastName))*/

        System.out.println("-------Creating Stream------------");
        Stream<String> stream = Stream.of("Ken", "Jeff", "Chris", "Ellen");
        Stream<Integer> integerStream = Stream.of(1, 2, 3, 4, 5);

        IntStream oneToFive = IntStream.range(1, 6);
        IntStream intStream = Arrays.stream(new int[]{1,2,3});

        //----File Stream
        Path dir = Paths.get("F:\\NARENDRA");// Paths not Path
        try{
            Stream<String> lines = Files.lines(dir);
        }catch (Exception ex){
        }


        System.out.println("-------Operatioins on Stream------------");
        //Take Odd numbers from the stream square them and them add them all
        int sum = IntStream.of(1,2,3,4,5)
                .peek(e -> System.out.println("Taking Integer: "+e))
                .filter( s -> s%2 == 1)
                .peek(e -> System.out.println("Filtering Integer: "+e))
                .map(n -> n*n)
                .peek(e -> System.out.println("Mapped Integeer: "+e))
                .reduce(0, Integer::sum);

        //Stream peek(Consumer action) returns a stream consisting of the elements of this stream, additionally performing the provided action on each element as elements are consumed from the resulting stream.

        //Stream API contains a set of predefined reduction operations, such as average(), sum(), min(), max(), and count()
        //T reduce(T identity, BinaryOperator<T> accumulator)
        //This method takes two parameters: the identity and the accumulator. The identity element is both the initial value of the reduction and the default result if there are no elements in the stream.


    }
}
