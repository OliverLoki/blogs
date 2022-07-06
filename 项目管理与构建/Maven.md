> 实际开发中需要下载大量的第三方jar包并手动导入，为了简化这个步骤，Maven就是自动下载并导入配置jar包的工具，Maven甚至可以自动导入一个jar包所依赖的其他jar包

# 一、Maven快速上手



### 下载并配置环境变量

**注：Maven 是一个基于 Java 的工具，所以需要本机的JDK环境**

[官网下载地址](https://maven.apache.org/)

[阿里云镜像站地址](https://developer.aliyun.com/mirror/maven)

|  系统   |          下载的包名           |
| :-----: | :---------------------------: |
| Windows |  apache-maven-3.3.9-bin.zip   |
|  Linux  | apache-maven-3.3.9-bin.tar.gz |
|   Mac   | apache-maven-3.3.9-bin.tar.gz |

+ 添加系统变量

```
M2_HOME [Maven根目录/bin]
MAVEN_HOME [MAVEN跟目录]
```

 ![在这里插入图片描述](https://img-blog.csdnimg.cn/20210408175140730.png#pic_center)

+ 配置path环境变量
  ![在这里插入图片描述](https://img-blog.csdnimg.cn/20210408175204148.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L05pZ2h0X19icmVlemU=,size_16,color_FFFFFF,t_70#pic_center)

> **Window命令行输入 `mvn -version`，如图所示则配置成功**

![在这里插入图片描述](https://img-blog.csdnimg.cn/20190904194650409.png)



### 配置Maven本地仓库

这个仓库是为了存储从第三方下载的jar包

1. 在Maven根目录下新增maven-rapo文件夹（即为本地仓库）

2. 在Maven根目录`/conf/settings.xml`文件下新增
```xml
<localRepository>
 	这里写你的本地仓库目录的绝对地址（例如D:\Maven\apache-maven-3.6.3\maven-repo）
</localRepository>
```

### 配置阿里云镜像

为什么需要这个？配置Maven时会下载大量的数据，Maven的服务器在国外。国内网络因为有墙的限制访问较慢。

在Maven根目录`/conf/settings.xml`文件下配置

```xml
<id>nexus-aliyun</id>
<mirrorOf>central</mirrorOf>
<name>Nexus aliyun</name>
<url>http://maven.aliyun.com/nexus/content/groups/public</url>
```



# 二、关于pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>

<!--Maven版本和头文件-->
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <!--Maven坐标-->
  <groupId>github.oliverloki</groupId>
  <artifactId>javaweb-maven-01</artifactId>
  <version>1.0-SNAPSHOT</version>

  <!-- Package 打包的方式
      	jar:jar包
		war:SSM应用常用
		pom:
  -->
    
  <packaging>war</packaging>
    
  <name>javaweb-maven-01 Maven Webapp</name>
  <!-- 项目网址 -->
  <url>http://www.example.com</url>

  <!--配置 -->
  <properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <maven.compiler.source>1.7</maven.compiler.source>
    <maven.compiler.target>1.7</maven.compiler.target>
  </properties>
  <!--项目依赖-->
  <!--maven官网有这个项目依赖，其强大之处在于可以导入某个jar包所依赖的jar包-->
  <dependencies>
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>4.11</version>
      <scope>test</scope>
    </dependency>
  </dependencies>
</project>
```



父项目中的jar包子项目可以直接使用，但是子项目的项目父项目不能使用



# 三、Idea中使用Maven常见问题

## IDEA中MAVEN项目Dependency not found 问题

[IDEA中MAVEN项目Dependency not found 问题](https://blog.csdn.net/goQuesting/article/details/78422964)

