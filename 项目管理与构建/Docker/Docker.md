**引言**

Docker解决了`"这段代码在我机器上没问题啊"`的问题

> **`Docker` 是世界领先的软件容器平台。 基于 `Go` 语言 并遵从 `Apache2.0` 协议开源，基于 Linux 内核 提供的 `CGroup` 功能和 `namespace` 来实现的，以及 AUFS 类的 `UnionFS(联合文件系统)` 等技术，对进程进行封装隔离，属于操作系统层面的虚拟化技术。由于隔离的进程独立于宿主和其它的隔离的进程，因此也称其为容器**

注：

+ 阅读本文之前，你需要有一定的Linux基础

+ Docker 从 17.03 版本之后分为 CE（Community Edition: 社区版） 和 EE（Enterprise Edition: 企业版），学习 Docker 用社区版就可以了

@[toc]



# 一、Docker基础架构与概念

> Docker架构





> Docker 包括三个基本概念
>

- **镜像（Image）**：Docker 镜像（Image），就相当于是一个 root 文件系统。比如官方镜像 ubuntu:16.04 就包含了完整的一套 Ubuntu16.04 最小系统的 root 文件系统。
- **容器（Container）**：镜像（Image）和容器（Container）的关系，就像是面向对象程序设计中的类和实例一样，镜像是静态的定义，容器是镜像运行时的实体。容器可以被创建、启动、停止、删除、暂停等。
- **仓库（Repository）**：仓库可看成一个代码控制中心，用来保存镜像

**Docker 使用客户端-服务器 (C/S) 架构模式，使用远程API来管理和创建Docker容器**

## 镜像（image）的概念

Docker 镜像，也称为 `image` 是一个特殊的文件系统，除了提供容器运行时所需的程序、库、资源、配置等文件外，还包含了一些为运行时准备的一些配置参数（如匿名卷、环境变量、用户等），镜像不包含任何动态数据，其内容在构建之后也不会被改变

> Docker 镜像充分利用 **`Union FS`** 的技术，将其设计为**分层存储的架构** 

**镜像构建时，会一层层构建，前一层是后一层的基础。每一层构建完就不会再发生改变，后一层上的任何改变只发生在自己这一层。** 比如，删除前一层文件的操作，实际不是真的删除前一层的文件，而是仅在当前层标记为该文件已删除。在最终容器运行的时候，虽然不会看到这个文件，但是实际上该文件会一直跟随镜像。因此，在构建镜像的时候，需要额外小心，每一层尽量只包含该层需要添加的东西，任何额外的东西应该在该层构建结束前清理掉。

分层存储的特征还使得镜像的复用、定制变的更为容易。甚至可以用之前构建好的镜像作为基础层，然后进一步添加新的层，以定制自己所需的内容，构建新的镜像

## 容器(container)的概念

以前的开发和部署是分开的，WEB应用的交付标准是 jar 包或者 war 包，这样就涉及到运行环境不同导致的bug，而容器的出现则解决了这个问题，容器赋予了软件独立性，使其免受外在环境差异，使得开发人员以便他们专注在真正重要的事情上：构建杰出的软件

> **一句话概括容器：**容器镜像是轻量的、可执行的独立软件包

用户可以方便地创建和使用容器，把自己的应用放入容器。容器还可以进行版本管理、复制、分享、修改，就像管理普通的代码一样，如下图所示

