

> + Shell 是一个用 `C` 语言编写的程序，既是一种命令语言，又是一种程序设计语言
> + Shell 作为命令行环境，是用户使用 Linux 的桥梁
> + Ubuntu默认的Shell环境是dash，其他主流 `Linux` 和 `Mac` 的默认 Shell 均为 `Bash`

**因此，本文的主要关注点为bash，Bash脚本系统管理和开发的重要组成部分。使用Bash进行系统管理，数据处理，Web应用程序部署，自动备份，为各个页面创建自定义脚本等。**

**简单的说shell就是一个包含若干行Shell或者Linux命令的文件。对于一次编写，多次使用的大量命令，就可以使用单独的文件保存下来，以便日后使用**

> 查看当前系统支持的Shell

```
cat /etc/shells
```

```
[root@VM-16-12-centos test]# cat /etc/shells
/bin/sh
/bin/bash
/usr/bin/sh
/usr/bin/bash
/bin/tcsh
/bin/csh
```





# 一、Bash入门

## 创建和运行一个Bash脚本

> **1、创建一个空的bash脚本**

```bash
cd /home/loki/shell学习
touch firstShell.sh 
vim firstShell.sh
```

> **2、编辑脚本内容**

```bash
#!/bin/bash
echo "hello world"
```

**注：**

1. Bash使用`echo`命令来打印输出

2. 每个基于Bash的Linux脚本都以`#!/bin/bash`开头，用于指定bash shell在操作系统中的位置（指定解析器），格式不正确会导致命令工作不正常。因此，在创建脚本时，要始终记住SheBang格式的这两点：

   + 它应该始终在脚本的第一行

   + 在#!和解释器的路径之间，#之前不应有任何空格

> **3、执行.sh脚本文件**

**`chmod +x ./hello.sh` 使脚本具有执行权限**

**通常在.sh文件目录下，以`./`作为前缀来执行bash脚本，**

如下所示，脚本成功执行

```
[root@centos shell学习]# ./hello.sh
hello world
```

也可通过以下方式执行

```bash
#source+绝对路径
source [shell绝对路径]
#.+空格+脚本名
. xxx.sh
```



## 注释

> 单行注释

#

> 多行注释

有两种方法可以在bash脚本中插入多行注释：

- 通过在`<< COMMENT`和`COMMENT`之间加上注释，可以在bash脚本中编写多行注释（COMMENT可修改）

- 也可以通过将注释括在(`:'`)和单引号(`'`)之间来编

## 数据类型

**Bash变量是无类型的，只需通过分配其值来键入变量名称，它会自动判断数据类型（如果将数字值分配给变量，它将自动转为整数工作，如果将字符值分配给该变量，则它将转为字符串类型）**

```
num=1+5
```

`echo num`输出

```
1+5
```

bash中，默认类型是字符串类型，不能进行数值运算操作，如果需要，则改为以下格式

```
#常用
num=$[1+5]
num=$((1+5))
```

`echo num`输出

```
6
```

或者可以通过以下方式计算值(不常用)，注意空格

```
expr 1 + 5
x=$(expr 2 /* 5)
x='$(expr 2 /* 5)'
```



## 变量

```bash
#查看系统中所有变量
set
#撤销变量定义
unset [变量名]
```

> 通常对Bash中的变量执行两个操作，如下所示：

- 为变量设置值
- 读取变量的值

**为变量设置值**

变量是将数据或有用的信息作为值存储的容器。以下是变量的语法：

```bash
variable_name = value
```

**读取变量的值**

通过在名称之前加上美元(`$`)号来读取

```
echo $变量名
```

> **Shell或UNIX系统中都有两种类型的变量**

- 系统定义的变量
- 用户定义的变量

**系统定义的变量**

1. `BASH`：表示Shell名称
2. `BASH_VERSION`：指定Bash持有的shell版本
3. `COLUMNS`:指定编号，屏幕的列数
4. `HOME`：为用户指定主目录。
5. `LOGNAME`：指定日志记录用户名。
6. `OSTYPE`：指示操作系统的类型。
7. `PWD`：代表当前的工作目录。
8. `USERNAME`：指定当前登录用户的名称。

