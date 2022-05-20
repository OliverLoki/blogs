# Java基础

## 一、面向对象和面向过程





## 二、Java语言的特点









## 三、JDK、JRE、JVM

图源：[JavaSE白皮书](https://www.oracle.com/cn/java/technologies/documentation/whitepapers-jsp.html)

![image-20220421163125577](https://s2.loli.net/2022/04/21/m9oJynLMwVe4Q8h.png)

## 四、Java和C++



## 五、聊一聊Java关键字

> final



> this



> static



> super



## 六、重写和重载

> **重载：**
>
> + 重载就是同一个类中多个同名方法根据不同的传参来执行不同的逻辑处理
>
> + 返回值无法造成重载

> **重写：**
>
> 重写发生在运行期，是子类对父类的允许访问的方法的实现过程进行重新编写。
>
> 1. 返回值类型、方法名、参数列表必须相同，抛出的异常范围小于等于父类，访问修饰符范围大于等于父类。
> 2. 如果父类方法访问修饰符为 `private/final/static` 则子类就不能重写该方法，但是被 static 修饰的方法能够被再次声明。
> 3. 构造方法无法被重写

综上：重写就是子类对父类方法的重新改造，外部样子不能改变，内部逻辑可以改变

## 七、聊一下String、StringBuffer、StringBuilder

+ 在 Java 9 之后，String 、`StringBuilder` 与 `StringBuffer` 的实现改用 byte 数组存储字符串 `private final byte[] value`

+ `StringBuilder` 与 `StringBuffer` 都继承自 `AbstractStringBuilder` 类，在 `AbstractStringBuilder` 中也是使用字符数组保存字符串`char[]value` 但是没有用 `final` 关键字修饰，所以这两种对象都是可变的。

+ `StringBuilder` 与 `StringBuffer` 的构造方法都是调用父类构造方法也就是 `AbstractStringBuilder` 实现的

  源码

  ```java
  AbstractStringBuilder.java
  abstract class AbstractStringBuilder implements Appendable, CharSequence {
      /**
       * The value is used for character storage.
       */
      char[] value;
  
      /**
       * The count is the number of characters used.
       */
      int count;
  
      AbstractStringBuilder(int capacity) {
          value = new char[capacity];
      }
  ```

+ **线程安全性**

  `String` 中的对象是不可变的，也就可以理解为常量，线程安全。`AbstractStringBuilder` 是 `StringBuilder` 与 `StringBuffer` 的公共父类，定义了一些字符串的基本操作，如 `expandCapacity`、`append`、`insert`、`indexOf` 等公共方法。`StringBuffer` 对方法加了同步锁或者对调用的方法加了同步锁，所以是线程安全的。`StringBuilder` 并没有对方法进行加同步锁，所以是非线程安全的。

+ **性能**

  每次对 `String` 类型进行改变的时候，都会生成一个新的 `String` 对象，然后将指针指向新的 `String` 对象。`StringBuffer` 每次都会对 `StringBuffer` 对象本身进行操作，而不是生成新的对象并改变对象引用。相同情况下使用 `StringBuilder` 相比使用 `StringBuffer` 仅能获得 10%~15% 左右的性能提升，但却要冒多线程不安全的风险。

+ **对于三者使用的总结：**

  1. 操作少量的数据: 适用 `String`
  2. 单线程操作字符串缓冲区下操作大量数据: 适用 `StringBuilder`
  3. 多线程操作字符串缓冲区下操作大量数据: 适用 `StringBuffer`

## 八、静态方法内调用一个非静态成员为什么非法?

类的静态成员(变量或方法)属于类本身，在类加载的时候就会分配内存，可以通过类名直接访问
非静态成员(变量或方法)属于类的对象，只有在类的对象产生(实例化)时才会分配内存，然后通过类的对象(实例)去访问
所以，如果一个类的静态方法去调用非静态方法或变量的时候，因为类的静态方法存在的时候，类的非静态成员可能不存在，访问一个内存中不存在的东西当然会出错

## 九、接口和抽象类

> **设计层面的区别**

**抽象类是对一种事物的抽象，即对类抽象，而接口是对行为的抽象**

> **语法层面上的区别**

1. 接口的方法默认是 `public`，所有方法在接口中不能有实现(Java 8 开始接口方法可以有默认实现），而抽象类可以有非抽象的方法
2. 接口方法默认修饰符是 `public`，抽象方法可以有 `public`、`protected` 和 `default` 这些修饰符（抽象方法就是为了被重写所以不能使用 `private` 关键字修饰！）。
3. 接口中不能含有静态代码块以及静态方法，而抽象类可以有静态代码块和静态方法
4. 一个类可以实现多个接口，但只能实现一个抽象类。接口自己本身可以通过 `extends` 关键字扩展多个接口

## 十、成员变量和局部变量的区别

1. 从语法形式上看:成员变量是属于类的，而局部变量是在方法中定义的变量或是方法的参数；成员变量可以被 `public`,`private`,`static` 等修饰符所修饰，而局部变量不能被访问控制修饰符及 `static` 所修饰；但是，成员变量和局部变量都能被 `final` 所修饰。
2. 从变量在内存中的存储方式来看:如果成员变量是使用`static`修饰的，那么这个成员变量是属于类的，如果没有使用`static`修饰，这个成员变量是属于实例的。对象存于堆内存，如果局部变量类型为基本数据类型，那么存储在栈内存，如果为引用数据类型，那存放的是指向堆内存对象的引用或者是指向常量池中的地址。
3. 从变量在内存中的生存时间上看:成员变量是对象的一部分，它随着对象的创建而存在，而局部变量随着方法的调用而自动消失。
4. 成员变量如果没有被赋初值:则会自动以类型的默认值而赋值（一种情况例外:被 `final` 修饰的成员变量也必须显式地赋值），而局部变量则不会自动赋值

## 十一、Java中为什么只有值传递



总结一下 Java 中方法参数的使用情况：

- 一个方法不能修改一个基本数据类型的参数（即数值型或布尔型）
- 一个方法可以改变一个对象参数的状态
- 一个方法不能让对象参数引用一个新的对象

## 十二、equals和hashCode





## 十三、简单聊一下线程、程序、进程?

> **基本概念**

+ **线程**与进程相似，但线程是一个比进程更小的执行单位。一个进程在其执行的过程中可以产生多个线程。与进程不同的是同类的多个线程共享同一块内存空间和一组系统资源，所以系统在产生一个线程，或是在各个线程之间作切换工作时，负担要比进程小得多，也正因为如此，线程也被称为轻量级进程。
+ **程序**是含有指令和数据的文件，被存储在磁盘或其他的数据存储设备中，也就是说程序是静态的代码。
+ **进程**是程序的一次执行过程，是系统运行程序的基本单位，因此进程是动态的。系统运行一个程序即是一个进程从创建，运行到消亡的过程。简单来说，一个进程就是一个执行中的程序，它在计算机中一个指令接着一个指令地执行着，同时，每个进程还占有某些系统资源如 CPU 时间，内存空间，文件，输入输出设备的使用权等等。换句话说，当程序在执行时，将会被操作系统载入内存中。 线程是进程划分成的更小的运行单位。线程和进程最大的不同在于基本上各进程是独立的，而各线程则不一定，因为同一进程中的线程极有可能会相互影响。从另一角度来说，进程属于操作系统的范畴，主要是同一段时间内，可以同时执行一个以上的程序，而线程则是在同一程序内几乎同时执行一个以上的程序段。



## 十四、简单聊一下Java序列化

1. 所有需要网络传输的对象都需要实现序列化接口，通过建议所有的javaBean都实现Serializable接口。
2. 对象的类名、实例变量（包括基本类型，数组，对其他对象的引用）都会被序列化；方法、类变量、transient实例变量都不会被序列化。
3. 如果想让某个变量不被序列化，使用transient修饰
4. 序列化对象的引用类型成员变量，也必须是可序列化的，否则，会报错。
5. 反序列化时必须有序列化对象的class文件
6. 当通过文件、网络来读取序列化后的对象时，必须按照实际写入的顺序读取。
7. 单例类序列化，需要重写readResolve()方法；否则会破坏单例原则。
8. 同一对象序列化多次，只有第一次序列化为二进制流，以后都只是保存序列化编号，不会重复序列化。
9. 建议所有可序列化的类加上serialVersionUID 版本号，方便项目升级

## 十五、获取用键盘输入常用的两种方法

方法 1：通过 Scanner

```java
Scanner input = new Scanner(System.in);
String s  = input.nextLine();
input.close();Copy to clipboardErrorCopied
```

方法 2：通过 BufferedReader

```java
BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
String s = input.readLine();
```































