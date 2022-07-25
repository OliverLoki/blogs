> Sentinel以流量为切入点，从**限流、流量整形、熔断降级、系统负载保护、热点防护**等多个维度来帮助开发者保障微服务的稳定性
>
> 官网：https://github.com/alibaba/Sentinel/wiki/%E4%BB%8B%E7%BB%8D
>
> 网站：https://sentinelguard.io/
>
> 功能介绍：https://sentinelguard.io/zh-cn/docs/introduction.html

## 一、Sentinel简介

### Sentinel逻辑架构

Sentinel与[主流框架适配](https://github.com/alibaba/Sentinel/wiki/主流框架的适配)，例如与 Spring Cloud、Apache Dubbo、gRPC、Quarkus 的整合。您只需要引入相应的依赖并进行简单的配置即可快速地接入 Sentinel。同时 Sentinel 提供 Java/Go/C++ 等多语言的原生实现

![](https://user-images.githubusercontent.com/9434884/50505538-2c484880-0aaf-11e9-9ffc-cbaaef20be2b.png)

### 主要功能和设计理念

详见：https://sentinelguard.io/zh-cn/docs/introduction.html

+ [流量控制](https://github.com/alibaba/Sentinel/wiki/流量控制)：监控应用流量的 QPS 或并发线程数等指标，当达到指定的阈值时对流量进行控制，以避免被瞬时的流量高峰冲垮，从而保障应用的高可用性
+ [熔断降级](https://github.com/alibaba/Sentinel/wiki/熔断降级)
+ [热点参数限流](https://github.com/alibaba/Sentinel/wiki/热点参数限流)
+ [系统自适应限流](https://github.com/alibaba/Sentinel/wiki/系统自适应限流)
+ [网关流控](https://github.com/alibaba/Sentinel/wiki/网关限流)
+ [@SentinelResource 注解](https://github.com/alibaba/Sentinel/wiki/注解支持)

+ [生产环境使用 Sentinel](https://github.com/alibaba/Sentinel/wiki/在生产环境中使用-Sentinel)

![arch](https://sentinelguard.io/docs/zh-cn/img/sentinel-flow-overview.jpg)



> Sentinel 的使用可以分为两个部分

- 核心库（Java 客户端）：不依赖任何框架/库，能够运行于 Java 8 及以上的版本的运行时环境，同时对 Dubbo / Spring Cloud 等框架也有较好的支持（见 [主流框架适配](https://github.com/alibaba/Sentinel/wiki/主流框架的适配)）
- 控制台（Dashboard）：控制台主要负责管理推送规则、监控、集群限流分配管理、机器发现等Sentinel 的使用可以分为两个部分

## 二、Sentinel控制台

**获取方式**

+ 从 [release 页面](https://github.com/alibaba/Sentinel/releases) 下载最新版本的控制台 jar 包

+ 从最新版本的源码自行构建 Sentinel 控制台

**环境要求**

+ 确保本机Java8+环境
+ 本机8080端口不被占用

**访问Centinel控制台**

访问localhost:8080，初始登录用户名密码均为 Sentinel

```
java -jar sentinel-dashboard-1.8.1.jar
```

参数：

+ -Dsentinel.dashboard.auth.username=sentinel 用于指定控制台的登录用户名为 sentinel；
+ -Dsentinel.dashboard.auth.password=123456 用于指定控制台的登录密码为 123456；如果省略这两个参数，默认用户和密码均为 sentinel；
+ -Dserver.servlet.session.timeout=7200 用于指定 Spring Boot 服务端 session 的过期时间，如 7200 表示 7200 秒；60m 表示 60 分钟，默认为 30 分钟

> **Sentinel懒加载机制的说明：注册进Sentinel的服务，需要进行一次服务调用，才可以在Sentinel控制台中显示，也可以通过配置application.yaml修改**
>
> ```
> #服务启动直接建立心跳连接
> spring.cloud.sentinel.eager=true
> ```
>
> spring.cloud.sentinel.eager=true 可使 你的SpringCloud应用启动时，直接与Sentinel建立心跳连接,访问sentinel 控制台就可以看到服务连接情况

![image-20220722184718627](https://s2.loli.net/2022/07/22/E6RqUeaLGOXkCJV.png)



## 三、SpringCloud整合Sentinel实现流量控制、熔断降级、热点参数限流

pom.xml

版本问题请参考：[SpringCloud组件版本说明](https://github.com/alibaba/spring-cloud-alibaba/wiki/版本说明)

```xml
<dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <!--nacos 服务发现与Sentinel持久化配置-->
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
            <version>2.0.3.RELEASE</version>
        </dependency>
        <!--Sentinel 流量控制与服务熔断-->
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-sentinel</artifactId>
        </dependency>
        <!--Sentinel数据持久化-->
        <dependency>
            <groupId>com.alibaba.csp</groupId>
            <artifactId>sentinel-datasource-nacos</artifactId>
        </dependency>
        <!--openFeign 服务调用-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-openfeign</artifactId>
        </dependency>
    </dependencies>
```

application.yaml

```yaml
#指定端口号
server:
  port: 8401

spring:
  application:
    name: cloudalibaba-sentinel-service
  cloud:
    nacos:
      discovery:
        #Nacos服务注册中心地址
        server-addr: loaclhost:8848
    sentinel:
      transport:
        #配置sentinel控制台地址
        dashboard: localhost:8080
        #sentinel后台监控端口,默认启动在8719端口，若被占用则从8719开始向后查找
        port: 8719
management:
  endpoint:
    web:
      expsure:
        include: '*'
#激活centinel对feign的支持
feign:
  circuitbreaker:
    enabled: true
```

Controller.java

```java
package top.loki;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@EnableFeignClients
@EnableDiscoveryClient
@SpringbootApplication
public class SentinelServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(SentinelServiceApplication.class, args);
    }
}
```



**流量控制规则**

> **流量控制**

一条限流规则主要由下面几个因素组成，我们可以组合这些元素来实现不同的限流效果：

- `resource`：资源名，即限流规则的作用对象
- `count`: 限流阈值
- `grade`: 限流阈值类型（QPS 或并发线程数）
- `limitApp`: 流控针对的调用来源，若为 `default` 则不区分调用来源
- `strategy`: 调用关系限流策略
- `controlBehavior`: 流量控制效果（直接拒绝、Warm Up、匀速排队）

**点击流控规则->新增流控规则**

+ **资源名**：指资源访问路径 如 localhost:8080/test 中的 /test
+ **针对来源**：Sentine可以针对调用者进行限流，填写微服务名`spring.application.name`，默认default (不区分来源)
+ **阈值类型/单机阈值**：
  + QPS (每秒钟的请求数量)：当调用该api的QPS达到阈值的时候，进行限流
  + 线程数：当调用该api的线程数达到阈值的时候,进行限流
+ **是否集群**：不需要集群
+ **流控模式**：
  + 直接：api达到限流条件时，直接限流
  + 关联：当与A关联的资源B达到阈值时，就限流A自己（比如支付接口达到阈值，限流订单接口0）
  + 链路：只记录指定链路上的流糧(指定资源从入口资源进来的流量,如果达到阈值,就进行限流) [api级别的针对来源]
+ **流控效果**（阈值类型为QPS才有这个选项）：
  + 快速失败：直接失败，抛异常
  + Warm Up：根据codeFactor (冷加载因子，默认3)的值，从阈值/codeFactor，经过预热时长，达到设置的QPS阈值
  + 排队等待：匀速排队，让请求以匀速的速度通过，阈值类型必须设置为QPS，**否则无效**

```java
@RestController
class FlowLimitController{

    @GetMapping("/test1")
    public String test1(){

        return "流控测试1";
    }
    @GetMapping("/test2")
    public String test2(){

        return "流控测试2";
    }
    //热点参数限流
    @SentinelResource(value = "hotkey",blockHandler = "dealHandler")
    public String test2(){
        return "流控测试2";
    }
    public String dealHandler(){
        return "这里是处理热点代码的逻辑";
    }
}
```

服务调用与降级

```java
@FeignClient(value = "nacos-payment-provider",fallback = "")
public interface PaymentService {

    @GetMapping(value = "/paymentSQL/{id}")
    public commonResult<Payment> paymentSQL(@PathVariable("id") Long id);
}
```





## 四、网关流控

### Sentinel实现gateway网关服务接口限流

从 1.6.0 版本开始，Sentinel 提供了 Spring Cloud Gateway 的适配模块，可以提供两种资源维度的限流：

+ route 维度：即在 Spring 配置文件中配置的路由条目，资源名为对应的 routeId

+ 自定义 API 维度：用户可以利用 Sentinel 提供的 API 来自定义一些 API 分组

**基于SpringBoot再构建一个service-gateway网关模块**

pom.xml

```xml
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-sentinel</artifactId>
</dependency>

<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-alibaba-sentinel-gateway</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-gateway</artifactId>
</dependency>
```

## 五、Sentinel规则持久化

### 使用Nacos做配置中心进行规则存储

pom.xml 新增

```xml
<dependency>
    <groupId>com.alibaba.csp</groupId>
    <artifactId>sentinel-datasource-nacos</artifactId>
</dependency>
```

application.yaml

```yaml

```















