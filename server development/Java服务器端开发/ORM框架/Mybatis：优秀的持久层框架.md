**引言——为什么需要Mybatis**

> **:rocket:Q:JAVA中使用Jdbc就能操作数据库了，为什么还要用Mybatis、Spring等框架?**
>
> **:rocket:A:**当然可以使用Jdbc来做这些事，不过随着业务的扩展，你就会发现jdbc建立一个连接居然要几百毫秒，而执行一个普通的SQL仅仅需要几毫秒。这么重量级的资源建立了就释放了不合适，得找个容器存起来，谁要就来取，不用了就还给容器，毕竟容器里的借取比建立一个连接要快的多。这样的容器叫做数据连接池。
>
> 小日子继续过，业务也越做越大，慢慢地你就发现了：这jdbc的接口也太粗暴了，有一大半的代码在往bean里塞数据，下标还是从1开始的。这时候你就会想，要不独立一层，专门处理把jdbc读取出来的数据塞进bean里吧。这一层就是DAO，data access object，比较出名的框架就是myBatis。
>
> 公司越做越大，你也在不断尝试新鲜的技术，并且在其中的一个项目上实践了敏捷，积极拥抱变化。不久后你就发现了，一次迭代中多出的一个字段，你的SQL模板就去同步，然后你望着项目里指数级增长的SQL模板，心里一阵阵发慌：要是有个框架，只需要管理bean之间的关系，就可以生成常用的CRUD语句。这就是ORM框架，比较出名的就是Hibernate，其中的大部分接口被JSR吸纳，成为JPA标准。
>
> 渐渐地，使用公司产品的客户越来越多，但是一部分客户希望系统通知通过短信来推送，另一部分希望通过邮件。除此之外，大家对剩下的功能没有任何分歧。虽然在编写代码时，你已经在这里对通知的推送做了接口隔离，但是因为这个接口的引用指向的一个new关键字创建对象，所以程序的行为在编译时就已经确定了。为了响应另一部分客户的需求，你不得不在打包前，去修改代码。这时候你就在想：如果程序的行为可以通过一些配置在运行时才确定，或许可以改善现在的处境。这种把对象的实例化交给容器（运行中的程序）而不是另一个对象（硬编码的代码）的设计思想叫控制反转（IOC），这就是Spring所做的事情



@[TOC]

# 一、Mybatis基础知识扫盲

## 1、什么是 MyBatis？

