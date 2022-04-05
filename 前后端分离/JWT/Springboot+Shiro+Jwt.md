Shiro过滤器——SpringBoot整合Shiro+Jwt自定义过滤器(流程分析与代码实现)



**网上有很多Shiro整合Jwt的教程，但是会因为版本或者依赖的选择问题而出现问题，首先对这些问题做一个总结**

> 1. JJWT是在JVM上创建和验证JSON Web Token的库。从Maven仓库可以看到它已经很久不更新了，我的项目JDk版本是Jdk11,导入JJWW库代码时会报错，[详情见JDK11，8引入不同版本的jjwt异常问题](https://blog.csdn.net/u010748421/article/details/107363925/)，所以我采用`Java-jwt`依赖,它是一个长期维护的库
>
>    [Jwt官方文档](https://github.com/auth0/java-jwt)，建议多看官方文档，是最权威的解决方案
>
> 2. Shiro依赖的问题，Shiro依赖有两种导入方式，会导致一些代码层面的差异，详情请看[两种方式引入Shiro依赖，异同点比较](https://blog.csdn.net/Night__breeze/article/details/123594845?spm=1001.2014.3001.5502#Shiro_392)

## 一、请求流程分析

> **登录请求**

**如果是Login请求，登录逻辑沿用jwt的登录逻辑，即登录时不需要调用shiro的subject.login()方法，只需要校验用户名和密码，然后返回token即可。到了需要进行权限认证时在执行login方法，这里使用的是jwtFilter来进行拦截**

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
>   继承的是Shiro内置的`BasicHttpAuthenticationFilter`
>
>   代码部分有详细注释
>
> + **com.loki.shiro.JwtToken**
>
>   封装token来替换Shiro原生Token，要实现AuthenticationToken接口,并重写它的两个方法
>
> + **com.loki.config.ShiroConfig**
>   1. 创建`defaultWebSecurityManagerBean`对象
>   2. 创建`ShiroFilterFactoryBean`来进行过滤拦截、权限控制和登录，并注册`JwtFilter`到`ShiroFilterFactoryBea`中
>   3. 关闭session
>   4. 添加注解权限开发
>
> + **com.loki.shiro.realms.AccountReaml**
>   1. `supports`：为了让realm支持jwt的凭证校验
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

### JwtUtil——Jwt工具类

这个代码一般是固定的，主要功能有 生成 token | 校验 token | 获取Token信息

> JJWT与JWT之我遇到的问题

+ JDK11环境下，导入jjwt包，编写测试类时还可以正常生成Token，但是在Springboot环境中会报错，因此改为JWT

+ JJWT与JWT代码逻辑相同但是也有不同之处，如JJWT生成TOken代码如下，JWT的方式在JwtUtil中

```java
return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setClaims(claims)
                .setId(UUID.randomUUID().toString())//JWT的唯一标识，一般设置成唯一的，这个方法可以生成唯一标识
                .setIssuedAt()//以毫秒为单位，换算当前系统时间生成的iat
                .setSubject("username")//签发人，也就是JWT是给谁的（逻辑上一般都是username或者userId）
                .setExpiration(expireTime)//过期时间
                .signWith(SignatureAlgorithm.HS256, secretKey)//生成jwt使用的算法和秘钥
                .compact();
```

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

这个过滤器是要注册到shiro配置里面去的，用来辅助shiro进行过滤处理。

对于自定义Shiro过滤器，需要继承Shiro内置过滤器类，一般继承以下几种

> `AccessControlFilter`：最常用的，该filter中onPreHandle调用isAccessAllowed和onAccessDenied决定是否继续执行。一般继承该filter，isAccessAllowed决定是否继续执行。onAccessDenied做后续的操作，如重定向到另外一个地址、添加一些信息到request域等等。

> `AuthenticatingFilter`/`BasicHttpAuthenticationFilter`
>
> 若要自定义登录filter，一般是由于前端传过来的需求所定义的token与shiro默认提供token的不符，在这里面实现createToken来创建自定义token

**三种代码我都实现了，实际运行时使用一种即可**

继承`AuthenticatingFilter`,实现以下几个方法

1. createToken：实现登录，我们需要生成我们自定义支持的JwtToken
2. onAccessDenied：拦截校验，当头部没有Authorization时候，我们直接通过，不需要自动登录；当带有的时候，首先我们校验jwt的有效性，没问题我们就直接执行executeLogin方法实现自动登录
3. onLoginFailure：登录异常时候进入的方法，我们直接把异常信息封装然后抛出
4. preHandle：拦截器的前置拦截，因为我们是前后端分析项目，项目中除了需要跨域全局配置之外，我们再拦截器中也需要提供跨域支持。这样，拦截器才不会在进入Controller之前就被限制了

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

继承`BasicHttpAuthenticationFilter`

```java
/**
 * @author oliverloki
 * @Description: Jwt过滤器 ref by Shiro.config
 * @date 2022年03月30日 1:06
 */
@Component
@Slf4j
public class JwtFilter extends BasicHttpAuthenticationFilter {
    /**
     * 是否允许访问，如果带有 token，则对 token 进行检查，否则直接通过
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if (isLoginAttempt(request, response)) {//请求头包含Token
            try { //如果存在，则进入 executeLogin 方法，检查 token 是否正确
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
        log.info("判断是否包含Token");
        HttpServletRequest req = (HttpServletRequest) request;
        String token = req.getHeader("Authorization");
        return token != null;
    }

    /*
     * executeLogin实际上就是先调用createToken来获取token，这里我们重写了这个方法，就不会自动去调用createToken来获取token
     * 然后调用getSubject方法来获取当前用户再调用login方法来实现登录
     * 这也解释了我们为什么要自定义jwtToken，因为我们不再使用Shiro默认的UsernamePasswordToken了。
     * */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        log.info("执行===》executeLogin");
        HttpServletRequest req = (HttpServletRequest) request;
        String token = req.getHeader("Authorization");

        JwtToken jwt = new JwtToken(token);
        //交给自定义的realm对象去登录，如果错误他会抛出异常并被捕获
        log.info("获取的Token为" + ((HttpServletRequest) request).getHeader("Authorization"));
        try {
            log.info("执行===》登录");
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


    /**
     * 非法请求
     */
    private void responseError(ServletResponse response, String message) {

        try {
            log.info("非法请求"+message);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

```



`AccessControlFilter`

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

shiro默认支持的是UsernamePasswordToken

而我们现在采用了jwt的方式，所以这里我们自定义一个JwtToken，来完成shiro的supports方法

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

```

### ShiroConfig——Shiro配置类

```java

```



## 五、测试结果

### LoginController



### GuestController



### AdminController







## 六、Shiro权限注解接口总结

