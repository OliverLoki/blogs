# 让你彻底理解Servlet中请求转发&重定向

## **forward（转发）**：

```java
request.getRequestDispatcher(String url).forward(request,response);
```

+ **地址栏：forward是服务器请求资源,服务器直接访问目标地址的URL,把那个URL的响应内容读取过来,然后把这些内容再发给浏览器.浏览器根本不知道服务器发送的内容从哪里来的,因为这个跳转过程实在服务器实现的，并不是在客户端实现的所以客户端并不知道这个跳转动作，所以它的地址栏还是原来的地址.**

![image-20210420155847218](https://i.loli.net/2021/04/20/G6LAdhsu3T7fgUV.png)

+ **状态码：200**
+ **数据共享：转发页面和转发到的页面可以共享request里面的数据，并且可以访问WEB-INF下的数据**
+ **应用：一般用于用户登陆的时候,根据角色转发到相应的模块**
+ **效率较高**
+ **示意图**：

![image-20210420160654838](https://i.loli.net/2021/04/20/M4rexaXPGwpTEB3.png)



## **redirect（重定向）：**

```java
response.sendRedirect(String url);//返回登录界面
```

+ **地址栏:是服务端根据逻辑,发送一个,告诉浏览器重新去请求那个地址.所以地址栏显示的是新的URL.转发是服务器行为，重定向是客户端行为。**

![image-20210420160025161](https://i.loli.net/2021/04/20/IhE96vMAKi5LWsm.png)

+ **状态码:302**
+ **数据共享：不能共享数据**，**只能使用session**

+ **应用:一般用于用户注销登陆时返回主页面和跳转到其它的网站等****
+ **效率偏低**
+ **示意图**：

![image-20210420160758252](https://i.loli.net/2021/04/20/gKjmLJ4zivNeydX.png)



## 转发和重定向路径问题：

1）使用相对路径在重定向和转发中没有区别
2）重定向和请求转发使用绝对路径时，根/路径代表了不同含义

+ 重定向response.sendRedirect("xxx")是服务器向客户端发送一个请求头信息，由客户端再请求一次服务器。/指的Tomcat的根目录,写绝对路径应该写成"/当前Web程序根名称/资源名" 。如"/WebModule/login.jsp"  &&   "/bbs/servlet/LoginServlet"

+ 转发是在服务器内部进行的，写绝对路径/开头指的是当前的Web应用程序。绝对路径写法就是"/login.jsp"或"/servlet/LoginServlet"。

**总结**：以上要注意是区分是从服务器外的请求，还在是内部转发，从服务器外的请求，从Tomcat根写起(就是要包括当前Web的根)；是服务器内部的转发，很简单了，因为在当前服务器内，/写起指的就是当前Web的根目录。

![image-20210420154534679](https://i.loli.net/2021/04/20/IiLbON29KW4auJs.png)

## 

