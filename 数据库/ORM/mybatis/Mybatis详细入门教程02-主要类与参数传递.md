# Mybatis详细入门笔记02---Mybatis主要类的介绍&Mybatis参数传递

## 主要类

**1）Resources**

+ 负责读取mybatis主配置文件

```java
//读取mybatis主配置文件
InputStream in = Resources.getResourceAsStream("mybatis.xml");
```

**2）SqlSessionFactoryBuilder**

+ 作用：创建SqlSessionFactory对象

```java
//创建SqlSessionFactoryBuilder对象
SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
```

**3）SqlSessionFactory**

```java
//创建SqlSessionFactory对象
SqlSessionFactory factory = builder.build(in);
```

+ 重量级对象（程序创建这个对象耗时比较长，使用资源比较多，一般一个项目中只有一个）

+ SqlSessionFactory是一个接口

  1. 接口实现类:DefaultSqlSessionFactory
  2. 作用: 调用**openSession()**获取SqlSession对象

+ openSession()方法说明

  1、openSession() : 无参数的，获取是非自动提交事务的SqlSession对象

  2、openSession(boolean) : 获取的是自动提交事务的sqlSession

**4）SqlSession**

```java
SqlSession sqlSession = factory.openSession();
```

+ 接口，定义了操作数据库的各种方法，例如 selectOne(),selectList(),insert(),update(),rollback

+ 接口的默认实现类 -- DefaultSqlSession

+ 该对象不是线程安全的，需要在方法内部中使用

  为了保证线程安全---> 执行sql语句之前，使用openSession()获取sqlSession，执行完sql语句后，将sqlSession关闭（SqlSession.close()）



## 参数传递

**从java代码中把参数传递到mapper.xml文件的操作叫做参数传递**

​		先了解一个概念：parameterType----- dao接口中方法的参数类型

1. 写在mapper文件中的一个属性
2. 可以不写，mybatis通过反射机制也能发现接口形参的类型
3. 它的值是java数据类型的全限定名称或者是mybatis定义的别名（官网可以搜到），例如：`parameterType="java.lang.Integer"`

### 1、传入一个简单参数

​		**简单类型参数 : java中的基本数据类型和String类型**

​		接口

```java
 public Goods selectGoodsById(Integer id);
```

​		mapper文件

```xml
<mapper namespace="org.example.dao.GoodsDao">
    <select id="selectGoodsById" parameterType="java.lang.Integer" resultType="org.example.pojo.Goods">
        select * from Goods where gid=#{id}
    </select>
</mapper>
```

​		**mapper文件中获取一个简单参数  #{}  ----底层实现是JDBC**

1. 使用#{}之后，mybatis执行sql使用的是jdbc中的PreparedStatement对象

   由mybatis执行以下的代码:

   ```java
   //mybatis创建connection，PreparedStatement对象
   String sql = "select * from Goods where gid=#{id}";
   PreparedStatement pstm = new PreparedStatement(sql);
   pstm.setInt(1,1);
   //执行sql封装为resultType="org.example.pojo.Goods"这个对象
   ResultSet rs = pstm.executeQuery();
   Goods goods = null;
   while(rs.next()){
       goods = new Goods();
       goods.setGid(rs.getInt("gid"));
       goods.setGprice(rs.getInt("gprice"));
       goods.setGname(rs.getInt("gname"));
   }
   return goods;
   
   ```

   相当于之前JDBC的代码被Mybatis封装

### 2、传入多个参数

​	**第一种方式、参数之前加@param注解**

​	接口

```java
List<Goods> selectGoods(String name)
```

​	mapper文件

```xml
<mapper namespace="org.example.dao.GoodsDao">
    <select id="selectGoods" resultType="org.example.pojo.Goods">
	select * from Goods where gname=#{myname} or gprice=#{myprice}
    </select>
</mapper>
```

​	**第二种方式、使用java对象传参**

​	接口

```java
List<Goods> selectMultiGoods(Goods goods);
```

​	mapper文件

```xml
<mapper namespace="org.example.dao.GoodsDao">
<select id="selectMultiGoods" resultType="org.example.pojo.Goods">
        select * from Goods where gname=#{gname} or gprice=#{gprice}
</select>
</mapper>
```

以下方式仅作了解

```
	法三：使用Map的key可以传参 - #{key}
    法四：按位置传参 - #{arg(1)}
```

## 两种方式的比较--#{}和&{}

+ 从编译后的sql语句分析

**#的结果**

​		用作占位符

```sql
select * from Goods where gname=?
```

**$的结果**

​		用做字符串拼接

```sql
select * from Goods where gname="足球"
```

**sql注入**

```sql
select * from Goods where gname="足球";drop table Goods;
```

![image-20210530213911109](https://i.loli.net/2021/05/30/7w6pxtMBT5Eslrm.png)









