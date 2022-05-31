# javaweb基础

## 1、基本概念

> 动态web资源开发的技术统称为JavaWeb

+  引子：地址栏输入www.baidu.com

1. 它应该在哪?     www.baidu:某个端口号（进行负载均衡之类的操作）/index.html
2. 可以根据ping操作得到他的IP地址
3. 能访问到的任何一个页面或者资源，都存在于某一个计算机上
4. 掌握静态界面和动态界面的概念，动态界面需要什么技术？动态界面和静态界面各有什么优势和劣势

## 2、web服务器

### 2.1**流行的技术**

####  	 1) ASP

+ 基于C#
+ 微软
+ 国内最早流行的技术
+ 在HTML中嵌入了VB的脚本，ASP +COM？
+ 在ASP开发中，基本一个页面需要几千行，维护成本高，重构成本更高

#### 2) PHP

+ PHP开发速度快，功能强大，跨平台
+ 无法承载大访问量情况（局限性）
+ WordPress（开源的博客引擎）使用PHP写的

#### 3) JSP/Servelt

+ SUN公司主推的B/S架构服务器
+ 基于Java
+ 可以承载三高问题（高并发，高可用，高性能）带来的影响
+ 语法像ASP

###     2.2**常见的服务器**

#### 1) IIS 

1. Window自带的服务器

#### 2) Tomcat

1. 下载安装
2. 对目录要有一定的了解
3. 在idea中配置Tomcat
4. 核心配置文件
   1. Tomcat\apache-tomcat-8.5.63\conf\server.xml
5. 常见bug的解决（端口占用等）

> 我们访问    localhost:8080    端口出现了一个页面，这个过程中发生了什么呢

+ localhost可以修改吗？
  + 可以修改，需要在Windows底层和tomcat核心配置文件中修改主机名

```
C:\Windows\System32\drivers\etc\hosts
将127.0.0.1 对应的主机号改为 woshiyizhizhu
```

+ 8080端口可以修改吗？
  + 可以

> 一些默认端口号
>
> + tomcat：8080
>
> + mysql：3306
>
> + http：80
>
> + https：443
>
>   



## 3、Http

### 3.1、什么是HTTP

