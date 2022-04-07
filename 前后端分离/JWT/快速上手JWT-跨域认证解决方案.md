

快速上手JWT-详细流程分析与代码实现（包含与Shiro的整合)

==JWT整合系列——SpringBoot整合Shiro+JWT整合(流程分析与代码实现)==

## 一、你需要的知识储备

### 理解跨域和CORS

> **浏览器同源策略**

在解释跨域的概念之前，先让我们来了解下浏览器的同源策略，这也是为什么会有跨域的由来。

同源策略是一项约定，是浏览器的行为，限制了从同一个源下的文档或脚本如何与来自另一个源的资源进行交互。这是一个用于隔离潜在恶意文件的重要安全机制。

所谓**同源**是指 **`协议`**+**`域名`**+**`端口`** 三者都相同，不满足这个条件即为**非同源**，即使两个不同域名指向同一IP地址。 **当协议、子域名、主域名、端口号中任意一个不相同时，都算作不同域。** 不同域之间相互请求资源，就算作`跨域`

> **同源策略限制的内容**

- Cookie、LocalStorage、IndexedDB 等存储性内容
- DOM 节点
- AJAX 请求发送后，响应结果被浏览器拦截（即请求发送了，服务器响应了）

### 传统跨域认证问题的解决方案

1. 用户第一次请求服务器的时候，服务器根据用户提交的相关信息，**创建对应的 Session**
2. 请求返回时将此 Session 的**唯一标识信息 SessionID** 返回给浏览器。
3. 浏览器接收到服务器返回的 SessionID 信息后，会**将此信息存入到 Cookie 中，同时 Cookie 记录此 SessionID 属于哪个域名** 。
4. **当用户第二次访问服务器的时候，请求会自动判断此域名下是否存在 Cookie 信息**，如果存在自动将 Cookie 信息也发送给服务端，服务端会从 Cookie 中获取 SessionID，再根据 SessionID 查找对应的 Session 信息，如果没有找到说明用户没有登录或者登录失效，如果找到 Session 证明用户已经登录可执行后面操作



> 引用廖雪峰老师的一段话

这种模式的问题在于，扩展性（scaling）不好。单机当然没有问题，如果是服务器集群，或者是跨域的服务导向架构，就要求 session 数据共享，每台服务器都能够读取 session。

举例来说，A 网站和 B 网站是同一家公司的关联服务。现在要求，用户只要在其中一个网站登录，再访问另一个网站就会自动登录，请问怎么实现？

一种解决方案是 session 数据持久化，写入数据库或别的持久层。各种服务收到请求后，都向持久层请求数据。这种方案的优点是架构清晰，缺点是工程量比较大。另外，持久层万一挂了，就会单点失败。

另一种方案是服务器索性不保存 session 数据了，所有数据都保存在客户端，每次请求都发回服务器。JWT 就是这种方案的一个代表

### 怎么理解HTTP是无状态的协议？

对于事务处理没有记忆能力，每次客户端和服务端会话完成时，服务端不会保存任何会话信息：每个请求都是完全独立的，服务端无法确认当前访问者的身份信息，无法分辨上一次的请求发送者和这一次的发送者是不是同一个人。所以服务器与浏览器为了进行**会话跟踪**（知道是谁在访问我），就必须主动的去维护一个状态，这个状态用于告知服务端前后两个请求是否来自同一浏览器。而这个状态需要通过`cookie`或者`session`去实现

```
有状态：
A：你今天中午吃的啥？
B：吃的大盘鸡。
A：味道怎么样呀？
B：还不错，挺好吃的。

无状态：
A：你今天中午吃的啥？
B：吃的大盘鸡。
A：味道怎么样呀？
B：？？？啊？啥？啥味道怎么样？

所以需要cookie这种东西：
A：你今天中午吃的啥？
B：吃的大盘鸡。
A：你今天中午吃的大盘鸡味道怎么样呀？
B：还不错，挺好吃的
```

### 什么是Cookie?

+ **`Cookie`是服务器发送到用户浏览器并保存在本地的数据**，`Cookie` 存储在客户端。它会在浏览器下次向同一服务器再发起请求时被携带并发送到服务器上

+ `Cookie`是不可跨域的：每个`Cookie`都会绑定单一的域名，无法在别的域名下获取使用，**一级域名和二级域名之间是允许共享使用的**（靠的是 domain）

### 什么是Session？

