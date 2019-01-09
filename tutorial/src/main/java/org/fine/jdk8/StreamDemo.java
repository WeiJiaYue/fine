package org.fine.jdk8;

import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * {see https://www.ibm.com/developerworks/cn/java/j-lo-java8streamapi/}
 * <p>
 * 为什么需要 Stream
 * Stream 作为 Java 8 的一大亮点，它与 java.io 包里的 InputStream 和 OutputStream 是完全不同的概念。
 * 它也不同于 StAX 对 XML 解析的 Stream，也不是 Amazon Kinesis 对大数据实时处理的 Stream。
 * Java 8 中的 Stream 是对集合（Collection）对象功能的增强，它专注于对集合对象进行各种非常便利、高效的聚合操作（aggregate operation），
 * 或者大批量数据操作 (bulk data operation)。Stream API 借助于同样新出现的 Lambda 表达式，极大的提高编程效率和程序可读性。
 * 同时它提供串行和并行两种模式进行汇聚操作，并发模式能够充分利用多核处理器的优势，使用 fork/join 并行方式来拆分任务和加速处理过程。
 * 通常编写并行代码很难而且容易出错, 但使用 Stream API 无需编写一行多线程的代码，就可以很方便地写出高性能的并发程序。
 * 所以说，Java 8 中首次出现的 java.util.stream 是一个函数式语言+多核时代综合影响的产物。
 * <p>
 * <p>
 * 总结
 * 1.Stream只有在最终执行Terminal函数的时候在会去遍历计算或聚合集合
 * 2.不是数据结构
 * 3.它没有内部存储，它只是用操作管道从 source（数据结构、数组、generator function、IO channel）抓取数据。
 * 4.它也绝不修改自己所封装的底层数据结构的数据。例如 Stream 的 filter 操作会产生一个不包含被过滤的元素的新 Stream，而不是从 source 删除那些元素。
 * 5.所有 Stream 的操作必须以 lambda 表达式为参数
 * 6.不支持索引访问
 * 7.你可以请求第一个元素，但无法请求第二个，第三个，或最后一个。不过请参阅下一项。
 * 8.很容易生成数组或者 List
 * 9.惰性化
 * 10.很多 Stream 操作是向后延迟的，一直到它弄清楚了最后需要多少数据才会开始。
 * 11.Intermediate 操作永远是惰性化的。
 * 12.并行能力
 * 13.当一个 Stream 是并行化的，就不需要再写多线程代码，所有对它的操作会自动并行进行的。
 * 14.可以是无限的
 * 集合有固定大小，Stream 则不必。limit(n) 和 findFirst() 这类的 short-circuiting 操作可以对无限的 Stream 进行运算并很快完成。
 */


public class StreamDemo {

    final static List<String> list = Arrays.asList("111", "asd2", "zexc", "123","99","223");
    final static List<String> intValList = Arrays.asList("2", "2", "2", "2");


    public static void main(String[] args) {
        IntStream.of(new int[]{1, 2, 13, 23}).forEach(System.out::println);
        IntStream.range(1, 6).forEach(System.out::println);
        IntStream.rangeClosed(1, 3).forEach(System.out::println);
        System.out.println("--------------------------------------------");
        toOtherDataStructure();
        System.out.println("--------------------------------------------");
        caseConvert();
        System.out.println("--------------------------------------------");
        List<Integer> l1 = pingFang();


        System.out.println("--------------------------------------------");
        flatMap();
        System.out.println("--------------------------------------------");
        printAll(l1);
    }

    /**
     * 流转化为其他数据类型
     * 一个 Stream 只可以使用一次,所以建议使用链式编程
     */
    private static void toOtherDataStructure() {

        //to array
        String[] arrs = list.stream().toArray(String[]::new);

        //2 Collection
        List<String> list1 = list.stream().collect(Collectors.toList());
        List<String> list2 = list.stream().collect(Collectors.toCollection(ArrayList::new));
        Set set1 = list.stream().collect(Collectors.toSet());
        Stack stack1 = list.stream().collect(Collectors.toCollection(Stack::new));
        // 3. String
        String str = list.stream().collect(Collectors.joining());
        System.out.println(str);

    }

    //----------用法--------------

    //1.转化大写
    private static List<String> caseConvert() {
        return list.stream().
                map(String::toUpperCase).
                collect(Collectors.toList());
    }


    //2.求平方
    private static List<Integer> pingFang() {
        return intValList.stream().
                map(e -> Integer.valueOf(e) * Integer.valueOf(e)).
                collect(Collectors.toList());
    }


    //3.flatMap
    //从上面例子可以看出，map 生成的是个 1:1 映射，每个输入元素，
    // 都按照规则转换成为另外一个元素。还有一些场景，是一对多映射关系的，这时需要 flatMap。
    //flatMap 把 input Stream 中的层级结构扁平化，就是将最底层元素抽出来放到一起，最终 output 的新 Stream 里面已经没有 List 了，都是直接的数字
    private static void flatMap() {

        /**
         * New List Val :[1]
         * New List Val :[2, 3]
         * New List Val :[4, 5, 6]
         * 这里打印的值其实是这样的一个二维数组
         * print(inputStream.collect(Collectors.toList()));
         */
        Stream<List<Integer>> inputStream = Stream.of(
                Arrays.asList(1),
                Arrays.asList(2, 3),
                Arrays.asList(4, 5, 6)
        );
        Stream<Integer> outputStream = inputStream.
                flatMap((childList) -> childList.stream());
        /**
         * New List Val :1
         * New List Val :2
         * New List Val :3
         * New List Val :4
         * New List Val :5
         * New List Val :6
         *
         * flatMap类似于addAll操作
         */
        print(outputStream.collect(Collectors.toList()));
    }


    //4.filter
    @Test
    public void testFilter() {
        Integer[] sixNums = {1, 2, 3, 4, 5, 6};
        Integer[] evens =
                Stream.of(sixNums).filter(n -> n % 2 == 0).toArray(Integer[]::new);
        print(Arrays.asList(evens));
        //只是此时原有元素的次序没法保证，并行的情况下将改变串行时操作的行为，
        //此时 forEach 本身的实现不需要调整，而 Java8 以前的 for 循环 code 可能需要加入额外的多线程逻辑。
        parallelPrint(Arrays.asList(evens));

    }


    /**
     * 5.forEach以及peek
     * 另外一点需要注意，forEach 是 terminal 操作，因此它执行后，Stream 的元素就被“消费”掉了，
     * 你无法对一个 Stream 进行两次 terminal 运算。下面的代码是错误的：
     * <p>
     * Stream  stream=list.stream();
     * stream.forEach(element -> doOneThing(element));
     * stream.forEach(element -> doAnotherThing(element));
     * 所以这边要用peek去解决
     * 记住forEach是一个Terminal操作
     */
    @Test
    public void forEach() {
        list.stream().filter(e -> e.length() > 3).peek(e -> System.out.println("Filtered Val :" + e))
                .map(String::toUpperCase)
                .peek(e -> System.out.println("Mapped Val :" + e))
                .collect(Collectors.toList());
    }


    /**
     * findFirst
     */
    @Test
    public void findFirst() {
        Optional<String> optional = list.stream().findFirst();
        // Java 8
        optional.ifPresent(System.out::println);
    }


    /**
     * 7.reduce
     * 这个方法的主要作用是把 Stream 元素组合起来。它提供一个起始值（种子），
     * 然后依照运算规则（BinaryOperator），和前面 Stream 的第一个、第二个、第 n 个元素组合。
     * 从这个意义上说，字符串拼接、数值的 sum、min、max、average 都是特殊的 reduce。例如 Stream 的 sum 就相当于
     * <p>
     * Integer sum = integers.reduce(0, (a, b) -> a+b); 或
     * <p>
     * Integer sum = integers.reduce(0, Integer::sum);
     * <p>
     * 也有没有起始值的情况，这时会把 Stream 的前面两个元素组合起来，返回的是 Optional。
     */

    @Test
    public void reduce() {
        // 字符串连接，concat = "ABCD"

        //identity the identity value for the accumulating function
        //identity 参数在不同的运算规则（BinaryOperator）为对应的初始值或参照值

        //或者可以使用Optional返回值

        String concat = Stream.of("A", "B", "C", "D").reduce("", String::concat);
        System.out.println(concat);
        // 求最小值，minValue = -3.0
        double minValue = Stream.of(-1.5, 1.0, -3.0, -2.0).reduce(Double.MAX_VALUE, Double::min);
        System.out.println(minValue);

        // 求和，sumValue = 10, 有起始值
        int sumValue = Stream.of(1, 2, 3, 4).reduce(0, Integer::sum);

        // 求和，sumValue = 10, 无起始值
        sumValue = Stream.of(1, 2, 3, 4).reduce(Integer::sum).get();
        System.out.println(sumValue);

        // 过滤，字符串连接，concat = "ace"
        concat = Stream.of("a", "B", "c", "D", "e", "F").
                filter(x -> x.compareTo("Z") > 0).
                reduce("", String::concat);
        System.out.println(concat);

    }


    /**
     *
     * 对 Stream 的排序通过 sorted 进行，它比数组的排序更强之处在于你可以首先对 Stream
     * 进行各类 map、filter、limit、skip 甚至 distinct 来减少元素数量后，再排序，这能帮助程序明显缩短执行时间
     */
    @Test
    public void sorted() {
        list.stream().limit(2).sorted().forEach(System.out::println);

        intValList.stream().distinct().forEach(System.out::println);
    }









    private static void printAll(List listing) {
        list.forEach(e -> System.out.println("String List Original Val :" + e));
        intValList.forEach(e -> System.out.println("Int List Original Val :" + e));
        listing.forEach(e -> System.out.println("New List Val :" + e));
    }


    private static void print(List listing) {
        listing.forEach(e -> System.out.println("New List Val :" + e));
    }


    private static void parallelPrint(List listing) {
        listing.parallelStream().forEach(e -> System.out.println("New List Val :" + e));
    }

}
