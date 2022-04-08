win+r输入calc，按下回车然后计算



# 算法

## DFS







## BFS



## 优化全排列





## 位运算

> 基本规则

| 符号 | 描述 | 运算规则                                                     |
| :--- | :--- | :----------------------------------------------------------- |
| &    | 与   | 两个位都为1时，结果才为1                                     |
| \|   | 或   | 两个位都为0时，结果才为0                                     |
| ^    | 异或 | 两个位相同为0，相异为1                                       |
| ~    | 取反 | 0变1，1变0                                                   |
| <<   | 左移 | 各二进位全部左移若干位，高位丢弃，低位补0                    |
| >>   | 右移 | 各二进位全部右移若干位，对无符号数，高位补0，有符号数，各编译器处理方法不一样，有的补符号位（算术右移），有的补0（逻辑右移） |









## 双指针

例题

> 给定一个数组 `nums`，编写一个函数将所有 `0` 移动到数组的末尾，同时保持非零元素的相对顺序。
>
> **请注意** ，必须在不复制数组的情况下原地对数组进行操作。

思路及解法

使用双指针，左指针指向当前已经处理好的序列的尾部，右指针指向待处理序列的头部。

右指针不断向右移动，每次右指针指向非零数，则将左右指针对应的数交换，同时左指针右移。

注意到以下性质：

左指针左边均为非零数；

右指针左边直到左指针处均为零。

因此每次交换，都是将左指针的零与右指针的非零数交换，且非零数的相对顺序并未改变。

```java
public void moveZeroes(int[] nums) {
        int j= 0;//记录非零元素的个数
        for(int i = 0;i<nums.length;i++){
            if(nums[i]!=0)
                nums[j++] = nums[i]; 
        }
        //将非零元素放在开头
        for(int k = j; k < nums.length;k++){
            nums[k]=0; 
        }
    }
```



# Java Api

## String类

### 字符串大小写转化

```
str.toLowerCase() 
```

toLowerCase()方法将String转换为小写

```
str.toUpperCase()
```

toUpperCase()方法将Srtring转换为大写。如果字符串中没有应该转换的字符，则将原字符串返回，否则返回一个新的字符串。

使用toLowerCase()方法和toUpperCase()方法进行大小写转换时，数字或非字符不受影响



### StringBuffer

> 相当于可变字符串，自带翻转等操作

```java
	String a = "hello";
	StringBuffer str = new StringBuffer(a);
	a.revrese().toString();//翻转字符串
```



## Java内置进制转换

进制转化在JAVA中已经封装好了.Integer和Long类都有对应方法

```java
10进制转其他进制
    Integer.toString(n, r);            n:十进制数，r:要转换的进制
其他进制转10进制
    Integer.parseInt((String) s,(int) radix); radix进制的字符串s转10进制
```

## int转int[]

```java
public static void toArrInt(int num) {
        ArrayList<Integer> list = new ArrayList<>();
        for(int i=0;i<String.valueOf(num).length();i++){
   list.add(Integer.parseInt(String.valueOf(String.valueOf(num).charAt(i))));
        }
        for (int s:list){
            System.out.print(s+" ");
        }
    }
```

## Java高精度运算

> Bigdecimal类

有加减乘除的方法，分别是add,substract,multiply,divide.他们的参数都是Bigdecimal类型的字符串

```java
public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
    	//Scanner有接受Bigdecimal的方法
        BigDecimal sum = new BigDecimal(Integer.MIN_VALUE);
    	//注意这一点
        BigDecimal num1 = sc.nextBigDecimal();
        BigDecimal num2 = sc.nextBigDecimal();
        sum = num1.add(num2);
        System.out.println(sum);
    }
```

**有BigDecimal类型的常数， 例如BigDecimal.ONE**

```java
	BigDecimal one = BigDecimal.ONE;
```

**将一个数字或者字符串转化成BigDecimal格式的数字，只需要使用传进它的构造方法就可以了**

```java
   BigDecimal abig = new BigDecimal(10.0)；
```

**对高精度除法做一个说明**

```java
	//如果不能整除，会报错java.lang.ArithmeticException
	//所以不能做整除的数，需要在除法中设置精度
	public class Test {
    public static void main(String[] args) {
        BigDecimal num1 = BigDecimal.valueOf(10);
        BigDecimal num2 = BigDecimal.valueOf(3);
        BigDecimal res = num1.divide(num2,200,BigDecimal.ROUND_HALF_UP);
        res.setScale(100);
        System.out.println(res);
    }
}
```

**BigDecimal有一个设置数字精度的方法**

setScale，在要求精度的比赛中经常用到。他有两个参数：
第一个是数字，表示小数点后面要精确的位数

第二个是舍入的方式。

关于舍入方式，有以下：

>   BigDecimal.ROUND_HALF_UP   这个是经常用到的，需要记住，就是经常使用的四舍五入。
>
>   BigDecimal.ROUND_HALF_DOWN  四舍六入。
>
>   BigDecimal.ROUND_HALF_EVEN   四舍六入，如果是五，分两种情况，如果前一位是奇数，则入位，否则舍去。	

**高精度的比较**

不是使用>,<,==这样的运算符，它有自己的方法

```java
    int flag = num1.compareTo(num2)
    flag = -1,表示num1小于num2
    flag = 0,表示num1等于num2
    flag = 1,表示num1大于num2
    //实际中直接跟0比较就可以了，别跟-1或者1比较。
```



**int转bigdecimal**

```java
   BigDecimal number = new BigDecimal(0);
   int value=score;
   number=BigDecimal.valueOf((int)value);
```


**bigdecimal转int**

```java
BigDecimal b = new BigDecimal(45.45);
int a = b.intValue();
```





