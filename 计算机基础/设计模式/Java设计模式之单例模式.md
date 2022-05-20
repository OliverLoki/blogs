Java设计模式之单例模式

> 单例模式是最容易理解，也是最常用的设计模式

## 单例模式概述

在软件开发过程中，如果我们想保证一个类只有一个实例，并想一个全局访问点给使用方提供这个实
例，则可以考虑下使用单例模式。

**优势**：单例模式可以避免类频繁创建和销毁所带来的开销，节省系统资源的同时，也简化了对该类相关逻辑的管理

## 怎样优雅的实现单例模式

**Loki将实现单例模式总结为以下三点，下文中所有的实现都是围绕这三点做工作的**

+ **在类的内部创建该类的实例**

+ **构造器私有化==>不可以通过new关键字创造对象**

+ **向外暴露一个静态的公共方法==>给这个实例提供全局访问点**

**首先科普一下懒汉式和饿汉式**

> **饿汉式**
>
> 不管程序是否需要这个对象的实例，总是在类加载的时候就先创建好实例，理解起来就像不管一个人想不想吃东西都把吃的先买好，如同饿怕了一样
>
> **懒汉式**
>
> 如果一个对象使用频率不高，占用内存还特别大，明显就不合适用饿汉式了，这时就需要一种懒加载的思想，当程序需要这个实例的时候才去创建对象，就如同一个人懒的饿到不行了才去吃东西。

### 饿汉模式

一般，我们写单例，最容易写的是饿汉模式，定义要给

```java
public class Singleton {
    //在类的内部创建该类的`private static final修饰的实例
    private static final Singleton INSTANCE = new Singleton();
	//构造器私有化
    private Singleton() {}
	//向外暴露一个静态的公共方法，给这个实例提供全局访问点
    public static Singleton getInstance() {
        return INSTANCE;
   } 
}
```

> 这种方式是没问题，但是这里的`INSTANCE`在在类装载的时候就完成实例化（由于导致类装载的原因有很多种，因此不能确定是否有其他的方式导致类装载）
>
> 如果这样的类非常多，但是只有少数几个才使用，那这些无效的实例化的对象就占用了很多的内存，并且没有达到 `Lazy loading` 的效果

这个时候，一般会想到懒汉模式了

### 懒汉模式

```java
public class Singleton {
    //类的内部创建对象实例
    private static Singleton instance = null;
	//构造器私有化
    private Singleton() {}
	//全局访问点
    public static Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
   } 
}
```

这份代码看上去是懒汉模式，但是其实是有问题的，在多线程环境下，如果两个线程同时进入到`if (instance == null)`这里之后，`instance`会被初始化两次，两个线程拿到的不是同一个对象，这就不是单例了。

于是乎，首先想到的就是给`getInstance()`方法加上`synchronized`关键字

### 线程安全的懒汉模式

```java
public class Singleton {
    private static Singleton instance = null;

    private Singleton() {}

    public static synchronized Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
   } 
}
```

这份代码虽然是解决了重复实例化的问题，但是同步的粒度比较大，并发线程比较多的时候，每个线程调用都要去做同步的操作，而单例模式的实例对象，一旦被实例化之后就不会再改变（除非重启应用），所以这个同步的粒度是可以优化的



于是乎，就出现了双重检查的懒汉模式

### 双重检查的懒汉模式

```java
public class Singleton {
    
    private static Singleton instance = null;
	//构造器私有化==》不可以通过new关键字创造对象
    private Singleton() {}
	//全局访问点
    public static Singleton getInstance() {
        if (instance == null) {
            synchronized(Singleton.class) {
                instance = new Singleton();
            }
        }
        return instance;
   } 
}
```

这里对`instance`的初始化做了两次检查，所以叫双重检查。双重检查完美的避免了上面的问题，只要`instance`被实例化了就不再走同步的代码块



但是，上面的代码还是有问题的，问题点就在于`instance`的实例化

在`Java`中，实例化一个对象分为三步：

1. 分配内存空间；
2. 初始化对象；
3. 将内存空间的地址赋值给对应的引用；

然而，现实是操作系统可以对指令进行重排序，所以上面的步骤可能会变成132，而不是123。所以，双重检查的懒汉模式需要给`instance`的定义处，加上`volatile`关键字，这个关键字在这里的作用是：禁止指令重排序优化。换句话说，就是`volatile`修饰的变量的赋值操作后面会有一个内存屏障，读操作不会被重排序到内存屏障之前。

所以，正确的代码应该是这样的：

```java
public class Singleton {
    private volatile static Singleton instance = null;

