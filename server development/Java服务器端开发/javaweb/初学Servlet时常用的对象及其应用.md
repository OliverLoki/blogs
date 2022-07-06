# Servlet中Request&Response对象

## 一、Request对象

**封装了客户端所有的请求数据**

+ 简介：在Servlet的API中，定义了一个HttpServletRequest接口，它继承自ServletRequest接口，类型为javax.servlet.http.HttpServletRequest，用于封装HTTP的请求，由于HTTP请求包含着请求行、请求头和请求体三部分，因此在HttpServletRequest中分别定义了接收请求行、请求头和请求体的相关方法
+ **主要功能**

### **1）请求转发&请求包含**

```java
RequestDispatcher rd = request.getRequestDispatcher("/MyServlet");  
/*
使用request获取RequestDispatcher对象，方法的参数是被转发或包含的Servlet的Servlet路径
*/
rd.forward(request,response);//请求转发
rd.include(request,response);//请求包含
```

+ <u>**请求转发必须以斜杠开头，/ 表示地址为： { http://ip:port/工程名 },映射到IDEA代码的webapp目录**</u>

+ WEB-INF目录对客户端是隐藏的，但是请求转发可以访问

   **=>** 

  WEB-INF里内容只能由服务器级别才能访问，客户端级别不能访问。服务器级别的例子就是`请求转发`，转发是由服务器自己处理，跟客户端（浏览器）无关，所以浏览器上的地址栏也就不会改变。
  客户端级别的例子就是`重定向`，两次请求，两次响应。所以地址栏会改变。

关于WEB-INF的访问

> https://blog.csdn.net/qq_43639081/article/details/109200999

+ 请求转发与请求包含比较：

  （1）如果在AServlet中请求转发到BServlet，那么在AServlet中就不允许再输出响应体，即不能再使用response.getWriter()和response.getOutputStream()向客户端输出，这一工作应该由BServlet来完成；如果是使用请求包含，那么没有这个限制；

  （2）请求转发虽然不能输出响应体，但还是可以设置响应头的，例如：response.setContentType(”text/html;charset=utf-8”);

  （3）请求包含大多是应用在JSP页面中，完成多页面的合并；

  （4）请求转发大多是应用在Servlet中，转发目标大多是JSP页面；

  图非原创，来源：https://blog.csdn.net/qq_29028175

  ![image-20210413145652980](E:\typoradata\img\image-20210413145652980.png)

### **2）获取前端传递的参数**

```java
String username = request.getParameter("username");
String password = request.getParameter("password");
//获取客户端发出的一组数据
String[] hobbys = request.getParameterValues("hobbys");
```

一个请求转发与获取请求参数的例子

```html
<!--创建一个有form表单的界面，用来让浏览器提交一个请求-->
<%@ page  pageEncoding="utf-8" isELIgnored="false" contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>request读取表单内容</title>
</head>
<body>
<div>
    <h1 style="text-align: center">登录</h1>
    <%--  ${pageContext.request.contextPath} 不生效的解决办法 --%>
    <%--  jsp头中添加   isELIgnored="false"    --%>
    <form action="${pageContext.request.contextPath}/login" method="post" style="text-align: center">
        用户名：<input type="text" name="username"><br>
        密码： <input type="text" name="password"><br>
        爱好： <input type="checkbox" name="hobbys" value="女孩">女孩
        <input type="checkbox" name="hobbys" value="篮球">篮球
        <input type="checkbox" name="hobbys" value="台球">台球
        <input type="checkbox" name="hobbys" value="足球">足球
        <br>
        <input type="submit" value="提交" >
    </form>      
</div>
</body>
</html>
```

```java
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@WebServlet(name = "requestServlet",urlPatterns = "/login")
public class request extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //解决后台接受中文乱码问题,需要放在最前面，实际开发中会在过滤器中做这件事
        response.setCharacterEncoding("utf-8");
        request.setCharacterEncoding("utf-8");

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String[] hobbys = request.getParameterValues("hobbys");

        System.out.println("================================");
        System.out.println(username);
        System.out.println(password);
        System.out.println(Arrays.toString(hobbys));
        System.out.println("===================================");
        //请求转发
        request.getRequestDispatcher("success.jsp").forward(request,response);
    }
}
```

## **3）Request与请求头相关的方法**

```
String getHeader(String name)：获取指定名称的请求头；

Enumeration getHeaderNames()：获取所有请求头名称；

int getIntHeader(String name)：获取值为int类型的请求头。
```





## 二、 Response对象

+ 简介：在Servlet的API，定义了一个HttpServletResponse接口，它继承自Serv1etResponse接口，专门用于封装HTTP的响应，由于HTTP响应包含着响应行、响应头和响应体三部分，因此在HttpServletResponse中分别定义了发送响应行、响应头和响应体的相关方法
+ **常见应用**

### **1）使用response对象的setHeader()方法来设置响应头**

```java
void setHeader(String var1, String var2);
```

```java
/*1）设置content-type响应头，该头的作用是告诉浏览器响应内容为html类型，编码为utf-8。而且同时会设置response的字符流编码为utf-8，即response.setCharaceterEncoding(“utf-8”)；*/
response.setHeader(“content-type”, “text/html;charset=utf-8”)：
//2）5秒后自动跳转到百度主页
response.setHeader("Refresh","5;URL=http://www.baidu.com")
//3）设置重定向状态码
//4) 设置文件下载
resp.setHeader("Content-Disposition", "attachment;filename=" + filename);
resp.setHeader("Content-Type", "application/octet-stream");
    
```

### **2) 设置状态码**

```java
void setStatus(int status)
  /*当Servlet向客户端回送响应消息时，需要设置一个状态码，该方法用于设置HTTP响应消息的状态码，并生成响应状态行。由于响应状态行中状态的描述直接和状态码相关，而HTTP协议版本由服务器决定，因此只需要设置该方法，就可以发送一个响应行，正常情况下，web服务器会默认发送一个200的状态码*/
void sendError(int code）
  /*使用指定的状态码并清空缓冲，发送一个错误响应至客户端，如果响应已经被提交，这个方法会抛出IllegalStateException。使用这个方法后，响应则应该被认为已被提交，且不应该再被进行写操作，.
  例如404找不到访问的资源，他还有一种重载的形式sendError (int code,String errorMessage) , errorMessage可以以文本形式显示在客户端浏览器*/
  
```

> sendError&setStatus：https://my.oschina.net/angerbaby/blog/468687

###  **3) response响应正文**

response是响应对象，向客户端输出响应正文（响应体）可以使用response的响应流，repsonse一共提供了两个响应流对象

```java
//获取字符流
PrintWriter out = response.getWriter();
//获取字节流
ServletOutputStreamout = response.getOutputStream();
//在一个请求中，不能同时使用这两个流,否则会会抛出illegalStateException异常
```

+ 字符编码
  在使用response.getWriter()时需要注意默认字符编码为ISO-8859-1，如果希望设置字符流的字符编码为utf-8，可以使用response.setCharaceterEncoding(“utf-8”)来设置。这样可以保证输出给客户端的字符都是使用UTF-8编码的！
  但客户端浏览器并不知道响应数据是什么编码的！如果希望通知客户端使用UTF-8来解读响应数据，那么还是使用**response.setContentType(“text/html;charset=utf-8”)**方法比较好，因为这个方法不只会调用response.setCharaceterEncoding(“utf-8”)，还会设置content-type响应头，客户端浏览器会使用content-type头来解读响应数据。
+ 缓冲区
  response.getWriter()是PrintWriter类型，所以它有缓冲区，缓冲区的默认大小为8KB。也就是说，在响应数据没有输出8KB之前，数据都是存放在缓冲区中，而不会立刻发送到客户端。当Servlet执行结束后，服务器才会去刷新流，使缓冲区中的数据发送到客户端。
  如果希望响应数据马上发送给客户端：
  1. 向流中写入大于8KB的数据；
  2. 调用response.flushBuffer()方法来手动刷新缓冲区；

### 4）**使用response对象的sendRedirect()方法实现请求重定向（重要）**

> ​	常见需求：用户登录

```java
protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       response.sendRedirect("http://www.baidu.com");
    /*
    	重定向的本质
    	response.setStatus(302);
        response.setHeader("Location","/w/test02"); 
     */
    }
```

### **5) 其他方法**	

```java
addHeader(String name, String value)
/*该方法用来设置HTTP协议的响应头字段，其中name是响应头字段名，value是响应字段的值*/
addIntHeader(String name, int value)
setIntHeader(String name, int value)
/*
addIntHeader(String name,int value)、setIntHeader(Stringname,int value):这两个方法用于将value值为int的字段加入到响应头中
*/
setContentType()
    
```

