> **SpringCloud版本说明**

+ 版本限制说明（需要借助json解析工具）：`start.spring.io/actuator/info`
+ SpringCloud官方推荐当前cloud版本适合的Springboot版本（详见Reference Doc）：https://spring.io/projects/spring-cloud#learn
+ SpringCloud Alibaba组件版本说明：https://github.com/alibaba/spring-cloud-alibaba/wiki/版本说明

+ **服务注册与发现**

  | 名称    | 描述 | 满足的CAP理论 |          |
  | ------- | ---- | ------------- | -------- |
  | Eureka  |      | AP            | 停止维护 |
  | `Nacos` |      |               | 推荐使用 |
  | Consul  |      | CP            |          |

+ **服务调用**

  | 名称      | 描述                   | 满足的CAP理论 |          |
  | --------- | ---------------------- | ------------- | -------- |
  | Ribbon    |                        | AP            | 停止维护 |
  | Feign     |                        | CP            |          |
  | OpenFeign | Netfilx新出的替代feign |               |          |
  
+ **服务熔断降级**

  | 名称         | 描述                        | 满足的CAP理论 |                            |
  | ------------ | --------------------------- | ------------- | -------------------------- |
  | Hystrix      |                             | AP            | 停止维护，但是国内使用较多 |
  | resilience4j |                             |               | 国外使用较多               |
  | `Sentinel`   | Springcloud Alibaba自研框架 | CP            |                            |

+ **服务网关**

  | 名称      | 描述 | 满足的CAP理论 |          |
  | --------- | ---- | ------------- | -------- |
  | zuul      |      | AP            | 停止维护 |
  | `GateWay` |      | CP            |          |

+ **服务分布式配置**

  | 名称    | 描述 | 满足的CAP理论 |                 |
  | ------- | ---- | ------------- | --------------- |
  | Config  |      | AP            | 逐渐被nacos替代 |
  | `Nacos` |      |               | 推荐使用        |

+ 服务总线

  | 名称    | 描述 | 满足的CAP理论 |             |
  | ------- | ---- | ------------- | ----------- |
  | Bus     |      | AP            | 被Nacos替换 |
  | `Nacos` |      |               | 推荐使用    |

+ 服务开发
  
  + `Springboot`

![image-20220317222303175](https://s2.loli.net/2022/07/13/7FgZxCRjdHlSpNU.png)



> [SpringCloud Alibaba 版本说明](github.com/alibaba/spring-cloud-alibaba/wiki/版本说明)

2022/7/13版本依赖推荐如下所示

| Spring Cloud Alibaba Version      | Spring Cloud Version        | Spring Boot Version |
| --------------------------------- | --------------------------- | ------------------- |
| 2.2.8.RELEASE*                    | Spring Cloud Hoxton.SR12    | 2.3.12.RELEASE      |
| 2.2.7.RELEASE                     | Spring Cloud Hoxton.SR12    | 2.3.12.RELEASE      |
| 2.2.6.RELEASE                     | Spring Cloud Hoxton.SR9     | 2.3.2.RELEASE       |
| 2.1.4.RELEASE                     | Spring Cloud Greenwich.SR6  | 2.1.13.RELEASE      |
| 2.2.1.RELEASE                     | Spring Cloud Hoxton.SR3     | 2.2.5.RELEASE       |
| 2.2.0.RELEASE                     | Spring Cloud Hoxton.RELEASE | 2.2.X.RELEASE       |
| 2.1.2.RELEASE                     | Spring Cloud Greenwich      | 2.1.X.RELEASE       |
| 2.0.4.RELEASE(停止维护，建议升级) | Spring Cloud Finchley       | 2.0.X.RELEASE       |
| 1.5.1.RELEASE(停止维护，建议升级) | Spring Cloud Edgware        | 1.5.X.RELEASE       |

> **Loki目前使用的版本（自用，保证可靠性，均来自官方推荐）**

+ Spring Cloud Hoxton.SR12
  + Gateway 
+ Springboot 2.3.12.RELEASE
+ cloud alibaba 2.2.7.RELEASE
  + Nacos Version：2.0.3
  + Sentinel Version：1.8.1
  + RocketMQ Version：4.6.1
+ JDK 11



`pom.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>

<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <groupId>top.loki</groupId>
  <artifactId>SpringCloudStudy</artifactId>
  <version>v1.0</version>
  <packaging>pom</packaging>


  <modules>
    <module>cloud-provider-payment8001</module>
  </modules>

  <!-- 统一管理jar包版本 -->
  <properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <maven.compiler.source>11</maven.compiler.source>
    <maven.compiler.target>11</maven.compiler.target>
    <log4j.version>1.2.17</log4j.version>
    <lombok.version>1.16.18</lombok.version>
    <mysql.version>8.0.23</mysql.version>
    <druid.version>1.1.16</druid.version>
    <mybatis-plus-boot-starter.version>3.5.1</mybatis-plus-boot-starter.version>
  </properties>

  <!-- dependencyManagement提供作用:子模块继承之后,指定版本+子modlue不用写groupId和version  -->
  <dependencyManagement>
    <dependencies>
      <!--spring boot 2.3.12.RELEASE-->
      <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-dependencies</artifactId>
        <version>2.3.12.RELEASE</version>
        <type>pom</type>
        <scope>import</scope>
      </dependency>
      <!--spring cloud Hoxton.SR12-->
      <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-dependencies</artifactId>
        <version>Hoxton.SR12</version>
        <type>pom</type>
        <scope>import</scope>
      </dependency>
      <!--spring cloud alibaba 2.1.0.RELEASE-->
      <dependency>
        <groupId>com.alibaba.cloud</groupId>
        <artifactId>spring-cloud-alibaba-dependencies</artifactId>
        <version>2.2.7.RELEASE</version>
        <type>pom</type>
        <scope>import</scope>
      </dependency>

      <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>${mysql.version}</version>
      </dependency>

      <dependency>
        <groupId>com.alibaba</groupId>
        <artifactId>druid</artifactId>
        <version>${druid.version}</version>
      </dependency>

      <dependency>
        <groupId>com.baomidou</groupId>
        <artifactId>mybatis-plus-boot-starter</artifactId>
        <version>${mybatis-plus-boot-starter.version}</version>
      </dependency>

      <dependency>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <version>${junit.version}</version>
      </dependency>
      <dependency>
        <groupId>log4j</groupId>
        <artifactId>log4j</artifactId>
        <version>${log4j.version}</version>
      </dependency>

      <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <version>${lombok.version}</version>
        <optional>true</optional>
      </dependency>
    </dependencies>

  </dependencyManagement>

  <build>
    <plugins>
      <plugin>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-maven-plugin</artifactId>
        <configuration>
          <fork>true</fork>	
          <addResources>true</addResources>
        </configuration>
      </plugin>
    </plugins>
  </build>

</project>
```







> **关于微服务开发的一些点**

+ 约定 > 配置 > 编码
+ 一个新模块的诞生
  1. 建moudle		
  2. 修改pom.xml
  3. 编写application.yaml
  4. 写业务代码