    private Singleton() {}

    public static Singleton getInstance() {
        if (instance == null) {
            synchronized(Singleton.class) {
                instance = new Singleton();
            }
        }
        return instance;
   } 
}
```

双重检查（据说需要使用`JDK 1.5+`，在此之前`JMM`模型存在缺陷，我没有验证过）完成了，但是还是会有同步的操作存在，也就是说会有锁，而无锁肯定会优于无锁的方式，那有没有一种无锁的方式来实现这个单例呢？===》静态内部类出现了

### 静态内部类

**具体代码如下，是我认为单例模式最优雅的实现**

```java
public class Singleton {
	//构造器私有化不可以通过new关键字创造对象
    private Singleton() {}
	//向外暴露一个静态的公共方法
    public static Singleton getInstance() {
        return SingletonInstance.instance;
    }
	//定义一个静态内部类，内部定义当前类的静态属性
    private static class SingletonInstance {
        private static final Singleton instance = new Singleton();
    }

}
```

这份代码没有锁，并且，`LazyHolder`中的`instance`实例在`Singleton#getInstance()`方法调用之前是不会被实例化的，这就不会出现饿汉模式那种过早占用内存的情况；而且，上面的代码是使用静态嵌套类的方式实现的，所以，能绝对的保证`instance`只会被实例化一次，因此，就不需要双重检查了

> 附：《Effective Java》作者 Josh Bloch 推荐的一种单例模式的实现 —— 枚举
>
> **借助 JDK1.5 中添加的枚举来实现单例模式。不仅能避免多线程同步问题，而且还能防止反序列化重新创建新的对象**

```java
public enum Singleton {
    INSTANCE;
    public void sayHello() {
        System.out.println("Hello World");
    }
}
```

## 源码中的单例模式

### JDK中Runtime类——饿汉模式

Jdk11的版本，Loki节选了`java.lang.Runtime`这个类的部分代码，包含

+ 单例模式的实现

+ `Runtime`类唯一的静态方法

  `public static Version version(){}`，只有这个方法可以被开发者调用，查看当前JRE的版本，其他方法均为实例方法（没有static关键字修饰）

```java
public class Runtime {
//单例模式实现源码部分----------------------------------------------------
    //构造器私有化不可以通过new关键字创造对象
    private Runtime() {}
    //在类的内部创建该类的private static final修饰的实例
    private static final Runtime currentRuntime = new Runtime();
	//全局访问点
    public static Runtime getRuntime() {
        return currentRuntime;
    }
	
//唯一的静态方法源码---------------------------------------------------------
    private static Version version;
	//开发者通过Runtime.Version获取JRE版本 
    public static Version version() { }
    //静态内部类
    public static final class Version implements Comparable<Version>{}
        
}
```



### 手写一个单例模式

假设我们正在开发一个手机应用程序，该应用程序支持进行各种各样的设置，比如字体大小、聊天背景等。在编码上，我们可以设计一个 `ConfigsManager` 类，这个类封装了应用程序中所有的设置项 (每一项配置对应一个实例变量)，也封装了实例变量对应的 `get` 和 `set` 方法



既然 `ConfigsManager` 已经封装了所有的设置项，那么我们就可以将其视为一个单例类，它只有一个实例：

```java
//Get、Set方法已省略
public class ConfigsManager {
    private String mFontSize;
    private String mChatBackground;
    private static ConfigsManager sConfigsManagers;

    private ConfigsManager() {
        // 假设当前数据库文字大小为「中」，聊天背景为「风景」
        mFontSize = "中";
        mChatBackground = "风景";
    }

    public static ConfigsManager getInstance() {
        if (sConfigsManagers == null) {
            synchronized (ConfigsManager.class) {
                if (sConfigsManagers == null) {
                    sConfigsManagers = new ConfigsManager();
                }
            }
        }
        return sConfigsManagers;
    }
}
```



