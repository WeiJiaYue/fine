package org.fine.jdk8;

import java.util.List;
import java.util.function.Predicate;

/**
 *  --------------jdk1.8函数式编程接口---------------
 */
public class FunctionalProgramSupport {


    private static void eval(List<Integer> list, Predicate<Integer> predicate) {
        list.stream().filter(predicate).forEach(System.out::println);
    }
}
