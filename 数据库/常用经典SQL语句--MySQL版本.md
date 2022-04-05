# 常用经典SQL语句--MySQL版本

## 常见SQL命令；

+ 创建数据库

```sql
create database databasename; 
```

+ 删除数据库

```sql
drop database databasebname; 
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

```sql
updade student set name='loki' where id=1; 
```

+ 查询当前数据库版本

```sql
select version();    
mysql --version   
mysql -V 
```