> **MyBatis 是一款优秀的持久层框架，它支持自定义 SQL、存储过程以及高级映射。MyBatis 免除了几乎所有的 JDBC 代码以及设置参数和获取结果集的工作。MyBatis 可以通过简单的 XML 或注解来配置和映射原始类型、接口和 Java POJO（Plain Old Java Objects，普通老式 Java 对象）为数据库中的记录**
>
> ——摘录自[官方文档](https://mybatis.org/mybatis-3/zh/index.html)

## 2、从JDBC到Mybatis的演进



> **JDBC代码的缺陷**

+ 重复代码多，开发效率低
+ 需要关注Connection，PreStatement，ResultSet 对象的创建和销毁
+ 对ResultSet查询的结果，需要自己封装为List
+ 业务代码和数据库代码混在一起

> **Mybatis提供的功能**

1. 提供了JDBC中需要手动创建Connection，PreStatement，ResultSet 对象的功能
2. 提供了执行SQL语句的功能
3. 提供循环结果集，将查询结果转为java对象/List集合的功能
4. 提供了关闭资源的功能

**开发人员在Mybatis只需要关注SQL语句的编写**

![image-20210527185040355](https://i.loli.net/2021/05/27/4wBtpMvhQAcbTRd.png)



## 3、Mybatis与JPA框架对比选型

参考文章：[持久层框架JPA与Mybatis该如何选型](https://cloud.tencent.com/developer/article/1702931)

| 对比项       | Spring Data JPA                                              | Mybatis                                                      |
| :----------- | :----------------------------------------------------------- | :----------------------------------------------------------- |
| 单表操作方式 | 只需继承，代码量极少，非常方便。而且支持方法名用关键字生成SQL | 可以使用代码生成工具或Mybatis-Plus等工具，也很方便，但相对JPA要弱一些。 |
| 多表关联查询 | 不太友好，动态SQL使用不够方便，而且SQL和代码耦合到一起       | 友好，可以有非常直观的动态SQL                                |
| 自定义SQL    | SQL写在注解里面，写动态SQL有些费劲                           | SQL可以写在XML里面，是书写动态SQL语法利器。也支持注解SQL。   |
| 学习成本     | 略高                                                         | 较低 ,基本会写SQL就会用                                      |

- 如果你是自己开发“小而美”的应用，建议你使用JPA
- 如果你是开发大而全的企业级应用，当然要遵从团队的技术选型。这个技术选型在国内通常是Mybatis。
- 如果你们公司的管理非常规范，微服务落地经验也非常成熟，可以考虑在团队项目中使用JPA。少用或不用关联查询





# 二、快速上手Mybatis框架

## 运行环境的搭建

+ 要使用 MyBatis， 只需将 [mybatis-x.x.x.jar](https://github.com/mybatis/mybatis-3/releases) 文件置于类路径（classpath）中即可。

+ 如果使用 Maven 来构建项目，则需将下面的依赖代码置于 pom.xml 文件中：

```xml
<!--Mybatis依赖-->
<dependency>
	<groupId>org.mybatis</groupId>
	<artifactId>mybatis</artifactId>
	<version>x.x.x</version>
</dependency>
<!--MySQL驱动依赖-->
<dependency>
	<groupId>mysql</groupId>
	<artifactId>mysql-connector-java</artifactId>
	<version>8.0.23</version>
</dependency>
```

> 构建Mybatis的核心——SqlSessionFactory实例

每个基于 MyBatis 的应用都是以一个 SqlSessionFactory 的实例为核心的。SqlSessionFactory 的实例可以通过 SqlSessionFactoryBuilder 获得。而 SqlSessionFactoryBuilder 则可以从 XML 配置文件或一个预先配置的 Configuration 实例来构建出 SqlSessionFactory 实例





## 一个完整的Mybatis查询案例

### 准备工作

1. idea创建一个空Maven项目

2. 在pom.xml中添加**Mybatis依赖/mysql驱动依赖**

3. 在数据库中新建一个表用于测试

   ![image-20210527163526273](D:\桌面\P_picture_cahe\TROh7Kqj5vSbize.png)

4. Java中编写与表对应的实体类

   ```java
   //Lombok简化开发
   @Data
   @AllArgsConstructor
   @NoArgsConstructor
   public class Goods {
       private int gid;//商品id
       private String gprice;//商品价格
       private String gname;//商品名称
       private String gurl;//商品图片地址url
       private int glist_id;//商品分类id
       private String gdesc;//商品描述
   }
   ```

### Mybatis配置部分详解

> dao层接口定义操作数据库的方法

```java
package com.loki.dao;
//查询Goods表的所有数据
public class GoodsDao{
    public List<Goods> selectGoods();
}
```

> **SQL映射XML文件**: `xxxMapper.xml  `

用来编写和表中方法对应的SQL语句，一般一个表对应一个sql映射文件

**注：Demo中放在与dao层接口在同一目录下，并且约定xml文件名和接口名相同**

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!--sql映射文件，用来编写sql让mybatis执行-->
<!--
指定约束文件
  约束文件名称 : mybatis-3-mapper.dtd
  约束文件作用 : 检查当前文件中出现的标签必须符合mybatis的要求
-->
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<!--mapper当前文件的根标签-->
<!--
namespace
命名空间
命名：约定使用dao接口的全限定名称 copy reference
-->
<mapper namespace="org.example.dao.GoodsDao">
<!--
id
    要执行sql语句的唯一标识，mybatis会使用这个id值来找到需要执行的sql语句
    命名：约定使用接口中方法名称
resultType
    表示sql执行后，得到的java对象类型
    命名: 类名的全限定名称 copy reference
-->
    <select id="selectGoods" resultType="org.example.pojo.Goods">
    select * from Goods
  </select>
</mapper>
```

> Mybatis的主配置文件:  `mybatis-config.xml`

一个项目一个主配置文件主配置文件，提供了数据库的连接信息和sql映射文件的位置

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <environments default="development">
        <environment id="development">
            <transactionManager type="JDBC"/>
            <dataSource type="POOLED">
                <!-- 这里的value值需要你自己定义，关于你数据库连接的基本信息 -->
                <property name="driver" value="${driver}"/>
                <property name="url" value="${url}"/>
                <property name="username" value="${username}"/>
                <property name="password" value="${password}"/>
            </dataSource>
        </environment>
    </environments>
    <mappers>
        <!--这里要指定自己maper文件的路径-->
        <mapper resource="org\example\dao\GoodsDao.xml"/>
    </mappers>
</configuration>
```

> **构建Mybatis的核心——SqlSessionFactory实例，以此来获取 SqlSession 实例**

每个基于 `MyBatis` 的应用都是以一个 `SqlSessionFactory` 的实例为核心的。SqlSessionFactory 的实例可以通过 `SqlSessionFactoryBuilder` 获得。而 `SqlSessionFactoryBuilder` 则可以从 `XML配置文件`或一个预先配置的 `Configuration` 实例来构建出 SqlSessionFactory 实例，既然有了 SqlSessionFactory，顾名思义，我们可以从中获得 `SqlSession` 的实例。SqlSession 提供了在数据库执行 SQL 命令所需的所有方法。你可以通过 SqlSession 实例来直接执行已映射的 SQL 语句

```java
package top.loki.test;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;
import java.util.List;

public class test {
    public static void main(String[] args) throws Exception {
        //访问mybatis读取Goods数据
        //1、定义mybatis主配置文件的名称,从类路径的根开始
        String config = "mybatis.xml";
        //2、读取这个config表示的文件
        InputStream in = Resources.getResourceAsStream(config);
        //3、创建SqlSessionFactoryBuilder对象
        SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
        //4、创建SqlSessionFactory对象
        SqlSessionFactory factory = builder.build(in);
        //5、【重要】从SqlSessionFactory中获取SqlSession对象
        SqlSession sqlSession = factory.openSession();
        //6、【重要】指定要执行的sql语句的标识
        //sql映射文件中的namespace  + "." + 标签id值
        String sqlId = "org.example.dao.GoodsDao" + "." + "selectGoods";
        //7、通过sqlId找到语句并执行
        List<Object> goodsList = sqlSession.selectList(sqlId);
        goodsList.forEach(good -> System.out.println(good));
        sqlSession.close();
    }
}
```

## 初探Mybatis源码——主要类介绍

> **1）Resources**

负责读取mybatis主配置文件

```java
//读取mybatis主配置文件
InputStream in = Resources.getResourceAsStream("mybatis.xml");
```

> **2）SqlSessionFactoryBuilder**

**作用：创建SqlSessionFactory对象**

**这个类可以被实例化、使用和丢弃，一旦创建了 SqlSessionFactory，就不再需要它了。 因此 SqlSessionFactoryBuilder 实例的最佳作用域是方法作用域（也就是局部方法变量）。 你可以重用 SqlSessionFactoryBuilder 来创建多个 SqlSessionFactory 实例，但最好还是不要一直保留着它，以保证所有的 XML 解析资源可以被释放给更重要的事情**

```java
//创建SqlSessionFactoryBuilder对象
SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
```

> **3）SqlSessionFactory**

**SqlSessionFactory 一旦被创建就应该在应用的运行期间一直存在，没有任何理由丢弃它或重新创建另一个实例。 使用 SqlSessionFactory 的最佳实践是在应用运行期间不要重复创建多次，多次重建 SqlSessionFactory 被视为一种代码“坏习惯”。因此 SqlSessionFactory 的最佳作用域是应用作用域。 有很多方法可以做到，最简单的就是使用单例模式或者静态单例模式**

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

> **4）SqlSession**

**每个线程都应该有它自己的 SqlSession 实例。SqlSession 的实例不是线程安全的，因此是不能被共享的，所以它的最佳的作用域是请求或方法作用域。 绝对不能将 SqlSession 实例的引用放在一个类的静态域，甚至一个类的实例变量也不行。 也绝不能将 SqlSession 实例的引用放在任何类型的托管作用域中，比如 Servlet 框架中的 HttpSession。 如果你现在正在使用一种 Web 框架，考虑将 SqlSession 放在一个和 HTTP 请求相似的作用域中。 换句话说，每次收到 HTTP 请求，就可以打开一个 SqlSession，返回一个响应后，就关闭它。 这个关闭操作很重要，为了确保每次都能执行关闭操作，你应该把这个关闭操作放到 finally 块中。 下面的示例就是一个确保 SqlSession 关闭的标准模式**

```java
try (SqlSession session = sqlSessionFactory.openSession()) {
  // 你的应用逻辑代码
}
```

+ 定义了操作数据库的各种方法，例如 selectOne(),selectList(),insert(),update(),rollback

+ 接口的默认实现类 -- DefaultSqlSession

+ 该对象不是线程安全的，需要在方法内部中使用，为了保证线程安全---> 执行sql语句之前，使用openSession()获取sqlSession，执行完sql语句后，将sqlSession关闭（SqlSession.close()）


## Mybatis参数传递

**从java代码中把参数传递到mapper.xml文件的操作叫做参数传递**

> **情况一、传入一个简单类型参数（指java中的基本数据类型和String类型）**

接口

```java
public class GoodsDao{
    public Goods selectGoodsById(Integer id);
}
```

接口的sql映射文件

```xml
<mapper namespace="org.example.dao.GoodsDao">
    <select id="selectGoodsById" parameterType="java.lang.Integer" resultType="org.example.pojo.Goods">
        select * from Goods where gid=#{id}
    </select>
</mapper>
```

注：`parameterType`字段的解释：指dao接口中方法的参数类型

1. 写在mapper文件中的一个属性
2. 可以不写，mybatis通过反射机制也能发现接口形参的类型
3. 它的值是java数据类型的全限定名称或者是mybatis定义的别名（官网可以搜到），例如：`parameterType="java.lang.Integer"`

> **情况二：传入多个参数**

+ **第一种方式、参数之前加@param注解**

  接口

```java
public class GoodsDao{
    public Goods selectGoods(@param("myname")Integer id，@param("myprice") Integer gprice);
}
```

​	mapper文件

```xml
<mapper namespace="org.example.dao.GoodsDao">
    <select id="selectGoods" resultType="org.example.pojo.Goods">
	select * from Goods where gname=#{myname} or gprice=#{myprice}
    </select>
</mapper>
```

+ **第二种方式、使用java对象传参**

  接口

```java
//这里直接传入一个java对象
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

还有一些其他的方式，读者自行了解

+ 使用Map的key可以传参 - #{key}
+ 按位置传参 - #{arg(1)}

## Mybatis配置文件中#{}和${}取参的比较

> **从源码角度分析**

+ 使用#{}之后，mybatis执行sql使用的是jdbc中的`PreparedStatement`对象

+ 使用${}之后，mybatis执行sql使用的是jdbc中的`Statement`对象

**关于JDBC如何操作数据库，请移步[快速上手*JDBC*——Java如何在底层操作数据库](https://blog.csdn.net/Night__breeze/article/details/120653466?ops_request_misc=%7B%22request%5Fid%22%3A%22165264015516781435457845%22%2C%22scm%22%3A%2220140713.130102334.pc%5Fblog.%22%7D&request_id=165264015516781435457845&biz_id=0&utm_medium=distribute.pc_search_result.none-task-blog-2~blog~first_rank_ecpm_v1~rank_v31_ecpm-1-120653466-null-null.nonecase&utm_term=jdbc)**



> 从编译后的sql语句分析

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

总结，通常使用 # 来取参

## Mybatis 日志

日志可以将sql打印出来，对解决bug来说非常重要

> 开启方式

在 Mybatis核心配置文件 mybatis.xml中添加以下配置

```xml
<!--注意是在configuration标签中添加-->
<configuration>
    <!--开启Mybatis日志-->
    <settings>
        <setting name="logImpl" value="STDOUT_LOGGING"/>
    </settings>
<configuration/>
```

开启之后如果执行sql语句会在控制台打印相关信息

![image-20210529173107907](https://s2.loli.net/2022/05/16/f9PvzCL6OB7Mtyq.png)



## Loki的总结

> 从Demo中可以看出Mybatis虽然简化了Jdbc的开发，但是仍然有较高的冗余度，怎么解决这些问题呢？

+ 代码层面，可以抽取公共代码，做一个MybatisUtil工具类，管理Mybatis运行过程中的实例

+ 如果与Spring进行整合，依赖注入框架可以创建线程安全的、基于事务的 SqlSession 和映射器，并将它们直接注入到你的 bean 中，因此可以直接忽略它们的生命周期。 如果对如何通过依赖注入框架使用 MyBatis 感兴趣，可以研究一下 `MyBatis-Spring` 或 `MyBatis-Guice` 两个子项目
+ 使用 Mybatis-plus 来简化开发



# 五、Mybatis主配置文件详解

> **首先，xml文档本身就是一个很严格的文本类型，coding过程中不要出现多余的空格之类的字符，验证xml格式文件是否正确最快的方式是直接将其拖入浏览器，若不正确，浏览器会报错**

1.  **这个文件是MyBatis 中极为重要的调整设置，它们会改变 MyBatis 的运行时行为**
2.  **该文件对标签的先后顺序有严格要求，具体可以查看mybatis官方文档**

![image-20210602181933976](https://i.loli.net/2021/06/02/Pow4WaIYsO8Vy9Q.png)



如果你的`mybatis-config.xml`中标签顺序不当会报错

![image-20210602182146477](https://i.loli.net/2021/06/02/kyX9wLM1xsGZ3gD.png)



**以下是Loki整理的Mybatis主配置文件的详细注释**

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!--
    mybatis的主配置文件
        主要定义了数据库的配置信息，sql映射文件的位置
-->
<!--约束文件-->
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<!--根标签-->
<configuration>
    <!--指定properties文件的位置，从类路径根开始找-->
    <properties resource="jdbc.properties"/>
    <!--日志
		log4j
    -->
    <settings>
        <setting name="logImpl" value="STDOUT_LOGGING"/>
    </settings>
    <!--环境配置：数据库连接信息
        default 表示使用哪一个环境（连接哪个数据库）
    -->
    <environments default="development">

        <!--id是唯一值，表示环境的名称 -->
        <environment id="development">
            <!--了解即可
                transactionManager : mybatis提交事务回滚方式
                type : 事务处理的类型
        type属性有两个值
        1、JDBC(表示使用jdbc中的connection对象commit，rollback做事务回滚)
        2、MANAGED：把mybatis的事务处理委托给其他的容器
            -->
            <transactionManager type="JDBC"/>
            <!--datasource表示数据源
			java体系中规定：实现了javax.sql.DataSource接口的都是数据源，数据源表示connection对象
			type表示数据源类型
			  1)POOLED : 使用连接池，mybatis会创建PooledDataSource类
			  2)UPOOLED : 不使用连接池，每次执行sql语句，先创建连接，执行sql，然后再关闭连接
			-->
            <dataSource type="POOLED">
                <!--name属性不能随便更改-->
                <property name="driver" value="${jdbc.driver}"/>
                <property name="url" value="${jdbc.url}"/>
                <property name="username" value="${jdbc.user}"/>
                <property name="password" value="${jdbc.pwd}"/>
            </dataSource>
        </environment>

        <environment id="onlinedevelopment">
            <transactionManager type="JDBC"/>
            <dataSource type="POOLED">
                <!--将以下value值改为你自己的 -->
                <property name="driver" value="${driver}"/>
                <property name="url" value="jdbc:mysql://localhost:3306/数据库名"/>
                <property name="username" value="${username}"/>
                <property name="password" value="${password}"/>
            </dataSource>
        </environment>
    </environments>
    <!--
    一个mappers标签可以包括多个mapper标签
	一个mapper标签指定一个SQL映射文件的位置

    -->
    <mappers>
        <!--两种方式指定mapper文件路径-->
        <!--第一种：
        这个路径是从类路径(target\classes)开始的路径
        target\classes\org\example\dao\GoodsDao.xml
        -->
        <mapper resource="org\example\dao\GoodsDao.xml"/>

        <!--第二钟
        使用包名指定mapper文件
            name: xml文件所在的包名
            使用包名后，这个包中所有的xml文件可以一次加载给mybatis
        使用package的要求：
            1)mapper文件和接口名称一样，区分大小写
            2)mapper文件和接口需要在同一目录
        -->
        <package name="org.example.dao"/>
    </mappers>
</configuration>
```

















# 四、Mybatis动态代理



> 先分析动态代理的必要性，然后给出动态代理的步骤

​		[直接看动态代理怎么用](#1)

## 先分析一下使用接口实现类来操作数据库

```xml
<!--mybatis-mapper文件-->
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="org.example.dao.GoodsDao">
    <select id="selectGoods" resultType="org.example.pojo.Goods">
        select * from Goods
    </select>
</mapper>
```



```java
//GoodsDao接口
public interface GoodsDao {
    //查询Goods表的所有数据
    public List<Goods> selectGoods();
}
```

```java
//GoodsDao接口实现类
public class GoodsDaoImpl implements GoodsDao{
    @Override
public List<Goods> selectGoods(){
    //MybatisUtils是我自己写的工具类
   SqlSession sqlSession = MybatisUtils.getSqlSession();
	//获取sqlId
   String sqlId = "org.example.dao.GoodsDao.selectGoods";
    //执行查询操作
   List<Goods> Goods = sqlSession.selectList(sqlId);
	//测试输出
   Goods.forEach(good -> System.out.println(good));
    //关闭流
    sqlSession.close();
    //返回Goods集合
    return Goods;
    }
}

```



```java
 @Test
    public void testSelectGoods() {
        //接口创建实现类对象
        GoodDao dao = new GoodsDaoImpl();
        //使用该对象调用接口中的方法
        List<Goods> goods = dao.selectGoods();
        //测试输出
        goods.forEach(g->System.out.println(g));
    }
```

### 分析List<Goods> goods = dao.selectGoods();

 1、dao对象类型是`GoodDao.dao`,通过反射获取它的全限定名称`org.example.dao.GoodsDao`,这个全限定名称和**namespace** 是一样的

2、方法名称`selectGoods` 这个方法名就是**mybatis-mapper**文件中的**id**值

3、通过dao中方法的返回值可以确定需要调用sqlsession的方法
如果返回值是List，会调用SqlSession。selectList()方法

如果返回值是int，或者是非List 看mapper文件中的标签是<insert>或者<update>,会对应调用insert/update方法

**以上代码，需要手动编写实现类，实现类中重复代码过多，可以通过进一步封装减少代码量，这就用到了Myabtis的动态代理**

<div id="1"><div/>
## Mybatis动态代理


 mybatis根据dao接口，创建出一个dao的实现类，并创建该类对象，完成SqlSession中的数据库操作方法

```java
//GoodsDao接口
public interface GoodsDao {
    //查询Goods表的所有数据
    public List<Goods> selectGoods();
}
```

```java
//改进后的代码--不需要写dao层实现类
@Test
    public void testSelectGoods() {
/*
使用mybatis的动态代理机制--SqlSession.getmapper(dao接口)
getMapper可以获取dao接口对应实现类对象（底层实现之后更新）
*/
        //获取sqlsession
SqlSession sqlSession = MybatisUtils.getSqlSession();
        //sqlsession调用getMapper返回一个实现类对象
GoodsDao dao = sqlSession.getMapper(GoodsDao.class);
        //调用dao的方法，操作数据库
        List<Goods> goods = dao.selectGoods();

        goods.forEach(good -> System.out.println(good));

    }
```

### 基本步骤

![image-20210530232140152](D:\桌面\P_picture_cahe\PKiIke3SUxg1dfA.png)

### 使用动态代理的前提条件

1. dao接口和mapper文件在同一个目录
2. dao接口和mapper文件名称一致
3. mapper文件中的namespace属性值是dao接口的全限定名称
4. mapper文件中的<select><insert><update>等id值为接口中的方法名称



# 四、Mybatis实际开发重点辨析

## 辨析resultType和resultMap

> Mybatis把执行sql语句后返回的ResultSet结果集封装为java对象。通过指定`resultType`和`resultMap`来实现这一操作
>

![image-20210531175729756](D:\桌面\P_picture_cahe\6TWcBhOrE87lzSu.png)

### resultType

+  执行sql得到ResultSet需要转换的类型
+  使用类的全限定名称或者别名作为值
+  注意如果返回的是集合，应该写集合包含的类型，而不是集合
+  resultType和resultSet不能同时使用

> **mybatis内部的处理方式：**

1. mybatis执行sql语句，然后**调用该类的无参构造方法**，创建对象
2. mybatis把指定ResultSet指定列值赋值给同名属性

> **resultType的值定义别名**

在mybatis主配置文件中添加以下代码

```xml
<configuration>
     <typeAliases>
<!--、name是包的名字添加这个之后，这这个包中的所有类，类名就是别名(类名不区分大小写)-->
        <package name="org.example.pojo"/>
    </typeAliases>
</configuration>
```

`resultType="org.example.pojo.Goods"`

如果定义别名之后在select标签中直接这样写

``resultType="Goods"``

> 返回值是Map类型

接口方法

```java
 Map<Object,Object> selectMapById(Integer id);
```

mapper文件

```xml
<!--sql执行后返回一个map集合-->
<select id="selectMapById" resultType="java.util.HashMap">
    select gname,gprice from Goods where gid=#{id}
</select>
```

### resultMap

+ resultMap可以自定义sql的结果和Java对象属性的映射关系，常用在列名和Java对象属性不一样的情况
+ 使用方式，先定义resultMap，指定列名和属性的对应关系
+ 在select标签中把resultType替换为resultMap

> 接口方法
>

```java
List<Goods> selectAllGoods();
```

> mapper文件
>

使用resultMap
1）先定义resultMap
2) 在select标签，使用resulyMap来引用定义

```xml
<!--定义resultMap
        id : 自定义名称，表示你定义的这个resultMap
        type : java类型的全限定名称
    -->
    <resultMap id="goodsMap" type="org.example.pojo.Goods">
        <!--
            column : 列名
            property : java类型的属性名
        -->
        <!--主键列使用id标签-->
        <id column="Gid" property="gid"/>
        <!--非主键列使用result标签-->
        <result column="Gname" property="gname"/>
        <result column="Gprice" property="gprice"/>
    </resultMap>

	<!--resultMap的值为之前自定义的id-->
    <select id="selectAllGoods" resultMap="goodsMap">
        select * from Goods
    </select>
```

这是属性名和列名正常对应的情况

![image-20210601183939299](https://s2.loli.net/2022/05/16/jdH8MEtGIXUPmOg.png)

假如` <id column="Gid" property="gid"/>`改为` <id column="Gid" property="gprice"/>`

查询返回结果属性名和列名会改变

![image-20210601183854211](https://s2.loli.net/2022/05/16/sd8rp5oDGMzXZiP.png)



## like查询

> like在sql中可以做模糊查询，如果对sql语句不是很熟悉可以戳

接口方法

```java
    /*模糊查询 -- 在java代码中指定 like 的内容*/
    List<Goods> selectLikeOne(String name);
```

mapper文件

```xml
<!--推荐like内容写在java代码中，也可以在mapper文件中使用字符串拼接-->
    <select id="selectLikeOne" resultType="Goods">
        select * from Goods where gname like #{name}
    </select>
```

测试代码

```java
@Test
    public void test() {
        //第一步获取sqlsession
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        //第二步调用getmapper方法
        GoodsDao dao = sqlSession.getMapper(GoodsDao.class);
        //在这一步中可以指定模糊查询的内容 
        String qiu ="球";
        String name = "%"+qiu+"%";
        //调用dao的方法，操作数据库
        List<Goods> goods = dao.selectLikeOne(name);
        System.out.println(goods);
    }
```





## 动态SQL



动态SQL：sql的内容是变化的，可以根据条件获取到不同的sql语句，主要是where部分发生变化

### if

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

sql语句为：![image-20210601194058230](https://s2.loli.net/2022/05/16/896qQzTAe1sYZGy.png)

**如果是多个if语句可能会报语法错误**

![image-20210601194310565](https://s2.loli.net/2022/05/16/l5dZBwybkjLzG8T.png)

> 解决方法： 在where后面加一个恒成立的条件，不影响sql的执行 比如`1=1`,效果如下

![image-20210601194424884](https://s2.loli.net/2022/05/16/qLKkhe2wFaml6uD.png)

### where

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

![image-20210601201042285](https://s2.loli.net/2022/05/16/5ZiwVbdSuNmAoDl.png)

### foreach

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

![image-20210601202557339](https://s2.loli.net/2022/05/16/LRjgBWhwntiyVUN.png)

**如果集合中不是简单值，是一个对象的话，也可以使用foreach**

```xml
item的值就就代表一个集合中的对象
#{对象.属性名}
```

### 使用重复sql片段

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

### 总结

![image-20210602163010269](https://s2.loli.net/2022/05/16/XPdLYDOskKm1pQN.png)



## 五、深入Mybatis——源码阅读

















