引言

> **异常就是程序运行过程中出现了不正常现象导致程序的中断，这些异常有的是因为用户错误引起，有的是程序错误引起的，还有其它一些是因为物理错误引起的**
>
> :rocket: **Java语言对异常的处理**
>
> Java语言在设计的当初就考虑到这些问题，提出异常处理的框架的方案，所有的异常都可以用一个异常类来表示，不同类型的异常对应不同的子类异常（目前我们所说的异常包括错误概念）
>
> 定义了异常处理的规范，在`JDK1.4`版本以后增加了异常链机制，从而便于跟踪异常
>
> Java异常是一个描述在代码段中发生异常的对象，当发生异常情况时，一个代表该异常的对象被创建并且在导致该异常的方法中被抛出，而该方法可以选择自己处理异常或者传递该异常

## 一、异常的体系结构详解

**Java把异常当作对象来处理，并定义一个基类`java.lang.Throwable`作为所有异常的超类**

**异常具有自己的语法和特定的继承结构,在Java API中已经定义了许多异常类**

> **Java异常层次结构图如下图所示**
>
> **如果 Java 中内置的异常类不能够满足需求，用户可以创建自己的异常类，Loki在下文有详细解释**

![img](https://s2.loli.net/2022/04/24/PzYUiy5RDMnhkE1.png)



从图中可以看出所有异常类型都是内置类`Throwable`的子类，因而`Throwable`在异常类的层次结构的顶层，`Throwable`分成了两个不同的分支

> **`Error`————不希望被程序捕获或者是程序无法处理的错误**

Error 类层次结构描述了 Java 运行时系统的内部错误和资源耗尽错误。例如，当JVM不再有继续执行操作所需的内存资源时，将出现 `OutOfMemoryError`。这些异常发生时，它们在应用程序的控制和处理能力之外，JVM应该做到通告给用户，并尽力使程序线程安全地终止

> **`Exception`————用户程序可能捕捉的异常情况或者说是程序可以处理的异常**

**在设计 Java 程序时，需要关注 `Exception` 层次结构**,这个层次结构又分解为两个分支

+ `RuntimeException`:由程序错误导致的异常 

+ `其他异常`:而程序本身没有问题，但由于像 I/O 错误这类问题导致的异常

## 二、Java内置异常类整理

Java 根据各个类库也定义了一些其他的异常，Loki会在这一小节详细整理

**注：对受检查异常和非受检查异常的解释**

> Java 语 言 规 范 将 **派 生 于 Error 类 或 RuntimeException 类的所有异常称为非受查**
> **( unchecked) 异常**， 所有其他的异常称为受查（checked) 异常,编译器将核查是否为所有的受査异常提供了异常处理器
>
> ——摘录自《Java核心技术 卷I 基础知识（原书第10版）》

> **注：对于Java中受检异常，必须进行预处理，否则编译报错，运行时异常（系统异常）不强制需要预处理，通过规范的代码可以避免产生这种异常**

> **对`受检异常`处理方法有两种：**
>
> - **当前方法知道如何处理该异常，则用try…catch块来处理该异常。**
> - **当前方法不知道如何处理，则在定义该方法时声明抛出该异常。**

### 非检查性异常

下面的表中列出了 Java 的非检查性异常。

| **异常**                        | **描述**                                                     |
| :------------------------------ | :----------------------------------------------------------- |
| ArithmeticException             | 当出现异常的运算条件时，抛出此异常。例如，一个整数"除以零"时，抛出此类的一个实例。 |
| ArrayIndexOutOfBoundsException  | 用非法索引访问数组时抛出的异常。如果索引为负或大于等于数组大小，则该索引为非法索引。 |
| ArrayStoreException             | 试图将错误类型的对象存储到一个对象数组时抛出的异常。         |
| ClassCastException              | 当试图将对象强制转换为不是实例的子类时，抛出该异常。         |
| IllegalArgumentException        | 抛出的异常表明向方法传递了一个不合法或不正确的参数。         |
| IllegalMonitorStateException    | 抛出的异常表明某一线程已经试图等待对象的监视器，或者试图通知其他正在等待对象的监视器而本身没有指定监视器的线程。 |
| IllegalStateException           | 在非法或不适当的时间调用方法时产生的信号。换句话说，即 Java 环境或 Java 应用程序没有处于请求操作所要求的适当状态下。 |
| IllegalThreadStateException     | 线程没有处于请求操作所要求的适当状态时抛出的异常。           |
| IndexOutOfBoundsException       | 指示某排序索引（例如对数组、字符串或向量的排序）超出范围时抛出。 |
| NegativeArraySizeException      | 如果应用程序试图创建大小为负的数组，则抛出该异常。           |
| NullPointerException            | 当应用程序试图在需要对象的地方使用 `null` 时，抛出该异常     |
| NumberFormatException           | 当应用程序试图将字符串转换成一种数值类型，但该字符串不能转换为适当格式时，抛出该异常。 |
| SecurityException               | 由安全管理器抛出的异常，指示存在安全侵犯。                   |
| StringIndexOutOfBoundsException | 此异常由 `String` 方法抛出，指示索引或者为负，或者超出字符串的大小。 |
| UnsupportedOperationException   | 当不支持请求的操作时，抛出该异常。                           |

