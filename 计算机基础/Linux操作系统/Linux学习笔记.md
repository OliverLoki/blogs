  

> Linux是自由、开源的操作系统，安装在计算机的硬件之上，是用来操作计算机硬件和软件资源的系统软件，一般应用于专业的web服务器上，具有以下特性
>
> + Linux注重系统的安全性，对文件访问权限有严格设定，最高权限账户为`root`用户，可以操作一切，详见第三章
> + 自由和可定制性非常高，可以根据需求对[Linux 核心内核](https://github.com/torvalds/linux)进行修改
>
> + 可视化界面相对较差，不太符合普通人使用习惯，软件支持欠缺

# 一、Linux入门

## Linux：开源的操作系统的不同发行版

> 开源资料推荐

+ [品读 Linux 0.11 核心代码](https://github.com/sunym1993/flash-linux0.11-talk)
+ [鸟哥的Linux私房菜](https://linux.vbird.org/)
+ 

**由于开源，Linux也有了很多的发行版本，下面简单介绍一下常用的Linux发行版，相对社区活跃，软件资源丰富**

> [Ubuntu](https://ubuntu.com/)
>
> + UI好看，但是相对不稳定，适合自用或者Windows开始转用Linux系统
> + 包管理工具：`apt`
> + 性能略差于CentOS

> [centos](https://www.centos.org/)
>
> + 开源、大腕、稳定
> + 包管理工具：`rpm`、`yum`

> Redhat
>
> + 

## 搭建一个Linux学习环境

两种方式搭建Linux学习环境

+ 通过VMware等软件安装虚拟机，再安装CentOS镜像
+ 从云服务器提供商租赁云服务器

###  云服务器搭建Linux环境（推荐）

> 注：
>
> + 云服务器的方式更接近真实开发，并且本地虚拟机配置较为繁琐
> + 我们需要用到以下两个工具来连接和管理远程虚拟机
>   + `Xshell`：用于连接远程机，连接后可用命令行操控远程机
>   + `Xftp`：用于本机和远程机传输文件时使用

1. **购买云服务器**

   分享几点个人意见

   + 前期学习，购买轻量应用服务器和云服务器均可

   + 善用Google找到当前最合适的优惠

   几个常见的优惠

   + 腾讯云教育优惠地址
   + Amazon会有时间很长的试用，但是有一定门槛

   + 阿里云免费试用1个月的活动

2. **进入云服务器控制台，进行以下操作，之后我们可以用`Xshell`（会话管理工具）连接到这个服务器**

   + 重置实例密码，用做与XShell连接时的登录密码
   + 记住公网ip，用作填写新建会话时的主机号
   + 进行安全组配置，开放端口号（例：22端口号就是Xshell远程登录端口号）

3. **Xshell新建会话，连接远程服务器**

   需要修改几点

   + 名称：自定义，可以修改为过期时间
   + 主机：刚刚购买的公网ip地址
   + 端口号：默认22端口
   + 密码：重置后的实例密码

4. **点击连接，默认用户名是root,密码就是你重置的密码**

> **使用Xftp进行文件管理和传输**

<img src="https://s2.loli.net/2022/06/19/8R3Alkw6etgWdjr.png" alt="image-20220103160503744" style="zoom:67%;" />



至此，一个远程Linux环境搭建完毕

### VMWare搭建一个Linux虚拟机

前置操作

> + [下载VMware Workstation Pro](https://www.vmware.com/products/workstation-pro/workstation-pro-evaluation.html)
>
> + 下载Linux镜像
>
>   + [CentOS清华镜像下载](http://ftp.tsukuba.wide.ad.jp/Linux/centos/7.9.2009/isos/x86_64/)
>   + [Kubuntu镜像下载](https://kubuntu.org/)：这是一个UI很好看的Ubuntu操作系统
>
> + 你的电脑CPU要支持虚拟化技术，对于Intel处理器而言，需要支持并开启`VTX`技术，可在任务管理器中查看，如果没有开启，则需要在BIOS中开启
>
>   <img src="https://s2.loli.net/2022/06/19/DJERijyOzNY4mBr.png" alt="image-20220619015035929" style="zoom:80%;" />

**步骤如下**

1. **VMware Workstation创建一个新的虚拟机，通过自定义方式安装，注意以下设置**
   + 硬件兼容性：默认
   + 资源分配：根据自己电脑配置
   + 网络类型：网络地址转换NAT 
   + IO控制器、磁盘类型：默认
   + 选择磁盘：创建新的虚拟磁盘
2. **选择下载好的Iso镜像文件，然后开启虚拟机，根据提示安装即可**
3. VMWare搭建好虚拟机后，需要进行网络配置，才可以在外网使用Xshell连接并管理这台主机，详情请参考[Xshell连接VMware虚拟机中的CentOS](https://www.cnblogs.com/niuben/p/13157291.html)



**图：Kubuntu系统在虚拟机中安装成功**

<img src="https://s2.loli.net/2022/06/19/I2AjDvowFQMcd7u.png" alt="image-20220619140740929" style="zoom:67%;" />

### 使用docker在windows上搭建Linux环境

> **[详情请参考这篇文章](https://blog.csdn.net/Night__breeze/article/details/124614391?spm=1001.2014.3001.5501)**



## Linux系统目录结构以及功能

登录系统后，在当前命令窗口下输入命令

```
ls / 
```

![image-20220619011237336](https://s2.loli.net/2022/06/19/NovWaEqYuwO4khd.png)

> 注：
>
> + 下图中的箭头指向该文件夹的的实际位置，可以理解为Windows中的快捷方式，Linux通过**`挂载`**实现这个功能

![img](https://www.runoob.com/wp-content/uploads/2014/06/d0c50-linux2bfile2bsystem2bhierarchy.jpg)

以下是对这些目录的解释：

- **/bin**：
  bin 是 Binaries (二进制文件) 的缩写, 这个目录存放着最经常使用的命令。

- **/boot：**
  这里存放的是启动 Linux 时使用的一些核心文件，包括一些连接文件以及镜像文件。

- **/dev ：**
  dev 是 Device(设备) 的缩写, 该目录下存放的是 Linux 的外部设备，在 Linux 中访问设备的方式和访问文件的方式是相同的。

- **/etc：** 
  etc 是 Etcetera(等等) 的缩写,这个目录用来存放所有的系统管理所需要的配置文件和子目录。

- **/home**：
  用户的主目录，在 Linux 中，每个用户都有一个自己的目录，一般该目录名是以用户的账号命名的，如上图中的 alice、bob 和 eve。

- **/lib**：
  lib 是 Library(库) 的缩写这个目录里存放着系统最基本的动态连接共享库，其作用类似于 Windows 里的 DLL 文件。几乎所有的应用程序都需要用到这些共享库。

- **/lost+found**：
  这个目录一般情况下是空的，当系统非法关机后，这里就存放了一些文件。

- **/media**：
  linux 系统会自动识别一些设备，例如U盘、光驱等等，当识别后，Linux 会把识别的设备挂载到这个目录下。

- **/mnt**：
  系统提供该目录是为了让用户临时挂载别的文件系统的，我们可以将光驱挂载在 /mnt/ 上，然后进入该目录就可以查看光驱里的内容了。

- **/opt**：
  opt 是 optional(可选) 的缩写，这是给主机额外安装软件所摆放的目录。比如你安装一个ORACLE数据库则就可以放到这个目录下。默认是空的。

- **/proc**：
  proc 是 Processes(进程) 的缩写，/proc 是一种伪文件系统（也即虚拟文件系统），存储的是当前内核运行状态的一系列特殊文件，这个目录是一个虚拟的目录，它是系统内存的映射，我们可以通过直接访问这个目录来获取系统信息。
  这个目录的内容不在硬盘上而是在内存里，我们也可以直接修改里面的某些文件，比如可以通过下面的命令来屏蔽主机的ping命令，使别人无法ping你的机器：

  ```
  echo 1 > /proc/sys/net/ipv4/icmp_echo_ignore_all
  ```

- **/root**：
  该目录为系统管理员，也称作超级权限者的用户主目录。

- **/sbin**：
  s 就是 Super User 的意思，是 Superuser Binaries (超级用户的二进制文件) 的缩写，这里存放的是系统管理员使用的系统管理程序。

- **/selinux**：
  这个目录是 Redhat/CentOS 所特有的目录，Selinux 是一个安全机制，类似于 windows 的防火墙，但是这套机制比较复杂，这个目录就是存放selinux相关的文件的。

- **/srv**：
  该目录存放一些服务启动之后需要提取的数据。

- **/sys**：

  这是 Linux2.6 内核的一个很大的变化。该目录下安装了 2.6 内核中新出现的一个文件系统 sysfs 

  sysfs 文件系统集成了下面3种文件系统的信息：针对进程信息的 proc 文件系统、针对设备的 devfs 文件系统以及针对伪终端的 devpts 文件系统。

  该文件系统是内核设备树的一个直观反映。

  当一个内核对象被创建的时候，对应的文件和目录也在内核对象子系统中被创建。

- **/tmp**：
  tmp 是 temporary(临时) 的缩写这个目录是用来存放一些临时文件的。

- **/usr**：
  usr 是 unix shared resources(共享资源) 的缩写，这是一个非常重要的目录，用户的很多应用程序和文件都放在这个目录下，类似于 windows 下的 program files 目录。

- **/usr/bin：**
  系统用户使用的应用程序。

- **/usr/sbin：**
  超级用户使用的比较高级的管理程序和系统守护程序。

- **/usr/src：**
  内核源代码默认的放置目录。

- **/var**：
  var 是 variable(变量) 的缩写，这个目录中存放着在不断扩充着的东西，我们习惯将那些经常被修改的目录放在这个目录下。包括各种日志文件。

- **/run**：
  是一个临时文件系统，存储系统启动以来的信息。当系统重启时，这个目录下的文件应该被删掉或清除。如果你的系统上有 /var/run 目录，应该让它指向 run。在 Linux 系统中，有几个目录是比较重要的，平时需要注意不要误删除或者随意更改内部文件。

+ **/etc**：

  上边也提到了，这个是系统中的配置文件，如果你更改了该目录下的某个文件可能会导致系统不能启动。

+ **/bin, /sbin, /usr/bin, /usr/sbin**: 这是系统预设的执行文件的放置目录，比如 ls 就是在 /bin/ls 目录下的。

+ 值得提出的是，/bin, /usr/bin 是给系统用户使用的指令（除root外的通用户），而/sbin, /usr/sbin 则是给 root 使用的指令。

+ **/var**： 这是一个非常重要的目录，系统上跑了很多程序，那么每个程序都会有相应的日志产生，而这些日志就被记录到这个目录下，具体在 /var/log 目录下，另外 mail 的预设放置也是在这里

## Linux系统开关机

Linux系统在开机时会自启动许多程序。它们在Windows叫做服务(`service`) , 在Linux就叫做守护进程 (`daemon`)

启动后，用户的登录方式有三种:

+ 命令行登录
+ SSH登录远程操作
+ 图形界面登录(GHOME桌面)

> 关机

在linux领域内大多用在服务器上,很少遇到关机的操作。毕竟服务器上跑一个服务是永无止境的，除非特殊情况下,不得已才会关机。
关机指令为 `shutdown`，默认不是马上关机，需要等待一分钟，这样做是为了在关机前执行一个`sync`操作

```bash
sync #将数据由内存同步到硬盘中，这个操作可以防止断电丢失数据
shutdown #关机指令，你可以man shutdown 来看一下 帮助文档。例如你可以运行如下命令关机:
shutdown -h 10 #这个命令告诉大家，计算机将在10分钟后关机
shutdown -h now#立马关机
shutdown -h 20:25 #系统会在今天20:25关机
shutdown -r now #系统立马重启，也会执行sync操作
shutdown -r +10 #系统十分钟后重启
reboot #就是重启，等同于shutdown -r now
halt #停机不断电，内存中数据还在，这样可以使系统处于一个低功耗状态
```

总结：不管是重启还是关闭系统，首先要运行sync命令，把内存中的数据写到磁盘中，执行命令之后没有返回消息则表明运行成功

## Vim：文本编辑器

> Vim 是从 vi 发展出来的一个文本编辑器。代码补全、编译及错误跳转等方便编程的功能特别丰富，在程序员中被广泛使用，由于Linux操作系统通常为了性能考量，没有图形化界面，所以文本的编辑就需要用到Vim了

Vim编辑器有三种模式

+ 一般模式
+ 编辑模式
+ 命令模式

> **一般模式：用户刚刚启动 vi/vim，便进入了一般模式。**

:rabbit:**使用 vi/vim 进入一般模式**

如果你想要使用 vi 来建立一个名为 runoob.txt 的文件时，你可以这样做：

```
$ vim runoob.txt
```

直接输入 **vi 文件名** 就能够进入 vi 的一般模式了。请注意，记得 vi 后面一定要加文件名，不管该文件存在与否！



:rabbit:**常用命令:**

+ **i** 切换到输入模式，以输入字符
+ **x** 删除当前光标所在处的字符
+ **:** 切换到命令模式，以在最底一行输入命令

**一般模式不常用，一般进入编辑/命令模式进行操作**

附：[Vim一般模式常用命令](https://zhuanlan.zhihu.com/p/89042423)

> **编辑模式：在一般模式按下 `i`，切换到输入模式**

在输入模式中，可以使用以下按键：

- **字符按键以及Shift组合**，输入字符
- **ENTER**，回车键，换行
- **BACK SPACE**，退格键，删除光标前一个字符
- **DEL**，删除键，删除光标后一个字符
- **方向键**，在文本中移动光标
- **HOME**/**END**，移动光标到行首/行尾
- **Page Up**/**Page Down**，上/下翻页
- **Insert**，切换光标为输入/替换模式，光标将变成竖线/下划线
- **ESC**，退出输入模式，切换到命令模式

命令模式只有一些最基本的命令，因此仍要依靠底线命令模式输入更多命令

> **命令模式：在一般模式按下`：`，进入命令模式**

| 常用指令 |                             说明                             |
| :------- | :----------------------------------------------------------: |
| :w       |               将编辑的数据写入硬盘档案中(常用)               |
| :w!      | 若文件属性为『只读』时，强制写入该档案。不过，到底能不能写入， 还是跟你对该档案的档案权限有关啊！ |
| :q       |                        离开 vi (常用)                        |
| :q!      |  若曾修改过档案，又不想储存，使用 ! 为强制离开不储存档案。   |
| :wq      |       储存后离开，若为 :wq! 则为强制储存后离开 (常用)        |
| :set nu  |      显示行号，设定之后，会在每一行的前缀显示该行的行号      |
| /        |                           向下查找                           |
| ?        |                           向上查找                           |





# 二、Linux常用命令



> **注：**
>
> + 可以使用man 命令来查看各个命令的使用文档
>
>   语法: man []

## 目录相关命令

```bash
cd:#切换目录命令!
./:#当前目录
/:#绝对路径
cd..:#返回上一级目录
cd ~:#回到当前的用户目录

pwd :#显示当前用户所在的目录

ls : #列出目录
	-a:#参数: all ,查看全部的文件,包括隐藏文件
	-l:#参数列出所有的文件,包含文件的属性和权限,没有隐藏文件
	ls -al #查看全部的文件包括隐藏文件的属性和权限
	
mkdir #新建目录
	mkdir -p test2/test3/test4 # 用-p递归创建层级目录
rmdir #删除目录
	rmdir -p test2/test3/test4 #递归删除文件
	#rmdir仅能删除空的目录,如果下面存在文件,需要先删除文件,递归删除多个目录-p参数即可

cp # 复制文件或目录
	cp install.sh cqhstudy #将当前目录下的install.sh 复制到cqhstudy文件夹中

rm #移除文件
	-f #忽略不存在的文件,不会出现警告,强制删除!
	-r #递归删除目录
	-i #互动,删除询问是否删除
	rm -rf test/#删除系统中的test目录下的所有内容
	rm -rf /* #删库跑路
	
mv #移动文件或者目录|重命名文件
	mv install.sh lokistudy #移动文件
	mv lokistudy lokistudy2 #重命名文件夹名
```

## 文件内容查看

Linux系统中使用以下命令来查看文件的内容:

```bash
cat #由第一行开始显示文件内容
tac #从最后一行开始显示，可以看出tac是cat的倒着写!
nl #显示的时候,顺道输出行号
more #一页一页的显示文件内容（空格表示翻页，enter代表向下看下一行）
less #与more类似,但是比more更好的是,他可以往前翻页!（空格翻页，上下键代表上下翻动页面，退出q命令
	#查找字符串 /要查询的字符 向下查询向上查询使用?要查询的字符串,用n继续搜寻下一个,用N向上寻找(在less界面的最下方输出)
head #只看头几行 通过-n参数来控制显示几行
tail #只看尾巴几行 通过-n参数来控制显示几行
```

+ 常用的有 `cat`,` nl`,`less`
+ 退出查看使用 `q`

## 其他常用命令

控制台输出

```bash
echo hello world
echo "hello       world"
#-e参数：支持转义字符
echo -e "hello \n world"
```

查看系统的环境变量

```bash
echo $ + Tab键
```

对控制台输出内容的保存（输出重定向）

```bash
#将输出内容保存到info文件中（覆盖重写）
ls > info
#将输出内容追加到info文件末尾
ls >> info
echo "hello loki" >> info 
```

创建一个软链接（类似Windows的快捷方式）

```bash
ln -s [源文件或目录] [软链接名]
```

查看已执行过的历史命令

```bash
history [可选数字：查看多少条]
```

清空历史命令

```
history -c
```

获取系统当前时间信息

```
[root@VM-16-12-centos conf]# date
Mon Jun 20 17:19:06 CST 2022
```



## Shell中常用快捷键

+ Ctrl+c 停止进程
+ Ctrl+l 清屏，或者用clear命令
+ 善用tab键
+ 善用上下键，PageDown，PageUp键

# 三、Linux文件访问权限

Linux 系统是一种典型的多用户系统，不同的用户处于不同的地位，拥有不同的权限。最高权限账户为`root`用户，可以操作一切

为了保护系统的安全性，Linux 系统对不同的用户访问同一文件（包括目录文件）的权限做了不同的规定

## ls -l：查看文件的属性和权限、并详细说明

> 通过 `ls -l` 命令来显示一个文件的属性以及文件所属的用户和组

:rocket:**注：每个文件的属性由左边第一部分的 10 个字符来确定，最后一行以点(.)结尾的是隐藏文档**

```bash
[root@VM-16-12-centos /]# ls -l
total 72
lrwxrwxrwx.   1 root root     7 Mar  7  2019 bin -> usr/bin
dr-xr-xr-x.   5 root root  4096 Apr 26 09:07 boot
drwxr-xr-x    2 root root  4096 Nov  5  2019 data		
drwxr-xr-x   19 root root  2980 Apr 26 10:44 dev
drwxr-xr-x.  96 root root 12288 May  4 18:37 etc
drwxr-xr-x.   3 root root  4096 May  7 19:31 home
lrwxrwxrwx.   1 root root     7 Mar  7  2019 lib -> usr/lib
lrwxrwxrwx.   1 root root     9 Mar  7  2019 lib64 -> usr/lib64
drwx------.   2 root root 16384 Mar  7  2019 lost+found
drwxr-xr-x.   2 root root  4096 Apr 11  2018 media
drwxr-xr-x.   2 root root  4096 Apr 11  2018 mnt
drwxr-xr-x.   5 root root  4096 May  4 18:37 opt
dr-xr-xr-x  109 root root     0 Apr 26 10:44 proc
dr-xr-x---.   9 root root  4096 May  7 19:27 root
drwxr-xr-x   28 root root  1000 Jun  4 19:36 run
lrwxrwxrwx.   1 root root     8 Mar  7  2019 sbin -> usr/sbin
drwxr-xr-x.   2 root root  4096 Apr 11  2018 srv
dr-xr-xr-x   13 root root     0 Apr 26 16:55 sys
drwxrwxrwt.   9 root root  4096 Jun 19 03:20 tmp
drwxr-xr-x.  14 root root  4096 Jan  8  2021 usr
drwxr-xr-x.  20 root root  4096 Jan  8  2021 var
```

从上面可以看到，每一行都有列，分别是

1. 第一列共10位，第1位表示文档类型，`d`表示目录，`-`表示文件，`l`表示链接文件，`b`表示可随机存取的设备，如U盘等，`c`表示一次性读取设备，如鼠标、键盘等。后9位，以三个为一组，依次对应三种身份所拥有的权限，且均为 **rwx** 的三个参数的组合。身份顺序为：owner、group、others

   + `r` 代表可读(read)
   + `w` 代表可写(write)
   + ` x` 代表可执行(execute)
   + 如果没有权限，就会出现减号` - `

   如：`-r-xr-x---`的含义为**当前文档是一个文件，拥有者可读、可执行，同一个群组下的用户，可读、可执行，其他人没有任何权限**。

   **总结**

   ![363003_1227493859FdXT](https://www.runoob.com/wp-content/uploads/2014/06/363003_1227493859FdXT.png)

2. **第二列**表示链接数，表示有多少个文件链接到inode号码

3. **第三列**表示拥有者（属主）

4. **第四列**表示所属群组（属组）

5. **第五列**表示文档容量大小，单位字节

6. **第六列**表示文档最后修改时间，注意不是文档的创建时间

> 注：
>
> + **Linux文件属主和属组**
>
>   对于文件来说，它都有一个特定的所有者，也就是对该文件具有所有权的用户。
>
>   同时，在Linux系统中，用户是按组分类的，一个用户属于一个或多个组。
>
>   文件所有者以外的用户又可以分为文件所有者的同组用户和其他用户。
>
>   因此，Linux系统按文件所有者、文件所有者同组用户和其他用户来规定了不同的文件访问权限。
>
> + **对于 root 用户来说，一般情况下，文件的权限对其不起作用**

## chgrp：更改文件属组

语法：

```
chgrp [-R] 属组名 文件名
```

参数选项

- -R：递归更改文件属组，就是在更改某个目录文件的属组时，如果加上-R的参数，那么该目录下的所有文件的属组都会更改。

## chown：更改文件属主，也可以同时更改文件属组

语法：

```
chown [–R] 属主名 文件名
chown [-R] 属主名：属组名 文件名
```

进入 /root 目录（~）将install.log的拥有者改为bin这个账号：

```
[root@www ~] cd ~
[root@www ~]# chown bin install.log
[root@www ~]# ls -l
-rw-r--r--  1 bin  users 68495 Jun 25 08:53 install.log
```

将install.log的拥有者与群组改回为root：

```
[root@www ~]# chown root:root install.log
[root@www ~]# ls -l
-rw-r--r--  1 root root 68495 Jun 25 08:53 install.log
```

## chmod：更改文件访问属性

Linux文件属性有两种设置方法，一种是数字（常用），一种是符号。

Linux 文件的基本权限就有九个，分别是 **owner/group/others(拥有者/组/其他)** 三种身份各有自己的 **read/write/execute** 权限。

先复习一下刚刚上面提到的数据：文件的权限字符为： **-rwxrwxrwx** ， 这九个权限是三个三个一组的！其中，我们可以使用数字来代表各个权限，**各权限的分数对照表如下**：

- r:4
- w:2
- x:1

每种身份(owner/group/others)各自的三个权限(r/w/x)分数是需要累加的，例如当权限为： **-rwxr-x---** 分数则是：

- owner = rwx = 4+2+1 = 7
- group = r-x = 4+0+1 = 5
- others= --- = 0+0+0 = 0

所以等一下我们设定权限的变更时，该文件的权限数字就是 **750**。变更权限的指令 chmod 的语法是这样的：

```
 chmod [-R] xyz 文件或目录
```

选项与参数：

- xyz : 就是刚刚提到的数字类型的权限属性，为 rwx 属性数值的相加。
- -R : 进行递归(recursive)的持续变更，亦即连同次目录下的所有文件都会变更

举例来说，如果要将 .bashrc 这个文件所有的权限都设定启用，那么命令如下：

```
[root@www ~]# ls -al .bashrc
-rw-r--r--  1 root root 395 Jul  4 11:45 .bashrc
[root@www ~]# chmod 777 .bashrc
[root@www ~]# ls -al .bashrc
-rwxrwxrwx  1 root root 395 Jul  4 11:45 .bashrc
```

那如果要将权限变成 *-rwxr-xr--* 呢？那么权限的分数就成为 [4+2+1][4+0+1][4+0+0]=754





# 五、Linux系统管理：Centos7版本

## 用户与用户组管理

### 用户管理：查看、新增删除、切换、密码管理

> 查看所有用户

用户的信息是保存在 **`/etc/passwd`** 下的

```bash
cat /etc/passwd
#只保留用户名
cat /etc/passwd |cut -f 1 -d :	
```

> 查看用户信息（也可用来判断用户是否存在）

```
id [用户名]
```

```
#举例
[-bash-4.2$] id loki
uid=1001(loki) gid=1001(loki) groups=1001(loki)
```

> 添加新用户

```bash
useradd [用户名]
useradd -g [用户名] [组名]	
useradd -d [用户数据目录] [用户名] #如果不指定用户目录，默认创建 /homme/用户名 数据目录
```

> 删除用户

```
userdel [用户名]
```

> 如何切换用户 

`su -> switch user`

+ 从root用户切换到普通用户，不需要输入密码

```
su [用户名]
```

+ 从普通用户切换到root用户，需要输入root密码8

```
sudo su
```

+ 从普通用户跳转到普通用户，需要输入密码

```
su [用户名]
```

+ 在终端输入exit或logout或使用快捷方式ctrl+d，可以退回到原来用户，其实ctrl+d也是执行的exit命令

在切换用户时，如果想在切换用户之后使用新用户的工作环境，可以在su和username之间加-，例如：【su - root】

$表示普通用户

> 查看当前用户是谁

```bash
#查看创建会话的用户是谁
who am i
#查看当前用户是谁
whoami
#查看创建会话的用户是谁
who
```

> 更改用户密码

```bash
passwd [用户名]
```

### 设置普通用户具有root权限

> 默认情况下，普通用户无法执行sudo命令，因此如果我们需要设置普通用户具有root权限，**这需要在root用户下进行操作**

1. 添加一个新用户并设置密码

2. 修改配置文件 `/etc/sudoers`，找到以下内容，在root下面新增一行，如下所示

   ```
   ## Allow root to run any commands anywhere 
   root    ALL=(ALL)       ALL
   loki    ALL=(ALL)       ALL
   ```

> 一个使用sudo命令的案例

```bash
#切换到loki用户
[root@VM-16-12-centos ~]# su loki
#没有权限访问，所以我们需要sudo命令
bash-4.2$ ls
ls: cannot open directory .: Permission denied
bash: history: /home/loki/.bash_history: cannot create: Permission denied
bash: history: /home/loki/.bash_history: cannot create: Permission denied
#可以访问了
bash-4.2$ sudo ls
We trust you have received the usual lecture from the local System
Administrator. It usually boils down to these three things:

    #1) Respect the privacy of others.
    #2) Think before you type.
    #3) With great power comes great responsibility.
[sudo] password for loki: 
nginx-1.21.6
```

### 用户组的管理：增加删除、修改、添加删除成员

当我们使用默认方法创建一个用户时，会创建一个用户组，如下所示，组id和用户id是一样的

```bash
[root@VM-16-12-centos ~]# id root
uid=0(root) gid=0(root) groups=0(root)
[root@VM-16-12-centos ~]# id loki
uid=1001(loki) gid=1001(loki) groups=1001(loki)
```

> 新建组

```
groupadd [用户组名]
```

> 将用户添加到组

```
#将loki添加到people组
usermod -g people loki （这个会把用户从其他组中去掉）
```

> 从组中删除用户

编辑/etc/group 找到需要删除的组编辑删除
或者用命令

```
gpasswd -d A [组名]
```

> 修改组名称

```
groupmod -n [新组名称] [旧组名称]
```

> 删除组

```
groupdel [组名称]
```

### 对用户组设置root权限

原理和上文中设置普通用户具有root权限一样，修改配置文件 `/etc/sudoers`，找到以下内容，在root下面新增一行，如下所示

```
## Allows people in group wheel to run all commands
%wheel	ALL=(ALL)	ALL
loki    ALL=(ALL)       ALL
```





## 进程和服务管理

+ **一个正在执行的程序或命令，称为进程**

+ **启动后常驻内存的进程，被称为服务（守护进程）**

### 服务管理：状态、启动、重启、停止、开机自启动

> 基本语法

```bash
systemctl start|stop|restart|status [服务名]
```

> 查看服务的方法

```bash
cd /usr/lib/systemd/system
ls -al
```

> 开机自启动服务配置

图形化界面命令:`setup`，进入后通过空格键勾选	

```
setup
```

命令行方式设置开机自启动和关闭

```
systemctl enable [服务名]
systemctl disable [服务名]
```

通过`systemctl status [服务名]` ，可以看出当前服务是否开机自启动，如下例所示

```bash
[root@VM-16-12-centos system]# systemctl status nginx
● nginx.service - nginx
   Loaded: loaded (/usr/lib/systemd/system/nginx.service; disabled; vendor preset: disabled)
   Active: active (running) since Sun 2022-05-08 00:01:32 CST; 1 months 12 days ago
 Main PID: 32377 (nginx)
    Tasks: 2
   Memory: 2.6M
   CGroup: /system.slice/nginx.service
           ├─32377 nginx: master process /usr/local/nginx/sbin/nginx
           └─32378 nginx: worker process

May 08 00:01:32 VM-16-12-centos systemd[1]: Starting nginx...
May 08 00:01:32 VM-16-12-centos systemd[1]: Started nginx.

```

其中，这一段说明了nginx服务开机自启动是关闭的

```
Loaded: loaded (/usr/lib/systemd/system/nginx.service; disabled; vendor preset: disabled)
```

> 列举所有的服务单元以及是否开机自启动

注：static状态说明暂时无法判断是否开机自启动

```
systemctl list-unit-files
```

### Linux防火墙管理

```bash
systemctl enable firewalld
systemctl disable firewalld
systemctl status firewalld #查看防火墙状态
systemctl start firewalld
systemctl stop firewalld
```

### 进程管理









## 磁盘管理











# 环境安装

安装软件一般有三种方式

+ rpm（以JDK为例）
+ 解压缩（以Tomcat为例）
+ yum在线安装（以Docker为例）



### JDK安装

1. 检测是否有java环境，如果有需要卸载
2. 从Oracle官网下载，如 `jdk8 rpm`
3. 通过Xftp传输到指定目录，执行以下操作

```bash
#检测当前系统是否存在Java环境 和windows命令一样
java -version 
#如果有的话就需要卸载
rpm -qa|grep jdk #查看JDK版本信息
rpm -e --nodeps #jdk_ 卸载
#卸载完毕后可安装JDK
rpm -ivh rpm包 
```

安装成功截图

![image-20220104021105588](https://s2.loli.net/2022/01/04/EuIn3meoA7x12is.png)



> 配置环境变量  -->  /etc/profile 

**注：通过rpm和yum方式安装JDK不需要配置环境变量**

通过压缩包解压安装，需要配置环境变量

1. ```bash
   vim /etc/profile #进入该文件
   ```

2. 启用编辑，在最后配置环境变量

   ![image-20220104022100812](https://s2.loli.net/2022/01/04/YQ5McpPWJTBqF1K.png)

   ```
   JAVA_HOME=/usr/java/jdk-14.0.1
   JRE_HOME=$JAVA_HOME/jre
   PATH=$PATH:$JAVA_HOME/bin:$JRE_HOME/bin
   CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar:$JRE_HOME/lib
   export JAVA_HOME JRE_HOME PATH CLASSPATH
   ```

> 尝试部署Springboot项目，通过ip访问

1. 将一个Springboot应用打jar包

2. 首先在本地Windows环境下测试运行

   ```
   java -jar 包名
   ```

3. 通过Xftp上传至Linux系统，

4. 



如果防火墙8080端口开放，并且阿里云安全组也开放，这个时候就可以通过公网ip进行访问了

### Tomcat安装

一般tomcat安装都是通过压缩包的方式

**注：安装Jdk后已经可以运行SpringBoot应用了，但是传统的SSM项目还是打war包，需要安装Tomcat才可以运行**

**方法**

1.下载tomcat 官网下载即可

![img](https://img-blog.csdn.net/20180522102228504)

2.解压

```
tar -zxvf apache-tomcat-9.0.36.tar.gz
```

3.启动tomcat

进入解压目录的bin文件夹，运行命令 

```
./startup.sh
```

### Docker安装

