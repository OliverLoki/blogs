# Mybatis详细入门笔记01---简介&maven搭建mybatis环境

> Mybatis中文文档：
>
> https://mybatis.org/mybatis-3/zh/index.html

## Mybatis简介

​	MyBatis 是一款优秀的持久层框架，它支持自定义 SQL、存储过程以及高级映射。MyBatis 免除了几乎所有的 JDBC 代码以及设置参数和获取结果集的工作。MyBatis 可以通过简单的 XML 或注解来配置和映射原始类型、接口和 Java POJO（Plain Old Java Objects，普通老式 Java 对象）为数据库中的记录。

## 什么情况下需要Mybatis

​	如果你所做的项目业务比较复杂，那么在DAO层可以考虑使用MyBatis框架，MyBatis本是apache的一个开源项目iBATIS，2010年这个项目由apache software foundation迁移到了google code，并且改名为MyBatis。2013年11月迁移到Github

## Dao层JDBC代码的缺陷

+ 重复代码多，开发效率低
+ 需要关注Connection，PreStatement，ResultSet 对象的创建和销毁
+ 对ResultSet查询的结果，需要自己封装为List
+ 业务代码和数据库代码混在一起

## Mybatis提供了什么功能？

1. 提供了JDBC中需要手动创建Connection，PreStatement，ResultSet 对象的功能
2. 提供了执行SQL语句的功能
3. 提供循环结果集，将查询结果转为java对象/List集合的功能
4. 提供了关闭资源的功能

+ 开发人员在Mybatis只需要关注SQL语句的编写

![image-20210527185040355](https://i.loli.net/2021/05/27/4wBtpMvhQAcbTRd.png)



## 你的第一个Mybatis程序

<div id="1"><div/>


> 这是最基础的写法，为了让你对底层有更深刻的理解，之后很多代码都会被精简

用一个案例在Maven中实现Mybatis---七步走

1. idea创建一个空Maven项目

2. 在pom.xml中添加**Mybatis依赖/mysql驱动依赖**

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

3. 在数据库中新建一个表用于测试

   ![image-20210527163526273](https://i.loli.net/2021/05/28/TROh7Kqj5vSbize.png)

   

4. Java中编写与表对应的实体类

   ```java
   public class Goods {
       /*
       1、变量首字母必须小写，大写会抛出异常
       2、作用域必须是private
       3、属性与表中字段一一对应
       */
       private int gid;//商品id
       private String gprice;//商品价格
       private String gname;//商品名称
       private String gurl;//商品图片地址url
       private int glist_id;//商品分类id
       private String gdesc;//商品描述
   
       public Goods() {
       }
   
       public String getGdesc() {
           return gdesc;
       }
   
       public void setGdesc(String gdesc) {
           this.gdesc = gdesc;
       }
   
       @Override
       public String toString() {
           return "Goods{" +
                   "gid=" + gid +
                   ", gprice='" + gprice + '\'' +
                   ", gname='" + gname + '\'' +
                   ", gurl='" + gurl + '\'' +
                   ", glist_id=" + glist_id +
                   ", gdesc='" + gdesc + '\'' +
                   '}';
       }
   
       public int getGid() {
           return gid;
       }
   
       public void setGid(int gid) {
           this.gid = gid;
       }
   
       public String getGprice() {
           return gprice;
       }
   
       public void setGprice(String gprice) {
           this.gprice = gprice;
       }
   
       public String getGname() {
           return gname;
       }
   
       public void setGname(String gname) {
           this.gname = gname;
       }
   
       public String getGurl() {
           return gurl;
       }
   
       public void setGurl(String gurl) {
           this.gurl = gurl;
       }
   
       public int getGlist_id() {
           return glist_id;
       }
   
       public void setGlist_id(int glist_id) {
           this.glist_id = glist_id;
       }
   
       public Goods(int gid, String gprice, String gname, String gurl, int glist_id, String gdesc) {
           this.gid = gid;
           this.gprice = gprice;
           this.gname = gname;
           this.gurl = gurl;
           this.glist_id = glist_id;
           this.gdesc = gdesc;
       }
   }
   ```

5. **创建持久层的dao接口，定义操作数据库的方法**

   ```java
   //查询Goods表的所有数据
   public List<Goods> selectGoods();
   ```

6. **创建一个mapper文件**

   + 也叫做sql映射文件---用来编写和表中方法对应的SQL语句，一般一个表对应一个sql映射文件
   + **这个文件是xml文件，和dao层接口在同一目录下，并且约定xml文件名和接口名相同**

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
   
   <!--
       在当前文件中，可以使用特定标签代表特点数据库操作
       <select> 查询操作
       <update> 更新操作，在此标签中写update sql语句
       <insert> 插入操作
       <delete> 删除操作
   -->
   ```
   
7. **创建mybatis的主配置文件**

   + 一个项目一个主配置文件
   + 主配置文件提供了数据库的连接信息和sql映射文件的位置信
   + 以下是详细解释，实际中了解即可

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

   > 这个mapper文件的路径是指相对类路径的相对路径，如果target目录中没有导出对应的xml文件，可以参考以下文章：

8. 访问mybatis读取数据

   ```java
   package org.example;
   
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
           //lamda表达式
           goodsList.forEach(good -> System.out.println(good));
           sqlSession.close();
       }
   }
   
   ```











