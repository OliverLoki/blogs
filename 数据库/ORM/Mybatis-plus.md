---
2022.3.26
---



# 快速上手Mybatis-plus



MyBatis-Plus（简称 MP）是一个 MyBatis (opens new window)的增强工具，在 MyBatis 的基础上只做增强不做改变，为简化开发、提高效率而生。

## Mybatis-plus简介

> 什么是Mybatis-Plus?
>
> 为什么学习它？

MyBatis-Plus（简称 MP）是一个 MyBatis 的增强工具，在 MyBatis 的基础上**只做增强不做改变**，为简化开发、提高效率而生。

我们已经学习过Mybatis这个框架，我们只需要在dao层定义抽象接口，基于Mybatis零实现的特性，就可以实现对[数据库](https://cloud.tencent.com/solution/database?from=10680)的crud操作。在业务类型比较多的时候，我们需要重复的定义一堆功能类似的接口方法。

使用Mybatis-plus工具，我们只需要将我们定义的抽象接口，继承一个公用的 `BaseMapper<T>` 接口，就可以获得一组通用的crud方法，来操作数据库。使用Mybatis-plus时，甚至都不需要任何的xml映射文件或者接口方法注解，真正的**dao层零实现** 

## 快速开始

**注：大环境是Springboot整合MP**

### 安装

全新的 `MyBatis-Plus` 3.0 版本基于 JDK8，提供了 `lambda` 形式的调用，所以安装集成 MP3.0 要求如下：

- JDK 8+
- Maven or Gradle

> 推荐idea安装`MybatisX`插件，这样可以方便mapper文件和接口的跳转

**导入Mybatis plus依赖**

```xml
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-boot-starter</artifactId>
    <version>3.5.1</version>
</dependency>
```

> 注意
>
> 引入 MyBatis-Plus 之后请不要再次引入 MyBatis 以及 MyBatis-Spring，以避免因版本差异导致的问题

### 配置

配置`application.yaml`文件

```yaml
#整合mybatis plus
mybatis-plus:
  #
  type-aliases-package: com.loki.pojo
  #
  mapper-locations: classpath:mapper/*.xml
  #关闭属性名大写自动转换_
  configuration:
    map-underscore-to-camel-case: false
  #配置日志输出,需要导入相应依赖
  log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
```

**配置 MapperScan 注解**

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

### 快速测试

Maven添加测试依赖

```java
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-boot-starter-test</artifactId>
    <version>3.5.1</version>
</dependency>
```

编写测试用例

通过 `@MybatisPlusTest` 可快速编写 Mapper 对应的测试类，实现快速测试代码

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

### 配置分页

PaginationInterceptor是MP的分页插件，想要使用MP进行分页查询，需要加入这个配置

```java
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

## 完整的流程

**第一步：实体类**

> Mybatis-Plus基于一组注解来解决实体类和数据库表的映射问题

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

**第二步：Mapper接口**

> 只需要继承BaseMapper公共接口

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

**第三步:service接口**

> mybatis-plus 还提供了 Service 层的快速实现。同样不需要写任何实现方法，即可轻松构建 Service 层

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

> 在启动类中使用`@MapperScan(basePackages= "com.loki.mapper")`批量扫描Mapper接口

```java
@Controller
public class TestController {
    @Autowired
    BookService bookService;//创建一个service层对象
    @RequestMapping("/test01")
    //从前端传入参数--当前页码，默认值为1
    public String test01(@RequestParam(value = "pn",defaultValue = "1")Integer pn,Model model){
        //参数：当前页码，每页几条记录
        Page<Books> bookPage = new Page<>(pn, 5);
        Page<Books> page = bookService.page(bookPage,null);
        model.addAttribute("page",page);
        return "test";
    }
}
```

> 前端部分代码
>

```html
<!-- 搭建显示页面 -->
<div class="container">
    <!-- 标题 -->
    <div class="row">
        <div class="col-md-12">
            <h1>员工信息管理系统</h1>
        </div>
    </div>
    <!-- 显示表格数据 -->
    <div class="row">
        <div class="col-md-12">
            <table class="table table-striped">
                <tr>
                    <th>ID</th>
                    <th>书名</th>
                    <th>数量</th>
                    <th>内容</th>
                </tr>
                <tr th:each="book:${page.records}">
                    <td th:text="${book.bookID}"></td>
                    <td th:text="${book.bookName}"></td>
                    <td th:text="${book.bookCounts}"></td>
                    <td th:text="${book.detail}"></td>
                </tr>
            </table>
        </div>
    </div>
     <!-- 显示分页信息 -->
    <div class="row">
        <!-- 分页文字信息 -->
        <div class="col-md-6">
            当前第 [[${page.current}]] 页,总 [[${page.pages}]] 页,总共有 [[${page.total}]] 条记录
        </div>
        <!-- 分页条信息 -->
        <div class="col-md-6">
            <nav aria-label="Page navigation example">
                <ul class="pagination justify-content-end">
                    <li class="page-item disabled">
                        <a class="page-link" href="#" tabindex="-1" aria-disabled="true">上一页</a>
                    </li>
                    <li th:class="${num==page.current?'page-item active':'page-item'}" th:each="num:${#numbers.sequence(1,page.pages)}">
                        <a class="page-link" th:href="@{/test01(pn=${num})}">[[${num}]]</a>
                    </li>

                    <li class="page-item">
                        <a class="page-link" href="#">下一页</a>
                    </li>
                </ul>
            </nav>
        </div>
    </div>
</div>
```

## 源码分析

**基于映射的原理`MyBatis-plus` 必然要解决两个问题**

1、`Mapper`中的方法与 `SQL` 语句的对应转化

2、`Mapper` 如何 操作数据库

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



## 常用代码

```java
//批量查询
	blogMapper.selectBatchIds(Arrays.asList(1,2,3))
//条件查询--只查询姓名为博客名，复杂的条件查询一般用wapper做 
 	HashMap<String, Object> o = new HashMap<>();
	o.put("name","博客名");
	blogMapper.selectByMap(o);  
```

**删除操作**

```
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

## 主键生成策略

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

## 自动填充

> 创建时间 . 修改时间! 这些个操作都是自动化完成的,我们不希望手动更新!
>
> 阿里巴巴开发手册:所有的数据库表:gmt_create .gmt_modified几乎所有的表都要配置上!而且需要自动化!

### 数据库级别

> 一般不会这么用，怕你删库跑路

在表中新增字段 create_time , update_time

字段类型为`datatime`

这样一来，在执行crud时，会自动修改当前时间

### 代码级别

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

## 逆向工程

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

## 乐观锁&悲观锁

> 乐观锁: 顾名思义十分乐观,他总是认为不会出现问题,无论干什么都不去上锁!如果出现了问题,再次更新值测试
>
> 悲观锁;顾名思义十分悲观,他总是认为出现问题,无论干什么都会上锁!再去操作!

## 性能分析插件

我们在平时的开发中,会遇到一些慢sql.

MP也提供了性能分析插件,如果超过这个时间就停止运行



## 条件构造器QueryMapper

```java
  // 查询name不为空的用户，并且邮箱不为空的用户，年龄大于等于12
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.isNotNull("name")
               .isNotNull("email")
               .ge("age",12);
        userMapper.selectList(wrapper).forEach(System.out::println); // 和我们刚才学习的map对比一下

```

