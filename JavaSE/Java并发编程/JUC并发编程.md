**引言——为什么需要多线程**

> 多核 CPU 时代意味着多个线程可以同时运行，多线程减少了线程上下文切换的开销。现在的系统动不动就要求百万级甚至千万级的并发量，而多线程并发编程正是开发高并发系统的基础，利用好多线程机制可以大大提高系统整体的并发能力以及性能

**:rocket:执行以下代码来获取当前电脑的CPU的核数**

```java
public static void main(String[] args) {
    int num = Runtime.getRuntime().availableProcessors();
    System.out.println("当前电脑CPU核数：" + num);
}
```



@[TOC]



# 一、需要掌握的前置概念与知识

**Loki建议：在学习之前最好对以下内容有一定了解**

> + 良好的Java基础，最好对函数式编程、lambda 有一定了解
> + 操作系统中的理论知识，如进程并发控制，PV操作，进程调度算法，死锁与死锁避免

## 线程与进程

> **何为进程**

+ **进程由`程序`、`数据集`、`PCB(进程控制块)`构成，是程序的一次执行过程，是系统运行程序的基本单位**

+ **进程是拥有系统资源的一个基本单位**

在 Java 中，当我们启动 main 函数时其实就是启动了一个 JVM 的进程，而 main 函数所在的线程就是这个进程中的一个线程，也称主线程



在 windows 中通过查看任务管理器的方式，我们就可以清楚看到 window 当前运行的进程

