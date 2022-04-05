## 题一：ASCII码

> 【问题描述】
> 已知大写字母 A 的 ASCII 码为 65，请问大写字母 L 的 ASCII 码是多少？
> 【答案提交】
> 这是一道结果填空的题，你只需要算出结果后提交即可。本题的结果为一
> 个整数，在提交答案时只填写这个整数，填写多余的内容将无法得分。

> 答案： 简单，不做解释
> 76

## 题二：卡片--暴力循环

> 小蓝有很多数字卡片，每张卡片上都是数字 0 到 9。
> 小蓝准备用这些卡片来拼一些数，他想从 1 开始拼出正整数，每拼一个，就保存起来，卡片就不能用来拼其它数了。
> 小蓝想知道自己能从 1 拼到多少。
> 例如，当小蓝有 30 张卡片，其中 0 到 9 各 3 张，则小蓝可以拼出 1 到 10，但是拼 11 时卡片 1 已经只有一张了，不够拼出 11。
> 现在小蓝手里有 0 到 9 的卡片各 2021 张，共 20210 张，请问小蓝可以从 1拼到多少？
> 提示：建议使用计算机编程解决问题。

> 思路：
>
> 暴力循环，注意循环结束的条件
>
> 答案：
>
> 3181

```java
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        //0-9出现的次数
        int index[] = new int[9];

        flag:for (int i = 1; ; i++) {
            int[] ints = new int[String.valueOf(i).length()];
            //把int 转为int 数组
            for (int j = 0; j < String.valueOf(i).length(); j++) {
                //获得该数组的每一个数字
                ints[j] = Integer.parseInt(String.valueOf(String.valueOf(i).charAt(j)));
                //遍历添加数字
                for (int k = 0; k < 9; k++) {
                    if(ints[j]==k){
                        index[k]++;
                        //注意结束的条件，当等于2021时，说明卡片全部用完，这个时候输出i
                        if (index[k]==2021){
                            System.out.println(i);
                            System.out.println("此时用牌的数量"+Arrays.toString(index));
                            break flag;
                        }
                    continue;
                    }
                }
            }
        }
    }
}
```

## 题三：直线--Set集合/浮点数运算

> 在平面直角坐标系中，两点可以确定一条直线。如果有多点在一条直线上， 那么这些点中任意两点确定的直线是同一条。
> 给定平面上 2 × 3 个整点 {(x, y)|0 ≤ x < 2, 0 ≤ y < 3, x ∈ Z, y ∈ Z}，即横坐标是 0 到 1 (包含 0 和 1) 之间的整数、纵坐标是 0 到 2 (包含 0 和 2) 之间的整数的点。这些点一共确定了 11 条不同的直线。
> 给定平面上 20 × 21 个整点 {(x, y)|0 ≤ x < 20, 0 ≤ y < 21, x ∈ Z, y ∈ Z}，即横坐标是 0 到 19 (包含 0 和 19) 之间的整数、纵坐标是 0 到 20 (包含 0 和 20) 之间的整数的点。请问这些点一共确定了多少条不同的直线

> 思路：
>
> 直线 y = kx + b ==》k和b相同就是一条直线
>
> 暴力循环，并且利用set集合去重
>
> 四重循环如何找到第二个点是关键，需要去思考一下
>
> 注意斜率不存在的情况

> 答案：
>
> 40257

```java
import java.util.HashSet;
public class Main {
    public static void main(String[] args) {
        HashSet<String> set=new HashSet<>();//用了set的互异性集合中不能有相同的元素
        double k,b;
        for(int x1=0;x1<20;x1++){
            for(int y1=0;y1<21;y1++){
                for(int x2=x1+1;x2<20;x2++){
                    for(int y2=0;y2<21;y2++){
                        k=(y2-y1)*1.0/(x2-x1);
                        b=(y1*x2-x1*y2)*1.0/(x2-x1);//注意要乘以1.0有小数斜率
                        set.add(k+","+b);//用字符串来表示每个点
                    }
                }
            }
        }
        for (String s :
                set) {
            System.out.println(s);
        }
        System.out.println(set.size()+20);//最后记得加上斜率不存在的
    }
}
```

