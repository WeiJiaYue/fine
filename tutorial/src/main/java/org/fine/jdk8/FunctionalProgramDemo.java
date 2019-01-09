package org.fine.jdk8;


/**
 * --------------函数式编程的用法---------------
 * Functional接口就是只有一个抽象方法的接口。
 * 注意是只有一个抽象方法，不是只有一个方法。
 * 也就是说Functional接口除了一个抽象方法外，还可以有默认方法和静态方法
 */


public class FunctionalProgramDemo {

    public static void main(String[] args) {
        Frog frog = new Frog();
        frog.run("我是青蛙，我会跳");
        frog.breathe();


        System.out.println("-----------");
        Frog2 frog2 = new Frog2();
        frog2.run("我是青蛙，我会跳");
        frog2.breathe();

        System.out.println("-----------");
        Frog3 frog3 = new Frog3();
        frog3.run("我是青蛙，我会跳");
        frog3.breathe();


        /**
         * 4.函数式编程，那么就可以使用Lambda表达式来表示该接口的一个实现(注：JAVA 8 之前一般是用匿名类实现的)：
         */
        System.out.println("-----------");
        IWaterAnimal fish = (String s) -> System.out.println("fish:" + s);
        fish.run("会游动");

    }


    /**
     * 1.默认方法  默认方法就是在接口中定义一个方法用default修饰
     * 2.解决接口中默认方法冲突 青蛙不仅可以在水中，还可以在陆地，再定义一个陆地动物接口ILandAnimal
     * 当实现的接口中默认方法冲突时，要通过接口名.super.方法名的方式来指定方法。或者重写
     * 3.父类方法与接口中默认方法相同的时候，默认调用的父类中的方法。也就是说不要试图通过接口的默认方法来覆盖Object类的方法。
     */

    @FunctionalInterface
    interface IWaterAnimal {
        void run(String a);


        default void breathe() {
            System.out.println("可以在水中呼吸");
        }
    }

    @FunctionalInterface
    interface ILandAnimal {
        void run(String s);

        default void breathe() {
            System.out.println("可以在空气中呼吸");
        }
    }


    /**
     * 青蛙类
     */
    static class Frog implements IWaterAnimal {
        @Override
        public void run(String a) {
            System.out.println(a);
        }
    }


    /**
     * 青蛙类
     */
    static class Frog2 implements IWaterAnimal, ILandAnimal {
        @Override
        public void run(String a) {
            System.out.println(a);
        }


        @Override
        public void breathe() {
            ILandAnimal.super.breathe();
        }
    }


    /**
     * 青蛙类
     */
    static class Frog3 implements ILandAnimal {
        @Override
        public void run(String a) {
            System.out.println(a);
        }


        @Override
        public void breathe() {
            System.out.println("都会");
        }
    }
}
