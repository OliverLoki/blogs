

> + Redis的出现是为了解决大规模数据带来的挑战
>+ Redis是一种满足CP原则的**高性能分布式**[NoSQL数据库](https://blog.csdn.net/Night__breeze/article/details/123766938)
>   + 关于CP原则请看[分布式数据库中的CAP定理](https://blog.csdn.net/Night__breeze/article/details/123766938)

# 一、Redis简介：key-value内存型数据库

+ **什么是Redis**

  > The open source, in-memory data store used by millions of developers as a database, cache, streaming engine, and message broker.

+ **Redis的特性**

  + Redis是用C语言开发的一个开源的高性能基于内存运行的键值对NoSQL数据库
  + Redis 通常被称为数据结构服务器，因为值（value）可以是字符串(String)、哈希(Hash)、列表(list)、集合(sets)和有序集合(sorted sets)等类型

  + Redis运行在内存中但是可以持久化到磁盘，所以在对不同数据集进行高速读写时需要权衡内存，因为数据量不能大于硬件内存。在内存数据库方面的另一个优点是，相比在磁盘上相同的复杂的数据结构，在内存中操作起来非常简单，这样Redis可以做很多内部复杂性很强的事情。同时，在磁盘格式方面他们是紧凑的以追加的方式产生的，因为他们并不需要进行随机访问。
  + Redis有着更为复杂的数据结构并且提供对他们的原子性操作，这是一个不同于其他数据库的进化路径。Redis的数据类型都是基于基本数据结构的同时对程序员透明，无需进行额外的抽象
  + Redis支持数据的备份，即master-slave模式的数据备份

+ **Redis可以用在如下场景，其中1，2，5用得较多**

  + 缓存
    热点数据（经常会被查询，但是不经常被修改或者删除的数据），首选是使用redis缓存。

  + 计数器
    单线程避免并发问题，高性能，如减库存。

  + 队列
    相当于消息系统，ActiveMQ，RocketMQ等工具类似，但是个人觉得简单用一下还行，如果对于数据一致性要求高的话还是用RocketMQ等专业系统。

  + 位操作
    使用setbit、getbit、bitcount命令，如统计用户签到，去重登录次数统计，某用户是否在线状态等；

  + redis内构建一个足够长的数组，每个数组元素只能是0和1两个值，然后这个数组的下标index用来表示我们上面例子里面的用户id（必须是数字哈），那么很显然，这个几亿长的大数组就能通过下标和元素值（0和1）来构建一个记忆系统，上面我说的几个场景也就能够实现。用到的命令是：setbit、getbit、bitcount

  + 分布式锁与单线程
    验证前端的重复请求（可以自由扩展类似情况），可以通过redis进行过滤：每次请求将request Ip、参数、接口等hash作为key存储redis（幂等性请求），设置多长时间有效期，然后下次请求过来的时候先在redis中检索有没有这个key，进而验证是不是一定时间内过来的重复提交。秒杀系统，基于redis是单线程特征，防止出现数据库“爆破”

  + 最新列表
    redis的LPUSH命令构建List。

  + 排行榜

    谁得分高谁排名往上。命令：ZADD（有序集，sorted set）

# 二、Linux环境下安装与配置Redis

推荐使用Linux环境学习使用Redis，更接近真实开发

## Linux环境搭建

[Linux基础环境搭建移步此处](https://blog.csdn.net/Night__breeze/article/details/125437582)

## 在CentOS中安装Redis7

### 官网下载压缩包安装

1. 在[Redis官网](https://redis.io/download/) 下载 `Redis` 压缩包

2. 通过 `Xftp` 可以将 `Redis` 安装包上传到远程 `linux` 服务器

   注：主机额外安装的软件通常安装在 `/opt` 目录下

3. 解压redis安装包

   ```bash
   mv redis-6.2.6.tar.gz /opt #首先移动安装包至opt目录
   tar -zxvf redis-6.2.6.tar.gz #解压安装包，解压后文件在当前目录
   ```

4. 由于redis是由C语言编写的，它的运行需要C环境，因此我们需要先安装gcc。

   通过 `gcc -v` 检查是否有GCC环境，如果没有，安装命令如下	

   ```
   yum install gcc-c++ 
   ```

5. 对解压后的文件进行编译与安装

   ```bash
   #进入Redis工作目录
   cd redis-6.2.6 
   #对解压后的文件进行编译
   make 
   #安装
   make install 
   ```

   **注：Redis的默认安装路径 `/usr/local/bin`，所以安装成功后，我们可以去这个目录看看，如下所示**

   ```bash
   [root@VM-16-12-centos bin]# ls
   busybox-x86_64  redis-benchmark  redis-check-rdb  redisConfig     redis-server
   dump.rdb        redis-check-aof  redis-cli        redis-sentinel
   ```

6. 查看`redis-cli`版本，若显示版本号，证明安装成功

   ```ruby
   redis-cli --version
   ```

### yum方式安装

Centos7 直接yum 安装的redis 不是最新版本

```bash
yum install redis
```


如果要安装最新的redis，需要安装Remi的软件源，官网地址：http://rpms.famillecollet.com/

```
yum install -y http://rpms.famillecollet.com/enterprise/remi-release-7.rpm
```


然后可以使用下面的命令安装最新版本的redis：

```
yum --enablerepo=remi install redis -y
```


安装完毕后，即可使用下面的命令启动redis服务

```
systemctl start redis
```

查看redis版本：

```ruby
redis-cli --version
```



## 启动与退出Redis、Redis-cli连接

> Reids启动

1. **Redis的启动需要配置文件，为了方便管理，可将Redis配置文件复制到Redis默认安装路径下`/usr/local/bin`，操作如下**

   ```bash
    mkdir lokiconfig #新建一个目录(名称可自定义)用来存储拷贝的redis.conf
    cp /opt/redis-6.2.6/redis.conf lokiconfig #拷贝
   ```

2. 在 `/usr/local/bin` 目录下

   ```
   redis-server [Redis配置文件的相对路径]
   ```

新开一个Xshell会话窗口，使用`redis-cli`测试是否启动成功

> Redis-cli的连接

在 `usr/local/bin` 目录下执行以下命令

```bash
redis-cli -p 6379 
ping #测试连通性
```

> 退出Redis服务

在Redis-cli中执行以下命令

```bash
shutdown #关闭进程
exit #退出
```

## 设置Redis开机自启动

> 修改redis.conf配置文件中的两项配置

- daemonize yes # 以守护进程方式启动
- supervised systemd # 可以跟systemd进程进行交互

> 新建redis.service

```
vim lib/systemd/system/redis.service
```

修改路径

+ ExecStart
+ ExecStop

```
[Unit]
Description=Redis In-Memory Data Store
After=network.target

[Service]
Type=forking
ExecStart=/usr/local/redis-6.0.10/src/redis-server /usr/local/redis-6.0.10/redis.conf
ExecStop=/usr/local/redis-6.0.10/src/redis-cli shutdown
ExecReload=/bin/kill -s HUP $MAINPID
Restart=always

[Install]
WantedBy=multi-user.target
```

> 开机自启动设置

```
设为开机启动
chkconfig redis on
关闭开机启动
chkconfig redis off
```



## 性能测试工具：redis-benchmark

`redis-benchmark`是一个官方自带的性能测试工具

redis 性能测试的基本命令如下：

```
redis-benchmark [option] [option value]
```

**注：该命令不是 redis 客户端的内部指令，在安装目录下执行这个命令**

附：redis 性能测试工具可选参数如下所示

| 序号 | 选项      | 描述                                       | 默认值    |
| :--- | :-------- | :----------------------------------------- | :-------- |
| 1    | **-h**    | 指定服务器主机名                           | 127.0.0.1 |
| 2    | **-p**    | 指定服务器端口                             | 6379      |
| 3    | **-s**    | 指定服务器 socket                          |           |
| 4    | **-c**    | 指定并发连接数                             | 50        |
| 5    | **-n**    | 指定请求数                                 | 10000     |
| 6    | **-d**    | 以字节的形式指定 SET/GET 值的数据大小      | 2         |
| 7    | **-k**    | 1=keep alive 0=reconnect                   | 1         |
| 8    | **-r**    | SET/GET/INCR 使用随机 key, SADD 使用随机值 |           |
| 9    | **-P**    | 通过管道传输 <numreq> 请求                 | 1         |
| 10   | **-q**    | 强制退出 redis。仅显示 query/sec 值        |           |
| 11   | **--csv** | 以 CSV 格式输出                            |           |

> 举个栗子

**测试：100个并发连接 100000请求**

```
redis-benchmark -h localhost -p 6379 -c 100 -n 100000
```

![image-20220105013514930](https://s2.loli.net/2022/01/05/ZMFYicTXejW1m3A.png)

可以看到Redis是非常快的



# 三、Redis入门

## 基础知识

> redis默认有16个数据库

默认使用 DB 0 ，可以使用select n切换到DB n，dbsize可以查看当前数据库的大小，与key数量相关。		

```bash
127.0.0.1:6379> config get databases # 命令行查看数据库数量databases

127.0.0.1:6379> select 8 # 切换数据库 DB 8
OK

127.0.0.1:6379[8]> dbsize # 查看数据库大小
(integer) 0

# 不同数据库之间 数据是不能互通的，并且dbsize 是根据库中key的个数。
127.0.0.1:6379> set name loki 
OK
127.0.0.1:6379> SELECT 8				
OK
127.0.0.1:6379[8]> get name # db8中并不能获取db0中的键值对。
(nil)
```

+ `keys *` ：查看当前数据库中所有的key。

+ `flushdb`：清空当前数据库中的键值对。

+ `flushall`：清空所有数据库的键值对

> Redis是单线程的，Redis是基于内存操作的

所以Redis的性能瓶颈不是CPU,而是机器内存和网络带宽。

那么为什么Redis的速度如此快呢，性能这么高呢？QPS达到10W+

> Redis为什么单线程还这么快？
>

误区1：高性能的服务器一定是多线程的？
误区2：多线程（CPU上下文会切换！）一定比单线程效率高！
核心：Redis是将所有的数据放在内存中的，所以说使用单线程去操作效率就是最高的，多线程（CPU上下文会切换：耗时的操作！），对于内存系统来说，如果没有上下文切换效率就是最高的，多次读写都是在一个CPU上的，在内存存储数据情况下，单线程就是最佳的方案。

## 关于Redis-key的一些操作

```bash
keys * #查看所有的key
set key value #set key
get key #get key
EXISTS key #判断key是否存在
move key 数据库序号 #移动当前库的key
del key #移除key
expire key 10 #设置key的存活时间为10s
type key #查看当前key的类型
append key "value" #对当前key的value追加字符串，如果key不存在，相当于set key
strlen key #测试字符串长度
```

# 四、Redis数据类型

> - redis自身是一个Map类型的存储方式，其中所有的数据都是采用key:value的形式存储
> - **我们讨论的数据类型指的是存储的数据的类型，也就是value部分的类型，key部分永远都是字符串**

## 五大基础数据类型

### String

+ 底层数据结构是一个动态字符串，类似于Java的ArrayList，采用预分配冗余空间的方式来减少内存的频繁分配

+ String类型是最常用最基础的数据类型
+ String类型是二进制安全的，意味着String类型可以保存任何数据，比如jpg图片，视频，或者任何序列化对象
+ 一个`Redis`中的`value`可以存储最多512M的内容

```
set name "loki"
```



> 下表列出了String常用的命令

| 常用命令及描述           |                                                              |
| :----------------------- | ------------------------------------------------------------ |
| `set [key] [value ]`     | 设置指定 key 的值                                            |
| `get [key]`              | 获取指定 key 的值。                                          |
| `getset [key] [value]`   | 将给定 key 的值设为 value ，并返回 key 的旧值(old value)     |
| `mset [k1 v1 k2 v2]`     | 同时设置一个或多个 key-value 对。                            |
| `mget [k1 v1 k2 v2]`     | 获取所有(一个或多个)给定 key 的值。                          |
| `strlen [key]`           | 返回 key 所储存的字符串值的长度                              |
| `incr [key]`             | 将 key 中储存的数字值增一 （原子操作，不会被进程调度打断）   |
| `decr [key]`             | 将 key 中储存的数字值减一                                    |
| `incrby [key increment]` | 将 key 所储存的值加上给定的增量值（increment）               |
| `decrby [key decrement]` | key 所储存的值减去给定的减量值（decrement）                  |
| `append [key value]`     | 如果 key 已经存在并且是一个字符串， APPEND 命令将指定的 value 追加到该 key 原来值（value）的末尾 |

### List：单键多值

+ 底层是一个双向链表，对两端的操作效率很高，但是对中间节点的操作效率较差

+ Redis列表是简单的字符串列表，按照插入顺序排序。你可以添加一个元素到列表的头部（左边）或者尾部（右边）
+ 一个列表最多可以包含 2^32 - 1 个元素 (4294967295, 每个列表超过40亿个元素)

| 命令及描述                                                   |                                                              |
| :----------------------------------------------------------- | ------------------------------------------------------------ |
| [BLPOP key1 key2  timeout](https://www.runoob.com/redis/lists-blpop.html) | 移出并获取列表的第一个元素， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止 |
| [BRPOP key1 key2  timeout](https://www.runoob.com/redis/lists-brpop.html) | 移出并获取列表的最后一个元素， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止 |
| [BRPOPLPUSH source destination timeout](https://www.runoob.com/redis/lists-brpoplpush.html) | 从列表中弹出一个值，将弹出的元素插入到另外一个列表中并返回它； 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止 |
| [lindex key index](https://www.runoob.com/redis/lists-lindex.html) | 通过索引获取列表中的元素                                     |
| [linsert key before\|after pivot value](https://www.runoob.com/redis/lists-linsert.html) | 在列表的元素前或者后插入元素                                 |
| [llen key](https://www.runoob.com/redis/lists-llen.html)     | 获取列表长度                                                 |
| [lpop key](https://www.runoob.com/redis/lists-lpop.html)     | 移出并获取列表的第一个元素                                   |
| [rpop key](https://www.runoob.com/redis/lists-rpop.html)     | 移除列表的最后一个元素，返回值为移除的元素。                 |
| [lpush key value1 value2](https://www.runoob.com/redis/lists-lpush.html) | 将一个或多个值插入到列表头部                                 |
| [LPUSHX key value](https://www.runoob.com/redis/lists-lpushx.html) | 将一个值插入到已存在的列表头部                               |
| [rpush key value1 value2](https://www.runoob.com/redis/lists-rpush.html) | 在列表尾部添加一个或多个值                                   |
| [lrange key start stop](https://www.runoob.com/redis/lists-lrange.html) | 获取列表指定范围内的元素                                     |
| [LREM key count value](https://www.runoob.com/redis/lists-lrem.html) | 移除列表元素                                                 |
| [LSET key index value](https://www.runoob.com/redis/lists-lset.html) | 通过索引设置列表元素的值                                     |
| [LTRIM key start stop](https://www.runoob.com/redis/lists-ltrim.html) | 对一个列表进行修剪(trim)，就是说，让列表只保留指定区间内的元素，不在指定区间之内的元素都将被删除。 |
| [RPOPLPUSH source destination](https://www.runoob.com/redis/lists-rpoplpush.html) | 移除列表的最后一个元素，并将该元素添加到另一个列表并返回     |
| [RPUSHX key value](https://www.runoob.com/redis/lists-rpushx.html) | 为已存在的列表添加值                                         |

### Set

+ Set 是 String 类型的无序集合。集合成员是唯一的，这就意味着集合中不能出现重复的数据

+ Redis 中集合是通过哈希表实现的，所以添加，删除，查找的复杂度都是 O(1)
+  集合中最大的成员数为 2^32 - 1 (4294967295, 每个集合可存储40多亿个成员)

| 常用命令及描述                                               |
| :----------------------------------------------------------- |
| [SADD key member1 member2](https://www.runoob.com/redis/sets-sadd.html) 向集合添加一个或多个元素 |
| [scard key](https://www.runoob.com/redis/sets-scard.html) 获取集合的成员数 |
| [SDIFF key1 [key2\]](https://www.runoob.com/redis/sets-sdiff.html) 返回第一个集合与其他集合之间的差异 |
| [SDIFFSTORE destination key1 [key2\]](https://www.runoob.com/redis/sets-sdiffstore.html) 返回给定所有集合的差集并存储在 destination 中 |
| [SINTER key1 [key2\]](https://www.runoob.com/redis/sets-sinter.html) 返回给定所有集合的交集 |
| [SINTERSTORE destination key1 [key2\]](https://www.runoob.com/redis/sets-sinterstore.html) 返回给定所有集合的交集并存储在 destination 中 |
| [SISMEMBER key member](https://www.runoob.com/redis/sets-sismember.html) 判断 member 元素是否是集合 key 的成员 |
| [SMEMBERS key](https://www.runoob.com/redis/sets-smembers.html) 返回集合中的所有成员 |
| [SMOVE source destination member](https://www.runoob.com/redis/sets-smove.html) 将 member 元素从 source 集合移动到 destination 集合 |
| [SPOP key](https://www.runoob.com/redis/sets-spop.html) 移除并返回集合中的一个随机元素 |
| [SRANDMEMBER key [count\]](https://www.runoob.com/redis/sets-srandmember.html) 返回集合中一个或多个随机数 |
| [SREM key member1 [member2\]](https://www.runoob.com/redis/sets-srem.html) 移除集合中一个或多个成员 |
| [SUNION key1 [key2\]](https://www.runoob.com/redis/sets-sunion.html) 返回所有给定集合的并集 |
| [SUNIONSTORE destination key1 [key2\]](https://www.runoob.com/redis/sets-sunionstore.html) 所有给定集合的并集存储在 destination 集合中 |
| [SSCAN key cursor [MATCH pattern\] [COUNT count]](https://www.runoob.com/redis/sets-sscan.html) 迭代集合中的元素 |

### Hash

+ Redis hash 是一个 string 类型的 field（字段） 和 value（值） 的映射表，hash 特别适合用于存储对象
+ Redis 中每个 hash 可以存储 2^32 - 1 键值对

| 常用命令及描述                                               |
| ------------------------------------------------------------ |
| [HDEL key field1 field2](https://www.runoob.com/redis/hashes-hdel.html) 删除一个或多个哈希表字段 |
| [HEXISTS key field](https://www.runoob.com/redis/hashes-hexists.html) 查看哈希表 key 中，指定的字段是否存在。 |
| [HGET key field](https://www.runoob.com/redis/hashes-hget.html) 获取存储在哈希表中指定字段的值。 |
| [HGETALL key](https://www.runoob.com/redis/hashes-hgetall.html) 获取在哈希表中指定 key 的所有字段和值 |
| [HINCRBY key field increment](https://www.runoob.com/redis/hashes-hincrby.html) 为哈希表 key 中的指定字段的整数值加上增量 increment 。 |
| [HINCRBYFLOAT key field increment](https://www.runoob.com/redis/hashes-hincrbyfloat.html) 为哈希表 key 中的指定字段的浮点数值加上增量 increment 。 |
| [HKEYS key](https://www.runoob.com/redis/hashes-hkeys.html) 获取所有哈希表中的字段 |
| [HLEN key](https://www.runoob.com/redis/hashes-hlen.html) 获取哈希表中字段的数量 |
| [HMGET key field1 field2](https://www.runoob.com/redis/hashes-hmget.html) 获取所有给定字段的值 |
| [HMSET key field1 value1 field2 value2 ](https://www.runoob.com/redis/hashes-hmset.html) 同时将多个 field-value (域-值)对设置到哈希表 key 中。 |
| [HSET key field value](https://www.runoob.com/redis/hashes-hset.html) 将哈希表 key 中的字段 field 的值设为 value 。 |
| [HSETNX key field value](https://www.runoob.com/redis/hashes-hsetnx.html) 只有在字段 field 不存在时，设置哈希表字段的值。 |
| [HVALS key](https://www.runoob.com/redis/hashes-hvals.html) 获取哈希表中所有值。 |
| [HSCAN key cursor [MATCH pattern\] [COUNT count]](https://www.runoob.com/redis/hashes-hscan.html) 迭代哈希表中的键值对。 |

### Zset：有序集合

+ Redis 有序集合和集合一样也是 string 类型元素的集合,且不允许重复的成员

| 常用命令及描述                                               |
| :----------------------------------------------------------- |
| [ZADD key score1 member1 score2 member2](https://www.runoob.com/redis/sorted-sets-zadd.html) 向有序集合添加一个或多个成员，或者更新已存在成员的分数 |
| [ZCARD key](https://www.runoob.com/redis/sorted-sets-zcard.html) 获取有序集合的成员数 |
| [ZCOUNT key min max](https://www.runoob.com/redis/sorted-sets-zcount.html) 计算在有序集合中指定区间分数的成员数 |
| [ZINCRBY key increment member](https://www.runoob.com/redis/sorted-sets-zincrby.html) 有序集合中对指定成员的分数加上增量 increment |
| [ZINTERSTORE destination numkeys key key ...](https://www.runoob.com/redis/sorted-sets-zinterstore.html) 计算给定的一个或多个有序集的交集并将结果集存储在新的有序集合 destination 中 |
| [ZLEXCOUNT key min max](https://www.runoob.com/redis/sorted-sets-zlexcount.html) 在有序集合中计算指定字典区间内成员数量 |
| [ZRANGE key start stop WITHSCORES](https://www.runoob.com/redis/sorted-sets-zrange.html) 通过索引区间返回有序集合指定区间内的成员 |
| [ZRANGEBYLEX key min max LIMIT offset count](https://www.runoob.com/redis/sorted-sets-zrangebylex.html) 通过字典区间返回有序集合的成员 |
| [ZRANGEBYSCORE key min max [WITHSCORES\] [LIMIT]](https://www.runoob.com/redis/sorted-sets-zrangebyscore.html) 通过分数返回有序集合指定区间内的成员 |
| [ZRANK key member](https://www.runoob.com/redis/sorted-sets-zrank.html) 返回有序集合中指定成员的索引 |
| [ZREM key member member ...](https://www.runoob.com/redis/sorted-sets-zrem.html) 移除有序集合中的一个或多个成员 |
| [ZREMRANGEBYLEX key min max](https://www.runoob.com/redis/sorted-sets-zremrangebylex.html) 移除有序集合中给定的字典区间的所有成员 |
| [ZREMRANGEBYRANK key start stop](https://www.runoob.com/redis/sorted-sets-zremrangebyrank.html) 移除有序集合中给定的排名区间的所有成员 |
| [ZREMRANGEBYSCORE key min max](https://www.runoob.com/redis/sorted-sets-zremrangebyscore.html) 移除有序集合中给定的分数区间的所有成员 |
| [ZREVRANGE key start stop WITHSCORES](https://www.runoob.com/redis/sorted-sets-zrevrange.html) 返回有序集中指定区间内的成员，通过索引，分数从高到低 |
| [ZREVRANGEBYSCORE key max min WITHSCORES](https://www.runoob.com/redis/sorted-sets-zrevrangebyscore.html) 返回有序集中指定分数区间内的成员，分数从高到低排序 |
| [ZREVRANK key member](https://www.runoob.com/redis/sorted-sets-zrevrank.html) 返回有序集合中指定成员的排名，有序集成员按分数值递减(从大到小)排序 |
| [ZSCORE key member](https://www.runoob.com/redis/sorted-sets-zscore.html) 返回有序集中，成员的分数值 |
| [ZUNIONSTORE destination numkeys key key ...](https://www.runoob.com/redis/sorted-sets-zunionstore.html) 计算给定的一个或多个有序集的并集，并存储在新的 key 中 |
| [ZSCAN key cursor [MATCH pattern\] [COUNT count]](https://www.runoob.com/redis/sorted-sets-zscan.html) 迭代有序集合中的元素（包括元素成员和元素分值） |

## 三种特殊数据类型

### geospatial：地理位置

这个功能可以将用户给定的地理位置信息储存起来， 并对这些信息进行操作

**GEO 的数据结构总共有六个命令**

- geoadd
- geopos
- geodist
- georadius
- georadiusbymember
- geohash

**增加一个定位**

```
geoadd china:city 116.40 39.90 beijing
geoadd china:city 106.50 29.53 chongqing
```


**获取当前定位**

```
deopos china:city beijing
```

**两个定位之间的距离**

单位：

+ m标识单位为米
+ km标识单位为千米
+ mi标识单位为英里
+ ft标识单位为英尺

```
geodist china:city beijing chongqing km
```

**查询指定半径中的数据**

```
georadius china:city 100 30 1000 km   #以经度100，纬度30 半径1000km的内的城市
georadius china:city 100 30 1000 km withdist  #显示到中心位置的距离
georadius china:city 100 30 1000 km withcoord  #显示符合条件的定位
georadius china:city 100 30 1000 km withcoord count 1  #输出指定个数符合条件的
```

### Hyperloglog：基数统计

+ Hyperloglog基数统计的算法。不过会有些许误差，如果允许容错，使用Hyperloglog，不允许容错的话使用set

+ 基数=不重复元素的个数

**网页的UV（一个人访问了一个网站多次，但是还是算作一个人)** 

示例：

```bash
127.0.0.1:6379> pfadd key1 a b c d e f g  #创建一组元素
(integer) 1
127.0.0.1:6379> pfcount key1  #统计key1中基数的数量
(integer) 7
127.0.0.1:6379> pfadd key2 f g h i z k b c #创建另一组元素  
(integer) 1
127.0.0.1:6379> pfcount key1 #统计key1中基数的数量
(integer) 7
127.0.0.1:6379> pfmerge key3 key1 key2  #合并两组key1和key2
OK
127.0.0.1:6379> pfcount key3 
(integer) 11 #key1+key2中基数的数量 a b c d e f g h i z k
```

### Bitmap

Bitmap就是通过一个bit位来表示某个元素对应的值或者状态。Bitmaps位图，只有0和1两个状态。

位存储。可以用来统计用户信息，登陆，未登录；打卡。

**用bitmap来记录周一到周日的打卡**

```
127.0.0.1:6379> setbit sign 0 1
(integer) 0
127.0.0.1:6379> setbit sign 1 1
(integer) 0
127.0.0.1:6379> setbit sign 2 1
(integer) 0
127.0.0.1:6379> setbit sign 3 1
(integer) 0
127.0.0.1:6379> setbit sign 4 0
(integer) 0
```

**查看某一天是否打卡**

```
127.0.0.1:6379> getbit sign 3
(integer) 1
```

**查看一周打卡的天数**

```
127.0.0.1:6379> bitcount sign
(integer) 4
```

# 五、Redis持久化操作

> Redis是一个内存数据库，所有的数据将保存在内存中，这与传统的MySQL、Oracle、SqlServer等关系型数据库直接把数据保存到硬盘相比，Redis的读写效率非常高。但是保存在内存中也有一个很大的缺陷，一旦断电或者宕机，内存数据库中的内容将会全部丢失。为了弥补这一缺陷，Redis提供了把内存数据持久化到硬盘文件，以及通过备份文件来恢复数据的功能，即Redis持久化机制。

Redis官方支持两种方式的持久化

+ 快照（Snapshot)
+ AOF(Append Only File) 只追加日志文件

## 持久化之RDB操作

> RDB快照用官方的话来说：RDB持久化方案是按照指定时间间隔对你的数据集生成的时间点快照（point-to-time snapshot）。它以紧缩的二进制文件（xxx.rdb）保存Redis数据库某一时刻所有数据对象的内存快照，可用于Redis的数据备份、转移与恢复。到目前为止，仍是官方的默认支持方案

注：快照持久化是默认开启的，这就是为什么有时候断电重启后数据不丢失的原因

> 快照生成的方式

**一、服务器配置自动触发**

如果用户在`redis.conf`中设置了save配置选项, redis会在save选项条件满足之后自动触发一次BCSAVE命令，如果设置多个save配置选项,当任意一个save配置选项条件满足, redis也会触发一次BGSAVE命令

以下为Redis.conf源码节选

```pascal
################### SNAPSHOTTING ##########################
# Save the DB to disk.
# save <seconds> <changes>
# following example
# Unless specified otherwise, by default Redis will save the DB:
#   * After 3600 seconds (an hour) if at least 1 key changed
#   * After 300 seconds (5 minutes) if at least 100 keys changed
#   * After 60 seconds if at least 10000 keys changed
# save 360 1
# save 300 100
# save 60 10000
```

**二、客户端命令** 

+ `BGSAVE`指令--推荐命令

  客户端可以使用BGSAVE命令来创建一个快照,当接收到客户端的BCSAVE命令时, redis会调用`fork`来创建一个子进程，然后子进程负责将快照写入磁盘中,而父进程则继续处理命令请求

+ `SAVE`指令

  使用SAVE命令来创建一个快照,接收到SAVE命令的redis服务器在快照创建完毕之前将不再响应任何其他的命令

很显然，`SAVE`指令不可取，持久化备份会导致短时间内Redis服务不可用，这对于高HA的系统来讲是无法容忍的。所以，`BGSAVE`是RDB持久化的主要实践方式。由于fork子进程后，父进程数据一直在变化，子进程并不与父进程同步，RDB持久化必然无法保证实时性；RDB持久化完成后发生断电或宕机，会导致部分数据丢失；备份频率决定了丢失数据量的大小，提高备份频率，意味着fork过程消耗较多的CPU资源，也会导致较大的磁盘I/O



**三、如果执行服务器关机`shutdown`命令时，会触发一次`SAVE`命令**



![image-20220327133059624](https://s2.loli.net/2022/03/27/vDwRaYro2dApP5N.png)



**注意：生成目录和文件名，都是可以在`redis.conf`中修改**



> 上一节我们知道RDB是一种时间点（point-to-time）快照，适合数据备份及灾难恢复，由于工作原理的“先天性缺陷”无法保证实时性持久化，这对于缓存丢失零容忍的系统来说是个硬伤，于是就有了AOF

## 持久化之AOF操作

AOF持久化默认是关闭的，修改redis.conf以下信息并重启，即可开启AOF持久化功能、

```
# no-关闭，yes-开启，默认no
appendonly yes
#修改文件名称
appendfilename appendonly.aof
```

> AOF日志追加频率的选择与修改

![image-20220327145820191](https://s2.loli.net/2022/03/27/flNFdKLyHTbEJSG.png)

+ alway（谨慎使用）
  + 说明:每个redis写命令都要同步写入硬盘，严重降低redis速度
  + 解释:如果用户使用了always选项,那么每个redis写命令都会被写入硬盘,从而将发生系统崩溃时出现的数据丢失减到最少;遗憾的是,因为这种同步策略需要对硬盘进行大量的写入操作，所以redis处理命令的速度会受到硬盘性能的限制;
  + 注意:转盘式硬盘在这种频率下200左右个命令/s ﹔固态硬盘(SSD)几百万个命令/s;
  + 警告︰使用SSD用户请谨慎使用always选项,这种模式不断写入少量数据的做法有可能会引发严重的写入放大问题,导致将固态硬盘的寿命从原来的几年降低为几个月。
+ everysec（推荐）
  + 说明:每秒执行一次同步显式的将多个写命令同步到磁盘
  + 解释:为了兼顾数据安全和写入性能，用户可以考虑使用everysec选项,让redis每秒一次的频率对AOF文件进行同步; redis每秒同步一次AOF文件时性能和不使用任何持久化特性时的性能相差无几,，而通过每秒同步一次AOF文件, redis可以保证，即使系统崩溃,用户最多丢失1秒之内产生的数据
+ no（不推荐）
  + 说明:由操作系统决定何时同步
  + 解释︰最后使用no选项,将完全有操作系统决定什么时候同步AOF日志文件,这个选项不会对redis性能带来影响但是系统崩溃时,会丢失不定数量的数据，另外如果用户硬盘处理写入操作不够快的话,当缓冲区被等待写入硬盘数据填满时, redis会处于阻塞状态,并导致redis的处理命令请求的速度变慢

> AOF文件的重写

如前面提到的，Redis长时间运行，命令不断写入AOF，文件会越来越大，不加控制可能影响宿主机的安全。

为了解决AOF文件体积问题，Redis引入了AOF文件重写功能，它会根据Redis内数据对象的最新状态生成新的AOF文件，新旧文件对应的数据状态一致，但是新文件会具有较小的体积。重写既减少了AOF文件对磁盘空间的占用，又可以提高Redis重启时数据恢复的速度。还是下面这个例子，旧文件中的6条命令等同于新文件中的1条命令，压缩效果显而易见。

```
set number 0
incr number
incr number
incr number
incr number
incr number
```

```
#等同于
set number 5
```

> 触发重写的方式

1. 客户端触发重写

   ```
   BGREWRITEAOF
   ```

   不会阻塞Redis服务

2. redis.conf配置自动触发

   配置redis.conf中的`auto-aof-rewrite-percentage`选项

   如下图所示

   ```
   auto-aof-rewrite-percentage 100
   auto-aof-rewrite-min-size 64mb
   ```

启用的AOF持久化时,当AOF文件体积大于64M,并且AOF文件的体积比上一次重写之后体积大了至少一倍(100%)时,会自动触发,如果重写过于频繁，用户可以考虑将`auto-aof-rewrite-percentage`设置为更大

![image-20220327151414991](https://s2.loli.net/2022/03/27/WSNjqg7fOsioKcx.png)

> AOF重写的原理

注意∶重写AOF文件的操作，并没有读取旧的AOF文件，而是将整个内存中的数据库内容用命令的方式重写了一个新AOF文件,替换原有的文件这点和快照有点类似

> AOF的缺点

- 对于相同的数据集合，AOF文件通常会比RDB文件大；
- 在特定的fsync策略下，AOF会比RDB略慢。一般来讲，fsync_every_second的性能仍然很高，fsync_no的性能与RDB相当。但是在巨大的写压力下，RDB更能提供最大的低延时保障。
- 在AOF上，Redis曾经遇到一些几乎不可能在RDB上遇到的罕见bug。一些特殊的指令（如BRPOPLPUSH）导致重新加载的数据与持久化之前不一致，Redis官方曾经在相同的条件下进行测试，但是无法复现问题。

> 如果同时使用AOF和RDB进行持久化，Redis会优先载入AOF文件，因为这是更安全的方式

## 使用建议

对RDB和AOF两种持久化方式的工作原理、执行流程及优缺点了解后，我们来思考下，实际场景中应该怎么权衡利弊，合理的使用两种持久化方式。如果仅仅是使用Redis作为缓存工具，所有数据可以根据持久化数据库进行重建，则可关闭持久化功能，做好预热、缓存穿透、击穿、雪崩之类的防护工作即可。

一般情况下，Redis会承担更多的工作，如分布式锁、排行榜、注册中心等，持久化功能在灾难恢复、数据迁移方面将发挥较大的作用。建议遵循几个原则：

- 不要把Redis作为数据库，所有数据尽可能可由应用服务自动重建。
- 使用4.0以上版本Redis，使用AOF+RDB混合持久化功能。
- 合理规划Redis最大占用内存，防止AOF重写或save过程中资源不足。
- 避免单机部署多实例
- 生产环境多为集群化部署，可在slave开启持久化能力，让master更好的对外提供写服务。
- 备份文件应自动上传至异地机房或云存储，做好灾难备份

# 七、关于Redis的整合

## Jedis：Java操作Redis

Jedis集成了Redis的相关命令操作，它是Java语言操作Redis数据库的桥梁。Jedis客户端封装了Redis数据库的大量命令，因此具有许多Redis操作[API]

> 准备工作

**如果你的Redis是远程连接的话，会出现连接超时或者是拒绝访问的问题，在这边需要进行以下修改**



**1、打开redis.conf配置文件**

+ daemonize yes
+ protected-mode no
+ 注释 bind 127.0.0.1
+ requirepass [设置密码]

**2、云服务器要开放端口**

> 连接步骤

**使用Maven项目导入jedis依赖**

```xml
 		<dependency>
            <groupId>redis.clients</groupId>
            <artifactId>jedis</artifactId>
            <version>4.0.1</version>
        </dependency>
```

> API测试---无非就是Redis的常用操作，自己在ide里多测试测试就好

```java
public class TestPing {
    public static void main(String[] args) {
        // new一个Jedis对象
        //构造方法不传入参数，默认连接的是你本机上面的6379端口
        //参数为主机IP和端口号，有可能是你云服务器的ip地址
        Jedis jedis = new Jedis("192.168.1.107", 6379);
        // Jedis中的API就是之前学习的命令
        System.out.println(jedis.ping());
        //选择数据库，默认是0号库
        jedis.select(0)；
        //设置Redis数据库的密码
        System.out.println(jedis.auth("123456"));
        //获取客户端信息
        System.out.println(jedis.getClient());
        //清空Redis数据库，相当于执行FLUSHALL命令
        System.out.println(jedis.flushAll());
        //查看Redis信息，相当于执行INFO命令
        System.out.println(jedis.info());
        //获取数据库中key的数量，相当于指定DBSIZE命令
        System.out.println(jedis.dbSize());
        //获取数据库名字
        System.out.println(jedis.getDB());
        //返回当前Redis服务器的时间，相当于执行TIME命令
        System.out.println(jedis.time());
        jedis.close();
    }
}
```

## SpringBoot整合Redis

操作无非就是对Redis的基本操作而已，主要是测试Springboot是否连通远程Redis，进行一个测试



新建一个Springboot工程，导入Maven依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

application.yaml

```yaml
spring:
  redis:
    host: 主机ip地址
    port: 端口号
    database: 使用的数据库
    password: 密码
```

> **注：从SpringBoot2.x之后，底层使用的Jedis被lettuce替代**

做完上述配置以后，`Spring Boot Data Redis` 提供了两个对象来操作Redis，并且已经注入Spring容器当中供我们使用

```
RedisTemplate
StringRedisTemplate
```

其中StringRedisTemplate是RedisTemplate的子类，两个方法基本一致，不同之处主要体现在操作的数据类型不同，RedisTemplate中的两个泛型都是object，意味着存储的key和value都可以是一个对象，而stringRedisTemplate的两个泛型都是sString，意味着stringRedisTemplate的key和value都只能是字符串

**注意**

+ 使用RedisTemplate默认是将对象序列化到Redis中,所以放入的对象必须实现对象序列化接口

+ 这些方法无非就是Redis数据类型的基本操作，对于它的Api就不一一举例了，自己在IDE里新建一个项目，试一试这两个对象的不同Api，掌握它的基本操作即可

+ 注意Redis远程连接时需要做的配置（上文中有提到），并且云服务器去打开6379这个端口

> 测试一下这两个对象

```java
import com.loki.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import java.util.Set;

@SpringBootTest
public class TestStringRedisTemplate {
    //这个对象，无非key和value都是String类型
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    //对于Java而言，我们需要将对象放入Redis，因此有了这个类
    @Autowired
    RedisTemplate redisTemplate;

    @Test
    void testKey(){
        stringRedisTemplate.opsForValue().set("name","loki");
        String name = stringRedisTemplate.opsForValue().get("name");
        //删除一个key
        stringRedisTemplate.delete("name");
        //判断key是否存在
        Boolean hasName = stringRedisTemplate.hasKey("name");
        //判断key对应值的类型
        DataType type = stringRedisTemplate.type("name");
        //获取所有key
        Set<String> keys = stringRedisTemplate.keys("*");
        //创建一个列表放入多个元素
        stringRedisTemplate.opsForList().leftPush("name","小王","小陈");
        //创建一个Hash类型，放入一个key value
        stringRedisTemplate.opsForHash().put("maps","name","张三");
    }
    @Test
    void testRedisTemplate(){
        //User必须implements Serializable，否则无法存入，会抛异常
        User user = new User("loki", "小loki");
        redisTemplate.opsForValue().set("user",user);
        //不多说了,自己尝试    redisTemplate.XXXX
    }
}
```

附：怎样序列化对象

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    private String username;
    private String nickname;
}
```



当执行testRedisTemplate方法后，可以看到数据库中已经存储了这个序列化的对象。

> redisTemplate对象中 key和 value 的默认序列化方式都是`JdkSerializationRedisSerializer`

但是我并不希望key被这样序列化，key应该是字符串类型的"user"才对，怎么办呢？

![image-20220327182144911](D:\桌面\P_picture_cahe\15T2BIh7jtX9Src.png)



```java
	@Test
    void testRedisTemplate(){
        //修改key的序列化方案
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        User user = new User("loki", "小loki");
        redisTemplate.opsForValue().set("user",user);
    }
```

这样我们就可以通过get key来获取到序列化的对象

![image-20220327182615833](D:\桌面\P_picture_cahe\u6Vv8wUkLAnhQRM.png)

同样对于HashKey来说，我们也可以修改Hashkey的序列化方式来让它按照我们想要的方式序列化















# 六、Redis.conf：配置文件各项配置详细解释

> **daemonize：是否守护线程方式运行Redis，默认是NO。用来指定redis是否要用守护线程的方式启动**
>
> **daemonize 设置yes或者no区别**
>
> - `daemonize  yes`:redis采用的是单进程多线程的模式。当redis.conf中选项daemonize设置成yes时，代表开启守护进程模式。在该模式下，redis会在后台运行，并将进程pid号写入至redis.conf选项pidfile设置的文件中，此时redis将一直运行，除非手动kill该进程。
> - `daemonize  no`: 当daemonize选项设置成no时，当前界面将进入redis的命令行界面，exit强制退出或者关闭连接工具(putty,xshell等)都会导致redis进程退出。



**总结**

```bash
daemonize yes #是否以后台进程运行

pidfile /var/run/redis/redis-server.pid    #pid文件位置

port 6379#监听端口

bind 127.0.0.1   #绑定地址，如外网需要连接，设置0.0.0.0

timeout 300     #连接超时时间，单位秒

loglevel notice  #日志级别，分别有：

# debug ：适用于开发和测试

# verbose ：更详细信息

# notice ：适用于生产环境

# warning ：只记录警告或错误信息

logfile /var/log/redis/redis-server.log   #日志文件位置

syslog-enabled no    #是否将日志输出到系统日志

databases 16#设置数据库数量，默认数据库为0

############### 快照方式 ###############
save 900 1    #在900s（15m）之后，至少有1个key发生变化，则快照

save 300 10   #在300s（5m）之后，至少有10个key发生变化，则快照

save 60 10000  #在60s（1m）之后，至少有1000个key发生变化，则快照

rdbcompression yes   #dump时是否压缩数据

dir /var/lib/redis   #数据库（dump.rdb）文件存放目录

############### 主从复制 ###############
slaveof <masterip> <masterport>  #主从复制使用，用于本机redis作为slave去连接主redis

masterauth <master-password>   #当master设置密码认证，slave用此选项指定master认证密码

slave-serve-stale-data yes     #当slave与master之间的连接断开或slave正在与master进行数据同步时，如果有slave请求，当设置为yes时，slave仍然响应请求，此时可能有问题，如果设置no时，slave会返回"SYNC with master in progress"错误信息。但INFO和SLAVEOF命令除外。

############### 安全 ###############
requirepass foobared   #配置redis连接认证密码



############### 限制 ###############
maxclients 128#设置最大连接数，0为不限制

maxmemory <bytes>#内存清理策略，如果达到此值，将采取以下动作：

# volatile-lru ：默认策略，只对设置过期时间的key进行LRU算法删除

# allkeys-lru ：删除不经常使用的key

# volatile-random ：随机删除即将过期的key

# allkeys-random ：随机删除一个key

# volatile-ttl ：删除即将过期的key

# noeviction ：不过期，写操作返回报错

maxmemory-policy volatile-lru#如果达到maxmemory值，采用此策略

maxmemory-samples 3   #默认随机选择3个key，从中淘汰最不经常用的



############### 附加模式 ###############
appendonly no    #AOF持久化，是否记录更新操作日志，默认redis是异步（快照）把数据写入本地磁盘

appendfilename appendonly.aof  #指定更新日志文件名

# AOF持久化三种同步策略：

# appendfsync always   #每次有数据发生变化时都会写入appendonly.aof

# appendfsync everysec  #默认方式，每秒同步一次到appendonly.aof

# appendfsync no       #不同步，数据不会持久化

no-appendfsync-on-rewrite no   #当AOF日志文件即将增长到指定百分比时，redis通过调用BGREWRITEAOF是否自动重写AOF日志文件。



############### 虚拟内存 ###############
vm-enabled no      #是否启用虚拟内存机制，虚拟内存机将数据分页存放，把很少访问的页放到swap上，内存占用多，最好关闭虚拟内存

vm-swap-file /var/lib/redis/redis.swap   #虚拟内存文件位置

vm-max-memory 0    #redis使用的最大内存上限，保护redis不会因过多使用物理内存影响性能

vm-page-size 32    #每个页面的大小为32字节

vm-pages 134217728  #设置swap文件中页面数量

vm-max-threads 4    #访问swap文件的线程数



############### 高级配置 ###############
hash-max-zipmap-entries 512   #哈希表中元素（条目）总个数不超过设定数量时，采用线性紧凑格式存储来节省空间

hash-max-zipmap-value 64     #哈希表中每个value的长度不超过多少字节时，采用线性紧凑格式存储来节省空间

list-max-ziplist-entries 512  #list数据类型多少节点以下会采用去指针的紧凑存储格式

list-max-ziplist-value 64    #list数据类型节点值大小小于多少字节会采用紧凑存储格式

set-max-intset-entries 512   #set数据类型内部数据如果全部是数值型，且包含多少节点以下会采用紧凑格式存储

activerehashing yes        #是否激活重置哈希
```



# 七、Redis事务和锁机制









# 八、Redis主从复制







# 九、Redis集群







# 十、Redis分布式锁









---



Current time:Sun Mar 27 18:40:32 CST 2022

update time: