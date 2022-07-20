**引言**

> Nginx 是十分轻量级的`开源`、`高性能`、`高可靠`的**HTTP和反向代理服务器**，性能是 Nginx 最重要的考量，其占用内存少、处理高并发能力是十分强大的，能支持高达 5w 个并发连接数，最重要的是，Nginx 是免费的并可以商业化，配置使用也比较简单



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

  Tengine是由淘宝网发起的Web服务器项目。它在[Nginx](http://nginx.org/)的基础上，针对大访问量网站的需求，添加了很多高级功能和特性。Tengine的性能和稳定性已经在大型的网站如[淘宝网](http://www.taobao.com/)，[天猫商城](http://www.tmall.com/)等得到了很好的检验。它的最终目标是打造一个高效、稳定、安全、易用的Web平台。

## Nginx的功能和特性

> **Nginx有以下三个基础功能，本文主要做到快速上手Nginx，这些功能会在其他篇章详细整理**

+ 可以作为静态页面的 web 服务器
+ 反向代理
+ 负载均衡

> **Nginx 做为 HTTP 服务器，有以下几项基本特性：**

+ 支持热部署，几乎可以做到 7 * 24 小时不间断运行，即使运行几个月也不需要重新启动，还能在不间断服务的情况下对软件版本进行热更新
+ Nginx 以事件驱动的方式编写，有非常好的性能，同时可以做非常高效的反向代理、负载均衡服务器了

- 处理静态文件，索引文件以及自动索引；打开文件描述符缓冲．
- 无缓存的反向代理加速，简单的负载均衡和容错．
- FastCGI，简单的负载均衡和容错

## Nginx 的缺点

+ Nginx 仅能支持http、https和Email协议，这样就在适用范围上面小些

+ 对后端服务器的健康检查，只支持通过端口来检测，不支持通过 url来检测。不支持 Session 的直接保持，但能通过 ip_hash 来解决



# 二、Nginx安装与配置

**一般使用解压包方式安装，也是我们主要使用的安装方式，另外可以通过yum命令安装（下文有提到）**

## step1.Linux环境检查

Loki以后都将在Linux环境下学习整理，因此你需要有一定的Linux基础，不用担心，Loki在写这篇文章时对Linux也是一知半解的状态

**请自行准备好一个可以连通网络的虚拟机并且搭载CentOS7系统，你可以通过**

+ **`VMWare`虚拟机搭建CentOS7环境**

+ **购买云服务器，装载CentOS7镜像**

  推荐阅读[搭建云服务器Linux学习环境](https://blog.csdn.net/Night__breeze/article/details/123778708?spm=1001.2014.3001.5501#Linux_74)

**除此之外，我们需要安装`XShell`和`Xftp`这两款软件用于控制远端服务器**



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

## step2.安装Nginx

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

## step3.启动Nginx服务

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



# 三、安装的其他问题

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



## 附：其他安装方式

> **Windows下安装Nginx**

1. 下载windows版本

2. 启动Nginx，直接双击nginx.exe即可

3. 检查nginx是否启动成功

   直接在浏览器地址栏输入网址 [http://localhost:80](https://links.jianshu.com/go?to=http%3A%2F%2Flocalhost%3A80) 回车，出现Nginx页面说明启动成功！

> **yum命令安装**
>
> 一般都使用源码编译安装，很少采用yum安装

```c#
[root@centos-81 ~]# yum install -y gcc gcc-c++ pcre pcre-devel zlib zlib-devel openssl openssl-devel
[root@centos-81 ~]# useradd -u 8080 -M -s /sbin/nologin nginx
[root@centos-81 ~]# cd /usr/local/src/ ; wget https://nginx.org/download/nginx-1.14.2.tar.gz
[root@centos-81 src]# tar -xf nginx-1.14.2.tar.gz  
[root@centos-81 src]# cd nginx-1.14.2/
[root@centos-81 nginx-1.14.2]# ./configure --prefix=/opt/apps/nginx --user=nginx --group=nginx
[root@centos-81 nginx-1.14.2]# make -j 4 && make install
```



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

# 四、初识Nginx架构

## Nginx.conf最小配置文件详解

**`#`开头的表示注释内容，去掉所有注释内容，精简之后的内容Loki做了详细注释**

```bash
#配置文件开始到 events 块之间的内容,主要设置一些影响nginx服务器整体运行的配置指令
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
    include  mime.types; #服务器返回文件类型，否则客户端不知道返回的数据是什么类型
    #如果返回类型在mime.type中没有，则返回默认类型application/octet-stream
    default_type  application/octet-stream; 
    
    #简单理解免除了数据传输中拷贝的过程，在调优中详细解释
    sendfile    on;
    #先简单理解为保持连接超时时间，在反向代理中详解
    keepalive_timeout  65;
	
	#server 块：Nginx 服务器配置中最频繁的部分，代理、缓存和日志定义等绝大多数功能和第三方模块的配置都在这里
	#一个server代表一个虚拟主机 vhost，下文中有详解
    server {
    	#当前主机监听端口号
        listen   80;
        #填写主机名或者域名，涉及到域名解析，下文详解
        #可以理解为在浏览器请求localhost的80端口到达这个虚拟主机
        server_name  localhost; 
        
#一个 server 块可以配置多个 location 块，这块的主要作用是基于 Nginx 服务器接收到的请求字符串，对虚拟主机名称之外的字符串进行匹配，对特定的请求进行处理
    	#例如对于localhost/userInfo,匹配的就是 /userInfo
        location / { 
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

## server块的说明

```bash
	server {
        listen       80;   
        server_name  Loki.com;
        location / {
        	#设置被代理服务器的地址。可以是主机名称、IP地址加端口号的形式
            proxy_pass http://127.0.0.1:8080;
            index  index.html index.htm index.jsp;
        }
    }
```

这块和虚拟主机有密切关系，虚拟主机从用户角度看，和一台独立的硬件主机是完全一样的，该技术的产生是为了节省互联网服务器硬件成本，详情请见本文下方的`虚拟主机与域名绑定`

**每个 http 块可以包括多个 server 块，而每个 server 块就相当于一个虚拟主机。而每个 server 块也分为全局 server 块，以及可以同时包含多个 locaton 块。**

### 全局 server 块

+ **最常见的配置是本虚拟机主机的监听配置和本虚拟主机的名称或IP配置**

**详细字段的解释**

> **listen：该指令用于配置网络监听。主要有如下三种配置语法结构**

　一、配置监听的IP地址

```
listen address[:port] [default_server] [setfib=number] [backlog=number] [rcvbuf=size] [sndbuf=size] [deferred]
[accept_filter=filter] [bind] [ssl];
```

　　二、配置监听端口

```
	listen port[default_server] [setfib=number] [backlog=number] 			[rcvbuf=size] [sndbuf=size] [accept_filter=filter] 
    [deferred] [bind] [ipv6only=on|off] [ssl];
```

　三、配置 UNIX Domain Socket

```
listen unix:path [default_server]  [backlog=number] [rcvbuf=size] [sndbuf=size] [accept_filter=filter] 
[deferred] [bind] [ssl];
```

上面的配置看似比较复杂，其实使用起来是比较简单的：

```
1 listen *:80 | *:8080 #监听所有80端口和8080端口
2 listen  IP_address:port   #监听指定的地址和端口号
3 listen  IP_address     #监听指定ip地址所有端口
4 listen port     #监听该端口的所有IP连接
```

**分别解释每个选项的具体含义：**

　　1、address:IP地址，如果是 IPV6地址，需要使用中括号[] 括起来，比如[fe80::1]等。

　　2、port:端口号，如果只定义了IP地址，没有定义端口号，那么就使用80端口。

　　3、path:socket文件路径，如 var/run/nginx.sock等。

　　4、default_server:标识符，将此虚拟主机设置为 address:port 的默认主机。（在 nginx-0.8.21 之前使用的是 default 指令）

　　5、setfib=number:Nginx-0.8.44 中使用这个变量监听 socket 关联路由表，目前只对 FreeBSD 起作用，不常用。

　　6、backlog=number:设置监听函数listen()最多允许多少网络连接同时处于挂起状态，在 FreeBSD 中默认为 -1,其他平台默认为511.

　　7、rcvbuf=size:设置监听socket接收缓存区大小。

　　8、sndbuf=size:设置监听socket发送缓存区大小。

　　9、deferred:标识符，将accept()设置为Deferred模式。

　　10、accept_filter=filter:设置监听端口对所有请求进行过滤，被过滤的内容不能被接收和处理，本指令只在 FreeBSD 和 NetBSD 5.0+ 平台下有效。filter 可以设置为 dataready 或 httpready 。

　　11、bind:标识符，使用独立的bind() 处理此address:port，一般情况下，对于端口相同而IP地址不同的多个连接，Nginx 服务器将只使用一个监听指令，并使用 bind() 处理端口相同的所有连接。

　　12、ssl:标识符，设置会话连接使用 SSL模式进行，此标识符和Nginx服务器提供的 HTTPS 服务有关

> **server_name**：该指令用于虚拟主机的配置，通常分为以下两种

**1、基于名称的虚拟主机配置**

语法格式如下：

```
server_name   name ...;
```

一、对于name 来说，可以只有一个名称，也可以有多个名称，中间用空格隔开。而每个名字由两段或者三段组成，每段之间用“.”隔开。

```
server_name 123.com www.123.com
```

二、可以使用通配符“*”，但通配符只能用在由三段字符组成的首段或者尾端，或者由两端字符组成的尾端。

```
server_name *.123.com www.123.*
```

三、还可以使用正则表达式，用“~”作为正则表达式字符串的开始标记。

```
server_name ~^www\d+\.123\.com$;
```

该表达式“~”表示匹配正则表达式，以www开头（“^”表示开头），紧跟着一个0~9之间的数字，在紧跟“.123.co”，最后跟着“m”($表示结尾)

以上匹配的顺序优先级如下：

```
1 ①、准确匹配 server_name
2 ②、通配符在开始时匹配 server_name 成功
3 ③、通配符在结尾时匹配 server_name 成功
4 ④、正则表达式匹配 server_name 成功
```

**2、基于 IP 地址的虚拟主机配置**

语法结构和基于域名匹配一样，而且不需要考虑通配符和正则表达式的问题。

```
server_name 192.168.1.1
```

### location 块

**该指令用于匹配 URL，地址定向、数据缓 存和应答控制等功能，还有许多第三方模块的配置也在这里进行**

> **语法如下**

```
 location [ = | ~ | ~* | ^~] uri {
 }
```

　　1、= ：用于不含正则表达式的 uri 前，要求请求字符串与 uri 严格匹配，如果匹配成功，就停止继续向下搜索并立即处理该请求。

　　2、~：用于表示 uri 包含正则表达式，并且区分大小写。

　　3、~*：用于表示 uri 包含正则表达式，并且不区分大小写。

　　4、^~：用于不含正则表达式的 uri 前，要求 Nginx 服务器找到标识 uri 和请求字符串匹配度最高的 location 后，立即使用此 location 处理请求，而不再使用 location 块中的正则 uri 和请求字符串做匹配。

**注意：如果 uri 包含正则表达式，则必须要有 ~ 或者 ~* 标识**



### proxy_pass

该指令用于设置被代理服务器的地址。可以是主机名称、IP地址加端口号的形式。

语法结构如下：

```
proxy_pass URL;
```

URL 为被代理服务器的地址，可以包含传输协议、主机名称或IP地址加端口号，URI等。

```
proxy_pass  http://www.123.com/uri;
```

### index

该指令用于设置网站的默认首页。

语法为：

```
index  filename ...;
```

后面的文件名称可以有多个，中间用空格隔开。

```
index  index.html index.jsp;
```

通常该指令有两个作用：第一个是用户在请求访问网站时，请求地址可以不写首页名称；第二个是可以对一个请求，根据请求内容而设置不同的首页



# 五、虚拟主机和云服务器

## 辨析虚拟主机与云服务器

> **虚拟主机和云服务器简介**

+ **`虚拟主机是`一种在单一服务器或服务器群上，完成多网域服务项目的方式，虚拟主机就是把一台物理服务器划分系统资源成为多个虚拟服务器**

+ **`云服务器ECS`（Elastic Compute Service）是在群集服务器上虚似独立服务器，每一个ECS上都是一个独立的镜像系统，提高了服务器的安全性可靠性**

> **目前来看，虚拟主机已经退出了市场，并被云服务器所取代，原因如下**

+ 虚拟主机是很多网站共用IP，这样的话很容易受到彼此的影响。而云服务器是完全独立的IP，也就不用共享IP，能避免很多麻烦
+ 虚拟主机的资源也是大家一起用的，其实使用虚拟主机根本无法享受正常资源，如果遇到很多人抢占系统资源，那么你的网站就很容易出现不稳定、卡顿等状况，有时甚至影响到业务的开展。但云服务器是独享型，它与虚拟主机不一样，云服务器能够独享硬件服务器的独立线程，并且无CPU的限制，也没有争抢，就不会出现上述状况
+ 虚拟主机容易出现权限不足的情况

## 云服务器的一个简单使用场景

开发者可以租用虚拟主机或者云服务器，以供用户放置站点及应用组件，提供必要的数据存放和传输功能

**:rabbit2:现在基本都是买阿里云腾讯云之类的云服务器了，按需购买，还有教育优惠多合适啊**



用户在浏览器输入域名，被DNS服务器解析为IP地址，访问应用服务器上，基于http协议，服务器把数据报文返回，报文内容可能是网页、视频、图片等格式，被客户端解析

> **注意：客户端是无法通过后缀名识别文件究竟是什么的，需要WEB服务器给它说明**
>
> 例如Nginx做web服务器的话，上文Nginx.conf中`include  mime.types;`这个字段，就是Nginx作为web服务器时向前端说明它返回了什么信息

> **:rabbit: 附：`当你在浏览器中输入一个url时， 到页面展示到底发生了什么？`**
>
> 1. url指定的请求一旦发起，浏览器首先要做的事情就是解析这个域名，一般来说，浏览器会首先查看本地硬盘的 hosts 文件，看看其中有没有和这个域名对应的规则，如果有的话就直接使用 hosts 文件里面的 ip 地址
>
>    **本机hosts文件:进入`C:\Windows\System32\drivers\etc`目录，打开 `hosts` 文件，该文件指定了本机的ip映射规则**
>
>    如下所示，你可以自行指定ip地址和域名的映射关系
>
>    ```
>    # For example:
>          102.54.94.97     loki.com          # 注释内容
>          38.25.63.10      oliverloki.com    # 注释内容
>    ```
>
> 2. 如果在本地的 hosts 文件没有能够找到对应的 ip 地址，浏览器会发出一个 DNS请求到本地DNS服务器 。本地DNS服务器一般都是你的网络接入服务器商提供，比如中国电信，中国移动
>
> 3. 查询你输入的网址的DNS请求到达本地DNS服务器之后，本地DNS服务器会首先查询它的缓存记录，如果缓存中有此条记录，就可以直接返回结果，此过程是递归的方式进行查询。如果没有，本地DNS服务器还要向DNS根服务器进行查询
>
> 4. 根DNS服务器没有记录具体的域名和IP地址的对应关系，而是告诉本地DNS服务器，你可以到域服务器上去继续查询，并给出域服务器的地址。这种过程是迭代的过程
>
> 5. 本地DNS服务器继续向域服务器发出请求，在这个例子中，请求的对象是.com域服务器。.com域服务器收到请求之后，也不会直接返回域名和IP地址的对应关系，而是告诉本地DNS服务器，你的域名的解析服务器的地址。
>
> 6. 最后，本地DNS服务器向域名的解析服务器发出请求，这时就能收到一个域名和IP地址对应关系，本地DNS服务器不仅要把IP地址返回给用户电脑，还要把这个对应关系保存在缓存中，以备下次别的用户查询时，可以直接返回结果，加快网络访问。

## Nginx配置多站点——超详细

:rabbit2:Q: 举个栗子，Loki在腾讯云买了一台云服务器，它制定了一个**`公网ip地址`**,我们就可以通过这个公网ip来访问这台主机，一台主机绑定一个IP地址，这样子就有可能出现以下问题



:dart:A：对于这个主机，很多时候我们开启了一个站点，没有那么多人同时来访问，那么这台主机上的资源是不是就浪费了，而且由于技术进步，主机性能愈发优秀，这种情况下，那我们再一台主机上只跑一个站点，那是非常非常浪费的

> **解决方案**
>
> **我们可以让多个站点对应一台主机，也就是多个域名对应一个ip来解决上述问题**
>
> `Nginx` 服务器可以通过配置server块的内容，通过不同serever块监听端口来判断用户究竟是访问哪个域名，再返回指向不同的站点的目录

:rooster:**我们来实现这样一个简单的业务场景，你可以跟着Loki一起尝试一下**

**具体实现如下：**

1. 新增一个站点的server块配置，这是完整的nginx.conf文件，相对于最小配置只增加了一个Server块

   ```bash
   worker_processes  1;
   events {
       worker_connections  1024;
   }
   http {
       include       mime.types;
       #当然了，Server块可以在其他地方写，在这里引入，会方便管理，Loki选择偷懒直接在下面写了
       default_type  application/octet-stream;
       sendfile        on;
       keepalive_timeout  65;
   
       server {
           listen   80;
           # 站点域名，Loki的云服务器ip没有进行解析，所以就是本机localhost
           server_name  localhost;
           location / {
               root   html;
               index  index.html index.htm;
           }
           error_page   500 502 503 504  /50x.html;
           location = /50x.html {
               root   html;
           }
       }
       server {
           listen  88;            # 监听端口
           # 站点域名，Loki的云服务器ip没有进行解析，所以就是本机localhost
           server_name localhost;  
           location / {
               #页面的路径，这个是系统资源的绝对路径，需要你自己写
               root   /home/loki/blog; 
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

2. 在你server块中配置了资源路径`/home/loki/www/blog`，因此Loki新建了这个目录，存放了资源文件

   即：/home/loki/www/blog/index.html

   ```html
   <!DOCTYPE html>
   <html lang="en">
   <head>
       <meta charset="UTF-8">
       <meta http-equiv="X-UA-Compatible" content="IE=edge">
       <meta name="viewport" content="width=device-width, initial-scale=1.0">
       <title>Nginx88端口</title>
   </head>
   <body>
       <h1>88端口</h1>
       <h1>Nginx实现同一ip多站点配置</h1>
   </body>
   </html>
   ```

3. 重启Nginx服务

+ `nginx -s reload`
+ 如果注册了系统服务，可以使用`systemctl reload nginx`重启，使用`systemctl status nginx`查看是否重启成功

> **访问，运行结果截图**
>
> 这里需要注意，如果端口访问不了，有可能是防火墙的原因

访问默认端口出现Nginx页面

<img src="https://s2.loli.net/2022/04/27/ZvXdguexNKpLY4h.png" alt="image-20220427181348760" style="zoom:67%;" />

访问88端口出现自定义页面

<img src="https://s2.loli.net/2022/04/27/q5NAau7U8mVPjOR.png" alt="image-20220427181332519" style="zoom:80%;" />

> 你可以通过这个在网上部署自己的静态博客，通过ip或域名访问，向下面这样
>
> ![image-20220427182351962](D:\桌面\P_picture_cahe\5JyoQOsjDNlgRSH.png)

# 六、Nginx基础命令

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



# 七、Nginx做反向代理

**引言**

> Nginx 服务器的反向代理服务是其最常用的重要功能，由反向代理服务也可以衍生出很多与此相关的 Nginx 服务器重要功能，比如下一篇会介绍的负载均衡。本篇博客我们会先介绍 Nginx 的反向代理，当然在了解反向代理之前，我们需要先知道什么是代理以及什么是正向代理

> 代理涉及到设计模式的理念，可以选择移步看一下这篇文章[Java设计模式之代理模式]()，
>
> 代理模式是这样定义的：给某个对象提供一个代理对象，并由代理对象控制原对象的操作，**说通俗一点就是，Loki由于疫情不方便去吃烤肉，然后委托外卖小哥作代理，去帮我买烤肉吃**

## 理解正向代理

> **正向代理**
>
> **如果把局域网外的 Internet 想象成一个巨大的资源库，则局域网中的客户端要访问 Internet，则需要通过代理服务器来访问，这种代理服务就称为正向代理**

举一个正向代理的例子

:rabbit:**Q:大家都知道，现在国内是访问不了 Google 的，那么怎么才能访问 Google呢？**



:airplane:**A**:如果我们电脑的对外公网 IP 地址能变成美国的 IP 地址，我们就可以访问 Google了，`VPN` 就是这样产生的。我们在访问 Google 时，先连上 VPN 服务器将我们的 IP 地址变成美国的 IP 地址，然后就可以顺利的访问了

+ 这里的 VPN 就是做正向代理的。正向代理服务器位于客户端和服务器之间，为了向服务器获取数据，客户端要向代理服务器发送一个请求，并指定目标服务器，代理服务器将目标服务器返回的数据转交给客户端。这里客户端是要进行一些正向代理的设置的

+ **不难看出，正向代理是帮忙处理客户端的请求**

## 理解反向代理

> **反向代理**

举一个反向代理的例子

:rabbit:**Q:我们在浏览器地址栏访问 `baidu.com`，百度服务器怎么处理我们的请求呢？** 



:airplane:**A**:由于客户端对代理是无感知的，我们在地址栏直接访问域名即可，但是服务器端呢？由于百度那么高的并发访问数，肯定不可能将所有用户的请求都打到一个服务器处理，这样这台服务器肯定会扛不住的，这样子就引出了这次的主角——**`Nginx做反向代理服务器`**

+ **我们只需要将请求发送到反向代理服务器，由反向代理服务器去选择目标服务器获取数据后，再返回给客户端，此时反向代理服务器和目标服务器对外就是一个服务器，暴露的是代理服务器 地址，隐藏了真实服务器 IP 地址。**

**Nginx做反向代理示意图**

![image-20220428151310140](D:\桌面\P_picture_cahe\pIhSn1Ge52wFtMW.png)

总结为一句话：**正向代理代理客户端，反向代理代理服务器**



## 反向代理配置实例

**这是网上比较常见的案例，但是我感觉大多数写的都不是很容易理解，Loki在这里重新整理一下**

**你需要具备两个前提条件**

+ 了解Tomcat的使用
+ 为了方便操作，这是Window环境下的一次演示，所以需要在Windows下安装Nginx和Tomcat

> **以Tomcat为例，这是一个Servlet容器，就像Java运行在JVM上一样，Servlet程序运行在Tomcat中**

[下载Tomcat压缩包](https://tomcat.apache.org/)，解压后进入`bin`目录，执行 `startup.bat` 文件，这样Tomcat服务就启动了

**通过 `localhost:8080` 端口访问这个web服务器**

![image-20220428182553093](D:\桌面\P_picture_cahe\KHzuiyRca6OwDA5.png)

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

# 八、Nginx负载均衡

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



# 九、搭建Nginx集群