## 题四：货物摆放--大数的因数

> 题目详情：
>
> 小蓝有一个超大的仓库，可以摆放很多货物。
> 现在，小蓝有 **n** 箱货物要摆放在仓库，每箱货物都是规则的正方体。小蓝规定了长、宽、高三个互相垂直的方向，每箱货物的边都必须**严格平行于长、宽、高**。
> 小蓝希望所有的货物最终摆成一个大的立方体。即在长、宽、高的方向上分别堆 L、W、H 的货物，满足 n = L × W × H。
> 给定 n，请问有多少种堆放货物的方案满足要求。
> 例如，当 n = 4 时，有以下 6 种方案：1×1×4、1×2×2、1×4×1、2×1×2、2 × 2 × 1、4 × 1 × 1。
> 请问，当 n = 2021041820210418 （注意有 16 位数字）时，总共有多少种方案？
> 提示：建议使用计算机编程解决问题。

> 思路：
>
> 用java暴力解决的话.....，4个小时不一定跑的完
>
> 附暴力解决主要代码
>
> ```java
> for (x = BigDecimal.ONE; x.compareTo(num) <= 0; x=x.add(BigDecimal.ONE)) {
> for (y = BigDecimal.ONE; y.compareTo(num) <= 0; y=y.add(BigDecimal.ONE)) {
> for (z = BigDecimal.ONE; z.compareTo(num) <= 0; z=z.add(BigDecimal.ONE)) {
>     if ((x.multiply(y).multiply(z)).compareTo(num) == 0) {
>         System.out.println(x+" y="+y);
>         ans++;
> }}}}
> System.out.println(ans);
> ```
>
> 因此需要改进，考虑到可以将输入数字的所有因子先求出来，然后使用这些因子去建立一个三层循环即可很快求出结果(因为这时候即使是三层循环结构，但是每一层循环的循环次数大大减少，速度也是相当可观的)。此时涉及到一个如何**求因子的问题**，我分享一个在刷Leetcode时得到的经验，便是从 1 遍历到该位数的开方数量级数值，对每一位数都使用普通方法判断是否为因子(是否整除)，若整除则当前遍历的数值即是因子，并且判断输入位数除以当前位的值是否等于当前数，若不是则该商也是因子，这样使得求公因子的数量级大大降低。该算法的Java代码实现如下
>
> 答案：
>
> 2430

```java
import java.util.*;
public class Main{
    public static void main(String[] args) {
        long n = 2021041820210418l;
        int count = 0;
        List<Long> list = new ArrayList<>();
        for (long i = 1; i * i <= n; i++) {
            //求所有公因子的方法 很大程度上提高了效率
            if (n % i == 0) {
                list.add(i);
                if (i != n / i) {
                    list.add(n / i);
                }
            }
        }
        for (long i : list) {
            for (long j : list) {
                for (long k : list) {
                    if (i * j * k == n) {
                        count++;
                        break;
                    }
                }
            }
        }
        System.out.println(count);
    }
}
```

## 题五：路径--dijkstra算法

> 小蓝学习了最短路径之后特别高兴，他定义了一个特别的图，希望找到图中的最短路径。
> 小蓝的图由 2021 个结点组成，依次编号 1 至 2021。
> 对于两个不同的结点 a, b，如果 a 和 b 的差的绝对值大于 21，则两个结点之间没有边相连；如果 a 和 b 的差的绝对值小于等于 21，则两个点之间有一条长度为 a 和 b 的最小公倍数的无向边相连。
> 例如：结点 1 和结点 23 之间没有边相连；结点 3 和结点 24 之间有一条无向边，长度为 24；结点 15 和结点 25 之间有一条无向边，长度为 75。
> 请计算，结点 1 和结点 2021 之间的最短路径长度是多少。-->（很明显使用Dijkstra算法）
> 提示：建议使用计算机编程解决问题。

