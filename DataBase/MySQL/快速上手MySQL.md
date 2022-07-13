参考资料

+ https://db-engines.com/en/ranking
+ https://www.bilibili.com/video/BV1iq4y1u7vj
  + 《MySQL必知必	会》

# 一、MySQL入门：最流行的关系型数据库管理系统

## 关系型数据库说明

我们可以将数据存储在文件中，但是在文件中读写数据速度相对较慢。所以，现在我们使用关系型数据库管理系统（RDBMS）来存储和管理大数据量。在 WEB 应用方面 MySQL 是最好的 `RDBMS(Relational Database Management System` 关系数据库管理系统)应用软件

> **RDBMS的特点**

- 数据以表格的形式出现
- 每列为记录名称所对应的数据域
- 许多的行和列组成一张表单
- 若干的表单组成database

> **RDBMS 术语**

- **数据库:** 数据库是一些关联表的集合
- **数据表:** 是数据库的基本组成单元，所有的数据都以表格的形式组织，一个表有行和列
- **列:** 一列包含了相同类型的数据也称为**字段**，所有表都是由一个或多个列组成的
  - 每一列类似Java类中的属性
  - 每个字段都应该有这些属性——字段名，数据类型，相关约束
- **行：**表中数据按行存储，每一行类似于java中的一个对象

+ **冗余**：存储两倍数据，冗余降低了性能，但提高了数据的安全性。
+ **索引：**使用索引可快速访问数据库表中的特定信息。索引是对数据库表中一列或多列的值进行排序的一种结构。类似于书籍的目录。
+ **参照完整性:** 参照的完整性要求关系中不允许引用不存在的实体。与实体完整性是关系模型必须满足的完整性约束条件，目的是保证数据的一致性。

> **表文件中的字段种类**

- **主键**：主键是唯一的。一个数据表中只能包含一个主键。行的唯一标识，用于区分数据行。不能为null，不能为重复值
- **外键：**外键用于关联两个表，只存在于多方表（一对多表中的多方）中，用于建立数据之间的隶属关系
- **复合键**：复合键（组合键）将多个列作为一个索引键，一般用于复合索引

## MySQL特性

**MySQL 是一种关系型数据库，主要用于持久化存储我们的系统中的一些数据比如用户信息,所谓的关系型数据库，是建立在关系模型基础上的数据库，借助于集合代数等数学概念和方法来处理数据库中的数据。**

- MySQL 使用标准的结构化查询语言 SQL ，支持复杂查询，支持事务
- MySQL 可以运行于多个系统上，并且支持多种语言。这些编程语言包括 C、C++、Python、Java、Perl、PHP、Eiffel、Ruby 和 Tcl 等。
- MySQL 支持大型数据库，支持 5000 万条记录的数据仓库，32 位系统表文件最大可支持 4GB，64 位系统支持最大的表文件为8TB。

> MySQL的四大版本

+ `MySQL Community Server 社区版本`：开源免费，不提供官方技术支持
+ `MySQL Community Server 企业版本`：付费，不能在线下载，提供了更多的功能和技术支持
+ `MySQL Cluster 集群版`：开源免费，用于假设集群服务器，可将几个MySQL server集群为一个MySQL server，需要在社区版或者企业版的基础上使用
+ `Mysql Cluster CGE 高级集群版`：付费

## 安装MySQL Commuinty Server

### Windows环境下安装卸载