执行以下bash脚本来理解系统变量

```bash
#! /bin/bash  
# Bash System-defined Variables  
echo $HOME # Home Directory  
echo $PWD # current working directory  
echo $BASH # Bash shell name  
echo $BASH_VERSION # Bash shell Version  
echo $LOGNAME # Name of the Login User  
echo $OSTYPE # Type of OS
```

**用户定义的变量**

+ 这些变量由用户创建和维护。通常，这些类型的变量以小写形式定义。但是不强制的，也**可以将变量名称写成大写(环境变量)**
+ **等号两侧不能有空格**

```bash
#!/bin/bash
# Bash Variables
USER_NAME=loki
echo Hey there! maxsu is any user curently working on he directory $PWD with the Bash Shell Version $BASH_VERSION.
```

> 声明静态变量，不能进行unset操作

```
readonly var = 1
```

> 把变量提升为全局环境变量，可供其他shell程序使用

```
export [变量名]
```

## Bash的引号

+ 使用简单的文本和字符串时，我们使用单引号或双引号都不会有任何区别

+ 应该注意的是，shell变量扩展仅适用于双引号。如果在单引号中定义变量，则不会将其视为变量。下面通过一个例子来理解这一点

请看以下示例

```bash
#!/bin/bash  
echo  
echo "When single quote is used with string:"  
invitation='Welcome to Yiibai'  
echo $invitation  
echo  
echo "When double quote is used with string:"  
invitation="Welcome to Yiibai"  
echo $invitation  
echo  
echo "When variable is used with double quote:"  
Remark="Hello User!, $invitation"  
echo $Remark  
echo  
echo "When variable is used with single quote:"  
Remark='Hello User!, $invitation'  
echo $Remark  
echo
```

## 命令行参数

命令行参数用于通过将输入传递给代码来使脚本更具动态性。在脚本运行时以以下形式传递这些参数：

```shell
 ./script_name.sh arg1 arg2 arg3.....
```

**如何使用命令行参数？**

在Bash Shell中，它们与以下默认参数或特殊变量的引用一起使用。

- `$0` - 指定要调用的脚本的名称。
- `$1`-`$9` - 存储前9个自变量的名称，或可用作自变量的位置。
- `$#` - 指定传递给脚本的参数总数(计数)。
- `$*` - 通过将所有命令行参数连接在一起来存储它们
- `$@` - 将参数列表存储为数组。
- `$?` - 指定当前脚本的进程ID（也可用来获取返回值）
- `$$` - 指定最后一个命令或最近执行过程的退出状态。
- `$!` - 显示最后一个后台作业的ID。

> 以下是用于传递命令行参数的两种方法：

**方法1：** 使用位置编号

下面是使用默认参数(`$1 ... $9`)访问参数。下面对此进行了解释，将以下代码保存到文件：*bash-argm1.sh*。

```bash
#!/bin/bash
echo $0 ' > echo $0'
echo $1 $2 $3 $4 $5 $6 $7 $8 $9 '>echo $1 $2 $3 $4 $5 $6 $7 $8 $9'
```



**方法2：**使用数组

这是将参数作为数组传递，按照给定的算法应用此方法。

**第1步：**创建Bash脚本。
**第2步：**声明变量名称，并将值分配为`$a`，格式如下：

```bash
variable_name=("$@")
Bash
```

其中`$@`是默认参数，用于将参数(传递)存储为数组。

**第3步：**通过以以下形式指定数组索引来显示参数的值：

```bash
${variable_name[i]}
Bash
```

**第4步：**保存脚本到文件：*bash-argm2.sh*。
**第5步：**通过传递参数来执行脚本。

请参阅以下程序：

```bash
#!/bin/bash  

args=("$@")  

echo ${args[0]} ${args[1]} ${args[2]} ${args[3]}
```

## 命令替换

> 命令替换允许命令的输出替换命令本身

语法

```
$(变量名)
```

## 条件表达式

> [ condtion ]
>
> test condition

+ 注意conndition两侧要有空格
+ 结果0表示真，1表示假
+ 支持&&和||