- 超文本传输协议（Hypertext Transfer Protocol，HTTP）是一个简单的请求-响应协议，它通常运行在[TCP](https://baike.baidu.com/item/TCP/33012)之上。

- http

  ​			默认端口：80

- https

  ​			默认端口： 443

### 3.2、两个时代

+ http1.0

  ​	**HTTP/1.0:客户端与web服务器连接后，只能获得一个web资源**

+ http2.0

  ​     **HTTP/1.1:客户端与web服务器连接后可以获得多个web资源**

### 3.3、Http请求（以百度为例）：

- **客户端---发请求-->服务器**

  ``` java
General:
  Request URL: https://www.baidu.com/    请求地址
  Request Method: GET       get方法/post方法           
  Status Code: 200 OK       状态码
  Remote Address: 127.0.0.1:7890       远程DNS地址
  
  ```
  
#### 1、请求行

  + **请求行中的请求方式**:Get,Post,HEAD,DELETE
  
    1. get：一次请求携带的参数有限制，会在URL地址栏显示数据内容，不安全，但是高效
    2. post:一次请求携带的参数没有限制，会在URL地址栏显示数据内容，安全，但不高效
    3. HEAD

> 面试题：简述GET和POST请求的异同点



#### 2、请求头

```java
  Accept //告诉浏览器支持的数据类型
  Accept-Encoding//告诉浏览器支持的编码格式
  Accept-Language//告诉浏览器支持的语言
Cache-Control //缓存控制
  Connection//告诉浏览器，请求完成是断开还是保持连接
HOST
```

  

  

  

### 3.4、Http响应（以百度为例）：

- **服务器---响应-->客户端**

  百度：

  ```java
  Cache-Control: private   //缓存控制
  Connection: keep-alive   //连接状态
  Content-Encoding: gzip   //编码
  Content-Type: text/html;charset=utf-8 //类型
  Date: Thu, 18 Mar 2021 03:38:11 GMT
  Expires: Thu, 18 Mar 2021 03:38:11 GMT
  Server: BWS/1.1
  Set-Cookie: BDSVRTM=279; path=/
  Set-Cookie: BD_HOME=1; path=/
  Set-Cookie: H_PS_PSSID=33272_33710_33594_33570_26350_22159; path=/; domain=.baidu.com
  Strict-Transport-Security: max-age=172800
  Traceid: 1616038691062356762612472098429264318577
  Transfer-Encoding: chunked
  X-Ua-Compatible: IE=Edge,chrome=1
  ```

  #### 1.**响应体**

  ```java
  Accept: text/html
  Accept-Encoding: gzip, deflate, br
  Accept-Language: zh-CN,zh;q=0.9
  Cache-Control: max-age=0
  Connection: keep-alive
  Location // 让网页重新定位
  Refresh // 告诉客户端多久刷新一次
  ```

  #### 3.响应状态码

  1. 200：请求响应成功
  2. 3 XX：请求重定向
  3. 404：找不到资源
  4. 5XX:服务器代码错误
     1. 502:网管错误

> 面试题：当浏览器从地址栏输入地址到页面展示出来经历了什么？
>

## 4、Maven的配置

### 4.1为什么需要Maven？

javaweb开发中需要大量的jar包，需要手动导入，为了简化这个步骤，Maven就是自动导入配置jar包的工具

### 4.2 Maven的下载

1. https://maven.apache.org/

2. 了解Maven的目录，配置文件

3. 配置环境变量

   1. ​	先在系统变量里配置

      ![image-20210322204258854](E:\typoradata\img\image-20210322204258854.png)

   2. 然后配置path环境变量

      ![image-20210322204450066](E:\typoradata\img\image-20210322204450066.png)

### 4.3 创建Maven本地仓库

1. 在Maven目录下新增maven-rapo文件夹

2. 在settings.xml文件下新增

```
 <localRepository>D:\Maven\apache-maven-3.6.3\maven-repo</localRepository>
```

### 4.4 Maven阿里云镜像

​		为什么需要这个？第一次配置Maven时会下载大量的数据，国内网络因为有墙的限制访问较慢，并且需要注意下载的位置

```xml
<id>nexus-aliyun</id>
<mirrorOf>central</mirrorOf>
<name>Nexus aliyun</name>
<url>http://maven.aliyun.com/nexus/content/groups/public</url>
```

### 4.5、在idea中使用Maven

1. Creat new project

2. ![image-20210323000121624](E:\typoradata\img\image-20210323000121624.png)

3. 配置Maven的 "gav"

   1. Group   : 组id      io.github.oliverloki
   2. Artifactld : 项目名称    javaweb--maven
   3. Version : 版本

4. 配置地址

   ![image-20210323134727886](E:\typoradata\img\image-20210323134727886.png)

5. 之后会下载很多东西，等待下载后出现下图说明配置成功

   1. ![image-20210323134822857](C:\Users\yu'chun\AppData\Roaming\Typora\typora-user-images\image-20210323134822857.png)

6. 观察Maven中的repo文件夹多了什么东西，**并且在Setting中Bulid Tools中检查一下MavenHome的地址**

   ![image-20210323142843729](E:\typoradata\img\image-20210323142843729.png)

7. 添加需要的文件夹（newdirectoty）

### 4.6 其他操作

1. 标记文件夹功能（idea的功能）

   

   ![image-20210323144456862](E:\typoradata\img\image-20210323144456862.png)

2. 了解一下Maven的侧边栏操作

### 4.7 pom.xml（Maven的核心配置文件）

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

 ### 4.8 Maven由于约定大于配置，会有一些bug

1. ​	![image-20210323172834652](E:\typoradata\img\image-20210323172834652.png)

### 4.9  关于Maven父子工程的理解

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

  ![image-20210324165037766](E:\typoradata\img\image-20210324165037766.png)



### 4.10 Maven环境优化

1. 修改xml为最新版本
2. 将maven的目录结构补充完整

## 5、Servlet

### 5.1、Servlet简介

+ sun公司开发的一种web技术
+ 编写一个Servlet小程序只需要两个步骤
  1. 编写一个类实现Servlet接口
  2. 把开发好的Java类部署到服务器中

+ Servlet接口sun公司有两个默认的实现类
  1. HttpServlet
  2. GenericServlet

+ 编写第一个servlet**一个类继承HttpServlet并且重写其doGet和doPost方法**

```java
public class helloservlet extends HttpServlet {

    //由于get和post只是请求实现的不同方式，可以相互调用，因为其业务逻辑都一样
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        PrintWriter writer = resp.getWriter(); //响应流
        writer.print("hello world");

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }
}


```

​        	**重写方法的底层实现**

![image-20210324184403880](E:\typoradata\img\image-20210324184403880.png)



+ **编写servlet的映射**

1. 为什么需要映射，我们写的是java程序，但是需要浏览器访问，而浏览器需要连接web服务器，所以我们需要在web服务器中注册我们写的servlet，还需要一个浏览器可以访问的路径




```xml
<!--注册servlet-->
    <servlet>
        <servlet-name>hello</servlet-name>
        <servlet-class>github.oliverloki.servlet.helloservlet</servlet-class>
    </servlet>
    <!--Servlet的请求路径-->
    <servlet-mapping>
        <servlet-name>hello</servlet-name>
        <url-pattern>/hello</url-pattern>
    </servlet-mapping>

```

+ **使用servlet3.0创建mysql时，可能会出现注解无效的问题**

1. 解决方法：尝试删除 web.xml 的顶层标签的**metadata-complete** 属性

+ **mapping**

1. 一个Servlet可以指定一个映射路径

2. 一个Servlet可以指定多个映射路径

3. 一个Serclet可以指定通用映射路径

   ```xml
    <url-pattern>/hello</url-pattern>
   ```

### 5.2 Servlet常用对象

#### 5.2.1、HttpServletContext

+ web容器在启动的时候，会为每个web程序都创建一个对应的ServletContext对象，它代表了当前的web应用

+ 应用(实际开发中几乎不会用到了)：

  **1、共享数据**

  ```java
  
  //创建ServletContext对象类
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
  
  
          //this.getInitParameter()   初始化参数
          //this.getServletConfig()   servlet配置
          //this.getServletContext()  servlet上下文
          ServletContext context = this.getServletContext();
          String username = "wangzhe";
          context.setAttribute("username",username);//将一个数据保存在了servletcontext中
  
      }
  ```

  ```java
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
          //
          ServletContext servletContext = this.getServletContext();
  
          String username = (String) servletContext.getAttribute("username");
          response.setContentType("text/html");
          response.setCharacterEncoding("utf-8");
          response.getWriter().print(username);
  
  
      }
  }
  
  ```

  **2、初始化参数**

  ```xml
  <!--在webxml中配置一些初始化参数-->
  <context-param>
  	<param-name>url</param-name>
  	<param-value>jdbc:mysql://localhost:3306/mybatis</param-value>
  </context-param>
  
  ```

  **3、请求转发**

  

  **4、读取资源文件**

#### 5.2.2 HttpServletRequest

+ 简介：在Servlet的API中，定义了一个HttpServletRequest接口，它继承自ServletRequest接口，专门用于封装HTTP的请求，由于HTTP请求包含着请求行、请求头和请求体三部分，因此在HttpServletRequest中分别定义了接收请求行、请求头和请求体的相关方法

+ **主要应用**

  1. **请求转发**
  2. **获取前端传递的参数**

+ **获取<请求行>相关信息的相关方法:**

  ```java
  getMethod()/*方法:返回请求方法，请求方法通常是GET或者POST,但也有可能是HEAD、PUT或者DELETE。*/ getRequestURI()/*方法:返回URI （URI是URL的从主机和端口之后到表单数据之前的那一部分）。
    locallhost:8080/hello/get.htm1name=xiaohong&passWord=12345*/
  getRemoteAddr()/*方法:该方法用于获取请求客户端的IP地址*/
  getRemoteport()/*方法:该方法用于获取请求客户端的端口号*/
  getLocalAddr()/*方法:该方法用于获取服务器当前接收请求的IP地址*/
  getContextPath()/*方法:该方法用于获取URL中属于web应用程序的路径*/
  getProtoco1()/*方法:该方法用于获取请求行中的协议名和版本*/
  ```

  

+ **获取<请求头>的相关方法**:

  ```java
  getHeader(String name)/*该方法用于获取一个指定头字段的值，如果请求头中不包含该字段则返回nu11，如果包含多个该字段的值则获取第一个值*/
  getIntHeader (String name）/*该方法用于获取指定头字段的值，并且将其值转为int类型，如果不存在该字段则返回-1，如果获取到的值不能转换为int则会发生NumberFormatException异常*/
  getDateHeaders (String name)/*该方法用于获取指定头字段的值，并将其按照GMT时间格式转换成一个代表日期/时间的长整数*/
  getHeaderNames()/*该方法用于获取所有包含请求头字段的Enumeration*/
  
  ```

+ **获取<请求参数>**（get请求会将请求参数放在url中，而post请求会放在请求体中）

  ```java
  getParameter (String name)/*用于获取某个指定名称的参数值,如果请求中没有包含指定名称的参数,则返回nu11,如果有指定参数但是没有给设置值，则返回空串""，如果包含多个该参数的值则返回第一个出现的参数值*/
  getParameterNames()/*该方法用于返回一个包含请求消息中所有参数名的Enumernation*/
  getParameterMap()/*该方法用于将请求中的所有参数和值装入一个map对象然后返回*/
  ```

  eg

  ![image-20210329154143400](E:\typoradata\img\image-20210329154143400.png)
  
  ``````java
  import javax.servlet.ServletException;
  import javax.servlet.annotation.WebServlet;
  import javax.servlet.http.HttpServlet;
  import javax.servlet.http.HttpServletRequest;
  import javax.servlet.http.HttpServletResponse;
  import java.io.IOException;
  import java.util.Enumeration;
  
  @WebServlet(name = "httpservletrequest", urlPatterns = "/request")
  public class request对象 extends HttpServlet {
      protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
  
      }
  
      protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
          //获取请求行相关信息
          System.out.println("请求方法：" + request.getMethod());
          System.out.println("URI：" + request.getRequestURI());
          System.out.println("URL：" + request.getRequestURL());
          System.out.println("客户端IP地址：" + request.getRemoteAddr());
          System.out.println("客户端端口：" + request.getRemotePort());
          System.out.println("服务器接受请求的ip地址：" + request.getLocalAddr());
          System.out.println("url中属于web应用程序的路径：" + request.getContextPath());
          System.out.println("请求行中的协议名和版本：" + request.getProtocol());
  
          //获取请求头相关信息
          /*
          getHeader(String name)该方法用于获取一个指定头字段的值，如果请求头中不包含该字段则返回nu11，如果包含多个该字段的值则获取第一个值
          getIntHeader (String name）该方法用于获取指定头字段的值，并且将其值转为int类型，如果不存在该字段则返回-1，如果获取到的值不能转换为int则会发生NumberFormatException异常
          getDateHeaders (String name)该方法用于获取指定头字段的值，并将其按照GMT时间格式转换成一个代表日期/时间的长整数
          getHeaderNames()该方法用于获取所有包含请求头字段的Enumeration
          */
          Enumeration<String> headerNames = request.getHeaderNames();
          while (headerNames.hasMoreElements()) {
              String element = headerNames.nextElement();
              System.out.println(element + ":" + request.getHeader(element));
          }
          System.out.println("*******************");
  //**************************************
  //需要掌握的getParamter和getParamatername
          String name  = request.getParameter("name");
          String password = request.getParameter("password");
          System.out.println("姓名"+ name);
          System.out.println("密码"+  password);
  
      }
  }

  ``````
  
  ```html
  <!DOCTYPE html>
  <html lang="en">
  <head>
      <meta charset="UTF-8">
      <title>测试界面</title>
  </head>
  <body>
  <center>
      <h2>
          <form action="request" method="get">
              <p>账号：</p><input type="text" name="name">
              <p>密码：</p><input type="text" name="password">
              <input type="submit" value="提交">
          </form>
  
  
      </h2>
  </center>
  
  </body>
</html>
  ```
  
  1. 这里如果访问不到想要的html页面可以查看一下html的文件路径是否正确
  2. form表单的action属性与urlpatterns相同