![image-20220506144929167](https://img-blog.csdnimg.cn/img_convert/d5d7921e3791d9c2c6d22b8cf8169921.png)

## 仓库(repository)的概念

镜像构建完成后，可以很容易的在当前宿主上运行，但是， **如果需要在其它服务器上使用这个镜像，我们就需要一个集中的存储、分发镜像的服务，Docker Registry 就是这样的服务。**

一个 Docker Registry 中可以包含多个仓库（Repository）；每个仓库可以包含多个标签（Tag）；每个标签对应一个镜像。所以说：**镜像仓库是 Docker 用来集中存放镜像文件的地方类似于我们之前常用的代码仓库。**

通常，**一个仓库会包含同一个软件不同版本的镜像**，而**标签就常用于对应该软件的各个版本** 。我们可以通过`<仓库名>:<标签>`的格式来指定具体是这个软件哪个版本的镜像。如果不给出标签，将以 `latest` 作为默认标签.。

**这里补充一下 Docker Registry 公开服务和私有 Docker Registry 的概念：**

**Docker Registry 公开服务** 是开放给用户使用、允许用户管理镜像的 Registry 服务。一般这类公开服务允许用户免费上传、下载公开的镜像，并可能提供收费服务供用户管理私有镜像。

最常使用的 Registry 公开服务是官方的 **Docker Hub** ，这也是默认的 Registry，并拥有大量的高质量的官方镜像，网址为：[https://hub.docker.com 。官方是这样介绍 Docker Hub 的：

> Docker Hub 是 Docker 官方提供的一项服务，用于与您的团队查找和共享容器镜像。

比如我们想要搜索自己想要的镜像：

![](https://s2.loli.net/2022/05/21/SNiFE15r6jyYs42.png)

在 Docker Hub 的搜索结果中，有几项关键的信息有助于我们选择合适的镜像：

- **OFFICIAL Image** ：代表镜像为 Docker 官方提供和维护，相对来说稳定性和安全性较高。
- **Stars** ：和点赞差不多的意思，类似 GitHub 的 Star。
- **Dowloads** ：代表镜像被拉取的次数，基本上能够表示镜像被使用的频度。

当然，除了直接通过 Docker Hub 网站搜索镜像这种方式外，我们还可以通过 `docker search` 这个命令搜索 Docker Hub 中的镜像，搜索的结果是一致的。



在国内访问**Docker Hub** 可能会比较慢国内也有一些云服务商提供类似于 Docker Hub 的公开服务。比如 [时速云镜像库open in new window](https://www.tenxcloud.com/)、[网易云镜像服务open in new window](https://www.163yun.com/product/repo)、[DaoCloud 镜像市场open in new window](https://www.daocloud.io/)、[阿里云镜像库open in new window](https://www.aliyun.com/product/containerservice?utm_content=se_1292836)等



除了使用公开服务外，用户还可以在 **本地搭建私有 Docker Registry** 。Docker 官方提供了 Docker Registry 镜像，可以直接使用做为私有 Registry 服务。开源的 Docker Registry 镜像只提供了 Docker Registry API 的服务端实现，足以支持 docker 命令，不影响使用。但不包含图形界面，以及镜像维护、用户管理、访问控制等高级功能



## Docker容器的优点

+ **安全**：容器是完全使用沙箱机制，相互之间不会有任何接口，不仅限于彼此隔离，还独立于底层的基础设施。因此应用出现问题，也只是单个容器的问题，而不会波及到整台机器
+ **标准**：Docker 容器基于开放式标准，能够在所有主流 Linux 版本、Microsoft Windows 以及包括 VM、裸机服务器和云在内的任何基础设施上运行
+ **轻量**：在一台机器上运行的多个 Docker 容器可以共享这台机器的操作系统内核；它们能够迅速启动，只需占用很少的计算和内存资源。镜像是通过文件系统层进行构造的，并共享一些公共文件。这样就能尽量降低磁盘用量，并能更快地下载镜像
+ **便捷**：可以很轻易的将在一个平台上运行的应用，迁移到另一个平台上，而不用担心运行环境的变化导致应用无法正常运行的情况

**[《Docker 从入门到实践](https://yeasy.gitbook.io/docker_practice/introduction/why) 这本开源书籍中也已经给出了使用 Docker 的原因**

![](https://s2.loli.net/2022/05/21/NfZjIcn7i2KXdq1.png)

# 二、Docker安装卸载与配置

如果宿主机已经安装过Docker，需要先进行卸载

## 卸载旧版本的Docker

卸载依赖

```shell
yum remove docker \
                  docker-client \
                  docker-client-latest \
                  docker-common \
                  docker-latest \
                  docker-latest-logrotate \
                  docker-logrotate \
                  docker-engine        
```

删除资源

```
rm -rf /var/lib/docker
```

`/var/lib/docker`是docker的默认工作目录



## 安装与启动第一个Docker实例

```bash
1、#安装yum-utils包（提供yum-config-manager 实用程序）并设置稳定存储库。
sudo yum install -y yum-utils

2、#设置镜像的仓库，使用国内阿里云镜像
sudo yum-config-manager \
	--add-repo \
	http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo
#更新yum软件包索引
yum makecache fast

4、#安装docker相关源 docker-ce 社区版 ee 企业版
sudo yum install docker-ce docker-ce-cli containerd.io

5、#启动docker
#docker默认注册了系统级别的服务，可以通过以下命令启动
systemctl start docker

6、#查看docker版本
docker version

7、#运行hello-world
docker run hello-world
```

![image-20220504184330467](https://img-blog.csdnimg.cn/img_convert/8e2ae58e5ad88a852c0b15796783eb28.png)



## Docker镜像加速

国内从 DockerHub 拉取镜像有时会遇到困难，此时可以配置镜像加速器。Docker 官方和国内很多云服务商都提供了国内加速器服务，例如：

- 科大镜像：**https://docker.mirrors.ustc.edu.cn/**
- 网易：**https://hub-mirror.c.163.com/**
- 阿里云：**https://www.aliyun.com/**
- 七牛云加速器：**https://reg-mirror.qiniu.com**

> Loki以配置阿里云容器镜像为例，在阿里云官网的产品一栏中找到阿里云容器镜像服务

找到镜像加速器，按照文档操作即可

![image-20220504224615570](https://img-blog.csdnimg.cn/img_convert/82105e9e5f79a26dcbf74f5949fe07ac.png)

daemon.json

```
{
  "registry-mirrors": ["https://v4e316go.mirror.aliyuncs.com"]
}
```

```bash
sudo systemctl daemon-reload
sudo systemctl restart docker
```

# 三、Docker常用命令汇总

**帮助命令**

| 命令                         | 描述                                 |
| :--------------------------- | ------------------------------------ |
| docker port                  | 快捷地查看端口的绑定情况             |
| docker [你不会的命令] --help | 类似于Liunx的 man 命令               |
| docker version               | docker版本信息                       |
| docker info                  | 系统级别的信息，包括镜像和容器的数量 |
| docker stats                 | 查看系统资源占用                     |

**镜像命令**

使用 `REPOSITORY:TAG` 来唯一标识一个镜像

| 命令                               | 描述                   |
| :--------------------------------- | ---------------------- |
| docker images                      | 查看本地镜像           |
| docker pull                        | 拉取Docker仓库的镜像   |
| docker inspect [容器id]            | 查看镜像的元数据       |
| docker rmi -f [镜像id]             | 通过镜像id删除指定镜像 |
| docker rmi -f $(docker images -aq) | 删除所有镜像           |
| docker save -o                     | 保存镜像成为一个tar包  |

**容器命令**

| 命令                                                   | 描述                     |
| :----------------------------------------------------- | ------------------------ |
| docker run -it -p [主机端口:容器端口] [REPOSITORY:TAG] | 运行一个容器             |
| docker run -d -p [主机端口:容器端口] [REPOSITORY:TAG]  | 后台运行                 |
| docker start [容器id]                                  | 启动本地容器             |
| docker restart [容器id]                                | 重启容器                 |
| docker stop [容器id]                                   | 停止运行容器             |
| exit                                                   | 直接退出容器并关闭       |
| Ctrl + P + Q                                           | 容器不关闭退出           |
| docker top [容器id]                                    | 查看容器中进程信息       |
| docker ps                                              | 列出当前正在运行的容器   |
| docker ps -a                                           | 列出所有容器，包括停止的 |
| docker exec -it [容器id] [bin\|bash]                   | 进入容器，修改配置       |
| docker rm -f [容器id]                                  | 删除指定容器             |
| docker rm -f $(docker ps -aq)                          | 删除所有容器             |
| docker restart [容器id]                                | 重启容器                 |
| docker logs -tf --tail [日志条数number] [容器id]       | 显示日志                 |




# 四、Docker操作详解

## Docker run详解

> 流程分析

```
docker run hello-world
```

![image-20220504230714676](https://img-blog.csdnimg.cn/img_convert/d90ff2815edcc132bfffb430250608ce.png)

> 容器启动命令

```bash
docker run [可选参数] [image]
 
# 参数说明
--name="loki"   容器名字    tomcat01    tomcat02    用来区分容器
-d      后台方式运行
-it     使用交互方式运行，进入容器查看内容
-p      指定端口映射     -p [宿主机端口]:[容器端口]
-P      随机指定端口
```

以下是一个实例

```bash
# 测试，启动并进入容器
[root@loki ~]# docker run -it centos /bin/bash
```

+ **`/bin/bash`**参数说明:  我们希望有个交互式 Shell，因此用的是 /bin/bash

**进入docker容器内部的centos**

```
[root@loki /]# ls   # 查看容器内的centos，基础版本，很多命令是不完善的
bin  etc   lib    lost+found  mnt  proc  run   srv  tmp  var
dev  home  lib64  media       opt  root  sbin  sys  usr
# 从容器中退回主机
[root@loki /]# exit
exit
[root@loki /]# ls
bin   dev  fanfan  lib    lost+found  mnt  proc  run   srv  tmp  var
boot  etc  home    lib64  media       opt  root  sbin  sys  usr
```

## Docker 镜像操作详解

> 当运行容器时，使用的镜像如果在本地中不存在，docker 就会自动从 docker 镜像仓库中下载，默认是从 Docker Hub 公共镜像源下载

本小节有两个内容

- 管理和使用本地 Docker 主机镜像
- 创建镜像

### 展示所有镜像

使用 **`docker images`** 来列出本地主机上的镜像

> 查看所有本地主机上的镜像

```bash
docker images 
# 可选项
-a      # 列出所有镜像
-q    # 只显示镜像的id
```

运行结果

```bash
[root @loki] # docker images
REPOSITORY    TAG       IMAGE ID       CREATED        SIZE
hello-world   latest    feb5d9fea6a5   7 months ago   13.3kB
# 解释
REPOSITORY      # 镜像的仓库
TAG             # 镜像的标签
IMAGE ID        # 镜像的ID
CREATED         # 镜像的创建时间
SIZE            # 镜像的大小
```

同一仓库源可以有多个 TAG，代表这个仓库源的不同个版本，我们使用 `REPOSITORY:TAG` 来唯一标识一个镜像

### 获取一个新的镜像

执行 `docker run [镜像名时]`，当镜像不存在时 Docker 就会自动下载这个镜像。如果我们想预先下载这个镜像，我们可以使用 `docker pull` 命令来下载它

```bash
docker pull [镜像名:tag]
# 下载镜像，
docker pull mysql
# 等价于
docker pull mysql
docker pull docker.io/library/mysql:latest
# 指定版本下载
docker pull mysql:5.7
# 如果不写tag，默认就是latest
Using default tag: latest    
```

example:

```
docker pull centos:7.6
```

下载完成后，我们就可以通过`docker run`实例化这个镜像了

```
docker run nginx
```

### 在Docker Hub查找镜像

我们可以从 [Docker Hub](https://hub.docker.com/) 网站来搜索镜像，然后通过 `docker pull` 拉取

也可以使用 `docker search` 命令来搜索镜像，如果你不配置Docker镜像加速，默认源在国外，是比较慢的

```bash
docker search [镜像名称]  
# 可选项
docker search --filter=STARS=3000     # 搜索出来的镜像就是STARS大于3000的
```

运行结果的解释

```bash
NAME    DESCRIPTION     STARS      OFFICIAL         AUTOMATED
名称	  描述            Star数量    是否官方         
```

example: 

```
docker search nginx
```

### DIY一个满足需求的镜像

当我们从 docker 镜像仓库中下载的镜像不能满足我们的需求时，我们可以通过以下两种方式对镜像进行更改。

- 从已经创建的容器中更新镜像，并且`docker commit`这个镜像
- 使用 Dockerfile 指令来创建一个新的镜像

> **更新镜像操作**

**更新镜像之前，我们需要使用镜像来创建一个容器**

```
docker run -it [镜像名:标签名] /bin/bash
```

在运行的容器内使用 **apt-get update** 命令进行更新

```
apt-get update
```

**在完成操作之后，输入 exit 命令来退出这个容器**



**通过命令 docker commit 来提交容器副本**

```
docker commit -m="提交内容的描述信息" -a="loki" [容器ID] <仓库名>:<标签>
```

各个参数说明：

- **-m:** 提交的描述信息
- **-a:** 指定镜像作者
- **e218edb10161：**容器 ID
- **runoob/ubuntu:v2:** 指定要创建的目标镜像名

通过 docker ps 查看新镜像，通过 docker run 来运行新镜像



> **使用 Dockerfile 指令来创建一个新的镜像，DokcerFile详细内容请看第四节**

1. 编写一个dockerFile文件
2. docker build 构建成为一个镜像
3. docker run 运行镜像
4. docker push 发布镜像（DockerHub、阿里云镜像）

```
docker build [dockerfile]
```

我们使用命令 **`docker build`** ， 从零开始来创建一个新的镜像。为此，我们需要创建一个 Dockerfile 文件，其中包含一组指令来告诉 Docker 如何构建我们的镜像

## Docker 容器操作详解

### 查看我们正在运行的容器

```
docker ps #列出正在运行的容器
-a #列出历史运行的容器
-p #只显示容器id
```

> **BUG说明：如果执行docker ps，发现后台运行的容器停止了**
>
> docker 容器使用后台运行， 就必须要有一个前台进程，docker发现没有应用，就会自动停止
>
> nginx，容器启动后，发现自己没有提供服务，就会立即停止，就是没有程序了

### 操作后台运行的容器

在使用 **-d** 参数时，容器启动后会进入后台。此时想要进入容器，可以通过以下指令进入：

- **docker attach**
- **docker exec**：推荐使用 docker exec 命令，因为此命令会退出容器终端，但不会导致容器的停止

```
docker exec -it [imageId] /bin/bash
```

### 查看WEB应用的日志

```bash
docker logs -tf --tail [number] [容器id]
参数解释：
-tf                 # 显示日志
--tail number       # 显示日志条数
```

### 从容器中拷贝文件到主机

```
docker cp [容器id:容器内路径]  [目的地主机路径]
```

example

```
docker cp 7af535f807e0:/home/Test.java  /home 
```

### 导出和导入容器

> **导出容器**

```
docker export [容器id] > centos.tar
```

> **导入容器快照**

```
cat [tar文件的相对路径] | docker import - [仓库名:标签名]
```

```
cat docker/centos.tar | docker import - test/centos:v1
```

## Docker 容器连接入门

> **场景：Docker运行在一台远程Linux服务器上，容器中可以运行一些网络应用，要让外部网络通过公网ip地址也可以访问这些应用，可以通过 `docker run` 命令的 -P 或 -p 参数来指定端口映射**

两种方式的区别是:

- **-P :**(大写P)是容器内部端口**随机**映射到主机的端口

```
docker run  -P  8866:80 nginx
```

- **-p :** 是容器内部端口绑定到**指定**的主机端口

```
docker run  -p  8866:80 nginx
```

**另外，我们可以指定容器绑定的网络地址，比如绑定 127.0.0.1**

```
docker run -d -p 127.0.0.1:80 [nginx]
```

这样我们就可以通过访问 127.0.0.1:5001 来访问容器的 80 端口



**上面的例子中，默认都是绑定 tcp 端口，如果要绑定 UDP 端口，可以在端口后面加上 /udp**

```
docker run -d -p --name lokiTest 8866:80/udp nginx
```

命令可以让我们快捷地查看端口的绑定情况

> 下面我们来实现通过端口连接到一个 docker 容器

1. 首先在云服务器防火墙中开启8866端口
2. 执行以下命令

```
docker run -d -p 8866:80 nginx
```

结果如下图所示，可以看到我们通过`公网ip地址+端口号`访问到了Docker容器内的网络应用

![image-20220505025740006](https://img-blog.csdnimg.cn/img_convert/5c50b08df54220b06e5ea4f463ec44a3.png)

> Docker容器互联

端口映射并不是唯一把 docker 连接到另一个容器的方法。

docker 有一个连接系统允许将多个容器连接在一起，共享连接信息。

docker 连接会创建一个父子关系，其中父容器可以看到子容器的信息

如果你有多个容器之间需要互相连接，推荐使用 Docker Compose，后面会介绍



## Docker远程仓库与镜像发布

仓库（Repository）是集中存放镜像的地方

Loki主要整理了 [Docker Hub](https://hub.docker.com/) 做远程仓库。当然不止 docker hub，只是远程的服务商不一样，操作都是一样的

:rocket: **Dockerhub 和 Github 有点像，可以对比理解**



**首先需要在[Docker Hub](https://hub.docker.com/)免费注册一个 Docker 账号**

> 登录操作，登录需要输入用户名和密码，登录成功后，我们就可以从 docker hub 上拉取自己账号下的全部镜像

```
docker login
```

> 退出 docker hub 可以使用以下命令

```
docker logout
```

> 拉取镜像

你可以通过 docker search 命令来查找官方仓库中的镜像，并利用 docker pull 命令来将它下载到本地

> 推送镜像

用户登录后，可以通过 docker push 命令将自己的镜像推送到 Docker Hub。

以下命令中的 username 请替换为你的 Docker 账号用户名

```
docker push [username]/[仓库名:标签名]
```



# 五、Docker容器数据卷

> 容器数据卷

- `数据卷` 是一个可供一个或多个容器使用的特殊目录，它绕过 UFS，可以提供很多有用的特性：
  - `数据卷` 可以在容器之间共享和重用
  - 对 `数据卷` 的修改会立马生效
  - 对 `数据卷` 的更新，不会影响镜像
  - `数据卷` 默认会一直存在，即使容器被删除

> Docker 提供了3种持久化数据的方式，最好使用`volumes`

1. **volumes**：存于主机文件系统中的某个区域，由Docker管理（/var/lib/docker/volumes/ on linux）。非Docker进程不应该修改这些数据。**这是Docker中持久化数据的最好方式**

2. ~~**bind mount**~~：存于主机文件系统中的任意位置。非Docker进程可以修改这些数据

   ```bash
   docker run -it -v /宿主机绝对路径目录:/容器内目录 -p 主机端口:容器内端口 [镜像名：标签名] /bin/bash
   #通过查看镜像的元数据查看是否挂载成功
   docker inspect 容器ID
   ```

3. ~~**tmpfs mount**（Linux中）~~：存于内存中（注意，并不是持久化到磁盘）。在容器的生命周期中，它能被容器用来存放非持久化的状态或敏感信息

> volumes方式持久化

+ 数据卷是被设计用来持久化数据的，它的生命周期独立于容器，Docker 不会在容器被删除后自动删除 数据卷，并且也不存在垃圾回收这样的机制来处理没有任何容器引用的 数据卷。如果需要在删除容器的同时移除数据卷。可以在删除容器的时候使用 `docker rm -v` 这个命令

```bash
#创建一个数据卷
docker volume create [卷名]
#查看所有的数据卷
docker volume ls
#查看指定 数据卷 的信息,数据卷 信息在 "Mounts" Key 下面
docker volume inspect [卷名]
#删除数据卷
docker volume rm [卷名]
#无主的数据卷可能会占据很多空间，要清理请使用以下命令
docker volume prune
```

**启动一个挂载数据卷的容器**

在用 `docker run` 命令的时候，使用 `--mount` 标记（Docker17.06或更高）来将 `数据卷` 挂载到容器里。在一次 `docker run` 中可以挂载多个 `数据卷`。

下面创建一个名为 `web` 的容器，并加载一个 `数据卷` 到容器的 `/usr/share/nginx/html` 目录

```
docker run -d -P --name [web] --mount source=[volume],target=/usr/share/nginx/html nginx:alpine
```

1. 挂载类型：key为`type`，value为bind、volume或tmpfs
2. 挂载源：key为`source`或`src`，对于命名卷，value为卷名，对于匿名卷，则忽略
3. 容器中的挂载点：key为`destination`、dst或target，value为容器中的路径
4. 读写类型：value为`readonly`，没有key
5. `volume-opt`选项，可以出现多次。比如volume-driver=local,volume-opt=type=nfs,…



# 六、DockerFile

**Dockerfile 是一个用来构建镜像的文本文件，文本内容包含了一条条构建镜像所需的指令和说明**

> DokerFile有以下几个特性

1. 每个保留关键字（指令）都是必须大写字母
2. 执行从上到下顺序执行
3. `#` 表示注释
4. 每个指令都会创建提交一个新的镜像层，并提交

> DockerFile的关键字如下所示

```bash
FROM            # 基础镜像，一切从这里开始构建，定制的镜像是基于 FROM 的镜像
MAINTAINER      # 镜像是谁写的， 姓名+邮箱
RUN             # 镜像构建的时候需要运行的命令
ADD             # 步骤， tomcat镜像， 这个tomcat压缩包！添加内容
WORKDIR         # 镜像的工作目录
VOLUME          # 挂载的目录
EXPOSE          # 暴露端口配置
CMD             # 指定这个容器启动的时候要运行的命令，只有最后一个会生效可被替代
ENTRYPOINT      # 指定这个容器启动的时候要运行的命令， 可以追加命令
ONBUILD         # 当构建一个被继承DockerFile 这个时候就会运行 ONBUILD 的指令，触发指令
COPY            # 类似ADD, 将我们文件拷贝到镜像中
ENV             # 构建的时候设置环境变量！
```

## 构建一个DockerFile

新建一个dockerfile目录来存放文件

```
cd /home
mkdir dockerfile
vim diycentos
```

`DockerFile`内容如下，主要是对官网的centos镜像做了一些增强，比如说不支持vim命令等

```bash
#基于官方centos镜像构建
FROM centos

#维护者信息，格式一半是姓名+邮箱
MAINTAINER loki<oliverloki@foxmail.com>

#环境变量的配置
ENV WORKPATH /usr/local
WORKDIR WORKPATH

#执行以下命令
RUN yum -y install vim \ && yum -y install net-tools

#暴露80端口
EXPOSE 80

#容器启动的时候要运行的命令
CMD /bin/bash
```

开始构建镜像，语法如下

```bash
docker build [OPTIONS] PATH | URL 
#OPTIONS说明：
#  -f :指定要使用的Dockerfile路径；
#  -t: 镜像的名字及标签，通常 name:tag 格式；可以在一次构建中为一个镜像设置多个标签。
```

```
docker build -f diycentos -t test .
```

启动该镜像后，可以发现它可以使用 ifconfig 等操作了



**注：最后的 . 代表本次执行的上下文路径**



> **那么什么是上下文路径呢？**

上下文路径，是指 docker 在构建镜像，有时候想要使用到本机的文件（比如复制），docker build 命令得知这个路径后，会将路径下的所有内容打包。

+ **解析**：由于 docker 的运行模式是 C/S。我们本机是 C，docker 引擎是 S。实际的构建过程是在 docker 引擎下完成的，所以这个时候无法用到我们本机的文件。这就需要把我们本机的指定目录下的文件一起打包提供给 docker 引擎使用。

+ 如果未说明最后一个参数，那么默认上下文路径就是 Dockerfile 所在的位置。

+ **注意**：上下文路径下不要放无用的文件，因为会一起打包发送给 docker 引擎，如果文件过多会造成过程缓慢。

## DockerFile指令详解

> `RUN`、`CMD` 和 `ENTRYPOINT` 这三个 `Dockerfile` 指令的区别

**简单的说**

1. RUN 执行命令并创建新的镜像层，RUN 经常用于安装软件包。
2. CMD 设置容器启动后默认执行的命令及其参数，但 CMD 能够被 `docker run` 后面跟的命令行参数替换
3. ENTRYPOINT 配置容器启动时运行的命令

**我们可用两种方式指定 RUN、CMD 和 ENTRYPOINT 要运行的命令：`Shell` 格式和 `Exec` 格式**

shell 格式：

```bash
RUN <命令行命令>
# <命令行命令> 等同于，在终端操作的 shell 命令。
```

exec 格式：

```bash
RUN ["可执行文件", "参数1", "参数2"]
# 例如：
# RUN ["./test.php", "dev", "offline"] 等价于 RUN ./test.php dev offline
```

> **Dockerfile 的指令每执行一次都会在 docker 上新建一层。所以过多无意义的层，会造成镜像膨胀过大。例如：**

```
FROM centos
RUN yum -y install wget
RUN wget -O redis.tar.gz "http://download.redis.io/releases/redis-5.0.3.tar.gz"
RUN tar -xvf redis.tar.gz
```

以上执行会创建 3 层镜像。可简化为以下格式：

```
FROM centos
RUN 
	yum -y install wget \
  	&& wget -O redis.tar.gz "http://download.redis.io/releases/redis-5.0.3.tar.gz" \
  	&& tar -xvf redis.tar.gz  
```



> TODO:使用过程中陆续整理更新DockerFile的操作

## 实战：DockerFile制作Tomcat镜像

准备Linux环境的 jdk 和 tomcat 压缩包，新建一个DockerFile文件

注：因为dockerfile命名使用默认命名 因此`docker run`不用使用 `-f` 参数指定文件

==DockerFile==

```bash
$ vim dockerfile
FROM centos 										# 基础镜像centos
MAINTAINER loki<oliverloki@foxmail.com>					# 作者
COPY README /usr/local/README 						# 复制README文件
# 添加jdk、tomcat，ADD 命令会自动解压
ADD jdk-8u231-linux-x64.tar.gz /usr/local/ 			
ADD apache-tomcat-9.0.35.tar.gz /usr/local/ 		
RUN yum -y install vim								# 安装 vim 命令

ENV LOKIPATH /usr/local 								# 环境变量设置 工作目录
WORKDIR $LOKIPATH

ENV JAVA_HOME /usr/local/jdk1.8.0_231 				# 环境变量： JAVA_HOME环境变量

ENV CLASSPATH $JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar

ENV CATALINA_HOME /usr/local/apache-tomcat-9.0.35 	# 环境变量： tomcat环境变量
ENV CATALINA_BASH /usr/local/apache-tomcat-9.0.35

# 设置环境变量 分隔符是：
ENV PATH $PATH:$JAVA_HOME/bin:$CATALINA_HOME/lib:$CATALINA_HOME/bin 	

EXPOSE 8080 										# 设置暴露的端口

CMD /usr/local/apache-tomcat-9.0.35/bin/startup.sh && tail -F /usr/local/apache-tomcat-9.0.35/logs/catalina.out 					# 设置默认命令

```

构建镜像

```
$ docker build -t mytomcat .
```

启动镜像

```
$ docker run -d -p 8080:8080 --name tomcat01
```


访问测试

```
$ docker exec -it 自定义容器的id /bin/bash
$ cul localhost:8080
```

## Dockerfile指令总结

- FROM：构建镜像基于哪个镜像
- MAINTAINER：镜像维护者姓名或邮箱地址
- RUN：构建镜像时运行的指令
- CMD：运行容器时执行的shell环境
- VOLUME：指定容器挂载点到宿主机自动生成的目录或其他容器
- USER：为RUN、CMD、和 ENTRYPOINT 执行命令指定运行用户
- WORKDIR：为 RUN、CMD、ENTRYPOINT、COPY 和 ADD 设置工作目录，就是切换目录
- HEALTHCHECH：健康检查
- ARG：构建时指定的一些参数
- EXPOSE：声明容器的服务端口（仅仅是声明）
- ENV：设置容器环境变量
- ADD：拷贝文件或目录到容器中，如果是URL或压缩包便会自动下载或自动解压
- COPY：拷贝文件或目录到容器中，跟ADD类似，但不具备自动下载或解压的功能
- ENTRYPOINT：运行容器时执行的shell命令

我们以后开发的步骤：需要掌握Dockerfile的编写！我们之后的一切都是使用docker镜像来发布运行！



编写镜像后可以参考第四节中的内容，发布镜像到远程仓库





# 七、Docker 网络

[Docker网络详解——原理篇](https://www.cnblogs.com/sanduzxcvbnm/p/13370773.html)

## Docker的四种网络模式

安装Docker时，它会自动创建三个网络

```
#列出docker网络
docker network ls
```

Docker内置这三个网络，运行容器时，你可以使用该`--network`标志来指定容器应连接到哪些网络。

该bridge网络代表docker0所有Docker安装中存在的网络。除非你使用该`docker run --network=选项`指定，否则Docker守护程序默认将容器连接到此网络。

```bash
docker run -it --network=host [nginx]
#host模式：使用 --net=host 指定。
#none模式：使用 --net=none 指定。
#bridge模式：使用 --net=bridge 指定，默认设置。
#container模式：使用 --net=container:NAME_or_ID 指定
```

| 网络模式   | 简介                                                         |
| ---------- | ------------------------------------------------------------ |
| Host       | 容器将不会虚拟出自己的网卡，配置自己的IP等，而是使用宿主机的IP和端口。相当于Vmware中的桥接模式 |
| `Bridge`   | 相当于Vmware中的Nat模式，容器使用独立network Namespace，并连接到docker0虚拟网卡（默认模式）。通过docker0网桥以及Iptables nat表配置与宿主机通信；**bridge模式是Docker默认的网络设置**，此模式会为每一个容器分配Network Namespace、设置IP等，并将一个主机上的Docker容器连接到一个虚拟网桥上 |
| None       | 该模式关闭了容器的网络功能。容器并不需要网络（例如只需要写磁盘卷的批处理任务） |
| Container  | 创建的容器不会创建自己的网卡，配置自己的IP，而是和一个指定的容器共享IP、端口范围。 |
| 自定义网络 | 略                                                           |

## 详细介绍一下Bridge模式

### Bridge模式的拓扑

当`Docker server`启动时，会在主机上创建一个名为`docker0`的`虚拟网桥`，此主机上启动的Docker容器会连接到这个虚拟网桥上。虚拟网桥的工作方式和物理交换机类似，这样主机上的所有容器就通过交换机连在了一个二层网络中。

接下来就要为容器分配IP了，Docker会从RFC1918所定义的私有IP网段中，选择一个和宿主机不同的IP地址和子网分配给docker0，连接到docker0的容器就从这个子网中选择一个未占用的IP使用。如一般Docker会使用172.17.0.0/16这个网段，并将172.17.0.1/16分配给docker0网桥（在主机上使用ifconfig命令是可以看到docker0的，可以认为它是网桥的管理接口，在宿主机上作为一块虚拟网卡使用）。



单机环境下的网络拓扑如下，主机地址为10.10.0.186/24。
![img](https://img2020.cnblogs.com/blog/794174/202007/794174-20200724102855118-171605356.png)

### 网络模式详解

Docker完成以上网络配置的过程大致是这样的：

（1）在主机上创建一对虚拟网卡veth pair设备。veth设备总是成对出现的，它们组成了一个数据的通道，数据从一个设备进入，就会从另一个设备出来。因此，veth设备常用来连接两个网络设备。

（2）Docker将veth pair设备的一端放在新创建的容器中，并命名为eth0。另一端放在主机中，以veth65f9这样类似的名字命名，并将这个网络设备加入到docker0网桥中，可以通过`brctl show`命令查看。

```bash
brctl show
bridge name     bridge id               STP enabled     interfaces
docker0        8000.02425f21c208          no
```

（3）从docker0子网中分配一个IP给容器使用，并设置docker0的IP地址为容器的默认网关。

```bash
# 运行容器
[root@server1 ~]# docker run --name=nginx_bridge --net=bridge -p 80:80 -d nginx        
9582dbec7981085ab1f159edcc4bf35e2ee8d5a03984d214bce32a30eab4921a
# 查看容器
[root@server1 ~]# docker ps
CONTAINER ID        IMAGE          COMMAND                  CREATED             STATUS              PORTS                NAMES
9582dbec7981        nginx          "nginx -g 'daemon ..."   3 seconds ago       Up 2 seconds        0.0.0.0:80->80/tcp   nginx_bridge

# 查看容器网络;
[root@server1 ~]# docker inspect 9582dbec7981
"Networks": {
    "bridge": {
        "IPAMConfig": null,
        "Links": null,
        "Aliases": null,
        "NetworkID": "9e017f5d4724039f24acc8aec634c8d2af3a9024f67585fce0a0d2b3cb470059",
        "EndpointID": "81b94c1b57de26f9c6690942cd78689041d6c27a564e079d7b1f603ecc104b3b",
        "Gateway": "172.17.0.1",
        "IPAddress": "172.17.0.2",
        "IPPrefixLen": 16,
        "IPv6Gateway": "",
        "GlobalIPv6Address": "",
        "GlobalIPv6PrefixLen": 0,
        "MacAddress": "02:42:ac:11:00:02"
    }
}

# 查看网桥信息，会看到有有一个容器
[root@server1 ~]# docker network inspect bridge
[
    {
        "Name": "bridge",
        "Id": "9e017f5d4724039f24acc8aec634c8d2af3a9024f67585fce0a0d2b3cb470059",
        "Created": "2019-06-09T23:20:28.061678042-04:00",
        "Scope": "local",
        "Driver": "bridge",
        "EnableIPv6": false,
        "IPAM": {
            "Driver": "default",
            "Options": null,
            "Config": [
                {
                    "Subnet": "172.17.0.0/16"
                }
            ]
        },
        "Internal": false,
        "Attachable": false,
        "Ingress": false,
        "Containers": {
            "9582dbec7981085ab1f159edcc4bf35e2ee8d5a03984d214bce32a30eab4921a": {
                "Name": "nginx_bridge",
                "EndpointID": "81b94c1b57de26f9c6690942cd78689041d6c27a564e079d7b1f603ecc104b3b",
                "MacAddress": "02:42:ac:11:00:02",
                "IPv4Address": "172.17.0.2/16",
                "IPv6Address": ""
            }
        },
        "Options": {
            "com.docker.network.bridge.default_bridge": "true",
            "com.docker.network.bridge.enable_icc": "true",
            "com.docker.network.bridge.enable_ip_masquerade": "true",
            "com.docker.network.bridge.host_binding_ipv4": "0.0.0.0",
            "com.docker.network.bridge.name": "docker0",
            "com.docker.network.driver.mtu": "1500"
        },
        "Labels": {}
    }
]
折叠 
```

### Bridge模式下容器的通信

在bridge模式下，连在同一网桥上的容器可以相互通信（若出于安全考虑，也可以禁止它们之间通信，方法是在DOCKER_OPTS变量中设置`--icc=false`，这样只有使用`--link`才能使两个容器通信）。

Docker可以开启容器间通信（意味着默认配置`--icc=false`），也就是说，宿主机上的所有容器可以不受任何限制地相互通信，这可能导致拒绝服务攻击。进一步地，Docker可以通过`--ip_forward`和`--iptables`两个选项控制容器间、容器和外部世界的通信。

容器也可以与外部通信，我们看一下主机上的Iptable规则，可以看到这么一条

```css
-A POSTROUTING -s 172.17.0.0/16 ! -o docker0 -j MASQUERADE
```

这条规则会将源地址为172.17.0.0/16的包（也就是从Docker容器产生的包），并且不是从docker0网卡发出的，进行源地址转换，转换成主机网卡的地址。这么说可能不太好理解，举一个例子说明一下。假设主机有一块网卡为eth0，IP地址为10.10.101.105/24，网关为10.10.101.254。从主机上一个IP为172.17.0.1/16的容器中ping百度（180.76.3.151）。IP包首先从容器发往自己的默认网关docker0，包到达docker0后，也就到达了主机上。然后会查询主机的路由表，发现包应该从主机的eth0发往主机的网关10.10.105.254/24。接着包会转发给eth0，并从eth0发出去（主机的ip_forward转发应该已经打开）。这时候，上面的Iptable规则就会起作用，对包做SNAT转换，将源地址换为eth0的地址。这样，在外界看来，这个包就是从10.10.101.105上发出来的，Docker容器对外是不可见的。

那么，外面的机器是如何访问Docker容器的服务呢？我们首先用下面命令创建一个含有web应用的容器，将容器的80端口映射到主机的80端口。

```lua
docker run --name=nginx_bridge --net=bridge -p 80:80 -d nginx
```

然后查看Iptable规则的变化，发现多了这样一条规则：

```css
-A DOCKER ! -i docker0 -p tcp -m tcp --dport 80 -j DNAT --to-destination 172.17.0.2:80
```

此条规则就是对主机eth0收到的目的端口为80的tcp流量进行DNAT转换，将流量发往172.17.0.2:80，也就是我们上面创建的Docker容器。所以，外界只需访问10.10.101.105:80就可以访问到容器中的服务。

除此之外，我们还可以自定义Docker使用的IP地址、DNS等信息，甚至使用自己定义的网桥，但是其工作方式还是一样的。



# 八、Docker compose和Docker swarm

[Docker Compose | 菜鸟教程](https://www.runoob.com/docker/docker-compose.html)



# 九、Docker部署实例

## Nginx部署

拉取镜像

```
docker pull nginx
```

启动Nginx服务

```
[root@VM-16-12-centos /]# docker run -d  -p 3389:80 nginx
7691c08b8add18e708fffb5b19e7b8c80a8e02ddbd276a54ff76a516c08b51dd
```

成功示例

```html
[root@VM-16-12-centos /]# curl localhost:3389
<!DOCTYPE html>
<html>
<head>
<title>Welcome to nginx!</title>
<style>
html { color-scheme: light dark; }
body { width: 35em; margin: 0 auto;
font-family: Tahoma, Verdana, Arial, sans-serif; }
</style>
</head>
<body>
<h1>Welcome to nginx!</h1>
<p>If you see this page, the nginx web server is successfully installed and
working. Further configuration is required.</p>

<p>For online documentation and support please refer to
<a href="http://nginx.org/">nginx.org</a>.<br/>
Commercial support is available at
<a href="http://nginx.com/">nginx.com</a>.</p>

<p><em>Thank you for using nginx.</em></p>
</body>
</html>
```

```
docker exec -it 容器id /bin/bash
whereis nginx
```

## Springboot微服务打包Docker镜像







## 部署Redis集群





# 十、可视化面板

+ portainer
+ Rancher(CI/CD持续集成)

## portainer

不常用，可以自行探索

安装命令：通过本机8088端口访问

```
docker run -d -p 8088:9000 --restart=always -v /var/run/docker.sock:/var/run/docker.sock --privileged=true portainer/portainer
```

测试一下是否安装成功，如果有返回结果则说明安装成功了

+ 浏览器访问 `ip:8088`
+ curl localhost:8088

## Ranhcer

[Rancher 中文文档](https://docs.rancher.cn/)

:taco:在学习CICD的时候更新这一部分



**持续更新~，如果Loki的整理对你有帮助的话点个赞吧**