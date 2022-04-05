		5+15+20+10

## 题一：门牌制作-int转int[]

> 本题总分：5 分
>
> 【问题描述】
> 小蓝要为一条街的住户制作门牌号。
> 这条街一共有 2020 位住户，门牌号从 1 到 2020 编号。
> 小蓝制作门牌的方法是先制作 0 到 9 这几个数字字符，最后根据需要将字
> 符粘贴到门牌上，例如门牌 1017 需要依次粘贴字符 1、0、1、7，即需要 1 个字符 0，2 个字符 1，1 个字符 7。
> 请问要制作所有的 1 到 2020 号门牌，总共需要多少个字符2？

> 思路：
>
> 很简单，将数字转为int数组，然后以0-9为下标，index[遍历数组值]++，最后输出index[2]即可
>
> 答案：
>
> 624

```java
import java.util.Arrays;

public class A {
	public static void main(String[] args) {
		int[] arr = new int[10];
		long ans = 0l;
		for (int i = 1; i < 2021; i++) {
			for (int j = 0; j < String.valueOf(i).length(); j++) {
           arr[Integer.parseInt(String.valueOf(String.valueOf(i).charAt(j)))]++;//转换为int[]	
			}
		}
		System.out.println(Arrays.toString(arr));
	}
}
```

## 题二：寻找 2020

