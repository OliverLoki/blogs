# Mybatis的主配置文件详解

**注意事项 :**

1.  **这个文件是MyBatis 中极为重要的调整设置，它们会改变 MyBatis 的运行时行为**
2. **该文件对标签的先后顺序有严格要求，具体可以查看mybatis官方文档**

![image-20210602181933976](https://i.loli.net/2021/06/02/Pow4WaIYsO8Vy9Q.png)

如果顺序不当会报错

![image-20210602182146477](https://i.loli.net/2021/06/02/kyX9wLM1xsGZ3gD.png)

​				3、注意coding过程中不要出现多余的空格之类的字符，xml文档本身就是一个很严格的文本类型

​			4、验证xml格式文件是否正确最快的方式是直接将其拖入浏览器，若不正确，浏览器会报错

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

