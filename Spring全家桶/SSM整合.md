# Readme.md

**demo-name：**图书管理系统

**demo-Demand analysis**:

+ 前端操作界面，和数据库进行交互，实现简单CRUD

**How to do?**

+ 分析需求，从持久层开始，自下向上先实现dao层，service层，controller层功能
+ 编写配置文件
+ 前端界面

**代码Github地址：**



## 1、新建空maven项目，pom.xml导入相关依赖

> tip:
>
> 1）如果直接创建maven-webapp项目，不能保证web.xml的版本是最新的

## 2、添加web框架支持

![image-20210623174313522](https://i.loli.net/2021/06/23/lihTbqKZSprnzGP.png)

## 3、完善包结构

![image-20210623170430356](https://i.loli.net/2021/06/23/lbHmgizEsxwPMc1.png)



## 4、 持久层

+ `database.properties` 数据库连接信息，被spring-dao读取

+ `mybatis-config.xml` Mybatis主配置文件

+ `spring-dao.xml` spring整合mybatis
+ `BookMapper` 功能dao层接口
+ `BookMapper.xml` 写Sql语句的地方

## 5、Service层

+ `spring-service.xml`Spring整合service层

+ `BookService`功能service层接口
+ `BookServiceImpl.java` 接口实现类

## 6、SpringMVC层

+ `web.xml` 注册DispatcherServlet和过滤器拦截器等
+ `spring-mvc.xml`Spring整合SpringMVC
+ `org.example.controller.XxxController`

## 7、applicationContext.xml的整合

```xml
	<import resource="spring-dao.xml"/>
    <import resource="spring-service.xml"/>
    <import resource="spring-mvc.xml"/>
```



## 8、前端界面

+ `/WEB-INF/jsp/xxx.jsp`

  Controller层return的字符串要被视图解析器解析后返回给前端，所以这个jsp页面的路径是固定的