- **`session `是另一种记录服务器和客户端会话状态的机制**，即告诉服务端前后两个请求是否来自同一个客户端（浏览器），知道谁在访问我。**因为http本身是无状态协议**，这样，无法确定你的本次请求和上次请求是不是你发送的。如果要进行类似论坛登陆相关的操作，就实现不了了。
- `session `是基于 cookie 实现的，`session `存储在服务器端`sessionId `会被存储到客户端的cookie 中。
- 如果浏览器禁用了cookie或不支持cookie，这种可以通过URL重写的方式发到服务器

### Cookie和Session的区别

- **安全性：**Session 是存储在服务器端的，Cookie 是存储在客户端的。所以 Session 相比 Cookie 安全，
- **存取值的类型不同**：Cookie 只支持存字符串数据，想要设置其他类型的数据，需要将其转换成字符串，Session 可以存任意数据类型。
- **有效期不同：** Cookie 可设置为长时间保持，比如我们经常使用的默认登录功能，Session 一般失效时间较短，客户端关闭（默认情况下）或者 Session 超时都会失效。
- **存储大小不同：** 单个 Cookie 保存的数据不能超过 4K，Session 可存储数据远高于 Cookie，但是当访问量过多，会占用过多的服务器资源。

有了以上的基础，我们接下来看一看传统跨域认证方式的实现以及它的缺点，再看一看JWT是如何解决这个问题的

## 二、快速入门JWT——定义、结构、功能、适用场景

### JWT定义与适用场景

**什么是JWT**

