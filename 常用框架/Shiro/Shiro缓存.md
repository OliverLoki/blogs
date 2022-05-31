Shiro使用Redis实现分布式会话与信息缓存

[掘金](https://juejin.cn/post/6844903894502342669#heading-5)



# Redis实现缓存

由于Cache不属于安全框架的核心功能，所以shiro本身并没有完全实现Cache机制。Cache接口相当于底层的缓存框架的顶层接口，shiro的一切的缓存操作都与这个Cache顶层接口操作，而底层的实现可以是任何Cache实例（JAche、Ehcache、OSCache、Redis...)



> **缓存原理图**

![](https://s2.loli.net/2022/03/29/dX64YpPDu3Vaf8e.png)

> **Shiro缓存的初始状态**

Shiro内部提供了对认证信息和授权信息的缓存，但是shiro默认是关闭认证信息缓存，对于授权信息的缓存shiro默认开启的。一般情况下，使用shiro缓存时，只需要关注授权信息缓存，因为认证信息只是一次验证查询，而授权信息需要在每次认证都会执行（访问量大），且一般情况下授权的数据量大。

> **在授权信息缓存的方案包含以下三种：**

1）使用Ehcache（系统混合缓存方案）

2）使用本地内存缓存方案
3）自定义CacheManager（比如Redis用来作为缓存）

我们使用第三种实现，自定义CacheManager，使用Redis做为缓存实现

## CacheManager接口

> cacheManager维护了Cache实例的生命周期，它和Cache一样，只是shiro的缓存框架的顶层接口，具体底层实现可以是任意的



![image-20220330150746343](https://s2.loli.net/2022/03/30/QVF5b8MIvADTjkO.png)





**对于经常查询但不进行增删改操作的数据，我们为了减轻数据库的压力，可以这样操作**

## 整合详细步骤

### **第一步：引入依赖**

> `shiro-spring-boot-web-starter` 按照 spring boot 的设计理念，底层实现了大量的配置。按照官方的介绍，用户只需要添加两个必须的 Bean，就可以运行 shiro。一个是 `Realm`，另一个是 `ShiroFilterChainDefinition`。其中 Realm 可以添加多个，在启动时，会自动将他们添加进 SecurityManager

```java
<!--Shiro-->
<dependency>
    <groupId>org.apache.shiro</groupId>
    <artifactId>shiro-spring-boot-web-starter</artifactId>
    <version>1.4.0-RC2</version>
</dependency>	 	
    
<!--SpringData Redis启动器-->
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

### **第二步：application.yml中配置Redis和数据库**

```yaml
spring:
  datasource:
    username: 
    password: 
    url: 
  redis:
    port: 6379
    host: 
    database: 0
    password: 没有设置就不写
```

### **第三步：配置启动Redis服务**

[不会的话看这篇文章](https://blog.csdn.net/Night__breeze/article/details/123778708?spm=1001.2014.3001.5501)

或者查看`org.springframework.boot.autoconfigure.data.redis.RedisProperties` 源码，那里有全部的配置说明，这就是starter的好处，直接看源码远比你网上百度要好千倍万倍

### **第四步：Redis实现Shiro提供的CacheManager接口**

shiro要想使用cacheManager

就必须实现org.apache.shiro.cache.CacheManager类，才能与shiro集成。我这里直接继承了AbstractCacheManager，需要实现createCache方法。我的实现类如下：

```java
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
/**
 * @author oliverloki
 * @Description: 自定义Shiro缓存管理器
 * @date 2022年03月28日 21:54
 */

public class RedisCacheManager implements CacheManager {

    @Override
    public <K, V> Cache<K, V> getCache(String cacheName) throws CacheException {
        return new RedisCache<K, V>(cacheName);
    }
}

```

### 第五步：配置Shiro





com.loki.config.Shiro.config

```java
	@Bean
    ShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition filter = new DefaultShiroFilterChainDefinition();
        //配置系统受限资源
        //配置系统公共资源
        HashMap<String, String> filterMap = new HashMap<>();
        /**
         * + anon: 无需认证(登录)即可访问
         * + authc: 必须认证才可访问
         * + user: 如果使用 rememberMe
         * + perms: 该资源必须得到资源权限才能访问
         * + role: 该资源必须得到角色权限才可访问
         */
        filter.addPathDefinition("/admin/*","authc");
        //filter.addPathDefinition("/**", "authc");

        return filter;
    }
    //Create Security Manager 安全管理器，管理Realm数据源
    @Bean(name = "securityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("userRealm") UserRealm userRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //inject UserRealm
        securityManager.setRealm(userRealm);
        return securityManager;
    }

    //注入自定义Realm
    @Bean(name = "userRealm")
    public UserRealm userRealm() {
        UserRealm userRealm = new UserRealm();
        //修改凭证校验登录器
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        //设置加密算法为Md5
        credentialsMatcher.setHashAlgorithmName("MD5");
        //设置散列次数
        credentialsMatcher.setHashIterations(1024);
        userRealm.setCredentialsMatcher(credentialsMatcher);
        //开启缓存管理
        userRealm.setCacheManager(new RedisCacheManager());
        //全局缓存
        userRealm.setCachingEnabled(true);
        //开启认证缓存
        userRealm.setAuthenticationCachingEnabled(true);
        userRealm.setAuthenticationCacheName("authenticationCache");
        //开启授权缓存
        userRealm.setAuthorizationCachingEnabled(true);
        userRealm.setAuthorizationCacheName("authorizationCacheName");
        return userRealm;
    }

```





> 还有一个开源项目整合包，自行了解

[Github地址（包含官方文档）](https://github.com/alexxiyang/shiro-redis/blob/master/docs/README.md#spring-boot-starter)





# Redis实现分布式会话































