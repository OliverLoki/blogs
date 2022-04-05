> Apache Shiro 是 [Java](https://www.w3cschool.cn/java/) 的一个安全框架。目前，使用 Apache Shiro 的人越来越多，因为它相当简单，对比 Spring Security，可能没有Spring Security 做的功能强大，但是在实际工作时可能并不需要那么复杂的东西，所以使用小而简单的 Shiro 就足够了。对于它俩到底哪个好，这个不必纠结，能更简单的解决项目问题就好了

> **Shiro可以做什么？**
> 其不仅可以用在JavaSE 环境，也可以用在JavaEE 环境。Shiro 可以帮助我们完成：认证、授权、加密、会话管理、与Web 集成、缓存等。

> **Apache Shiro 的目标**是Shiro开发团队所说“应用程序安全的四大基石”——**身份验证、授权、会话管理和密码学**
>
> - **身份验证**：有时也称为“登录”，这是证明用户就是他们所说的身份的行为。
> - **授权**：访问控制的过程，即确定“谁”可以访问“什么”。
> - **会话管理**：管理特定于用户的会话，即使是在非 Web 或 EJB 应用程序中。
> - **密码学**：使用密码算法确保数据安全，同时仍然易于使用。

@[TOC]

## Shiro的架构

Shiro 可以非常容易的开发出足够好的应用，其不仅可以用在 JavaSE 环境，也可以用在 JavaEE 环境。Shiro 可以帮助我们完成：认证、授权、加密、会话管理、与 Web 集成、缓存等。这不就是我们想要的嘛，而且 Shiro 的 API 也是非常简单；其基本功能点如下图所示

![image-20211010164107799.png](https://i.loli.net/2021/10/10/ImtT5yL2wAUcdBi.png)

**记住一点，Shiro 不会去维护用户、维护权限；这些需要我们自己去设计 / 提供；然后通过相应的接口注入给 Shiro 即可。**



接下来我们分别从外部和内部来看看 Shiro 的架构

首先，我们从外部来看 Shiro 吧，即从应用程序角度的来观察如何使用 Shiro 完成工作

> 外部架构

Shiro从外部来看--->记住这三个对象代表的含义

![image-20211011122912457](https://i.loli.net/2021/10/11/Au9WmGyFjNOUvCn.png)

应用代码直接交互的对象是Subject，也就是说Shiro的对外API核心就是Subject

**其每个 API 的含义：**

`Subject`：主体，<u>代表了当前“用户”</u>，这个用户不一定是一个具体的人，与当前应用交互的任何东西都是Subject，如网络爬虫，机器人等；即一个抽象概念；所有Subject都绑定到SecurityManager，**与Subject的所有交互都会委托给SecurityManager**；可以把Subject认为是一个门面；SecurityManager才是实际的执行者；

`SecurityManager`：安全管理器；即所有与安全有关的操作都会与SecurityManager交互；且<u>**它管理着所有Subject**</u>；相当于 SpringMVC 中的 DispatcherServlet 或者 Struts2 中的 FilterDispatcher；是 Shiro 的心脏；所有具体的交互都通过 SecurityManager 进行控制；它管理着所有 Subject、且负责进行认证和授权、及会话、缓存的管理。

`Realm`：域，Shiro从从Realm<u>**获取安全数据**</u>（如用户、角色、权限），就是说SecurityManager要验证用户身份，那么它需要从Realm获取相应的用户进行比较以确定用户身份是否合法；也需要从Realm得到用户相应的角色/权限进行验证用户是否能进行操作；可以把Realm看成DataSource，即安全数据源

> 内部架构

![image-20220318230428041](https://s2.loli.net/2022/03/18/1AjqfJNbt2dEgch.png)

> 也就是说对于我们而言，最简单的一个 Shiro 应用

1. 应用代码通过 Subject 来进行认证和授权，而 Subject 又委托给 SecurityManager；
2. 我们需要给 Shiro 的 SecurityManager 注入 Realm，从而让 SecurityManager 能得到合法的用户及其权限进行判断。

**从以上也可以看出，Shiro 不提供维护用户 / 权限，而是通过 Realm 让开发人员自己注入**



接下来我希望可以通过一个官方给出的例子，源自[《10 Minute Tutorial on Apache Shiro》](https://shiro.apache.org/10-minute-tutorial.html)，借此来快速上手shiro

**但是你也可以根据需要直接看Springboot整合Shiro**

## 快速入门

### 环境搭建

> *Shiro可以在任何环境下运行，小到最简单的命令行应用，大到大型的企业应用以及集群应用。我们使用最简单的 main 方法的方式（参考自官网文档），让你对 Shiro的API有个感官的认识*
>
> ```java
> 	public static void main(String[] args) {
>         //shiro的入门代码
>     }
> ```

在开始之前，请保证已经熟悉以下几点

> + Maven的基本操作
> + Java基础与运行环境

Idea新建一个普通Maven工程，然后我们删除Src文件夹，在该project下新建一个module，起名为quickstart（这样做的目的是可以使用一个工程管理多个模块，在多模块项目中，父项目充当基础Maven配置的容器）

**目录结构如下图**

![image-20220318124131862](https://s2.loli.net/2022/03/18/FyuXWoIKUOrSzl8.png)





官方的`QuickStart`案例托管在`Github`，如下图

**:ice_cream: 并且samples目录下有很多shiro在其他环境下应用的案例**！(值得你去看看)

![image-20220318130052745](https://s2.loli.net/2022/03/18/f3LFRvQwT7i9G2x.png)

我们将通过`QuickStart.java`这个类来学习Shiro的基本操作，在此之前，我们需要准备这个类运行所依赖的环境和依赖包

然后开始准备环境

> 1. 在pom.xml导入Maven依赖，`log4j`与`shiro-core`
>
> 2. 在Resources目录下，补充上面两个中间件的配置文件
>
>    用户名 / 密码硬编码在 `ini` 配置文件，以后需要改成如数据库存储，且密码需要加密存储

**具体操作如下**

首先在quickstart模块的`pom.xml`文件中导入以下依赖

(关于版本号的问题，如果你离该博客更新时间较远，可以去[maven仓库](https://mvnrepository.com/artifact/org.slf4j/slf4j-api)自行选择版本)

```xml
<dependencies>
        <!-- https://mvnrepository.com/artifact/org.apache.shiro/shiro-core -->
        <dependency>
            <groupId>org.apache.shiro</groupId>
            <artifactId>shiro-core</artifactId>
            <version>1.8.0</version>
        </dependency>
        <!-- configure logging -->
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>jcl-over-slf4j</artifactId>
            <version>1.7.26</version>
        </dependency>
        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-slf4j-impl</artifactId>
            <version>2.17.2</version>
        </dependency>
    </dependencies>
```

分别编写`log4j`和`Shiro`的配置文件,添加这两个文件到resource目录下

`log4j.xml`

注：Log4j支持两种配置文件格式，一种是XML格式的文件，一种是properties属性文件

```xml
<Configuration name="ConfigTest" status="ERROR" monitorInterval="5">
    <Appenders>
        <Console name="Console" target="SYSTEM_OUT">
            <PatternLayout pattern="%d{HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n"/>
        </Console>
    </Appenders>
    <Loggers>
        <Logger name="org.springframework" level="warn" additivity="false">
            <AppenderRef ref="Console"/>
        </Logger>
        <Logger name="org.apache" level="warn" additivity="false">
            <AppenderRef ref="Console"/>
        </Logger>
        <Logger name="net.sf.ehcache" level="warn" additivity="false">
            <AppenderRef ref="Console"/>
        </Logger>
        <Logger name="org.apache.shiro.util.ThreadContext" level="warn" additivity="false">
            <AppenderRef ref="Console"/>
        </Logger>
        <Root level="info">
            <AppenderRef ref="Console"/>
        </Root>
    </Loggers>
</Configuration>
```

`shiro.ini`

```ini
[users]
# user 'root' with password 'secret' and the 'admin' role
root = secret, admin
# user 'guest' with the password 'guest' and the 'guest' role
guest = guest, guest
# user 'presidentskroob' with password '12345' ("That's the same combination on
# my luggage!!!" ;)), and role 'president'
presidentskroob = 12345, president
# user 'darkhelmet' with password 'ludicrousspeed' and roles 'darklord' and 'schwartz'
darkhelmet = ludicrousspeed, darklord, schwartz
# user 'lonestarr' with password 'vespa' and roles 'goodguy' and 'schwartz'
lonestarr = vespa, goodguy, schwartz

# -----------------------------------------------------------------------------
# Roles with assigned permissions
#
# Each line conforms to the format defined in the
# org.apache.shiro.realm.text.TextConfigurationRealm#setRoleDefinitions JavaDoc
# -----------------------------------------------------------------------------
[roles]
# 'admin' role has all permissions, indicated by the wildcard '*'
admin = *
# The 'schwartz' role can do anything (*) with any lightsaber:
schwartz = lightsaber:*
# The 'goodguy' role is allowed to 'drive' (action) the winnebago (type) with
# license plate 'eagle5' (instance specific id)
goodguy = winnebago:drive:eagle5
```

### QuickStrat.java

`《10 Minute Tutorial on Apache Shiro》`中指出，`Quickstart.java` 包含刚刚我们提到的所有内容(认证、授权等等)，通过这个简单的示例可以让你轻松的熟悉Shiro的API。

代码已经被我复制来并且进行了详细注释，你可以直接使用该代码，也可以去Github找到最新版本的quickstart

那么，让我们把Quickstart.java中的代码，一点一点剖析，这样便于理解它们的作用。 几乎所有的环境下，都可以通过这种方式获取当前用户

> 官方对这个类的解释：
>
> ```
> The easiest way to create a Shiro SecurityManager with configured
> realms, users, roles and permissions is to use the simple INI config.
> ```
>
> 译：通过在`Shiro.ini`配置`realms`,`users`,`rols`,`permission`等，实现一个最简单的方式来创建 Shiro 安全管理实例
>
> ```
> For this simple example quickstart, make the SecurityManager
> accessible as a JVM singleton.  Most applications wouldn't do this
> and instead rely on their container configuration or web.xml for
> webapps.  That is outside the scope of this simple quickstart, so
> we'll just do the bare minimum so you can continue to get a feel
> for things.
> ```
>
> 译：
>
> 对于这个简单的示例QuickStart，使得SecurityManager作为一个JVM单例模式来访问
> 大多数应用程序都不会这样做，而是依靠他们的容器配置或WebApps的web.xml
> 这些内容超出了这个简单的Quickstart，所以对于这个简单的例子，我们可以更容易理解

对于这个quickstart，可能会比较长，暂时看不懂没关系，我会将它拆分分析

QuickStart.java

```java
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple Quickstart application showing how to use Shiro's API.
 *
 * @since 0.9 RC2
 */
public class QuickStart {
    private static final transient Logger log = LoggerFactory.getLogger(QuickStart.class);

    public static void main(String[] args) {

    // 工厂模式，通过SecurityManager读取shiro.ini配置文件中的信息，生成一个工厂实例
        //已过时，大多数情况下我们不会使用以下代码
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
        SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);

    // Now that a simple Shiro environment is set up, let's see what you can do:
    // 现在一个简单的 shiro环境已经被建立，来看看你可以做什么
        // get the currently executing user:
        //获得当前用户对象
        Subject currentUser = SecurityUtils.getSubject();

        // Do some stuff with a Session (no need for a web or EJB container!!!)
        //通过当前用户拿到Session
        Session session = currentUser.getSession();
        //Session存值
        session.setAttribute("loki", "loki的全名是Oliverloki");
        //Session取值
        String value = (String) session.getAttribute("loki");

        if (value.equals("Oliverloki")) {
            log.info("检索到正确值! [" + value + "]");
        }
    // let's login the current user so we can check against roles and permissions:
    // 登录当前用户，以便我们检查角色和权限
        //判断是否被认证
        if (!currentUser.isAuthenticated()) {
            //获取一个Token:令牌
            UsernamePasswordToken token = new UsernamePasswordToken("lonestarr", "vespa");
            token.setRememberMe(true);
            try {//执行登录操作，然后根据返回值抛出异常
                currentUser.login(token);
            } catch (UnknownAccountException uae) {
                log.info("There is no user with username of " + token.getPrincipal());
            } catch (IncorrectCredentialsException ice) {
                log.info("Password for account " + token.getPrincipal() + " was incorrect!");
            } catch (LockedAccountException lae) {
                log.info("The account for username " + token.getPrincipal() + " is locked.  " +
                        "Please contact your administrator to unlock it.");
            }
            // ... catch more exceptions here (maybe custom ones specific to your application?
            catch (AuthenticationException ae) {
                //unexpected condition?  error?
            }
        }
    //currentUser的一些玩法
        //print their identifying principal (in this case, a username):
        log.info("User [" + currentUser.getPrincipal() + "] logged in successfully.");

        //test a role:
        if (currentUser.hasRole("schwartz")) {
            log.info("May the Schwartz be with you!");
        } else {
            log.info("Hello, mere mortal.");
        }
        //粗粒度测试权限
        //test a typed permission (not instance-level)
        if (currentUser.isPermitted("lightsaber:wield")) {
            log.info("You may use a lightsaber ring.  Use it wisely.");
        } else {
            log.info("Sorry, lightsaber rings are for schwartz masters only.");
        }
        //细粒度测试权限
        //a (very powerful) Instance Level permission:
        if (currentUser.isPermitted("winnebago:drive:eagle5")) {
            log.info("You are permitted to 'drive' the winnebago with license plate (id) 'eagle5'.  " +
                    "Here are the keys - have fun!");
        } else {
            log.info("Sorry, you aren't allowed to drive the 'eagle5' winnebago!");
        }

        //all done - log out!
        //结束，登出
        currentUser.logout();
        System.exit(0);
    }
}
```



了解了Shiro的基本操作以后，我们使用Springboot整合Shiro实现用户登陆认证、用户授权





> 在开始之前，请确保已经做到以下几点

+ Spring Boot 基本语法，页面跳转，自动注入
+ 以上 Shiro 的基本操作
+ 模拟 HTTP 请求工具，我使用的是 PostMan（Chrom应用商店有这个插件，可以自行了解安装）
+ 新建一个Maven项目，自行测试跑通一个查询数据的功能

项目结构如下图所示，但是本篇并没有描述Shiro之外的细节

![image-20220319132843552](https://s2.loli.net/2022/03/19/Jo7BvauLO45ehQY.png)



该博客侧重于Springboot项目导入依赖包后，shiro怎样实现用户登录认证，用户授权

![image-20220328222347245](https://s2.loli.net/2022/03/28/BR3IJFL7ATV6kpK.png)

## Springboot整合Shiro

**先做个总结**

> Springboot整合Shiro就做两件事
>
> 1. 引入Shiro依赖
> 2. 配置Shiro环境
>
> `Realm.java`
>
> ```java
> public class CustomRealm {
>     //认证
>     //授权
> }
> ```
>
> `ShiroConfig.java`
>
> ```java
> @Configuration
> public class ShiroConfig {//做三件事
>     //1、创建ShiroFilter   
>     //2、创建安全管理器
>     //3、创建自定义Realm
> }
> ```

接下来我们一步步拆分详细解释这些步骤

### 两种方式引入Shiro依赖

在 Spring Boot 中整合 Shiro ，有两种不同的方案

注：**下面文章中的所有代码是导入的第一种依赖**



1、第一种就是原封不动的，将 SSM 整合 Shiro 的配置用 Java 重写一遍

```xml
<dependency>
    <groupId>org.apache.shiro</groupId>
    <artifactId>shiro-spring</artifactId>
    <version>1.8.0</version>
</dependency>
```

2、第二种就是使用 Shiro 官方提供的一个 Starter 来配置

> shiro-spring-boot-starter给我们自动配置了大多数组件，三大组件也注入了IOC容器中，可是security他给我注入的是SessionSecurityManager，并且已经将Realm待参进行构建，这一点很重要，因为我们必须定义自己的认证授权规则，所以必须创建继承 AuthorizingRealm的Realm类，那么为什么不适用@Compant注解将其注入到IOC容器中，而是@Bean将Rleam加入呢，一点在于必须将Rleam注入到SecurityManager中，所以shiro团队也因为进行了改进。所以我们使用是可以使用@Autowire自动注入SessionSecurityManager到ShiroConfig里，而不需要@Bean再往IOC里面注入，如果你要使用其他类型的SecurityManager，可以自己@Bean配置。而其他的如
> ShiroFilterFactoryBean 并没有进行配置，所以需要手动配置。

主要变化是在ShiroConfig类中，不再需要 ShiroFilterFactoryBean 实例了，替代它的是 ShiroFilterChainDefinition ，在这里定义 Shiro 的路径匹配规则即可

+ com.example.config.ShiroConfig

```java
	@Bean
    ShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition definition = new DefaultShiroFilterChainDefinition();
        definition.addPathDefinition("/**", "anon");
        definition.addPathDefinition("/index", "authc");
        return definition;
    }
```

```xml
<dependency>
    <groupId>org.apache.shiro</groupId>
    <artifactId>shiro-spring-boot-web-starter</artifactId>
    <version>1.4.0-RC2</version>
</dependency>
```

### Shiro登录原理

从Controller层开始，自顶向下看看Shiro如何处理登录

```java
	@RequestMapping("/login")
    public String login(String username, String password, Model model) {	
//通过 SecurityUtils 得到 Subject，其会自动绑定到当前线程；如果在 web 环境在请求结束时需要解除绑定；然后获取身份验证的 Token，如用户名 / 密码；
        Subject subject = SecurityUtils.getSubject();
        //封装用户登录数据--变成一个Token
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        //执行登录
        try {
            //调用 subject.login 方法进行登录，其会自动委托给 SecurityManager.login 方法进行登录；
            subject.login(token);
            return "index";//登录成功跳转
        } catch (AuthenticationException e) {//登录失败跳转
            return "login";
        } 
    }
```

> 对这个异常处理的解释：
>
> 如果身份验证失败请捕获 `AuthenticationException` 或其子类（可以捕获多个异常），常见的如： `DisabledAccountException`（禁用的帐号）、`LockedAccountException`（锁定的帐号）、`UnknownAccountException`（错误的帐号）、`ExcessiveAttemptsException`（登录失败次数过多）、`IncorrectCredentialsException` （错误的凭证）、`ExpiredCredentialsException`（过期的凭证）等，具体请查看其继承关系；对于页面的错误消息展示，最好使用如 “用户名 / 密码错误” 而不是 “用户名错误”/“密码错误”，防止一些恶意用户非法扫描帐号库
>

可以看到shiro处理登录只用了一行代码，非常的便捷，那它是怎么实现的呢，我们往下看



**Shiro进行身份验证的流程**

1. 首先调用 `Subject.login(token)` 进行登录，其会自动委托给 `Security Manager`，调用之前必须通过 `SecurityUtils.setSecurityManager()` 设置；
2. `SecurityManager` 负责真正的身份验证逻辑；它会委托给 `Authenticator` 进行身份验证；
3. `Authenticator` 才是真正的身份验证者，`Shiro API` 中核心的身份认证入口点，此处可以自定义插入自己的实现；
4. `Authenticator` 可能会委托给相应的 `AuthenticationStrategy` 进行多 `Realm` 身份验证，默认 `ModularRealmAuthenticator` 会调用 `AuthenticationStrategy` 进行多 `Realm` 身份验证；
5. `Authenticator `会把相应的 `token` 传入 `Realm`，从 `Realm` 获取身份验证信息，如果没有返回 / 抛出异常表示身份验证成功了。此处可以配置多个 `Realm`，将按照相应的顺序及策略进行访问。



是不是又看到了我们熟悉的三个对象，`Subject`,`Security Manger`,`Realm`,这些对象在Springboot又是怎么实例化的呢？这就涉及到Shiro的两个核心配置类，他们做了quickstart中 shiro.ini配置文件的工作

于是，在config目录下编写Shiro的两个核心配置类`ShiroConfig`和`Realm`，这两个类完成了Shiro的所有操作

### Shiro核心配置类

#### 自定义Realm类

> 继承`AuthorizingRealm`
>
> 实现它的两个方法，分别实现认证和授权操作

> 作用：
>
> 从数据库查询用户的角色和权限信息进行比对并保存到权限管理器（securityManager）

我们先分开来看，最后有完整代码，**先来看认证操作**

**实现步骤如下：**
1、构造usernamepasswordtoken（由于传入的数据是确定的，所以我们强制转换即可）
2、获取输入用户的用户名密码
3、根据用户名查询数据库
4、比较密码和数据库中是否一致（密码可使用加密操作）
5、如果成功，向shiro中存入安全数据
6、如果失败，抛出异常，返回null（Shiro会自动处理这个异常）

```java
	//认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("执行了认证==》doGetAuthenticationInfo");
        //1、构造usernamepasswordtoken
        UsernamePasswordToken userToken = (UsernamePasswordToken) authenticationToken;
        //判断数据库中有没有指定用户
        User user = userService.getOne(new QueryWrapper<User>().eq("username", userToken.getUsername()));

        if (user==null){//Shiro会自动处理
            return null;//UnknownAccountException
        }
        //如果不为空，向Shiro存入安全数据，Shiro自动会做密码认证
        //可以加密：md5/md5盐值加密
        //这一行可玩性很高，会另外写一篇博客
        //第一个参数安全数据，第二个参数密码，第三个参数自定义Realm名称
        return new SimpleAuthenticationInfo("",user.getPassword(),"");
    }
```

接下来看看Shiro的授权操作，先理解一下授权

> 授权，也叫访问控制，即在应用中控制谁能访问哪些资源（如访问页面/编辑数据/页面操作等）。
>
> 在授权中需了解的几个关键对象：主体（Subject）、资源（Resource）、权限（Permission）、角色（Role）
>
> Shiro 支持粗粒度权限（如用户模块的所有权限）和细粒度权限（操作某个用户的权限，即实例级别的），即粒度是以角色为单位进行访问控制的.但是若粒度较粗,如果进行修改可能造成多处代码修改

**授权操作**

获取到用户的授权数据(用户的权限数据)
主要目的：根据认证的数据获取到用户的权限信息
参数：PrincipalCollection 包含所有已认证的安全数据，AuthorizationInfo 授权数据

**实现步骤如下：**
1、获取安全数据
2、根据id或名称查询用户
3、查询用户的角色和权限信息
4、构造返回

```java
 	@Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("执行了授权==》doGetAuthorizationInfo");
        //1、获取安全数据
        String username = (String) principals.getPrimaryPrincipal();
        //2、根据id或名称查询用户
        //3、查询用户的角色和权限信息，目前使用编造数据进行操作
        List<String> roles = new ArrayList<>();
        roles.add("role1");
        roles.add("role2");
        List<String> perms = new ArrayList<>();
        perms.add("user:save");
        perms.add("user:update");
        //4、构造返回
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        //设置权限集合
        info.addStringPermissions(perms);
        //设置角色集合
        info.addRoles(roles);
        return info;
    }
```





完整的**Realm.java**

```java
package com.loki.config;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
public class UserRealm extends AuthorizingRealm {
    @Autowired
    UserService userService;
    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("执行了授权==》doGetAuthorizationInfo");
        //1、获取安全数据
        String username = (String) principals.getPrimaryPrincipal();
        //2、根据id或名称查询用户
        //3、查询用户的角色和权限信息，目前使用编造数据进行操作
        List<String> roles = new ArrayList<>();
        roles.add("role1");
        roles.add("role2");
        List<String> perms = new ArrayList<>();
        perms.add("user:save");
        perms.add("user:update");
        //4、构造返回
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        //设置权限集合
        info.addStringPermissions(perms);
        //设置角色集合
        info.addRoles(roles);
        return info;
    }
    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("执行了认证==》doGetAuthenticationInfo");
        //1、构造usernamepasswordtoken
        UsernamePasswordToken userToken = (UsernamePasswordToken) authenticationToken;
        //判断数据库中有没有指定用户
        User user = userService.getOne(new QueryWrapper<User>().eq("username", userToken.getUsername()));

        if (user==null){//Shiro会自动处理
            return null;//UnknownAccountException
        }
        //如果不为空，向Shiro存入安全数据，Shiro自动会做密码认证
        //可以加密：md5/md5盐值加密
        //这一行可玩性很高，会另外写一篇博客
        //第一个参数安全数据，第二个参数密码，第三个参数自定义Realm名称
        return new SimpleAuthenticationInfo("",user.getPassword(),"");
    }
}
```



#### ShiroConfig类

> 这个类主要做以下四点事

**1、创建Realm对象**

实例化自定义Realm对象，由Spring接管

```java
	//创建realm对象:第一步
    @Bean(name = "userRealm")//这样做我们自定义的UserRealm类就会被Spring托管
    public UserRealm userRealm() {
        return new UserRealm();
    }
```

**2、创建安全管理器**

注意SecurityManager导包，是shiro的SecurityManager。
配置原理：对SecurityManager来说他管理了所有的Realm，通过这些代码获取了Realm管理信息

```java
//DefaultWebSecurityManager：第二步
    @Bean(name = "securityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("userRealm") UserRealm userRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //关联UserRealm
        securityManager.setRealm(userRealm);
        return securityManager;
    }
```

**3、配置shiro过滤器,拦截所有url请求**

> 配置shiro过滤器工厂，可以实现权限相关的登录拦截功能
> 在web程序中，shiro进行权限控制是通过一组过滤器集合进行的操作。
> 过滤器配置需要有以下几步：
> 1、创建过滤器工厂
> 2、设置安全管理器
> 3、通用配置（跳转登录页面，为授权跳转的页面）
> 4、设置过滤器集合

```java
//ShiroFilterFactoryBean:第三步
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("securityManager") DefaultWebSecurityManager defaultWebSecurityManager) {
         //1、创建过滤器工厂
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        //2、设置安全管理器
        bean.setSecurityManager(defaultWebSecurityManager);
        //3、通用配置（跳转登录页面，为授权跳转的页面）
        //登陆失败处理方式，跳转的url
        bean.setLoginUrl("/toLogin");
        //设置未授权时处理方式
        bean.setUnauthorizedUrl("/toIndex");
        //4、设置过滤器集合
        /**
         * 设置所有过滤器，使用有顺序的map
         *  key->拦截url地址
         *  value-> 过滤器类型
         */
        LinkedHashMap<String, String> filterMap = new LinkedHashMap<>();         	   Map<String,String> filterMap = new LinkedHashMap<>();
        filterMap.put("/user/home","anon");//无需认证即可访问/user/home
        filterMap.put("/user/**","authc");//当前请求地址必须认证后访问
        filterMap.put("/admin","perms[user:add]");  
        //将自定义规则添加进shiro的内置过滤器
        bean.setFilterChainDefinitionMap(filterMap);
        return bean;
    }
```

> 常用的过滤器
>
> + anon: 无需认证(登录)即可访问
> + authc: 必须认证才可访问
> + user: 如果使用 rememberMe
> + perms: 该资源必须得到资源权限才能访问
> + role: 该资源必须得到角色权限才可访问  

**4、开启对shiro注解的支持**

```java
	@Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }
```

完整的**ShiroConfig.java**

```java
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.LinkedHashMap;

@Configuration
public class ShiroConfig {
    //ShiroFilterFactoryBean:第三步
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("securityManager") DefaultWebSecurityManager defaultWebSecurityManager) {
         //1、创建过滤器工厂，用来过滤所有请求
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        //2、设置安全管理器
        bean.setSecurityManager(defaultWebSecurityManager);
        //3、通用配置（跳转登录页面，为授权跳转的页面）
        //登陆失败处理方式，跳转的url
        bean.setLoginUrl("/toLogin");
        //设置未授权时处理方式
        bean.setUnauthorizedUrl("/toIndex");
        //4、设置过滤器集合
        /**
         * 设置所有过滤器，使用有顺序的map
         *  key->拦截url地址
         *  value-> 过滤器类型
         */
        LinkedHashMap<String, String> filterMap = new LinkedHashMap<>();         Map<String,String> filterMap = new LinkedHashMap<>();
        filterMap.put("/user/home","anon");//无需认证即可访问
        filterMap.put("/user/**","authc");//当前请求地址必须认证后访问
        filterMap.put("/admin","perms[user:add]");  
        //将自定义规则添加进shiro的内置过滤器
        bean.setFilterChainDefinitionMap(filterMap);
        return bean;
    }

    //DefaultWebSecurityManager：第二步
    @Bean(name = "securityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("userRealm") UserRealm userRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //关联UserRealm
        securityManager.setRealm(userRealm);
        return securityManager;
    }
	/*
	@Qualifier注解=>@Bean 注解里的 name 指定放到spring容器中的名字， 若不写， 默认为方法名
    */
    //创建realm对象:第一步
    @Bean(name = "userRealm")//这样做我们自定义的UserRealm类就会被Spring托管
    public UserRealm userRealm() {
        return new UserRealm();
    }
}

```

至此，我们已经学会了Springboot中搭建一个Shiro的运行环境

而shiro的缓存和会话信息，我们一般考虑使用redis来存储这些数据，所以，我们不仅仅需要学习整合shiro，同时也需要整合redis。

> [Springboot+Shiro+Redis整合]()



> 参考文章
>
> [使用shiro-starter在前后端分离的Springboot项目](https://springboot.io/t/topic/2374)