> Json web token (JWT), 是为了在网络应用环境间传递声明而执行的一种基于JSON的开放标准（[(RFC 7519](https://link.jianshu.com?t=https://tools.ietf.org/html/rfc7519)).该token被设计为紧凑且安全的，特别适用于分布式站点的单点登录（SSO）场景。JWT的声明一般被用来在身份提供者和服务提供者间传递被认证的用户身份信息（作为JSON对象传输），以便于从资源服务器获取资源，也可以增加一些额外的其它业务逻辑所必须的声明信息，该token也可直接被用于认证，也可被加密。

### 功能——什么时候应该用JWT

下列场景中使用JSON Web Token是很有用的

> - Authorization (授权) : 这是使用JWT的最常见场景。一旦用户登录，后续每个请求都将包含JWT，允许用户访问该令牌允许的路由、服务和资源。单点登录是现在广泛使用的JWT的一个特性，因为它的开销很小，并且可以轻松地跨域使用。
> - Information Exchange (信息交换) : 对于安全的在各方之间传输信息而言，JSON Web Tokens无疑是一种很好的方式。因为JWT可以被签名，例如，用公钥/私钥对，你可以确定发送人就是它们所说的那个人。另外，由于签名是使用头和有效负载计算的，您还可以验证内容没有被篡改。

### JWT结构

实质上是一个字符串，由三部分组成，用 . 分割

理论Jwt应该是这个样子的

![image-20220331171404801](https://s2.loli.net/2022/04/03/3dhy8j2vFoKkYSf.png)

```apl
Header(base64Url). 	
Payload (base64Url). 
Secret(header(base64Url)+payload (base64Url)+Salt)
```

一个真实的Jwt

```
eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJjdXJyZW50VGltZU1pbGxpcyI6MTY0ODgwNzQ3NDk3MCwiZXhwIjoxNjQ4ODE0Njc0LCJ1c2VybmFtZSI6Imxva2kifQ.fWW0m_Dvt62dJoxujsy0TRsHdpOPerGfy4PQKSiJtDA
```

> JWT组成

1.标头(Header)

2.有效载荷(Payload)

3.签名(Signature)

+ **Header**

  标头通常由两部分组成：令牌的类型(即JWT)和所使用的签名算法，例如HMAC SHA256(默认)或RSA。它会使用Base64编码组成JWT结构的第一部分。

+ **Payload**

  将能用到的用户信息放在 Payload中。不要放特别敏感的信息，例如密码

+ **签名**   ==服务器验证Token时只会验证签名==

  前面两部分都是使用 Base64进行编码的，即前端可以解开知道里面的信息。Signature需要使用编码后的header和payload以及我们提供的一个密钥，然后使用header 中指定的签名算法(HS256)进行签名。签名的作用是保证JWT没有被篡改过

==**注：服务器验证Token时只会验证第三部分**==



**JWt的优点**

> - 因为 JWT 是自包含的（内部包含了一些会话信息），因此减少了需要查询数据库的次数
> - 因为 JWT 并不使用 Cookie 的，所以你可以使用任何域名提供你的 API 服务而不需要担心跨域资源共享问题（CORS）
> - 因为用户的状态不再存储在服务端的内存中，所以这是一种**无状态的认证机制**

下面我们来看一下具体实现

## 三、JWT认证流程分析——与Springboot的整合

**注：这不是一个完整的实现，只展示了核心代码，但是一定能让你更直观的理解它的工作流程**

[你可以点击这里看到一个完整实现的demo](https://github.com/OliverLoki/LokiBlogver2.0)

### 流程分析

> 1、客户端发起请求，拦截器生效，判断是否是login或logout或公共资源请求，如果是就直接执行请求
>
> 2、如果是Login请求，就执行登录Controller并且生成一个Token返回给前端
>
> 3、后续如果请求需要登录之后才能访问的接口，会被拦截器拦截，进行JWT的验签过程，判断签名是否过期，是否被篡改，进而做出下一步决策

![image-20220331160949189](D:\桌面\P_picture_cahe\image-20220331160949189.png)

### 代码结构分析

> + com.loki.util.JwtUtil
>
>   Jwt工具类，用于生成Token，验证Token的正确性，判断Token是否过期
>
> + com.loki.intercepter.JwtInterceptor
>
>   重写`preHandle`方法，这个方法将在请求处理之前进行调用
>
> + com.loki.config.IntercepterConfig
>
>   将自定义好的拦截器处理类进行注册，并通过`addPathPatterns`、`excludePathPatterns`等属性设置需要拦截或需要排除的URL
>
> + com.loki.controller.TestController
>
>   + 登录接口
>   + 需要登录才能访问的接口
>   + 公共资源接口

### Jwt依赖两种方式全实现

> 代码差距在于JwtUtil这个类，具体代码中我都实现了，实际只需要一种方式

导入jwt

```xml
<!-- jwt -->
<dependency>
    <groupId>com.auth0</groupId>
    <artifactId>java-jwt</artifactId>
    <version>3.18.1</version>
</dependency>
```

或者导入jjwt

```xml
<!--jjwt 18年以后就不更新了-->
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt</artifactId>
            <version>0.9.1</version>
        </dependency>
```

### 具体代码

#### JWtUtil

**com.loki.util.JwtUtil**

导入JWT依赖的实现

```java
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
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
@Data
public class JwtUtil {
    /**
     * 密钥
     */
    private String secretKey="";

    /**
     * Token有效时间，单位min
     */
    private long effectiveTime=1234;

    /**
     * 根据payload信息生成JSON WEB TOKEN
     *
     * @param payloadClaims 在jwt中存储的一些非隐私信息
     * @return
     */
    public String generateToken(Map<String, Object> payloadClaims) {
        long currentTimeMillis = System.currentTimeMillis();
        Date expireTime = new Date(System.currentTimeMillis() + effectiveTime * 1000 * 60);
        return JWT.create()
                .withPayload(payloadClaims)
                .withExpiresAt(expireTime)//过期时间
                //.withJWTId(UUID.randomUUID().toString())
                .withIssuedAt(new Date(currentTimeMillis))
                .sign(Algorithm.HMAC256(secretKey));
    }
    

    /**
     * 校验并获得Token中的信息
     * 1、token的header和payload是否没改过；
     * 2、没有过期
     *
     * @param token
     * @return
     */
    public Map<String, Claim> getClaimByToken(String token) {
        try {
            return JWT
                    .require(Algorithm.HMAC256(secretKey))
                    .build()
                    .verify(token)
                    .getClaims();
        } catch (Exception e) {
            log.debug("解析token出错", e);
            return null;
        }
    }
}
```

导入JJWT依赖的实现

```java
@Data
public class JwtUtils {

    private  String secret;
    private  long expire;
    private  String header;

    /**
     * 生成jwt token
     */
    public  String generateToken(long userId) {
        Date nowDate = new Date();
        //过期时间
        Date expireDate = new Date(nowDate.getTime() + expire * 1000);

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(userId + "")
                .setIssuedAt(nowDate)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 校验jwt是否合法
     * @param token
     * @return
     */
    public  Claims getClaimByToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            log.debug("validate is token error ", e);
            return null;
        }
    }

    /**
     * token是否过期
     * @return true：过期
     */
    public  boolean isTokenExpired(Date expiration) {
        return expiration.before(new Date());
    }
}
```

#### 配置拦截器

com.loki.intercepter.JwtInterceptor

> Interceptor的拦截范围其实就是Controller方法，它实际上就相当于基于AOP的方法拦截。因为Interceptor只拦截Controller方法，所以要注意，返回`ModelAndView`并渲染后，后续处理就脱离了Interceptor的拦截范围

```java
package com.loki.intercetpor;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.loki.dto.Result;
import com.loki.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@Slf4j
public class JwtInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //官方建议把Jwt放在请求头，获取请求头中的令牌
        //返回统一封装的结果
        Result result = new Result();
        String token = request.getHeader("token");
        try {
            //log.info("前端返回的令牌{{}}}",token);
            JwtUtil.verify(token);//验证令牌是否正确
            return true;//请求放行
        } catch (SignatureVerificationException e) {//签名不一致异常
            e.printStackTrace();
            result.setMsg("签名不一致异常");
        } catch (TokenExpiredException e) {//令牌过期异常
            e.printStackTrace();
            result.setMsg("令牌过期异常");
        } catch (AlgorithmMismatchException e) {//算法不匹配异常
            e.printStackTrace();
            result.setMsg("算法不匹配异常");
        }catch (InvalidClaimException e){//失效的payload异常
            e.printStackTrace();
            result.setMsg("失效的payload异常");
        }catch (Exception e){
            e.printStackTrace();
        }
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().println(result);
        return false;//为了用户友好度，需要返回给前端错误原因

    }
}
```

#### 注册拦截器 

com.loki.config.IntercepterConfig

```java
package com.loki.config;

import com.loki.intercetpor.JwtInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //配置token拦截器
        registry.addInterceptor(new JwtInterceptor())
                .addPathPatterns("/**")
            	//放行的意思是不进入拦截器
                .excludePathPatterns("/login");//登录请求放行
        		.excludePathPatterns("/guest");//访客公共资源请求放行
    }
}
```

#### 测试Controller

com.loki.controller.TestController

```java
@RestController
public class UserController {
//登录请求放行，不进入拦截器
    @GetMapping("/login/{username}/{password}")
    public Result login(@PathVariable("username") String username,
                        @PathVariable("password") String password) {
        //根据用户名获取这个用户
        User u = userService.getOne(new QueryWrapper<User>().eq("username", username));

        if (u.getPassword().equals(password)) {//如果密码正确
            HashMap<String, String> payload = new HashMap<>();
            payload.put("role", u.getRole());
            payload.put("username", u.getUsername());
            //生成token
            String token = JwtUtil.getToken(payload);
            return Result.succ(200, "登录成功", token);
        } else {
            return Result.fail("用户名或密码错误");
        }
    }

	//用户请求不放行
    //如果用户请求如果携带token，会被进去拦截器JwtInterceptor进行认证
    @GetMapping("/user")
    public Result test() {
 		return "content";
    }
}
```

## 四、总结以及我的理解

谈一谈我的理解，如果有任何问题的话，才疏学浅，请您多多指教

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

### Jwt的适用场景

JWT 最适合的场景是不需要服务端保存用户状态的场景，如果考虑到 token 注销和 token 续签的场景话，没有特别好的解决方案，大部分解决方案都给 token 加上了状态，这就有点类似 Session 认证了

### JWT的优点

1. 无状态，去中心化

2. 有效避免了CSRF 攻击

3. 适合移动端应用

   使用 Session 进行身份认证的话，需要保存一份信息在服务器端，而且这种方式会依赖到 Cookie（需要 Cookie 保存 SessionId），所以不适合移动端。

   但是，使用 token 进行身份认证就不会存在这种问题，因为只要 token 可以被客户端存储就能够使用，而且 token 还可以跨语言使用

4. 单点登录友好

### Token常见问题以及解决办法

> 注销登录等场景下 token 还有效
>
> 与之类似的具体相关场景有：
>
> 1. 退出登录;
> 2. 修改密码;
> 3. 服务端修改了某个用户具有的权限或者角色；
> 4. 用户的帐户被删除/暂停。
> 5. 用户由管理员注销；

解决方法：

> 使用Redis存储Token

将 token 存入**内存数据库**：将 token 存入 DB 中，redis 内存数据库在这里是是不错的选择。如果需要让某个 token 失效就直接从 redis 中删除这个 token 即可。但是，这样会导致每次使用 token 发送请求都要先从 DB 中查询 token 是否存在的步骤，而且违背了 JWT 的无状态原则

> 附：
>
> [JWT官方文档](https://jwt.io/introduction)
>
> [Redis官方文档](https://redis.io/docs/)
>
> [Shrio官方文档](https://shiro.apache.org/documentation.html)

