## Redis主从复制

### 简介

主机数据更新后根据配置和策略，自动同步到备机的master/slaver机制，Master 以写为主，Slave以读为主

![image-20220627175727017](https://s2.loli.net/2022/06/27/ZT7FGCcJfhopr9x.png)

### 优势

+ 读写分离，性能拓展，但是只能有一台主服务器
+ 容灾快速恢复

### 配置Reids主从复制

1. 启动redis服务器，一台主服务器，多台从服务器

2. 在从服务器执行以下命令  

   ```
   slaveof <ip> <port>
   ```



通过以下命令查看当前redis服务属性

```
info replication
```



### 哨兵模式





























































