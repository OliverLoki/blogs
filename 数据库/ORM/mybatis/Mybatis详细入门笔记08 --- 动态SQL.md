# Mybatis详细入门笔记08 --- 动态SQL

什么是动态SQL  :  sql的内容是变化的，可以根据条件获取到不同的sql语句，主要是where部分发生变化

## 动态SQL - if

对于该标签的执行，当test的值为true时，会将其包含的SQL片断拼接到其所在的SQL语句中。语法:<if test=”条件">sql 语句的部分</if>

> 注意该if语句的等于判断应该是   a==b 而不像sql中是  a=b

**接口方法**

```java
	//if操作
    //动态sql参数要使用java对象
    List<Goods> selectGoodsIf(Goods goods);
```

**mapper文件**

```xml
<!--
        <if test=”使用参数java对象的属性值作为判断条件">sql 语句</if>
    -->
    <select id="selectGoodsIf" resultType="org.example.pojo.Goods">
        select * from Goods
        where
        <if test="gprice > 50">
            Gname = #{gname}
        </if>
    </select>
```

**测试方法**

```java
 @Test
    public void test() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        GoodsDao mapper = sqlSession.getMapper(GoodsDao.class);
        Goods goods = new Goods();
        goods.setGid(1);
        goods.setGprice(120);
        goods.setGname("足球");
        List<Goods> goods1 = mapper.selectGoodsIf(goods);
        System.out.println(goods1);

    }
```

sql语句为：![image-20210601194058230](https://i.loli.net/2021/06/01/VEAkTXSclBoif7p.png)

**如果是多个if语句可能会报语法错误**

![image-20210601194310565](https://i.loli.net/2021/06/01/cy9tegaPjMQoLdX.png)

> 解决方法： 在where后面加一个恒成立的条件，不影响sql的执行 比如`1=1`,效果如下

![image-20210601194424884](https://i.loli.net/2021/06/01/nEpP3Hef6y5tZc4.png)

## 动态SQL - where

+ <if/>标签的中存在一个比较麻烦的地方:需要在where后手工添加1=1的子句。因为，若where后的所有<if/>条件均为 false，而 where后若又没有1=1子句，则 SQL 中就会只剩下一个空的where，SQL出错。所以，在 where后，需要添加永为真子句1=1，以防止这种情况的发生。但当数据量很大时，会严重影响查询效率
+ 使用<where/>标签，在有查询条件时，可以自动添加上 where子句;没有查询条件时，不会添加where子句。需要注意的是，第一个<if/>标签中的SQL片断，可以不包含and。不过，写上 and 也不错，系统会将多出的and去掉。但其它<if/>中 SQL片断的and，必须要求写上。否则SQL语句将拼接出错

mapper文件

```xml
<select id="selectGoodsWhere" resultType="org.example.pojo.Goods">
        select * from Goods
        <where>
            <if test="gname =='不可能相等'">
                Gid=#{gid}
            </if>
            <if test="gname !='不可能相等'">
                and Gprice > #{gprice}
            </if>
        </where>

    </select>
```

结果如下

![image-20210601201042285](https://i.loli.net/2021/06/01/lIZViMfOLFTeuhW.png)

## 动态SQL - foreach

+ <foreach/>标签用于实现对于数组与集合的遍历。
+ 对其使用，需要注意
  1. collection表示要遍历的集合类型, list , array等。
  2. open、close、separator为对遍历内容的SQL拼接。
+ 主要用在sql的 `in`语句之中

**接口**

```java
//应用场景：传入一个Goods集合，查询数据库中价格存在于这些集合中的商品其他信息
List<Goods> selectGoodsForeach(Goods goods);
```

**mapper文件**

```xml
<select id="selectGoodsForeach" resultType="org.example.pojo.Goods">
        select * from Goods where gprice in
        <foreach collection="list" item="goodsprice" open="(" close=")" separator=",">
            #{goodsprice}
        </foreach>
</select>
```

**对于foreach中各参数的解读：**

1. collection:表示接口中方法参数的类型，如果数组使用array，如果是数组使用Array,如果是集合使用List
2. item: 自定义循环变量
3. open：循环开始的字符
4. close：循环结束的字符
5. separator：集合成员间的分隔符

**测试方法**

```java
@Test
    public void testForeach() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        GoodsDao mapper = sqlSession.getMapper(GoodsDao.class);
        List list = new ArrayList();
        list.add(121);
        list.add(168);
        List<Goods> goods1 = mapper.selectGoodsForeach(list);
        System.out.println(goods1);
    }
```

**测试结果**

![image-20210601202557339](https://i.loli.net/2021/06/01/YgIDA3Q7fFsWHRl.png)

**如果集合中不是简单值，是一个对象的话，也可以使用foreach**

```xml
item的值就就代表一个集合中的对象
#{对象.属性名}
```

## 动态SQL - 使用重复sql片段

<sql/>标签用于定义SQL片断，以便其它SQL标签复用。而其它标签使用该SQL片断，需要使用<include/>子标签。该<sql/>标签可以定义SQL语句中的任何部分,所以<include/>子标签可以放在动态SQL的任何位置。

**mapper文件**

```xml
<!--代码片段的复用-->
<!--先定义sql语句-->
    <sql id="selectAll">
        select  * from Goods
    </sql>
<!--然后使用include标签使用-->
    <select id="selectrepeat" resultType="org.example.pojo.Goods">
        <include refid="selectAll"/>
    </select>
```

## 总结

![image-20210602163010269](https://i.loli.net/2021/06/02/EXqud4IcPOHylse.png)