**MySQL Commuinty Server在[MySQL官网](https://dev.mysql.com/downloads/mysql/)进行下载，有两种安装方式**

+ **ZIP Archive**：二进制文件解压，相对纯净，但是数据目录等需要自己配置，比较繁琐

+ **MSI Installer**：可以在安装时配置MySQL，但是需要注意安装时的选项

> **解压方式安装步骤详解**

1. 下载`Windows (x86, 64-bit), ZIP Archive`

2. 解压到工作目录（**路径中不可有英文**），解压后将MySQL的bin目录绝对路径到path环境变量

3. 安装版需要在根目录下新建并配置 `my.ini` 文件（Windows 系统环境下 MySQL 的这个配置文件叫 `my.ini `，但是在 CentOS 系统环境下这个配置文件就叫 `my.cnf` ，他们的后缀不同，但是里面的配置项基本上都是相通的）

   修改这个文件后需要重新启动mysql服务

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

4. 初始化安装，以管理员身份运行cmd，切换到mysql的bin目录，执行下面语句

   ```
   #初始化安装，运行之后会有初始密码。记得保存
   mysqld --initialize --console
    
   #安装mysql服务（服务名可以自定义，也可以省略）
   mysqld --install [服务名] 
   
   #启动mysql服务
   net start mysql --defaults-file=["my.ini路径"]
   ```

至此，安装完成

> 查看mysql数据库数据的存放目录(需要登陆)

```
show variables like 'datadir'; 
```

```
mysql> show variables like 'datadir';
+---------------+------------------------------------------+
| Variable_name | Value                                    |
+---------------+------------------------------------------+
| datadir       | D:\environment\mysql-8.0.23-winx64\data\ |
+---------------+------------------------------------------+
1 row in set, 1 warning (0.00 sec)
```

> 命令行登录

+ **使用mysql -u root -p登陆数据库会提示输入密码或者用Navicat图形界面直接建立连接的话会报错：**

```
ERROR 1045 (28000): Access denied for user 'root'@'localhost' (using password）
```

+ **查看初始密码并进行修改**

重新启动MySQL服务，并进入CMD命令行，在此使用mysql -u root -p登陆，键入密码

```
set password for root@localhost = password('你的密码');
```

> Windows卸载MySQL

+ 打开windows服务管理，停止MySQL服务

- 控制面板卸载 MySQL
- 删除 MySQL 数据库安装文件夹
- 打开注册表编辑器
- `Win+R`，输入 `regedit` 进入系统注册表窗口，删除残留注册表文件夹，如果没有则忽略
  + HKEY_LOCAL_MACHINE\SYSTEM\ControlSet001\Services\Eventlog\Application\MYSQL
  + HKEY_LOCAL_MACHINE\SYSTEM\ControlSet002\Services\Eventlog\Application\MYSQL
  + HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\Services\Eventlog\Application\MYSQL
- 删除 **`C：\ProgramData\MYSQL`** 所有文件

### Linux环境下安装卸载









### Docker环境下安装卸载



## MySQL基础架构与目录结构分析

[MySQL基础架构与目录结构分析](https://blog.csdn.net/Night__breeze/article/details/124372695)



## MySQL数据类型





## MySQl数据库关命令行操作

+ 登录数据库(dos命令行操作)

```
mysql -u[用户名] -p[密码]
```

+ 创建数据库

```sql
create database [数据库名]; 
```

+ 删除数据库

```sql
drop database [数据库名]; 
```

+  修改当前库选项信息

```
alter database [数据库名] [选项]
```

+  输出所有数据库

```sql
show databases; 
```

+  选择数据库

```sql
use [数据库名];  
```

+ 查看当前数据库的所有表

```sql
show tables;   
```

+ 查看其他库的所有表

```sql
show tables from [数据库名];
```

+ 输出当前使用的数据库名

```sql
select database();
```

## MySQL数据库表命令行操作

+ 创建一个新表

```sql
create table tablename(
    id int,
    name varchar(20)
);
```

+ 删除一个数据表

```sql
drop table [tablename]
```

+ 重命名表

```sql
rename table [原表名] to [新表名]
```

+ 清空表数据

```sql
truncate table [表名]
```

+ 查看表的结构

```sql
desc tablename; 
```

+ 输出表中所有对象

```sql
select *  from tablename;  
```

+ 更新表对象

```mysql
updade student set name='wangzhe' where id=1; 
```

+ 查询当前数据库版本

```mysql
select version();    
mysql --version   
mysql -V 
```





# 二、结构化查询语言SQL

**DBMS负责编译执行SQL语句，通过执行SQL语句来控制DB中的数据**



> SQL语言分类

+ `DDL（Data Definition Languages、数据定义语言）`这些语句定义了不同的数据库、表、视图、索
  引等数据库对象，还可以用来创建、删除、修改数据库和数据表的结构。关键字包括 `CREATE 、 DROP 、 ALTER` 等
+ `DML（Data Manipulation Language、数据操作语言）`，用于添加、删除、更新和查询数据库记
  录，并检查数据完整性。关键字包括 `INSERT 、 DELETE 、 UPDATE 、 SELECT` 等

+ `DCL（Data Control Language、数据控制语言）`，用于定义数据库、表、字段、用户的访问权限和
  安全级别。关键字包括 `GRANT 、 REVOKE 、 COMMIT 、 ROLLBACK 、 SAVEPOINT` 等

+ `DQL（数据查询语言）`：`SELECT`
+ `TCL （Transaction Control Language，事务控制语言）`: `COMMIT 、 ROLLBACK`

> SQL语言大小写规范

+ **MySQL 在 Windows 环境下是大小写不敏感的**
+ **MySQL 在 Linux 环境下是大小写敏感的**
  + 数据库名、表名、表的别名、变量名是严格区分大小写的
  + 关键字、函数名、列名(或字段名)、列的别名(字段的别名) 是忽略大小写的
+ **推荐采用统一的书写规范：**
  + 数据库名、表名、表别名、字段名、字段别名等都小写
  + SQL 关键字、函数名、绑定变量等都大写

> SQL注释

```
单行注释：#注释文字
单行注释：-- 注释文字 (--后面必须包含一个空格。)
多行注释：/* 注释文字  */
```

## 数据查询语言DQL：查询表中数据

DQL全称`Data Query language`，即数据查询语言

**首先提出一个概念：临时表**

1. 定义：查询命令在内存中生成的表
2. 作用：单表查询包含了7个查询命令，除了from命令之外，剩下六个查询操作的都是上一个查询命令生成的临时表
3. 生命周期：当前查询命令执行完毕后，MySQL服务器会自动删除上一个临时表，所以最终返回的临时表只能是最后一个查询命令生成的临时表
4. 只有group by命令执行完毕后有可能生成多个临时表
5. 只有having命令执行完毕后，不会生成新的临时表，负责将不满足条件的临时表进行销毁处理

###  单表查询

> **七个查询命令以及执行优先级**

**FROM > WHERE > GROUP BY > HAVING > SELECT  > ORDER BY > LIMIT**

> **一个使用了7个命令的栗子**

Q:从员工表中查询50-70岁之间的员工姓名，根据性别进行分组，然后按找薪资升序排列，截取0到5行的数据

```mysql
select employee_name from employee where age between 50,70 group by sex order by alary  ASC limit 0,5
```

**==SELECT==**

select [字段名||函数||子查询] FROM 表文件;

```mysql
select [字段名] from [表名];
#查询表文件所有字段
select * from [表名];
#列的别名，as可以不加
select [字段名] as [字段别名] from [表名];
#在SELECT语句中使用关键字DISTINCT去除重复行
SELECT DISTINCT department_id FROM   employees;
#这里有两点需要注意
    1.  DISTINCT 需要放到所有列名的前面，如果写成 SELECT salary, DISTINCT department_id
    FROM employees 会报错。
    2.  DISTINCT 其实是对后面所有列名的组合进行去重，你能看到最后的结果是 74 条，因为这 74 个部
    门id不同，都有 salary 这个属性值。如果你想要看都有哪些不同的部门（department_id），只需
    要写 DISTINCT department_id 即可，后面不需要再加其他的列名了。
```

**==WHERE==**

过滤数据：循环遍历from生成的临时表中数据行，遍历时定位满足条件的数据行，结束后返回一个新的临时表

```mysql
SELECT * FROM [tablename] WHERE 判断条件;
	#判断条件
		#逻辑表达式——mysql服务器提供特殊运算符
		SELECT * FROM [tablename] WHERE 判断条件1 and 判断条件2;
		SELECT * FROM [tablename] WHERE 判断条件1 or 判断条件2;	
		# between...and... 在某个范围内
		SELECT * FROM [tablename] WHERE 判断内容 between.. and..;
		# in 包含这些
		SELECT * FROM [tablename] WHERE 判断内容 in('条件1','条件2'...);
		# not in 不包含这些
		SELECT * FROM [tablename] WHERE 判断内容 not in('条件1','条件2'...);
		# is null 所选字段是否为空 不能是 name==null,这是错误的运算
		SELECT * FROM [tablename] WHERE 判断内容 is null;
		# is not null
		SELECT * FROM [tablename] WHERE 判断内容 is not null;
```

**==like：模糊查询==**

```mysql
#通配符:  % 表示一个任意长度的字符串 _ 表示一个占位符
	#查询姓名以s开头的职员--前置模糊查询
	select * from 表 where name like 's%';
	#查询姓名以s结尾的职员--后置模糊查询
	select * from 表 where name like '%s';
	#查询姓名包含s的职员--包含模糊查询
	select * from 表 where name like '%s%';
	#查询姓名第三个是s的职员
	select * from 表 where name like '__As%';
```

**==聚合函数：统计当前字段下所有数据信息==**

```mysql
# max() -- 统计当前字段下最大值
	select max(age) from 表名 where grade=6;
	# min() -- 统计当前字段下最小值
    select min(age) from 表名 where grade=6;
	# avg() -- 统计当前字段下平均值
	select avg(age) from 表名 where grade=6;
	# sum() -- 统计当前字段数据和
	select sum(age) from 表名 where grade=6;
	# count() -- 统计当前字段内容不为null的个数
	select count(name) from 表名 where grade=6;
	# count(*) -- 统计当前表下有多少行（对象个数）
	select count(*) from 表名;
```

**==group by：分组查询命令==**

```mysql
-- 语句中group by 和 where 要写在select的后面，当同时存在时，where要写在前面
	select count(*) from 表名 where 数据行定位条件条件 group by 分组字段
    -- eg:select count(*) from 表名 where age>20 group by sex 根据性别分组
	-- 多字段分组
		-- 多字段分组时，分组字段出现的顺序都查询结果没有任何影响
		-- 会出现多个临时表
```

**==having：临时表过滤条件==**

```mysql
-- 只能声明在group by的后面
	-- 该命令执行完毕后不会生成新的临时表
	-- 循环遍历group by生成的每一个临时表，遍历过程中将不满足条件的临时表进行销毁
	-- where与having区别(面试常考)
   /*
   	1、where 将满足条件的数据行读取出来生成一个全新的临时表，而having 将不满足的条件的临时表进行销毁
    2、where每次只能操作一行数据，而having每次操作一个临时表
    3、having可以调用聚合函数
    4、having不可以独立的出现在查询语句中，只能出现在group by的后面
   */

```

**==order by：排序==**

+ asc   升序
+ desc  降序

```sql
select * from [tablename] order by age desc;
```

**==limit：mysql特有==**

+ 写在查询语句最后
+ [位置偏移量] [行数]

```mysql
#从第0行开始查询5条数据
select * from [tablename] limit 0,5;
```



### 多表查询

**合并两张表的解决方案：连接查询合并方案  /  联合查询合并方案**

+ 每次多表查询只有两张，如果是更多的表则需要两两合并

+ 多表查询的流程

  1、将两个表中的数据行合并到一个全新临时表

  2、使用6个查询命令从这个临时表摘取需要数据(少了from)

#### 1、连接查询合并方案

1. 要求两个表存在隶属关系

2. 将两张表沿着【水平方向】拼接

3. 展示给用户一个合法的隶属关系数据

4. 格式

   ```mysql
   FROM 一方表 JOIN 多方表;
   FROM 多方表 JOIN 一方表;
   #FROM命令将两张表加载到内存生成两个临时表
   #JOIN命令将两张临时表数据横向拼接生成一个全新临时表
   ```

+ JOIN的规则

1. 临时表中字段的行数 = 一方临时表字段个数 + 多方临时表字段个数

2. 临时表字段名称 ： 原始表.字段名，避免出现同名字段

3. **临时表数据行个数 = 一方临时表数据个数 * 多方临时表数据个数**

4. 拼接规则：一方表中的每一行数据和多方表中的所有数据行拼接

5. **由join生成的临时表必然存在【脏数据行】**

   **由于使用JOIN连接后会有许多脏数据行，因此需要将这些数据过滤**

+ **内连接过滤方案**

  ```mysql
  FROM 一方表 JOIN 多方表
  ON 合法数据判断条件
  ```

  ON的 执行和WHERE非常相似:

  ​	1、循环遍历临时表的每一个数据行

  ​	2、遍历过程中将合法的数据行取出来存到新的临时表

  ​    3、区别：On主要是用来过滤不合法数据，where用来获取业务需要的数据，

+ **合法数据行的判断条件：**

  ```mysql
  1、多方表中存在明显的外键字段
  	FROM 一方表 JOIN 多方表
  	ON 当前数据行.来自于一方表的主键值 = 当前数据行.来自多方表的外键值
  2、如果多方表中没有外键字段，此时根据实际生活中的隶属关系判断
  	FROM 一方表 JOIN 多方表
  	ON 实际生活中隶属关系
  ```

+ **外连接过滤方案**

  相对于内连接方案稍微宽松一点，对**某一张表进行偏袒**

  1、将两张表划分一张需要帮助的表，确保被帮助的表所有的数据行都能进入合法临时表

  2、如果【需要帮助的表】与【不需要帮助的表】所有数据行都没法匹配成功，需要将这个数据行作为一个独立的数据行填充到新的临时表，缺失的数据都是null值

  3、**命令格式： 左外连接 / 右外连接(效果是一样的)**

```mysql
#左外连接-被帮助的表在右边
FROM 需要帮助表 LEFT JOIN 不需要帮助的表
ON 合法数据行定位条件

#右外连接
FROM 不需要帮助的表 RIGHT JOIN 需要帮助的表
ON 合法数据行定位条件
```

#### 2、联合查询合并方案

**开发中一般只用于行转列查询**

+ 不需要两张表临时表之间存在隶属关系
+ 要求两个临时表字段结构必须保持一致【字段个数，字段类型，字段类型排列顺序】
+ 将两张临时表沿着垂直方向堆砌
+ 生成的临时表字段只能来自于第一个临时表字段

```mysql
select 字段一,字段2,字段3 ... from 表1
union
select 字段一,字段2,字段3 ... from 表2
```

### 子查询

+ 【简单】依赖子查询/独立子查询

+ 七个查询命令需要在当前临时表中获得相关数据才能正常进行，因此**MySQL服务器允许开发人员通过一个完整的查询语句为当前命令提供需要的数据**

##### 1、子查询与七个查询命令的关系--需要使用数据就可以使用子查询

1、from

```mysql
select t.name,t.age from (select name,job,age from stu) as t 
```

​				2、where

```mysql
select .. from .. where (select .. from .. )
```

​				3、group by 之后设置的是分组字段名称，不能出现子查询

​				4、having

```mysql
select .. from .. group by .. having(select .. from ..)
```

​				5、select--子查询可以提供临时表中不存在的数据

​				6、order by 后面是字段名，不能使用子查询，如果碰到类似子查询的语句，应当将它理解为字段

​				7、limit 之后是数据行的行号，无法使用子查询

##### 2、独立子查询

+ 指子查询执行时不需要被帮助的查询提供任何帮助
+ 一些规则
  1. 返回的结果相当于一个常量
  2. 独立子查询只会在被绑住命令执行前执行一次
  3. 对最终运行速度影响不打所以推荐使用独立子查询
+ 注意子查询的执行顺序，尤其对于select，是独立子查询先运行

##### 3、依赖子查询---【过街老鼠】没人用

+ 子查询必须得到被帮助的命令提供帮助才能执行
+ 根据接受的参数不同，返回结果也不同
+ 在被帮助的命令执行时被调用，加载表文件次数会增加，因此实际开发过程中禁止使用依赖子查询
+ 可以使用连接查询或者自关联查询代替依赖子查询--通过查询获得一个临时表，然后将两个表连接后进行查询88

### 自关联查询

+ 表中字段本身存在隶属关系，就可以使用自关联

​	1、连接查询的一种特殊的表现形式

​	2、将同一张表分别加载到内存中两次进行合并

​	3、从一张临时表读取数据，另一张临时表做对比

```mysql
#查询职员姓名，工资，领导姓名，工资
SELECT t1.ename,t1.sal,t2.ename as 领导姓名,t2.sal as 领导工资
FROM emp as t1 join emp as t2#t1获得职员信息，t2获得领导信息
ON 
t1.mgr =t2.empno

```

### 查询结果中解释字段数据的来源

+ 解释查询的字段是什么

```mysql
select 查询内容1 '解释内容1' as 解释字段名1，查询内容2 '解释内容2' as 解释字段名2 from ...
```

+ 在select执行之前，在临时表动态的生成一个列，列中内容用于对临时表每一行数据进行解释分析

```mysql
#用于解释于聚合函数

#用于解释独立子查询

#用于解释开发者手动设置的常量

#用于CASE...AND语句
```

CASE...END语句

给每一行一个解释数据

+ 特点
  1. 在select执行之前执行
  2. 执行时遍历临时表的每一行数据，在遍历时为每一行数据提供一个动态解释内容
  3. 在执行完毕时，生成的解释数据作为一个独立的列出现在临时表中
+ 命令格式

```mysql
#CASE...END进行区间值判断，格式类似于if..else 
select (case 
			when 当前数据行字段值 区间判断 then "解释数据1"
        	when 当前数据行字段值 区间判断 then "解释数据2"
        	when 当前数据行字段值 区间判断 then "解释数据3"
        	else '解释数据3'
        end
       )as 解释字段名称
       

```

### 行转列  /  列转行查询

​	实际开发和面试中常用，留作日后单独写博客补充

+ 行转列查询

+ 列转行查询

## 数据操作语言DML：对表数据的增删改

DML全称`Data manipulation language`

+ **insert**

  向表中插入数据

  ```sql
  insert into 表名 (字段名1,字段名2,字段名...) values(值1,值2,值...);
  ```

  简化版

  ​		使用场景:如果插入数据行时，可以保证对表文件中每一个字段都进行赋值，可以省略赋值字段名称。此时要		求values中值个数声明顺序必须与表文件字段声明顺序保持一致

  ```sql
  insert into  values(值1,值2,值...);
  ```

  批处理插入--一次性将多条数据添加到表文件

  ```sql
  insert into values(值1,值2,值..),(值1,值2,值..),(值1,值2,值..);
  ```

  

+ **delete**

  删除表

  ```sql
  delete from 表名;
  ```

  有条件的删除

  ```sql
  /*删除指定条件的数据*/
  delete from 表名 where 判断条件;
  ```

+ **update**

  更新数据

  ```sql
  /*修改指定表中所有数据指定字段的值*/
  update 表名 set 字段1=新值,字段2=新值;
  ```

  有条件的更新

  ```sql
  update 表名 set 字段1=新值,字段2=新值 where 判断条件;
  ```

  

> 特别注意表文件备份和数据行备份

+ **表文件的备份**

  ```sql
  create table 新表 select * from 旧表
  ```

+ **数据行的备份--只备份数据，将旧表数据存入新表**

  ```sql
  /*要求字段个数，类型，排列顺序保持一致*/
  insert into 新表 select * from 旧表;
  ```





## 数据定义语言DDL：对表结构的增删改

DDL全称`Data Define Language`

+ **creat**

  创建数据库

  ```sql
  create database 数据库名;
  ```

  创建新表

  ```sql
  create table 表名(
  	字段名 数据类型,
  	字段名 数据类型
  );
  ```

  

+ **drop**

  删除数据库

  ```sql
  drop database 数据库名;
  ```

  删除表

  ```sql
  drop table 表名;
  ```

+ **alter**

  为表添加新字段

  ```sql
  alter table 表名 add 字段名 数据类型;
  ```

  删除表中的字段

  ```
  alter table 表名 drop 字段名;
  ```

  修改文件字段----修改字段名/字段类型

  1. ```sql
     mysql修改字段类型：	 
     --能修改字段类型、类型长度、默认值、注释
     ALTER  TABLE 表名 MODIFY [COLUMN] 字段名 新数据类型 新类型长度  新默认值  新注释;
     ```

  2. ```sql
     mysql修改字段名：
     ALTER  TABLE 表名 CHANGE [column] 旧字段名 新字段名 新数据类型;	
     ```

  3. -- COLUMN关键字可以省略不写



# 三、Innodb：MySQL默认存储引擎

官网介绍[Introduction to InnoDB](https://dev.mysql.com/doc/refman/8.0/en/innodb-introduction.html)











# 四、MySQL事务





# 五、MySQL日志与备份







# 五、MySQL索引与调优篇















# 六、如何书写高性能的SQL







