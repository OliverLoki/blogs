# Mybatis详细入门笔记07 -- like查询

> like在sql中可以做模糊查询，如果对sql语句不是很熟悉可以戳
>
> []()

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

