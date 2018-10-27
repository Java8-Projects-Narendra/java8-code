package com.java.mkyong;

import com.java.model.Developer;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Runner {
    public static void main(String[] args) {

        // From http://www.mkyong.com/tutorials/java-8-tutorials/

        List<Developer> developerList = getDevelopers();

        System.out.println("----------------Comparator Example with Lambda------------");
        //---------------------------------------------------------------------------------
        developerList.sort(( d1,  d2) -> d1.getName().compareTo(d2.getName()));
        //developerList.sort(( Developer d1, Developer d2) -> d1.getName().compareTo(d2.getName()));

        developerList.forEach(d -> System.out.println(d.getName()));

        //---Without Lambda
        Collections.sort(developerList, new Comparator<Developer>() {
            @Override
            public int compare(Developer o1, Developer o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });

        for (Developer developer : developerList) {
            System.out.println(developer.getName());
        }



        System.out.println("--------------------For each----------------------");
        //---------------------------------------------------------------------------------
        //----With Map
        Map<String, Integer> hashMap = getHashmapSample();

        hashMap.forEach((k, v) -> System.out.println(k + " : "+v)); //Takes Biconsumer

        hashMap.forEach((k, v) -> {
            if ("E".equals(k)) {
                System.out.println("Found E");
            }
        });

        //----With List
        List<String> list = getListSample();
        list.forEach( str -> System.out.println(str));
        list.forEach(System.out::println);




        System.out.println("--------------------filter() and collect()----------------------");
        //---------------------------------------------------------------------------------
        List<String> list2 = getListSample();
        List<String> filteredList = list2.stream()
                .filter(str -> !"EE".equals(str))  //Takes Predicate
                .collect(Collectors.toList());     //Collectors

        System.out.println(filteredList);




        System.out.println("--------------------filter(), findAny() and orElse() use of Optional----------------------");
        //---------------------------------------------------------------------------------

        String foundString =  list2.stream()
                .filter(str -> !"AA".equals(str))
                .findAny()
                .orElse("not Found");
        System.out.println(foundString);

        //--Optional
        Optional<String> optional = list2.stream()
                .findAny(); //Returns Optional
        String str = optional.isPresent() ? optional.get() : "not found";
        System.out.println(str);



        System.out.println("--------------------map()----------------------");
        //---------------------------------------------------------------------------------
        List<Integer> listint = Arrays.asList(1,2,3,4);
        List<Integer> mappedList = listint.stream()
                .map(i -> i*i) // Takes Function
                .collect(Collectors.toList());
        System.out.println(mappedList);

        //--With object
        List<String> devNames = developerList.stream()
                .map( dev -> dev.getName())
                .collect(Collectors.toList());
        System.out.println(devNames);


        System.out.println("--------------------Group By, Count and Sort----------------------");
        //---------------------------------------------------------------------------------

        List<String> items = Arrays.asList("apple", "apple", "banana", "apple", "orange", "banana", "papaya");
        Map<String, Long> result = items.stream()
                .collect(
                        Collectors.groupingBy(
                                Function.identity() ,
                                Collectors.counting()
                        )
                );

        System.out.println(result);

        //More example ther in http://www.mkyong.com/java8/java-8-collectors-groupingby-and-mapping-example/ but didn't get


        System.out.println("--------------------Other Operations----------------------");
        //-----------------------Array to strem-----------------------------------------

        String[] array = {"a", "b", "c", "d", "e"};

        //Arrays.stream
        Stream<String> stream1 = Arrays.stream(array);

        //Stream.of
        Stream<String> stream2 = Stream.of(array);
        stream2.forEach(x -> System.out.println(x));


        //---------------Reuse Stream
        //java.lang.IllegalStateException: stream has already been operated upon or closed


        String[] arrays = {"a", "b", "c", "d", "e"};

        Supplier<Stream<String>> streamSupplier = () -> Stream.of(arrays);


        //get new stream
        streamSupplier.get().forEach(x -> System.out.println(x));

        //get another new stream
        long count = streamSupplier.get().filter(x -> "b".equals(x)).count();
        System.out.println(count);









    }

    private static List<Developer> getDevelopers() {

        List<Developer> result = new ArrayList<>();

        result.add(new Developer("mkyong", new BigDecimal("70000"), 33));
        result.add(new Developer("alvin", new BigDecimal("80000"), 20));
        result.add(new Developer("jason", new BigDecimal("100000"), 10));
        result.add(new Developer("iris", new BigDecimal("170000"), 55));

        return result;

    }

    private static Map<String, Integer> getHashmapSample() {
        Map<String, Integer> map = new HashMap<>();
        map.put("A", 1);
        map.put("B", 2);
        map.put("C", 3);
        map.put("D", 4);
        map.put("E", 5);

        return map;
    }

    private static List<String> getListSample() {
        return Arrays.asList("AA", "BB", "CC", "DD");
    }
}
