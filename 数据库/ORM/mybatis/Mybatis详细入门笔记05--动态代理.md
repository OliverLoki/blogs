# Mybatis详细入门笔记05--动态代理

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

![image-20210530232140152](https://i.loli.net/2021/05/31/PKiIke3SUxg1dfA.png)

### 使用动态代理的前提条件

1. dao接口和mapper文件在同一个目录
2. dao接口和mapper文件名称一致
3. mapper文件中的namespace属性值是dao接口的全限定名称
4. mapper文件中的<select><insert><update>等id值为接口中的方法名称





