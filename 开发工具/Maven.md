@[TOC]



> 为什么需要Maven？

+ 实际开发中需要下载大量的第三方jar包并手动导入，为了简化这个步骤，Maven就是自动下载并导入配置jar包的工具
+ Maven还有一个功能是可以自动导入一个jar包所依赖的其他jar包

## 一、Maven环境搭建

> Maven 是一个基于 Java 的工具，所以要做的第一件事情就是安装 JDK

### 下载

|  系统   |             包名              |
| :-----: | :---------------------------: |
| Windows |  apache-maven-3.3.9-bin.zip   |
|  Linux  | apache-maven-3.3.9-bin.tar.gz |
|   Mac   | apache-maven-3.3.9-bin.tar.gz |

> [官网](https://maven.apache.org/)
>
> [阿里云镜像站](https://developer.aliyun.com/mirror/maven)

![img](https://img-blog.csdnimg.cn/20190904194557366.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2E4MDU4MTQwNzc=,size_16,color_FFFFFF,t_70)



### 配置环境变量

 先在系统变量里配置

 ![在这里插入图片描述](https://img-blog.csdnimg.cn/20210408175140730.png#pic_center)


 然后配置path环境变量
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210408175204148.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L05pZ2h0X19icmVlemU=,size_16,color_FFFFFF,t_70#pic_center)

win+R 运行cmd 输入 mvn -version，如图所示则配置成功

![在这里插入图片描述](https://img-blog.csdnimg.cn/20190904194650409.png)

### Maven目录结构





### Maven本地仓库

这个仓库是为了存储从第三方下载的jar包

1. 在Maven根目录下新增maven-rapo文件夹（即为本地仓库）

2. 在Maven根目录/conf/settings.xml文件下新增
```
 <localRepository>
 	这里写你的本地仓库目录的绝对地址（例如D:\Maven\apache-maven-3.6.3\maven-repo）
 </localRepository>
```

### 配置阿里云镜像

为什么需要这个？配置Maven时会下载大量的数据，Maven的服务器在国外。国内网络因为有墙的限制访问较慢。

`Maven//conf//setting.xml`

```xml
<id>nexus-aliyun</id>
<mirrorOf>central</mirrorOf>
<name>Nexus aliyun</name>
<url>http://maven.aliyun.com/nexus/content/groups/public</url>
```



## 二、Maven目录结构

### pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>

<!--Maven版本和头文件-->
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <!--这里是刚才配置的gav-->
  <groupId>github.oliverloki</groupId>
  <artifactId>javaweb-maven-01</artifactId>
  <version>1.0-SNAPSHOT</version>

  <!-- Package 打包的方式
  jar:Java应用
  war:javaWeb应用
  -->
  <packaging>war</packaging>

  <name>javaweb-maven-01 Maven Webapp</name>
  <!-- FIXME change it to the project's website -->
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





关于Maven父子工程的理解

+ 父项目中会有

```xml
<moudles>
	<moudle>servlet-01</moudle>
</moudles>
```

+ 子项目会有

  ```xml
  <parent>
          <artifactId>javaweb-02-Servlet</artifactId>
          <groupId>github.oliverloki</groupId>
          <version>1.0-SNAPSHOT</version>
      </parent>
  ```

+ 父项目中的jar包子项目可以直接使用，但是子项目的项目父项目不能使用，类似于java的多态

+ 父子工程示范目录

  ![在这里插入图片描述](https://img-blog.csdnimg.cn/20210408175525622.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L05pZ2h0X19icmVlemU=,size_16,color_FFFFFF,t_70#pic_center)






## 三、Idea与Maven的集成

1. Creat new project


![在这里插入图片描述](https://img-blog.csdnimg.cn/20210408175245337.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L05pZ2h0X19icmVlemU=,size_16,color_FFFFFF,t_70#pic_center)


3. 配置Maven的 "gav"

   1. Group   : 组id      io.github.oliverloki
   2. Artifactld : 项目名称    javaweb--maven
   3. Version : 版本

4. 配置地址
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210408175326297.png#pic_center)


5. 之后会下载很多东西，等待下载后出现下图说明配置成功

    ![在这里插入图片描述](https://img-blog.csdnimg.cn/20210408175347728.png#pic_center)


6. 观察Maven中的repo文件夹多了什么东西，**并且在Setting中Bulid Tools中检查一下MavenHome的地址**
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210408175416865.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L05pZ2h0X19icmVlemU=,size_16,color_FFFFFF,t_70#pic_center)



了解一下Maven的侧边栏操作
![在这里插入图片描述](https://img-blog.csdnimg.cn/2021040817544419.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L05pZ2h0X19icmVlemU=,size_16,color_FFFFFF,t_70#pic_center)

