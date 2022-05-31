**引言**

> ***`观察者（Observer）模式`*** 又名发布-订阅（Publish/Subscribe）模式，是一种行为设计模式，允许你定义一种订阅机制，可在对象事件发生时通知多个 “观察” 该对象的其他对象。

![image-20220531230616909](https://s2.loli.net/2022/05/31/96ZAE3HmBQ2sdRb.png)

## 一、引子：观察者模式解决怎么样的问题、怎么解决

现在我们碰到一个实际问题描述如下

> **假如你有两种类型的对象：`顾客` 和`商店`。顾客对某个特定品牌的产品非常感兴趣， 而该产品很快将会在商店里出售**
>
> **顾客可以每天来商店看看产品是否到货。但如果商品尚未到货时，绝大多数来到商店的顾客都会空手而归**
>
> **另一方面，每次新产品到货时，商店可以向所有顾客发送邮件 （可能会被视为垃圾邮件）。 这样，部分顾客就无需反复前往商店了，但也可能会惹恼对新产品没有兴趣的其他顾客**

:robot: Q:**我们似乎遇到了一个矛盾： 要么让顾客浪费时间检查产品是否到货， 要么让商店浪费资源去通知没有需求的顾客**

A：**使用观察者模式来解决这个问题，首先需要了解一下发布者与订阅者的概念，了解基础的Observer模式怎么运行，然后再来解决这个问题**



拥有一些值得关注的状态的对象通常被称为目标，由于它要将自身的状态改变通知给其他对象， 我们也将其称为发布者`publisher`。所有希望关注发布者状态变化的其他对象被称为订阅者`subscribers`

**回到上述问题**

+ publisher -> 超市
+ subscribers -> 顾客

**观察者模式建议你为发布者类添加订阅机制，让每个对象都能订阅或取消订阅发布者事件流**

**这并不像听上去那么复杂。实际上，我们用一张图就可以将观察者模式搞明白**

## 二、一张图说明观察者模式的结构

![image-20220601001524508](https://s2.loli.net/2022/06/01/fwHUWocdGqSzpPL.png)

[图源](https://refactoringguru.cn/design-patterns/observer)

可以看到图中有 publisher  / subscriber / client 三个角色，他们各司其职，而观察者模式常用的几个角色无非以下几种

+ 基础发布者接口
+ 具体发布者实现类
+ 通用订阅者接口
+ 具体订阅者实现类，实现响应等操作

> 对于发布者而言

1. 发布者一个用于存储订阅者对象引用的列表成员变量

   ```java
   ArrayList<> subscribers = new ArrayList<>();
   ```

   ![image-20220427010533450](https://s2.loli.net/2022/04/27/McW1nVRNgIj78XG.png)

2. 几个用于添加或删除该列表中订阅者的公有方法

   ```java
   public boolean addSubscriber(subscriber);
   public boolean removeSubscriber(subscriber);
   ```

3. 通知更新

   ```java
   for(s : subscribers){
   	s.update();
   }
   ```

> 对于订阅者接口

在绝大多数情况下，该接口仅包含一个update更新方法，该方法可以拥有多个参数，使发布能在更新时传递事件的详细信息。

```java
public void update(context);
```

> 对于订阅者

+ 订阅者通常需要一些上下文信息来正确地处理更新，因此需要实现订阅者接口

+ 发布者通常会将一些上下文数据作为通知方法的参数进行传递。发布者也可将自身作为参数进行传递，使订阅者直接获取所需的数据
+ 订阅者可以对得到的数据做出一些响应

## 三、Java**观察者**模式代码示例

###  EventManager.java: 基础发布者

```java
import EventSubscriber;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventManager {
    Map<String, List<EventSubscriber>> Subscribers = new HashMap<>();

    public EventManager(String... operations) {
        for (String operation : operations) {
            this.Subscribers.put(operation, new ArrayList<>());
        }
    }

    public void subscribe(String eventType, EventSubscriber Subscriber) {
        List<EventSubscriber> users = Subscribers.get(eventType);
        users.add(Subscriber);
    }

    public void unsubscribe(String eventType, EventSubscriber Subscriber) {
        List<EventSubscriber> users = Subscribers.get(eventType);
        users.remove(Subscriber);
    }

    public void notify(String eventType, File file) {
        List<EventSubscriber> users = Subscribers.get(eventType);
        for (EventSubscriber Subscriber : users) {
            Subscriber.update(eventType, file);
        }
    }
}
```

### Editor.java:具体发布者，由其他对象追踪

```java
package editor;

import refactoring_guru.observer.example.publisher.EventManager;

import java.io.File;

public class Editor {
    public EventManager events;
    private File file;

    public Editor() {
        this.events = new EventManager("open", "save");
    }

    public void openFile(String filePath) {
        this.file = new File(filePath);
        events.notify("open", file);
    }

    public void saveFile() throws Exception {
        if (this.file != null) {
            events.notify("save", file);
        } else {
            throw new Exception("Please open a file first.");
        }
    }
}
```

### EventSubscriber.java: 通用订阅者接口

```java
package Subscribers;

import java.io.File;

public interface EventSubscriber {
    void update(String eventType, File file);
}
```

###  EmailNotificationSubscriber.java: 收到通知后发送邮件

```java
package Subscribers;

import java.io.File;

public class EmailNotificationSubscriber implements EventSubscriber {
    private String email;

    public EmailNotificationSubscriber(String email) {
        this.email = email;
    }

    @Override
    public void update(String eventType, File file) {
        System.out.println("Email to " + email + ": Someone has performed " + eventType + " operation with the following file: " + file.getName());
    }
}
```

###  LogOpenSubscriber.java: 收到通知后在日志中记录一条消息

```java
package Subscribers;
import java.io.File;

public class LogOpenSubscriber implements EventSubscriber {
    private File log;

    public LogOpenSubscriber(String fileName) {
        this.log = new File(fileName);
    }

    @Override
    public void update(String eventType, File file) {
        System.out.println("Save to log " + log + ": Someone has performed " + eventType + " operation with the following file: " + file.getName());
    }
}
```

### Demo.java: 测试代码

```java
package example;

import Editor;
import EmailNotificationSubscriber;
import LogOpenSubscriber;

public class Demo {
    public static void main(String[] args) {
        Editor editor = new Editor();
        editor.events.subscribe("open", new LogOpenSubscriber("/path/to/log/file.txt"));
        editor.events.subscribe("save", new EmailNotificationSubscriber("admin@example.com"));

        try {
            editor.openFile("test.txt");
            editor.saveFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

### 执行结果

```
Save to log \path\to\log\file.txt: Someone has performed open operation with the following file: test.txt
Email to admin@example.com: Someone has performed save operation with the following file: te
```



## 四、Java源码中的观察者模式

观察者模式在 Java 代码中很常见， 特别是在 GUI 组件中。 它提供了在不与其他对象所属类耦合的情况下对其事件做出反应的方式。

这里是核心 Java 程序库中该模式的一些示例：

- [`java.util.Observer`](http://docs.oracle.com/javase/8/docs/api/java/util/Observer.html)/[`java.util.Observable`](http://docs.oracle.com/javase/8/docs/api/java/util/Observable.html) （极少在真实世界中使用）
- [`java.util.EventSubscriber`](http://docs.oracle.com/javase/8/docs/api/java/util/EventSubscriber.html)的所有实现 （几乎广泛存在于 Swing 组件中）
- [`javax.servlet.http.HttpSessionBindingSubscriber`](http://docs.oracle.com/javaee/7/api/javax/servlet/http/HttpSessionBindingSubscriber.html)
- [`javax.servlet.http.HttpSessionAttributeSubscriber`](http://docs.oracle.com/javaee/7/api/javax/servlet/http/HttpSessionAttributeSubscriber.html)
- [`javax.faces.event.PhaseSubscriber`](http://docs.oracle.com/javaee/7/api/javax/faces/event/PhaseSubscriber.html)

**识别方法：** 该模式可以通过将对象存储在列表中的订阅方法， 和对于面向该列表中对象的更新方法的调用来识别

## 五、观察者模式适合应用场景

![image-20220601003955837](https://s2.loli.net/2022/06/01/AWGrjVqZp5foSUw.png)

## 六、与其他模式的关系

- [责任链模式](https://refactoringguru.cn/design-patterns/chain-of-responsibility)、 [命令模式](https://refactoringguru.cn/design-patterns/command)、 [中介者模式](https://refactoringguru.cn/design-patterns/mediator)和[观察者模式](https://refactoringguru.cn/design-patterns/observer)用于处理请求发送者和接收者之间的不同连接方式：

  - *责任链*按照顺序将请求动态传递给一系列的潜在接收者， 直至其中一名接收者对请求进行处理。
  - *命令*在发送者和请求者之间建立单向连接。
  - *中介者*清除了发送者和请求者之间的直接连接， 强制它们通过一个中介对象进行间接沟通。
  - *观察者*允许接收者动态地订阅或取消接收请求。

- [中介者](https://refactoringguru.cn/design-patterns/mediator)和[观察者](https://refactoringguru.cn/design-patterns/observer)之间的区别往往很难记住。 在大部分情况下， 你可以使用其中一种模式， 而有时可以同时使用。 让我们来看看如何做到这一点。

  *中介者*的主要目标是消除一系列系统组件之间的相互依赖。 这些组件将依赖于同一个中介者对象。 *观察者*的目标是在对象之间建立动态的单向连接， 使得部分对象可作为其他对象的附属发挥作用。

  有一种流行的中介者模式实现方式依赖于*观察者*。 中介者对象担当发布者的角色， 其他组件则作为订阅者， 可以订阅中介者的事件或取消订阅。 当*中介者*以这种方式实现时， 它可能看上去与*观察者*非常相似。

  当你感到疑惑时， 记住可以采用其他方式来实现中介者。 例如， 你可永久性地将所有组件链接到同一个中介者对象。 这种实现方式和*观察者*并不相同， 但这仍是一种中介者模式。

  假设有一个程序， 其所有的组件都变成了发布者， 它们之间可以相互建立动态连接。 这样程序中就没有中心化的中介者对象， 而只有一些分布式的观察者。

## 作业：解决上述商店与购物者的问题

todo





