# SQl学习详细笔记



## 一、SQL 简介

+ **Structured Query Language)**

+ **结构化查询语言，高级语言**
+ **是用于管理关系数据库管理系统（RDBMS）。** 
+ **SQL 的范围包括数据插入、查询、更新和删除，数据库模式创建和修改，以及数据访问控制**
+ **SQl语句在执行的时候，内部会先进行编译，由DBMS完成**
+ **DBMS负责执行SQL语句，通过执行SQL语句来控制DB中的数据**

## 二、数据库简介

[数据库分类特点及简介](https://blog.csdn.net/Night__breeze/article/details/116610686)

## 三、对表的理解

+ table是数据库的基本组成单元，所有的数据都以表格的形式组织，目的是于一种可读性强

+ 一个表包括行和列

  1. 列：我么也称为字段，所有表都是由一个或多个列组成的，**每一列类似Java类中的属性**

     ​		**每个字段都应该有这些属性-----字段名，数据类型，相关约束**

  2. 行：**表中数据按行存储，每一行类似于java中的一个对象**
  
+ 表文件中的字段种类

  1. 主键字段
     1. 数据行的唯一标识，用于区分数据行
     2. 不能为null，不能为重复值
  2. 非主键字段
     1. 解释主键字段，让主键字段更加丰富
  3. 外键字段
     1. 只存在于多方表（一对多表中的多方）中，用于建立数据之间的隶属关系

## 四、SQL的学习--CRUD

### 1、常用对数据库的操作

+ 登录数据库(dos命令行操作)

```
mysql -uroot -proot
```

+ 创建数据库

```sql
create database 数据库名; 
```

+ 删除数据库

```sql
drop database 数据库名; 
```

+  输出所有数据库

```sql
show databases; 
```

+  使用这个数据库

```sql
use 数据库名;  
```

+ 查看当前数据库的所有表

```sql
show tables;   
```

+ 查看其他库的所有表

```sql
show tables from databasename;
```

+ 输出当前使用的数据库名

```sql
select database();  
```

+ 创建一个新表

```sql
create table tablename(
id int,
name varchar(20)
);
```

+ 输出表的结构

```sql
desc tablename; 
```

+ 输出表中所有对象

```sql
select *  from tablename;  
```

+ 更新对象

```mysql
updade student set name='wangzhe' where id=1; 
```

+ 查询当前数据库版本

```mysql
select version();    
mysql --version   
mysql -V 
```

+ 查看表结构

  ![image-20210511180918835](https://i.loli.net/2021/05/11/jWq3BAGFwxrsdSu.png)

### 2、DQL

**(数据查询语言)---Data Query language--查询数据**

> 学习SQL语句应当有练习数据，附练习sql代码片段如下
>
> ```mysql
> #插入表的结构
> drop table if exists dept;
> drop table if exists salgrade;
> drop table if exists emp; 
> 
> create table dept(  
> 	deptno int(10) primary key,  
> 	dname varchar(14),  
> 	loc varchar(13));  
> 
> create table salgrade(  
> 	grade int(11), 
> 	 losal int(11), 
> 	 hisal int(11)  );  
> 
> create table emp(  
> 	empno int(4) primary key, 
> 	ename varchar(10), 
> 	job varchar(9),  
> 	mgr int(4),  
> 	hiredate date, 
> 	sal double(7,2),  
> 	comm double(7,2),  
> 	deptno int(2)  ); 
> 
> #插入表中的数据
> 
> insert into dept(deptno,dname,loc) values(10,'ACCOUNTING','NEW YORK');
> insert into dept(deptno,dname,loc) values(20,'RESEARCHING','DALLAS');
> insert into dept(deptno,dname,loc) values(30,'SALES','CHICAGO');
> insert into dept(deptno,dname,loc) values(40,'OPERATIONS','BOSTON');
> 
> insert into salgrade(grade,losal,hisal) values(1,700,1200);
> insert into salgrade(grade,losal,hisal) values(2,1201,1400);
> insert into salgrade(grade,losal,hisal) values(3,1401,2000);
> insert into salgrade(grade,losal,hisal) values(4,2001,3000);
> insert into salgrade(grade,losal,hisal) values(5,3001,5000); 
> 
> insert into emp(empno,ename,job,mgr,hiredate,sal,comm,deptno) values(7369,'SIMITH','CLERK',7902,'1980-12-17',800,null,20);
> insert into emp(empno,ename,job,mgr,hiredate,sal,comm,deptno) values(7499,'ALLEN','SALESMAN',7698,'1981-02-20',1600,300,30);
> insert into emp(empno,ename,job,mgr,hiredate,sal,comm,deptno) values(7521,'WARD','SALESMAN',7698,'1981-02-22',1250,500,30);
> insert into emp(empno,ename,job,mgr,hiredate,sal,comm,deptno) values(7566,'JONES','MANAGER',7839,'1981-04-02',2975,null,20);
> insert into emp(empno,ename,job,mgr,hiredate,sal,comm,deptno) values(7654,'MARTIN','SALESMAN',7698,'1981-09-28',1250,1400,30);
> insert into emp(empno,ename,job,mgr,hiredate,sal,comm,deptno) values(7698,'BLAKE','MANAGER',7839,'1981-05-01',2850,null,30);
> insert into emp(empno,ename,job,mgr,hiredate,sal,comm,deptno) values(7782,'CLARK','MANAGER',7839,'1981-06-09',2450,null,10);
> insert into emp(empno,ename,job,mgr,hiredate,sal,comm,deptno) values(7788,'SCOTT','ANALYST',7566,'1987-04-19',3000,null,20);
> insert into emp(empno,ename,job,mgr,hiredate,sal,comm,deptno) values(7839,'KING','PRESIDENT',null,'1981-11-17',5000,null,10);
> insert into emp(empno,ename,job,mgr,hiredate,sal,comm,deptno) values(7844,'TURNER','SALESMAN',7698,'1981-09-08',1500,null,30);
> insert into emp(empno,ename,job,mgr,hiredate,sal,comm,deptno) values(7876,'ADAMS','CLERK',7788,'1987-05-23',1100,null,20);
> insert into emp(empno,ename,job,mgr,hiredate,sal,comm,deptno) values(7900,'JAMES','CLERK',7698,'1981-12-03',950,null,30);
> insert into emp(empno,ename,job,mgr,hiredate,sal,comm,deptno) values(7902,'FORD','ANALYST',7566,'1981-12-03',3000,null,20);
> insert into emp(empno,ename,job,mgr,hiredate,sal,comm,deptno) values(7934,'MILLER','CLERK',7782,'1982-01-23',1300,null,10); 
> 
> select * from dept;select * from salgrade;select * from emp;
> ```
>
> 

####  （1）单表查询

学习内容：单表查询七个查询命令执行特征以及零时表

+ **临时表**
  
  1. 定义：查询命令在内存中生成的表
  2. 作用：单表查询包含了7个查询命令，除了from命令之外，剩下六个查询操作的都是上一个查询命令生成的临时表
  3. 生命周期：当前查询命令执行完毕后，MySQL服务器会自动删除上一个临时表，所以最终返回的临时表只能是最后一个查询命令生成的临时表
  4. 只有group by命令执行完毕后有可能生成多个临时表
  5. 只有having命令执行完毕后，不会生成新的临时表，负责将不满足条件的临时表进行销毁处理
  
+ 查询命令

  1. 执行优先级：

     **FROM > WHERE > GROUP BY > HAVING > SELECT  > ORDER BY > LIMIT**

  2. select的行为取决于提供临时表的命令--尝试写代码来理解
     	1、select操作的临时表由【where/from】提供
     	此时select面对的是一个临时表，select将临时表指定字段下所有数据切成一个新的临时表
     	2、select草错的临时表是【group by】提供
     	此时select面对的临时表是多个，select会依次操作每个临时表，操作每一个临时表的时候，之后读取临时表的第一行，将第一行在内存中合成一个全新的临时表  

  3. from命令相当于读取流【InputStream】，用于将硬盘中表文件加载到内存中生成一个零时表，供后续查询命令使用

```mysql
#1、SELECT
select 字段名||函数||子查询 FROM 表文件;
    #查询表文件所有字段
    select * from 表名;
    #查询表的部分字段
    select 字段名1,字段名2... from 表名;

```

```mysql
#2、WHERE--遍历每一个对象并筛选
	#循环遍历from生成的临时表中数据行，遍历时定位满足条件的数据行，结束后返回一个新的临时表
	SELECT * FROM 表文件 WHERE 判断条件;
	#判断条件
		#逻辑表达式——mysql服务器提供特殊运算符
		SELECT * FROM 表文件 WHERE 判断条件1 and 判断条件2;
		SELECT * FROM 表文件 WHERE 判断条件1 or 判断条件2;	
		# between...and... 在某个范围内
		SELECT * FROM 表文件 WHERE 判断内容 between.. and..;
		# in 包含这些
		SELECT * FROM 表文件 WHERE 判断内容 in('条件1','条件2'...);
		# not in 不包含这些
		SELECT * FROM 表文件 WHERE 判断内容 not in('条件1','条件2'...);
		# is null 所选字段是否为空 不能是 name==null,这是错误的运算
		SELECT * FROM 表文件 WHERE 判断内容 is null;
		# is not null
		SELECT * FROM 表文件 WHERE 判断内容 is not null;
```

```mysql
	#like--模糊查询
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

```mysql
#3、聚合函数--统计当前字段下所有数据信息
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

```mysql
#4、group by--分组查询命令
	-- 语句中group by 和 where 要写在select的后面，当同时存在时，where要写在前面
	select count(*) from 表名 where 数据行定位条件条件 group by 分组字段
    -- eg:select count(*) from 表名 where age>20 group by sex 根据性别分组
	-- 多字段分组
		-- 多字段分组时，分组字段出现的顺序都查询结果没有任何影响
		-- 会出现多个临时表

```

```mysql
#5、having--临时表过滤条件
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

```mysql
#6、order by——排序
	-- asc  升序
	-- desc 降序
	select 查询内容 from 表名 where 数据行定位条件条件 group by 分组字段 order by 字段名称/字段位置（查询内容的） ASC
    -- 多字段排序：在已有条件下进行进一步排序

```

```mysql
#7、limit——mysql特有
	limit 0 , 5
	-- 截取0到5行的数据
	-- mysql临时表字段起始位置从一开始计算
	-- mysql临时表数据行其实位置从0行进行计算
	-- 一般写在查询语句最后
```

#### （2）多表查询

**合并两张表的解决方案：连接查询合并方案  /  联合查询合并方案**

+ 每次多表查询只有两张，如果是更多的表则需要两两合并

+ 多表查询的流程

  1、将两个表中的数据行合并到一个全新临时表

  2、使用6个查询命令从这个临时表摘取需要数据(少了from)

##### 1、连接查询合并方案

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

##### 2、联合查询合并方案

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

#### （3）子查询

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

#### （4）自关联查询

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

#### （5）查询结果中解释字段数据的来源

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

#### （6）行转列  /  列转行查询

​	实际开发和面试中常用，留作日后单独写博客补充

+ 行转列查询

+ 列转行查询

### 3、DML

**(数据操作语言)---Data manipulation language---对表中数据进行增删改**

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





### 4、DDL

**(数据定义语言)---Data Define Language---对表结构的增删改**

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

  3.  -- COLUMN关键字可以省略不写

### 5、DCL

**(数据控制语言)---Data Control Language**

+ grant授权
+ revoke撤销权限

### 6、TCL

**(事务控制语言)---Transactional Control Laguage**

+ commit提交事务
  + rollback回滚事务