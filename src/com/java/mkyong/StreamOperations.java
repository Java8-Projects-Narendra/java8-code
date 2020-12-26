package com.java.mkyong;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class StreamOperations {
    public static void main(String[] args) {
        //************************ Collectors *****************
        //https://mkyong.com/java8/java-8-collectors-groupingby-and-mapping-example/



        //--------Grouping--------
        //Counting
        Map<String, Long> counting = items.stream().collect(
                Collectors.groupingBy(Item::getName, Collectors.counting()));

        //Summing
        Map<String, Integer> sum = items.stream().collect(
                Collectors.groupingBy(Item::getName, Collectors.summingInt(Item::getQty)));

        //group by price
        Map<BigDecimal, List<Item>> groupByPriceMap =
                items.stream().collect(Collectors.groupingBy(Item::getPrice));

        // group by price, uses 'mapping' to convert List<Item> to Set<String>
        Map<BigDecimal, Set<String>> result =
                items.stream().collect(
                        Collectors.groupingBy(Item::getPrice,
                                Collectors.mapping(Item::getName, Collectors.toSet())
                        )
                );

        //----------To Collection----
        Collectors.toSet();

        Collectors.toList();

        Collectors.toMap();
        Map<String, Integer> sortedMap = unsortedMap.entrySet().stream()  //unsortedMap.strem() doesn't works
                .sorted(Map.Entry.comparingByKey())// Everything Map.Entry
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));


        Collectors.toCollection()
        List<String> result = givenList.stream()
                .collect(toCollection(LinkedList::new));

        //----------Partitioning
        Map<Boolean, List<String>> result = givenList.stream()
                .collect(partitioningBy(s -> s.length() > 2))
    }

}
