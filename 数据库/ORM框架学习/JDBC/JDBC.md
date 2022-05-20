# 快速上手JDBC——Java如何在底层操作数据库

> 我们为什么学习这个，用一句话概括，JDBC就是用Java语言操作关系型数据库
>
> 原来我们操作数据库是在控制台使用SQL语句来操作数据库，JDBC是用Java语言向数据库发送SQL语句
>
> 同理，以后学习用Java语言操作非关系型数据库，例如用Java操作Redis，这个技术就被叫做Jedis



在阅读本文之前，假设你已经掌握以下知识点

+ 关系型数据库基础————[MySQL和常用的Sql语句](https://blog.csdn.net/Night__breeze/article/details/116943834)
+ Java基础

## 一、什么是JDBC

+ JDBC`(Java Database Connectitity`)是一个独立于特定数据库管理系统，通用的SQL数据库存取和操作的公共接口，定义了用来访问数据库操作的标准java类库
+ JDBC为访问不同的数据库提供了一种统一的途径，为开发者屏蔽了一些细节问题
+ JDBC是一个接口，由sun公司提供给各大数据库厂商，各数据库厂商编写实现这个接口的实现类，即为JDBC驱动

如图所示，不同的JDBC驱动实际上就是不同的jar包

![image-20211008153121625](https://i.loli.net/2021/10/08/AUls84YtwkOzVDN.png)

JDBC是接口，而JDBC驱动才是接口的实现，没有驱动无法完成数据库连接！每个数据库厂商都有自己的驱动，用来连接自己公司的数据库。

当然还有第三方公司专门为某一数据库提供驱动，这样的驱动往往不是开源免费的！

> 关于JDBC我们需要了解什么？

JDBC是以后框架学习的基础，我们应该自己尝试写一个Java从数据库中获取数据的Demo，了解JDBC的运行原理和获取数据的步骤

## 二、JDBC是怎样工作的？

JDBC主要完成三件事：

1）建立连接；

2）发送SQL语句；

3）处理返回的结果

我们先暂时了解一下Java如何通过JDBC驱动来获取一个数据库连接，之后的例子中我们会一步步具体实现这三件事

> 第一步：建立连接

使用JDBC时，我们先了解什么是`Connection`。Connection代表一个JDBC连接，它相当于Java程序到数据库的连接（通常是TCP连接）

打开一个Connection时，需要准备URL、用户名和口令，才能成功连接到数据库

URL是由数据库厂商指定的格式，例如，MySQL的URL是：

```
jdbc:mysql://<hostname>:<port>/<db>?key1=value1&key2=value2
```

假设数据库运行在本机`localhost`，端口使用标准的`3306`，数据库名称是`learnjdbc`，那么URL如下：

```
jdbc:mysql://localhost:3306/learnjdbc?useSSL=false&characterEncoding=utf8
```

后面的两个参数表示不使用SSL加密，使用UTF-8作为字符编码

我们通过以下代码来获取连接

```java
// JDBC连接的URL, 不同数据库有不同的格式
String JDBC_URL = "jdbc:mysql://localhost:3306/test";
String JDBC_USER = "root";
String JDBC_PASSWORD = "password";
// 获取连接，核心代码
Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
// TODO: 访问数据库...
// 关闭连接:
conn.close();
```

核心代码是`DriverManager`提供的静态方法`getConnection()`。`DriverManager`会自动扫描classpath，找到所有的JDBC驱动，然后根据我们传入的URL自动挑选一个合适的驱动



注：因为JDBC连接是一种昂贵的资源，所以使用后要及时释放



接下来，我们来进行一次实战，通过Java去数据库中查询数据

> 先记住一句话！不管你懂不懂，这在之后的操作中非常重要
>
> 使用Java对数据库进行操作时，必须使用PreparedStatement，严禁任何通过参数拼字符串的代码！

## 三、JDBC查询DEMO

这一步我会带你一步一步来完成JDBC的所有步骤，知道Java从数据库中获取到信息

> 首先，我们要准备一个环境来完成这个项目

新建一个普通Maven项目

因为我们选择了MySQL作为数据库，所以我们首先得找一个MySQL的JDBC驱动。所谓JDBC驱动，其实就是一个第三方jar包，我们直接添加一个Maven依赖就可以了：

```xml
<!--Maven导入数据库连接依赖-->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.23</version>
</dependency>
```

其次，我们需要一个测试数据库，这个我就不写了，去Naviacte准备一个数据库和测试表用来学习就可以了

下一步我们就可以查询数据库了。查询数据库分以下几步：

第一步，通过`Connection`提供的`createStatement()`方法创建一个`Statement`对象，用于执行一个查询；

第二步，执行`Statement`对象提供的`executeQuery("SELECT * FROM students")`并传入SQL语句，执行查询并获得返回的结果集，使用`ResultSet`来引用这个结果集；

第三步，反复调用`ResultSet`的`next()`方法并读取每一行结果。

完整查询代码如下：

```java
import java.sql.*;

public class TestJDBC {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        //配置url信息
        //test为数据库名
        String url = "jdbc:mysql://localhost:3306/test";
        String username = "root";
        String password = "root";

        //1、加载驱动，之后会产生一个异常，抛出即可
        //MySQL不同版本的包名不同，需要注意
        Class.forName("com.mysql.cj.jdbc.Driver");

        //2、连接数据库,connection代表数据库
        Connection connection = DriverManager.getConnection(url, username, password);

        //3、向数据库发送SQL的对象Statement --> CRUD
        Statement statement = connection.createStatement();

        //4、编写SQL
        String sql = "SELECT * from users";

        //5、执行查询SQL，返回一个resultSet : 结果集
        ResultSet rs = statement.executeQuery(sql);
        
        while (rs.next()) {
            System.out.println("id=" + rs.getObject("id"));
            System.out.println("name=" + rs.getObject("name"));
            System.out.println("password=" + rs.getObject("password"));
            System.out.println("email=" + rs.getObject("email"));
            System.out.println("birthday" + rs.getObject("birthday"));
            //6、关闭连接释放资源 先开后关原则
            rs.close();
            statement.close();
            connection.close();
        }
    }
}
```

> 但是，使用`Statement`拼字符串非常容易引发SQL注入的问题，这是因为SQL参数往往是从方法参数传入的
>
> 有关于SQL注入，通过以下这个例子简单理解一下
>
> ```sql
> select * from users where name= 'admin' and pwd='1' or 'a'='a'
> ```
>
> 这样的话，根据运算规则（先算and 再算or），最终结果为真，这样就可以直接获取到用户信息

因此，我们使用`PreparedStatement`对象来避免SQL注入。这也是更常用的方法

使用`PreparedStatement`可以*完全避免SQL注入*的问题，因为`PreparedStatement`始终使用`?`作为占位符，并且把数据连同SQL本身传给数据库，这样可以保证每次传给数据库的SQL语句是相同的，只是占位符的数据不同，还能高效利用数据库本身对查询的缓存。上述登录SQL如果用`PreparedStatement`可以改写如下

```java
import java.sql.*;
public class TestJDBC {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        //配置信息,没有url是无法连接到数据库的
        //解决中文乱码
        String url = "jdbc:mysql://localhost:3306/jdbc?";
        String username = "root";
        String password = "root";

        //1、加载驱动，会产生异常，抛出即可
        Class.forName("com.mysql.cj.jdbc.Driver");

        //2、Connection对象连接数据库
        Connection connection = DriverManager.getConnection(url, username, password);

        // 3、预编译SQL,编写SQL放在了前面，并且防止了SQL注入
        String sql = "insert into users(id,name,password,email,birthday) values (?,?,?,?,?)";

        //4、预编译SQL对象
        PreparedStatement pst = connection.prepareStatement(sql);

        pst.setInt(1, 2);//给第一个占位符？赋值为1
        pst.setString(2, "loki");
        pst.setString(3, "123456");
        pst.setString(4, "loki@qq.com");
        pst.setDate(5, new Date(new java.util.Date().getTime()));

        //5、执行SQL
        int i = pst.executeUpdate();
        if(i>0) {
            System.out.println("插入成功");
        }

        //6、 e.printStackTrace();
            request.setAttribute("error", "服务器端错误");
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("/WEB-INF/login.jsp");
            response.sendRedirect("/WEB-INF/login.jsp?error=" + URLEncoder.encode("服务器错误", "utf-8"));

        pst.close();
        connection.close();

    }
}
```

## 四、辨析Statement和PreparedStatement

preparedstatement接口是statement接口的子接口

> `Statement`： 用于执行静态SQL语句并返回它所生成的结果的对象

> `PreparedStatement`：SQL语句被预编译存储在此对象中，可以使用此对象多次高效的执行该语句(**常用**)

**使用Statement对象操作数据表的弊端**

+ `Statement`用于执行静态SQL语句,在执行时,必须指定一个事先准备好的SQL语句

+ `PreparedStatement`是预编译的SQL语句对象,语句中可以包含动态参数"?", 在执行时可以为"?"动态设置参数值

+ `PreparedStatement`可以减少编译次数提高数据库性能

+ 存在sql注入的问题	

  利用有些系统没有对用户输入数据进行充分检查，而在用户输入数据中注入非法的SQL语句段，从而利用系统的SQL引擎完成恶意行为的做法

## 四、总结JDBC操作数据库的步骤

1. 加载驱动`com.mysql.cj.jdbc.Driver`（这里是Mysql8版本的jdbc驱动）

   如果你见到 `com.mysql.jdbc.Driver`,也不必担心，这是之前版本的驱动名称

2. 获取`Connection`对象连接数据库

3. **向数据库发送SQL的对象**`statement `和`PreparedStatement`

4. 编写SQL

5. 执行SQL

6. 关闭连接 

![image-20210519215623614](https://i.loli.net/2021/05/19/wQtOhEPj3HxZ2yi.png)



​			