> 字符串类型

```
[ $a = hello ]
[ $a != hello]
```

> 数字类型判断

```
    #等于
    [ $2 -eq 2 ]  
    #小于
    [ $2 -lt 5]   
    #小于等于
    [ $2 -le ]
    #大于
    [ $2 -gt 5]
    #大于等于
    [ $2 -ge 5]
    #不等于
    [ $2 -lt 5]
```

> 文件权限判断

+ -r：可读权限
+ -x：可写权限
+ -e：可执行权限

```
[ -r hello.sh ]
```

> 文件类型判断

+ -d：目录





+ -z：是否是零值



## 流程控制

> 分支流程

```bash
if [ 条件表达式 ]
then
	程序
elif [ 条件表达式 ]
	程序
else [ 条件表达式 ]
	程序
fi
```

> 循环流程

```
for((初始值；循环控制条件；变量变化))
do
	程序
done
```

增强for循环

```
for i in 值1，值2，值3...;
do
	程序;
done
```

```
while [ 条件表达式 ]
do
	程序;
done
```

案例：从0加到100

```
sum=0
for((a=1；i <= 100；i++))
do
	sum=$[$sum+$i]
done
echo $sum
```

 增强for循环用法

```
for i in {1..100};
do
	sum=$[$sum+$i];
done;
echo $sum
```

## 读取控制台输入

> read [-t] [-p] [赋值的变量名]

+ -t：表示等待时间
+ -p：提示语

```bash
#!/bin/bash
read -t 10 -p "请输入姓名" name
echo "输入"$name
```

## 函数

> 系统函数

+ basename：通过文件完整路径获取文件名
+ dirname：通过文件完整路径获取路径

```
echo $(basename /home/loki/test.txt)
```

输出

```
test.txt
```

> 用户函数：

```
function [函数名][()]
{
	程序
	return
}
```

+ 调用函数之前必须要申明
+ 函数返回值只能通过 `$?` 来获得，且值只能为数值`（0~255）`



# 二、简单综合应用

## 文件的定期归档备份

```bash
#!/bin/bash
#首先判断输入参数个数是否为1

if [ $# -ne 1 ] 
then
	echo "参数个数错误"
	exit
fi


#从参数中获取目录名称
if [ -d $1 ]
then
	echo
else
 	echo
	echo "目录不存在"
	echo
	exit
fi 


#获取绝对路径
DIR_NAME=$(basename $1)
DIR_PATH=$(cd $(dirname $1); pwd)


#获取日期
DATE=$(date +%y%m%d)
#定义生成归档文件名
FILE=archive_${DIR_NAME}_$DATE.tar.gz
DEST=/home/loki/test/$FILE

#开始归档
echo "start"
echo

tar -czf $DEST $DIR_PATH/$DIR_NAME

if [ $? -eq 0 ] 
then
        echo
        echo "success"
        echo "归档文件为：$DEST"
else
        echo "出现问题"

fi

exit
```

甚至可以通过crontab来进行每天自动备份



## 正则表达式匹配







## 向登录同一服务器的用户发送消息

> 我们可以利用Linux自带的`mesg`和`write`工具，向其它用户发送消息

**需求:**

实现一个向某个用户快速发送消息的脚本，输入用户名作为第一个参数，后面直接跟要发送的消息。脚本需要检测用户是否登录在系统中、是否打开消息功能，以及当前发送消息是否为空。



对于centos而言，使用以下命令查看登录服务器的用户和是否打开消息功能

```
who -T
```

如下所示

```bash
[root@centos7]# who -T
root     + pts/0        2022-06-24 17:36 (42.93.144.98)
loki     + pts/1        2022-06-25 01:20 (42.93.144.98)
```

注：

+ `+` 代表消息功能是打开的
+ 以上代码表示有两个用户，root和loki
+ 这个功能可以通过 `mesg n` 关闭

在root用户下发送消息

```
[root@centos7]# write loki pts/1 
hello,loki
```

loki用户收到消息

```
Message from root@VM-16-12-centos on pts/0 at 01:25 ...
hello,loki
```

