### 检查性异常

下面的表中列出了 Java 定义在 java.lang 包中的检查性异常类。

| **异常**                   | **描述**                                                     |
| :------------------------- | :----------------------------------------------------------- |
| ClassNotFoundException     | 应用程序试图加载类时，找不到相应的类，抛出该异常。           |
| CloneNotSupportedException | 当调用 `Object` 类中的 `clone` 方法克隆对象，但该对象的类无法实现 `Cloneable` 接口时，抛出该异常。 |
| IllegalAccessException     | 拒绝访问一个类的时候，抛出该异常。                           |
| InstantiationException     | 当试图使用 `Class` 类中的 `newInstance` 方法创建一个类的实例，而指定的类对象因为是一个接口或是一个抽象类而无法实例化时，抛出该异常。 |
| InterruptedException       | 一个线程被另一个线程中断，抛出该异常。                       |
| NoSuchFieldException       | 请求的变量不存在                                             |
| NoSuchMethodException      | 请求的方法不存在                                             |

## 三、Java异常的处理机制

Java的异常处理本质上是**抛出异常**和**捕获异常**

> Java异常处理涉及到五个关键字，分别是：`try`、`catch`、`finally`、`throw`、`throws`。
>
> 下面将逐一介绍，通过认识这五个关键字，掌握基本异常处理知识。

+ **try**：用于监听。将要被监听的代码(可能抛出异常的代码)放在try语句块之内，当try语句块内发生异常时，异常就被抛出。
+ **catch**：catch用来捕获try语句块中发生的异常，匹配的原则是：如果抛出的异常对象属于`catch`子句的异常类，或者属于该异常类的子类，则认为生成的异常对象与`catch`块捕获的异常类型相匹配
+ **finally**：finally语句块总是会被执行。它主要用于回收在try块里打开的资源(如数据库连接、网络连接和磁盘文件)。只有finally块，执行完成之后，才会回来执行try或者catch块中的return或者throw语句，如果finally中使用了return或者throw等终止方法的语句，则就不会跳回执行，直接停止。
+ **throw**：用于抛出异常。
+ **throws**：用在方法签名中，用于声明该方法可能抛出的异常

****

### 捕获异常处理

**捕获异常通过try-catch来实现，如下所示**

**执行顺序，先执行try 在执行finally，最后执行return**

```java
		try {
            //1、对可能产生异常的代码进行监视
            //2、如果try代码块的某条语句产生了异常, 就立即跳转到catch子句执行, try代码块后面的代码不再执行
            //3、try代码块可能会有多个受检异常需要预处理, 可以通过多个catch子句分别捕获
        } catch (异常类型1 e1) {
            //捕获异常类型1的异常, 进行处理
            //在开发阶段, 一般的处理方式要么获得异常信息, 要么打印异常栈跟踪信息(e1.printStackTrace())
            //在部署后, 如果有异常, 一般把异常信息打印到日志文件中, 如：logger.error(e1.getMessage());
        } catch (异常类型2 e1) {
            //捕获异常类型2的异常, 进行处理
            //如果捕获的异常类型有继承关系, 应该先捕获子异常再捕获父异常
            //如果没有继承关系, catch子句没有先后顺序
        } finally {
            //不管是否产生了异常, finally子句总是会执行
            //一般情况下, 会在finally子句中释放系统资源
        }
```

> **try语句的一些说明**

`try`语句可以被嵌套。也就是说，一个`try`语句可以在另一个`try`块的内部。每次进入`try`语句，异常的前后关系都会被推入堆栈。如果一个内部的`try`语句不含特殊异常的`catch`处理程序，堆栈将弹出，下一个`try`语句的`catch`处理程序将检查是否与之匹配。这个过程将继续直到一个`catch`语句被匹配成功，或者是直到所有的嵌套`try`语句被检查完毕。如果没有`catch`语句匹配，Java运行时系统将处理这个异常

> **Catch语句的一些说明**

1、`catch（）{ }`语句括号中可以写当前准确类型的异常，也可以写父类型，这是一种多态的写法
2、catch可以写多个，建议写catch的时候，精确处理
3、catch的中异常的捕获粒度必须从小到大，因为它`先截获子异常`，`再截获父异常`，举一个例子