> 思路：
>
> 先求出最大公约数，然后求出最小公倍数（思路在下方），这样就可以得到一个2021*2021的无向图，用邻接矩阵存储这张图，然后dijkstra算法求最短路径
>
> dijkstra算法概括:广度优先遍历，每次求出到一个点的最短路径，代码中有详细注释
>
> 欧几里德算法又称辗转相除法，用于计算两个整数a,b的最大公约数`GCD(greatest common divisor)`。其计算原理依赖于下面的定理：
>
> 定理：gcd(a,b) = gcd(b,a mod b)
>
> ```java
> int Gcd(int a, int b)
> {
>     if(b == 0)
>         return a;
>     return Gcd(b, a % b);
> }
> ```
>
> **再利用最大公约数求最小公倍数**
>
> 两个数的乘积等于这两个数的最大公约数与最小公倍数的乘积。假设有两个数是a、b，它们的最大公约数是p，最小公倍数是q。那么存在这样的关系式：ab=pq
>
> 答案：
>
> 10266837

```java
public class Main {
    //答案：10266837
    final static int C = 999999999;//定义该点无直达
    public static void main(String[] args) {
        //初始化邻接矩阵
        int[][] map = new int[2050][2050];
        //首先初始化为最大值
        for (int[] temp : map) {
            for (int i = 0; i < temp.length; i++) {
                temp[i] = C;
            }
        }
        //按照题意赋值
        for (int i = 1; i <= 2021; i++) {
            for (int j = i; j <= i + 21; j++) {
                //对a,b相差21以内的边赋权
                map[i][j] = lcm(i, j);
            }
        }
        //Dijkstra:按路径长度递增次序产生最短路径
        /*
        V:代表所有顶点
        bj:代表顶点是否已确定最短路径
        */
        boolean bj[] = new boolean[2050];//用来标记该点是否找到最短路径
        int dis[] = new int[2050];//存储源点到其他顶点的初始路径
        for (int i = 1; i <= 2021; i++)
            dis[i] = map[1][i];//先赋值为直达路径
        int min, minIdx = 0;
        //没执行一次while循环，确定到一个点的最短路径
        while (!bj[2021]) {//如果2021点的最短路径还没求到就一直循环
            min = Integer.MAX_VALUE;
            //每次找到从源点出发最近的距离
            for (int i = 2; i <= 2021; i++) {
                if (!bj[i] && dis[i] < min) {
                    //交换
                    min = dis[i];
                    minIdx = i;
                }
            }
            bj[minIdx] = true;//循环一圈以后，可以确定一个最短的点，然后进行下一次循环，直到bj[2021]==true

            //从最近的这个点当中间点，找到 V(0) -- V(minIdx) -- V(和V(minIdx)有直连的点)，再一次更新最短路径
            for (int i = minIdx + 1; i <= minIdx + 21; i++) {
                //如果该点到源点没有边
                if (dis[i] == C)
                    dis[i] = dis[minIdx] + map[minIdx][i];
                //两边之和小于直达边，更新距离
                else if (dis[minIdx] + map[minIdx][i] < dis[i])
                    dis[i] = dis[minIdx] + map[minIdx][i];          
            }
        }
        System.out.println(dis[2021]);
    }
    //求最大公约数
    public static int gcd(int x, int y) {
        if (y == 0)
            return x;
        return gcd(y, x % y);
    }
    public static int lcm(int x, int y) {//最小公倍数
        return x * y / gcd(x, y);
    }
}
```



## 题六：时间显示--Calendar类

