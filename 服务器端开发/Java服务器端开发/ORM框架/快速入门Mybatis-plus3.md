快速上手Mybatis-plus



> [快速上手JDBC——Java如何在底层操作数据库](https://blog.csdn.net/Night__breeze/article/details/120653466?spm=1001.2014.3001.5501)
>
> [超详细的Mybatis整理——优秀的持久层框架]()



`MyBatis-Plus`（简称 MP）是一个`MyBatis`的增强工具，在MyBatis的基础上只做增强不做改变，为简化开发、提高效率而生





# 一、从Jdbc到Mybatis到MP的演进与分析

推荐阅读：**[Java如何在底层操作数据库](https://blog.csdn.net/Night__breeze/article/details/120653466?spm=1001.2014.3001.5501)**

## JDBC分析

> 它有最显而易见的优点

1.直接访问数据库底层类。执行效率高

2.步骤比较简单，容易理解。

> 但这种传统的桥接模式也逐渐显现一些缺点：

+ **SQL语句硬编码在java代码文件中，由于实际应用sql语句经常发生变化。那么变动后就需要啊重新编译部署**

+ **频繁地创建数据库连接和释放，造成数据库资源浪费，要对异常进行捕捉处理，处理转换数据类型。最后还要正确关闭连接。较为繁琐，效率下降**
+ **对结果集解析存在硬编码（查询列名），sql变化导致解析代码变化，系统不易维护，如果能将数据库记录封装成pojo对象解析比较方便**
+ **有时进行CRUD操作时需要进行一些判断限定时，拼接sql语句的过程令人十分痛苦**

## Mybaits框架的出现

为了消除Jdbc这些问题，`Mybatis`将`sql`语句写在xml文件中，支持动态sql，就明显很好地解耦合，解决了硬编码和sql语句拼接的问题

+ **Mybatis建立了对象与数据库的orm字段关系映射，使得业务逻辑和数据访问逻辑分离，系统设计更清晰合理**

+ **提供映射标签，支持对象与数据库的orm字段关系映射，建立映射后，能够将程序中的对象自动持久化到关系数据库中，并且能够像操作对象一样从数据库获取数据**

+ **xml标签支持动态SQL。在执行时先判断好逻辑，再直接从数据库取结果。减轻数据库负担。动态sql消除了自行拼接sql的痛苦**

> 不足之处：

1. 整个框架还是比较简陋。实际sql代码还是要自己编写
2. 二级缓存机制不佳

解释：二级缓存是保存在Mapper对象中的，比如现在有一张user表，两个Mapper文件，AMapper.xml和BMapper.xml，B修改了user表中的内容，A是感知不到的，那么再从A里查询如果用到了缓存，就是旧的数据。所以就要必须所有增删改查的操作在同一个命名空间下了



## 使用Mybatis-plus简化开发

CRUD的sql大体都是比较接近的，项目持久层代码的结构也如此，那么有什么方法能自动生成这种高重复度的代码和项目结构呢？虽然mybatis也有代码生成器MBG可以生成mapper和映射文件，但Mybatis-plus在此基础上还可以生成service和controller。MybatisPlus的BaseMapper已经封装了许多基本的CRUD操作，我们只需要让pojo对应的dao继承BaseMapper就可以进行绝大部分需求下的CRUD，什么事情都帮我们干好，这还不香吗？



# 二、MybatisPlus+Springboot实现简单的分页查询

使用Mybatis-plus工具，我们只需要将我们定义的抽象接口，继承一个公用的 `BaseMapper<T>` 接口，就可以获得一组通用的crud方法，来操作数据库。使用Mybatis-plus时，甚至都不需要任何的xml映射文件或者接口方法注解，真正的**dao层零实现**



全新的 `MyBatis-Plus` 3.0 版本基于 JDK8，提供了 `lambda` 形式的调用，这个Demo要求如下：

- JDK 8+
- Maven or Gradle
- 准备一个测试数据库

**环境导入**

> **一、导入Mybatis plus依赖**

```xml
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-boot-starter</artifactId>
    <version>3.5.1</version>
</dependency>
```

:a: **注意: **

+ 推荐idea安装`MybatisX`插件，这样可以方便mapper文件和接口的跳转
+ 引入 MyBatis-Plus 之后请不要再次引入 MyBatis 以及 MyBatis-Spring，以避免因版本差异导致的问题

> **二、配置`application.yaml`文件**

```yaml
#关闭模板引擎缓存
spring:
  datasource:
    username: root
    password: root
    url: jdbc:mysql://localhost:3306/db?serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8
mybatis-plus:
  type-aliases-package: com.loki.pojo
  mapper-locations: classpath:mapper/*.xml
configuration:
  map-underscore-to-camel-case: false
  #配置日志输出,需要导入相应依赖
  log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
  #关闭属性名大写自动转换
  #configuration:
  #map-underscore-to-camel-case: false
```

> **三、SpringbootApplication 配置 MapperScan 注解**

```java
@SpringBootApplication
//这个添加自己Mapper包的路径
@MapperScan("com.baomidou.mybatisplus.samples.quickstart.mapper")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

> **四、配置分页插件**

+ 在启动类中使用`@MapperScan(basePackages= "com.loki.mapper")`批量扫描Mapper接口

+ PaginationInterceptor是MP的分页插件，想要使用MP进行分页查询，需要加入这个配置

```java
package com.loki.config;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisPlusConfig {
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }
}
```

> **业务代码**

**第一步：实体类**

Mybatis-Plus基于一组注解来解决实体类和数据库表的映射问题

![image-20211007231853914.png](https://i.loli.net/2021/10/07/QHTvapgBmdC7Rcj.png)

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
//指定对应的表，表名和类名一致时，可以省略value属性。
@TableName("user")
public class User {
    //指定类的属性映射的表字段，名称一致时可以省略该注解。
    @TableId("username")
    private String username;
    @TableField("password")
    private String password;
}
```

**第二步：Mapper接口**：只需要继承BaseMapper公共接口

```java
public interface UserMapper extends BaseMapper<User> {
    /**
     * 自定义方法，展示所有用户的用户名
     * @return 用户名集合
     */		
    @Select("select u_name from tb_user")
    List<String> listAllUsername();
}
```

**第三步:service接口**：mybatis-plus 还提供了 Service 层的快速实现。同样不需要写任何实现方法，即可轻松构建 Service 层

```java
//泛型中写入要操作的pojo类
public interface UserService extends IService<User> {
}
```

**第四步:service接口实现类**

```java
/**
 * 继承 ServiceImpl，实现 UserService 接口
 *      ServiceImpl：该类实现了 IService 接口，需要两个泛型参数
 *          参数1：对应的 Mapper 类
 *          参数2：对应的 Pojo 类
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
```

**第五步:Controller层**

```java
@RestController
public class TestController {
    @Autowired
    BookService bookService;//创建一个service层对象
    @RequestMapping("/test01")
    //从前端传入参数--当前页码，默认值为1
    public String test01(@RequestParam(value = "pn",defaultValue = "1")Integer pn){
        //参数：当前页码，每页几条记录
        Page<Books> bookPage = new Page<>(pn, 5);
        Page<Books> page = bookService.page(bookPage,null);
        return page;
    }
}
```



# 三、Mybatis-plus源码分析

**基于映射的原理`MyBatis-plus` 必然要解决两个问题**

1、`Mapper`中的方法与 `SQL` 语句的对应转化

2、`Mapper` 如何操作数据库



可以从以下两个类的源码来分析Mybatis-plus执行流程

+ 自动配置类 ：`MybatisPlusAutoConfiguration`

```java
@EnableConfigurationProperties({MybatisPlusProperties.class})
public class MybatisPlusAutoConfiguration implements InitializingBean {
	...
}
```

+ 配置项绑定==》 mybatis-plus: xxx 就是对mybatis-plus的配置

```java
@ConfigurationProperties(
    prefix = "mybatis-plus"
)
public class MybatisPlusProperties {
	...
}
```

# 四、Mybatis-plus常用操作

## 快速测试

> **Maven添加测试依赖**

```java
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-boot-starter-test</artifactId>
    <version>3.5.1</version>
</dependency>
```

> **编写测试用例**
>
> **通过 `@MybatisPlusTest` 可快速编写 Mapper 对应的测试类，实现快速测试代码**

```java
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@MybatisPlusTest
class MybatisPlusSampleTest {

    @Autowired
    private SampleMapper sampleMapper;

    @Test
    void testInsert() {
        Sample sample = new Sample();
        sampleMapper.insert(sample);
        assertThat(sample.getId()).isNotNull();
    }
}

```

## 批量查询与条件查询

```java
//批量查询
	blogMapper.selectBatchIds(Arrays.asList(1,2,3))
//条件查询--只查询姓名为博客名，复杂的条件查询一般用wapper做 
 	HashMap<String, Object> o = new HashMap<>();
	o.put("name","博客名");
	blogMapper.selectByMap(o);  
```

## 删除操作与逻辑删除

```java
//通过id删除
	blogMapper.deleteById(1);
//批量删除
	blogMapper.deleteBatchIds(Arrays.asList(1,2,3));
```

**逻辑删除**

> 物理删除:从数据库中直接移除
>
> 逻辑删除: 在数据库中没有被移除,而是通过一个变量来让他失效! deleted=0=>deleted=1

管理员可以查看被删除的记录!防止数据的丢失,类似于回收站!



## 条件构造器QueryMapper

```java
  // 查询name不为空的用户，并且邮箱不为空的用户，年龄大于等于12
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.isNotNull("name")
               .isNotNull("email")
               .ge("age",12);
        userMapper.selectList(wrapper).forEach(System.out::println); // 和我们刚才学习的map对比一下

```



## 自动填充

创建时间 . 修改时间! 这些个操作都是自动化完成的,我们不希望手动更新!

阿里巴巴开发手册:所有的数据库表:gmt_create .gmt_modified几乎所有的表都要配置上!而且需要自动化!

> 数据库级别

一般不会这么用，怕你删库跑路

在表中新增字段 create_time , update_time

字段类型为`datatime`

这样一来，在执行crud时，会自动修改当前时间

> 代码级别

![image-20211019232025779](D:\桌面\P_picture_cahe\image-20211019232025779.png)

```
//记住用util包下的Date!!
//字段添加填充内容
//字段自动填充
  @TableField(fill = FieldFill.INSERT)//在插入时更新数据
  private Date gmtCreate;
  @TableField(fill = FieldFill.INSERT_UPDATE)//在插入和更新时更新数据
  private Date gmtModified;
```

编写处理器来处理这个注解

```java
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component //把处理器加到IOC容器中
public class MyMetaObjectHandler implements MetaObjectHandler {

    //插入时的填充策略
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("Start insert fill.... ");
        this.setFieldValByName("createTime",new Date(),metaObject);
        this.setFieldValByName("updateTime",new Date(),metaObject);
    }

    //更新时的填充策略
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("Start update fill.... ");
        this.setFieldValByName("updateTime",new Date(),metaObject);
    }
}
```

测试插入





# 五、主键生成策略

```
@TableId(type = )
```

```
AUTO(0),
    NONE(1),
    INPUT(2),
    ASSIGN_ID(3),
    ASSIGN_UUID(4),
    /** @deprecated */
    @Deprecated
    ID_WORKER(3),
    /** @deprecated */
    @Deprecated
    ID_WORKER_STR(3),
    /** @deprecated */
    @Deprecated
    UUID(4);
```





# 六、逆向工程

主要有两个点需要注意
1、指定输出目录
**直接右键复制项目根目录的绝对路径**

2、设置mapperXml生成路径

**直接右键复制项目mapper文件夹的绝对路径，也就是`classpath:mapper`文件夹**

```java
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import java.util.Collections;

public class Generator {
    public static void main(String[] args) {
        FastAutoGenerator.create(
                "url",
                "username",
                "password")
                .globalConfig(builder -> {
                    builder.author("loki") // 设置作者
                            .fileOverride() // 覆盖已生成文件
                            .enableSwagger() // 开启 swagger 模式
                            // 指定输出目录
                            //直接右键复制项目根目录的绝对路径
                            .outputDir("E:\\Blog\\blogverson2\\lokiblog-springboot//src//main//java//com//loki"); 
                })
                .packageConfig(builder -> {
                    builder.parent("") // 设置父包名
                    		// 设置mapperXml生成路径
                            //直接右键复制项目mapper文件夹的绝对路径
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml,"E://Blog//blogverson2//lokiblog-springboot//src//main//resources//mapper")); 
                })
                .strategyConfig(builder -> {
                    builder.addInclude("blog").addInclude("user") // 设置需要生成的表名
                            .addTablePrefix("t_", "c_"); // 设置过滤表前缀
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
```



# 七、性能分析插件

我们在平时的开发中,会遇到一些慢sql.

MP也提供了性能分析插件,如果超过这个时间就停止运行