```java
try{   }
catch (IOException e) {       } 
catch (FileNotFoundException e) {  }	
```

分析：将IOException放到前面，IDE会题时报错，因为`IOException`是`FileNotFoundException`的父类，所以截获了IOException异常后，IOException的子异常都不会执行到，所以再次截获FileNotFoundException没有任何意义

> **final语句的一些说明**

1. finall语句块是一定执行的，不管是出现异常，还是没有出现异常，finally里的代码都执行，不管什么情况，有return也不行
2. 通常在finally语句块中实现资源的释放和关闭
3. `finally`子句是可选项，可以有也可以无，但是每个`try`语句至少需要一个`catch`或者`finally`子句
4. try里面执行退出虚拟机`System.exit();`，finally语句中的代码不执行

**Demo**

```java
static void func() {
        try {
            //创建输入流
            FileInputStream fis = new FileInputStream("E:\\loki的测试目录");
            //读取文件
            fis.read();
            //JDK8新特性
            System.out.println(100 / 0);//这是运行时异常
        } catch (ArithmeticException | NullPointerException e) {
            System.out.println("这是JDK8的新特性");
        } catch (FileNotFoundException e) {//多态 IOException e = new FileNotFoundException();
            System.out.println("文件不存在");
        } catch (IOException e) {
            System.out.println("读取异常");
        }
        //异常的捕获必须先捕获比较小的
    }

```

### 抛出异常处理

到目前为止，我们只是获取了被Java运行时系统引发的异常。然而，我们还可以用`throw`抛出明确的异常。`Throw`的语法形式如下

```java
throw ThrowableInstance;
```

`throw`抛出的异常需要被处理，除了可以用上面的`try-catch`语句处理，我们还可以使用**`throws`**语句将它抛出，请看下面这段代码

```java
class TestThrows{
    static void throw() throws IllegalAccessException {
        //抛出一个异常不做处理，在方法签名处抛出
        throw new IllegalAccessException("diy");
    }
    public static void main(String[] args){
        try {
            throw();
        }catch(IllegalAccessException e ){
         	//打印异常堆栈追踪信息是用异步线程的方式打印的
        	e.printStackTrace();
        }
    }
}
```

> `Throws`抛出异常的规则：

- 如果是不受检查异常（`unchecked exception`），即`Error`、`RuntimeException`或它们的子类，那么可以不使用`throws`关键字来声明要抛出的异常，编译仍能顺利通过，但在运行时会被系统抛出。
- 必须声明方法可抛出的任何检查异常（`checked exception`）。即如果一个方法可能出现受可查异常，要么用`try-catch`语句捕获，要么用`throws`子句声明将它抛出，否则会导致编译错误
- 仅当抛出了异常，该方法的调用者才必须处理或者重新抛出该异常。当方法的调用者无力处理该异常的时候，应该继续抛出，而不是囫囵吞枣。
- 调用方法必须遵循任何可查异常的处理和声明规则。若覆盖一个方法，则不能声明与覆盖方法不同的异常。声明的任何异常必须是被覆盖方法所声明异常的同类或子类。

> **throw 和 throws 的区别**

**throw**

+ throw 语句用在方法体内，表示抛出异常，由方法体内的语句处理。

+ throw是具体向外抛出异常的动作，所以它抛出的是一个异常实例，执行throw一定是抛出了某种异常。

+ throw一般用于抛出自定义异常。

**throws**

+ throws语句是用在方法声明后面，表示如果抛出异常，由该方法的调用者来进行异常的处理
+ throws主要是声明这个方法会抛出某种类型的异常，让它的使用者要知道需要捕获的异常的类型
+ throws表示出现异常的一种可能性，并不一定会发生这种异常

> **方法重写时异常的处理事项**

==**子类方法的异常要比父类方法的异常更小**==

+ 如果父类方法没有抛出异常，子类重写后也不能抛出异常
+ 如果父类方法抛出了异常，子类方法可以抛出相同的异常，也可以抛出父类异常的子异常，也可以不抛出异常

## 四、异常中常用的方法

> 下面的列表是 Throwable 类的主要方法

| **序号** | **方法及说明**                                               |
| :------- | :----------------------------------------------------------- |
| 1        | **public String getMessage()** 返回关于发生的异常的详细信息。这个消息在Throwable 类的构造函数中初始化了。 |
| 2        | **public Throwable getCause()** 返回一个 Throwable 对象代表异常原因。 |
| 3        | **public String toString()** 返回此 Throwable 的简短描述。   |
| 4        | **public void printStackTrace()** 将此 Throwable 及其回溯打印到标准错误流。。 |
| 5        | **public StackTraceElement [] getStackTrace()** 返回一个包含堆栈层次的数组。下标为0的元素代表栈顶，最后一个元素代表方法调用堆栈的栈底。 |
| 6        | **public Throwable fillInStackTrace()** 用当前的调用栈层次填充Throwable 对象栈层次，添加到栈层次任何先前信息中。 |

