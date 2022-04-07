**概述**

==**阅读本文之前你应该熟悉以下基础知识**==

> + Springboot
> + [Shiro](https://blog.csdn.net/Night__breeze/article/details/123594845?spm=1001.2014.3001.5501)
> + [JWt](https://blog.csdn.net/Night__breeze/article/details/123937833?spm=1001.2014.3001.5501)

==**首先说一下我的理解，如果有任何问题的话，请私信或者评论区及时指出**==

> **在微服务中我们一般采用的是无状态登录，因此如果我们需要在前后端分离项目中使用[Shiro](https://blog.csdn.net/Night__breeze/article/details/123594845)的话，会不恰巧的与我们的期望有所违背，原因：**
>
> 1. **Shiro默认的拦截跳转都是跳转url页面，而前后端分离后，后端并无权干涉页面跳转**
> 2. **Shiro默认使用的登录拦截校验机制恰恰就是使用的Session**
>
> **因此如需使用shiro，我们就需要对其进行改造，那么要如何改造呢？我们可以在整合Shiro的基础上自定义登录校验，继续整合JWT，或者oauth2.0等，使其成为支持服务端==无状态登录==，即token登录。**
>
> **但是我们又遇到了问题，Token颁发之后，由于只在客户端存储，所以在Token有效期内，我们无法实现Jwt的可控性，例如无法实现将用户强制下线的功能**
>
> **因此我们如果想要实现这个功能的话，就又要进行改造了，使用内存型数据库Redis作为第三方存储，这样一来我们可以通过删除Redis中的Token信息让Token失效**
>
> **但是这样做，就失去了Token最大的优点去中心化:cry:**

==**有很多Shiro整合Jwt的教程，因此会因为版本或者依赖的选择问题而出现问题，首先对这些问题做一个总结**==

> 1. JJWT是在JVM上创建和验证JSON Web Token的库。从Maven仓库可以看到它已经很久不更新了，我的项目JDk版本是Jdk11,导入JJWW库代码时会报错，[详情见JDK11，8引入不同版本的jjwt异常问题](https://blog.csdn.net/u010748421/article/details/107363925/)，所以我采用`Java-jwt`依赖,它是一个长期维护的库[Jwt官方文档](https://github.com/auth0/java-jwt)，建议多看官方文档，是最权威的解决方案
> 2. Shiro依赖的问题，Shiro依赖有两种导入方式，会导致一些代码层面的差异，详情请看[两种方式引入Shiro依赖，异同点比较](https://blog.csdn.net/Night__breeze/article/details/123594845?spm=1001.2014.3001.5502#Shiro_392)
>

==**Shiro和JWT的区别**==

> 1. Shiro是一套安全认证框架，JWT是一种生成token的机制，需要我们自己编写相关的生成逻辑
> 2. 其次Shiro可以对权限进行管理，JWT在权限这部分也只能封装到token中，需要我们自己实现处理逻辑
> 3. Shiro是基于session保持会话，进行登录校验，也就是说是有状态的，在前后端分离后是不推荐的，而JWT则是无状态的（服务端不保存session，而是生成token发送给客户端进行保存，之后的所有的请求都需要携带token，再对token进行解析判断）
> 4. Shiro已经有了对token的相关封装(如`UsernamePasswordTOken`），但是只是Shiro在服务端对用户信息进行判断的方式而已，并不是JWT所生成的可发送给客户端的字符串token。也就是说Shiro的token并不能响应给客户端。
>

综上，所以如果是要构建前后端分离且无状态的项目，还需要权限等其他安全操作，就可以对着两者进行整合使用





## 一、请求流程分析

> **登录请求**

**如果是Login请求，登录逻辑沿用jwt的登录逻辑，即登录时不需要调用shiro的subject.login()方法，只需要校验用户名和密码，然后返回token即可。到了需要进行权限认证时在执行login方法，这里使用的是jwtFilter来进行拦截**



注：由于JWT的特性，在Token有效期内，我们无法在后端实现Jwt的可控性，例如登出功能

> **所有请求流程分析**

1. **客户端发起请求，ShiroConfig配置的公共资源放行请求和JwtFilter中配置的拦截器生效，判断是否是login或logout或公共资源请求，如果是就直接执行请求，不经过Shiro的处理**
2. **其他请求被JwtFilter拦截，验证Header携带的Token**

+ **不携带token，就在JwtFilter处抛出异常/返回没有登录，让它去登陆**
+ **携带token，就到JwtFilter中获取jwt，封装成JwtToken对象。然后使用JwtRealm进行认证。在JwtRealm中进行认证判断这个token是否有效**

> **Shiro——Realm工作流程分析**

- **如果是只要拥有登录权限的话，那么就经过认证方面就可以了**
- **如果是要控制权限的话，那么就要先认证再授权**

> **JwtFilter执行流程**    

1. **获取header是否有"Authorization"的键，有就获取，没有就抛出异常**    
2. **将获取的jwt字符串封装在创建的JwtToken中，使用subject执行login()方法进行校验。这个方法会调用创建的JwtRealm**    
3. **执行JwtRealm中的认证方法，使用`jwtUtil.Verify(jwt)`判断是否登录过，返回true就继续执行下去**

> 整合Jwt最重要的点就在于自定义JwtFilter

Shiro本身也提供了很多内置Filter,但是与Jwt的整合需要我们自定义Filter，这是一个坑点，详情请看JwtFilter

图为ShiroFilter继承关系图

![image-20220404161445301](https://s2.loli.net/2022/04/04/H7wVn6YDQXLGluy.png)

## 二、代码结构分析

> + **com.loki.utils.JwtUtil**
>   1. 生成token
>   2. 校验token
>   3. 获取token的信息
>
> + **com.loki.shiro.JwtFilter**——重点
>
>   继承的是Shiro内置的`BasicHttpAuthenticationFilter`,但是其它方法我也都实现了
>
> + **com.loki.shiro.JwtToken**
>
>   封装token来替换Shiro原生Token，要实现AuthenticationToken接口,并重写它的两个方法
>
> + **com.loki.shiro.CustomHashedCredentialsMatcher**
>
>   之前我们使用用户名和密码认证时，需要指明用户在注册时密码的加密方式然后交给realm执行认证，而现在我们需要认证token,因此需要自定义token的认证方式
>
> + **com.loki.config.ShiroConfig**
>   1. 构建`securityManager`环境
>   2. 配置`shiroFilter`并将`jwtFilter`添加进`shiro`的拦截器链中，放行登录注册等公共资源请求
>   3. 配置自定义Token校验器
> + **com.loki.shiro.realms.AccountReaml**
>   1. `supports`：为了让realm支持jwt的凭证校验，不写Shiro会报错
>   2. `doGetAuthorizationInfo`：权限校验
>   3. `doGetAuthenticationInfo`：登录认证校验

## 三、依赖导入

由于需要对shiro的SecurityManager进行设置，`shiro-spring-boot-starter`给我们自动配置了大多数组件，可是security注入的是`SessionSecurityManager`，并且已经将Realm待参进行构建

这里我们使用`spring-shiro`

```xml
<!-- 自动依赖导入 shiro-core 和 shiro-web -->
<dependency>
    <groupId>org.apache.shiro</groupId>
    <artifactId>shiro-spring</artifactId>
    <version></version>
</dependency>
<!-- jwt -->
<dependency>
    <groupId>com.auth0</groupId>
    <artifactId>java-jwt</artifactId>
    <version>3.18.1</version>
</dependency>
```

## 四、Demo Project——代码实现

**请确保已经掌握以下内容**

+ Springboot
+ Mybatis-plus

### JwtUtil——Jwt工具类

这个代码一般是固定的，主要功能有 生成 token | 校验 token | 获取Token信息

> JJWT与JWT之我遇到的问题

+ JDK11环境下，导入jjwt包，编写测试类时还可以正常生成Token，但是在Springboot环境中会报错，因此导入 `java-jwt` 依赖

+ JJWT与JWT代码逻辑相同但是也有不同之处,[详情请见](https://blog.csdn.net/Night__breeze/article/details/123937833?spm=1001.2014.3001.5501)

```java
package com.loki.utils;

import cn.hutool.core.lang.UUID;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;	
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.Map;

/**
 * @author oliverloki
 * @Description: JWt工具类--生成 token 和 校验 token
 * @date 2022年03月29日 22:11
 */
@Slf4j
@Data
@Component
@ConfigurationProperties(prefix = "loki.jwt")
public class JwtUtil {
    /**
     * 密钥
     */
    private String secretKey;

    /**
     * Token有效时间
     */
    private long effectiveTime;

    /**
     * 根据payload信息生成JSON WEB TOKEN
     * @param payloadClaims 在jwt中存储的一些非隐私信息
     * @return
     */
    public String getToken(Map<String, Object> payloadClaims) {
        long currentTimeMillis = System.currentTimeMillis();
        Date expireTime = new Date(System.currentTimeMillis() + effectiveTime);
        return JWT.create()
                .withJWTId(UUID.randomUUID().toString())
                .withIssuedAt(new Date(currentTimeMillis))//以毫秒为单位，换算当前系统时间生成的iat
                .withExpiresAt(expireTime)//过期时间
                .withSubject("username")//签发人，也就是JWT是给谁的（逻辑上一般都是username或者userId）
                .withPayload(payloadClaims)
                .sign(Algorithm.HMAC256(secretKey));
    }
    /**
     * 校验token是否合法,可以做很精细的细粒度处理
     *
     * @param token
     * @return
     */
    public boolean verify(String token) {
        try{
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secretKey)).build();
            verifier.verify(token);// 校验不通过会抛出异常
            return true;//判断合法的标准：1. 头部和荷载部分没有篡改过 2. 没有过期
        }catch (IllegalArgumentException e) {
            log.info("IllegalArgumentException");
            return false;
        } catch (JWTVerificationException e) {
            log.info("JWTVerificationException");
            return false;
        }
    }
    /**
     * 获得Token中的信息无需secret解密也能获得
     *
     * @param token
     * @return
     */
    public Claims getClaimByToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
        }catch (Exception e){
            log.debug("解析token出错", e);
            return null;
        }
    }

}
```

application.yaml

```yaml
loki:
  jwt:
    #Jwt密钥
    secret-key: $$oliverloki$$#
    #token有效时间，单位ms
    effective-time: 300000
```

### JwtFilter——自定义Shiro过滤器

这个过滤器是要注册到shiro配置里面去的，用来辅助shiro进行过滤处理。对于自定义Shiro过滤器，需要继承Shiro内置过滤器类，一般继承以下几种

> **`AccessControlFilter`：最常用的，该filter中onPreHandle调用isAccessAllowed和onAccessDenied决定是否继续执行。一般继承该filter，isAccessAllowed决定是否继续执行。onAccessDenied做后续的操作，如重定向到另外一个地址、添加一些信息到request域等等。**

> **`AuthenticatingFilter`/`BasicHttpAuthenticationFilter`**
>
> **若要自定义登录filter，一般是由于前端传过来的需求所定义的token与shiro默认提供token的不符，在这里面实现createToken来创建自定义token**

**注：我选择继承三种`BasicHttpAuthenticationFilter`,但是继承三个类的代码我都放在下面，实际运行时使用一种即可**,最好和我一样



> 这里我遇到一个大坑，不能在这个类添加`@Component`注解,否则会报错
>
> ![](https://files.catbox.moe/ycak4v.png)
>
> 问题思考，[Shiro](https://so.csdn.net/so/search?q=Shiro&spm=1001.2101.3001.7020)中的filter是在项目本身的Filter链执行之前加载的，所以@`Component`的存在让它没有被正常加载进Shiro的配置中，或者被注册成了Spring的bean，无法被Shiro使用



`BasicHttpAuthenticationFilter`

```java
package com.loki.config.shiro.jwt;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author oliverloki
 * @Description: Jwt过滤器 ref by Shiro.config
 * @date 2022年03月30日 1:06
 */

@Slf4j
public class JwtFilter extends BasicHttpAuthenticationFilter {

    /**
     * 过滤器拦截请求的入口方法
     * 是否允许访问，如果带有 token，则对 token 进行检查，否则直接通过
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        log.info("检查是否携带Token");
        if (isLoginAttempt(request, response)) {//请求头包含Token
            try { //如果存在，则进入 executeLogin 方法，检查 token 是否正确
                log.info("执行===》executeLogin");
                executeLogin(request, response);
                return true;
            } catch (Exception e) {
                //token 错误
                responseError(response, e.getMessage());
            }
        }
        //如果请求头不存在 Token，则可能是执行登陆操作或者是游客状态访问，无需检查 token，直接返回 true
        return true;
    }

    /**
     * 判断用户是否已经登录
     * 检测 header 里面是否包含 Token 字段
     */

    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        HttpServletRequest req = (HttpServletRequest) request;
        String token = req.getHeader("Authorization");
        return token != null;
    }

    /**
     * Shiro认证操作
     * executeLogin实际上就是先调用createToken来获取token，这里我们重写了这个方法，就不会自动去调用createToken来获取token
     * 然后调用getSubject方法来获取当前用户再调用login方法来实现登录
     * 这也解释了我们为什么要自定义jwtToken，因为我们不再使用Shiro默认的UsernamePasswordToken了。
     * */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {

        HttpServletRequest req = (HttpServletRequest) request;
        String token = req.getHeader("Authorization");

        JwtToken jwt = new JwtToken(token);
        //交给自定义的realm对象去登录，如果错误他会抛出异常并被捕获
        //log.info("获取的Token为" + ((HttpServletRequest) request).getHeader("Authorization"));
        try {
            log.info("进入Shiro认证");
            getSubject(request, response).login(jwt);
        }catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    /**
     * 在JwtFilter处理逻辑之前，进行跨域处理
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        log.info("进入预处理器--处理完成进入JwtFilter");
        HttpServletRequest req= (HttpServletRequest) request;
        HttpServletResponse res= (HttpServletResponse) response;
        res.setHeader("Access-control-Allow-Origin",req.getHeader("Origin"));
        res.setHeader("Access-control-Allow-Methods","GET,POST,OPTIONS,PUT,DELETE");
        res.setHeader("Access-control-Allow-Headers",req.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
        if (req.getMethod().equals(RequestMethod.OPTIONS.name())) {
            res.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }

    /**
     * 非法请求
     */
    private void responseError(ServletResponse response, String message) {
        try {
            log.info("非法请求"+message);
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }

}
```



`AuthenticatingFilter`

```java
/**
 * @author oliverloki
 * @Description: Jwt过滤器 ref by Shiro.config
 * @date 2022年03月30日 1:06
 */
@Component
@Slf4j
public class JwtFilter extends AuthenticatingFilter {
    @Autowired
    JwtUtil jwtUtil;

    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String jwt = request.getHeader("Authorization");
        if (StringUtils.isEmpty(jwt)){
            return null;
        }
        return new JwtToken(jwt);
    }


    /**
     *
     * @param servletRequest
     * @param servletResponse
     * @return
     * @throws Exception
     */
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String jwt = request.getHeader("Authorization");
        if (StringUtils.isEmpty(jwt)){
            return true;
        }
        // 校验jwt
        Claims claim = jwtUtil.getClaim(jwt);
        if (claim == null || jwtUtil.isTokenExpired(claim.getExpiration())){
            servletResponse.setContentType("text/html;charset=UTF-8");
            // token过期之后的处理
            servletResponse.getWriter().write(String.valueOf(JSONUtil.parse(Result.error("token已过期，请重新登录"))));
            servletResponse.getWriter().flush();
            servletResponse.getWriter().close();
        }
        // 执行登录
        return executeLogin(servletRequest,servletResponse);
    }

    /**
     *  登录失败处理
     * @param token
     * @param e
     * @param request
     * @param response
     * @return
     */
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        Throwable throwable = e.getCause() == null ? e : e.getCause();
        Result result = Result.error(throwable.getMessage());
        String json = JSONUtil.toJsonStr(result);
        try{
            // 响应给前端
            httpServletResponse.getWriter().print(json);
        } catch (IOException ioException) {

        }
        return false;
    }

    /**
     * 在JwtFilter处理逻辑之前，进行跨域处理
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {

        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个OPTIONS请求，这里我们给OPTIONS请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(org.springframework.http.HttpStatus.OK.value());
            return false;
        }

        return super.preHandle(request, response);
    }
}
```

继承`AccessControlFilter`

```java
package com.loki.shiro;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author oliverloki
 * @Description: Jwt过滤器 ref by Shiro.config
 * @date 2022年03月30日 1:06
 */
@Component
@Slf4j
public class JwtFilter extends AccessControlFilter {
    //设置请求头中需要传递的字段名
    protected static final String AUTHORIZATION_HEADER = "Authorization";

    /*
     * 1. 返回true，shiro就直接允许访问url
     * 2. 返回false，shiro才会根据onAccessDenied的方法的返回值决定是否允许访问url
     *
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        log.warn("isAccessAllowed 方法被调用");
        //这里先让它始终返回false来使用onAccessDenied()方法
        return false;
    }

    /*
     *  如果返回true表示登录通过
     *
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        log.warn("onAccessDenied 方法被调用");
        //所以以后发起请求的时候就需要在Header中放一个Authorization，值就是对应的Token
        HttpServletRequest req = (HttpServletRequest) request;

        // 解决跨域问题
        if (HttpMethod.OPTIONS.toString().matches(req.getMethod())) {
            return true;
        }
        //获得JWtToken
        JwtToken token = new JwtToken(req.getHeader(AUTHORIZATION_HEADER));
        //委托给Realm进行验证
        try {
            //调用登陆会走Realm中的身份认证方法
            getSubject(request, response).login(token);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            onLoginFail(response);
            //调用下面的方法向客户端返回错误信息
            return false;
        }
    }

    //登录失败时默认返回 401 状态码
    private void onLoginFail(ServletResponse response) throws IOException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpResponse.getWriter().write("login error");
    }
    
}
```

### JwtToken——用户名密码的载体

JWTToken 差不多就是 Shiro 用户名密码的载体

shiro默认支持的是UsernamePasswordToken，而我们现在采用了Jwt的方式进行登录验证，所以这里我们自定义一个JwtToken，同时在shiro的Realm类中完成supports方法

```java
package com.loki.shiro;

import lombok.AllArgsConstructor;
import org.apache.shiro.authc.AuthenticationToken;
/**
 * @author oliverloki
 * @Description:
 * @date 2022年03月29日 22:34
 */
@AllArgsConstructor
public class JwtToken implements AuthenticationToken{
    private String token;

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
```

### AccountRealm——Shiro认证授权操作

创建判断`jwt`是否有效的认证方式的`Realm`	

```java
package com.loki.config.shiro;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.loki.entity.User;
import com.loki.service.IUserService;
import com.loki.config.shiro.jwt.JwtToken;
import com.loki.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author oliverloki
 * @Description: 自定义Realm
 * @date 2022年03月29日 15:58
 */
@Slf4j
public class AccountRealm extends AuthorizingRealm {

    @Autowired
    IUserService userService;
    @Autowired
    JwtUtil JwtUtil;

    /*
     * 多重写一个support
     * 标识这个Realm是专门用来验证JwtToken，不负责验证其他的token（UsernamePasswordToken）
     * 必须重写此方法，不然Shiro会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }


    /**
     * Shiro 认证操作
     * 默认使用此方法进行用户名正确与否验证，错误抛出异常即可
     *
     * @param token 就是从过滤器中传入的jwtToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        log.info("————————身份认证——————————");
        String credentials = (String) token.getCredentials();
        if (null == credentials || JwtUtil.getClaimByToken(credentials) == null) {
            throw new AuthenticationException("token无效!");
        }
        // 解密获得username，用于和数据库进行对比
        String username = JwtUtil.getTokenUername(credentials);
        log.info("认证的username为"+username);
        User user = userService.getOne(new QueryWrapper<User>().eq("username", username));

        if (null == user) throw new AuthenticationException("用户不存在!");

        //交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配，不配置的话则使用默认的SimpleCredentialsMatcher
        //用户名,凭证,realm name
        return new SimpleAuthenticationInfo(username,token,"accountRealm");
    }

    /**
     * Shiro授权操作
     * 只有当需要检测用户权限的时候才会调用此方法，例如Controller层方法有Shiro权限注解
     * 使用userID去数据库中查找到对应的权限，然后将权限赋值给这个用户就可以实现权限的认证了
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        log.info("————权限认证 [ roles、permissions]————");
        //暂不编写，此处编写后，controller中可以使用@RequiresPermissions来对用户权限进行拦截
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        //String username = principals.toString();
        //authorizationInfo.setRoles(userService.getRoles(username));
        //authorizationInfo.setStringPermissions(userService.queryPermissions(username));
        return authorizationInfo;
    }
}
```

### 自定义Token校验

**Realm认证时需要用到这个类，在ShiroConfig中配置**

自定义你的Token校验逻辑，比如与存储在Redis中的Token做对比，这里我先默认返回True，实现具体逻辑的请看Shiro整合Jwt+Redis

```java
package com.loki.config.shiro;

import com.loki.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author oliverloki
 * 自定义token的认证方式
 * @date 2022年04月07日 1:11
 */
@Slf4j
public class CustomHashedCredentialsMatcher extends HashedCredentialsMatcher {
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        /*String accessToken = (String) jwtToken.getCredentials();
        String userId = JwtUtil.getUserId(accessToken);*/

       /* //判断用户是否被删除
        if (redisService.hasKey(Constant.DELETED_USER_KEY + userId)) {
            throw new BusinessException(BaseResponseCode.ACCOUNT_HAS_DELETED_ERROR);
        }
        //判断是否被锁定
        if (redisService.hasKey(Constant.ACCOUNT_LOCK_KEY + userId)) {
            throw new BusinessException(BaseResponseCode.ACCOUNT_LOCK);
        }
        //校验token
        if (!JwtTokenUtil.validateToken(accessToken)) {
            throw new BusinessException(BaseResponseCode.TOKEN_PAST_DUE);
        }*/
        return true;
    }
}
```

### ShiroConfig——Shiro配置类

1. 创建`defaultWebSecurityManagerBean`对象
2. 创建`ShiroFilterFactoryBean`来进行过滤拦截、权限控制和登录，并注册`JwtFilter`到`ShiroFilterFactoryBean`中
3. 关闭session
4. 实现自定义Token认证器
5. 添加注解权限开发

```java
package com.loki.config.shiro;
import com.loki.config.shiro.jwt.JwtFilter;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.servlet.Filter;
import java.util.LinkedHashMap;


/**
 * @author oliverloki
 * @Description: Shiro配置类
 * @date 2022年03月28日 18:21
 */
@Configuration
public class ShiroConfig {


    @Bean(name = "accountRealm")
    public AccountRealm accountRealm() {
        AccountRealm accountRealm = new AccountRealm();
        //配置自定义密码匹配器
        accountRealm.setCredentialsMatcher(new CustomHashedCredentialsMatcher());
        /*
        //开启全局缓存管理
        accountRealm.setCachingEnabled(true);
        //开启认证缓存
        accountRealm.setAuthenticationCachingEnabled(true);
        accountRealm.setAuthenticationCacheName("authenticationCache");
        //开启授权缓存
        accountRealm.setAuthorizationCachingEnabled(true);
        accountRealm.setAuthorizationCacheName("authorizationCacheName");
        accountRealm.setCacheManager(new RedisCacheManager());
        */

        return accountRealm;
    }


    /**
     * Create Security Manager 安全管理器，管理Realm数据源
     */
    @Bean(name = "securityManager")
    public DefaultWebSecurityManager securityManager(@Qualifier("accountRealm") AccountRealm accountRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //向安全管理器注入自定义Realm
        securityManager.setRealm(accountRealm);
        //关闭ShiroSession，实现Shiro无状态
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        securityManager.setSubjectDAO(subjectDAO);
        return securityManager;
    }
    /**
     * @param defaultWebSecurityManager
     * @return
     */
    @Bean
    ShiroFilterFactoryBean shiroFilterFactoryBean(@Qualifier("securityManager") DefaultWebSecurityManager defaultWebSecurityManager) {
        //设置securityManager
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager);
        //自定义过滤器，JwtFilter
        LinkedHashMap<String, Filter> filterMap = new LinkedHashMap<String, Filter>();
        filterMap.put("jwt",new JwtFilter());
        shiroFilterFactoryBean.setFilters(filterMap);

        //设置拦截器，使用LinkedHashMap保证Filter的有序性
        LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        //公共资源或登录接口，无需进入过滤器直接执行即可
        filterChainDefinitionMap.put("/login", "anon");
        filterChainDefinitionMap.put("/guest/**", "anon");

        filterChainDefinitionMap.put("/**", "jwt");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }


    //开启Shiro注解支持
    /**
     * 如果userPrefix和proxyTargetClass都为false会导致 aop和shiro权限注解不兼容 资源报错404
     * 因此两个属性至少需要其中一个属性为true才可以
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }
    /**
     * 开启aop注解支持
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(@Qualifier("securityManager") DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager); // 这里需要注入 SecurityManger 安全管理器
        return authorizationAttributeSourceAdvisor;
    }

}
```

## 五、测试结果与分析

### LoginController


ShiroConfig中配置了Login请求放行，所以这个请求不会进入过滤器

```java
	@PostMapping("/login")
    public Result login(@Validated @RequestBody LoginDto loginDto, HttpServletResponse response) {
        //根据唯一用户名去查询用户
        User user = userService.getOne(new QueryWrapper<User>().eq("username", loginDto.getUsername()));
        Assert.notNull(user, "用户不存在");
        if (!user.getPassword().equals(SecureUtil.md5(loginDto.getPassword()))) { 
            return Result.error("密码不正确");
        }
        //生成Jwt，返回响应头给前端
        Map<String, Object> map = new HashMap<>();
        map.put("username", user.getUsername());//唯一用户名
        map.put("avatar", user.getAvatar());//头像
        map.put("nickName", user.getNickname());//昵称
        String jwt = jwtUtil.generateToken(map);
        response.setHeader( "Authorization",jwt);
        return Result.success("登录成功",map);//直接返回数据前端要用
    }
```

> **成功截图**
>
> ![](D:\桌面\P_picture_cahe\wg719w.png)

### GuestController

**这个代表的是公共资源接口，不会进入过滤器**

```java
 	@PostMapping("/guest")
    public String guest() {
        return "Loki爱吃红烧肉";
    }
```

> 成功截图
>
> ![image-20220407142704554](https://s2.loli.net/2022/04/07/cKjMQeuUCOlxwvG.png)

### AdminController

**这个代表的是需要登陆后才能访问的接口，需要在Controller方法上加`@RequiresAuthentication`**注解，登录以后前端每次请求头会携带一个Token，进入JwtFilter认证

```java
	@RequiresAuthentication
    @PostMapping("/user/{id}")
    public Object user(@PathVariable("id") long id) {

        return userService.getById(id);
    }

```

> 携带Token截图
>
> ![image-20220407143142266](https://s2.loli.net/2022/04/07/vA8OSxuDr9sVQqN.png)

> 不携带Token截图
>
> ![image-20220407143229045](https://s2.loli.net/2022/04/07/WrR6LwGqHb9oZcn.png)



## 六、Shiro权限注解接口总结

在ShiroConfig中开启注解支持以后，在需要鉴权的接口方法上面加上注解就可以对该接口进行鉴权了

下面我来整理总结以下常用的注解

#### @RequiresAuthentication

这个注解还没有涉及到鉴权，所以是不会走`Realm`授权部分的

这个注解的作用就是，要求用户登录了之后才可以访问这个接口。加上了这个注解，服务器会先判断传递过来的请求头是否带有token，如果没有直接拒绝访问，如果带有会进行认证部分，认证通过了就可以访问，不通过拒绝访问

例子就是刚才的LoginController

#### @RequiresRoles

这个注解已经涉及到鉴权，所以是会走`Realm`授权部分的。

这个注解是用来鉴别用户的角色的，拥有这个角色的用户才可以访问这个接口

```java
// 拥有 admin 角色可以访问
@RequiresRoles("admin")
// 拥有 user 或 admin 角色可以访问
@RequiresRoles(logical = Logical.OR, value = {"user", "admin"})
```

#### @RequiresPermissions

这个注解是用来鉴别用户的权限的，拥有这个权限的用户才可以访问这个接口。

运用

```java
// 拥有 vip 和 normal 权限可以访问
@RequiresPermissions(logical = Logical.AND, value = {"vip", "normal"})

```

#### 结合使用

```java
// 拥有 user 或 admin 角色，且拥有 vip 权限可以访问
@GetMapping("/getVipMessage")
@RequiresRoles(logical = Logical.OR, value = {"user", "admin"})
@RequiresPermissions("vip")
public ResultMap getVipMessage() {
    return resultMap.success().code(200).message("成功获得 vip 信息！");
}

```

> **==如果有帮助到你的话，点个赞再走吧，也可以在评论区留下你宝贵的意见~~:factory:==**









