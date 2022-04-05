#  Mybatis详细入门笔记04--使用模板创建mybatis配置文件

## 工具类包utils

解决重复代码较多的问题，但是在之后与spring的整合过程中会被配置文件所替代

```java
public class MybatisUtils {
    private static SqlSessionFactory factory;
    //类加载时创建SqlSesssionFactory对象
    static {
        String config = "mybatis.xml";
        try {
            InputStream in = Resources.getResourceAsStream(config);
            factory = new SqlSessionFactoryBuilder().build(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 	//获取SqlSesion的方法
    public static SqlSession getSqlSession() throws Exception {
        SqlSession sqlSession = null;
        if (null != factory) {
            sqlSession = factory.openSession(true);
        }
        return sqlSession;
    }
}
```

## idea中使用模板创建mybatis-config和mybatis-mapper

**step**

​	依次打开 idea-->File-->Setting

![image-20210529184915987](https://i.loli.net/2021/05/31/o7uAeNhLHT2IDFO.png)



![image-20210529185208013](https://i.loli.net/2021/05/31/4a1qxtPFhnUJ6uf.png)

![image-20210529185328249](https://i.loli.net/2021/05/31/h8KWAYlLuFVOJwU.png)

 