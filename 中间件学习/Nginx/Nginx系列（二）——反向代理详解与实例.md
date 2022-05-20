> **[Nginx系列（一）——快速入门Nginx](https://blog.csdn.net/Night__breeze/article/details/124457586)**
>



**Nginx可以代理以下几种协议，用到最多的是Http代理服务器**

![image-20220428172330650](https://s2.loli.net/2022/04/28/Cb3lu2wLDAjfvsp.png)

**引言**

> Nginx 服务器的反向代理服务是其最常用的重要功能，由反向代理服务也可以衍生出很多与此相关的 Nginx 服务器重要功能，比如下一篇会介绍的负载均衡。本篇博客我们会先介绍 Nginx 的反向代理，当然在了解反向代理之前，我们需要先知道什么是代理以及什么是正向代理

> 代理涉及到设计模式的理念，可以选择移步看一下这篇文章[Java设计模式之代理模式]()，
>
> 代理模式是这样定义的：给某个对象提供一个代理对象，并由代理对象控制原对象的操作，**说通俗一点就是，Loki由于疫情不方便去吃烤肉，然后委托外卖小哥作代理，去帮我买烤肉吃**



@[TOC]

## 一、理解正向代理

> **正向代理**
>
> **如果把局域网外的 Internet 想象成一个巨大的资源库，则局域网中的客户端要访问 Internet，则需要通过代理服务器来访问，这种代理服务就称为正向代理**

举一个正向代理的例子

:rabbit:**Q:大家都知道，现在国内是访问不了 Google 的，那么怎么才能访问 Google呢？**



:airplane:**A**:如果我们电脑的对外公网 IP 地址能变成美国的 IP 地址，我们就可以访问 Google了，`VPN` 就是这样产生的。我们在访问 Google 时，先连上 VPN 服务器将我们的 IP 地址变成美国的 IP 地址，然后就可以顺利的访问了

+ 这里的 VPN 就是做正向代理的。正向代理服务器位于客户端和服务器之间，为了向服务器获取数据，客户端要向代理服务器发送一个请求，并指定目标服务器，代理服务器将目标服务器返回的数据转交给客户端。这里客户端是要进行一些正向代理的设置的

+ **不难看出，正向代理是帮忙处理客户端的请求**

## 二、理解反向代理

> **反向代理**

举一个反向代理的例子

:rabbit:**Q:我们在浏览器地址栏访问 `baidu.com`，百度服务器怎么处理我们的请求呢？** 



:airplane:**A**:由于客户端对代理是无感知的，我们在地址栏直接访问域名即可，但是服务器端呢？由于百度那么高的并发访问数，肯定不可能将所有用户的请求都打到一个服务器处理，这样这台服务器肯定会扛不住的，这样子就引出了这次的主角——**`Nginx做反向代理服务器`**

+ **我们只需要将请求发送到反向代理服务器，由反向代理服务器去选择目标服务器获取数据后，再返回给客户端，此时反向代理服务器和目标服务器对外就是一个服务器，暴露的是代理服务器 地址，隐藏了真实服务器 IP 地址。**

**Nginx做反向代理示意图**

![image-20220428151310140](https://s2.loli.net/2022/04/28/pIhSn1Ge52wFtMW.png)

总结为一句话：**正向代理代理客户端，反向代理代理服务器**



## 三、反向代理配置实例

**这是网上比较常见的案例，但是我感觉大多数写的都不是很容易理解，Loki在这里重新整理一下**

**你需要具备两个前提条件**

+ 了解Tomcat的使用
+ 为了方便操作，这是Window环境下的一次演示，所以需要在Windows下安装Nginx和Tomcat

> **以Tomcat为例，这是一个Servlet容器，就像Java运行在JVM上一样，Servlet程序运行在Tomcat中**

[下载Tomcat压缩包](https://tomcat.apache.org/)，解压后进入`bin`目录，执行 `startup.bat` 文件，这样Tomcat服务就启动了

**通过 `localhost:8080` 端口访问这个web服务器**

![image-20220428182553093](https://s2.loli.net/2022/04/28/KHzuiyRca6OwDA5.png)

这样看起来有点麻烦，我们可不可以通过访问 `loki.com` 的方式来访问这个网页呢？

> 第一步：修改本机hosts文件
>
> 路径：`C:\Windows\System32\drivers\etc`

hosts文件新增一行

```
127.0.0.1 loki.com
```

这一步完成以后，我们就可以通过`loki.com:8080`访问到这个界面了，还差一点，8080怎么省略呢？

于是，Nginx登场了~

```
附：更改hosts文件不起作用的解决办法

+ 用记事本打开，别用IDE，否则会修改文件编码，它应该是`ANSI`编码才对
+ 重启浏览器
+ cmd命令行执行:          ipconfig /flushall
+ 不要翻墙，不要开代理
```

> 第二步：Nginx反向代理配置

官方下载Nginx（win版本），目录结构和上一篇学习的一样，就不再解释

[如果对目录结构不了解请看---快速上手Nginx](https://blog.csdn.net/Night__breeze/article/details/124457586)

1. 修改 `nginx.conf` 配置文件

   配置文件修改如下，将原来的那个server块替换

   ```bash
   	server {
   		#默认监听端口
           listen       80;   
           #指定虚拟主机的配置
           server_name  Loki.com;
           location / {
           	#设置被代理服务器的地址。可以是主机名称、IP地址加端口号的形式
               proxy_pass http://127.0.0.1:8080;
               index  index.html index.htm index.jsp;
           }
       }
   ```

2. 双击`nginx.exe`，启动Nginx服务，这样所有的网络请求必须要先过Nginx这一关，于是我们就可以通过`loki.com`访问到`127.0.0.1:8080`

> **流程分析**

1. Http默认端口就是80端口，我们访问`loki.name`请求，实际上是访问`loki.name:80`端口
2. 在Server块中配置监听`80`端口，请求会被代理到`127.0.0.1:8080`
3. 而在第二步中我们把`127.0.0.1`和`loki.com`做了映射关系，所以可以通过``loki.com`访问原来是`localhost:8080`的Tomcat首页了

## 四、Nginx负载均衡

### 简介

负载均衡是将负载分摊到多个操作单元上执行，从而提高服务的可用性和响应速度，带给用户更好的体验。对于Web应用，通过负载均衡，可以将一台服务器的工作扩展到多台服务器中执行，提高整个网站的负载能力。其本质采用一个调度者，保证所有后端服务器都将性能充分发挥，从而保持服务器集群的整体性能最优，这就是负载均衡

### 常用负载均衡策略

+ round-robin：轮询。以轮询方式将请求分配到不同服务器上。
+ least-connected：最少连接数。将下一个请求分配到连接数最少的那台服务器上。
+ ip-hash：基于客户端的IP地址。散列函数被用于确定下一个请求分配到哪台服务器上。

**在生产环境中，一般只会用到轮询策略**

> **轮询的概念**

轮询为负载均衡中较为基础也较为简单的算法，它不需要配置额外参数。假设配置文件中共有 **M** 台服务器，该算法遍历服务器节点列表，并按节点次序每轮选择一台服务器处理请求。当所有节点均被调用过一次后，该算法将从第一个节点开始重新一轮遍历

> 默认轮询方式配置文件

```bash
upstream mybalance01 {
    server 172.24.10.22:9090;
    server 172.24.10.23:9090;
    server 172.24.10.24:9090;
}

server {
    listen  80;
    server_name  balance.linuxds.com;
    access_log  /var/log/nginx/mybalance.access.log  main;
    error_log   /var/log/nginx/mybalance.error.log  warn;
    location / {
        proxy_pass http://mybalance01;
        index index.html;
        proxy_redirect     off;
        proxy_set_header   Host             $host;
        proxy_set_header   X-Real-IP        $remote_addr;
        proxy_set_header   X-Forwarded-For  $proxy_add_x_forwarded_for;
        client_max_body_size       10m;		#允许客户端请求的最大单文件字节数
        client_body_buffer_size    128k;	#缓冲区代理缓冲用户端请求的最大字节数
        proxy_connect_timeout      300;		#nginx跟后端服务器连接超时时间(代理连接超时)
        proxy_send_timeout         300;		#后端服务器数据回传时间(代理发送超时)
        proxy_read_timeout         300;		#连接成功后，后端服务器响应时间(代理接收超时)
        proxy_buffer_size          4k;		#设置代理服务器（nginx）保存用户头信息的缓冲区大小
        proxy_buffers              4 32k;	#proxy_buffers缓冲区，网页平均在32k以下的话，这样设置
        proxy_busy_buffers_size    64k;		#高负荷下缓冲大小（proxy_buffers*2）
        proxy_temp_file_write_size 64k;		#设定缓存文件夹大小，大于这个值，将从upstream服务器传
    }
}
```