> 本题总分：5 分
>
> 【问题描述】
> 小蓝有一个数字矩阵，里面只包含数字 0 和 2。小蓝很喜欢 2020，他想找到这个数字矩阵中有多少个 2020 。
> 小蓝只关注三种构成 2020 的方式：
> •同一行里面连续四个字符从左到右构成 2020。
> •同一列里面连续四个字符从上到下构成 2020。
> •在一条从左上到右下的斜线上连续四个字符，从左上到右下构成 2020。例如，对于下面的矩阵：
> 220000
> 000000
> 002202
> 000000
> 000022
> 002020
>
> 一共有 5 个 2020。其中 1 个是在同一行里的，1 个是在同一列里的，3 个是斜线上的。
> 小蓝的矩阵比上面的矩阵要大，由于太大了，他只好将这个矩阵放在了一 个文件里面，在试题目录下有一个文件 2020.txt，里面给出了小蓝的矩阵。
> 请帮助小蓝确定在他的矩阵中有多少个 2020
> [2020.txt文档下载](https://download.csdn.net/download/God_stf/15615501)

> 思路：两个难点
>
> 1、怎么读入那么大的矩阵，由于是填空题所以不需要关注性能问题，使用Scanner即可
>
> 如果是编程题需要读取大量数据,使用下面的输入方式可以提高很多性能
>
> ```java
> StringBuffer reader = new StringBuffer(new InputStream(System.in));
> ```
>
> 2、循环判断的条件，可以画图来看
>
> 答案：
>
> 16520

第一次解的时候出错是因为对循环结构的判断不准确，WA了~

```java
public class Main{
	public static void main(String[] args) {
		int[][] arr = new int[301][301];
		long ans = 0;
		// 初始化
		Scanner sc = new Scanner(System.in);
		for (int i = 0; i < 300; i++) {
			String s = sc.next();
			for (int j = 0; j < s.length(); j++) {
				arr[i][j] = Integer.parseInt(String.valueOf(s.charAt(j)));
				// arr[i][j] = s.charAt(j)-'0';
			}
		}
		for (int i = 0; i <= 300; i++) {
			for (int j = 0; j <= 300; j++) {
                //这个判断条件是难点
				if (i + 3 < 300 && arr[i][j] == 2 && arr[i + 1][j] == 0 && arr[i + 2][j] == 2 && arr[i + 3][j] == 0)
					ans++;// 橫向
				if (j + 3 < 300 && arr[i][j] == 2 && arr[i][j + 1] == 0 && arr[i][j + 2] == 2 && arr[i][j + 3] == 0)
					ans++;// 纵向
				if (i + 3 < 300 && j + 3 < 300 && arr[i][j] == 2 && arr[i + 1][j + 1] == 0 && arr[i + 2][j + 2] == 2
						&& arr[i + 3][j + 3] == 0)
					ans++;
			}
		}
		System.out.println(ans);
	}
}
```



## 题三：蛇形填数--找规律

> 本题总分：10 分
>
> 【问题描述】
> 如下图所示，小明用从 1 开始的正整数“蛇形”填充无限大的矩阵。
>
> ![矩阵](https://img-blog.csdnimg.cn/20210306175208395.png#pic_center)
>
> 容易看出矩阵第二行第二列中的数是 5。请你计算矩阵中第 20 行第 20 列的数是多少？
>
> 答案：
>
> 761

> 思路：我一开始还想用excel暴力写
>
> ![image-20220309164828780](https://s2.loli.net/2022/03/17/DcYx4PT7Nv8r6lC.png)
>
> ![image-20220309165144696](https://s2.loli.net/2022/03/17/SwdbD4E9RGk6sHa.png)

```java
public class Main {
    public static void main(String[] args) {
        int a = 4,sum = 1;
        for (int i = 0; i < 20; i++) {
            sum += a;
            a += 4;
            System.out.print(sum+"  ");
        }
    }
}
```



## 题四：七段码

> 本题总分：10 分
>
> 【问题描述】
> 小蓝要用七段码数码管来表示一种特殊的文字。
>
> ![image-20220308173447434](https://s2.loli.net/2022/03/17/HTREB2aCJlMnAcK.png)
>
> 上图给出了七段码数码管的一个图示，数码管中一共有 7 段可以发光的二极管，分别标记为 a, b, c, d, e, f, g。
> 小蓝要选择一部分二极管（至少要有一个）发光来表达字符。在设计字符 的表达时，要求所有发光的二极管是连成一片的。
> 例如：b 发光，其他二极管不发光可以用来表达一种字符。
> 例如：c 发光，其他二极管不发光可以用来表达一种字符。这种方案与上一行的方案可以用来表示不同的字符，尽管看上去比较相似。
> 例如：a, b, c, d, e 发光，f, g 不发光可以用来表达一种字符。
> 例如：b, f 发光，其他二极管不发光则不能用来表达一种字符，因为发光的二极管没有连成一片。
> 请问，小蓝可以用七段码数码管表达多少种不同的字符？

> 我只会直接数，有时候不失为一个好办法（流下了没有技术的眼泪）
> 这个可以数发光的个数：
> 1 ： 7种
> 2： 10种
> 3： 16种
> 4： 20种
> 5： 19种
> 6： 7种
> 7： 1种
> ans = 7 + 10 + 16 + 20 + 19 + 7 + 1 = 80思路：
>
> 每个数码管都有两种选择，亮或者不亮。其实就是图{a, b, c, d, e, f, g}的所有子集，然后从所有子集中选出子集元素相通的，就是合法的。

> 先枚举所有子集（递归），然后通过dfs看该子集元素是否相通。

## 题五：排序（最难顶的一道题）

> 【问题描述】
> 小蓝最近学习了一些排序算法，其中冒泡排序让他印象深刻。 在冒泡排序中，每次只能交换相邻的两个元素。
> 小蓝发现，如果对一个字符串中的字符排序，只允许交换相邻的两个字符， 则在所有可能的排序方案中，冒泡排序的总交换次数是最少的。
> 例如，对于字符串 lan 排序，只需要 1 次交换。对于字符串 qiao 排序， 总共需要 4 次交换。
> 小蓝找到了很多字符串试图排序，他恰巧碰到一个字符串，需要 100 次交换，可是他忘了吧这个字符串记下来，现在找不到了。
> 请帮助小蓝找一个只包含小写英文字母且没有字母重复出现的字符串，对该串的字符排序，正好需要 100 次交换。如果可能找到多个，请告诉小蓝最短的那个。如果最短的仍然有多个，请告诉小蓝字典序最小的那个。请注意字符 串中可以包含相同的字符。

> 思路：
>
> 对线性代数行列式的内容足够熟悉的话就能知道逆序数是具有实际应用的。在线性代数中用逆序数的奇偶性来确定余子式的符号 r(num)
>
> 逆序数等于一个序列要变成**升序排列**所需要的**相邻元素**交换的**最小**次数（仅适用于冒泡排序）

```
(1)考虑冒泡排序的复杂度，对于拥有N个字母的字符串，最多需要交换
N*(N-1)/2次（完全乱序时）
易知N=14时，有(1413)/2=91，N=15时，有(1514)/2=105，
即满足100次交换所需的最短字符串有15个字母。
(2)要求字典序最小，那么显然要取a~o这15个字典序最小的字母
(3)逆向思考，目标字符串经过100次交换后，得到正序字符串abcdefghijklmno，而完全逆序的字符串onmlkjihgfedcba变成正序字符串需要105次交换，那么将完全逆序的字符串交换5次后，便能得到答案。
(4)而要求字典序最小，那么将j交换5次提到字符串最前面，就得到了最小的情况
```

## 题六：成绩分析

> 时间限制: 1.0s 内存限制: 512.0MB
> 本题总分：15 分
>
> 【问题描述】
> 小蓝给学生们组织了一场考试，卷面总分为 100 分，每个学生的得分都是
> 一个 0 到 100 的整数。
> 请计算这次考试的最高分、最低分和平均分。
>
> 【输入格式】
> 输入的第一行包含一个整数 n，表示考试人数。
> 接下来 n 行，每行包含一个 0 至 100 的整数，表示一个学生的得分。
>
> 【输出格式】
> 输出三行。
> 第一行包含一个整数，表示最高分。第二行包含一个整数，表示最低分。
> 第三行包含一个实数，四舍五入保留正好两位小数，表示平均分。
>
> 【样例输入】
> 7
> 80
> 92
> 56
> 74
> 88
> 99
> 10
>
> 【样例输出】
> 99
> 10
> 71.29
>
> 【评测用例规模与约定】
> 对于 50% 的评测用例，1 ≤ n ≤ 100。对于所有评测用例，1 ≤ n ≤ 10000。

> 思路：
>
> 注意sc.nextInt()是获取下一个整数，再无其他坑点

简单，注意.2f输出即可，就不上代码了

## 题七：单词分析--HashMap

> 时间限制: 1.0s 内存限制: 512.0MB
> 本题总分：20 分
>
> 【问题描述】
> 小蓝正在学习一门神奇的语言，这门语言中的单词都是由小写英文字母组成，有些单词很长，远远超过正常英文单词的长度。小蓝学了很长时间也记不 住一些单词，他准备不再完全记忆这些单词，而是根据单词中哪个字母出现得 最多来分辨单词。
> 现在，请你帮助小蓝，给了一个单词后，帮助他找到出现最多的字母和这个字母出现的次数。
>
> 【输入格式】
> 输入一行包含一个单词，单词只由小写英文字母组成。
>
> 【输出格式】
> 输出两行，第一行包含一个英文字母，表示单词中出现得最多的字母是哪 个。如果有多个字母出现的次数相等，输出字典序最小的那个。
> 第二行包含一个整数，表示出现得最多的那个字母在单词中出现的次数。
>
> 【样例输入】
> lanqiao
>
> 【样例输出】
> a 2
>
> 【样例输入】
> longlonglongistoolong
>
> 【样例输出】
> o 6
>
> 【评测用例规模与约定】
> 对于所有的评测用例，输入的单词长度不超过 1000。

> 利用HashMap集合来做

```java
public class Demo_G单词分析 {

    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        //用字符串接收输入的字母
        String str=sc.next();
        //定义HashMap，key类型为Character，value类型为Integer
        Map<Character,Integer> map=new HashMap<Character, Integer>();
        //对字符串进行for循环
        for (int i = 0; i < str.length(); i++) {
        //获取字符串的每个字符c
            char c=str.charAt(i);
            /*判断map的key中是否包含c，如果不包含表示该字符第一次出现
            ，value赋值1并将c和1 put进map中*/
            if(!map.containsKey(c)){
                map.put(c,1);
            }else{
            /*如果map中已经存在c的key，先获取该key的value值，
            在对该value值进行自加操作，最后再put到map中*/
                int a = map.get(c);
                a++;
                map.put(c,a);
            }
        }
/*由于hashmap集合的键是Character类型时会对键值自动进行字典序排序所以
我们可以直接遍历map集合*/

        int max=0;//最大值
        char max_c=' ';//次数最多的字符
        //HashMap的遍历使用foreach循环
        for (Character key:map.keySet()) {
        //当遍历的value值大于max，就把该value值赋值给max，max_c就等于当前的key
            if(map.get(key)>max){
                max=map.get(key);
                max_c=key;
            }
        }
        System.out.print(max_c);
        System.out.println(max);
    }
}
```

> 另外一种思路：利用数组下标记录

```java
public class Main {
	
	public static void main(String[] args) {
		Scanner scanner =new Scanner(System.in);
		char[] ch=scanner.nextLine().toCharArray();
		int[] num=new int[30];
		for (int i = 0; i < ch.length; i++) {
			num[ch[i]-'a']++;
		}
		int max=-1;
		int maxi=-1;
		for (int i = 0; i < num.length; i++) {
			if (max<num[i]) {
				max=num[i];
				maxi=i;
			}
		}
		char result =(char) ('a'+maxi);
		System.out.println(result);
		System.out.println(max);
	}
}
```



## 题八：数字三角形--动态规划

> 时间限制: 1.0s 内存限制: 512.0MB
> 本题总分：20 分
>
> 【问题描述】![img](https://img-blog.csdnimg.cn/20210406190911359.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NTI4OTY3OA==,size_16,color_FFFFFF,t_70)
>
> 
>
> 上图给出了一个数字三角形。从三角形的顶部到底部有很多条不同的路径。 对于每条路径，把路径上面的数加起来可以得到一个和，你的任务就是找到最大的和。
> 路径上的每一步只能从一个数走到下一层和它最近的左边的那个数或者右边的那个数。此外，向左下走的次数与向右下走的次数相差不能超过 1。
> 【输入格式】
> 输入的第一行包含一个整数 N (1 < N ≤ 100)，表示三角形的行数。下面的
> N 行给出数字三角形。数字三角形上的数都是 0 至 100 之间的整数。
>
> 【输出格式】
> 输出一个整数，表示答案。
>
> 【样例输入】
>
> 5
> 7
> 3 8
> 8 1 0
> 2 7 4 4
>
> 4 5 2 6 5
>
> 【样例输出】
>
> 27

> 思路：
>
> 1、如果三角形只有一层，最大路径和显然是它自己，如果再增加一层，最大路径和是第二层的最大值加第一层，如果再增加一层呢？本题依然离不开动态规划的基本套路
>
> 
>
> 状态转移方程：注意判断条件
>
> base case:当只有一层时，最大路径就是其本身，也就是用最下面那一层初始化dp数组
>
> ```
> dp[i][j] = Math.max(dp[i + 1][j], dp[i + 1][j + 1]) + nums[i][j];
> ```
>
> 2、如果没有**向左下走的次数与向右下走的次数相差不能超过1**这个条件，这是一道很简单的动态规划
>
> ​	要发现这个规律：
>
> ​	如果输入N为奇数，那么最后一定会走到最后一行中间的位置；如果输入N为偶数，那么最后一定会	走到中间两个的其中之一



代码：

```java
public class Main{
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        int n = s.nextInt();
        int[][] nums = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j <= i; j++) {
                nums[i][j] = s.nextInt();
            }
        }
        dp(nums,n);
    }
    public static void dp(int[][] nums,int n){
        int[][] dp = new int[n][n];
        //先初始化，对dp最后一行中间的数赋值
        if (n % 2 == 0) {//n是偶数
            dp[n - 1][n / 2] = nums[n - 1][n / 2];
            dp[n - 1][n / 2 - 1] = nums[n - 1][n / 2 - 1];
        } else {//n是奇数
            dp[n - 1][n / 2] = nums[n - 1][n / 2];
        }
        for (int[] a:dp){
            System.out.println(Arrays.toString(a));
        }
        for (int i = n - 2; i >= 0; i--) {//从下至上遍历
            for (int j = 0; j <= i; j++) {//遍历每一行往下的两个元素的dp值，与自己相加
                if (dp[i + 1][j] != 0 || dp[i + 1][j + 1] != 0) {//判断条件
                    dp[i][j] = Math.max(dp[i + 1][j], dp[i + 1][j + 1]) + nums[i][j];
                    System.out.println(dp[i][j]+"="+nums[i][j]+"+"+Math.max(dp[i + 1][j], dp[i + 1][j + 1]));
                }
            }
        }
        for (int[] a:dp){//输出某一行某个结点到底部的距离最大值
            System.out.println(Arrays.toString(a));
        }
    }
```

## 题九：子串分值和--代码的优化

思考：即使是同一个复杂度的代码也可以优化

> 时间限制: 1.0s 内存限制: 512.0MB
> 本题总分：25 分
>
> 【问题描述】
> 对于一个字符串 S ，我们定义 S 的分值 f (S ) 为 S 中出现的不同的字符个数。例如 f (”aba”) = 2， f (”abc”) = 3, f (”aaa”) = 1。
> 现在给定一个字符串 S [0…n − 1]（长度为 n），请你计算对于所有 S 的非空子串 S [i… j](0 ≤ i ≤ j < n)， f (S [i… j]) 的和是多少。
> 【输入格式】
> 输入一行包含一个由小写字母组成的字符串 S 。
>
> 【输出格式】
> 输出一个整数表示答案。
>
> 【样例输入】
> ababc
>
> 【样例输出】
> 28
>
> 【样例说明】
>
> ![图示](https://img-blog.csdnimg.cn/20210306194054787.png#pic_center)
>
> 【评测用例规模与约定】
> 对于 20% 的评测用例，1 ≤ n ≤ 10；
> 对于 40% 的评测用例，1 ≤ n ≤ 100；
> 对于 50% 的评测用例，1 ≤ n ≤ 1000；
> 对于 60% 的评测用例，1 ≤ n ≤ 10000；
> 对于所有评测用例，1 ≤ n ≤ 100000。

> 思路:
>
> 用O(n^2)，只能得60分，不过第八题也不错了
>
> ```java
> import java.util.HashSet;
> import java.util.Scanner;
> 
> public class Main {
>     public static void main (String[]args){
>         Scanner sc = new Scanner(System.in);
>         String[] str = sc.next().split("");
>         long ans = 0;
>         //遍历--所有字串
>         for (int i = 0; i < str.length; i++) {
>             HashSet<String> set = new HashSet<>();
>             for (int j = i; j < str.length; j++) {
>                 set.add(str[j]);
>                 ans += set.size();
>             }
>         }
>         System.out.println(ans);
>     }
> }
> ```
>
> 正确思路：
>
> 对于字符串的每一个字符，它对答案的贡献就等于它前一次出现的位置到现在这个位置的距离差乘上现在这个位置一直到字符串末尾的距离的乘积。因为前一个出现的相同的字符的贡献度已经加入到答案中，不能再进行重复计算。

```java
//时间复杂度O(n)
public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        char[] arr1 = scan.nextLine().toCharArray();
        int[] arr2 = new int[26];
        long count = 0;
        int index;
        long len = arr1.length;
        //填充arr2数值为-1
        Arrays.fill(arr2, -1);
        for (int i = 0; i < arr1.length; i++) {
            index = arr1[i] - 'a';
            count += (len - i) * (i - arr2[index]);
            arr2[index] = i;
        }
        System.out.println(count);
    }
}
```



## 题十：装饰珠

> 时间限制: 1.0s 内存限制: 512.0MB 本题总分：25 分
>
> 【问题描述】
> 在怪物猎人这一款游戏中，玩家可以通过给装备镶嵌不同的装饰珠来获取 相应的技能，以提升自己的战斗能力。
> 已知猎人身上一共有 6 件装备，每件装备可能有若干个装饰孔，每个装饰孔有各自的等级，可以镶嵌一颗小于等于自身等级的装饰珠 (也可以选择不镶嵌)。
> 装饰珠有 M 种，编号 1 至 M，分别对应 M 种技能，第 i 种装饰珠的等级为 Li，只能镶嵌在等级大于等于 Li 的装饰孔中。
> 对第 i 种技能来说，当装备相应技能的装饰珠数量达到 Ki 个时，会产生Wi(Ki) 的价值。镶嵌同类技能的数量越多，产生的价值越大，即 Wi(Ki − 1) < Wi(Ki)。但每个技能都有上限 Pi(1 ≤ Pi ≤ 7)，当装备的珠子数量超过 Pi 时，只会产生 Wi(Pi) 的价值。
> 对于给定的装备和装饰珠数据，求解如何镶嵌装饰珠，使得 6 件装备能得到的总价值达到最大。
>
> 【输入格式】
> 输入的第 1 至 6 行，包含 6 件装备的描述。其中第 i 的第一个整数 Ni 表示第 i 件装备的装饰孔数量。后面紧接着 Ni 个整数，分别表示该装备上每个装饰孔的等级 L(1 ≤ L ≤ 4)。
> 第 7 行包含一个正整数 M，表示装饰珠 (技能) 种类数量。
> 第 8 至 M + 7 行，每行描述一种装饰珠 (技能) 的情况。每行的前两个整数Lj(1 ≤ Lj ≤ 4) 和 Pj(1 ≤ Pi ≤ 7) 分别表示第 j 种装饰珠的等级和上限。接下来Pj 个整数，其中第 k 个数表示装备该中装饰珠数量为 k 时的价值 Wj(k)。
>
> 【输出格式】
> 输出一行包含一个整数，表示能够得到的最大价值。
>
> 【样例输入】
>
> 1 1
> 2 1 2
> 1 1
> 2 2 2
> 1 1
> 1 3
> 3
> 1 5 1 2 3 5 8
> 2 4 2 4 8 15
> 3 2 5 10
>
> 【样例输出】
> 20
>
> 【样例说明】
> 按照如下方式镶嵌珠子得到最大价值 18，括号内表示镶嵌的装饰珠的种类编号：
>
> 1: (1)
> 2: (1) (2)
> 3: (1)
> 4: (2) (2)
> 5: (1)
> 6: (2)
>
> 4 颗技能 1 装饰珠，4 颗技能 2 装饰珠 W1(4) + W2(4) = 5 + 15 = 20。
>
> 【评测用例规模与约定】
> 对于 30% 的评测用例，1 ≤ Ni ≤ 10, 1 ≤ M ≤ 20, 1 ≤ Wj(k) ≤ 500； 对于所有评测用例，1 ≤ Ni ≤ 50, 1 ≤ M ≤ 10000, 1 ≤ Wj(k) ≤ 10000。













