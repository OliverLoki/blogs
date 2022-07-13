-javaagent:D:\\IDE\\IntelliJ IDEA 2022.1\\ja-netfilter.v3\\ja-netfilter.v3\\ja-netfilter.jar

> **SpringCloud版本说明**

+ 版本限制说明（需要借助json解析工具）：`start.spring.io/actuator/info`
+ SpringCloud官方推荐当前cloud版本适合的boot版本（详见Reference Doc）：https://spring.io/projects/spring-cloud#learn

> **SpringCloud组件停更说明与技术选型**

+ 服务注册与发现
  + Eureka：AP，停止维护
  + `Nacos`：推荐使用
  + Consul：CP
+ 服务调用
  + Ribbon：也逐渐停止维护
  + LoadBalancer：
  + Feign：停止维护
  + OpenFeign：Netfilx新出的替代feign
+ 服务熔断降级
  + Hystrix：停止维护，但是国内使用较多
  + resilience4j：国外使用较多
  + `Sentinel`：Springcloud Alibaba自研框架，
+ 服务网关
  + zuul：停更
  + `gateway`：
+ 服务分布式配置
  + Config：逐渐被nacos替代
  + `Nacos`
+ 服务总线
  + Bus：已被Nacos替换
  + `Nacos`
+ 服务开发
  + `Springboot`

![image-20220317222303175](https://s2.loli.net/2022/07/13/7FgZxCRjdHlSpNU.png)



​		

+ Springboot 2.2.2RELEASE	
+ cloud alibaba 2.1.0RELEASE

+ java 8

> **关于微服务开发的一些点**

+ 约定 > 配置 > 编码
+ 一个新模块的诞生
  1. 建moudle		
  2. 修改pom.xml
  3. 编写application.yaml
  4. 写业务代码

## 一、服务注册与发现





## 二、服务负载与调用





## 三、服务熔断降级





## 四、服务网关





## 五、服务分布式配置





## 六、服务开发







