> 小蓝要和朋友合作开发一个时间显示的网站。在服务器上，朋友已经获取了当前的时间，用一个整数表示，值为从 1970 年 1 月 1 日 00:00:00 到当前时刻经过的毫秒数。
> 现在，小蓝要在客户端显示出这个时间。小蓝不用显示出年月日，只需要显示出时分秒即可，毫秒也不用显示，直接舍去即可。
> 给定一个用整数表示的时间，请将这个时间对应的时分秒输出。
>
> 【输入格式】
> 输入一行包含一个整数，表示时间。
> 【输出格式】
> 输出时分秒表示的当前时间，格式形如 HH:MM:SS，其中 HH 表示时，值
> 为 0 到 23，MM 表示分，值为 0 到 59，SS 表示秒，值为 0 到 59。时、分、秒
> 不足两位时补前导 0。
> 【样例输入 1】
> 46800999
> 【样例输出 1】
> 13:00:00
> 【样例输入 2】
> 1618708103123
> 【样例输出 2】
> 01:08:23
> 【评测用例规模与约定】
> 对于所有评测用例，给定的时间为不超过 10^18 的正整数

> 思路：
>
> 用java日期类来做就好，对于比较大的数多次加法即可
>
> 但是题目有一个坑点就是不足1ms忽略即可，所以需要对这一点进行处理

> 1、Calender类对于不足1s的时间处理
>
> 2、Calendar类对于日期格式转换的Api调用

```java
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
public class Main {
    public static void main(String[] args) {
        Calendar date = Calendar.getInstance();
        //初始化date对象
        date.set(1970,1,1,0,0,0);
        Scanner scanner = new Scanner(System.in);
        BigDecimal num = scanner.nextBigDecimal();
        BigDecimal temp = new BigDecimal("20000000");
        BigDecimal[] arr = num.divideAndRemainder(temp);
        for (int i = 0; i < arr[0].intValue(); i++) {
            date.add(Calendar.MILLISECOND,20000000);
        }
        date.add(Calendar.MILLISECOND,arr[1].intValue()/1000*1000);

        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        System.out.println(date.getTime());
        String ans = format.format(date.getTime());
        System.out.println(ans);
    }
}
```

## 题七：最少砝码

> 你有一架天平。现在你要设计一套砝码，使得利用这些砝码可以称出任意小于等于N的正整数重量。
> 那么这套砝码最少需要包含多少个砝码？
> 注意砝码可以放在天平两边。
> 【输入格式】
> 输入一个整数 N。
> 【输出格式】
> 输出一个整数代表答案。
> 【样例输入】
> 7
> 【样例输出】
> 3
> 【样例说明】
> 3个砝码重量是1、4、6，可以称出1至7的所有重量。
> 1 = 1；
> 2 = 6 - 4（天平一边放6，另一边放4）；
> 3 = 4 - 1；
> 4 = 4；
> 5 = 6 - 1；
> 6 = 6；
> 7 = 1 + 6；
> 少于3个砝码不可能称出1至7的所有重量。
>
> 【评测用例规模与约定】
> 对于所有评测用例，1 ≤ N ≤ 1000000000。

> 思路：没太搞懂这道题，好像涉及到平衡三进制
>
> 规律：当砝码为x个的时候，最多可以称出任意小于等于`3^0 + 3^1 + 3^2 + ···· + 3^（x-1)`  的正整数重量 

```java
import java.util.*;
public class Main {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		long n = sc.nextLong();
		long ans = 0;
		while(n>0) {
			n -= Math.pow(3, ans);
			ans++;
		}
		System.out.println(ans);
	}
}
```



## 题八：杨辉三角形-搞不懂

