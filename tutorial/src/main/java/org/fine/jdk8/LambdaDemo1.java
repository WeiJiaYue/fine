package org.fine.jdk8;

import java.util.*;

/**
 *
 *
 * --------------lambda表达式的用法---------------
 */
public class LambdaDemo1 {


    /**
     * ()包括的就是参数列表，但这里无参
     * System.out.println("Hello World!!!")是函数体
     * <p>
     * 1.没有参数时:一定要加上() {@link #noArgs()}
     * 2.有一个参数时:   {@link #oneArgs()}
     * a.如果写了参数类型，就需要加()
     * b.如果没有写参数类型，可以不加()
     * <p>
     * 3.有两个参数时：一定需要()；当参数需要加修饰符(如final修饰)或者标签时，
     * 参数则需要加上完整的参数类型，否则，可以不加完整的参数类型。{@link #twoArgs()}
     * <p>
     * 4.函数体
     * a.函数体只有一行，可以省略{}，需要返回值时，return也可以省略
     * b.多行时则需要{}，需要返回值时，return也不可以省略  {@link #functionBody()}
     * <p>
     * 5.Lambda表达式中的变量
     * a.参数 Arrays.asList("b","a","c","d").sort((s1,s2)->s1.compareTo(s2)); s1,s2就是Labmbda表达式的参数
     * b.局部变量和自由变量 {@link #variable(int, String)}
     * <p>
     * 6.lambda表达式方法引用  {@link #ref()}
     * <p>
     * 7.构造方法引用
     * 形式：类::new  构造方法引用，接口需要有一个无参的并且一定有返回值。
     */
    public static void main(String[] args) {
        Thread t = new Thread(new Runnable() {
            public void run() {
                System.out.println("hello world at inner class");
            }
        }, "common");
        t.start();
        System.out.println(t.getName());
        noArgs();

        oneArgs();

        twoArgs();

        functionBody();
        System.out.println("-------------构造方法引用---------------");
        forEach();

    }


    public static void noArgs() {
        Thread t2 = new Thread(() -> System.out.println("hello world at lambda"), "lambda");
        t2.start();
        System.out.println(t2.getName());
    }

    private static void oneArgs() {
        List<String> list = Arrays.asList("qwe", "asd", "zxc");
        //a.如果写了参数类型，就需要加()
        list.forEach((String s) -> System.out.println(s));
        System.out.println();
        //b.如果没有写参数类型，可以不加()
        list.forEach(s -> System.out.println(s));
        System.out.println();
        //c.
        list.forEach(System.out::println);

    }

    /**
     * 3.有两个参数时：一定需要()；当参数需要加修饰符(如final修饰)或者标签时，
     * 参数则需要加上完整的参数类型，否则，可以不加完整的参数类型。{@link #twoArgs()}
     */
    private static void twoArgs() {
        List<String> list = Arrays.asList("qwe", "asd", "zxc", "123");
        list.sort((final String s1, final String s2) -> s1.compareTo(s2));
        list.sort((String s1, String s2) -> s1.compareTo(s2));
        list.sort((s1, s2) -> s1.compareTo(s2));
        list.sort((String::compareTo));

        list.sort(Comparator.naturalOrder());
        list.forEach(s -> System.out.println("val:" + s));
    }

    private static void functionBody() {
        List<String> list = Arrays.asList("qwe", "asd", "zxc", "123");
        //函数体在一 函数体为(final String s1, final String s2) -> s1.compareTo(s2)
        list.sort((final String s1, final String s2) -> s1.compareTo(s2));

        //多行时则需要{}，需要返回值时，return也不可以省略
        list.sort((s1, s3) -> {
            int result = s1.compareTo(s3);
            return result;
        });

    }


    /**
     * for()中的i就是局部变量
     * int times, String str既不是参数，也不是局部变量，而是自由变量
     * 自由变量在Lambda表达式中不能修改。如果是采用内明内部类的形式，内部类想用使用方法的参数int times, String str，是需要加上final的。
     *
     * @param times
     * @param str
     */
    private static void variable(final int times, final String str) {
        Runnable runnable = () -> {
            for (int i = 0; i < times; i++) {
                System.out.println(str);
            }
        };
        new Thread(runnable).start();

    }


    /**
     * 方法引用形式：
     * 类 ::静态方法
     * 对象::方法
     * 对象::静态方法
     */
    private static void ref() {
        Arrays.asList("b", "a", "c", "d").forEach(System.out::println);
        Arrays.asList("b", "a", "c", "d").sort(String::compareTo);
    }


    private static void forEach() {
        LambdaDemo1 ld = new LambdaDemo1();

        List<String> list = ld.asList(LinkedList::new, "1", "2");

        System.out.println("list type is "+list.getClass());

        list.sort(String::compareTo);
        list.stream().forEach(System.out::println);
    }


    private static <T> List<T> asList(ICreattor<List<T>> creattor, T... ts) {
        List<T> list = creattor.create();
        for (T a : ts)
            list.add(a);
        return list;
    }

    @FunctionalInterface
    interface ICreattor<T extends List<?>> {
        T create();
    }

}