#### 5.2.3 HttpServletResponse

+ 简介：在Servlet的API，定义了一个HttpServletResponse接口，它继承自Serv1etResponse接口，专门用于封装HTTP的响应，由于HTTP响应包含着响应行、响应头和响应体三部分，因此在
  HttpServletResponse中分别定义了发送响应行、响应头和响应体的相关方法
  
+ 常见应用

+ 一、下载文件

  ```java
  //在resourse中放置资源
  
  import java.io.IOException;
  
  import javax.servlet.ServletException;
  import javax.servlet.ServletOutputStream;
  import javax.servlet.annotation.WebServlet;
  import javax.servlet.http.HttpServlet;
  import javax.servlet.http.HttpServletRequest;
  import javax.servlet.http.HttpServletResponse;
  import java.io.FileInputStream;
  import java.io.IOException;
  
  @WebServlet(name = "DownloadServlet",urlPatterns = "/test01")
  public class DownloadServlet extends HttpServlet {
      protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
  
      }
  
      protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
          //要获取下载文件的路径
          String realPath = "E:/java_study/javaweb/target/classes/1.png";
          System.out.println("下载文件的路径"+realPath);
  
          // 下载的文件名是啥
          //极其精妙的一个方法这里使用substring获取最后一个/之后的字符串
          String filename = realPath.substring(realPath.lastIndexOf("\\") + 1);
          // 通过如下设置让浏览器能支持下载我们需要的文件
          response.setHeader("Content-disposition","attachment;filename="+filename);
          // 获取下载文件的输入流
          FileInputStream in = new FileInputStream(realPath);
          // 创建缓冲区
          int len = 0;
          byte[] buffer = new byte[1024];
          // 获取OutputStream对象
          ServletOutputStream outputStream = response.getOutputStream();
          // 将FileOutputStream流写入到buffer缓冲区,  使用OutputStream将缓冲区的数据输出到客户端
          while((in.read())!=-1){
              outputStream.write(buffer,0,len);
          }
          in.close();
          outputStream.close();
      }
  }
  
  ```
  