> 下面的图形是著名的杨辉三角形：
>
> ![杨辉三角形](https://img-blog.csdnimg.cn/20210418142926546.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0ZhbGxlblNRTA==,size_16,color_FFFFFF,t_70)
>
> 如果我们按从上到下、从左到右的顺序把所有数排成一列，可以得到如下数列：
> 1, 1, 1, 1, 2, 1, 1, 3, 3, 1, 1, 4, 6, 4, 1, …
> 给定一个正整数 N，请你输出数列中第一次出现 N 是在第几个数？
>
> 【输入格式】
> 输入一个整数 N。
> 【输出格式】
> 输出一个整数代表答案。
> 【样例输入】
> 6
> 【样例输出】
> 13
> 【评测用例规模与约定】
> 对于 20% 的评测用例，1 ≤ N ≤ 10；
> 对于所有评测用例，1 ≤ N ≤ 1000000000。

> 思路：
>
> 直接生成杨辉三角形并判断第一次出现次数，发现数据太大导致异常java.lang.OutOfMemoryError: Java heap space，所以这种方法不可取
>
> 附该代码：得分40
>
> ```java
> public static void main(String[] args) {
>         int ans = 1;
>         Scanner sc = new Scanner(System.in);
>         int num = sc.nextInt();
>         long[][] longs = new long[1001200][10000];
>         //生成杨辉三角形并判断首次出现次数
>         for (int i = 0; i < 50000; i++) {
>             for (int j = 0; j < i; j++) {//遍历每个数
>                 if (i == j || j == 0) {
>                     longs[i][j] = 1;
>                 } else {
>                     longs[i][j] = longs[i - 1][j - 1] + longs[i - 1][j];
>                 }
> 
>                 if (longs[i][j] == num) {
>                     System.out.println(ans);
>                     return;
>                 } else ans++;
>             }
>         }
>     }
> ```
>
> 所以需要换一种思路：杨辉三角与组合数的关系
>
> 用DP的思想：
> C（n，m）= 从 n 个 物 品 中 选 择 m 个 物 品 有 多 少 种 选 法
> 对应的动态转移方程正好是杨辉三角形的公式
>
> f[i ] [j ] = f [i−1] [j−1] + f[i−1] [ j ]

正确代码

```java
import java.util.Scanner;
public class Main{
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		while(sc.hasNext()) {
			System.out.println(solve(sc.nextLong()));
		}
	}
	public static long solve(long n) {
		long res = 0l;
		long san[] = new long[50010];
		san[1]=1;
            //san[x]表示当前层的第x个位置，这是为了方便后面在每层第一个位置执行san[j] = san[j-1]+san[j]不出现数组越界的情况
		if(n==1)return 1;
		long cur=0l;
		for(int i=2;i<50000;i++) {
			//cur现在所在的位置是上上层的最后一个位置
			//然后通过+i是到了本层第一个位置，再加i-1就到了这层最后一个位置
			//加一起也就是cur+2*i-2
			cur+=2*i-1;
			for(int j=i;j>=1;j--) {
				san[j] = san[j-1]+san[j];
				if(san[j]==n)res=cur;
				cur--;
			}
			//这里一直减到了本层第一后位置后还要--，所以一层循环完cur的位置就到了上一层的最后一个位置
			//下一次循环的时候i++了，所以下一次循环开始前cur的位置是上上层的最后一个位置
			if(res!=0)return res;
		}
		//在前五万行都找不到n这个数，也就是说n这个数不可能出现在五万行以后的第二个位置
		//因为五万行以后的第二个位置的值都比1000000000大，所以n第一次出现的位置必然是第n+1行的第三个位置，也就是C上标1，下标n的值
		
		return (n+1)*n/2+2; 
		//第一行是1个数，第n行是n个数，而且是等差数列，用等差数列求和公式算出前n行的值加2就是n+1行第二个数的位置了
	}
}
```



## 题九:双向排序--暴力Api

> 给定序列 (a1, a2, · · · , an) = (1, 2, · · · , n)，即 ai = i。
> 小蓝将对这个序列进行 m 次操作，每次可能是将 a1, a2, · · · , aqi 降序排列，或者将 aqi, aqi+1, · · · , an 升序排列。
> 请求出操作完成后的序列。
>
> 【输入格式】
> 输入的第一行包含两个整数 n, m，分别表示序列的长度和操作次数。接下来 m 行描述对序列的操作，其中第 i 行包含两个整数 pi
> , qi 表示操作类型和参数。当 pi = 0 时，表示将 a1, a2, · · · , aqi 降序排列；当 pi = 1 时，表示
> 将 aqi, aqi+1, · · · , an 升序排列。
> 【输出格式】
> 输出一行，包含 n 个整数，相邻的整数之间使用一个空格分隔，表示操作
> 完成后的序列。
> 【样例输入】
> 3 3
> 0 3
> 1 2
> 0 2
> 【样例输出】
> 3 1 2
> 【样例说明】
> 原数列为 (1, 2, 3)。
> 第 1 步后为 (3, 2, 1)。
> 第 2 步后为 (3, 1, 2)。
> 第 3 步后为 (3, 1, 2)。与第 2 步操作后相同，因为前两个数已经是降序了。
> 【评测用例规模与约定】
> 对于 30% 的评测用例，n, m ≤ 1000；
> 对于 60% 的评测用例，n, m ≤ 5000；
> 对于所有评测用例，1 ≤ n, m ≤ 100000，0 ≤ ai ≤ 1，1 ≤ bi ≤ n。

> 思路：
>
> 首先说明，此解法只得60分（比赛中已经不错了）,其它测试用例会超时，但这是最容易理解的一个方法
>
> 需要满分的看这个题解
>
> https://www.acwing.com/video/2845/
>
> Arrays.sort（）,注意点就是Arrays.sort怎么实现倒序

```java
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int length = sc.nextInt();//数组长度
        int op = sc.nextInt();//操作次数
        //初始化数组
        Integer[] arr = new Integer[length];
        for (int i = 0; i < length; i++) arr[i] = i + 1;
        
        for (int i = 0; i < op; i++) {
            //接受两个数字并处理
            int num = sc.nextInt();
            int num2 = sc.nextInt();
            if (num == 0) {// ai-an降序排列
                //降序排列
                //从fromIndex到toIndex-1的元素排序!!!
                Arrays.sort(arr, 0, num2, new Comparator<Integer>() {
                    public int compare(Integer a, Integer b) {
                        return b - a;
                    }
                });
                continue;
            }
            if (num == 1) {
                Arrays.sort(arr, num2-1, length);
                continue;
            }
        }
        for (int i : arr) System.out.print(i + " ");
    }
}
```



## 题十：括号序列

> 给定一个括号序列，要求尽可能少地添加若干括号使得括号序列变得合法，当添加完成后，会产生不同的添加结果，请问有多少种本质不同的添加结果。两个结果是本质不同的是指存在某个位置一个结果是左括号，而另一个是右括
> 号。
> 例如，对于括号序列 ((()，只需要添加两个括号就能让其合法，有以下几种不同的添加结果：()()()、()(())、(())()、(()()) 和 ((()))。
>
> 【输入格式】
> 输入一行包含一个字符串 s，表示给定的括号序列，序列中只有左括号和
> 右括号。
> 【输出格式】
> 输出一个整数表示答案，答案可能很大，请输出答案除以 1000000007 (即
> 10^9 + 7) 的余数。
> 【样例输入】
> ((()
> 【样例输出】
> 5
> 【评测用例规模与约定】
> 对于 40% 的评测用例，|s| ≤ 200。
> 对于所有评测用例，1 ≤ |s| ≤ 5000

[CSDN  AC题解](https://blog.csdn.net/lz970704/article/details/117332593?ops_request_misc=%257B%2522request%255Fid%2522%253A%2522164665143416780265474560%2522%252C%2522scm%2522%253A%252220140713.130102334.pc%255Fall.%2522%257D&request_id=164665143416780265474560&biz_id=0&utm_medium=distribute.pc_search_result.none-task-blog-2~all~first_rank_ecpm_v1~rank_v31_ecpm-5-117332593.pc_search_result_cache&utm_term=%E8%93%9D%E6%A1%A5%E6%9D%AF%E6%8B%AC%E5%8F%B7%E5%BA%8F%E5%88%97&spm=1018.2226.3001.4187)





















