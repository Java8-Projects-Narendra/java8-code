package com.java.mkyong;

import com.java.model.Developer;
import com.java.model.Hosting;
import com.java.model.Person;
import com.java.model.Student;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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

        System.out.println("----------------Sorting Map-------------------");
        //------------------------------------------------------------------
        Map<String, Integer> unsortedMap = getHashmapSample();
        Map<String, Integer> sortedMap = unsortedMap.entrySet().stream()  //unsortedMap.strem() doesn't works
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new)); // toMap() will returns HashMap by default, we need LinkedHashMap to keep the order.
        System.out.println(sortedMap);

        //---Sort by value
        Map<String, Integer> sortedMap2 = getHashmapSample().entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
        System.out.println(sortedMap2);

        //----Map<Object,Object>
        //The Stream can’t sort the Map<Object,Object> directly. To solve it, convert it into Map<String,String>, review below example.


        System.out.println("----------------Converting List to map with sorting-------------------");
        //------------------------------------------------------------------
        Map result2 = getHostingList().stream()
                .sorted(Comparator.comparingLong(Hosting::getWebsites).reversed())
                .collect(
                        Collectors.toMap(Hosting::getName, Hosting::getWebsites, // key = name, value = websites
                                (oldValue, newValue) -> oldValue, // if same key, take the old key
                                LinkedHashMap::new) // returns a LinkedHashMap, keep order
                );

        System.out.println(result2);


        System.out.println("----------------Filter Map-------------------");
        //------------------------------------------------------------------
        Map result3 = getHashmapSample().entrySet().stream()
                .filter( entry -> "A".equals(entry.getKey()) || "E".equals(entry.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        System.out.println( result3);

        //--Using predicate
        Map<String, Integer> filteredMap = filterByValue(getHashmapSample(), x -> x==1 || x ==2); // Inside method we are using V generics
        System.out.println(filteredMap);


        System.out.println("----------------Flat Map-------------------");
        //------------------------------------------------------------------

        String[][] data = new String[][]{{"a", "b"}, {"c", "d"}, {"e", "f"}};

        List listt = Arrays.stream(data)
                .flatMap(x -> Arrays.stream(x))
                .collect(Collectors.toList());
        System.out.println(listt);

        String foundData = Arrays.stream(data)
                .flatMap( x -> Arrays.stream(x))
                .filter( x -> "a".equals(x))
                .findAny()
                .orElse("Not found");
        System.out.println(foundData);

        // Stream<String[]>		-> flatMap ->	Stream<String>
        // Stream<Set<String>>	-> flatMap ->	Stream<String>
        // Stream<List<String>>	-> flatMap ->	Stream<String>
        // Stream<List<Object>>	-> flatMap ->	Stream<Object>

        //---Get all book list
        List<Student> studentList = new ArrayList<>();
        Student st1 = new Student("stu1", Arrays.asList("book1", "book2", "book3"));
        Student st2 = new Student("stu2", Arrays.asList("book3", "book4", "book5"));
        studentList.add(st1);
        studentList.add(st2);

        List<String> bookList = studentList.stream()
                .map(student -> student.getBooks())
                .flatMap(booklist -> booklist.stream())
                .collect(Collectors.toList());
        System.out.println(bookList);

        //---Stream + Primitive + flatMapToInt
        int[] intArray = {1, 2, 3};
        Stream<int[]> streamArray = Stream.of(intArray); // Not stream of int, it return stream of int[]
        IntStream intStream = streamArray.flatMapToInt(x -> Arrays.stream(x));
        intStream.forEach(x -> System.out.println(x));



        System.out.println("----------------Optional-------------------");
        //------------------------------------------------------------------
        //---Creating Optional
        Optional<String> empty = Optional.empty();
        System.out.println(empty);

        Optional<String> opt = Optional.of("name");
        System.out.println(opt);

            //Optional<String> opt2 = Optional.of(null); //Will throw Nullpointer exception, use ofNullable instead
        Optional opt3 = Optional.ofNullable(null);
        Optional opt4 = Optional.ofNullable("nonEmpty");
        System.out.println(opt3 + ", "+opt4);

        //----Checking value with isPresent
        Optional<String> opt5 = Optional.of("nare");
        System.out.println(opt5.isPresent());

        //----Conditional Action with ifPresent
        Optional<String> opt6 = Optional.of("nare");
        opt6.ifPresent(x -> System.out.println(x.toUpperCase())); //Takes consumeer parameter

        //----Default value orElse
        String name = null;
        String gotName = Optional.ofNullable(name).orElse("not found"); //orElse API is used to retrieve the value wrapped inside an Optional instance
        System.out.println("gotname "+gotName);

        //----orElseGet
        String gotName2 = Optional.ofNullable(name).orElseGet(() -> "not found 2"); // Takes Supplier parameter which is invoked and returns the value of the invocation
        System.out.println("gotname 2 "+gotName2);

        //--- difference between
        //String text = null; //For null value result of both are same both call getMyDefault method
        String text = "testing"; // For non null value orElse runs getMydefault method whereas orElseGet not runs
        String defaultText = Optional.ofNullable(text).orElseGet(() -> getMyDefault());
        System.out.println(defaultText);
        String defaultText2 = Optional.ofNullable(text).orElse(getMyDefault());
        System.out.println(defaultText2);

        //----Exception with orElseThrow
        String nullName = null;
        try{
            String name2 = Optional.ofNullable(nullName).orElseThrow(
                    IllegalArgumentException::new);
        } catch (IllegalArgumentException ex) {
            System.out.println("Found Illegalargument exception");
        }

        //---Returning value with get()
        Optional<String> optt = Optional.of("baeldung");
        String name3 = optt.get();
        System.out.println(name3);

        //---Conditional return with filter()
        Integer year = 2016;
        Optional<Integer> yearOptional = Optional.of(year);
        boolean is2016 = yearOptional.filter(y -> y == 2016).isPresent(); //.filter returns Optional

        //----Transforming Value with map()
        List<String> companyNames = Arrays.asList("paypal", "oracle", "", "microsoft", "", "apple");
        Optional<List<String>> listOptional = Optional.of(companyNames);
        int size = listOptional
                .map(List::size)
                .orElse(0);

        //----Transforming Value with flatMap()
        Person person = new Person("john", 26);
        Optional<Person> personOptional = Optional.of(person); //notice now that when we wrap a Person object, it will contain nested Optional instances:
        String name4 = personOptional.flatMap(Person::getName).orElse("");
        System.out.println("flatmap name "+name4);
            //The only difference is that getName() returns an Optional rather than a String as did the trim() operation. This, coupled with the fact that a map transformation result is wrapped in an Optional object leads to a nested Optional.
            //My : when returned value is optional the use flatmap , when returned value is not optional then use map
            //http://www.mkyong.com/java8/java-8-optional-in-depth/
            //https://www.baeldung.com/java-optional





























    }

    public static String getMyDefault() {
        System.out.println("Getting Default Value");
        return "Default Value";
    }

    // Generic Map filterbyvalue, with predicate
    public static <K, V> Map<K, V> filterByValue(Map<K, V> map, Predicate<V> predicate) { // Use of generics
        return map.entrySet()
                .stream()
                .filter(x -> predicate.test(x.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
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
        map.put("E", 1);
        map.put("D", 2);
        map.put("C", 3);
        map.put("B", 4);
        map.put("A", 5);

        return map;
    }

    private static List<String> getListSample() {
        return Arrays.asList("AA", "BB", "CC", "DD");
    }

    private static List<Hosting> getHostingList(){
        List<Hosting> list = new ArrayList<>();
        list.add(new Hosting(1, "liquidweb.com", 80000));
        list.add(new Hosting(2, "linode.com", 90000));
        list.add(new Hosting(3, "digitalocean.com", 120000));
        list.add(new Hosting(4, "aws.amazon.com", 200000));
        list.add(new Hosting(5, "mkyong.com", 1));
        list.add(new Hosting(6, "linode.com", 100000));

        return list;
    }
 }
