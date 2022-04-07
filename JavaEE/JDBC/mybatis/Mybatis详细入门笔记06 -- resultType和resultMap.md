# Mybatis详细入门笔记06 -- resultType和resultMap



Mybatis把执行sql语句后返回的ResultSet结果集封装为java对象的操作

![image-20210531175729756](https://i.loli.net/2021/05/31/6TWcBhOrE87lzSu.png)

## resultType

+  执行sql得到ResultSet需要转换的类型
+ 使用类的全限定名称或者别名作为值
+ 注意如果返回的是集合，应该写集合包含的类型，而不是集合
+ resultType和resultSet不能同时使用

**mybatis内部的处理方式：**

1. mybatis执行sql语句，然后**调用该类的无参构造方法**，创建对象
2. mybatis把指定ResultSet指定列值赋值给同名属性

**resultType的值定义别名**

​	   在mybatis主配置文件中添加以下代码

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

## 返回值是Map类型

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

## resultMap

+ resultMap可以自定义sql的结果和Java对象属性的映射关系，常用在列名和Java对象属性不一样的情况
+ 使用方式，先定义resultMap，指定列名和属性的对应关系
+ 在select标签中把resultType替换为resultMap

接口方法

```java
List<Goods> selectAllGoods();
```

mapper文件

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

![image-20210601183939299](https://i.loli.net/2021/06/01/IHbeVoumKORGY2h.png)

假如` <id column="Gid" property="gid"/>`改为` <id column="Gid" property="gprice"/>`

查询返回结果属性名和列名会改变

![image-20210601183854211](https://i.loli.net/2021/06/01/NMzdmEUQa6fKlGe.png)