+ 二、实现验证码切换

  ```java
  import javax.imageio.ImageIO;
  import javax.servlet.ServletException;
  import javax.servlet.annotation.WebServlet;
  import javax.servlet.http.HttpServlet;
  import javax.servlet.http.HttpServletRequest;
  import javax.servlet.http.HttpServletResponse;
  import java.awt.*;
  import java.awt.image.BufferedImage;
  import java.awt.image.RGBImageFilter;
  import java.io.IOException;
  import java.util.Random;
  
  @WebServlet(name = "ImageServlet",urlPatterns = "/test02")
  public class ImageServlet extends HttpServlet {
      protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      }
      
      protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
          //如何让浏览器五秒刷新一次
          response.setHeader("refresh","3");
  
          //内存中创建图片
          BufferedImage bufferedImage = new BufferedImage(80, 20, BufferedImage.TYPE_INT_RGB);
          //得到图片
          Graphics2D graphics = (Graphics2D) bufferedImage.getGraphics();
          //设置图片的背景颜色
          graphics.setColor(Color.white);
          graphics.fillRect(0,0,80,20);
          //给图片写数据
          graphics.setColor(Color.blue);
          graphics.setFont(new Font(null,Font.BOLD,20));
          graphics.drawString(makeNum(),0,20);
          //告诉浏览器这个请求用图片的方式打开
          response.setContentType("image/jpeg");
          //网站存在缓存，需要设置不让他缓存
          response.setDateHeader("expires",-1);
          response.setHeader("Cache-COntrol","no-cache");
          response.setHeader("Pragma","no-cache");
  
          //把图片写给浏览器
          ImageIO.write(bufferedImage,"jpg",response.getOutputStream());
  
      }
      //生成随机数
      private String makeNum(){
          Random random = new Random();
          String s = random.nextInt(999999) + " ";
          StringBuffer stringBuffer = new StringBuffer();
          for (int i = 0; i < 7- s.length();i++) {
              stringBuffer.append("0");
          }
          String ss = stringBuffer.toString() + s;
          return ss;
      }
  }
  
  ```

