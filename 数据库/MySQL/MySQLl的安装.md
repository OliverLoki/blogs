

# Windows环境

## 安装

:dash: **推荐下载解压版自己配置，别用msi安装，方法如下**

1. 打开官网[MySQL Community Downloads](https://dev.mysql.com/downloads/)

   下载`Windows (x86, 64-bit), ZIP Archive`

2. 解压到你的工作目录，配置MySQL根目录到环境变量

3. 在根目录下新建并配置 `my.ini` 文件（Windows 系统环境下 MySQL 的这个配置文件叫 `my.ini `，但是在 CentOS 系统环境下这个配置文件就叫 `my.cnf` ，他们的后缀不同，但是里面的配置项基本上都是相通的）

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

4. 关于MySQL数据文件夹的说明

   **根目录新建一个data文件夹**，默认数据信息就会在这个data文件夹中，在my.ini中配置``datadir='你的data文件夹路径'`,数据库记录的所有信息都会放到这个data文件夹里面

   > **查看mysql数据库数据的存放目录(需要登陆)**
   >
   > ```
   > show variables like 'datadir'; 
   > ```

   ![image-20220423191219672](D:\桌面\P_picture_cahe\gxUmCQ2IMOJNP3n.png)

5. 初始化安装，以管理员身份运行cmd，切换到mysql的bin目录，执行下面语句

   ```
   #初始化安装，运行之后会有初始密码。记得保存
   mysqld --initialize --console
    
   #安装mysql服务（服务名可以自定义，也可以省略）
   mysqld --install [服务名]
   
   #启动mysql服务
   net start mysql
   ```

> 安装完毕，可以登录了



+ **使用mysql -u root -p登陆数据库会提示输入密码或者用Navicat图形界面直接建立连接的话会报错：**

```
ERROR 1045 (28000): Access denied for user 'root'@'localhost' (using password）
```

+ **查看初始密码并进行修改**

重新启动MySQL服务，并进入CMD命令行，在此使用mysql -u root -p登陆，键入密码

```
set password for root@localhost = password('你的密码');
```

## 卸载

1、关闭Mysql服务

+ 打开服务的快捷键关闭

```
services.msc
```

+ 可以用命令行关闭管理员身份运行命令行

```
 net stop mysql
```

2、删除Mysql的注册表

+ Win+R打开运行界面，在输入框中输入 regedit 进入系统注册表窗口
+ 分别在以下目录中找到 MySQL 的注册表，鼠标右键直接删除MySQL目录中的 EventMessageFile 和 TypesSupported 两个文件就好了,如果对应的目录中没有,就不用删除了，也可以搜索注册表: 在系统注册表窗口选择「编辑」 — 选择「查找」 — 输入 「MySQL」进行查找,将找到的MySQL目录中的 EventMessageFile 和 TypesSupported 两个文件进行删除
  ![img](https://img-blog.csdn.net/20180910201015150?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM5MTM1Mjg3/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

3、移除Mysql服务

1. 以管理员身份使用命令行(cmd)进入MySQL的 bin 目录下
2. 执行移除 MySQL服务的命令 : mysqld -remove
3. 当看到有Service successfully removed时，则表示移除Mysql服务成功
4. 删除Mysql文件

   将Mysql安装目录下的文件全部删除即可



# Linux环境











# Docker容器

















# Navicat Premium的破解使用

## 1、Navicat Premium的安装

https://www.navicat.com.cn/download/navicat-premium

## 2、使用注册机破解

https://www.jianshu.com/p/3af7fccc22c7

破解教程：

> https://www.jianshu.com/p/3af7fccc22c7















