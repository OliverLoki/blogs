> 下面将通过经典的8锁问题，认清锁原理

## 场景一

```java
import java.util.concurrent.TimeUnit;

/**
 * 标准情况下 是先sendEmail()　还是先callPhone()?
 * 答案：sendEmail
 * 解释：被 synchronized 修饰的方式，锁的对象是方法的调用者
 * 所以说这里两个方法调用的对象是同一个，先调用的先执行！
 */
public class LockDemo1 {
    public static void main(String[] args) throws InterruptedException {
        Phone1 phone1 = new Phone1();
        new Thread(()->{
            phone1.sendEmail();
        },"A").start();
        TimeUnit.SECONDS.sleep(3);
        new Thread(()->{
            phone1.callPhone();
        },"B").start();
    }
}
class Phone1{
    public synchronized void sendEmail(){
        System.out.println("senEmail");
    }
    public synchronized  void callPhone(){
        System.out.println("callPhone");
    }
}
```

## 场景二

```java
import java.util.concurrent.TimeUnit;

/**
 * sendEmail()休眠三秒后  是先执行sendEmail() 还是 callPhone()
 * 答案： sendEmail
 * 解释：被 synchronized 修饰的方式，锁的对象是方法的调用者
 * 所以说这里两个方法调用的对象是同一个，先调用的先执行！
 */
public class LockDemo2 {
    public static void main(String[] args) throws InterruptedException {
        Phone2 phone2 = new Phone2();
        new Thread(()->{
            try {
                phone2.sendEmail();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"A").start();
        TimeUnit.SECONDS.sleep(2); // 休眠2秒
        new Thread(()->{
            phone2.callPhone();
        },"B").start();
    }
}
class Phone2{
    public synchronized void sendEmail() throws InterruptedException {
        TimeUnit.SECONDS.sleep(3);
        System.out.println("sendEmail");
    }
    public synchronized void callPhone(){
        System.out.println("callPhone");
    }
}
```

## 场景三

```java
import java.util.concurrent.TimeUnit;

/**
 * 被synchronized 修饰的方式和普通方法 先执行sendEmail() 还是 callPhone()
 * 答案： callPhone
 * 解释：新增加的这个方法没有 synchronized 修饰，不是同步方法，不受锁的影响！
 */
public class LockDemo3 {
    public static void main(String[] args) throws InterruptedException {
        Phone3 phone3 = new Phone3();
        new Thread(()->{
            try {
                phone3.sendEmail();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"A").start();

        TimeUnit.SECONDS.sleep(2);
        new Thread(()->{
            phone3.callPhone();
        },"B").start();
    }
}
class Phone3{
    public synchronized void sendEmail() throws InterruptedException {
        TimeUnit.SECONDS.sleep(4);
        System.out.println("sendEmail");
    }

    // 没有synchronized 没有static 就是普通方式
    public void callPhone(){
        System.out.println("callPhone");
    }
}
```

## 场景四

```java
import java.util.concurrent.TimeUnit;

/**
 * 被synchronized 修饰的不同方法 先执行sendEmail() 还是callPhone()？
 * 答案：callPhone
 * 解释：被synchronized 修饰的不同方法 锁的对象是调用者
 * 这里锁的是两个不同的调用者，所有互不影响
 */
public class LockDemo4 {
    public static void main(String[] args) throws InterruptedException {
        Phone4 phoneA = new Phone4();
        Phone4 phoneB = new Phone4();

        new Thread(()->{
            try {
                phoneA.sendEmail();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"A").start();

        TimeUnit.SECONDS.sleep(1);
        new Thread(()->{
            phoneB.callPhone();
        },"B").start();
    }
}
class Phone4{
    public synchronized void sendEmail() throws InterruptedException {
        TimeUnit.SECONDS.sleep(3);
        System.out.println("sendEmail");
    }
    public synchronized void callPhone(){
        System.out.println("callPhone");
    }
}
```

## 场景五

