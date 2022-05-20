## Linux入门

> Linux简介

+ Linux 是一种自由和开放源码的类 UNIX 操作系统，开源免费
+ Linux 英文解释为 **Linux is not Unix**
+ 一般应用于专业的web服务器上

> Linux特性

优势：

+ Linux是一种操作系统，可以安装在计算机的硬件之上，用来操作计算机硬件和软件资源的系统软件
+ Linux注重系统的安全性，对文件访问权限有严格设定
+ 高稳定性和高并发处理能力

缺点：

+ 可视化界面比较差

> Linux发行版本

+ Ubuntu(乌班图)
+ Redhat Linux(红帽)
+ centos
+ 红旗Linux

## Xshell连接远程服务器

两种方式搭建Linux学习环境

+ 通过VMware等软件安装虚拟机，再安装CentOS镜像
+ 购买云服务器（相当于远程的Linux系统）

云服务器的方式更接近真实开发，并且本地虚拟机配置较为繁琐，本文以阿里云服务器为例



**搭建阿里云服务器Linux学习环境**

> 1、购买云服务器

[腾讯云教育优惠地址][https://cloud.tencent.com/act/campus?fromSource=gwzcw.3083777.3083777.3083777&utm_medium=cpc&utm_id=gwzcw.3083777.3083777.3083777&gclid=Cj0KCQiAt8WOBhDbARIsANQLp96ZrLlgwyd1EYvofuIlEVleNNMdQQkG1Mn0AAiSKkn9FxLXV8MXvNUaAnu-EALw_wcB] 

阿里云也有免费试用1个月的活动

> 2、云服务器的配置

**必须进行以下三步配置才可以用Xshell连接**

1. 重置实例密码，用做与XShell连接时的登录密码
2. 记住公网ip，用作填写新建会话时的主机号
3. 进行安全组配置，开放端口号

![image-20220103155138685](https://s2.loli.net/2022/01/03/g5toM4CIA1jaVyZ.png)



> 3、XShell与Xftp工具的下载与使用

+ Xshell：用于连接远程机，连接后可用命令行操控远程机
+ Xftp:用于本机和远程机传输文件时使用

> 4、Xshell新建会话

![image-20220103160009777](D:\桌面\P_picture_cahe\image-20220103160009777.png)

> 5、点击连接，默认用户名是root,密码就是你重置的密码

![image-20220103160251680](D:\桌面\P_picture_cahe\image-20220103160251680.png)

![image-20220103160503744](D:\桌面\P_picture_cahe\image-20220103160503744.png)



至此，基本环境搭建完毕

## Linux基本操作

> 开机

会启动许多程序。它们在Windows叫做服务( service ) , 在Linux就叫做守护进程 (daemon)。
一般来说，用户的登录方式有三种:
 ●命令行登录
 ●SSH登录(远程)
 ●图形界面登录(GHOME桌面)
最高权限账户为root,可以操作一切

> 关机

在linux领域内大多用在服务器上,很少遇到关机的操作。毕竟服务器上跑一个服务是永无止境的,除非特殊情况下,不得已才会关机。
关机指令为 `shutdown`

```
sync #将数据由内存同步到硬盘中。
shutdown #关机指令，你可以man shutdown 来看一下 帮助文档。例如你可以运行如下命令关机:
shutdown -h 10 #这个命令告诉大家，计算机将在10分钟后关机
shutdown -h now#立马关机
shutdown -h 20:25 #系统会在今天20:25关机
shutdown -h +10 #十分钟后关机
shutdown -r now #系统立马重启
shutdown -r +10 #系统十分钟后重启
reboot #就是重启，等同于shutdown -r now
halt #关闭系统，等同于shutdown -h now和poweroff
```

总结：不管是重启还是关闭系统，首先要运行sync命令，把内存中的数据写到磁盘中，执行命令之后没有返回消息则表明运行成功

> 系统目录

登录系统后，在当前命令窗口下输入命令

```
ls / 
```

![img](https://www.runoob.com/wp-content/uploads/2014/06/d0c50-linux2bfile2bsystem2bhierarchy.jpg)

以下是对这些目录的解释：

- **/bin**：
  bin 是 Binaries (二进制文件) 的缩写, 这个目录存放着最经常使用的命令。

- **/boot：**
  这里存放的是启动 Linux 时使用的一些核心文件，包括一些连接文件以及镜像文件。

- **/dev ：**
  dev 是 Device(设备) 的缩写, 该目录下存放的是 Linux 的外部设备，在 Linux 中访问设备的方式和访问文件的方式是相同的。

- **/etc：** :small_blue_diamond:
  etc 是 Etcetera(等等) 的缩写,这个目录用来存放所有的系统管理所需要的配置文件和子目录。

- **/home**：:small_blue_diamond:
  用户的主目录，在 Linux 中，每个用户都有一个自己的目录，一般该目录名是以用户的账号命名的，如上图中的 alice、bob 和 eve。

- **/lib**：
  lib 是 Library(库) 的缩写这个目录里存放着系统最基本的动态连接共享库，其作用类似于 Windows 里的 DLL 文件。几乎所有的应用程序都需要用到这些共享库。

- **/lost+found**：
  这个目录一般情况下是空的，当系统非法关机后，这里就存放了一些文件。

- **/media**：
  linux 系统会自动识别一些设备，例如U盘、光驱等等，当识别后，Linux 会把识别的设备挂载到这个目录下。

- **/mnt**：
  系统提供该目录是为了让用户临时挂载别的文件系统的，我们可以将光驱挂载在 /mnt/ 上，然后进入该目录就可以查看光驱里的内容了。

- **/opt**：:small_blue_diamond:
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

  这是 Linux2.6 内核的一个很大的变化。该目录下安装了 2.6 内核中新出现的一个文件系统 sysfs 。

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

+ **/etc**： :small_blue_diamond:

  上边也提到了，这个是系统中的配置文件，如果你更改了该目录下的某个文件可能会导致系统不能启动。

+ **/bin, /sbin, /usr/bin, /usr/sbin**: 这是系统预设的执行文件的放置目录，比如 ls 就是在 /bin/ls 目录下的。

+ 值得提出的是，/bin, /usr/bin 是给系统用户使用的指令（除root外的通用户），而/sbin, /usr/sbin 则是给 root 使用的指令。

+ **/var**： 这是一个非常重要的目录，系统上跑了很多程序，那么每个程序都会有相应的日志产生，而这些日志就被记录到这个目录下，具体在 /var/log 目录下，另外 mail 的预设放置也是在这里。

## 常用命令

**tips：可以使用man 命令来查看各个命令的使用文档,如: man [命令]**

### 目录相关

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

rm #移除文件或者目录
	-f #忽略不存在的文件,不会出现警告,强制删除!
	-r #递归删除目录!
	-i #互动,删除询问是否删除
	rm -rf install.sh/#删除系统中的install.sh
	rm -rf /* #删库跑路
	
mv #移动文件或者目录|重命名文件
	mv install.sh lokistudy #移动文件
	mv lokistudy lokistudy2 #重命名文件夹名
```

### 文件内容查看

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

常用的有 `cat`,` nl`,`less`

## Linux文件基本属性

Linux 系统是一种典型的多用户系统，不同的用户处于不同的地位，拥有不同的权限。

为了保护系统的安全性，Linux 系统对不同的用户访问同一文件（包括目录文件）的权限做了不同的规定。

> 通过 `ls -l` 命令来显示一个文件的属性以及文件所属的用户和组

![image-20220103232010878](https://s2.loli.net/2022/01/03/o4yIX6Yx5MK2vS8.png)

> 对上图的理解----每个文件的属性由左边第一部分的 10 个字符来确定

+ 第一个字符代表这个文件是目录、文件或链接文件等等
  + 当为 **d** 则是目录
  + 当为 **-** 则是文件；
  + 若是 **l** 则表示为链接文档(link file)；
  + 若是 **b** 则表示为装置文件里面的可供储存的接口设备(可随机存取装置)；
  + 若是 **c** 则表示为装置文件里面的串行端口设备，例如键盘、鼠标(一次性读取装置)

+ 接下来的字符中，以三个为一组，且均为 **rwx** 的三个参数的组合。其中， **r** 代表可读(read)、 **w** 代表可写(write)、 **x** 代表可执行(execute)。 要注意的是，这三个权限的位置不会改变，如果没有权限，就会出现减号 **-** 而已。

+ 总结

![363003_1227493859FdXT](https://www.runoob.com/wp-content/uploads/2014/06/363003_1227493859FdXT.png)

**对于 root 用户来说，一般情况下，文件的权限对其不起作用**

> Linux文件属主和属组

对于文件来说，它都有一个特定的所有者，也就是对该文件具有所有权的用户。

同时，在Linux系统中，用户是按组分类的，一个用户属于一个或多个组。

文件所有者以外的用户又可以分为文件所有者的同组用户和其他用户。

因此，Linux系统按文件所有者、文件所有者同组用户和其他用户来规定了不同的文件访问权限。

### 更改文件属性

> 1、chgrp：更改文件属组

语法：

```
chgrp [-R] 属组名 文件名
```

参数选项

- -R：递归更改文件属组，就是在更改某个目录文件的属组时，如果加上-R的参数，那么该目录下的所有文件的属组都会更改。

> 2、chown：更改文件属主，也可以同时更改文件属组

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

> 3、chmod：更改文件9个属性 **常用**

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

那如果要将权限变成 *-rwxr-xr--* 呢？那么权限的分数就成为 [4+2+1][4+0+1][4+0+0]=754。

## Vim编辑器

> 什么是 vim

Vim 是从 vi 发展出来的一个文本编辑器。代码补全、编译及错误跳转等方便编程的功能特别丰富，在程序员中被广泛使用

> Vim的使用-三种模式

+ **命令模式：用户刚刚启动 vi/vim，便进入了命令模式。**

  常用命令:

  + **i** 切换到输入模式，以输入字符。
  + **x** 删除当前光标所在处的字符。
  + **:** 切换到底线命令模式，以在最底一行输入命令。

  命令模式只有一些最基本的命令，因此仍要依靠底线命令模式输入更多命令。

+ **输入模式：启动Vim，进入了命令模式，按下i，切换到输入模式**

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

命令模式只有一些最基本的命令，因此仍要依靠底线命令模式输入更多命令。

+ **底线命令模式常用命令**

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

## 用户管理

> 如何切换用户

1.切换用户的命令为：su username 【username是你的用户名哦】

2.从普通用户切换到root用户，还可以使用命令：sudo su

3.在终端输入exit或logout或使用快捷方式ctrl+d，可以退回到原来用户，其实ctrl+d也是执行的exit命令

4.在切换用户时，如果想在切换用户之后使用新用户的工作环境，可以在su和username之间加-，例如：【su - root】

$表示普通用户

## 磁盘管理



## 进程管理



## 环境安装

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