**计算阶乘**

```java
import java.math.BigDecimal;
import java.util.Scanner;
public class Demo04 {
    public static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {
        System.out.println("请输入您要计算的阶乘数：");
        long num = sc.nextLong();
        BigDecimal sum = new BigDecimal("1");
        for (int i = 1; i <= num; i++) {
            sum = sum.multiply(BigDecimal.valueOf(i));
        }
        System.out.println(num + "的阶乘：" + sum);
        System.out.println(num + "的阶乘位数：" + sum.toString().length());
    }
}
```

## 涉及到日期的计算--Calender

**首先辨析一下Calendar与Date**

> `java.util.Date` 是个日期数据；`java.util.Calendar` 用于日期相关的计算，从 JDK 1.1 开始，应使用 Calendar 类实现日期和时间字段之间转换，使用 DateFormat 类来格式化和解析日期字符串。Date 中的相应方法已废弃（查阅自 API 文档）

+ ```
星期六为0，星期日为1，星期一为2
  0代表一月份，11代表12月份
  ```
  
+ 实例化当前时间

  ```java
  Calendar date = Calendar.getInstance();
  System.out.println(date.toString());
  ```
  
+ 初始化date时间，不初始化默认为系统当前时间

  ```java
  date.set(2021, Calendar.MAY, 5, 8, 0, 0);
  ```

+ 对日期进行操作。即日期加减：年月日以及小时分秒等

  ```java
  void add(int field, int amount) 
  date.add(Calendar.MINUTE, -10);
  ```

  - field表示时间量：可以为如下：

    `Calendar.ERA`：表示无加减；

    `Calendar.YEAR`：年

    `Calendar.MONTH`：月

    `Calendar.DATE`：日

    `Calendar.HOUR`：小时

    `Calendar.MINUTE`：分钟

    `Calendar.SECOND`：秒

    注意：`Calendar`中的所有常量都可以为参数，不一定局限于上面的。比如`Calendar.HOUR_OF_DAY`,`Calendar.WEEK_OF_MONTH`等。不管是什么，只要这个值和周相关就是周（月中的第几周，年中的第几周等），和日相关就是日（一年当中的第几天，一月当中的第几天，一周当中的第几天），和小时相关就是小时

  - amount：为整数表示当前时间的基础上加上对应的时间量；为负数表示当前时间的基础上减去对应的时间量。

+ 格式化输出时间

  ```java
  SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  //注意M的大小写一定要区分开，其他无所谓
  System.out.println(format.format(date.getTime()));
  ```

+ calender类碰到毫秒运算时，不足一秒会自动补全

## Math类

> Math类的常用方法

```java
Math.sqrt(double n)  //开方运算
Math.pow(double a, double b) //指数运算
```

## Arrays

### sort

```java
//降序排列
//从fromIndex到toIndex-1的元素排序!!!
Arrays.sort(arr, fromIndex, toIndex, new Comparator<Integer>() {
    public int compare(Integer a, Integer b) {
    return b - a;
	}
});
```

### fill

```java
Arrays.fill（ a1, value ; //作用：填充a1数组中的每个元素都是value
```



## 集合

### HashMap

> 遍历

```java
for (Map.Entry<Integer, String> node : map.entrySet()) {
    System.out.println(node.getKey() + "=" + node.getValue());
}
```

### TreeSet

> 遍历

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
```



## 对于Scanner输入的一些坑

> sc.nextInt()

获取下一个int型数字

> sc.next()

next()从遇到第一个有效字符（非空格、换行符）开始扫描，遇到第一个[分隔符](https://so.csdn.net/so/search?q=分隔符&spm=1001.2101.3001.7020)或结束符（空格’ ‘或者换行符 ‘\n’）时结束。

> sc.nextLine()

nextLine()则是扫描剩下的所有字符串知道遇到回车为止





# 刷题思路与总结

## 求斐波那契数列

求两个比较大的斐波那契数

```java
public static void main(String[] args) {
        BigInteger a = BigInteger.ONE;
        BigInteger b = BigInteger.ONE;
        for(int i = 0;i < 1000;i ++) {
            a = a.add(b);
            b = b.add(a);
        }
}   
```

递归求斐波那契数列数列和

```java

```



## 求一个比较大的数的所有因子

```java
		for (long i = 1; i * i <= n; i++) {//求所有公因子的方法 很大程度上提高了效率
            if (n % i == 0) {
                list.add(i);
                if (i != n / i) {
                    list.add(n / i);
                }
            }
        }
```



## 求最小公倍数和最大公约数

欧几里德算法又称辗转相除法，用于计算两个整数a,b的最大公约数`GCD(greatest common divisor)`。其计算原理依赖于下面的定理：

定理：gcd(a,b) = gcd(b,a mod b)

```java
int Gcd(int a, int b)
{
    if(b == 0)
        return a;
    return Gcd(b, a % b);
}
```

> 利用最大公约数求最小公倍数

两个数的乘积等于这两个数的最大公约数与最小公倍数的乘积。假设有两个数是a、b，它们的最大公约数是p，最小公倍数是q。那么存在这样的关系式：ab=pq

## 杨辉三角形

```java
        for (int i = 0; i < 50; i++) {//前50行
            for (int j = 0; j < i; j++) {//遍历每个数
                if (i==j||j==0){
                    longs[i][j]=1;
                } else {
                    longs[i][j]=longs[i-1][j-1]+longs[i-1][j];
                }            
            }
        }
```

## 素数打表







### 子集

对N个元素的集合

![image-20220312174827263](https://s2.loli.net/2022/03/12/APVmFC4WGUo2Dsh.png)



### 全排列





