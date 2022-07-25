Gateway：Spring Cloud API网关组件

## 服务网关的必要性

+ 微服务背景下，一个系统被拆分为多个服务，但是像安全认证，流量控制，日志，监控等功能是每个服务都需要的，没有网关的话，我们就需要在每个服务中单独实现，这使得我们做了很多重复的事情并且没有一个全局的视图来统一管理这些功能

+ 在微服务架构中，一个系统往往由多个微服务组成，而这些服务可能部署在不同机房、不同地区、不同域名下。这种情况下，客户端（例如浏览器、手机、软件工具等）想要直接请求这些服务，就需要知道它们具体的地址信息，例如 IP 地址、端口号等								

**这种客户端直接请求服务的方式存在以下问题：**

+ 各个业务服务可以被独立的设计、开发、测试、部署和管理，各个独立部署单元可以用不同的开发测试团队维护，可以使用不同的编程语言和技术平台进行设计，这就要求必须使用一种语言和平台无关的服务协议作为各个单元间的通讯方
+ 当服务数量众多时，客户端需要维护大量的服务地址，这对于客户端来说，是非常繁琐复杂的。
+ 在某些场景下可能会存在跨域请求的问题
+ 身份认证的难度大，每个微服务需要独立认证

我们可以通过 API 网关来解决这些问题，API 网关就像整个微服务系统的门面一样，是系统对外的唯一入口。有了它，客户端会先将请求发送到 API 网关，然后由 API 网关根据请求的标识信息将请求转发到微服务实例