+ **三、实现请求重定向**（三个response中唯一需要掌握的）

> ​	常见需求：用户登录

```java
void sendRedirext(String var1) throws IOException
```

​					测试：

```
protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("/w/test02");
    /*
    	重定向的本质
        response.setHeader("Location","/w/test02");
        response.setStatus(302);
     */
    }
```

> 面试题：请你聊聊重定向和转发的区别

​			    相同点

​						1.页面都会实现跳转

​				不同点

​						1.请求转发的时候，url不会发生变化

​						2.重定向时，url会发生变化

+ **发送<响应行>**

  ```java
    setStatus(int status)
    /*当Servlet向客户端回送响应消息时，需要设置一个状态码，该方法用于设置HTTP响应消息的状态码，并生成响应状态行。由于响应状态行中状态的描述直接和状态码相关，而HTTP协议版本由服务器决定，因此只需要设置该方法，就可以发送一个响应行，正常情况下，web服务器会默认发送一个200的状态码*/
    sendError (int code）
    /*该方法用于发送表示错误信息的状态码，例如404找不到访问的资源，他还有一种重载的形式sendError (intcode,String errorMessage) , errorMessage可以以文本形式显示在客户端浏览器*/
    
  ```

  + **发送<响应头>**

    ​	

    ```java
    addHeader(String name, String value)
  /*该方法用来设置HTTP协议的响应头字段，其中name是响应头字段名，value是响应字段的值*/
    setHeader(String name, String value)
  /*该方法和addHeader相同，唯一的区别是addHeader可以重复添加一个同名的响应头字段，setHeader会覆盖之前添加的同名的响应头
    */
  addIntHeader(String name, int value)
    setIntHeader(String name, int value)
    /*
  addIntHeader(String name,int value)、setIntHeader(Stringname,int value):这两个方法用于将value值为int的字段加入到响应头中
    */
  setContentType()
    ```

  + response对象发送请求体

    由于在HTTP响应消息中，大量的数据都是通过响应体传递的，因此ServletResponse遵循以IO流传递大数据的设计理念，定义了两个与输出流相关的方法，具体如下:

    1. getOutputStream()方法:该方法获取的字节流输出对象为**ServletOutputStream**类型，它是OutputStream的子类，因此可以直接输出字节数组中的二进制数据。
  2. getWrite()方法:该方法获得的字符输出流对象是**PrintWriter**类型由于它可以直接输出文本类型，因此要输出网页文档，需要使用这个方法


