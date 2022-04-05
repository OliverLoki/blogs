# Mysql绿色版安装与Navicat的破解

## 绿色版安装步骤

### 1、在官网下载MySQL

https://dev.mysql.com/downloads/mysql/

![image-20210409010750115](E:\typoradata\img\image-20210409010750115.png)

注:oracle官网安装需要登录，注册一个账户即可

### 2、在根目录新建data文件夹。

### 3、在根目录新建my.ini配置文件。

```ini
[mysqld]
# 设置3306端口
port=3306
# 设置mysql的安装目录
#可以自定义
basedir=C:\Program Files\MySQL
# 设置mysql数据库的数据的存放目录
#可以自定义
datadir=C:\Program Files\MySQL\Data
# 允许最大连接数
max_connections=200
# 允许连接失败的次数。
max_connect_errors=10
# 服务端使用的字符集默认为utf8mb4
character-set-server=utf8mb4
# 创建新表时将使用的默认存储引擎
default-storage-engine=INNODB
# 默认使用“mysql_native_password”插件认证
#mysql_native_password
default_authentication_plugin=mysql_native_password
[mysql]
# 设置mysql客户端默认字符集
default-character-set=utf8mb4
[client]
# 设置mysql客户端连接服务端时默认使用的端口
port=3306
default-character-set=utf8mb4
```

### 4、以管理员身份运行cmd，切换到mysql的bin目录，执行下面两个语句

```sql
mysqld --initialize --console
```

​			 运行之后会有初始密码。记得保存

![image-20210321190422180](C:\Users\yu'chun\AppData\Roaming\Typora\typora-user-images\image-20210321190422180.png)

```
mysqld --install
```

 	net start mysql

### 5、使用mysql -u root -p登陆数据库会提示输入密码或者用Navicat图形界面直接建立连接的话会报错：

  ERROR 1045 (28000): Access denied for user 'root'@'localhost' (using password）

### 6、查看初始密码并进行修改

1. 重新启动MySQL服务，并进入CMD命令行，在此使用mysql -u root -p登陆，键入密码
2. cmd进入数据库输入以下代码

```
set password for root@localhost = password('你的密码');
```

### 7、提示Query OK，修改成功，之后你可以尝试重新登录。至此安装完成，使用Navicat（Mysql图形化管理页面）来使用Mysql





## Mysql安装目录

1. bin  ->二进制的可执行文件
2. data -> mysql的数据文件日志文件等
3. include -> C语言的一些头信息
4. lib ->存放运行所需要的一些jar包
5. share - > Mysql的一些错误信息

## Mysql数据目录

## 卸载

### 1、关闭Mysql服务

+ 打开服务的快捷键关闭

```
services.msc
```

+ 可以用命令行关闭管理员身份运行命令行

```
 net stop mysql
```

### 2、删除Mysql的注册表

+ Win+R打开运行界面，在输入框中输入 regedit 进入系统注册表窗口
+ 分别在以下目录中找到 MySQL 的注册表，鼠标右键直接删除MySQL目录中的 EventMessageFile 和 TypesSupported 两个文件就好了,如果对应的目录中没有,就不用删除了，也可以搜索注册表: 在系统注册表窗口选择「编辑」 — 选择「查找」 — 输入 「MySQL」进行查找,将找到的MySQL目录中的 EventMessageFile 和 TypesSupported 两个文件进行删除
  ![img](https://img-blog.csdn.net/20180910201015150?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM5MTM1Mjg3/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)



### 3、移除Mysql服务

1. 以管理员身份使用命令行(cmd)进入MySQL的 bin 目录下
2. 执行移除 MySQL服务的命令 : mysqld -remove
3. 当看到有Service successfully removed时，则表示移除Mysql服务成功

###  4、删除Mysql文件

   将Mysql安装目录下的文件全部删除即可

# Navicat Premium的破解使用

## 1、Navicat Premium的安装

https://www.navicat.com.cn/download/navicat-premium

## 2、使用注册机破解

https://www.jianshu.com/p/3af7fccc22c7

破解教程：

> https://www.jianshu.com/p/3af7fccc22c7