```java
import java.util.concurrent.TimeUnit;

/**
 * 两个静态同步方法 都被synchronized 修饰 是先sendEmail() 还是callPhone()？
 * 答案：sendEmial
 * 解释：只要方法被 static 修饰，锁的对象就是 Class模板对象,这个则全局唯一！
 *      所以说这里是同一个锁，并不是因为synchronized  这里程序会从上往下依次执行
 */
public class LockDemo5 {
    public static void main(String[] args) throws InterruptedException {
        Phone5 phone5 = new Phone5();
        new Thread(()->{
            try {
                phone5.sendEmail();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"A").start();

        TimeUnit.SECONDS.sleep(1);
        new Thread(()->{
            phone5.callPhone();
        },"B").start();
    }
}
class Phone5{
    public static synchronized void sendEmail() throws InterruptedException {
        TimeUnit.SECONDS.sleep(3);
        System.out.println("sendEmail");
    }

    public static synchronized void callPhone(){
        System.out.println("callPhone");
    }
}
```

## 场景六

```java
import java.util.concurrent.TimeUnit;

/**
 * 被synchronized 修饰的普通方法和静态方法  是先sendEmail() 还是 callPhone()?
 * 答案：callPhone
 * 解释：只要被static修饰锁的是class模板, 而synchronized 锁的是调用的对象
 * 这里是两个锁互不影响，按时间先后执行
 */
public class LockDemo6 {
    public static void main(String[] args) throws InterruptedException {
        Phone6 phone6 = new Phone6();
        new Thread(()->{
            try {
                phone6.sendEmail();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"A").start();

        TimeUnit.SECONDS.sleep(1);
        new Thread(()->{
            phone6.callPhone();
        },"B").start();
    }
}
class Phone6{
    public static synchronized void sendEmail() throws InterruptedException {
        TimeUnit.SECONDS.sleep(3);
        System.out.println("sendEmail");
    }

    public synchronized void callPhone(){
        System.out.println("callPhone");
    }
}
```

## 场景七

```java
import java.util.concurrent.TimeUnit;

/**
 * 同被static+synchronized 修饰的两个方法，是先sendEmail()还是callPhone()?
 *  答案：sendEmail
 *  解释：只要方法被 static 修饰，锁的对象就是 Class模板对象,这个则全局唯一
 *  所以说这里是同一个锁，并不是因为synchronized
 */
public class LockDemo7 {
    public static void main(String[] args) throws InterruptedException {
        Phone7 phoneA = new Phone7();
        Phone7 phoneB = new Phone7();

        new Thread(()->{
            try {
                phoneA.sendEmail();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"A").start();

        TimeUnit.SECONDS.sleep(1);
        new Thread(()->{
            phoneB.callPhone();
        },"B").start();
    }
}
class Phone7{
    public static synchronized void sendEmail() throws InterruptedException {
        TimeUnit.SECONDS.sleep(3);
        System.out.println("sendEmail");
    }

    public static synchronized void callPhone(){
        System.out.println("callPhone");
    }
}
```

## 场景八

```java
import java.util.concurrent.TimeUnit;

/**
 * 一个被static+synchronized 修饰的方法和普通的synchronized方法，先执行sendEmail()还是callPhone()？
 * 答案：callPhone()
 * 解释： 只要被static 修饰的锁的就是整个class模板
 * 这里一个锁的是class模板 一个锁的是调用者 
 * 所以锁的是两个对象 互不影响
 */
public class LockDemo8 {
    public static void main(String[] args) throws InterruptedException {
        Phone8 phoneA = new Phone8();
        Phone8 phoneB = new Phone8();

        new Thread(()->{
            try {
                phoneA.sendEmail();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"A").start();

        TimeUnit.SECONDS.sleep(1);
        new Thread(()->{
            phoneB.callPhone();
        },"B").start();
    }
}
class Phone8{
    public static synchronized void sendEmail() throws InterruptedException {
        TimeUnit.SECONDS.sleep(3);
        System.out.println("sendEmail");
    }

    public synchronized void callPhone(){
        System.out.println("callPhone");
    }
}
```

> 小结：

```java
synchronized(Demo.class){

}
synchronized(this){

}
```

1、new this 调用的是这个对象，是一个具体的对象！
2、static class 唯一的一个模板！
在我们编写多线程程序得时候，只需要搞明白这个到底锁的是什么就不会出错了！