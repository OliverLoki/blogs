# Mybatis详细入门笔记03---如何打开Mybatis日志

日志可以将组合之后的完整sql打印出来，对解决bug来说非常重要。

## 如何开启

在 Mybatis核心配置文件 mybatis.xml中添加以下配置

```xml
<!--注意是在configuration标签中添加-->
<configuration>
    <!--开启Mybatis日志-->
    <settings>
        <setting name="logImpl" value="STDOUT_LOGGING"/>
    </settings>
<configuration/>
```

开启之后如果执行sql语句会在控制台打印相关信息

![image-20210529173107907](https://i.loli.net/2021/05/29/KaMbgh4HtiZp8J6.png)