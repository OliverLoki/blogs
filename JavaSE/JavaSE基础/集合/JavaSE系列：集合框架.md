**参考资料**

> [[Why We Need Collection Framework in Java? - GeeksforGeeks]](https://www.geeksforgeeks.org/why-we-need-collection-framework-in-java/)
>
> [[Java SE 8文档-集合框架概述 - doc.oracle]](https://docs.oracle.com/javase/8/docs/technotes/guides/collections/overview.html)

**引言——为什么需要集合框架**

> 在引入集合框架(JDK1.2)之前，我们存储同一类型的对象只能使用数组等手段，尽管所有集合的主要目的是相同的，但它们之间没有关联，需要独立定义每个集合不同的方法、语法和构造函数
>
> **因此，Java引入集合框架来处理不同类型的复杂重复操作**

**:rocket:对框架的理解：**

+ 框架是一组提供现成架构的类和接口

+ 为了实现一个新特性或一个类，不需要定义一个框架
+ 一个最佳的面向对象的设计总是包含一个具有类集合的框架，这样所有的类都执行相同类型的任务



@[TOC]

# 1）快速上手定义与特性

> **Java集合定义**

在Java中，如果一个Java对象可以在内部持有若干其他Java对象，并对外提供访问接口，我们把这种Java对象称为集合，而**集合框架**就是这些集合对象的抽象

> **集合的特点**

+ **集合中的对象比较需要重写equals方法，否则比较的是对象地址不同的集合底层对应不同的数据结构**
+ **所有的集合类和集合接口都在java.util包下集合也是一个对象，也有一个内存地址指向它**
+ **没有使用泛型之前，集合可以存储任何Object的子类型，使用泛型之后，集合只能存储某个具体的类型**
+ **集合任何时候存储的都是引用，它不能存基本数据类型**

# 2）集合的分类

Java的集合类主要由两个接口派生而出：Collection和Map，Collection和Map是Java集合框架的根接口，这两个接口又包含了一些子接口或实现类，使用集合时需要进行导包`java.util`

+ 单列(`value`)集合根接口：java.util.Collection
+ 双列（`key-value`型）集合根接口：java.util.Map

**Java集合框架图如下所示**，**本篇Loki只整理常用的集合类与接口**

![image-20220421171357994](https://s2.loli.net/2022/04/22/7CN2Bdo3yIkAjEn.png)



> Java主要提供了以下三种类型的集合

- **`List`(对付顺序的好帮手)**

  **集合是有序集合，集合中的元素可以重复，访问集合中的元素可以根据元素的索引来访问**

- **`Set`(注重独一无二的性质)**

  **集合是无序集合，集合中的元素不可以重复，访问集合中的元素只能根据元素本身来访问（也是集合里元素不允许重复的原因）**

- **`Map`(用 Key 来搜索的专家)**

  + **集合中保存Key-value对形式的元素，访问时只能根据每项元素的key来访问其value**
  + **Key 是无序的、不可重复的，value 是无序的、可重复的，每个键最多映射到一个值**

# 3）废弃的集合类与接口

由于Java的集合设计非常久远，中间经历过大规模改进，因此有一小部分集合类是遗留类，不应该继续使用

- `Hashtable`：一种线程安全的`Map`实现

- `Vector`：一种线程安全的`List`实现，增加了性能开销，一般不会去使用它

  在线程不安全的情况下，被 `ArrayList` 替代

  线程安全的情况下，应该使用(并充分理解)`java.util.concurrent`中的集合

- `Stack`：基于`Vector`实现的`LIFO`的栈，被替代，JDK推荐用以下方式生成一个栈

  ```java
   Deque<Integer> stack = new ArrayDeque<Integer>();
  ```

还有一小部分接口是遗留接口，也不应该继续使用：

- `Enumeration<E>`：已被`Iterator<E>`取代

# 4）Collection接口

> Collection接口作为单列集合的顶端接口，它的方法可以被子类使用

![image-20210422181830481](https://s2.loli.net/2022/04/22/VKRzLXJ61briaUD.png)

> **Collection接口方法**
>
> 意味着所有直接或间接实现Collection接口的类都可以使用这些方法

注意：`Collection`接口继承`Iterable`接口,下文中有详细说明

```java
public interface Collection<E> extends Iterable<E> {
    Iterator<E> iterator(); //迭代器对象
 	boolean add(E e);  //像集合中添加元素
    boolean remove(E e);  //删除集合中某个元素
    void clear(); //清空集合中所有元素
	boolean contains(E e) //判断集合中是否包含某个元素
    boolean isEmpty(); ///判断集合是否为空
	int size(); //获取集合的长度
	Object[] toArray(); //将集合转变成一个数组
}
```

**注：Collection中的remove和contains方法底层需要重写equals方法**

## 一、Iterator接口和Iterable接口详解

### Iterator接口

Collection接口中有一个方法，叫`iterator()`,这个方法返回的就是迭代器的实现类对象

> 对`Iterator`接口的解释

+ Iterator也被叫做迭代器，迭代器提供了统一的语法进行集合对象（Collection）遍历操作，无需关心集合对象内部的实现方式
+ Iterator只能向前移，无法后退

+ Iterator迭代器是一个接口，无法直接使用，需要使用Iterator接口的实现类对象，

> 迭代器中的常用方法

```java
public interface Iterator<E> {
    boolean hasNext();//如果仍有元素可以迭代，则返回true
    E next();//返回迭代的下一个元素
    void remove();//删除当前迭代元素的值
}
```

```java
	public void func() {
        Collection<String> obj = new ArrayList<String>();
        obj.add("oliver");
        obj.add("loki");
        obj.add("twenty");
        Iterator<String> it = obj.iterator();
        while (it.hasNext()) {
            System.out.println(it.next());
            //删除当前迭代元素的值
            it.remove();
            //调用对象的remove方法会报并发异常：java.util.ConcurrentModificationException
            //obj.remove("oliver");
        }
        System.out.println(obj.isEmpty());
    }
```

**注:在迭代集合元素的过程中，不能调用对象的remove方法删除元素，否则会出现异常**

### Iterable接口

jdk1.5之后新增了Iterable接口**用于支持foreach循环**，所有实现Iterable接口的对象都可以实现foreach循环操作

> **==Jdk11中Iterable源码阅读==**
>
> ```java
> //Since:1.5
> public interface Iterable<T> {
>     //iterator()方法，返回集合的Iterator对象
>     Iterator<T> iterator();
> 
>     default void forEach(Consumer<? super T> action) {
>         Objects.requireNonNull(action);
>         for (T t : this) {
>             action.accept(t);
>         }
>     }
> 
>     default Spliterator<T> spliterator() {
>         return Spliterators.spliteratorUnknownSize(iterator(), 0);
>     }
> }
> 
> ```

## 二、Collections工具类

java.util.Collections是集合工具类，封装了集合的很多操作

> **常用方法**

```java
//像集合中添加一些元素
public static <T> boolean addAll(Collecrion<T> c,T... elements) 
//打乱集合顺序
public static void shuffle(<List<?> list) 
//将集合中元素按照默认规则排序
public static <T> void sort(List<T> list)  
//将及各种元素按照指定规则排序    
public static <T> void sort(List<T> list,Comparator<? super T>) 
```

> 示例代码
>

```java
public class CollectionsTest {
    //测试集合工具类的方法
    public void test() {
        List list = new ArrayList();
        //addAll方法添加多个元素
        Collections.addAll(list, "e", "d", "c", "b", "a");
        System.out.println(list);
        Collections.shuffle(list);//shuffle方法打乱集合顺序
        System.out.println(list);
        //对集合排序
        Collections.sort(list);//默认规则一般是升序
        System.out.println(list);
    }
```

> **`Collections.sort()`对自定义类的排序需要特别说明，直接排序会按照默认规则，不合理**
>
> 1. **使用`void sort(List<T> list,Comparator<? super T>) `方法，在Compartor中指定规则**
> 2. **对于自定义类需要实现`comparable`接口并重写`compareTo`方法**

代码示例

法一：在Compartor中指定规则

```java
	@Test
	public void methodOne() {
        ArrayList<Person> person = new ArrayList<>();
        person.add(new Person("张三", 27));
        person.add(new Person("李四", 79));
        person.add(new Person("王五", 20));

        Collections.sort(person, new Comparator<Person>() {
            @Override
            public int compare(Person o1, Person o2) {
                //升序排序
                return o1.getAge() - o2.getAge();
            }
        });
    }
```

**法二：实现`comparable`接口并重写`compareTo`方法**

```java
	public void methodTwo() {
        //对自定义类person排序，需要实现comparable接口并重写方法
        ArrayList<Person> person = new ArrayList<>();
        person.add(new Person("张三", 27));
        person.add(new Person("李四", 79));
        person.add(new Person("loki", 20));
        Collections.sort(person);
        System.out.println(person);
    }
```

```java
	@NoArgsConstructor
	@AllArgsConstructor
	@Data
    class Person implements Comparable<Person> {
        String name;
        int age;

        @Override
        public int compareTo(Person o) {
            //排序规则
            //this - 参数 > 0 => 升序，反之降序
            return o.getAge() - this.getAge();
        }
}
```

在Collection集合类中，`List`是最基础的一种集合：它是一种有序列表

## 三、List接口

### List接口方法与特性

> **List接口特有方法总结**
>
> 意味着所有直接或间接实现List接口的类都可以使用这些方法

```java
public interface List<E> extends Collection<E> {
    public void add(int index,E element); //将元素添加到指定位置
	public E get(int index) // 返回指定位置元素
    public E remove(int index) //移除列表指定位置的元素并将其作为返回值
    public E set(int index,E element) //用指定元素替换集合中指定位置的元素，返回更新前的元素 
    boolean equals(Object o);
}
```

**注：调用`List`的`contains()`、`indexOf()`这些方法，放入的元素需要实现`equals()`方法**

> **顺便说一下java对equals()的要求。有以下几点：**
>
> 1. `对称性`：如果x.equals(y)返回是"true"，那么y.equals(x)也应该返回是"true"
> 2. `反射性`：x.equals(x)必须返回是"true"
> 3. `类推性`：如果x.equals(y)返回是"true"，而且y.equals(z)返回是"true"，那么z.equals(x)也应该返回是"true"
> 4. `一致性`：如果x.equals(y)返回是"true"，只要x和y内容一直不变，不管你重复x.equals(y)多少次，返回都是"true"
> 5. `非空性`，x.equals(null)，永远返回是"false"；x.equals(和x不同类型的对象)永远返回是"false"

**除了以上方法，Jdk9中List接口，Set接口，Map接口中添加了一个静态的方法`of`，可以给集合一次性添加多个元素**

```java
public class of方法 {
    public static void main(String[] args) {
        //List.of
        List<String> list = List.of("a","b","c","d");
        //注：list.add("w"); 会报错，不支持操作异常
        System.out.println(list);
        //Set.of
        Set<String>  = Set.of("a", "b", "c", "a");
        //Map.of
        Map<String, ? extends Serializable> user = Map.of("name", "loki", "age", 20);
        System.out.println(user);
    }
}
```

注意事项：

+ 集合中存储的元素个数已经确定，不再改变，才可以使用这个方法

+ `List.of()`方法不接受`null`值(`List`的`add`方法接受null值)，如果传入`null`，会抛出`NullPointerException`异常
+ **`List.of()`，它返回的是一个只读`List`。对只读`List`调用`add()`、`remove()`方法会抛出	`UnsupportedOperationException`**

+ 只适用于List，Map，Set，不适用于接口的实现类
+ Set和Map接口在调用of方法时，不能有重复的元素，否则回抛出异常

> List集合的遍历方式

```java
	// for循环遍历方式
    for (String s : list){
        System.out.println(s);
    }
    // 迭代器遍历方式
    Iterator<String> ite= list.iterator();
    while(it.hasNext()){
        System.out.println(it.next());
    }
    // foreach遍历方式
    list.forEach(ites->{
        System.out.println(ites);
    });
```

**注：操作索引的时候一定要防止索引越界异常**

1. IndexOutOfBoundException : 索引越界异常，一般是集合
2. ArrayIndexOutofBoundException: 数组索引越界异常
3. StringIndexOutOfBoundException 字符串索引越界异常

### List与Array的转换

**一、将List变为Array**

​	第一种是调用`toArray()`方法直接返回一个`Object[]`数组，这种方法会丢失类型信息，所以实际应用很少。

```java
Object[] array = list.toArray();
```

​	第二种方式是给`toArray(T[])`传入一个类型相同的`Array`，`List`内部自动把元素复制到传入的`Array`中

```java
 Integer[] array = list.toArray(new Integer[3]);
```

​	更简洁的一种做法

```java
Integer[] array = list.toArray(new Integer[list.size()]);
//函数式的写法
Integer[] array = list.toArray(Integer[]::new);
```

**二、把Array变为List---`List.of（）`方法**

```java
List<Integer> list = List.of(array);
```

### ArrayList实现类（重要）

> 特性

+ `ArrayList` 是 `List` 的主要实现类，底层使用 `Object[ ]`存储，适用于频繁的查找工作，线程不安全 ；

+ **`ArrayList` 采用数组存储，所以插入和删除元素的时间复杂度受元素位置的影响。**

  比如：执行`add(E e)`方法的时候

  + `ArrayList` 会默认在将指定的元素追加到此列表的末尾，这种情况时间复杂度就是 O(1)

  + 如果要在指定位置 i 插入和删除元素的话（`add(int index, E element)`）时间复杂度就为 O(n-i)。因为在进行上述操作的时候集合中第 i 和第 i 个元素之后的(n-i)个元素都要执行向后位/向前移一位的操作

> ArrayList 自动扩容机制

**扩容步骤**

1. 创建一个更大的新的数组对象
2. 将老数组里面的元素复制到新数组里面
3. 改变引用指向
4. 回收老数组对象
5. 继续添加元素

**注：在项目开发的时候 尽量避免扩容**

> 自动扩容：//默认开辟10块空间
>
> + jdk6.0及之前	x * 3 / 2 + 1
>   10 -> 16 -> 25....
>
> + jdk7.0及之后	x + (x >> 1)
>   10 -> 15 -> 22....

> 构造方法

```java
	ArrayList list = new ArrayList(int 数组空间大小);
	ArrayList list = new ArrayList();
```

> 常用方法

```java
public boolean add(E e); //像集合当中添加元素，参数类型和泛型一致

注：对于ArrayList集合来说，add添加一定成功，所以返回值可用可不用，但是对于其他集合来说，add添加动作不一定成功，所以返回值需要使用
    
public E get(int index); //从集合当中获取元素，参数是索引编号，返回值就是对应位置的元素

public E remove(int index);//从集合中删除元素，参数是索引编号

public int size(); //获取集合的尺寸长度，返回值是集合中包含的元素个数,不是获取集合的容量
```

### LinkedList实现类

底层是链表结构

元素增删快，查询慢

LinkedList语法和ArrayList一样

### ArrayList与LinkedList辨析

|                     | ArrayList                                   | LinkedList                                                   |
| :------------------ | :------------------------------------------ | ------------------------------------------------------------ |
| 获取指定元素        | 速度很快                                    | 需要从头开始查找元素                                         |
| 添加元素到末尾      | 速度很快                                    | 速度很快                                                     |
| 在指定位置添加/删除 | 需要移动元素                                | 不需要移动元素                                               |
| 内存占用            | 较少（list 列表的结尾会预留一定的容量空间） | 较大（它的每一个元素都需要消耗比 ArrayList 更多的空间）      |
| 线程安全            | 不同步的，也就是不保证线程安全              | 不同步的，也就是不保证线程安全                               |
| 底层数据结          | `Object` 数组                               | **双向链表** 数据结构（JDK1.6 之前为循环链表，JDK1.7 变为双向链表） |

通常情况下，我们总是优先使用`ArrayList`



## 四、Set接口

### Set接口方法与特性

> Set特性

+ Set集合是继承Collection的接口，是一个不包含重复元素的集合
+ Set和List都是以接口的形式来进行声明
+ Set有三个主要实现类`HashSet`、`LinkedHashSet`、`TreeSet`
+ 与List一样，它同样允许null的存在但是仅有一个

> **Set注重独一无二的性质，不会存储重复的元素**

需要说明的是，在set接口中的不重复是有特殊要求的

**HashSet存储自定义类型的元素时**，需要同时重写hashCode和equals方法，建立自己的比较方式，才能保证hashSet中集合的对象唯一



> **遍历Set集合**

基本上HashSet、LinkedHashSet和TreeSet采用的遍历方式都是一样的，不同的是三者输出结果的顺序不同

```java
	// for循环遍历方式
    for (String s : set){
        System.out.println(s);
    }
    // 迭代器遍历方式
    Iterator<String> ite = set.iterator();
    while(ite.hasNext()){
        System.out.println(ite.next());
    }
    // foreach遍历方式
    set.forEach(ites->{
        System.out.println(ites);
    });
```

### HashSet实现类（重要）

> 如果你看过 `HashSet` 源码的话就应该知道：`HashSet` 底层就是基于 `HashMap` 实现的

HashSet的实现方式大致如下，通过一个HashMap存储元素，元素是存放在HashMap的Key中，而Value统一使用一个Object对象。

`HashSet` 的源码非常非常少，因为除了 `clone()`、`writeObject()`、`readObject()`是 `HashSet` 自己不得不实现之外，其他方法都是直接调用 `HashMap` 中的方法



**:rocket:辨析一下 HashMap 和 HashSet**

| `HashMap`                              | `HashSet`                                                    |
| -------------------------------------- | ------------------------------------------------------------ |
| 实现了 `Map` 接口                      | 实现 `Set` 接口                                              |
| 存储键值对                             | 仅存储对象                                                   |
| 调用 `put()`向 map 中添加元素          | 调用 `add()`方法向 `Set` 中添加元素                          |
| `HashMap` 使用键（Key）计算 `hashcode` | `HashSet` 使用成员对象来计算 `hashcode` 值，对于两个对象来说 `hashcode` 可能相同，所以` equals()`方法用来判断对象的相等性 |

#### HashSet如何检查重复

> **摘自《Head first java》第二版**

当你把对象加入`HashSet`时，`HashSet` 会先计算对象的`hashcode`值来判断对象加入的位置，同时也会与其他加入的对象的 `hashcode` 值作比较，如果没有相符的 `hashcode`，`HashSet` 会假设对象没有重复出现。但是如果发现有相同 `hashcode` 值的对象，这时会调用`equals()`方法来检查 `hashcode` 相等的对象是否真的相同。如果两者相同，`HashSet` 就不会让加入操作成功

> **`hashCode()`与 `equals()` 的相关规定**

1. 如果两个对象相等，则 `hashcode` 一定也是相同的
2. 两个对象相等，对两个 `equals()` 方法返回 true
3. 两个对象有相同的 `hashcode` 值，它们也不一定是相等的

综上，如果一个类的 `equals()` 方法被覆盖过，则 `hashCode()` 方法也必须被覆盖。

` hashCode()` 的默认⾏为是对堆上的对象产⽣独特值。如果没有重写 `hashCode() `，即使通过 `equals()` 判断为相同的两个对象，在加入 `HashSet` 时，也不会被 `HashSet` 认为是重复对象

#### HashSet总结

1. **HashSet中存放null值**

   HashSet 中是允许存入 null 值的，但是在 HashSet 中仅仅能够存入一个 null 值

2. **HashSet中存储元素的位置是固定的**

   HashSet中存储的元素的是无序的，这个没什么好说的，但是由于HashSet底层是基于Hash算法实现的，使用了hashcode，所以HashSet中相应的元素的位置是固定的

3. **必须小心操作可变对象**（`Mutable Object`）

   如果一个Set中的可变元素改变了自身状态导致`Object.equals(Object)=true`将导致一些问题

4. **HashSet 不保证元素的顺序**

   这里所说的没有顺序是指：元素插入的顺序与输出的顺序不一致

5. **HashSet 不是线程安全的**

6. **如果多个线程尝试同时修改 HashSet，则最终结果是不确定的**

   你必须在多线程访问时显式同步对 HashSet 的并发访问。

7. HashSet按Hash算法来存储集合的元素，因此**具有很好的存取和查找性能**

### LinkedHashSet实现类

+ LinkedHashSet继承 HashSet，其底层是**基于LinkedHashMap来实现的**

+ LinkedHashSet中不能有相同元素，可以有一个Null元素

+ 线程不安全

+ LinkedHashSet集合同样是根据元素的hashCode值来决定元素的存储位置，但是它同时**使用链表维护元素的次序**。这样使得元素看起来像是以插入顺序保存的，也就是说，当遍历该集合时候，**LinkedHashSet将会以元素的添加顺序访问集合的元素**

  即：**链表保证了元素的有序即存储和取出一致，哈希表保证了元素的唯一性**

### TreeSet实现类

> 特性

1.TreeSet是中不能有相同元素，不可以有Null元素，根据元素的自然顺序进行排序。

2.TreeSet如何保证元素的排序和唯一性？

底层的数据结构是红黑树(一种自平衡二叉查找树)

3.添加、删除操作时间复杂度都是O(log(n))

4.非线程安全

> 注意：TreeSet集合不是通过hashcode和equals函数来比较元素的.它是通过compare或者compareTo函数来判断元素是否相等.compare函数通过判断两个对象的id，相同的id判断为重复元素，不会被加入到集合中

### HashSet、LinkedHashSet、TreeSet 三者比较

:rocket:**三者都保证了元素的唯一性**

**如果无排序要求可以选用HashSet；**

**如果想取出元素的顺序和放入元素的顺序相同，那么可以选用LinkedHashSet。**

**如果想插入、删除立即排序或者按照一定规则排序可以选用TreeSet**



# 5）Map接口

Map与List、Set接口不同，它是由一系列键值对组成的集合，提供了key到Value的映射。同时它也没有继承Collection。在Map中它保证了key与value之间的一一对应关系。也就是说一个key对应一个value，所以它**不能存在相同的key值，当然value值可以相同**

## Map接口方法与特性

> **Map集合结构**

![image-20220124222131189](https://s2.loli.net/2022/01/24/B4y3ANnbmOix2EL.png)

> **Map集合特性**

1. Map是<K,V>集合双列集合最顶层的接口，一个元素包含两个值<key,value>
2. key不允许重复，value允许重复
3. key和value一一对应
4. key和value都是引用数据类型
5. key和value都是存储对象的内存地址

> **常用实现类**

+ HashMap
+ LinkedHashMap
+ TreeMap

> **Map集合常用的方法**

```java
public V put(K key, V value) //像Map集合中添加键值对
public V remove(Object key) //删除指定键所对应的键值对，并返回V值
public V get(Object key) //返回指定键所映射的值；如果此映射不包含该键的映射关系，则返回 null。
boolean containsKey(Object key) //判断Map中是否包含某个Key
boolean containsValue(Object value) //判断Map中是否包含某个Value
void clear() //清空Map集合
boolean isEmpty() //判断Map集合是否为空
public Set(K) keySet() //获取Map集合所有的key,存储到set集合中
int Size() //返回此映射中的键-值映射关系数。
Collection<V> values() //返回此映射中包含的值的 Collection 视图。
public Set<Map.Entry<K,V>> entrySet() //获取到Map集合中所有键值对对象的集合(Set集合)===Map集合全部转换成Set集合，Set集合中的元素类型是:Map.Entry
```

> TODO:掌握map.put(k,v)和map.get(key)的实现原理，掌握哈希表
>
> 在存取过程中，都是先调用hashCode方法再调用equals方法，equals方法有可能调用，也有可能不调用

## Map集合的遍历

```java
//推荐使用的方式----entrySet方法
for (Map.Entry<Integer, String> node : map.entrySet()) {
    System.out.println(node.getKey() + "=" + node.getValue());
}
//foreach
for (String s : map.values()) {
    System.out.println(s).;
}

//keySet方法
//将map集合中所有的key取出来存储到一个set集合中，遍历set集合键找值
for (Integer key : map.keySet()) {
    String s = map.get(key);
    System.out.println(s);
}
```

## HashMap实现类（重点）

> 特性

+ `Hashmap` 是一个最常用的Map，它根据键的 HashCode 值存储数据，根据键可以直接获取它的值，具有很快的访问速度。遍历时，取得数据的顺序是完全随机的

+ HashMap最多只允许一条记录的键为Null；允许多条记录的值为Null
+ HashMap不支持线程的同步,如果需要同步，可以用Collections的synchronizedMap方法使HashMap具有同步的能力。

### HashMap的底层实现

> jdk1.8之前 ： 数组 + 单向链表

HashMap 通过 key 的 hashCode 经过扰动函数处理过后得到 hash 值，然后通过 (n - 1) & hash 判断当前元素存放的位置（这里的 n 指的是数组的长度），如果当前位置存在元素的话，就判断该元素与要存入的元素的 hash 值以及 key 是否相同，如果相同的话，直接覆盖，不相同就通过拉链法解决冲突。

+ **扰动函数**指的就是 HashMap 的 hash 方法。使用 hash 方法也就是扰动函数是为了防止一些实现比较差的 hashCode() 方法 换句话说使用扰动函数之后可以减少碰撞
+ **拉链法**：将链表和数组相结合。也就是说创建一个链表数组，数组中每一格就是一个链表。若遇到哈希冲突，则将冲突的值加到链表中即可

> jdk1.8之后 ： 数组 + 单项链表/红黑树（链表长度超过8）

相比于之前的版本， JDK1.8 之后在解决哈希冲突时有了较大的变化，当链表长度大于阈值（默认为 8）（将链表转换成红黑树前会判断，如果当前数组的长度小于 64，那么会选择先进行数组扩容，而不是转换为红黑树）时，将链表转化为红黑树，以减少搜索时间

### HashMap存储自定义类型键值对

由于map集合要保证key的唯一性，但**实际需求中可能相同的key值代表不同的对象**，因此，需要重写hashCode和equals方法

```java
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HashMap存储自定义键值对 {
    public void test01() {
    //key使用String类型，已经重写hashCode和equals方法
    //结果==>北京的两个人不能并存
    //value：Person类型，是可重复的
        HashMap<String,Person> map = new HashMap<>();
        map.put("北京",new Person("张三",18));
        map.put("上海",new Person("李四",18));
        map.put("广州",new Person("王五",18));
        map.put("北京",new Person("赵六",18));
        //遍历
        for(Map.Entry<String,Person> m: map.entrySet()){
            System.out.println(m.getKey()+"--->"+m.getValue());
        }
    }
    @Test
    public void test02(){
        //key使用Person类型，已经重写hashCode和equals方法
        //结果==>北京的两个人可以并存
        //value：String类型，是可重复的
        HashMap<Person,String> map = new HashMap<>();
        map.put(new Person("张三",18),"北京");
        map.put(new Person("李四",18),"上海");
        map.put(new Person("王五",18),"广州");
        map.put(new Person("赵六",18),"北京");
        //遍历
        for(Map.Entry<Person,String> m: map.entrySet()){
            System.out.println(m.getKey()+"--->"+m.getValue());
        }
    }
}
	@NoArgsConstructor
	@AllArgsConstructor
	@Data
class Person{
    private String name;
    private int age;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return age == person.age &&
                Objects.equals(name, person.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }
}
```

### HashMap 多线程操作导致死循环问题

主要原因在于并发下的 Rehash 会造成元素之间会形成一个循环链表。不过，jdk 1.8 后解决了这个问题，但是还是不建议在多线程下使用 HashMap,因为多线程下使用 HashMap 还是会存在其他问题比如数据丢失。并发环境下推荐使用 `ConcurrentHashMap`



推荐阅读：https://coolshell.cn/articles/9606.html

## LinkedHashMap实现类

1. LinkedHashMap是HashMap的一个子类，底层是哈希表+链表（保证**集合有序**，多一层链表保证了迭代的顺序，如果需要输出的顺序和输入时的相同，那么就选用LinkedHashMap。
2. **LinkedHashMap是Map接口的哈希表和链接列表实现，具有可预知的迭代顺序。**此实现提供所有可选的映射操作，并允许使用null值和null键。此类不保证映射的顺序，特别是它不保证该顺序恒久不变。
3. LinkedHashMap实现与HashMap的不同之处在于，后者维护着一个运行于所有条目的双重链接列表。此链接列表定义了迭代顺序，该迭代顺序可以是插入顺序或者是访问顺序。
4. 根据链表中元素的顺序可以分为：按插入顺序的链表，和按访问顺序(调用get方法)的链表。默认是按插入顺序排序，如果指定按访问顺序排序，那么调用get方法后，会将这次访问的元素移至链表尾部，不断访问可以形成按访问顺序排序的链表。

> 注意，此实现不是同步的。如果多个线程同时访问链接的哈希映射，而其中至少一个线程从结构上修改了该映射，则它必须保持外部同步。由于LinkedHashMap需要维护元素的插入顺序，因此性能略低于HashMap的性能，但在迭代访问Map里的全部元素时将有很好的性能，因为它以链表来维护内部顺序。

## TreeMap实现类

> **说明**

**TreeMap 是一个有序的key-value集合，非同步，实现SortMap接口,基于红黑树（Red-Black tree）实现，每一个key-value节点作为红黑树的一个节点。**

> **存储时就排序的特性**

TreeMap存储时会进行排序的，会根据key来对key-value键值对进行排序，其中排序方式也是分为两种，一种是自然排序，一种是定制排序，具体取决于使用的构造方法

+ **自然排序**：TreeMap中所有的key必须实现Comparable接口，并且所有的key都应该是同一个类的对象，否则会报ClassCastException异常。

+ **定制排序**：定义TreeMap时，创建一个comparator对象，该对象对所有的treeMap中所有的key值进行排序，采用定制排序的时候不需要TreeMap中所有的key必须实现Comparable接口。

> **TreeMap判断两个元素相等的标准：两个key通过`compareTo()`方法返回0，则认为这两个key相等。**
>
> **如果使用自定义的类来作为TreeMap中的key值，且想让TreeMap能够良好的工作，则必须重写自定义类中的`equals()`方法，TreeMap中判断相等的标准是：两个key通过`equals()`方法返回为true，并且通过`compareTo()`方法比较应该返回为0**

## Hashtable实现类

+ since jdk1.0
+ 线程安全，单线程，速度慢
+ 和vector一样，在jdk1.2版本后被更先进的集合取代了（HashMap,ArrayList)
+ 但是Hashtable的子类Properties仍然使用
+ Properties集合是唯一一个和IO流相结合的集合
+ Hashtable 不允许键值对为空，但是HashMap允许

## 异同点的比较

**HashMap、Hashtable、LinkedHashMap和TreeMap比较**

> `LinkedHashMap`保存了记录的插入顺序，在用Iterator遍历LinkedHashMap时，先得到的记录肯定是先插入的，也可以在构造时用带参数，按照应用次数排序。在遍历的时候会比HashMap慢，不过有种情况例外，**当HashMap容量很大，实际数据较少时，遍历起来可能会比LinkedHashMap慢，因为LinkedHashMap的遍历速度只和实际数据有关，和容量无关，而HashMap的遍历速度和他的容量有关。**

> **如果需要输出的顺序和输入的相同，那么用LinkedHashMap可以实现，它还可以按读取顺序来排列，像连接池中可以应用。LinkedHashMap实现与HashMap的不同之处在于，后者维护着一个运行于所有条目的双重链表。此链接列表定义了迭代顺序，该迭代顺序可以是插入顺序或者是访问顺序。对于LinkedHashMap而言，它继承与HashMap、底层使用哈希表与双向链表来保存所有元素。其基本操作与父类HashMap相似，它通过重写父类相关的方法，来实现自己的链接列表特性。**



> **一般情况下，我们用的最多的是HashMap，HashMap里面存入的键值对在取出的时候是随机的，它根据键的HashCode值存储数据，根据键可以直接获取它的值，具有很快的访问速度。在Map 中插入、删除和定位元素，HashMap 是最好的选择。**
>
> **TreeMap取出来的是排序后的键值对。但如果您要按自然顺序或自定义顺序遍历键，那么TreeMap会更好。**

# 6）Properties配置文件详解

在编写应用程序的时候，经常需要读写配置文件。例如，用户的设置

```
# 上次最后打开的文件:
last_open_file=/data/hello.txt
# 自动保存文件的时间间隔:
auto_save_interval=60
```

配置文件的特点是，它的Key-Value一般都是`String`-`String`类型的，因此我们完全可以用`Map<String, String>`来表示它。

因为配置文件非常常用，所以Java集合库提供了一个`Properties`来表示一组“配置”。由于历史遗留原因，`Properties`内部本质上是一个`Hashtable`，但我们只需要用到`Properties`自身关于读写配置的接口。

### 读取配置文件

用`Properties`读取配置文件非常简单。Java默认配置文件以`.properties`为扩展名，每行以`key=value`表示，以`#`课开头的是注释。以下是一个典型的配置文件：

```
# setting.properties
last_open_file=/data/hello.txt
auto_save_interval=60
```

可以从文件系统读取这个`.properties`文件：

```
String f = "setting.properties";
Properties props = new Properties();
props.load(new java.io.FileInputStream(f));

String filepath = props.getProperty("last_open_file");
String interval = props.getProperty("auto_save_interval", "120");
```

可见，用`Properties`读取配置文件，一共有三步：

1. 创建`Properties`实例；
2. 调用`load()`读取文件；
3. 调用`getProperty()`获取配置。

调用`getProperty()`获取配置时，如果key不存在，将返回`null`。我们还可以提供一个默认值，这样，当key不存在的时候，就返回默认值。

也可以从classpath读取`.properties`文件，因为`load(InputStream)`方法接收一个`InputStream`实例，表示一个字节流，它不一定是文件流，也可以是从jar包中读取的资源流：

```java
Properties props = new Properties();
props.load(getClass().getResourceAsStream("/common/setting.properties"));
```



如果有多个`.properties`文件，可以反复调用`load()`读取，后读取的key-value会覆盖已读取的key-value：

```java
Properties props = new Properties();
props.load(getClass().getResourceAsStream("/common/setting.properties"));
props.load(new FileInputStream("C:\\conf\\setting.properties"));
```

上面的代码演示了`Properties`的一个常用用法：可以把默认配置文件放到classpath中，然后，根据机器的环境编写另一个配置文件，覆盖某些默认的配置。

`Properties`设计的目的是存储`String`类型的key－value，但`Properties`实际上是从`Hashtable`派生的，它的设计实际上是有问题的，但是为了保持兼容性，现在已经没法修改了。除了`getProperty()`和`setProperty()`方法外，还有从`Hashtable`继承下来的`get()`和`put()`方法，这些方法的参数签名是`Object`，我们在使用`Properties`的时候，不要去调用这些从`Hashtable`继承下来的方法。

### 写入配置文件

如果通过`setProperty()`修改了`Properties`实例，可以把配置写入文件，以便下次启动时获得最新配置。写入配置文件使用`store()`方法：

```
Properties props = new Properties();
props.setProperty("url", "http://www.liaoxuefeng.com");
props.setProperty("language", "Java");
props.store(new FileOutputStream("C:\\conf\\setting.properties"), "这是写入的properties注释");
```

### 编码

早期版本的Java规定`.properties`文件编码是ASCII编码（ISO8859-1），如果涉及到中文就必须用`name=\u4e2d\u6587`来表示，非常别扭。从JDK9开始，Java的`.properties`文件可以使用UTF-8编码了。

不过，需要注意的是，由于`load(InputStream)`默认总是以ASCII编码读取字节流，所以会导致读到乱码。我们需要用另一个重载方法`load(Reader)`读取：

```
Properties props = new Properties();
props.load(new FileReader("settings.properties", StandardCharsets.UTF_8));
```

就可以正常读取中文。`InputStream`和`Reader`的区别是一个是字节流，一个是字符流。字符流在内存中已经以`char`类型表示了，不涉及编码问题。

### 小结

Java集合库提供的`Properties`用于读写配置文件`.properties`。`.properties`文件可以使用UTF-8编码。

可以从文件系统、classpath或其他任何地方读取`.properties`文件。

读写`Properties`时，注意仅使用`getProperty()`和`setProperty()`方法，不要调用继承而来的`get()`和`put()`等方法。

# 7）大总结

## 集合框架底层数据结构总结

先来看一下 `Collection` 接口下面的集合。

> List

- `Arraylist`： `Object[]`数组
- `Vector`：`Object[]`数组
- `LinkedList`： 双向链表(JDK1.6 之前为循环链表，JDK1.7 取消了循环)

> Set

- `HashSet`（无序，唯一）: 基于 `HashMap` 实现的，底层采用 `HashMap` 来保存元素
- `LinkedHashSet`：`LinkedHashSet` 是 `HashSet` 的子类，并且其内部是通过 `LinkedHashMap` 来实现的。有点类似于我们之前说的 `LinkedHashMap` 其内部是基于 `HashMap` 实现一样，不过还是有一点点区别的
- `TreeSet`（有序，唯一）： 红黑树(自平衡的排序二叉树)

再来看看 `Map` 接口下面的集合。

> Map

- `HashMap`： JDK1.8 之前 `HashMap` 由数组+链表组成的，数组是 `HashMap` 的主体，链表则是主要为了解决哈希冲突而存在的（“拉链法”解决冲突）。JDK1.8 以后在解决哈希冲突时有了较大的变化，当链表长度大于阈值（默认为 8）（将链表转换成红黑树前会判断，如果当前数组的长度小于 64，那么会选择先进行数组扩容，而不是转换为红黑树）时，将链表转化为红黑树，以减少搜索时间
- `LinkedHashMap`： `LinkedHashMap` 继承自 `HashMap`，所以它的底层仍然是基于拉链式散列结构即由数组和链表或红黑树组成。另外，`LinkedHashMap` 在上面结构的基础上，增加了一条双向链表，使得上面的结构可以保持键值对的插入顺序。同时通过对链表进行相应的操作，实现了访问顺序相关逻辑。详细可以查看：[《LinkedHashMap 源码详细分析（JDK1.8）》](https://www.imooc.com/article/22931)
- `Hashtable`： 数组+链表组成的，数组是 `HashMap` 的主体，链表则是主要为了解决哈希冲突而存在的
- `TreeMap`： 红黑树（自平衡的排序二叉树）

## 如何选用合适的集合框架

主要根据集合的特点来选用，比如我们需要根据键值获取到元素值时就选用 `Map` 接口下的集合，需要排序时选择 `TreeMap`,不需要排序时就选择 `HashMap`,需要保证线程安全就选用 `ConcurrentHashMap`。

当我们只需要存放元素值时，就选择实现`Collection` 接口的集合，需要保证元素唯一时选择实现 `Set` 接口的集合比如 `TreeSet` 或 `HashSet`，不需要就选择实现 `List` 接口的比如 `ArrayList` 或 `LinkedList`，然后再根据实现这些接口的集合的特点来选用。





