![image-20220724180425348](https://s2.loli.net/2022/07/24/oel8IESTmtwYRAG.png)

**使用 API 网关具有以下好处**

+ 客户端通过 API 网关与微服务交互时，客户端只需要知道 API 网关地址即可，而不需要维护大量的服务地址，简化了客户端的开发。
+ 客户端直接与 API 网关通信，能够减少客户端与各个服务的交互次数。
+ 客户端与后端的服务耦合度降低。
+ 节省流量，提高性能，提升用户体验。
+ API 网关还提供了安全、流控、过滤、缓存、计费以及监控等 API 管理功能

**常见的 API 网关实现方案主要有以下 5 种：**

- Spring Cloud Gateway
- ~~Spring Cloud Netflix Zuul~~
- Kong
- Nginx+Lua
- Traefik

## Gateway快速开始

`Spring Cloud Gateway` 旨在提供一种简单而有效的方式来路由到 API，并为它们提供横切关注点，例如：安全性、监控/指标和弹性

**特性**

+ 基于 `Spring 5.0`、`Spring Boot 2.0` 和 `Project Reactor` 等技术
+ Spring Cloud Gateway 是基于 WebFlux 框架实现的，WebFlux 框架底层则使用了高性能的 `reactor-netty` 响应式编程组件，底层使用了Netty通讯框架

### Spring Cloud Gateway 核心概念

Spring Cloud GateWay 最主要的功能就是路由转发，而在定义转发规则时主要涉及了以下三个核心概念，如下表。

| 核心概念          | 描述                                                         |
| ----------------- | ------------------------------------------------------------ |
| Route（路由）     | 网关最基本的模块。它由一个 ID、一个目标 URI、一组断言（Predicate）和一组过滤器（Filter）组成。 |
| Predicate（断言） | 路由转发的判断条件，我们可以通过 Predicate 对 HTTP 请求进行匹配，例如请求方式、请求路径、请求头、参数等，如果请求与断言匹配成功，则将请求转发到相应的服务。 |
| Filter（过滤器）  | 过滤器，我们可以使用它对请求进行拦截和修改，还可以使用它对上文的响应进行再处理。 |

> Gateway和Nginx

直接用 Nginx 做网关当然也可以，Nginx 也可以作为网关使用，高性能网关 OpenResty 就是在 Nginx 的基础上扩展而来

Spring Cloud Gateway 只是为了方便使用 Spring Cloud 技术栈的团队快速的实现网关功能而已

流量大：Nginx打向Gateway，否则Gateway扛不住



### 工作流程

Spring Cloud Gateway 工作流程说明如下：

1. 客户端将请求发送到 Spring Cloud Gateway 上。
2. Spring Cloud Gateway 通过 Gateway Handler Mapping 找到与请求相匹配的路由，将其发送给 Gateway Web Handler。
3. Gateway Web Handler 通过指定的过滤器链（Filter Chain），将请求转发到实际的服务节点中，执行业务逻辑返回响应结果。
4. 过滤器之间用虚线分开是因为过滤器可能会在转发请求之前（pre）或之后（post）执行业务逻辑。
5. 过滤器（Filter）可以在请求被转发到服务端前，对请求进行拦截和修改，例如参数校验、权限校验、流量监控、日志输出以及协议转换等。
6. 过滤器可以在响应返回客户端之前，对响应进行拦截和再处理，例如修改响应内容或响应头、日志输出、流量监控等。
7. 响应原路返回给客户端。
   总而言之，客户端发送到 Spring Cloud Gateway 的请求需要通过一定的匹配条件，才能定位到真正的服务节点。在将请求转发到服务进行处理的过程前后（pre 和 post），我们还可以对请求和响应进行一些精细化控制





### Predicate 断言示例

Spring Cloud Gateway 通过 Predicate 断言来实现 Route 路由的匹配规则。简单点说，Predicate 是路由转发的判断条件，请求只有满足了 Predicate 的条件，才会被转发到指定的服务上进行处理。

使用 Predicate 断言需要注意以下 3 点：

- Route 路由与 Predicate 断言的对应关系为“一对多”，一个路由可以包含多个不同断言。
- 一个请求想要转发到指定的路由上，就必须同时匹配路由上的所有断言。
- 当一个请求同时满足多个路由的断言条件时，请求只会被首个成功匹配的路由转发

常见的 Predicate 断言如下表（假设转发的 URI 为 http://localhost:8001）。

| 断言    | 示例                                                         | 说明                                                         |
| ------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| Path    | - Path=/dept/list/**                                         | 当请求路径与 /dept/list/** 匹配时，该请求才能被转发到 http://localhost:8001 上。 |
| Before  | - Before=2021-10-20T11:47:34.255+08:00[Asia/Shanghai]        | 在 2021 年 10 月 20 日 11 时 47 分 34.255 秒之前的请求，才会被转发到 http://localhost:8001 上。 |
| After   | - After=2021-10-20T11:47:34.255+08:00[Asia/Shanghai]         | 在 2021 年 10 月 20 日 11 时 47 分 34.255 秒之后的请求，才会被转发到 http://localhost:8001 上。 |
| Between | - Between=2021-10-20T15:18:33.226+08:00[Asia/Shanghai],2021-10-20T15:23:33.226+08:00[Asia/Shanghai] | 在 2021 年 10 月 20 日 15 时 18 分 33.226 秒 到 2021 年 10 月 20 日 15 时 23 分 33.226 秒之间的请求，才会被转发到 http://localhost:8001 服务器上。 |
| Cookie  | - Cookie=name,c.biancheng.net                                | 携带 Cookie 且 Cookie 的内容为 name=c.biancheng.net 的请求，才会被转发到 http://localhost:8001 上。 |
| Header  | - Header=X-Request-Id,\d+                                    | 请求头上携带属性 X-Request-Id 且属性值为整数的请求，才会被转发到 http://localhost:8001 上。 |
| Method  | - Method=GET                                                 | 只有 GET 请求才会被转发到 http://localhost:8001 上。         |

pom.xml

```xml
<!--特别注意：在 gateway 网关服务中不能引入 spring-boot-starter-web 的依赖，否则会报错--> 		
<!-- Spring cloud gateway 网关依赖-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-gateway</artifactId>
        </dependency>
```

> 在配置文件application.yaml中配置

```yaml
spring:
  cloud:
    gateway: #网关路由配置
      routes:
      - id: #路由id，没有固定规则，但要求唯一，建议配合服务名
        uri: #匹配后提供的服务路由地址
        predicates:
        - Cookie=mycookie,mycookievalue #断言，路径匹配的进行路由
```



## Spring Cloud Gateway 动态路由

默认情况下，Spring Cloud Gateway 会根据服务注册中心（例如 Nacos）中维护的服务列表，以服务名（`spring.application.name`）作为路径创建动态路由进行转发，从而实现动态路由功能



我们可以在配置文件中，将 Route 的 uri 地址修改为以下形式。

```
lb://service-name
```

以上配置说明如下：

- lb：uri 的协议，表示开启 Spring Cloud Gateway 的负载均衡功能。
- service-name：服务名，Spring Cloud Gateway 会根据它获取到具体的微服务地址。