![image-20220501174325791](https://s2.loli.net/2022/05/01/1il7n8p4aOmsSK6.png)



> **何为线程**

为减少程序并发执行的开销，使得并发粒度更细，并发性能更好。一些操作系统引入了线程这个概念，将进程两个功能分开

+ `独立分配资源`->仍由进程负责
+ `调度分派执行`->交给线程实体负责

:rabbit:**线程是系统最小调度单位**

Java 程序天生就是多线程程序，我们可以通过 JMX 来看一下一个普通的 Java 程序有哪些线程，代码如下

```java
/**
 * @author oliverloki
 * @Description: TODO
 * @date 2022年05月01日 17:32
 */
public class Java线程管理 {
    public static void main(String[] args) {
        // 获取 Java 线程管理 MXBean
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        // 不需要获取同步的 monitor 和 synchronizer 信息，仅获取线程和线程堆栈信息
        ThreadInfo[] threadInfos = threadMXBean.dumpAllThreads(false, false);
        // 遍历线程信息，仅打印线程 ID 和线程名称信息
        for (ThreadInfo threadInfo : threadInfos) {
            System.out.println("[" + threadInfo.getThreadId() + "] " + threadInfo.getThreadName());
        }
    }
}
```

输出结果如下，输出内容可能不同

```java
[1] main //main 线程,程序入口
[2] Reference Handler //清除 reference 线程
[3] Finalizer //调用对象 finalize 方法的线程
[4] Signal Dispatcher // 分发处理给 JVM 信号的线程
[5] Attach Listener //添加事件
[11] Common-Cleaner
[12] Monitor Ctrl-Break
```

## 从 JVM 角度说进程和线程之间的关系

> 一个进程在其执行的过程中可以产生多个线程。与进程不同的是同类的多个线程共享进程的**堆**和**方法区**资源，但每个线程有自己的**程序计数器**、**虚拟机栈**和**本地方法栈**，所以系统在产生一个线程，或是在各个线程之间作切换工作时，负担要比进程小得多，也正因为如此，线程也被称为轻量级进程。

+ **如果想学习JVM内存结构请移步[JVM内存结构详解](https://blog.csdn.net/Night__breeze/article/details/124276389?spm=1001.2014.3001.5501)**

+ **JVM虚拟机进程在Java在运行过程中向系统申请、分配内存，保证了JVM的高效稳定运行。Java虚拟机在执行Java程序的过程中会把它所管理的内存划分为若干个不同的数据区域。这些区域有各自的用途，以及创建和销毁的时间，有的区域随着虚拟机进程的启动而一直存在，有些区域则是依赖用户线程的启动和结束而建立和销毁**

  原文链接：https://blog.csdn.net/Night__breeze/article/details/124276389

从下图可以看出：一个进程中可以有多个线程

多个线程共享进程的`堆`和`方法区` (JDK1.8 之后的元空间)资源，但是每个线程有自己的`程序计数器`、`虚拟机栈` 和 `本地方法栈`

![](https://img-blog.csdnimg.cn/img_convert/f52ae24966597ea21877df94b5f1d575.png)



> 并发和并行的区别

- **并发：** 同一时间段，多个任务都在执行 (单位时间内不一定同时执行)；
- **并行：** 单位时间内，多个任务同时执行

**简单来说，对于CPU的一个核，想要处理多个任务，就需要时间片轮转来处理任务，这就是并发，对于多个核，则可以同时执行这多个任务，这就是并行**

# 二、Java实现多线程

## 三种方式定义线程

> **在*java*中要想实现多线程有以下手段**

1. 继承`Thread`类——将任务和线程合并在一起
2. 实现`Runnable`接口——将任务和线程分开了
3. 实现`Callable`接口，并与Future、线程池结合使用，这部分在下文JUC中详解

```java
@Slf4j
class T extends Thread {
    @Override
    public void run() {
        log.info("我是继承Thread的任务");
    }
}
@Slf4j
class R implements Runnable {

    @Override
    public void run() {
        log.info("我是实现Runnable的任务");
    }
}
@Slf4j
class C implements Callable<String> {

    @Override
    public String call() throws Exception {
        log.info("我是实现Callable的任务");
        return "success";
    }
}
```

> **Thread实现任务的局限性**

1. 任务逻辑写在Thread类的run方法中，有单继承的局限性
2. 创建多线程时，每个任务有成员变量时不共享，必须加static才能做到共享

**Runnable和Callable解决了Thread的局限性**

> **Runbale相比Callable有以下的局限性**

1. 任务没有返回值
2. 任务无法抛异常给调用方

## 启动线程

**通过调用线程的 start() 方法来启动一个线程**

> 以实现`Runnable`接口为例

**Runnable是一个函数式接口**

+ **任何接口，如果只包含唯一一个抽象方法，那么它就是一个函数式接口**

Jdk11中源码如下

```java
@FunctionalInterface
public interface Runnable {
    public abstract void run();
}
```

实现一个线程并启动

```java
Thread t = new Thread(new Runnable() {
    @Override
    public void run() {
        System.out.println("我是Runnable的lambda简化后的任务");
    }
});
//启动线程
t.start();
```

对于函数式接口，我们可以使用`Lambda`表达式来定义并启动线程，代码如下

```java
//创建一个线程实例
new Thread(()->{
       System.out.println(Thread.currentThread().getName());
}).start();
```



# 三、Thread类学习

## 方法列表

```java
static Thread currentThread() 返回对当前正在执行的线程对象的引用。 
long getId()返回该线程的标识符。 
String getName()返回该线程的名称。 
int getPriority() 返回线程的优先级。 
void interrupt() 中断线程。 
boolean isAlive()测试线程是否处于活动状态。 
void join()等待该线程终止。 
void join(long millis)等待该线程终止的时间最长为 millis 毫秒。 
void join(long millis, int nanos)等待该线程终止的时间最长为 millis 毫秒 + nanos 纳秒。 
void setDaemon(boolean on) 将该线程标记为守护线程或用户线程
void setPriority(int newPriority)更改线程的优先级。 
static void sleep(long millis)在指定的毫秒数内让当前正在执行的线程休眠（暂停执行），此操作受到系统计时器和调度程序精度和准确性的影响。 
static void sleep(long millis, int nanos)在指定的毫秒数加指定的纳秒数内让当前正在执行的线程休眠（暂停执行），此操作受到系统计时器和调度程序精度和准确性的影响。 
void start() 使该线程开始执行；Java 虚拟机调用该线程的 run 方法。 
static void yield()暂停当前正在执行的线程对象，并执行其他线程。
```

## Thread类常用方法

### start()与run()方法

+ start()用来启动一个线程，当调用start方法后，系统才会开启一个新的线程来执行用户定义的子任务，在这个过程中，会为相应的线程分配需要的资源

+ run()方法是不需要用户来调用的，当通过start方法启动一个线程之后，当线程获得了CPU执行时间，便进入run方法体去执行具体的任务。注意，继承Thread类必须重写run方法，在run方法中定义具体要执行的任务

### sleep()与wait()方法

> **Sleep方法定义**

+ sleep()使当前线程进入停滞状态（阻塞当前线程），让出CUP的使用权，目的是不让当前线程独自霸占该进程所获的CPU资源，以留一定时间给其他线程执行的机会;
+ sleep()是Thread类的Static(静态)的方法，因此不能改变对象的锁
+ 在sleep()休眠时间期满后，该线程不一定会立即执行，这是因为其它线程可能正在运行而且没有被调度为放弃执行，除非此线程具有更高的优先级

```java
public static native void sleep(long millis) throws InterruptedException; 
```

**Thred.sleep()方法示例**

```java
try {
    // 该方法会抛出 InterruptedException异常 即休眠过程中可被中断，被中断后抛出异常
    //休眠1秒
    Thread.sleep(1000);
} catch (InterruptedException e) {
    e.printStackTrace(); 
}
```

**实际开发中使用TimeUnit的api可替代 Thread.sleep** 

```java
try {
   TimeUnit.SECONDS.sleep(1);
} catch (InterruptedException e) {
    e.printStackTrace(); 
}
```

> **Wait()方法定义**

+ wait()方法是Object类里的方法，当一个线程执行到wait()方法时，它就进入到一个和该对象相关的等待池中，同时释放对象的锁（暂时失去机锁，超时时间到后还需要返还对象锁）
+ wait()使用notify或者notifyAll或者指定睡眠时间来唤醒当前等待池中的线程
+ wiat()必须放在synchronized block中，否则会在program runtime时扔出`java.lang.IllegalMonitorStateException` 异常



> **sleep()和wait()方法的区别**

+ **两者都可以暂停线程的执行**

- 两者最主要的区别在于：**`sleep()` 方法没有释放锁，而 `wait()` 方法释放了锁** 。

+ `wait()` 通常被用于线程间交互/通信，`sleep()`通常被用于暂停执行。
+ `wait()` 方法被调用后，线程不会自动苏醒，需要别的线程调用同一个对象上的 `notify()`或者 `notifyAll()` 方法。`sleep()`方法执行完成后，线程会自动苏醒。或者可以使用 `wait(long timeout)` 超时后线程会自动苏醒。

### join()方法-线程强制插队

Join()方法把指定的线程加入到当前线程，可以将两个交替执行的线程合并为顺序执行的线程。比如在线程B中调用了线程A的Join()方法，直到线程A执行完毕后，才会继续执行线程B

```java
public class Join {
    public static void main(String[] args) {
        Thread thread = new JoinThread();
        thread.start();
        try {
            //主线程等待thread的业务处理完了之后再向下运行  
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for(int i = 0; i < 5; i++){
            System.out.println(Thread.currentThread().getName()+" -- " + i);
        }
    }
}

class JoinThread extends Thread{
    @Override
    public void run() {
        for(int i = 0; i < 5; i++){
            System.out.println(Thread.currentThread().getName() + " -- "+i);
            try {
                sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
//---------------运行结果---------------------
//主线程等待JoinThread执行完再执行
Thread-0 -- 0
Thread-0 -- 1
Thread-0 -- 2
Thread-0 -- 3
Thread-0 -- 4
main -- 0
main -- 1
main -- 2
main -- 3
main -- 4
```

### yield()-线程的礼让

yield()方法会让运行中的线程切换到就绪状态，重新争抢cpu的时间片，争抢时是否获取到时间片看cpu的分配

```java
// 方法的定义
public static native void yield();
```

**示例代码**

```java
Runnable r1 = () -> {
    int count = 0;
    for (;;){
       log.info("---- 1>" + count++);
    }
};
Runnable r2 = () -> {
    int count = 0;
    for (;;){
        Thread.yield();
        log.info("            ---- 2>" + count++);
    }
};
Thread t1 = new Thread(r1,"t1");
Thread t2 = new Thread(r2,"t2");
t1.start();
t2.start();
```

## 线程优先级

**Java提供一个线程调度器来监控程序中启动后进入就绪状态的所有线程，线程调度器按照优先级决定应该调度哪个线程来执行**

+ 线程优先级用数字表示，范围 `1 ~ 10`，在Thread类中定义，源码如下

```java
 	public static final int MIN_PRIORITY = 1;
    public static final int NORM_PRIORITY = 5;//默认优先级
    public static final int MAX_PRIORITY = 10;
```

+ 可以通过以下方式改变或者获取优先级

```java
int getPriority() 返回线程的优先级
void setPriority(int newPriority)更改线程的优先级
```

+ CPU比较忙时，优先级高的线程获取更多的时间片，CPU比较闲时，优先级设置基本没用

**注意: 优先级的设定要在 start()方法之前**

## 主线程与守护线程

默认情况下，Java 进程需要等待所有线程都运行结束，才会结束。有一种特殊的线程叫做守护线程，只要其它非守护线程运行结束了**，**即使守护线程的代码没有执行完，也会强制结束



**可以通过这个方法将线程标记为守护线程，默认为false**

```java
void setDaemon(boolean on) 
```

**设置为true后，这个线程就成为守护线程**

```java
thread.setDaemon(true);
```

**配置实例**

```java
log.debug("开始运行...");
Thread t1 = new Thread(() -> {
	log.debug("开始运行...");
	sleep(2);
	log.debug("运行结束...");
}, "daemon");
// 设置该线程为守护线程
t1.setDaemon(true);
t1.start();
sleep(1);
log.debug("运行结束...");
```

输出

```
08:26:38.123 [main] c.TestDaemon - 开始运行...
08:26:38.213 [daemon] c.TestDaemon - 开始运行...
08:26:39.215 [main] c.TestDaemon - 运行结束...
```

**守护线程是指 `内存监控`，`垃圾回收`，`操作日志`等线程**

> 1.`垃圾回收器线程`就是一种守护线程
> 2.Tomcat 中的 `Acceptor` 和 `Poller` 线程都是守护线程，所以 Tomcat 接收到 `shutdown` 命令后，不会等待它们处理完当前请求

## 线程的状态

> **操作系统层面的线程状态划分有很多种，比较典型的有三状态模型，五状态模型，七状态模型**

进程的五状态模型如下所示

![image-20220502202408390](https://s2.loli.net/2022/05/02/2NJM5OHrmUK4Qve.png)

> Java层面将线程分为了六种状态

根据 `Thread.State` 枚举，源码如下

```java
public enum State {
    NEW,
    RUNNABLE,
    BLOCKED,
    WAITING,
    TIMED_WAITING,
    TERMINATED;
}
```

> 1.`NEW` 线程刚被创建，但是还没有调用 start() 方法
> 2.`RUNNABLE `当调用了 start() 方法之后，注意，Java API 层面的 RUNNABLE 状态涵盖了操作系统层面的可运行状态、运行状态和阻塞状态（由于 BIO 导致的线程阻塞，在 Java 里无法区分，仍然认为是可运行）
> 3.`BLOCKED` ， `WAITING` ， `TIMED_WAITING` 都是 Java API 层面对阻塞状态的细分，在下面线程的阻塞中详细整理
> 4.`TERMINATED` 当线程代码运行结束

## 线程的阻塞

线程的阻塞可以分为好多种，从操作系统层面和java层面阻塞的定义可能不同，但是广义上使得线程阻塞的方式有下面几种

1. BIO阻塞，即使用了阻塞式的io流
2. sleep(long time) 让线程休眠进入阻塞状态
3. a.join() 调用该方法的线程进入阻塞，等待a线程执行完恢复运行
4. sychronized或ReentrantLock 造成线程未获得锁进入阻塞状态 (同步锁章节细说)
5. 获得锁之后调用wait()方法 也会让线程进入阻塞状态 (同步锁章节细说)
6. LockSupport.park() 让线程进入阻塞状态 (同步锁章节细说)

## 几个面试题

> **sleep和wait的区别**

- 两者最主要的区别在于：`sleep()` 方法没有释放锁，而 `wait()` 方法释放了锁。
- 两者都可以暂停线程的执行。
- `wait()` 通常被用于线程间交互/通信，`sleep()`通常被用于暂停执行。
- `wait()` 方法被调用后，线程不会自动苏醒，需要别的线程调用同一个对象上的 `notify()`或者 `notifyAll()` 方法。`sleep()`方法执行完成后，线程会自动苏醒。或者可以使用 `wait(long timeout)` 超时后线程会自动苏醒。

> **为什么我们调用 start() 方法时会执行 run() 方法，为什么我们不能直接调用 run() 方法？**

+ 调用 `start()` 方法，会启动一个线程并使线程进入了就绪状态，当分配到时间片后就可以开始运行了。 `start()` 会执行线程的相应准备工作，然后自动执行 `run()` 方法的内容，这是真正的多线程工作。 
+ 线程的run()方法是由java虚拟机直接调用的，如果我们没有启动线程（没有调用线程的start()方法）而是在应用代码中直接调用run()方法，那么这个线程的run()方法其实运行在当前线程（即run()方法的调用方所在的线程）之中，而不是运行在其自身的线程中，从而违背了创建线程的初衷

**总结： 调用 `start()` 方法方可启动线程并使线程进入就绪状态，直接执行 `run()` 方法的话不会以多线程的方式执行。**



# 四、线程不安全

> 首先来看一下线程安全的定义

线程安全指的是多线程调用同一个对象的临界区的方法时，对象的属性值一定不会发生错误，这就是保证了线程安全

**临界区: 一段代码如果对共享资源的多线程读写操作,这段代码就被称为临界区**

## 为什么线程会不安全

**一个程序运行多个线程本身是没有问题的，问题有可能出现在多个线程访问共享资源，当多个线程读写共享资源时,如果发生指令交错，就会出现问题**

+ 指令交错指的是 java代码在解析成字节码文件时，java代码的一行代码在字节码中可能有多行，在线程上下文切换时就有可能交错

## 线程不安全的情况

**线程不安全情况的示例代码**

下面的这段代码有两个线程，都去操作这个临界区资源，一个加5000次，一个减5000次，如果线程安全，count的值应该还是0。

但是运行很多次，每次的结果不同，且都不是0，所以是线程不安全的

```java
public class 线程不安全问题 {
    // 共同操作临界区资源
    private static int resource = 0;
    public static void main(String[] args) throws InterruptedException {
        // t1线程对变量+5000次
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 5000; i++) {
                resource++;
            }
        });
        // t2线程对变量-5000次
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 5000; i++) {
                resource--;
            }
        });
        t1.start();
        t2.start();
        // 让t1 t2都执行完
        t1.join();
        t2.join();
        System.out.println(resource);
    }

}
```

> 在多线程的情况下，为了保证线程安全，经常会使用`synchronized`和`Lock`锁。所以Loki将这两个关键字的使用方式进行整理，然后解决上面这个问题，至于底层原理等比较高级的话题以后再谈



## Synchronized解决线程不安全

**synchronized是JDK自带的一个关键字，在JDK1.5之前是一个重量级锁，所以从性能上考虑大部分人会选择Lock锁，不过毕竟是JDK自带的关键字，所以在JDK1.6后对它进行优化，引入了偏向锁，轻量级锁，自旋锁等概念**

具体解决方法代码示例：

使用`synchronized`后，代码线程安全，程序运行结果为0

**方法一**

```java
public class Synchronized关键字{
    //临界区资源
    private static int resource = 0;

    synchronized public static void add(){
        resource++;
    }
    synchronized public static void subtraction(){
        resource--;
    }

    public static void main(String[] args) throws InterruptedException {
        // t1线程对变量+5000次
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 5000; i++) {
                add();
            }
        });
        // t2线程对变量-5000次
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 5000; i++) {
                subtraction();
            }
        });
        t1.start();
        t2.start();

        // 让t1 t2都执行完
        t1.join();
        t2.join();
        System.out.println(resource);
    }
}
```

方法二

```java
public class Synchronized关键字2{
    private static int count = 0;
    private static Object lock = new Object();
     // t1线程和t2对象都是对同一对象加锁。保证了线程安全。此段代码无论执行多少次，结果都是0
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 5000; i++) {
                synchronized (lock) {
                    count++;
                }
            }
        });
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 5000; i++) {
                synchronized (lock) {
                    count--;
                }
            }
        });
        t1.start();
        t2.start();
        // 让t1 t2都执行完
        t1.join();
        t2.join();
        System.out.println(count);
    }
}
```

## Synchronized关键字总结

Java 1.6之前，`synchronized` 属于重量级锁，效率低下，在 Java 6 之后 Java 官方对从 JVM 层面对 `synchronized` 较大优化，所以现在的 `synchronized` 锁效率也优化得很不错了。JDK1.6 对锁的实现引入了大量的优化，如自旋锁、适应性自旋锁、锁消除、锁粗化、偏向锁、轻量级锁等技术来减少锁操作的开销。所以，你会发现目前的话，不论是各种开源框架还是 JDK 源码都大量使用了 `synchronized` 关键字



**使用方式**

> 作用在实例方法

修饰实例方法，相当于对当前实例对象this加锁，this作为对象监视器。

```javascript
public synchronized void hello(){
    System.out.println("hello world");
}
```

> 作用在静态方法

修饰静态方法，相当于对当前类的Class对象加锁，当前类的Class对象作为对象监视器。

```java
public synchronized static void helloStatic(){
    System.out.println("hello world static");
}
```

> 修饰代码块

指定加锁对象，对给定对象加锁，括号括起来的对象就是对象监视器。

```javascript
public void test(){
    SynchronizedTest test = new SynchronizedTest();        
    synchronized (test){
        System.out.println("hello world");
    }
}
```

:rabbit: 重点：**加锁是加在对象上，一定要保证是同一对象，加锁才能生效**

> **谈一谈对 synchronized 关键字的了解与使用**

**`synchronized` 关键字解决的是多个线程之间访问资源的同步性，`synchronized`关键字可以保证被它修饰的方法或者代码块在任意时刻只能有一个线程执行**

+ **synchronized 关键字最主要的三种使用方式：**
  1. 修饰实例方法:关键字加到实例方法上是给对象实例上锁
  2. 修饰静态方法
  3. 修饰代码块:synchronized` 关键字加到 `static` 静态方法和 `synchronized(class)代码块上都是是给 Class 类上锁。
+ 不要理解为一个线程加了锁 ，进入`synchronized`代码块中就会一直执行下去。如果时间片切换了，也会执行其他线程，再切换回来会紧接着执行，只是不会执行到有竞争锁的资源，因为当前线程还未释放锁。
+ synchronized实际上使用对象锁保证临界区的**原子性** 临界区的代码是不可分割的 不会因为线程切换所打断

> 面试中面试官经常会说："单例模式了解吗？来给我手写一下！给我解释一下双重检验锁方式实现单例模式的原理呗！"

```java
public class Singleton {

    private volatile static Singleton uniqueInstance;

    private Singleton() {
    }

    public static Singleton getUniqueInstance() {
       //先判断对象是否已经实例过，没有实例化过才进入加锁代码
        if (uniqueInstance == null) {
            //类对象加锁
            synchronized (Singleton.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new Singleton();
                }
            }
        }
        return uniqueInstance;
    }
}
```

# 五、JUC中的Locks包

**注：JUC是指 `java.util.concurrent`包，这个包是Java的并发工具箱，存放了很多Java处理并发的类**



`Lock`和`ReadWriteLock`是`java.util.concurrent.locks`包中两大锁的根接口，Lock代表实现类是`ReentrantLock`（可重入锁），ReadWriteLock（读写锁）的代表实现类是`ReentrantReadWriteLock`

![image-20220503220017297](https://s2.loli.net/2022/05/03/IBMVuA3yUojeC5S.png)

+ **`ReentrantLock`**: 是唯一实现了Lock接口的类

  > **可重入锁** 指的是自己可以再次获取自己的内部锁。比如一个线程获得了某个对象的锁，此时这个对象锁还没有释放，当其再次想要获取这个对象的锁的时候还是可以获取的，如果是不可重入锁的话，就会造成死锁。同一个线程每次获取锁，锁的计数器都自增 1，所以要等到锁的计数器下降为 0 时才能释放锁

+ **`ReadWriteLock`**：ReadWriteLock（读写锁）的代表实现类

  ReadWriteLock 接口以类似方式定义了一些读取者可以共享而写入者独占的锁。此包只提供了一个实现，即 ReentrantReadWriteLock，因为它适用于大部分的标准用法上下文。但程序员可以创建自己的、适用于非标准要求的实现

+ **`Condition`** 接口描述了可能会与锁有关联的条件变量

  这些变量在用法上与使用 Object.wait 访问的隐式监视器类似，但提供了更强大的功能。需要特别指出的是，单个 Lock 可能与多个 Condition 对象关联。为了避免兼容性问题，Condition 方法的名称与对应的 Object 版本中的不同

## Lock接口

### Synchronized锁的局限性

**虽然jdk1.6后对`Synchronized`锁做了很多优化，但是还是有很多局限性**

+ `Synchronized`不是Java语言内置的，synchronized是Java语言的关键字，因此是内置特性，在JVM层面处理。而Lock是一个类，通过这个类可以实现同步访问

+ `Lock`和`synchronized`有一点非常大的不同

  + `synchronized`不需要用户去手动释放锁，当synchronized方法或者synchronized代码块执行完之后，系统会自动让线程释放对锁的占用
  + `Lock`必须主动去释放锁，并且在发生异常时，不会自动释放锁。因此一般来说，使用Lock必须在try{}catch{}块中进行，并且将释放锁的操作放在finally块中进行，以保证锁一定被被释放，防止死锁的发生。通常使用Lock来进行同步的话，是以下面这种形式

  ```java
  Lock lock = new ReentrantLock();
  
  lock.lock();
  try {
      //处理任务
  } finally {
      //释放锁
      lock.unlock();
  }
  ```

**因此，在Jdk1.5中引入了Lock锁，Lock锁适合大量同步的代码的同步问题，synchronized锁适合代码少量的同步问题**

### Lock接口源码阅读

jdk11中`java.util.concurrent.locks`包下的lock接口源码，Loki删减注释后放在下方

```java
public interface Lock {
	//用来获取锁。如果锁已被其他线程获取，则进行等待
    void lock();
	// 如果当前线程未被中断，则获取锁，可以响应中断  
    void lockInterruptibly() throws InterruptedException;
	// 仅在调用时锁为空闲状态才获取该锁，可以响应中断
    boolean tryLock();
	// 如果锁在给定的等待时间内空闲，并且当前线程未被中断，则获取锁
    boolean tryLock(long time, TimeUnit unit) throws InterruptedException;
	// 释放锁
    void unlock();
	// 返回绑定到此 Lock 实例的新 Condition 实例
    Condition newCondition();
}

```

逐个分析Lock接口中每个方法。`lock()`、`tryLock()`、`tryLock(long time, TimeUnit unit)` 和 `lockInterruptibly()`都是用来获取锁的。`unLock()`方法是用来释放锁的。`newCondition()` 返回 绑定到此 Lock 的新的 `Condition` 实例，用于线程间的协作，详细内容请在下文线程通信中阅读

### Lock锁的优点

> `Lock`可以得知线程有没有成功获取到锁，但是`synchronized`不可以

+ 解决方案：`ReentrantLock`

> `Lock` 可以不让等待的线程一直无期限地等待下去，但是`synchronized`不可以

+ 解决方案：`lockInterruptibly（）`，`tryLock() `，响应中断 

在使用`synchronized`关键字的情形下，假如占有锁的线程由于要等待IO或者其他原因（比如调用sleep方法）被阻塞了，但是又没有释放锁，那么其他线程就只能一直等待，别无他法。这会极大影响程序执行效率

> **Lock锁可以实现读者写者问题**(多个线程都只有读操作时，线程之间不会冲突)，`synchronized`不可以

+ 解决方案：`ReentrantReadWriteLock`读写锁

当多个线程读写文件时，读操作和写操作会发生冲突现象，写操作和写操作也会发生冲突现象，但是读操作和读操作不会发生冲突现象。但是如果采用synchronized关键字实现同步的话，就会导致一个问题，即当多个线程都只是进行读操作时，也只有一个线程在可以进行读操作，其他线程只能等待锁的释放而无法进行读操作

### Lock锁的缺点

> `Lock`提供了比`synchronized`更多的功能。但是要注意以下几点：

1. synchronized是在JVM层面上实现的，不但可以通过一些监控工具监控synchronized的锁定，而且在代码执行时出现异常，JVM会自动释放锁定，但是使用Lock则不行，lock是通过代码实现的，要保证锁定一定会被释放，就必须将unLock()放到finally{}中
2. 在资源竞争不是很激烈的情况下，Synchronized的性能要优于ReetrantLock，但是在资源竞争很激烈的情况下，Synchronized的性能会下降几十倍，但是ReetrantLock的性能能维持常态

### ReentrantLock锁

类的继承关系

```java
public class ReentrantLock implements Lock, java.io.Serializable
```

**ReentrantLock是唯一实现了Lock接口的类，并且ReentrantLock提供了更多的方法。下面通过一些实例学习如何简单使用 ReentrantLock，等有机会在出一篇 ReentrantLock 锁详解**

> **构造方法**

`ReentrantLock`默认是使用非公平策略，如果想指定模式，可以通过入参`fair`来选择

+ true: 公平锁
+ false: 非公平锁

```java
public ReentrantLock() {
	sync = new NonfairSync();
}
public ReentrantLock(boolean fair) {
	sync = fair ? new FairSync() : new NonfairSync();
}
```

> **实际使用**

```java
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author oliverloki
 * @Description: TODO
 * @date 2022年05月03日 16:38
 */
public class ReentrantLockTest {
    static Lock lock = new ReentrantLock();

    public static void setLock(String name) {
        lock.lock();
        try {
            System.out.println(name + " get the lock");
        } finally {
            //这两行代码顺序颠倒会导致不一样的结果，由CPU调度觉得决定
            System.out.println(name + " release the lock");
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        new Thread(() -> setLock("A线程")).start();
        new Thread(() -> setLock("B线程")).start();
    }
}

```

运行结果

```
A线程 get the lock
A线程 release the lock
B线程 get the lock
B线程 release the lock
```

从执行结果可以看出，A线程和B线程同时对资源加锁，A线程获取锁之后，B线程只好等待，直到A线程释放锁B线程才获得锁

## ReadWriteLock接口

> **源码阅读**

ReadWriteLock 维护了一对相关的锁，一个用于只读操作，另一个用于写入操作。只要没有 writer，读取锁可以由多个 reader 线程同时保持，而写入锁是独占的

jdk11中`java.util.concurrent.locks`包下的ReadWriteLock接口源码，删减注释后

```java
public interface ReadWriteLock {
  	//返回用于读取操作的锁 
    Lock readLock();
 	//返回用于写入操作的锁
    Lock writeLock();
}
```

### ReentrantReadWriteLock锁

源码阅读

```java
public class ReentrantReadWriteLock implements ReadWriteLock, java.io.Serializable{
        ...
}
```

> 示例代码——三个线程同时对一个共享数据进行读写

```java
import java.util.Random;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class Queue {
    //共享数据，只能有一个线程能写该数据，但可以有多个线程同时读该数据。
    private Object data = null;

    ReadWriteLock lock = new ReentrantReadWriteLock();

    // 读数据
    public void get() {
        // 加读锁
        lock.readLock().lock();
        try {
            System.out.println(Thread.currentThread().getName() + " be ready to read data!");
            Thread.sleep((long) (Math.random() * 1000));
            System.out.println(Thread.currentThread().getName() + " have read data :" + data);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 释放读锁
            lock.readLock().unlock();
        }
    }

    // 写数据
    public void put(Object data) {
        // 加写锁
        lock.writeLock().lock();
        try {
            System.out.println(Thread.currentThread().getName() + " be ready to write data!");
            Thread.sleep((long) (Math.random() * 1000));
            this.data = data;
            System.out.println(Thread.currentThread().getName() + " have write data: " + data);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 释放写锁
            lock.writeLock().unlock();
        }

    }
}

public class ReadWriteLockDemo {
    public static void main(String[] args) {
        final Queue queue = new Queue();
        //一共启动6个线程，3个读线程，3个写线程
        for (int i = 0; i < 3; i++) {
            //启动1个读线程
            new Thread() {
                public void run() {
                    while (true) {
                        queue.get();
                    }
                }

            }.start();
            //启动1个写线程
            new Thread() {
                public void run() {
                    while (true) {
                        queue.put(new Random().nextInt(10000));
                    }
                }
            }.start();
        }
    }
}
```









# 六、进程通信与线程间通信

> **实际上只有进程间需要通信,同一进程的线程共享地址空间,没有通信的必要，但要做好同步/互斥,保护共享的全局变量。进程间通信无论是信号，管道pipe还是共享内存都是由操作系统保证的，是系统调用**

## 进程间通信方式

> **管道(pipe)**

管道是一种半双工的通信方式，数据只能单向流动，而且只能在具有亲缘关系的进程间使用。进程的亲缘关系通常是指父子进程关系。

> **有名管道 (namedpipe)**

有名管道也是半双工的通信方式，但是它允许无亲缘关系进程间的通信。

> **信号量(semaphore)** 

信号量是一个计数器，可以用来控制多个进程对共享资源的访问。它常作为一种锁机制，防止某进程正在访问共享资源时，其他进程也访问该资源。因此，主要作为进程间以及同一进程内不同线程之间的同步手段。

注：信号量通常与PV操作挂钩

> **消息队列(messagequeue)**

消息队列是由消息的链表，存放在内核中并由消息队列标识符标识。消息队列克服了信号传递信息少、管道只能承载无格式字节流以及缓冲区大小受限等缺点。

> **信号 (sinal)**

信号是一种比较复杂的通信方式，用于通知接收进程某个事件已经发生。

> **共享内存(shared memory)**

共享内存就是映射一段能被其他进程所访问的内存，这段共享内存由一个进程创建，但多个进程都可以访问。共享内存是最快的 IPC 方式，它是针对其他进程间通信方式运行效率低而专门设计的。它往往与其他通信机制，如信号量，配合使用，来实现进程间的同步和通信。

> **套接字(socket)**

套接口也是一种进程间通信机制，与其他通信机制不同的是，它可用于不同设备及其间的进程通信



## 线程间通信方式

+ **锁机制：包括互斥锁、条件变量、读写锁**
+ **信号量机制(Semaphore)**

+ **信号机制(Signal)**

## 生产者与消费者问题

`生产者-消费者问题`、`读者-写者问题`、`时间同步问题`是进程并发控制的经典问题，`生产者-消费者问题`也是线程通信的经典问题

> 问题描述

生产者消费者问题是一个典型的同步例子。假定有一个生产者和一个消费者，他们共用一个缓冲器，生产者不断地生产物品，每生产一件物品就要存人缓冲器，但缓冲器中每次只能存入一件物品，只有当消费者把物品取走后，生产者才能把下一件物品存人缓冲器。同样地，消费者要不断地从缓冲器取出物品消费，当缓冲器中有物品时他就可以去取，每取走一件物品后必须等生产者再放一件物品后才能再取。在这个问题中，生产者要向消费者发送“缓冲器中有物品”的消息，而消费者要向生产者发送“可把物品存人缓冲器”的消息。

![image-20220503233618808](https://s2.loli.net/2022/05/03/Aun7rBxKw84X6Mg.png)

我们可以用上文中提到的方式来解决这个消息传递的问题

+ 信号量操作：用PV操作实现生产者-消费者之间的同步，应该定义两个信号量,分别表示两个消息
+ 在Java中使用锁机制：也就是下文中讲的两种办法，`Synchronized`和`lock`锁解决生产者消费者问题



我们模拟一个生产者，一个消费者，资源池有资源消费者才能消费，否则需要生产者生产

### Synchronized解决生产者消费者问题

> synchronized + wait() + notifyAll()

代码示例

```java
/**
 * @author oliverloki
 * @Description: TODO
 * @date 2022年05月03日 23:53
 */
public class synchronized解决生产者消费者 {
    public static void main(String[] args) {
        Resource2 r = new Resource();
        new Thread(() -> {
            try {
                for (int i = 0; i < 20; i++) {
                    r2.provider("生产者");
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
        new Thread(() -> {
            try {
                for (int i = 0; i < 20; i++) {
                    r2.consumer("消费者");
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

}

class Resource {
    //默认缓冲池大小
    private int resource = 0;

    public synchronized void provider(String name) throws InterruptedException {
        //资源缓冲池有资源，等待消费者消费
        if (resource == 1 ) {
            this.wait();
        }
        resource++;
        this.notifyAll();
        System.out.println(name + "存入资源");
    }

    public synchronized void consumer(String name) throws InterruptedException {
        //缓冲池为空，等待生产者存入资源
        if (resource == 0) {
            this.wait();
        }
        resource--;
        this.notifyAll();
        System.out.println(name + "取出资源");
    }
}
```

### lock锁解决生产者消费者问题

> ReentrantLock() + await() + signalAll() + Condition

代码示例

```java
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author oliverloki
 * @Description: TODO
 * @date 2022年05月03日 23:48
 */
public class lock解决生产者消费者问题 {
    public static void main(String[] args) {
        Resource2 r2 = new Resource2();
        new Thread(() -> {
            try {
                for (int i = 0; i < 3; i++) {
                    r2.provider("生产者");
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
        new Thread(() -> {
            try {
                for (int i = 0; i < 3; i++) {
                    r2.consumer("消费者");
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }


}

class Resource{
    //默认缓冲池大小
    private int resource = 0;
    private ReentrantLock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    public  void provider(String name) throws InterruptedException {
        //资源缓冲池有资源，等待消费者消费
        while (resource == 1 ) {
            condition.await();
        }
        resource++;
        System.out.println(name + "存入资源");
        condition.signalAll();
    }

    public  void consumer(String name) throws InterruptedException {
        //缓冲池为空，等待生产者存入资源
        while (resource == 0) {
            condition.await();
        }
        resource--;
        System.out.println(name + "取出资源");
        condition.signalAll();
    }
}
```

### 虚假唤醒的问题

> 为什么判断resource资源的值的时候用while而不是if?

当一个条件满足的时候，很多线程都被唤醒了，但是有些是无用的唤醒。如果说原来num为0，生产之后num为1，这样就会唤醒其他消费者来消费，但是实际上这个num=1只会被一个消费者消费。

+ while不会出现虚假唤醒: 线程被唤醒后会重新判断while中的条件，通过了while循环才会执行后面的代码

+ if会出现虚假唤醒: 唤醒后线程会从wait之后的代码开始运行，但是不会重新判断if条件，直接继续运行if代码块之后的代码

**在jdk1.8文档中 Object - > wait ->可以看到这段话:**

线程也可以唤醒，而不会被通知，中断或超时，即所谓的*虚假唤醒* 。 虽然这在实践中很少会发生，但应用程序必须通过测试应该使线程被唤醒的条件来防范，并且如果条件不满足则继续等待。 换句话说，等待应该总是出现在循环中，就像这样：

```java
synchronized (obj) {
    while (<condition does not hold>)
        obj.wait(timeout);
    ... // Perform action appropriate to condition
} 
```

## Condition接口

> Condition是在java 1.5中才出现的，它用来替代传统的Object的wait()、notify()实现线程间的协作，相比使用Object的wait()、notify()，使用Condition的await()、signal()这种方式实现线程间协作更加安全和高效。因此通常来说比较推荐使用Condition，阻塞队列实际上是使用了Condition来模拟线程间协作

### Condition接口方法

```java
    // 当前线程进入等待状态直到被通知（signal）或中断
    void await() throws InterruptedException;

    // 当前线程进入等待状态直到被通知，该方法不响应中断
    void awaitUninterruptibly();

    // 当前线程进入等待状态直到被通知、中断或者超时，返回值表示剩余超时时间
    long awaitNanos(long nanosTimeout) throws InterruptedException;

    // 当前线程进入等待状态直到被通知、中断或者到指定时间，如果未超时返回true，否则返回false 
    boolean await(long time, TimeUnit unit) throws InterruptedException;

    // 当前线程进入等待状态直到被通知、中断或者到某个时间。如果未超时返回true，否则返回false 
    boolean awaitUntil(Date deadline) throws InterruptedException;

    // 唤醒一个等待在Condition上的线程，该线程从等待方法返回前必须获得与Condition相关联的锁
    void signal();

    // 唤醒所有等待在Condition上的线程，能够从等待方法返回的线程必须获得与Condition相关联的锁
    void signalAll();
```

### 实现线程间精准通知唤醒——生产者消费者代码的改进

还是以生产者消费者为例，生产者生产以后通知消费者消费，消费者消费以后通知生产者生产

改进后代码如下

```java

public class ResourceCondtion {
    private Lock lock = new ReentrantLock();
    private Condition consumeCondition = lock.newCondition();
    private Condition productCondition = lock.newCondition();
    private int number = 0;
 
    public  void increace(){
        lock.lock();
        try {
            while(number > 0){
                productCondition.await();
            }
            number ++;
            System.out.println("生产后"+Thread.currentThread().getName()+" : "+number);
            //通知消费者
            consumeCondition.signal();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
 
    public void decreace(){
        lock.lock();
        try {
            while(number == 0){
                consumeCondition.await();
            }
            number --;
            System.out.println("消费后"+Thread.currentThread().getName()+" : "+number);
            //通知生产者
            productCondition.signal();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
 
    }
 
    public static void main(String[] args) {
        ResourceCondtion resource = new ResourceCondtion();
        new Thread(new ProductCondition(resource)).start();
        new Thread(new ProductCondition(resource)).start();
        new Thread(new ConsumeCondition(resource)).start();
        new Thread(new ConsumeCondition(resource)).start();
        new Thread(new ConsumeCondition(resource)).start();
    }
}
```





# 七、死锁问题与死锁避免

> **产生死锁必须具备以下四个条件**

1. 互斥条件：该资源任意一个时刻只由一个线程占用。
2. 请求与保持条件：一个进程因请求资源而阻塞时，对已获得的资源保持不放。
3. 不剥夺条件:线程已获得的资源在末使用完之前不能被其他线程强行剥夺，只有自己使用完毕后才释放资源。
4. 循环等待条件:若干进程之间形成一种头尾相接的循环等待资源关系

> **死锁避免**

上面说了产生死锁的四个必要条件，为了避免死锁，我们只要破坏产生死锁的四个条件中的其中一个就可以了。现在我们来挨个分析一下：

1. **破坏互斥条件** ：这个条件我们没有办法破坏，因为我们用锁本来就是想让他们互斥的（临界资源需要互斥访问）。
2. **破坏请求与保持条件** ：一次性申请所有的资源。
3. **破坏不剥夺条件** ：占用部分资源的线程进一步申请其他资源时，如果申请不到，可以主动释放它占有的资源。
4. **破坏循环等待条件** ：靠按序申请资源来预防。按某一顺序申请资源，释放资源则反序释放。破坏循环等待条件

> 产生死锁代码示例

线程 A 通过 synchronized (resource1) 获得 resource1 的监视器锁，然后通过`Thread.sleep(1000);`让线程 A 休眠 1s 为的是让线程 B 得到执行然后获取到 resource2 的监视器锁。线程 A 和线程 B 休眠结束了都开始企图请求获取对方的资源，然后这两个线程就会陷入互相等待的状态，这也就产生了死锁。上面的例子符合产生死锁的四个必要条件

```java
public class DeadLockDemo {
    private static Object resource1 = new Object();//资源 1
    private static Object resource2 = new Object();//资源 2

    public static void main(String[] args) {
        new Thread(() -> {
            synchronized (resource1) {
                System.out.println(Thread.currentThread() + "get resource1");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread() + "waiting get resource2");
                synchronized (resource2) {
                    System.out.println(Thread.currentThread() + "get resource2");
                }
            }
        }, "线程 1").start();

        new Thread(() -> {
            synchronized (resource2) {
                System.out.println(Thread.currentThread() + "get resource2");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread() + "waiting get resource1");
                synchronized (resource1) {
                    System.out.println(Thread.currentThread() + "get resource1");
                }
            }
        }, "线程 2").start();
    }
}
```



# 八、八锁现象彻底理解锁

请移步Loki的另一篇文章[Java实现经典八锁问题](https://blog.csdn.net/Night__breeze/article/details/124654523?spm=1001.2014.3001.5501)



# 九、线程安全的集合

## CopyOnWriteArrayList















# 十、Callable详解





# 十一、JUC辅助类详解





# 十二、线程池



































> **参考好文推荐**

[Java6及以上版本对synchronized的优化](https://www.cnblogs.com/wuqinglong/p/9945618.html)

[java 锁 Lock接口详解](https://www.cnblogs.com/myseries/p/10784076.html)