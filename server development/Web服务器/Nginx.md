**引言**

> Nginx 是十分轻量级的`开源`、`高性能`、`高可靠`的**HTTP和反向代理服务器**，性能是 Nginx 最重要的考量，其占用内存少、处理高并发能力是十分强大的，能支持高达 5w 个并发连接数，最重要的是，Nginx 是免费的并可以商业化，配置使用也比较简单
>
> + Nginx中文文档：https://www.nginx.cn/doc/



@[TOC]

# 一、Nginx基础扫盲

## Nginx的发行版本

**和Linux一样，Nginx有一些常用的发行版，我们来看一下，本文主要是对Nginx开源版本的学习**

+ [Nginx开源版](nginx.org)

  简单的web服务器、反向代理、负载均衡

+ [Nginx 商业版](https://www.nginx.com/)

  把Nginx和Lua脚本进行了一个整合，对微服务和云原生整合较好

+ [openResty——免费开源](https://openresty.org/cn/)

  OpenResty是一个基于 [Nginx](https://openresty.org/cn/nginx.html) 与 Lua 的高性能 Web 平台，其内部集成了大量精良的 Lua 库、第三方模块以及大多数的依赖项。用于方便地搭建能够处理超高并发、扩展性极高的动态 Web 应用、Web 服务和动态网关

+ [Tengine——免费开源](https://openresty.org/cn/)

  Tengine是由淘宝网发起的Web服务器项目。它在[Nginx](http://nginx.org/)的基础上，针对大访问量网站的需求，添加了很多高级功能和特性。Tengine的性能和稳定性已经在大型的网站如淘宝网，天猫商城等得到了很好的检验。它的最终目标是打造一个高效、稳定、安全、易用的Web平台。

## Nginx的功能和特性

+ 可以作为静态页面的 web 服务器
+ 反向代理
+ 负载均衡

> **Nginx 做为 HTTP 服务器，有以下几项基本特性：**

+ 支持热部署，几乎可以做到 7 * 24 小时不间断运行，即使运行几个月也不需要重新启动，还能在不间断服务的情况下对软件版本进行热更新
+ Nginx 以事件驱动的方式编写，有非常好的性能，同时可以做非常高效的反向代理、负载均衡服务器了

- 处理静态文件，索引文件以及自动索引；打开文件描述符缓冲．
- 无缓存的反向代理加速，简单的负载均衡和容错．
- FastCGI，简单的负载均衡和容错

## Nginx的缺点

+ Nginx 仅能支持http、https和Email协议，这样就在适用范围上面小些
+ 对后端服务器的健康检查，只支持通过端口来检测，不支持通过 url来检测。不支持 Session 的直接保持，但能通过 ip_hash 来解决





# 二、Nginx快速开始

## 安装step1：环境准备

**请自行准备好一个可以连通网络的虚拟机并且搭载Linux的系统，你可以通过**

+ **`VMWare`虚拟机搭建CentOS7环境**
+ [搭建云服务器Linux学习环境](https://blog.csdn.net/Night__breeze/article/details/123778708?spm=1001.2014.3001.5501#Linux_74)

**除此之外，推荐安装`XShell`和`Xftp`这两款软件用于控制远端服务器**



**安装Nginx前，我们要先安装好它的依赖环境，就像Java程序需要运行在JVM上一样，nginx安装依赖以下环境，因此要检查是否安装，如果没有则需要安装这些环境**

> + `GCC`
>
>   GCC编译器是Linux下最常用的C/C++编译器,它以gcc命令的形式呈现
>
> + `openssl-devel`
>
>   `openssl`是多功能命令工具，用于生成密钥，创建数字证书，手动加密解密数据
>   nginx 不仅支持 http 协议，还支持 https（即在ssl协议上传输http），所以需要在 Centos 安装 OpenSSL 库
>
>   ```
>   yum -y install pcre-devel openssl openssl-devel
>   ```
>
> + `pcre-devel`
>
>   PCRE(Perl Compatible Regular Expressions)是一个轻量级的Perl函数库，包括 perl 兼容的正则表达式库。它比Boost之类的正则表达式库小得多。PCRE十分易用，同时功能也很强大，性能超过了POSIX正则表达式库和一些经典的正则表达式库
>
> + `zlib-devel`
>
>   zlib是提供数据压缩用的函式库，由Jean-loup Gailly与Mark Adler所开发，初版0.9版在1995年5月1日发表。zlib使用DEFLATE算法，最初是为libpng函式库所写的，后来普遍为许多软件所使用。此函式库为自由软件，使用zlib授权。截至2007年3月，zlib是包含在Coverity的美国国土安全部赞助者选择继续审查的开源项目

1. **gcc安装**  

   检查是否安装

   ```
   gcc --version
   ```

   已安装会显示版本号，未安装执行在线安装指令

   ```
   yum install gcc-c++
   ```

2. **PCRE 安装**

   查看是否安装

   ```
   rpm -qa pcre
   ```

   已安装会显示版本号，未安装执行在线安装指令

   ```undefined
   yum install -y pcre pcre-devel
   ```

3. **zlib 安装**

   查看是否安装

   ```perl
   yum list installed | grep zlib*
   ```

   已安装会显示版本号，未安装执行在线安装指令

   ```undefined
   yum install -y zlib zlib-devel
   ```

**以上环境没有问题之后，我们来安装Nginx**

## 安装step2：编译安装

[官方下载地址](http://nginx.org/en/download.html)：http://nginx.org/en/download.html

![image-20220426172024670](https://s2.loli.net/2022/06/28/bT6JlnAjv4fHZir.png)



在本机下载以后通过xftp上传到`Linux`服务器，解压后进入解压目录，执行以下命令

| 命令                                    | 功能          |
| --------------------------------------- | ------------- |
| `./configure --prefix=/usr/local/nginx` | 配置Nginx环境 |
| `make`                                  | 编译          |
| `make install`                          | 安装          |

**安装完nginx后，会在路径 `/usr/local` 下生成 nginx 文件夹**

进入这个目录

```
cd /usr/local/nginx
```

`ls`查看目录结构，建议自行去看看这些文件夹

+ `sbin`目录(里面有两个文件：`nginx ` 和 `nginx.old`)
+ `conf`目录，Nginx的配置文件目录，其中**`nginx.conf`**是主配置文件

![image-20220426223005186](https://s2.loli.net/2022/06/28/PFhRYame3fHcvQo.png)

## 安装step3：启动Nginx服务

```bash
#进入nginx下的sbin目录
cd /usr/local/nginx/sbin
#启动Nginx服务，./表示是当前目录
./nginx 
#指定配置文件启动
./nginx -c [指定文件路径]
#指定文件重启
/nginx -s reload -c [指定文件路径]
```

**如果访问你的Linux主机的ip地址能看到这个界面，说明Nginx服务启动成功**

![image-20220426223307823](https://s2.loli.net/2022/06/28/eHDBxq4V5rbyzPh.png)

## 防火墙问题导致无法访问Nginx

**:rocket: 因为防火墙问题，在 windows 系统中访问 linux 中 nginx，默认不能访问的**

**因此需要开放入口规则，即开放80 端口，操作如下**

查看开放的端口号

```
firewall-cmd --list-all 
```


设置开放的端口号

```
firewall-cmd --add-service=http –permanent 
firewall-cmd --add-port=80/tcp --permanent 
```

重启防火墙

```
firewall-cmd –reload 
```

> `redis.conf`可以看到Nginx默认的监听端口

![image-20220426224718318](https://s2.loli.net/2022/06/28/xX1zHhSYmjVd9rw.png)

## 注册Nginx为开机自启动的系统服务

> 首先编写自启动脚本

```
# 进入系统服务目录
cd /usr/lib/systemd/system
#创建nginx.service文件，并编辑
vim nginx.service
```

进入编辑模式，复制以下内容

```apl
[Unit]
Description=nginx service
After=network.target

[Service]
Type=forking
ExecStart=/usr/local/nginx/sbin/nginx
ExecReload=/usr/local/nginx/sbin/nginx -s reload
ExecStop=/usr/local/nginx/sbin/nginx -s quit
PrivateTmp=true

[Install]
WantedBy=multi-user.target
```

**然后就是配置自启动了**

```bash
#重启系统服务
systemctl daemon-reload

#查看当前Redis状态
ps -ef | grep nginx

#如果ngin处于启动状态，关闭该服务
cd /usr/local/nginx/sbin
./nginx -s stop

#根据系统服务启动 Nginx，不用到 nginx/sbin目录下启动了
systemctl start nginx
```

```bash
#查看进程状态，到这一步，如果Redis显示正在运行中，说明配置文件编写成功
systemctl status nginx
```

设置Nginx服务开机自启动

```
systemctl enable nginx.service
```

如果不想开机自启动了，可以使用下面的命令取消开机自启动

```
systemctl disable nginx
```

## Linux系统操作Nginx常用命令

> **查看nginx是否启动**

```
ps -ef|grep nginx
```

> **停止nginx服务**

```bash
cd /usr/local/nginx/sbin #这个是你的Nginx安装目录
./nginx -s stop
```

> **查看 nginx 的版本号**

```bash
cd /usr/local/nginx/sbin #这个是你的Nginx安装目录
./nginx -v
```

> **重新加载 nginx**

```bash
./nginx -s reload
```

> **检查Nginx配置文件正确性**

```
./nginx -t
```



# 四、Nginx.conf配置文件详解

**`#`开头表示注释，去掉所有注释内容，精简之后的内容Loki做了详细注释**

> Nginx 的文件结构主要包括三个区块，结构如下

+ Global: nginx 运行相关
  + worker_processes：配置文件开始到 events 块之间的内容,主要设置一些影响nginx服务器整体运行的配置指令
+ Events: 涉及的指令主要影响 Nginx 服务器与用户的网络连接
+ http：包括 http全局部分、server 部分
  + http Global: 代理，缓存，日志，以及第三方模块的配置
  + server：Nginx 服务器配置中最频繁的部分，代理、缓存和日志定义等绝大多数功能和第三方模块的配置都在这里
    + server Global: 虚拟主机相关
    + location: 地址定向，数据缓存，应答控制，以及第三方模块的配置

从上面展示的 nginx 结构中可以看出 `location` 属于请求级别配置，这也是我们最常用的配置

> Nginx配置文件详解：注释版

```bash
#Nginx是多进程的（一个主进程和多个副进程），这个参数表明了开启一个副进程，worker_processes值越大，可支持的并发处理量也越多，主要受当前服务器物理CPU内核数制约
worker_processes  1;

#enents块：涉及的指令主要影响 Nginx 服务器与用户的网络连接
events {
	#表示每个 work process 支持的最大连接数为 1024,这部分的配置对 Nginx 的性能影响较大，在实际中应该灵活配置。
    worker_connections  1024;
}

#http块：包括 http全局部分、server 部分
http {
	#引入其他配置文件，便于后期维护管理与协同工作下死锁的问题
	#客户端是无法通过后缀名识别文件究竟是什么的，mime.types指定了服务器返回文件的类型
    include  mime.types; 
    #如果返回类型在mime.type中没有，则返回默认类型application/octet-stream
    default_type  application/octet-stream; 
    
    #免除了数据传输中拷贝的过程，涉及到Nginx调优开启，可以在http块、server块或者location块中进行配置
    sendfile    on;
    #长连接超时时间，单位是秒
    keepalive_timeout  65;
	
	#每个http块可以包括多个 server 块，可以通过配置多个server块监听不同端口号，实现同ip地址下多站点的配置
	#server 块：代理、缓存和日志定义等绝大多数功能和第三方模块的配置都在这里
    server {
    	#当前主机监听端口号，下文详解
        listen   80;
        #下文详解
        server_name  localhost; 
     
		#下文详解
        location / { 
        	#设置被代理服务器的地址。可以是主机名称、IP地址加端口号的形式
            proxy_pass http://127.0.0.1:8080;
            root   html;
            index  index.html index.htm;
        }
        
        #错误页服务器错误重定向地址
        error_page   500 502 503 504  /50x.html; 
        location = /50x.html {
            root   html;
        }      
    }
}
```

**详细字段的解释**

> **listen：该指令用于配置网络监听**
>
> 指令默认的配置值是：listen *:80 | *:8000

+ 配置监听的IP地址

```
listen address[:port] [default_server] [ssl] [http2 | spdy] [proxy_protocol] [setfib=number] [fastopen=number] [backlog=number] [rcvbuf=size] [sndbuf=size] [accept_filter=filter] [deferred] [bind] [ipv6only=on|off] [reuseport] [so_keepalive=on|off|[keepidle]:[keepintvl]:[keepcnt]];
```

+ 配置监听端口

```
listen port [default_server] [ssl] [http2 | spdy] [proxy_protocol] [setfib=number] [fastopen=number] [backlog=number] [rcvbuf=size] [sndbuf=size] [accept_filter=filter] [deferred] [bind] [ipv6only=on|off] [reuseport] [so_keepalive=on|off|[keepidle]:[keepintvl]:[keepcnt]];
```

上面的配置看似比较复杂，其实使用起来是比较简单的：

```
1 listen *:80 | *:8080 #监听所有80端口和8080端口
2 listen  IP_address:port   #监听指定的地址和端口号
3 listen  IP_address     #监听指定ip地址所有端口
4 listen port     #监听该端口的所有IP连接
```

**分别解释每个选项的具体含义：**

+ address:IP地址，如果是 IPV6地址，需要使用中括号[] 括起来，比如[fe80::1]等。
+ port:端口号，如果只定义了IP地址，没有定义端口号，那么就使用80端口。
+ path:socket文件路径，如 var/run/nginx.sock等。
+ default_server:标识符，将此虚拟主机设置为 address:port 的默认主机。（在 nginx-0.8.21 之前使用的是 default 指令）
+ setfib=number:Nginx-0.8.44 中使用这个变量监听 socket 关联路由表，目前只对 FreeBSD 起作用，不常用。
+ backlog=number:设置监听函数listen()最多允许多少网络连接同时处于挂起状态，在 FreeBSD 中默认为 -1,其他平台默认为511.
+ rcvbuf=size:设置监听socket接收缓存区大小。
+ sndbuf=size:设置监听socket发送缓存区大小。
+ deferred:标识符，将accept()设置为Deferred模式。
+ accept_filter=filter:设置监听端口对所有请求进行过滤，被过滤的内容不能被接收和处理，本指令只在 FreeBSD 和 NetBSD 5.0+ 平台下有效。filter 可以设置为 dataready 或 httpready 。
+ bind:标识符，使用独立的bind() 处理此address:port，一般情况下，对于端口相同而IP地址不同的多个连接，Nginx 服务器将只使用一个监听指令，并使用 bind() 处理端口相同的所有连接。
+ ssl:标识符，设置会话连接使用 SSL模式进行，此标识符和Nginx服务器提供的 HTTPS 服务有关

> **server_name**：该指令用于指定虚拟主机

**语法格式如下**

```
server_name  [域名];
```

**通常分为以下两种**

+ 基于名称的虚拟主机配置
+ 基于 IP 地址的虚拟主机配置

**1、基于名称的虚拟主机配置**

+ 对于`server_name`来说，可以只有一个名称，也可以有多个名称，中间用空格隔开。而每个名字由两段或者三段组成，每段之间用 `.` 隔开。

  ```
  server_name 123.com www.123.com
  ```

+ 可以使用通配符 `*` ，但通配符只能用在由三段字符组成的首段或者尾端，或者由两端字符组成的尾端。

  ```
  server_name *.123.com www.123.*
  ```

+ 使用正则表达式，用 `~` 作为正则表达式字符串的开始标记，`^` 表示开头，`$` 表示结尾

  ```
  #表示匹配正则表达式，以www开头，紧跟着一个0~9之间的数字，在紧跟.123.co，最后跟着m
  server_name ~^www\d+\.123\.com$;
  ```

**由于server_name指令支持使用通配符和正则表达式两种配置名称的方式，因此在包含有多个虚拟主机的配置文件中，可能会出现一个名称被多个虚拟主机的server_name匹配成功。那么，来自这个名称的请求到底要交给哪个虚拟主机处理呢？Nginx服务器做出如下规定**

1. 准确匹配 server_name
2. 通配符在开始时匹配 server_name 成功
3. 通配符在结尾时匹配 server_name 成功
4. 正则表达式匹配 server_name 成功

**2、基于 IP 地址的虚拟主机配置**

语法结构和基于域名匹配一样，而且不需要考虑通配符和正则表达式的问题。

```
server_name 192.168.1.1
```

> location

一个 server 可以配置多个 location ，location 对`server_name`名称之外的字符串进行匹配，实现地址定向、数据缓存和应答控制等功能，还有许多第三方模块的配置也在这里进行

**语法如下**

```bash
#[]内为可选项
location [ = | ~ | ~* | ^~ ] uri {
	 #设置被代理服务器的地址，可以是主机名称、IP地址加端口号的形式。可以包含传输协议、主机名称或IP地址加端口号，URI等。
     proxy_pass http://127.0.0.1:8080;
     #这个指令用于设置请求寻找资源的根目录，默认值：index index.html
     root   html;	
     index  index.html index.htm;
}
```

+ `=`：用于不含正则表达式的 uri 前，要求请求字符串与 `uri` 严格匹配，如果匹配成功，就停止继续向下搜索并立即处理该请求。

+ `~`：用于表示 uri 包含正则表达式，并且区分大小写。

+ `~*`：用于表示 uri 包含正则表达式，并且不区分大小写。

+ `^~`：用于不含正则表达式的 uri 前，要求 Nginx 服务器找到标识 uri 和请求字符串匹配度最高的 location 后，立即使用此 location 处理请求，而不再使用 location 块中的正则 uri 和请求字符串做匹配。

**注意：如果 uri 包含正则表达式，则必须要有 ~ 或者 ~* 标识**

> index

通常该指令有两个作用：第一个是用户在请求访问网站时，请求地址可以不写首页名称；第二个是可以对一个请求，根据请求内容而设置不同的首页

```
	location / {
        root    /data/www;
        index   index.html index.php;
    }
    
    location ~ \.php$ {
        root    /data/www/test;
    }

```

上面的例子中，如果你使用example.org或www.example.org直接发起请求，那么首先会访问到“/”的location，结合root与index指令，会先判断/data/www/index.html是否存在，如果不，则接着查看
/data/www/index.php ，如果存在，则使用/index.php发起内部重定向，就像从客户端再一次发起请求一样，Nginx会再一次搜索location，毫无疑问匹配到第二个~ \.php$，从而访问到/data/www/test/index.php

> **其他常用配置**

```bash
#允许客户端请求的最大单文件字节数
client_max_body_size 10m; 

#缓冲区代理缓冲用户端请求的最大字节数，
client_body_buffer_size 128k;

#nginx跟后端服务器连接超时时间(代理连接超时)
proxy_connect_timeout 90;

#连接成功后，后端服务器响应时间(代理接收超时)
proxy_read_timeout 90;

#设置代理服务器（nginx）保存用户头信息的缓冲区大小
proxy_buffer_size 4k;

#proxy_buffers缓冲区，网页平均在32k以下的话，这样设置
proxy_buffers 32k;

#高负荷下缓冲大小（proxy_buffers*2）
proxy_busy_buffers_size 64k; 

#设定缓存文件夹大小，大于这个值，将从upstream服务器传
proxy_temp_file_write_size 64k;    
```



# 五、Nginx反向代理



**引言**

> Nginx 服务器的反向代理服务是其最常用的重要功能，由反向代理服务也可以衍生出很多与此相关的 Nginx 服务器重要功能，比如下一节中的负载均衡

当然在了解反向代理之前，我们需要先知道什么是代理以及什么是正向代理，请参考[Java设计模式之代理模式]()中对代理的定义：给某个对象提供一个代理对象，并由代理对象控制原对象的操作，**说通俗一点就是，Loki由于疫情不方便去吃烤肉，然后委托外卖小哥作代理，去帮我买烤肉吃**。

> **Nginx可以代理以下几种协议，用到最多的是Http代理服务器**

![](https://img-blog.csdnimg.cn/img_convert/59a8b509dc5a5ba9137d1f6e74aa66e4.png)

## 解释代理模式、正向代理、反向代理

> **理解正向代理**

+ **如果把局域网外的 Internet 想象成一个巨大的资源库，则局域网中的客户端要访问 Internet，则需要通过代理服务器来访问，这种代理服务就称为正向代理**

+ **举一个正向代理的例子，大家都知道，现在国内是访问不了 Google 的，那么怎么才能访问 Google呢？**

  :airplane:**Answer**:如果我们电脑的对外公网 IP 地址能变成美国的 IP 地址，我们就可以访问 Google了，`VPN` 就是这样产生的。我们在访问 Google 时，先连上 VPN 服务器将我们的 IP 地址变成美国的 IP 地址，然后就可以顺利的访问了。这里的 VPN 就是做正向代理的。正向代理服务器位于客户端和服务器之间，为了向服务器获取数据，客户端要向代理服务器发送一个请求，并指定目标服务器，代理服务器将目标服务器返回的数据转交给客户端。这里客户端是要进行一些正向代理的设置的

**不难看出，正向代理是帮忙处理客户端的请求**

> **理解反向代理**

+ **举一个反向代理的例子，我们在浏览器地址栏访问 `baidu.com`，百度服务器怎么处理我们的请求呢？** 

  :airplane:**Answer**:由于客户端对代理是无感知的，我们在地址栏直接访问域名即可，但是服务器端呢？由于百度那么高的并发访问数，肯定不可能将所有用户的请求都打到一个服务器处理，这样这台服务器肯定会扛不住的，这样子就引出了这次的主角——**`Nginx做反向代理服务器`**

**我们只需要将请求发送到反向代理服务器，由反向代理服务器去选择目标服务器获取数据后，再返回给客户端，此时反向代理服务器和目标服务器对外就是一个服务器，暴露的是代理服务器 地址，隐藏了真实服务器 IP 地址。**

**Nginx做反向代理示意图**

![image-20220428151310140](D:\桌面\P_picture_cahe\pIhSn1Ge52wFtMW.png)

总结为一句话：**正向代理代理客户端，反向代理代理服务器**

## 反向代理配置实例

> **以Tomcat为例，这是一个Servlet容器，就像Java运行在JVM上一样，Servlet程序运行在Tomcat中，[下载Tomcat压缩包](https://tomcat.apache.org/)，解压后进入`bin`目录，执行 `startup.bat` 文件，启动Tomcat服务，可通过 `localhost:8080` 端口访问Tomcat页面**
>
> *Nginx配置反向代理效果：通过`localhost`访问Tomcat页面*

修改 `nginx.conf` 配置文件，Http默认端口就是80端口，在Server块中配置监听`80`端口，请求会被代理到`127.0.0.1:8080`，所以可以通过`localhost`访问原来是`localhost:8080`的Tomcat首页了

```bash
	server {
		#默认监听端口
        listen       80;   
        server_name  localhost; #域名
        location / {
        	#设置被代理服务器的地址。可以是主机名称、IP地址加端口号的形式
            proxy_pass http://127.0.0.1:8080;
            index  index.html index.htm index.jsp;
        }
    }
```

双击`nginx.exe`，启动Nginx服务，这样所有的网络请求必须要先过Nginx这一关，于是我们就可以通过`localhost`访问到`localhost:8080`



​		

# 八、Nginx负载均衡

> 负载均衡是将负载分摊到多个操作单元上执行，从而提高服务的可用性和响应速度，带给用户更好的体验。对于Web应用，通过负载均衡，可以将一台服务器的工作扩展到多台服务器中执行，提高整个网站的负载能力。其本质采用一个调度者，保证所有后端服务器都将性能充分发挥，从而保持服务器集群的整体性能最优，这就是负载均衡

+ 现代通用负载均衡器，例如 NGINX Plus 和开源 NGINX 软件，通常在第 7 层（应用层）运行并充当完全[反向代理](https://www.nginx.com/resources/glossary/reverse-proxy-server)，现代商用硬件通常足够强大，因此第 4 层负载平衡所节省的计算成本不足以超过第 7 层负载平衡带来的更大灵活性和效率的好处	

### 常用负载均衡策略

> round-robin：轮询（生产环境常用）

+ 轮询为负载均衡中较为基础也较为简单的算法，它不需要配置额外参数。假设配置文件中共有 **M** 台服务器，该算法遍历服务器节点列表，并按节点次序每轮选择一台服务器处理请求。当所有节点均被调用过一次后，该算法将从第一个节点开始重新一轮遍历

+ 每个请求按时间顺序逐一分配到不同的后端服务器，如果后端服务器down掉，能自动剔除
+ 可以指定权重

```bash
upstream mybalance01 {
    server 192.168.0.14 weight=3;
    server 192.168.0.15 weight=7;
}
#权重越高，在被访问的概率越大，如上例，分别是30%，70%。
```

> least-connected：最少连接数。将下一个请求分配到连接数最少的那台服务器上



> ip-hash：基于客户端的IP地址。散列函数被用于确定下一个请求分配到哪台服务器上

+ 存在问题：在负载均衡系统中，假如用户在某台服务器上登录了，那么该用户第二次请求的时候，因为我们是负载均衡系统，每次请求都会重新定位到服务器集群中的某一个，那么已经登录某一个服务器的用户再重新定位到另一个服务器，其登录信息将会丢失

+ 采用`ip_hash`指令解决：如果客户已经访问了某个服务器，当用户再次访问时，会将该请求通过哈希算法，自动定位到该服务器。每个请求按访问ip的hash结果分配，这样每个访客固定访问一个后端服务器，可以解决session的问题
+ 缺点：存在单点风险，倘若我已经在192.168.0.14:88端口登录后，过段时间发现14服务器挂了（session时间未过期），那么这时候会访问到15服务器，那这时候需要重新登录，因为在拿14服务器上的JsessionId去15服务器请求发现不存在。

```
upstream backserver {
    ip_hash;
    server 192.168.0.14:88;
    server 192.168.0.15:80;
}
```

总结，实现简单，但是不好用

> [使用Redis做缓存解决Session一致性问题]()

# 九、搭建Nginx集群

