# Spring学习笔记

## 简介

**Spring的历史**

+ Spring最早是由`Rod Johnson`在他的`《Expert One-on-One J2EE Development without EJB》`一书中提出的用来取代`EJB`的轻量级**框架**。随后`Rod Johnson`又开始专心开发这个基础框架，并起名为Spring Framework。

+ Spring最初的版本--`interface21`，Spring框架以interface21为基础，并不断丰富，于2004.3.24发布了`Spring1.0`

**Spring是什么？**

+ Spring是一个轻量级控制反转(loC)和面向切面(AOP)的**容器框架**。

**Spring的目的**

+ 解决企业应用开发的复杂性，使用基本的JavaBean代理[EJB](https://baike.baidu.com/item/EJB)，使得对象的创建不在像之前那样使用new的方式，而通过注入的方式创建对象（可以先理解为调用了构造方法初始化一个对象）
+ 做一个粘合剂，使得现有的技术更容易使用

**Spring的现况**

+ 随着Spring越来越受欢迎，在Spring Framework基础上，又诞生了Spring Boot、 Spring Cloud、Spring Data、Spring Security等一系列基于Spring Framework的项 目。

**Spring的优点**

+ 开源免费
+ 轻量级的，非入侵式的
+ 控制反转（IOC）,面向切面编程（AOP）
+ 支持事务处理，对框架整合的支持

**Spring:the source fpr modern java**

![image-20210614155719379](https://i.loli.net/2021/06/14/HZvCoWTcszMXuxI.png)

+ Spring Boot -- Build Anything

  1. 一个快速开发的脚手架
  2. 基于Spring Boot可以快速地开发单个微服务
  3. 约定大于配置
+ Spring Cloud -- Coordinate Anything
  1. Spring Cloud 是基于Spring Boot实现的


+ SpringCloud Data Flow -- Connect Everything

**因为现在大多数公司都在使用SpringBoot进行快速开发，学习SpringBoot的前提，需要完全掌握Spring及SpringMVC!承上启下的作用!**

**Spring7大模块**

![image-20210614155600352](https://i.loli.net/2021/06/14/oaKXRCPLG3VWuNZ.png)

## Maven导入Spring依赖

![image-20210614154845549](https://i.loli.net/2021/06/14/xbLCpt79VXAJDyd.png)

```xml
<dependencies>
        <!--spring-webmvc-->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-webmvc</artifactId>
            <version>5.3.8</version>
        </dependency>
    </dependencies>
```

![image-20210622191302199](https://i.loli.net/2021/06/22/8XfiHtDThRWvVx4.png)

> 上文中提到Spring是一个轻量级控制反转(loC)和面向切面(AOP)的**容器框架**，现在先来讨论IoC

##  IOC容器

**IoC全称Inversion of Control，直译为控制反转。**

### 理解为什么要使用IoC

> 举一个栗子：**传统MVC架构下，service层要调用dao层代码**

```java
//假设一个接口有AImpl,BImpl,CImpl 三个实现类
//使用A实现类创建接口
private UserDao userDao = new AImpl();
```

> 弊端：传统方法：使用接口初始化类，这个userDao对象被写死了，如果要调用不同的实现类，必须要改这一行代码

如果使用IoC注入

```java
public class MyService implements UserService {
    //使用set方法注入userDao，可以保证userDao是动态的
    private UserDao userDao = new OracleImpl();
    public void setUserDao(UserDao userDao){
        this.userDao = userDao;
    }
    public void func() {
        userDao.func();
    }
}
```

以上代码的思想就是IoC的原型

### 理解控制反转

+ 传统方法，程序是主动创建对象，控制权在程序猿手上。使用了set注入后，程序不再具有主动性，而是变成了被动的接受对象。这就是**控制反转**

+ 控制反转是一种通过描述（XML或注解）并通过第三方去生产或获取特定对象的方式。在Spring中实现控制反转的是loC容器，其实现方法是**依赖注入**(`Dependency Injection`)。

###  IoC创建对象的方式

1. **默认通过调用类的无参构造初始化**

2. **如果类中有有参构造，导致无参构造消失，Spring也提供了使用有参构造初始化的方法**

   ```xml
   	
   <bean id="user" class="org.example.pojo.User">
           <constructor-arg name="name" value="wangzhe"/>
       </bean>
   ```

   ![image-20210614184626039](https://i.loli.net/2021/06/14/ehOxHEJWgkFGydL.png)

总结:

​	在配置文件加载的时候，容器中管理的对象就已经初始化了! 

## applicationContext.xml

### 这个文件是什么

**spring的核心配置文件**

### 该文件中的各种标签

+ 该文件的标签不多，但是bean的可选值有很多，是用来注入属性值使用的
+ Spring配置文件的根元素是`beans`节点，在该节点内，我们可以配置Spring内置的各种命名空间以及bean默认的几项配置，通过配置各种命名空间，然后使用各命名空间的元素来完成对Spring的配置

`bean`

```xml
<!--bean
            id bean的唯一标识符，相当于对象名
            class bean对象所对应的全限定名
            name 别名，可以取多个别名
            property 相当于给对象的属性设置一个值
                ref:引用Sping创建好的对象
			autowire="byname" 自动装配
byName:会自动在容器上下文中查找，和自己对象set方法后面的值对应的beanid !

    -->
```

`alias`配置别名，一般不用

```xml
<!--配置别名后，可以通过别名找到对象，没啥用，取别名用name不香嘛
name可以配置多个别名
同时设置id和name，那么id设置的是标识符，name设置的是别名
id和name的值相同，那么spring容器会自动检测并消除冲突：让这个bean只有标识符，而没有别名
name属性设置多个值。不设置id，那么第一个被用作标识符，其他的被视为别名。如果设置了id，那么name的所有值都是别名。
-->
    <alias name="wangjia,wangzhe" alias="shazhu"/>
```

`import`可以将多个配置文件，导入合并为一个

example:

![image-20210622191828631](https://i.loli.net/2021/06/22/zuHnwiTmtvEAPCN.png)

## 依赖注入（DI）--IoC能控制反转的底层实现

### 理解依赖注入

依赖：bean对象的创建依赖于容器

注入：bean对象中的所有属性，由容器来注入

### 依赖注入的方式

+ 构造器注入

  `example`：sqlSession没有set方法，只能使用构造器注入

  ```xml
  <!--sqlSession-->
  <bean id="sqlSession" class="org.mybatis.spring.SqlSessionTemplate">
          <constructor-arg index="0" ref="sqlSessionFactory"/>
  </bean>
  ```

+ **Set方式注入

  我们通常给类属性设置一个set方法使得它能被注入

+ 基于注解的 `@Autowired` 自动装配（Field 注入）

### Set方法注入

**测试类**，里面有不同类型的属性需要被注入

```java
package org.example.pojo;
import java.util.*;

public class Student {
    private String name;
    private Address address;
    private String[] books;
    private Properties info;
    private List<String> hobbys;
    private Map<String,String> card;
    private Set<String> games;
    //这个类的set和get方法需要自行补充
}
```

**applicationContext.xml**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="zhuzhi" class="org.example.pojo.Address"/>
    <bean id="student" class="org.example.pojo.Student">
        <!--简单类型注入 - value-->
        <property name="name" value="wangzhe"/>
        <!--bean注入 - ref-->
        <property name="address" ref="zhuzhi"/>
        <!--数组注入 - ref-->
        <property name="books">
            <array>
                <value>水浒传</value>
                <value>红楼梦</value>
                <value>西游记</value>
            </array>
        </property>
        <!--List注入-->
        <property name="hobbys">
            <list>
                <value>听歌</value>
                <value>敲代码</value>
            </list>
        </property>
        <!--map集合注入-->
        <property name="card">
            <map>
                <entry key="身份证" value="123456"/>
                <entry key="校园卡" value="19008079"/>
            </map>
        </property>
        <!--Set集合注入-->
        <property name="games">
            <set>
                <value>听歌</value>
                <value>敲代码</value>
            </set>
        </property>
        <!--Properties注入-->
        <property name="info">
            <props>
                <prop key="学号">19008079</prop>
                <prop key="姓名">小明</prop>
                <prop key="username">xiaoming</prop>
                <prop key="password">123456</prop>
            </props>
        </property>
    </bean>
</beans>
```

## Bean的作用域

![image-20210614200055325](https://i.loli.net/2021/06/14/8tdPvsxFhHNkqYz.png)

## Bean的自动装配

+ **自动装配是Spring满足bean依赖一种方式,Spring会在上下文中自动寻找，并自动给bean装配属性!**

+ 在Spring中有三种装配的方式
  1.在xml中显示的配置

  2.在java中显示配置
  3.隐式的自动装配bean【重要】，下面介绍这一种

```xml
<! --
    byName:会自动在容器上下文中查找，和自己对象set方法后面的值对应的 beanid
	byType:会自动在容器上下文中查找，和自己对象属性类型相同的bean 

-->
    <bean id="people" class="com.kuang.pojo.People" autowire="byName ">
        <property name="name" value="wangzhe"/>
    </bean>

```

小结： 

+ byname的时候，需要保证所有bean的id唯一，并且这个bean需要和自动注入的属性的set方法的值一致
+  bytype的时候，需要保证所有bean的class唯一，并且这个bean需要和自动注入的属性的类型一致



## Spring注解实现自动装配

jdk1.5支持的注解，Spring2.5就支持注解了

Spring4之后，使用注解开发，必须保证aop的包导入了

**Spring官网有一段话**

`The introduction of annotation-based configuration raised the question of 	whether this approach is "better"than XML.`

**使用步骤**:

1.使用注解需要在`applicationContext.xml`导入context命名空间，增加对注解的支持

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:context="http://www.springframework.org/schema/context"
 
  xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/context
        https://www.springframework.org/schema/context/spring-context.xsd">
    <context:annotation-config/>
</beans>
```

**注意必须在`applicationContext.xml`中打开注解的支持**

```
<context:annotation-config/>
```

3、@Autowired

+ 直接在类中属性上使用即可
+ 默认通过byName方式实现

+ 由于注解是利用反射来实现的，所以设置注解以后甚至不需要set方法
+ 如果自动装配环境比较复杂，自动装配无法通过一个注解`@autowired`完成的时候，我们可以使用`@quailfiler(value="XXX")`去指定一个唯一的bean对象注入

## 使用注解开发

### 常用注解

- @Autowired : 自动装配 by 类型 then 名字 属于Spring

- @Nullable : 字段标记了这个注解，说明这个字段可以为null

- @Resource : 自动装配 by 名字 then 类型 属于JDK

- @Component 组件 ： 相当于自动注册bean 

  @Component("userdao")  引号中的相当于id

  有几个衍生的注解,我们在web开发中，会按照mvc三层架构分层，这几个注解功能都是一样的

  + dao @Repository
  + service @Service
  + controller  @Controller

- @Scope 作用域

![image-20210616011517588](https://i.loli.net/2021/06/16/phM9PwQJYgXZLAi.png)

### xml与注解的比较

**从维护看**

xml 更加万能，适用于任何场合!维护简单方便。

注解不是自己类使用不了，维护相对复杂

**xml与注解最佳实践:**

xml 用来管理bean;
注解只负责完成属性的注入;

**我们在使用的过程中，只需要注意一个问题:必须让注解生效，就需要开启注解的支持**

```xml
	<!--开启注解支持-->
    <context:annotation-config/>
    <!--指定要扫描的包，这个包下的注解就会生效-->
    <context:component-scan base-package="org.example"/>
```

## AOP

### AOP是什么

**AOP (Aspect Oriented Programming)意为:面向切面编程，通过预编译方式和运行期动态代理实现程序功能的统一维护的一种技术。AOP是OOP的延续，是软件开发中的一个热点，也是Spring框架中的一个重要内容，是函数式编程的一种衍生范型。利用AOP可以对业务逻辑的各个部分进行隔离，从而使得业务逻辑各部分之间的耦合度降低，提高程序的可重用性，同时提高了开发的效率。**

### AOP的思想

动态代理的思想

横向编程的思想，不影响当前业务的情况下，实现横向业务的增强

### 实现AOP的几种方式

不管哪种方式都需要提前在`applicationContext.xml`配置文件中导入aop的约束

![image-20210616153528428](https://i.loli.net/2021/06/16/wbUeQC2m8Rg1vDK.png)

**方式一：使用原生SpringAPI接口**

```xml
	
    <!--配置aop-->
    <aop:config>
        <!--切入点 expression 表达式 execution（要执行的位置 * * * * *）-->
        <aop:pointcut id="pointct" expression="execution(* org.example.service.UserServiceImpl.*(..))"/>
        <!--执行环绕增加-->
        <aop:advisor advice-ref="log" pointcut-ref="pointct"/>
        <aop:advisor advice-ref="afterlog" pointcut-ref="pointct"/>
    </aop:config>
```



**方式二：自定义类**

```xml
    <bean id="diy" class="org.example.diyPoint.Point"/>
    <aop:config>
        <!--自定义切面 -->
        <aop:aspect ref="diy">
            <!--切入点 引入需要被切面编程的bean id  -->
            <aop:pointcut id="point" expression="execution(* org.example.service.UserServiceImpl.*(..))"/>
            <!--通知 将自定义切面插入-->
            <aop:before method="before" pointcut-ref="point"/>
            <aop:after method="after" pointcut-ref="point"/>
        </aop:aspect>
    </aop:config>
```



**方式三:使用注解实现aop**

```xml
<bean id="annotationPointCut" class="org.example.diyPoint.AnnotationPointCut"/>
    <!--开启注解支持-->
    <aop:aspectj-autoproxy/>
```

```java
//使用注解实现AOP
//标注这个类是一个切面，不用在配置文件中去写了
@Aspect
public class AnnotationPointCut {
    @Before("execution(* org.example.service.UserServiceImpl.*(..))")
    public void before(){
        System.out.println("我要被上下插入了");
    }
}
```

































