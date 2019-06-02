package com.java.mkyong;

import com.java.model.Developer;
import com.java.model.Hosting;
import com.java.model.Person;
import com.java.model.Student;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
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
        getDevelopers().sort(( d1,  d2) -> d1.getName().compareTo(d2.getName()));
        //developerList.sort(( Developer d1, Developer d2) -> d1.getName().compareTo(d2.getName()));

        getDevelopers().sort(Comparator.comparing(Developer::getName).reversed());// reverse comparator

        developerList.forEach(d -> System.out.println(d.getName()));//Internal loop

        System.out.println("---Without Lambda---");
        Collections.sort(developerList, new Comparator<Developer>() {
            @Override
            public int compare(Developer o1, Developer o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });

        for (Developer developer : developerList) {
            System.out.println(developer.getName());
        }

        System.out.println("----Sort using strem----");// its sorted not sort
        getDevelopers()
                .stream()
                .sorted(Comparator.comparing(Developer::getName))
                .forEach(dev -> System.out.println(dev.getName()));

        System.out.println("----Multiple Sorting Criteria-------");
        Comparator<Developer> byName = (e1, e2) -> e1.getName().compareTo(e2.getName());
        Comparator<Developer> bySalary = (e1, e2) -> e1.getSalary().compareTo(e2.getSalary());

        getDevelopers()
                .stream()
                .sorted(byName.thenComparing(bySalary))//******************
                .forEach(dev -> System.out.println(dev.getName()+" "+ dev.getSalary()));




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
                .sorted(Map.Entry.comparingByKey())// Everything Map.Entry
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


        System.out.println("----------------Generate random number using java.util.Random.ints-------------------");
        //------------------------------------------------------------------
        int min =10;
        int max =20;
        Random r = new Random();
        int randomVal = r.ints(min, (max + 1)).findFirst().getAsInt();
        System.out.println(randomVal);

        System.out.println("----------------StringJoiner  and String.join and Collectors.joining-------------------");
        //------------------------------------------------------------------
        StringJoiner sj = new StringJoiner(",");
        sj.add("aa").add("bb").add("cc").add("dd");
        System.out.println(sj.toString());

        //---with prefix and suffix
        StringJoiner sj2 = new StringJoiner("/", "prefix-", "-suffix");
        sj2.add("aa").add("bb").add("cc").add("dd");
        System.out.println(sj2.toString());

        //----String.join
            //StringJoiner is used internally by static String.join().

        String joinedStr = String.join("-", "aaaa", "bbbb");
        System.out.println(joinedStr);

        //---- Join a List by a delimiter.
        List<String> listtt = Arrays.asList("java", "python", "nodejs", "ruby");
        String resu = String.join(", ", listtt);
        System.out.println(resu);

        //-----Collectors.joining
        String reesult = listtt.stream().map(x -> x).collect(Collectors.joining(" | ", "(", ")"));
        System.out.println(reesult);

        System.out.println("----------------Java 8 Stream – Read a file line by line-------------------");
        //------------------------------------------------------------------
        String fileName = "D:\\IntellijWorkspace\\TestFile.txt";
        try(Stream<String> filestream = Files.lines(Paths.get(fileName))){ // It Files not File
            filestream.filter(x -> !"row 1".equals(x))
                    .map(y -> y.toUpperCase())
                    .forEach(System.out::println);
            //String fileStr = filestream.collect(Collectors.joining(","));
           // System.out.println(fileStr);

        } catch (IOException ex){
            System.out.println("found io exception");
        }

        //---BufferedReader + Stream
        try(BufferedReader br = Files.newBufferedReader(Paths.get(fileName))){
            List<String> brList = br.lines().collect(Collectors.toList()); // br.lines return stream
            System.out.println(brList);
        }catch (IOException ex){
            System.out.println("exception occured");
        }


        System.out.println("----------------How to join Arrays-------------------");
        //------------------------------------------------------------------
        //--Apache common lang
        //ArrayUtils.addAll(arr1, s2);

        int[] arr1= new int[]{ 1,3,4,5};
        int[] arr2= new int[]{ 11,13,14,15};

        int[] arr3 = IntStream.concat(Arrays.stream(arr1), Arrays.stream(arr2)).toArray();
        System.out.println(Arrays.toString(arr3));

        System.out.println("----------------Convert String to Stream Char-------------------");
        //------------------------------------------------------------------
        String ssstr = "nare";
        ssstr.chars() //IntStream
                .mapToObj(x -> (char)x) //Stream<Character>
                .forEach(System.out::println);


        System.out.println("----------------convert a primitive Array to List-------------------");
        //------------------------------------------------------------------
        int[] number = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

        List<Integer> intList = Arrays.stream(number)
                .boxed()
                .collect(Collectors.toList());
        System.out.println(intList);

        System.out.println("----------------Check if Array contains a certain value------------------");
        //------------------------------------------------------------------

        String[] alphabet = new String[]{"A", "B", "C"}; // normal way is to convert to list and call contains
        boolean matchPresent = Arrays.stream(alphabet).anyMatch( x -> "A".equals(x));
        System.out.println(matchPresent);


        System.out.println("---------------Summary Statistics------------------------");
        List<Developer> personList =  Arrays.asList(new Developer("a",new BigDecimal("70000"),20), new Developer("b",new BigDecimal("70000"), 43), new Developer("c",new BigDecimal("70000") ,33));

        //Find min max age
        IntSummaryStatistics statistics = personList.stream()
                .map( dev-> dev.getAge())
                .mapToInt(Integer :: intValue) //Convert Integer to int
                .summaryStatistics();
        System.out.println("Max age: "+statistics.getMax());
        System.out.println("Max age: "+statistics.getMin());

        System.out.println("---------------------findFirst vs findAny-------------");
        //The findAny() method returns any element from a Stream while the findFirst() method returns the first element in a Stream.
        List<String> list1 = Arrays.asList("A","B","C","D");

        Optional<String> result1 = list1.stream().findAny();
        System.out.println("isPresent: "+result1.isPresent() +" get Value: "+result1.get());

        System.out.println("---------------------findFirst non repeatable character-------------");

        String str1 = "AABBCCDDEFGGHH";
        Optional<Long> resssult = str1.chars()
                .mapToObj(c -> Character.valueOf((char)c))
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue() == 1L)
                .findFirst()
                .map( entry -> entry.getValue() );

        System.out.println("first Non repeatable char :"+(char)resssult.get().intValue());

        System.out.println("---------------------min max avg Find Employee with min age-------------");
        List<Developer> personList2 =  Arrays.asList(new Developer("a",new BigDecimal("70000"),20),
                new Developer("b",new BigDecimal("70000"), 43),
                new Developer("c",new BigDecimal("70000") ,33));

        Optional<Developer> developerWithMinAge = personList2.stream()
                .min(Comparator.comparing(Developer::getAge));
        System.out.println(developerWithMinAge);

        Integer maxNumber = Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9)
                .max(Comparator.comparing(Integer::valueOf))
                .get();

        System.out.println("---------------------Merge two Array-------------");
        String[] strArr1 = new String[]{"a", "b", "c", "d"};
        String[] strArr2 = new String[]{"e", "f", "g"};

        int[] intArrr1 = new int[]{1,2,3,4};
        int[] intArrr2 = new int[]{5,6,7};

        Integer[] mergedIntArr = Stream.concat(Arrays.stream(intArrr1).boxed(), Arrays.stream(intArrr2).boxed())// Using boxec
                .toArray(Integer[]::new);
        System.out.println("mergedIntArr: "+Arrays.toString(mergedIntArr));

        //---Way1
        String[] mergedStrArr1 = Stream.concat(Arrays.stream(strArr1), Arrays.stream(strArr2))
                .toArray(String[]::new);
        System.out.println("mergedStrArr1: "+Arrays.toString(mergedStrArr1));


        //---Way2
        String[] mergedStrArr2 = Stream.of(strArr1, strArr2)
                .flatMap(Stream::of)
                .toArray(String[]::new);
        System.out.println("mergedStrArr2: "+Arrays.toString(mergedStrArr2));

        //--------System.arraycopy Java
        String[] mergedStrArr3 = new String[strArr1.length + strArr2.length];
        System.arraycopy(strArr1, 0, mergedStrArr3, 0, strArr1.length);
        System.arraycopy(strArr2, 0, mergedStrArr3, strArr1.length, strArr2.length);
        System.out.println("mergedStrArr3: "+mergedStrArr3);
        //src - Source array (Object type)
        //srcPos - Starting position in Source array (Integer type)
        //dest - Destination array (Object Type)
        //destpos - Starting position in destination array (Integer type)
        //length - Number of elements to be copied (Integer type)

        //------Using ArrayUtils
        //org.apache.commons.lang3.ArrayUtils.addAll(arr1, arr2)

        System.out.println("-----------Each Double of Array------------");
        Integer[] intArr1 = new Integer[]{1,2,3};
        Integer[] intArr2 = new Integer[]{4,5,6};
        Object[] eachDoubleArr = Stream.of(intArr1)
                .map( s -> eachDouble(s, intArr2))
                .flatMap(s -> s)
                .toArray();
        System.out.println("eachDoubleArr: "+Arrays.toString(eachDoubleArr));










    }

    public static Stream<Integer[]> eachDouble(Integer s, Integer[] arr){
        return Arrays.stream(arr).map(a -> new Integer[]{a, s});
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
        result.add(new Developer("alvin", new BigDecimal("60000"), 20));
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
