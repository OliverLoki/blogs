## 解决maven默认不允许src目录下xml等文件导出到target目录

**maven项目中有一个目录标准，其中src下的xml文件构建时不会被输出到target/classes目录下**，解决方法如下

在maven核心配置文件 --- pom.xml中配置以下内容

> XML文件不应该放在src/main/java中，而应该放在src/main/resources中。Maven将只编译src/main/java中的java源代码，默认情况下会忽略其中的所有其他内容。
>
> 如果希望将XML保存在src/main/java中，可以将其添加到POM的资源部分。我建议不要这样做，尽量将您想要的非Java文件保存在src/main/resources的工件中。

```xml
	<build>
        <resources>
            <resource>
                <directory>src/main/java</directory>
                <includes>
                    <include>**/*.properties</include>
                    <include>**/*.xml</include>
                </includes>
                <filtering>false</filtering>
            </resource>
        </resources>
    </build>
```

