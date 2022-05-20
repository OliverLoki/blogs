理解函数式接口是学习Lanbda表达式的关键

> **函数式接口**

+ 任何接口，如果只包含唯一一个抽象方法，那么它就是一个函数式接口
+ 对于函数式接口，我们可以通过Lambda表达式来创建该接口的对象

> **以Runnable接口为例**

Jdk11 `Runnable`源码

```java
@FunctionalInterface
public interface Runnable {
    public abstract void run();
}

```

使用匿名内部类的方式创建一个线程

```java
new Thread(new Runnable() {
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName());
    }
}).start();
```

使用Lambda表达式——简化了匿名内部类的使用

```java
new Thread(()->{
    System.out.println(Thread.currentThread().getName());
}).start();
```

> **lambda表达式的重要特征**

- **可选类型声明：**不需要声明参数类型，编译器可以统一识别参数值。
- **可选的参数圆括号：**一个参数无需定义圆括号，但多个参数需要定义圆括号。
- **可选的大括号：**如果主体包含了一个语句，就不需要使用大括号。
- **可选的返回关键字：**如果主体只有一个表达式返回值则编译器会自动返回值，大括号需要指定表达式返回了一个数值

```
语法格式 (参数类型 参数名称) -> { 代码体 }
```

```java
public class Lambda表达式 {
    //操作接口
    interface MathOperation {
        int operation(int a, int b);
    }

    private int operate(int a, int b, MathOperation mathOperation) {
        return mathOperation.operation(a, b);
    }

    public static void main(String[] args) {
        Lambda表达式 tester = new Lambda表达式();

        // 类型声明
        MathOperation addition = (int a, int b) -> a + b;

        // 不用类型声明
        MathOperation subtraction = (a, b) -> a - b;

        // 大括号中的返回语句
        MathOperation multiplication = (int a, int b) -> {
            return a * b;
        };

        // 没有大括号及返回语句
        MathOperation division = (int a, int b) -> a / b;

        System.out.println("10 + 5 = " + tester.operate(10, 5, addition));
        System.out.println("10 - 5 = " + tester.operate(10, 5, subtraction));
        System.out.println("10 x 5 = " + tester.operate(10, 5, multiplication));
        System.out.println("10 / 5 = " + tester.operate(10, 5, division));
    }
}
```