>
> **使用异常对象的getMessage()方法，通常用于打印日志时**
>
> **使用异常对象的printStackTrace()方法，比较适合于程序调试阶段**

## 五、异常链

异常链顾名思义就是将异常发生的原因一个传一个串起来，即把底层的异常信息传给上层，这样逐层抛出。 Java API文档中给出了一个简单的模型：

```java
try {   
    lowLevelOp();   
} catch (LowLevelException le) {   
    throw (HighLevelException) new HighLevelException().initCause(le);   
}
```

当程序捕获到了一个底层异常，在处理部分选择了继续抛出一个更高级别的新异常给此方法的调用者。 这样异常的原因就会逐层传递。这样，位于高层的异常递归调用getCause()方法，就可以遍历各层的异常原因。 这就是`Java异常链`的原理。异常链的实际应用很少，发生异常时候逐层上抛不是个好注意，上层拿到这些异常又能奈之何？而且异常逐层上抛会消耗大量资源,因为要保存一个完整的异常链信息

## 六、自定义异常

> **实际开发中我们会经常用到自定义异常**

用户自定义异常类，必须继承`Exception`或者`RuntimeException`，但是我们一般遵守以下的规范

1. 一般地，用户自定义异常类都是 **RuntimeException 的子类**；
2. 自定义异常类通常需要编写几个**重载的构造器**
3. 自定义异常最重要的是异常类的名字，自定义异常类一般都是以Exception结尾，说明该类是一个异常类，当异常出现时，可以根据名字判断异常类型

> 案例

在程序中使用自定义异常类，大体可分为以下几个步骤:

- 创建自定义异常类
- 在方法中通过`throw`关键字抛出异常对象
- 如果在当前抛出异常的方法中处理异常，可以使用`try-catch`语句捕获并处理；否则在方法的声明处通过`throws`关键字指明要抛出给方法调用者的异常，继续进行下一步操作
- 在出现异常方法的调用者中捕获并处理异常

```java
public class XXXExcepiton extends Exception | RuntimeException{
        添加一个空参数的构造方法
        添加一个带异常信息的构造方法
}
```



> **最佳实践**

+ 自定义异常体系时，推荐从`RuntimeException`派生"根异常"，再派生出业务异常

+ 自定义异常时，应该提供多种构造方法。

  ```java
  public class BaseException extends RuntimeException {
      public BaseException() {
          super();
      }
  
      public BaseException(String message, Throwable cause) {
          super(message, cause);
      }
  
      public BaseException(String message) {
          super(message);
      }
  
      public BaseException(Throwable cause) {
          super(cause);
      }
  }
  ```

## 七、总结与面试

### 总结

1.处理运行时异常时，采用逻辑去合理规避同时辅助try-catch处理
2.在多重catch块后面,可以加一个catch ( Exception )来处理可能会被遗漏的异常
3.对于不确定的代码,也可以加上try-catch ,处理潜在的异常
4.尽量去处理异常,切忌只是简单的调用printStackTrace(去打印输出
5.具体如何处理异常，要根据不同的业务需求和异常类型去决定
6.尽量添加finally语句块去释放占用的资源

### 面试题

当try语句中有return的时候，其finally语句的执行状况

> 结论:try中有return, 会先将值暂存，无论finally语句中对该值做什么处理，最终返回的都是try语句中的暂存值

**这段代码的返回值为100**

```java
public static int func(){
    int i = 100;
    try {
        return i;
    }finally {
        i++;
    }
}
```

> **调用下面的方法，得到的返回值是什么？**

```java
public static int getNum() {
        try {
            int a = 1 / 0;
            return 1;
        } catch (Exception e) {
            return 2;
        } finally {
            return 3;
        }
    }
```

**答案为3，分析如下**

代码走到第 3 行的时候遇到了一个MathException，这时第 4 行的代码就不会执行了，代码直接跳转到catch语句中，走到第 6 行的时候，
异常机制有一个原则：如果在catch中遇到了return或者异常等能使该函数终止的话那么有finally就必须先执行完finally代码块里面的代码然后再返回值。
因此代码又跳到第 8 行，可惜第 8 行是一个return语句，那么这个时候方法就结束了，因此第 6 行的返回结果就无法被真正返回。
如果finally仅仅是处理了一个释放资源的操作，那么该道题最终返回的结果就是2，因此上面这道题返回值是



**参考资料**

> [JDK specification————Exceptions](https://docs.oracle.com/javase/specs/jls/se17/html/jls-11.html)
>
> 《Java核心技术 卷I 基础知识（原书第10版）》