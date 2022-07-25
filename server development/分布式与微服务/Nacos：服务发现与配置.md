Nacos Quick start

> 什么是Nacos

一个更易于构建云原生应用的动态服务发现、配置管理和服务管理平台

**逻辑架构及其组件**

![nacos-logic.jpg](https://cdn.nlark.com/yuque/0/2022/png/25601973/1646715315872-7ee3679a-e66e-49e9-ba9f-d24168a86c14.png)

> 作用

Nacos就是注册中心+配置中心，即`Nacos = Eureka + Config + Bus`

+ 替代Eureka做服务注册重信
+ 替代Config做服务配置中心

## Nacos下载安装运行

1. [Github下载地址](https://github.com/alibaba/nacos/releases/tag/2.1.0)，下载后解压

2. 进入nacos/bin目录，运行nacos

   ```
   cd nacos/bin
   startup.cmd -m standalone
   ```

3. 访问Nacos可视化界面地址（用户名和密码均为nacos）：`localhost:8848/nacos`

> **注：Naos2.0版本部署需要注意端口占用问题**

![image-20220713233932555](https://s2.loli.net/2022/07/13/UBTsWpgv823lmOR.png)



## Nacos替代Eureka作为服务注册中心

### 服务提供者注册

1. 新建Springboot工程`cloudalibaba-provider-payment9001`，`pom.xml`导入依赖（注意版本）

   ```xml
   <dependency>
       <groupId>com.alibaba.cloud</groupId>
       <artifactId>spring-cloud-alibaba-dependencies</artifactId>
       <version>2.2.7.RELEASE</version>
       <type>pom</type>
       <scope>import</scope>
   </dependency>
   
   <!--Springcloud alibaba nacos-->
   <dependency>
       <groupId>com.alibaba.cloud</groupId>
       <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
       <version>2.0.3.RELEASE</version>
   </dependency>
   ```

2. Springboot配置文件**`application.yml`**

   ```yaml
   server:
     port: 9001
   spring:
     application:
       name: nacos-provider
     cloud:
       nacos:
         discovery:
           server-addr: 127.0.0.1:8848
   ```

3. 下载对应版本Nacos，启动并登录控制台 localhost:8848/nacos，进入==服务管理->服务列表==，可以看到nacos-provider

4. `NacosProviderApplication`

   ```java
   package top.loki;
   import org.springframework.beans.factory.annotation.Value;
   import org.springframework.boot.SpringApplication;
   import org.springframework.boot.autoconfigure.SpringBootApplication;
   import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
   import org.springframework.web.bind.annotation.PathVariable;
   import org.springframework.web.bind.annotation.RequestMapping;
   import org.springframework.web.bind.annotation.RequestMethod;
   import org.springframework.web.bind.annotation.RestController;
   
   /**
    * @author oliverloki
    * @Description: TODO
    * @date 2022年07月14日 0:40
    */
   @SpringBootApplication
   @EnableDiscoveryClient
   public class NacosProviderApplication {
   
       public static void main(String[] args) {
           SpringApplication.run(NacosProviderApplication.class,args);
       }
   
       @RestController
       class EchoController{
           @Value("${server.port}")
           private String serverPort;
   		//返回当前服务的端口号
           @RequestMapping(value = "/echo", method = RequestMethod.GET)
           public String echo() {
               return "Nacos serverPortId: " + serverPort;
           }
       }
   }
   ```

至此，Nacos服务提供者注册完成

### 服务消费者注册与负载均衡demo

**消费者模块的开发与注册**

1. Ribbon配置

   ```java
   package top.loki.config;
   
   import org.springframework.cloud.client.loadbalancer.LoadBalanced;
   import org.springframework.context.annotation.Bean;
   import org.springframework.context.annotation.Configuration;
   import org.springframework.web.client.RestTemplate;
   
   /**
    * @author oliverloki
    * @Description: TODO
    * @date 2022年07月14日 1:52
    */
   @Configuration
   public class NacosConsumerApplication {
       @Bean
       @LoadBalanced
       public RestTemplate getResttemplate(){
           return new RestTemplate();
       }
   }
   
   ```

2. application.yaml

   ```yaml
   server:
     port: 83
   spring:
     application:
       name: nacos-consumer
     cloud:
       nacos:
         discovery:
           server-addr: localhost:8848
   service-url:
     nacos-user-services: http://nacos-provider
   ```

3. TestController

   ```java
   package top.loki;
   
   import org.springframework.beans.factory.annotation.Value;
   import org.springframework.boot.SpringApplication;
   import org.springframework.boot.autoconfigure.SpringBootApplication;
   import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
   import org.springframework.web.bind.annotation.GetMapping;
   import org.springframework.web.bind.annotation.RestController;
   import org.springframework.web.client.RestTemplate;
   
   import javax.annotation.Resource;
   
   @SpringBootApplication
   @EnableDiscoveryClient
   public class ConsumerApplication83 {
       public static void main(String[] args) {
           SpringApplication.run(ConsumerApplication83.class, args);
       }
   
       @RestController
       class TestController{
           @Resource
           private RestTemplate restTemplate;
           @Value("${service-url.nacos-user-services}")
           private String serverURL;
   
           @GetMapping("/providerApi")
           public String paymentInfo(){
   
               return restTemplate.getForObject(serverURL+"/echo",String.class);
           }
       }
   }
   ```

至此，consumer模块开发完成并注册进Nacos



为了达到负载均衡的效果，需要再建立运行在不同端口的多个provider服务，如下所示

+ `cloudalibaba-provider-payment9001`
+ `cloudalibaba-provider-payment9002`
+ `cloudalibaba-provider-payment9003`

> 注：测试环境可以采用**拷贝虚拟端口映射**来搭建服务集群避免重复劳动（自行Google）
>

![image-20220714011834281](https://s2.loli.net/2022/07/14/3gYikvrJsLXUMRV.png)

> 负载均衡效果：访问 `localhost:83//providerApi`，返回`"Nacos serverPortId: " + [不同的provider serverPortId]`

## Nacos替代Config作为服务配置中心

**前提条件：需要先下载 Nacos 并启动 Nacos server**

1. 添加依赖

   ```xml
   <dependency>
       <groupId>com.alibaba.cloud</groupId>
       <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
       <version>${latest.version}</version>
   </dependency>
   ```

2. bootstrap.yaml

   ```yaml
   spring:
     profiles:
       active: dev #表示开发环境
     cloud:
       nacos:
         discovery:
           server-addr: localhost:8848 #Nacos服务注册中心地址
         config:
           server-addr: localhost:8848 #Nacos服务配置中心地址
           file-extension: yaml #指定yaml格式的配置
     application:
       name: nacos-config-client
   server:
     port: 3377
   #根据传给nacos的配置文件的命名规则，dataId为    nacos-config-client-dev.yaml
   ```

3. 通过 Spring Cloud 原生注解 `@RefreshScope` 实现配置自动更新

   ```java
   @SpringBootApplication
   public class NacosConfigApplication3377 {
       public static void main(String[] args) {
           SpringApplication.run(NacosConfigApplication3377.class, args);
       }
       
       @RestController
       @RequestMapping("/config")
       @RefreshScope
       class ConfigController {
           @Value("${loki.name}")
           private boolean name;
           @RequestMapping("/get")
           public boolean get() {
               return name;
           }
       }
   }
   ```

4. 登录Nacos控制台，在 配置管理->配置列表->点击+号新建配置，本例中tableId为`nacos-config-client-dev.yaml`，配置内容如下

   > 在 Nacos Spring Cloud 中，`dataId` 的命名规则如下

   ```plain
   ${prefix}-${spring.profiles.active}.${file-extension}
   ```

   - `prefix` 默认为 `spring.application.name` 的值，也可以通过配置项 `spring.cloud.nacos.config.prefix`来配置。
   - `file-exetension` 为配置内容的数据格式，可以通过配置项 `spring.cloud.nacos.config.file-extension` 来配置。目前只支持 `properties` 和 `yaml` 类型。

   + `spring.profiles.active` 即为当前环境对应的 profile，详情可以参考 [Spring Boot文档](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-profiles.html#boot-features-profiles)。 **注意：当 `spring.profiles.active` 为空时，对应的连接符 `-` 也将不存在，dataId 的拼接格式变成 `${prefix}.${file-extension}`**

   ```yaml
   loki:
       name: oliverloki
   ```

5. 运行 `NacosConfigApplication`，调用 `curl http://localhost:8080/config/get`，返回内容是 `true`

6. 再次调用 [Nacos Open API](https://nacos.io/zh-cn/docs/open-api.html) 向 Nacos server 发布配置：dataId 为`example.properties`，内容为`useLocalCache=false`

   ```
   curl -X POST "http://127.0.0.1:8848/nacos/v1/cs/configs?dataId=example.properties&group=DEFAULT_GROUP&content=useLocalCache=false"
   ```

7. 再次访问 `http://localhost:8080/config/get`，此时返回内容为`false`，说明程序中的`useLocalCache`值已经被动态更新了



## 配置中心分类管理

网上说烂的两个问题

1. **如果实际开发中，通常一个系统会准备，dev开发环境，test测试环境，prod生产环境，那如何保证指定环境启动时服务能正确读取到Nacos上相应环境的配置文件呢？**
2. **一个大型分布式微服务系统会有很多微服务子项目，每个微服务项目又都会有相应的开发环境、测试环境、预发环境、正式环境…那么怎么对这些微服务配置进行管理呢？**

直接进入主题，`Nacos`有分类管理的操作。抛出三个概念，**namespace(命令空间)、group(分组)、dataid。**

> DataId

+ Nacos 中的某个配置集的 ID。配置集 ID 是组织划分配置的维度之一。Data ID 通常用于组织划分系统的配置集。一个系统或者应用可以包含多个配置集，每个配置集都可以被一个有意义的名称标识。Data ID 通常采用类 Java 包（如 com.taobao.tc.refund.log.level）的命名规则保证全局唯一性。此命名规则非强制。==这个概念来自于官方文档，说人话就是配置文件的名字，相当于主键的作用==

通过application.yaml中的`spring.profile.active`属性和Nacos配置文件的 `DataId`，可以让不同环境读取不同配置

> Group分组

**group(分组)**: Nacos 中的一组配置集，是组织配置的维度之一。通过一个有意义的字符串（如 Buy 或 Trade ）对配置集进行分组，从而区分 Data ID 相同的配置集。当您在 Nacos 上创建一个配置时，如果未填写配置分组的名称，则配置分组的名称默认采用 DEFAULT_GROUP 。配置分组的常见场景：不同的应用或组件使用了相同的配置类型，如 database_url 配置和 MQ_topic 配置。==说人话，就是可以分组，不同的系统或微服务的配置文件可以放在一个组里。比如用户系统和订单系统的配置文件都可以放在同个组中。==

1. 在nacos配置中心，新建配置，并且可以修改分组，eg: TEST_GROUP
2. bootstrap.yaml

> Namespace

+ **命名空间(namespace)**: 用于进行租户粒度的配置隔离。不同的命名空间下，可以存在相同的 Group 或 Data ID 的配置。==Namespace 的常用场景之一是不同环境的配置的区分隔离，例如开发测试环境和生产环境的资源（如配置、服务）隔离等==

![image-20220715230352717](https://s2.loli.net/2022/07/15/bhkHtVaSExCnvg7.png)



> 示例

```yaml
spring:
  profiles:
    active: dev #表示开发环境
  cloud:
    nacos:
   	  group: TEST_GROUP  #组名
      config:
        server-addr: localhost:8848 #Nacos服务配置中心地址
        file-extension: yaml #指定yaml格式的配置
      namespace: 7c03f343-6482-4930-b9dc-82b824be6dd9 #命名空间id，在nacos管理界面获取
  application:
    name: nacos-config-client
server:
  port: 3377
```

## Nacos使用MySQL做持久化

> **Nacos自带嵌入式数据库 derby ，可通过以下配置切换到MySQL，使用内置数据源无需进行任何配置。生产使用建议至少主备模式，或者采用高可用数据库。**

**`docker安装MySQL8.0.23`**

```
docker run --name nacos_mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=root -d mysql:8.0.23
```

```
#查看MySQL的服务器地址，填写进MySQL连接URL
docker inspect mysql | grep IPAddress
```

1. `\Nacos\nacos-server-2.0.3\nacos\conf`下找到 `nacos-mysql.sql` ，执行这个sql脚本

2. `\Nacos\nacos-server-2.0.3\nacos\conf`下找到 `application.properties` 增加mysql数据源配置，添加mysql数据源的url、用户名和密码

   ```properties
   spring.datasource.platform=mysql
   db.num=1
   db.url.0=
   db.user=
   db.password=
   ```

再以单机模式启动nacos，nacos所有写嵌入式数据库的数据都写到了mysql，可以尝试新增一个配置，然后再MySQL中查看变化



## Linux下搭建Nacos集群

推荐用户把所有服务列表放到一个virtual ip下面，然后挂到一个域名下面

![image-20220716003721693](https://s2.loli.net/2022/07/16/7phmn1BsSlcuHX8.png)



**预备环境**

+ 64 bit OS Linux/Unix/Mac，推荐使用Linux系统
+ Nginx ，MySQL ，JDK 8+ ，Maven 3.2.x+；
+ 3个或3个以上Nacos节点才能构成集群

**步骤**

1. 下载[nacos-server-2.0.3.tar.gz](https://github.com/alibaba/nacos/releases/download/2.0.3/nacos-server-2.0.3.tar.gz)，解压，进入安装目录

2. Linux环境下配置MySQL，jdk环境（可用docker）

3. 初始化数据库，执行 `nacos/conf/nacos_mysql.sql`

4. 修改`conf/application.properties`文件，增加支持mysql数据源配置（目前只支持mysql），添加mysql数据源的url、用户名和密码

   ```
   ### If use MySQL as datasource:
   spring.datasource.platform=mysql
   
   ### Count of DB:
   db.num=1
   
   ### Connect URL of DB:
   db.url.0=jdbc:mysql://192.168.103.99:3306/nacos_config?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true&useUnicode=true&useSSL=false&serverTimezone=UTC
   db.user.0=root
   db.password.0=root
   ```

5. nacos集群配置 cluster.conf

   这样就说明端口号1111，2222，3333的nacos服务构成了一个集群

   ```
   #example
       192.168.103.99:1111
       192.168.103.99:2222
       192.168.103.99:3333
   ```

6. 启动nacos集群

   ```
   ./startup.sh -p 1111
   ./startup.sh -p 2222
   ./startup.sh -p 3333
   ```

   ```
   [root@centos bin]# ./startup.sh -p 1111
   /usr/java/jdk-11.0.15/bin/java   -server -Xms2g -Xmx2g -Xmn1g -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=320m -XX:-OmitStackTraceInFastThrow -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/opt/nacos/logs/java_heapdump.hprof -XX:-UseLargePages -Dnacos.member.list= -Xlog:gc*:file=/opt/nacos/logs/nacos_gc.log:time,tags:filecount=10,filesize=102400 -Dloader.path=/opt/nacos/plugins/health,/opt/nacos/plugins/cmdb -Dnacos.home=/opt/nacos -jar /opt/nacos/target/nacos-server.jar  --spring.config.additional-location=file:/opt/nacos/conf/ --logging.config=/opt/nacos/conf/nacos-logback.xml --server.max-http-header-size=524288
   nacos is starting with cluster
   nacos is starting，you can check the /opt/nacos/logs/start.out
   [root@centos bin]# ./startup.sh -p 2222
   /usr/java/jdk-11.0.15/bin/java   -server -Xms2g -Xmx2g -Xmn1g -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=320m -XX:-OmitStackTraceInFastThrow -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/opt/nacos/logs/java_heapdump.hprof -XX:-UseLargePages -Dnacos.member.list= -Xlog:gc*:file=/opt/nacos/logs/nacos_gc.log:time,tags:filecount=10,filesize=102400 -Dloader.path=/opt/nacos/plugins/health,/opt/nacos/plugins/cmdb -Dnacos.home=/opt/nacos -jar /opt/nacos/target/nacos-server.jar  --spring.config.additional-location=file:/opt/nacos/conf/ --logging.config=/opt/nacos/conf/nacos-logback.xml --server.max-http-header-size=524288
   nacos is starting with cluster
   nacos is starting，you can check the /opt/nacos/logs/start.out
   [root@centos bin]# ./startup.sh -p 3333
   /usr/java/jdk-11.0.15/bin/java   -server -Xms2g -Xmx2g -Xmn1g -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=320m -XX:-OmitStackTraceInFastThrow -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/opt/nacos/logs/java_heapdump.hprof -XX:-UseLargePages -Dnacos.member.list= -Xlog:gc*:file=/opt/nacos/logs/nacos_gc.log:time,tags:filecount=10,filesize=102400 -Dloader.path=/opt/nacos/plugins/health,/opt/nacos/plugins/cmdb -Dnacos.home=/opt/nacos -jar /opt/nacos/target/nacos-server.jar  --spring.config.additional-location=file:/opt/nacos/conf/ --logging.config=/opt/nacos/conf/nacos-logback.xml --server.max-http-header-size=524288
   nacos is starting with cluster
   nacos is starting，you can check the /opt/nacos/logs/start.out
   ```

7. Nginx配置

   ```
   	upstream cluster{
   		server 127.0.0.1:1111;
           server 127.0.0.1:2222;
           server 127.0.0.1:3333;      
       }
   
       server {
       
           listen       80;
           server_name  localhost;
           
           location / {
               proxy_pass http://cluster;
           }
       }
   
   ```

8. 访问 ip/nacos，即可直接访问nacos管理页面

![image-20220721044012603](https://s2.loli.net/2022/07/21/68F3ZpdDHVkUweE.png)

> 注
>
> + nacos 2.0.3不支持jdk11
>
>   + 装jdk8
>   + 修改startup.sh
>
> + 如果内存不足，可以修改nacos的 startup.sh 参数
>
> + 查看Nacos服务启动的数量
>
>   ```
>   ps -ef | grep nacos |grep -v grep|wc -l
>   ```
>



































