---
StartTime: 2021-9-21
EndTime: 2021-10-21
---

# SpringBoot学习笔记

## 回顾Spring

+ Spring是一个开源框架，2003 年兴起的一个轻量级的Java 开发框架，作者：Rod Johnson  

+ Spring是为了解决企业级应用开发的复杂性而创建的，简化开发

## SpringBoot简介

+ Spring Boot 基于 Spring 开发
+ Spirng Boot 本身并不提供 Spring 框架的核心特性以及扩展功能，只是用于快速、敏捷地开发新一代基于 Spring 框架的应用程序。也就是说，它并不是用来替代 Spring 的解决方案，而是和 Spring 框架紧密结合用于提升 Spring 开发者体验的工具。
+ Spring Boot 以**约定大于配置的核心思想**，默认帮我们进行了很多设置，多数 Spring Boot 应用只需要很少的 Spring 配置。
+ 同时它集成了大量常用的第三方库配置（例如 Redis、MongoDB、Jpa、RabbitMQ、Quartz 等等），Spring Boot 应用中这些第三方库几乎可以零配置的开箱即用
+ 简单来说就是SpringBoot其实不是什么新的框架，它默认配置了很多框架的使用方式，就像Maven整合了所有的jar包，spring boot整合了所有的框架
+ [官方api文档](https://docs.spring.io/spring-boot/)

## 微服务

`James Lewis` 和 `Martin Fowler` 提出微服务完整概念

https://martinfowler.com/microservices/

> In short, the **microservice architectural style** is an approach to developing a single application as a **suite of small services**, each **running in its own process** and communicating with **lightweight** mechanisms, often an **HTTP** resource API. These services are **built around business capabilities** and **independently deployable** by fully **automated deployment** machinery. There is a **bare minimum of centralized management** of these services, which may be **written in different programming languages** and use different data storage technologies.-- [James Lewis and Martin Fowler (2014)](https://martinfowler.com/articles/microservices.html)

- 微服务是一种架构风格
- 一个应用拆分为一组小型服务

- 每个服务运行在自己的进程内，也就是可独立部署和升级
- 服务之间使用轻量级HTTP交互

- 服务围绕业务功能拆分
- 可以由全自动部署机制独立部署

- 去中心化，服务自治。服务可以使用不同的语言、不同的存储技术

## 第一个SpringBoot程序

### 项目创建

+ 使用 IDEA 直接创建项目--`Spring Initializr`
  + New Project
  + 选择spring initalizr，可以看到默认就是去官网的快速构建工具那里实现
  + 填写项目信息
  + 选择需要初始化的组件（初学勾选 Web 即可）
    + 这一步相当于自动在pom.xml中加入依赖，项目创建成功后添加也可以
  + 填写项目路径

### 项目结构

> 目录结构

 

![image-20210921180503563](https://i.loli.net/2021/09/21/wnVG2WOMslq1hif.png)

> SpringBoot 多模块项目

**Multi-Module项目**

包含嵌套Maven项目的Spring Boot项目称为 **多模块项目**。在多模块项目中，父项目充当基础Maven配置的容器。

换句话说， **多模块项目**是从管理一组子模块的父pom构建的。或 **多模块项目**由父POM引用一个或多个子模块来定义。

父maven项目必须包含 **pom** 的包装类型使该项目成为聚合器。父项目的 **pom.xml** 文件包含子项目继承的所有 **模块，公共依赖项**和 **属性**的列表。父pom位于项目的根目录中。子模块是实际的Spring Boot项目，它们从父项目继承maven属性。

当我们运行多模块项目时，所有模块都一起部署在嵌入式Tomcat Server中。我们也可以部署单个模块。



## 底层原理

### 自动装配

在阅读源码时，我们可以在和其他启动器的Starter中经常看到这个文件`spring.factories`，它和springboot的自动装配息息相关

> spring-boot-autoconfigure-2.5.5.jar包中的`spring.factories`部分截图

![image-20211012150908400](D:\桌面\P_picture_cahe\image-20211012150908400.png)

阅读这写`spring.factories`文件，发现里面写了自动配置（AutoConfiguration）相关的类名,**明明自动配置的类已经打上了@Configuration的注解，为什么还要写spring.factories文件呢？**

那么我们就从启动类中的`@SpringBootApplication`注解开始说起

```java
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(
    excludeFilters = {@Filter(
    type = FilterType.CUSTOM,
    classes = {TypeExcludeFilter.class}
), @Filter(
    type = FilterType.CUSTOM,
    classes = {AutoConfigurationExcludeFilter.class}
)}
)
public @interface SpringBootApplication {
    ...
}
```

**比较重要的几个注解**

`@SpringBootConfiguration`作用是标注spring的自动配置类

​	@Configuration : 需要Spring自动配置的类就加这个注解

​	@Component ： 说明这是一个Spring的组件

`@ComponentScan`作用是扫描当前类同级的包

> 到现在或许会有个疑问，在spring-boot项目中pom文件里面添加的依赖中的bean（spring-boot项目外的bean）是如何注册到spring-boot项目的spring容器中的呢？

`@EnableAutoConfiguration`这个就是自动配置bean的核心注解，查看其源码

```java
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@AutoConfigurationPackage
@Import({AutoConfigurationImportSelector.class})
public @interface EnableAutoConfiguration {
    String ENABLED_OVERRIDE_PROPERTY = "spring.boot.enableautoconfiguration";
    Class<?>[] exclude() default {};
    String[] excludeName() default {};
}

```

**分析**	

​	@AutoConfigurationPackage : 自动配置包

​	**@Import(AutoConfigurationImportSelector.class) ：自动配置导入选择**

> `AutoConfigurationImportSelector.class`这个类做了什么呢
>
> 阅读源码可以看出关键的部分为
>
> ![image-20211012154641135](https://i.loli.net/2021/10/12/VNfPBxld8gQWTGI.png)
>
> 进一步查看`getCandidateConfigurations`方法的源码
>
> ```java
>  protected List<String> getCandidateConfigurations(AnnotationMetadata metadata, AnnotationAttributes attributes) {
>         List<String> configurations = SpringFactoriesLoader.loadFactoryNames(this.getSpringFactoriesLoaderFactoryClass(), this.getBeanClassLoader());
>         Assert.notEmpty(configurations, "No auto configuration classes found in META-INF/spring.factories. If you are using a custom packaging, make sure that file is correct.");
>         return configurations;
>     }
> ```
>
> 这个方法的具体实现为，读取spring-boot项目的classpath下META-INF/spring.factories的内容，这个文件常常以K/V的形式存储数据，
>
> 例如：类路径下的META-INF/spring.factories中获取EnableAutoConfiguration指定的值

**结论**

不难得出spring.factories文件是帮助spring-boot项目包以外的bean（即在pom文件中添加依赖中的bean）注册到spring-boot项目的spring容器的结论。由于@ComponentScan注解只能扫描spring-boot项目包内的bean并注册到spring容器中，因此需要@EnableAutoConfiguration注解来注册项目包外的bean。而spring.factories文件，则是用来记录项目包外需要注册的bean类名。


![image-20210921190848023](https://i.loli.net/2021/09/21/IBwTOSyViXnQN54.png)

> 自动配置真正实现是从classpath中搜寻所有的META-INF/spring.factories配置文件 ，并将其中对应的 org.springframework.boot.autoconfigure. 包下的配置项，通过反射实例化为对应标注了 @Configuration的JavaConfig形式的IOC容器配置类 ， 然后将这些都汇总成为一个实例并加载到IOC容器中





来关注一个细节问题，自动配置类必须在一定的条件下才能生效，因此，应该知道一个spring底层注解：@Conditional

作用：必须是@Conditional指定的条件成立，才给容器中添加组件，配置里面的所有内容才生效
![image-20210923010453918](https://i.loli.net/2021/09/23/ZwfgQS9Hp7u1KBP.png)

### 和yaml配置文件如何绑定在一起？

![image-20210923010006414](D:\桌面\P_picture_cahe\image-20210923010006414.png)

那么多的自动配置类，必须在一定的条件下才能生效；也就是说，我们加载了这么多的配置类，但不是所有的都生效了，怎么知道哪些自动配置类生效？

**我们可以通过启用 debug=true属性；来让控制台打印自动配置报告，这样我们就可以很方便的知道哪些自动配置类生效；**

```yaml
#开启springboot的调试类
debug=true
```

### 为什么引入依赖不需要指定版本

**Springboot自动版本仲裁机制**

在pom.xml中找到父工程

+ spring-boot-dependencies: 核心依赖在父工程中

+ 我们在引入一些依赖的时候不需要指定版本，就是因为有这些版本仓库

  ![image-20210921181432494](https://i.loli.net/2021/09/21/7NXSoabQOqYvWkK.png)

### 启动器

```xml
<dependency>
	<groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

+ 我们需要什么功能，只需要找到对应的启动器就可以了
+ 比如spring-boot-starter-web，他就会帮我们自动导入web环境所有的依赖!

### 主程序

```java
//标注这个类是一个Springboot的应用,添加这个注解的类就是SpingBoot的启动类
@SpringBootApplication
//扫描Mapper文件，这样就不用每个Mapper类都需要加Maooer注解了
@MapperScan(basePackages= "com.loki.mapper")
public class Springboot01HelloApplication {
    public static void main(String[] args) {
        //启动Springboot应用
        SpringApplication.run(Springboot01HelloApplication.class, args);
    }
}
```

### **SpringApplication.run分析**

分析该方法主要分两部分，一部分是SpringApplication的实例化，二是run方法的执行；

`SpringApplication`

**这个类主要做了以下四件事情：**

1、推断应用的类型是普通的项目还是Web项目

2、查找并加载所有可用初始化器 ， 设置到initializers属性中

3、找出所有的应用程序监听器，设置到listeners属性中

4、推断并设置main方法的定义类，找到运行的主类

`run方法`

+ 关于Springboot谈谈自己的理解

  首先将自动装配的过程分析一下，关于run方法并不需要了解太多，除非你想重构一个springboot

**结论：**

1. SpringBoot在启动的时候从类路径下的META-INF/spring.factories中获取EnableAutoConfiguration指定的值
2. 将这些值作为自动配置类导入容器 ， 自动配置类就生效 ， 帮我们进行自动配置工作；
3. 整个J2EE的整体解决方案和自动配置都在springboot-autoconfigure的jar包中；
4. 它会给容器中导入非常多的自动配置类 （xxxAutoConfiguration）, 就是给容器中导入这个场景需要的所有组件 ， 并配置好这些组件 ；
5. 有了自动配置类,免去了我们手动编写配置注入功能组件等的工作；

## 配置文件

> SpringBoot使用一个全局的配置文件 ， 配置文件名称是固定的 application.yaml

### yaml语法

YAML仍是一种标记语言

+ **基础语法**

  说明：语法要求严格！

  1、空格不能省略

  2、以缩进来控制层级关系，**只要是左边对齐的一列数据都是同一个层级的**

  3、属性和值的大小写都是十分敏感的。

```yaml
#普通的 key-value
name: oliverloki
#对象
student:
	name: loki
	age: 20
#行内写法
student: {name:loki,age:3}
#数组
pet:
	- cat
	- dog
	- pig
pet: [cat,dog,pig]
```

+ yaml可以注入到配置类中

  `@ConfigurationProperties`

  作用：将配置文件中配置的每一个属性的值，映射到这个组件中；告诉SpringBoot将本类中的所有属性和配置文件中相关的配置进行绑定参数 `prefix = “person”` , 将配置文件中的person下面的所有属性一一对应

  ```java
  @Component //注册bean
  
  @ConfigurationProperties(prefix = "person")
  
  public class Person {  
  	...
  }
  ```

### Springboot配置文件加载的位置

**请求进来，先去找Controller看能不能处理。不能处理的所有请求又都交给静态资源处理器。静态资源也找不到则响应404页面**

springboot 启动会扫描以下位置的application.properties或者application.yml文件作为Spring boot的默认配置文件：

优先级1：项目路径下的config文件夹配置文件

优先级2：项目路径下配置文件

优先级3：资源路径下的config文件夹配置文件

优先级4：资源路径下配置文件

## 多环境切换

我们在主配置文件编写的时候，文件名可以是 `application-{profile}.properties/yml` , 用来指定多个环境版本

**例如：**

**application-test.properties 代表测试环境配置**

**application-dev.properties 代表开发环境配置**

但是Springboot并不会直接启动这些配置文件，它**默认使用application.properties主配置文件**；

我们需要通过一个配置来选择需要激活的环境：

```
#比如在配置文件中指定使用dev环境，我们可以通过设置不同的端口号进行测试；
#我们启动SpringBoot，就可以看到已经切换到dev下的配置了；
spring.profiles.active=dev
```

> 多环境的情况下，yaml格式的配置文档比起properties有很多优势

```yaml
server:
  port: 8081
#选择要激活那个环境块
spring:
  profiles:
    active: prod
    
---
server:
  port: 8083
spring:
  profiles: dev #配置环境的名称
---

server:
  port: 8084
spring:
  profiles: prod  #配置环境的名称
```

## SpringbootWeb开发

+ 静态资源映射规则
+ 欢迎页的映射
+ 从jsp到模板引擎Thymeleaf
+ 拓展与装配SpringMVC
+ 增删改查
+ 拦截器
+ 国际化（中英文切换）

### 静态资源映射规则

> SpringBoot中，SpringMVC的web配置都在 `WebMvcAutoConfiguration` 这个配置类里面

该类中有一个`addResourceHandlers`方法，该方法决定了静态资源映射的两种方式

+ webjars

  + Webjars本质就是以jar包的方式引入我们的静态资源，我们以前要导入一个静态资源文件，直接导入即可

  + 使用SpringBoot需要使用Webjars，我们可以去搜索一下：网站：https://www.webjars.org 

    ![image-20210923153723919](D:\桌面\P_picture_cahe\image-20210923153723919.png)

  + 所有的 /webjars/** ， 都需要去 classpath:/META-INF/resources/webjars/ 找对应的资源

+ **导入自己的静态资源，该遵循什么样的规则呢**

  + `CLASSPATH_RESOURCE_LOCATIONS`,全局搜索这个变量，它代表和静态资源有关的参数；这里面指向了它会去寻找资源的文件夹，即上面数组的内容

    **所以得出结论，以下四个目录存放的静态资源可以被我们识别**

    ```java
    private static final String[] CLASSPATH_RESOURCE_LOCATIONS = { 
        "classpath:/META-INF/resources/",
    	"classpath:/resources/",
        "classpath:/static/", 
        "classpath:/public/" };
    ```



> 2.1.7版本的addResourceHandlers方法，每个版本不尽相同

```java
@Override
public void addResourceHandlers(ResourceHandlerRegistry registry) {
    if (!this.resourceProperties.isAddMappings()) {
        // 已禁用默认资源处理
        logger.debug("Default resource handling disabled");
        return;
    }
    // 缓存控制
    Duration cachePeriod = this.resourceProperties.getCache().getPeriod();
    CacheControl cacheControl = this.resourceProperties.getCache().getCachecontrol().toHttpCacheControl();
    // webjars 配置
    if (!registry.hasMappingForPattern("/webjars/**")) {
        customizeResourceHandlerRegistration(registry.addResourceHandler("/webjars/**")
                                             .addResourceLocations("classpath:/META-INF/resources/webjars/")
                                             .setCachePeriod(getSeconds(cachePeriod)).setCacheControl(cacheControl));
    }
    // 静态资源配置
    String staticPathPattern = this.mvcProperties.getStaticPathPattern();
    if (!registry.hasMappingForPattern(staticPathPattern)) {
        customizeResourceHandlerRegistration(registry.addResourceHandler(staticPathPattern)
                                             .addResourceLocations(getResourceLocations(this.resourceProperties.getStaticLocations()))
                                             .setCachePeriod(getSeconds(cachePeriod)).setCacheControl(cacheControl));
    }
}
```

### 欢迎页的映射

在`WebMvcAutoConfiguration`中，看到获取欢迎页的源码

```java
@Bean
public WelcomePageHandlerMapping welcomePageHandlerMapping(ApplicationContext applicationContext,
                                                           FormattingConversionService mvcConversionService,
                                                           ResourceUrlProvider mvcResourceUrlProvider) {
    WelcomePageHandlerMapping welcomePageHandlerMapping = new WelcomePageHandlerMapping(
        new TemplateAvailabilityProviders(applicationContext), applicationContext, getWelcomePage(), // getWelcomePage 获得欢迎页
        this.mvcProperties.getStaticPathPattern());
    welcomePageHandlerMapping.setInterceptors(getInterceptors(mvcConversionService, mvcResourceUrlProvider));
    return welcomePageHandlerMapping;
}
```

结论：欢迎页-----静态资源文件夹下的所有 index.html 页面；被 /** 映射

### 模板引擎Thymeleaf

**使用方法**

在html中导入Thymeleaf命名空间，在html编写符合规则的代码即可

```
<html lang="en" xmlns:th="http://www.thymeleaf.org">
```

**关闭Thymeleaf可以解决静态资源更新不及时的问题**

```
#关闭模板引擎缓存
spring:
  thymeleaf:
    cache: false
```

[Thymeleaf语法]()

### 拓展与装配SpringMVC

[MVC自动配置官方文档（Spring MVC Auto-configuration）](https://docs.spring.io/spring-boot/docs/2.2.5.RELEASE/reference/htmlsingle/#boot-features-spring-mvc-auto-configuration)

> SpringBoot在自动配置很多组件的时候，先看容器中有没有用户自己配置的（如果用户自己配置@bean），如果有就用用户配置的，如果没有就用自动配置的

如果你想保留Spring Boot MVC特性，并且想添加额外的MVC配置(拦截器、格式化器、视图控制器和其他特性)，可以**定义一个WebMvcConfigurer类型带@Configuration的类**，但不需要@EnableWebMvc。

```java
//应为类型要求为WebMvcConfigurer，所以我们实现其接口
//可以使用自定义类扩展MVC的功能
@Configuration
/*
@EnableWebMvc 
全面接管视图解析器-即：SpringBoot对SpringMVC的自动配置不需要了，所有都是我们自己去配置，不推荐使用全面接管SpringMVC
*/
public class MyMvcConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // 浏览器发送/test ， 就会跳转到test页面；
        registry.addViewController("/test").setViewName("test");
    }
}
```

**对@EnableWebMvc 注解的底层解析**

容器中不存在这个类时，自动配置类才会生效

![image-20210924163701519](https://i.loli.net/2021/09/24/qrwAKkisLovn8fO.png)

@EnableWebMvc生成WebMvcConfigurationSupport类，进而使得MVC完全失效

![image-20210924163844290](https://i.loli.net/2021/09/24/nu49osyfV3HltGY.png)

### 页面国际化

1. 在resources资源文件下新建一个i18n目录，存放国际化配置文件
2. 新建`login.properties`、`login_zh_CN.properties`==>IDEA自动识别了我们要做国际化操作

![image-20210927214840111](https://i.loli.net/2021/09/27/Avl28Q9HqKbYB1G.png)

**idea支持配置文件可视化编程**

![image-20210927215234873](D:\桌面\P_picture_cahe\image-20210927215234873.png)

3.国际化文件夹要在这里配置

```
#关闭模板引擎缓存
spring:
  #国际化配置
  messages:
    basename: i18n.login,i18n.leftbar
```

**实现**

通过继承`LocaleResolver`并重写``resolveLocale`方法实现**根据按钮自动切换中文英文**

```java
import org.springframework.web.servlet.LocaleResolver;
import org.thymeleaf.util.StringUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;
public class MyLocaleResolver implements LocaleResolver {
    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        //获取请求中的语言参数 获取 l
        String language = request.getParameter("l");
        Locale locale = Locale.getDefault();//如果没有参数就使用默认的

        if (!StringUtils.isEmpty(language)) {//判断locale是否为空
            //zh_CN  国家_地区
            /*
             public Locale(String language, String country) {
                     this(language, country, "");
                }
             */
            String[] split = language.split("_");
            locale = new Locale(split[0], split[1]);
        }
        return locale;
    }
    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
    }
}

```

配置组件，在自定义MvcConofig下添加bean

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyMvcConfig implements WebMvcConfigurer {
    //注册自定义国际化组件
    @Bean
    public LocaleResolver localeResolver(){
        return new MyLocaleResolver();
    }
}

```

 修改前端页面的跳转链接

```html
Thymeleaf传参使用括号
<a class="btn btn-sm" th:href="@{/index.html(l='zh_CN')}">中文</a>
<a class="btn btn-sm" th:href="@{/index.html(l='en_US')}">English</a>
```

### 登录拦截器

+ 拦截器要继承`HandlerInterceptor`接口，并重写`preHandle`方法
+ 写好之后的拦截器要在自定义视图解析器中注入到IoC容器

```java
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//登录拦截器，通过判断session来实现
public class LoginHandlerInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //通过判断用户session是否存在来确定是否登录
        if (request.getSession().getAttribute("loginuser")==null){//session为空
            request.setAttribute("msg","请登录后访问该界面");
            request.getRequestDispatcher("/index.html").forward(request,response);
            return false;//不放行
        }else
        return true;
    }
}
```

```java
@Configuration
public class MyMvcConfig implements WebMvcConfigurer {
    //注册拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginHandlerInterceptor()).addPathPatterns("/**")
                .excludePathPatterns("/index.html","/","/user/login","/js/**","/css/**","/img/**");
    }
}
```

### 设置网站Favicon

Spring Boot不同版本对Favicon的支持

在早些版本中Spring Boot对Favicon进行了默认支持，并且通过如下配置进行关闭操作：

```yaml
spring.mvc.favicon.enabled=false ## 关闭
```

在Spring Boot2.2.x中，将默认的favicon.ico移除，同时也不再提供上述application.properties中的属性配置

因此，可以使用自定义Favicon

**自定义Favicon** 

正常情况下，直接将命名为favicon.ico的网站图标放在resources或static目录即可显示，但如果使用的版本无法显示。

首先排除浏览器缓存的问题。在撰写本文时多次遇到浏览器缓存导致无法展示的情况。一般操作步骤，清除浏览器缓存，重启浏览器，即可展示。

同时，如果需要在页面中通过代码进行引入。下面展示使用Thymeleaf时的引入方式：

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8"/>
    <title>Hello Favicon</title>
    <link rel="icon" th:href="@{/favicon.ico}" type="image/x-icon"/>
</head>
<body>
	<h1>Hello Favicon!</h1>
</body>
</html>
```

## Springboot持久层

### 整合Druid

**Druid简介**

+ Java程序很大一部分要操作数据库，为了提高性能操作数据库的时候，又不得不使用数据库连接池。

+ Druid 是阿里巴巴开源平台上一个数据库连接池实现，结合了 C3P0、DBCP 等 DB 池的优点，同时加入了日志监控。

+ Druid 可以很好的监控 DB 池连接和 SQL 的执行情况，天生就是针对监控而生的 DB 连接池。

+ Spring Boot 2.0 以上默认使用 Hikari 数据源，可以说 `Hikari` 与 `Driud` 都是当前 Java Web 上最优秀的数据源，我们来重点介绍 Spring Boot 如何集成 Druid 数据源，如何实现数据库监控。

+ Github地址：https://github.com/alibaba/druid/

**Springboot添加Druid依赖**

druid别名德鲁伊

```xmL
<!-- https://mvnrepository.com/artifact/com.alibaba/druid -->
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>druid</artifactId>
    <version>1.2.6</version>
</dependency>
```

**Druid常见问题（github官方版）**

https://github.com/alibaba/druid/wiki/%E5%B8%B8%E8%A7%81%E9%97%AE%E9%A2%98

**配置Druid**

数据源连接初始化大小、最大连接数、等待时间、最小连接数 等设置项；可以查看源码

```yaml

spring:
  datasource:
    username: root
    password: 123456
    #?serverTimezone=UTC解决时区的报错
    url: jdbc:mysql://localhost:3306/springboot?serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8
    driver-class-name: com.mysql.cj.jdbc.Driver
    type: com.alibaba.druid.pool.DruidDataSource

    #Spring Boot 默认是不注入这些属性值的，需要自己绑定
    #druid 数据源专有配置
    initialSize: 5
    minIdle: 5
    maxActive: 20
    maxWait: 60000
    timeBetweenEvictionRunsMillis: 60000
    minEvictableIdleTimeMillis: 300000
    validationQuery: SELECT 1 FROM DUAL
    testWhileIdle: true
    testOnBorrow: false
    testOnReturn: false
    poolPreparedStatements: true

    #配置监控统计拦截的filters，stat:监控统计、log4j：日志记录、wall：防御sql注入
    #如果允许时报错  java.lang.ClassNotFoundException: org.apache.log4j.Priority
    #则导入 log4j 依赖即可，Maven 地址：https://mvnrepository.com/artifact/log4j/log4j
    filters: stat,wall,log4j
    maxPoolPreparedStatementPerConnectionSize: 20
    useGlobalDataSourceStat: true
    connectionProperties: druid.stat.mergeSql=true;druid.stat.slowSqlMillis=500
```

这一步要导入Log4j的依赖

```xml
<!-- https://mvnrepository.com/artifact/log4j/log4j -->
<dependency>
    <groupId>log4j</groupId>
    <artifactId>log4j</artifactId>
    <version>1.2.17</version>
</dependency>
```

### 整合Mybatis plus



## 开发小技巧

### Lombok

引入lombok Maven依赖

```xml
		<!--lombok依赖-->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
```

```java
@Data //提供该类所有属性的getter/setter方法，还提供了equals、canEqual、hashCode、toString方法
@AllArgsConstructor //提供有参构造
@NoArgsConstructor //提供无参构造
@Slf4j //提供日志打印
public class Books {
    private int bookId;
    private String bookName;
    private int bookCounts;
    private String detail;

    public void testLog(){
        //lombok插件的@slf4j提供的功能
        log.info("进入到这个方法了");
    }

}

```

### Dev tools热部署

```xml
	<!--Devtools-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <version>2.5.2</version>
        </dependency>
```

导入后Ctrl+F9===>重新启动

这个重启做不到真正意义上的热部署，`JRebel`可以实现这个功能，但是是收费的