#### 5.2.4 Servlet的生命周期

  + 什么是Servlet的生命周期

    1. ​	指一个servlet对象从创建到销毁的过程

  + Servlet接口及相关方法

    ​	Serv1et接口是指:javax.servlet.Servlet
  
  + init (ServletConfig）方法,初始化方法
  
  + service (ServletRequest，ServletResponse）方法，每次访问都会调用来处理请求
    + destory (）方法，销毁servlet方法
    
    

    ​	**具体实现时会继承HttpServlet接口**

    + ​	HttpServ1et接口: javax.serv1et.http
    ​	继承自Servlet接口，并重新实现了service方法，根据	不同请求方式调用不同的处理方法。service (HttpServletRequestHttpServletResponse）方法,获取请求方式，分别调用doGet(),或者doPost()方法。
  
+ Servlet生命周期整个过程描述
  
    1. ​	创建一个servlet实例，重写init方法，service方法。destory方法（简单输出hello即可）
  2. 在service方法被执行的时候，会调用doGet和doPost方法
    3. 启动tomcat，观察控制台的输出

  
  
  
  

  
+ **注意:**Servlet实例是单例的，即无论请求多少次Servlet，最多只有一个Serv1et实例，如果是多个客户端并发，同时访问Servlet的时候，服务器会启动多个线程分别执行Serv1et的service方法
    原因:如果我们每次访问都创建一个Servlet实例,会占用和浪费过多的计算机资源

#### 5.2.5 HttpServletConfig对象

+ 简介：ServletConfig对象是它所对应的Servlet对象的相关配置信息
+ 特点
  1. 每一个Servlet对象都有一个ServletConfig对象和它相对应
  2. ServletConfig对象在多个Servlet对象中是不可共享的

+ 常见方法

  ```java
  //常见的Serv1etConfig对象的方法
  getInitParameter(String name)//返回一个初始化变量的值
  getInitParameterNames()//返回servlet初始化参数的所有名称
  getServletContext()//获取ServletContext对象
  4.getServletName()//获取Servlet的name配置值。
  
  ```

#### /的意义

![image-20210409152801530](E:\typoradata\img\image-20210409152801530.png)

## 6、Cookie Session

###  6.1 保存会话的两种技术

1. Cookie
   + 客户端技术 （通过响应）
2. session
   + 服务器技术。利用这个技术，可以保存用户的会话信息，我们可以把信息或者数据放进session

### 6.2 Cookie

1. 从请求中拿到cookie信息

2. 服务器相应给客户端cookie

   ```java
   Cookie[] cookies = request.getCookies(); //获得cookie
   cookies.getName(); //获取cookie中的key
   cookies.getValue(); //获取cookie中的value
    new Cookie("username","name"); //新建一个cookie
   cookie.setMaxage(24*60*60); // 设置cookie的有效期
   resp.addCookie(cookie); //响应给客户端一个cookie
   ```

+ 一个cookie是否存在上限问题
  1. 一个cookie只能保存一个信息
  2. 一个web站点可以给浏览器垡村多个cookie，多存放20个cookie
  3. cookie大小有限制4kb
  4. 浏览器的上限在300个左右

+ 删除cookie
  1. 不设置有效期，关闭浏览器cookie自动失效
  2. 设置有效期时间为0；

### 6.3 Session（重点）

+ Session简介

  1. 服务器会给每个用户创建一个session对象，**一个session独占一个浏览器，只要浏览器没有关闭，session就存在**

  2. 用户登录之后，整个网站都可以访问

  3. 常用方法

     ![image-20210330174148326](E:\typoradata\img\image-20210330174148326.png)

  ```java
  
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
          //解决乱码问题
          request.setCharacterEncoding("utf-16");
          response.setCharacterEncoding("utf-16");
          //浏览器的响应是一个html页面
          response.setContentType("text/html;charst = utf-8");
          //得到session
          HttpSession session = request.getSession();
  
          //void setAttribute(String var1, Object var2);
      	//这个方法可以像session中存放一个键值对
  	    session.setAttribute("username","wangzhe");
  
          //获取session得id
          String id = session.getId();
  
          //判断是不是新创建的session
          if(session.isNew()){
              response.getWriter().write("session创建成功，ID:" +session.getId());
          }else{
              response.getWriter().write("session已经存在，ID:"+session.getId());
          }
  
         /*
        session在这段时间做了什么？是
        Cookie cookie = new 				    Cookie("JSESSIONID","3452BA94C9044AD349770A6A5D9BC08A");
        response.addCookie(cookie);
        */
      
      //session注销
          session.removeAttribute("name");
          //注销session后id消失，但是会马上生成一个新id
          session.invalidate();
      }
  }
  ```

  会话自动过期；web.xml配置

  ```xml
    //设置session的失效时间
    <session-config>
      <session-timeout>15</session-timeout>
    </session-config>
  ```

### 6.4、Session和Cookie的区别

1. Cookie是把用户的数据写给用户的浏览器，浏览器保存（可以保存多个）
2. Session把用户的数据写到用户独占的session中，服务器端保存（保存重要的信息，减少服务器资源的浪费）
3. session对象由服务创建



使用场景：

1. 保存一个登录用户的信息
2. 购物车信息
3. 在整个网站中经常使用的数据保存在session

## 7、JSP

### 7.1、什么是jsp

Java Server Pages：java服务器端页面，也和servlet一样，用于动态web技术

特点：

1. 写jsp就像是再写HMTL
2. JSP页面中嵌入了java代码，为用户提供动态数据

### 7.2、JSP原理

思路：JSP到底是怎么执行的

+ 代码层面没有问题

+ 关注其服务器内部工作

+ 在tomcat工作（work）目录中发现页面转变成了java程序   **= > **不管访问什么资源，其实都是在访问servlet

+ JSP最终也会被转换为一个java类

+ JSP本质上就是一个servlet

  ```java
  //初始化  
  public void _jspInit() {
    }
  //销毁
    public void _jspDestroy() {
    }
  //JSPService
  public void _jspService(HttpServletRequest request,HttpServletResponse response)
  ```

  1. 判断请求

  2. jsp内置了一些对象

     ```java
         final javax.servlet.jsp.PageContext pageContext; //页面上下文
         javax.servlet.http.HttpSession session = null; //session
         final javax.servlet.ServletContext application;  //applicationContext
         final javax.servlet.ServletConfig config;  //config
         javax.servlet.jsp.JspWriter out = null;  //out
         final java.lang.Object page = this;  //当前页
     	HttpServletRequest request  //请求
     	HttpServletResponse response  //响应
     
     ```

### 7.3、JSP基础语法

```jsp
<%=   %>     作用:用来将程序的输出显示到客户端
------------------------------------------------
<%
作用：内嵌代码块
%>
------------------------------------------------
<%!
作用：JSP声明-->会被编辑到java的类中，其他的jsp代码会被生成到jspservice方法中
%>
------------------------------------------------


HTML的注释会在客户端显示
JSP的注释会在客户端显示


```

### 7.4、JSP指令

+ Page指令

+ include指令

+ taglib指令

  ```jsp
  <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
  ```

  



```java
//自定义错误界面吗，本质上使用请求转发的方式进行跳转
<%@ page errorPage="error.jsp" %>
    
    
    
```

```xml
      <error-page>
              <error-code>404</error-code>
              <location>/errorpage/error_404.jsp</location>
       </error-page>
       <error-page>
              <error-code>500</error-code>
              <location>/errorpage/error_500.jsp</location>
       </error-page>
```



### 7.5、JSP9大内置对象

+ PageContext
+ Request
+ Response
+ Session
+ Application【ServletContext】
+ config 【ServletConfig】
+ out
+ ~~page    不用了解~~
+ exception

### 7.6、作用域

```java
    pageContext.setAttribute("name1","zhangsan");//保存的数据在一个界面中有效
    request.setAttribute("name2","lisi");//保存的数据在一次请求中有效，请求转发会携带这个数据
    session.setAttribute("name3","wangmazi");//保存数据在一次会话中有效，从打开服务器到关闭服务器
    application.setAttribute("name4","guoer");//保存数据在服务器中保存，只要服务器不崩就有效
```

request：客户端向服务器发送请求产生的数据，用户看完没用的，比如：新闻

session：客户端向服务器发送请求产生的数据，用户看完还有用，比如：购物车

application：客户端向服务器发送请求产生的数据，其他用户还有用，比如：聊天内容

### 7.7 JSP标签，JSTL标签，EL表达式

EL表达式：    **${ }**

+ 用处：从四大域中获取数据

  EL获取pageContext域中的值: ${pageScope.key}​

  EL获取request域中的值: ${requestScope.key}​

  EL获取pageContext域中的值: ${sessionScope.key}​

  EL获取pageContext域中的值: ${applicationScope.key}​

  



JSP标签：

![image-20210407175450907](E:\typoradata\img\image-20210407175450907.png)

```jsp
//引入一个界面
<jsp:include page=""></jsp:include>
//请求转发
<jsp:forward page="">
    //存入一个键值对
    <jsp:param name="" value=""/>
</jsp:forward>
```

JSTL表达式

​	**JSTL标签库的使用就是为了弥补HTML标签的不足**，**但功能和Java是一样的**

​	使用方法：

1. 引入对应得taglib：https://www.runoob.com/jsp/jsp-jstl.html
2. 核心标签

​	核心标签是最常用的 JSTL标签。引用核心标签库的语法如下：

```
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
```

+ ~~格式化标签~~

+ ~~SQL标签~~

+ ~~XML标签~~

## 8、Javabean--实体类

JavaBean有特定的写法：

+ 必须要有一个无参构造
+ 属性必须私有化
+ 必须有set/get方法

**一般用来和数据库的字段做映射 ORM**

ORM：对象关系映射

+ 表--> 类
+ 字段 -->属性
+ 行记录 --> 对象

| id   | name    | age  | address |
| :--- | ------- | ---- | ------- |
| 1    | wangyi  | 18   | 1       |
| 2    | wanger  | 19   | 2       |
| 3    | wangsan | 20   | 3       |

```java
class People{
    private int id;
    private String name;
    private String address;
}
```



## 9、MVC架构

### 9.1、什么是MVC: 

+ Model  模型
  1. 业务处理：业务逻辑（Service）
  2. 数据持久层：CRUD (Dao)
+ View  视图
  1. 展示数据
  2. 提供链接发起servlet请求 （a ,img ,form）
+ Controller   控制器
  1. 接受用户请求 (Request :请求参数 ，Session信息)
  2. 交给业务层处理对应的业务代码
  3. 控制视图的跳转

```
登录 ---> 接受用户的请求 --->处理用户的请求(获取用户登录参数 username.password) ----> 交给业务层处理登录业务(判断用户名密码是否正确) ---> Dao层查询用户名和密码是否正确 ---> 数据库
```



早些年用户直接访问控制层，控制层就可以直接操作数据库

![image-20210408151550823](E:\typoradata\img\image-20210408151550823.png)

```
优化：
servlet--CRUD--数据库
弊端：程序十分臃肿，不利于维护
servlet的代码中：处理请求，响应，试图跳转，处理JDBC,处理业务代码，处理逻辑代码
-----------------------------------------------------
利用架构思想优化：
架构：没有什么是加一层解决不了的

中间件举例：
程序员调用
	|
	|
  JDBC 中间件
    |
    |
MySQL Oracle SqlServer
```

![image-20210409153004504](E:\typoradata\img\image-20210409153004504.png)



## 10、过滤器 Filter(重点)

Filter：过滤器，用来过滤网站的数据;

+ 处理中文乱码
+ 登录验证

### 1、Fliter开发步骤

1. 导包

2. 编写过滤器

   ```java
   import javax.servlet.*;
   import java.io.IOException;
   
   public class Filter implements javax.servlet.Filter {
       //编写过滤器需要重写三个方法
       //销毁
       public void destroy() {
           System.out.println("filter销毁");
       }
       //chain: 链
       /*
       1、过滤器中的代码，在过滤特定请求是都会执行
       2、
        */
       public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
           req.setCharacterEncoding("utf-8");
           resp.setCharacterEncoding("utf-8");
           resp.setContentType("text/html;charset=utf-8");
           System.out.println("filter执行前");
           //起到一个放行作用
           chain.doFilter(req,resp);  //如果不写，程序到这里就被拦截停止了
           System.out.println("filter执行后");
       }
   
       //web服务器启动就马上开始初始化
       public void init(FilterConfig config) throws ServletException {
           System.out.println("fliter初始化");
       }
   }
   
   ```

3. web.xml中配置filter

   ```xml
       <filter>
               <filter-name>Filter</filter-name>
               <filter-class>org.filter.Filter</filter-class>
       </filter>
       <filter-mapping>
           <filter-name>Filter</filter-name>
           <!--只要是/servlet的任何请求，都会经过这个过滤器-->
           <url-pattern>/servlet/*</url-pattern>
       </filter-mapping>
   
       <servlet>
           <servlet-name>showServlet</servlet-name>
           <servlet-class>org.servlet.showServlet</servlet-class>
       </servlet>
       <servlet-mapping>
           <servlet-name>showServlet</servlet-name>
           <url-pattern>/show</url-pattern>
       </servlet-mapping>
       <servlet-mapping>
           <servlet-name>showServlet</servlet-name>
           //走这个路径会解决乱码问题
           <url-pattern>/servlet/show</url-pattern>
       </servlet-mapping>
   ```

   ### 2、Filter常见用途

   + 

## 11、监听器

### 1、编写一个监听器

实现监听器的接口

```java
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
//统计网络在线人数 ： 统计session
public class OnlineCountListener implements HttpSessionListener{

    //创建session监听
    //一旦创建一个session，会触发一次这个事件
    @Override
    public void sessionCreated(HttpSessionEvent se) {
        ServletContext servletContext = se.getSession().getServletContext();

        System.out.println(se.getSession().getId());

        Integer onlineCount = (Integer)servletContext.getAttribute("OnlineCount");

        if(onlineCount == null){
            onlineCount  = new Integer(1);
        }else {
            int count = onlineCount.intValue();
            onlineCount = new Integer(count++);
        }
        servletContext.setAttribute("OnlineCount",onlineCount);
    }

    @Override
    //销毁session监听
    public void sessionDestroyed(HttpSessionEvent se) {
        ServletContext servletContext = se.getSession().getServletContext();
        Integer onlineCount = (Integer)servletContext.getAttribute("OnlineCount");

        if(onlineCount == null){
            onlineCount  = new Integer(1);
        }else {
            int count = onlineCount.intValue();
            onlineCount = new Integer(count--);
        }
        servletContext.setAttribute("OnlineCount",onlineCount);
    }
}
```

在xml中配置

```xml
  <listener>
        <listener-class>org.listener.OnlineCountListener</listener-class>
    </listener>
```



### 2、GUI编程中常用到监听器

```java
package org.listener;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class testJUI {
    public static void main(String[] args) {
        //新建一个窗体
        Frame frame = new Frame("中秋节快乐");
        //面板
        Panel panel = new Panel(null);
        //设置窗体的布局
        frame.setLayout(null);

        frame.setBounds(300,300,500,500);

        frame.setBackground(new Color(0,0,255));

        panel.setBounds(50,50,300,300);

        panel.setBackground(new Color(0,255,0));

        frame.add(panel);
        frame.setVisible(true);

        //给这个面板增加一些监听事件
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                System.out.println("打开");
            }

            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("关闭ing");
            }

            @Override
            public void windowClosed(WindowEvent e) {
                System.out.println("关闭了");
            }

            @Override
            public void windowActivated(WindowEvent e) {
                System.out.println("激活");
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
                System.out.println("未激活");
            }

        });
    }
}

```





















































