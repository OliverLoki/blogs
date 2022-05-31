Thymeleaf学习笔记

> [Theymeleaf官方文档](https://www.thymeleaf.org/documentation.html)

## 简介

### 模板引擎

模板引擎的作用就是我们来写一个页面模板，比如有些值呢，是动态的，我们写一些表达式。而这些值，从哪来呢，就是我们在后台封装一些数据。然后把这个模板和这个数据交给我们模板引擎，模板引擎按照我们这个数据帮你把这表达式解析、填充到我们指定的位置，然后把这个数据最终生成一个我们想要的内容给我们写出去，这就是我们这个模板引擎，不管是jsp还是其他模板引擎，都是这个思想

### Thymeleaf简介

​	Thymeleaf是Springboot推荐的模板引擎技术（SpringBoot并不推荐使用jsp，但是支持一些模板引擎技术，如：Freemarker，Thymeleaf，Mustache）

### Thymeleaf特点

+ 动静结合：Thymeleaf 在有网络和无网络的环境下皆可运行，即它可以让美工在浏览器查看页面的静态效果，也可以让程序员在服务器查看带数据的动态页面效果。这是由于它支持 html 原型，然后在 html 标签里增加额外的属性来达到模板+数据的展示方式。浏览器解释 html 时会忽略未定义的标签属性，所以 thymeleaf 的模板可以静态地运行；当有数据返回到页面时，Thymeleaf 标签会动态地替换掉静态内容，使页面动态显示。
+ 开箱即用：它提供标准和spring标准两种方言，可以直接套用模板实现JSTL、 OGNL表达式效果，避免每天套模板、改jstl、改标签的困扰。同时开发人员也可以扩展和创建自定义的方言。
+ 多方言支持：Thymeleaf 提供spring标准方言和一个与 SpringMVC 完美集成的可选模块，可以快速的实现表单绑定、属性编辑器、国际化等功能。
  与SpringBoot完美整合，SpringBoot提供了Thymeleaf的默认配置，并且为Thymeleaf设置了视图解析器，我们可以像以前操作jsp一样来操作Thymeleaf。代码几乎没有任何区别，就是在模板语法上有区别。

Thymeleaf 官网：https://www.thymeleaf.org/

Thymeleaf 在Github 的主页：https://github.com/thymeleaf/thymeleaf

Spring官方文档：找到我们对应的版本

https://docs.spring.io/spring-boot/docs/2.2.5.RELEASE/reference/htmlsingle/#using-boot-starter 

## 如何使用Thymeleaf

**pom.xml**

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>
```

把html 的名称空间，改成：`xmlns:th="http://www.thymeleaf.org"` 会有语法提示

```html
<html lang="en" xmlns:th="http://www.thymeleaf.org">
```

## 默认配置

**Thymeleaf默认会开启页面缓存，提高页面并发能力。但会导致我们修改页面不会立即被展现，因此我们关闭缓存：**

```yaml
# 关闭Thymeleaf的缓存
spring.thymeleaf.cache=false
```

## Thymeleaf对公共界面的提取!

**对被提取的元素(组件)**

页面导航栏等

```html
<div th:fragment="属性值">
```

**对被插入的元素**

```html
<div th:replace="~{xxx.html的路径::属性值}">
```

**像组件中的元素传值**

比如判断当前在哪个页面让导航栏高亮

```html
<div th:replace="~{common/xxx.html::属性值(属性名2="属性值2")}">
```

**组件判断参数进行一些操作**

```html
<!--如果属性值a1等于属性值2，说明在属性二的界面，于是三元表达式给a的class属性赋值，这个a标签高亮-->
	<a th:class="${属性值a1=='属性值2'?'topnav':'topnav active'}" > 
    <a th:class="${属性值a2=='属性值3'?'leftnav':'leftnav active'}" >    
```

## 常用语法！

> **class属性通过判断条件赋值**

```html
<p th:class="${num==page.current?'page-item active':'page-item'}">
	判断当前页给p标签的class属性赋值
</p>
```

> **循环遍历数据**

```
<tr th:each="book:${page.records}">
    <!--实际记录数据的在page.records里面-->
    <td th:text="${book.bookID}"></td>
    <td th:text="${book.bookName}"></td>
    <td th:text="${book.bookCounts}"></td>
    <td th:text="${book.detail}"></td>
</tr>
```

> **thymeleaf @{}和${}嵌套使用**

```html
//错误的写法
<a th:href="@{/article/type/${typeId}}">上一页</a>
//正确的写法
<a th:href="@{/article/type/{typeId}(typeId=${typeId})}">上一页</a>
//正常带参数
<a th:href="@{/active(l='en_US')}">上一页</a>
//带参数时遇到${}该怎么写
<a th:href="@{{active}(active=${active},l='en_US')}">English</a>
```

> **Thymeleaf展示后端传到前端的html代码**

```html
<span th:utext="${html}"></span>
//其中“html”是我们model中存储html代码的名称
```

## 基础语法

### 运算符优先级

**Attribute Precedence**

![image-20210924144605218](https://i.loli.net/2021/10/11/hO7tyIRokui1QFL.png)

### 表达式

- 变量表达式: ${…}

- 选择变量表达式: *{…}

- 信息表达式: #{…}

- URL连接表达式: @{…}

  ```css
    <link rel="stylesheet" type="text/css" th:href="@{/css/login.css}" />
  ```

  

### 数据类型

+ 字符型: ‘one text’ , ‘Another one!’ **(注意是单引号)**
+ 数值型: 0 , 34 , 3.0 , 12.3 ,…
+ Boolean型: `true` , `false`
+ 空值: `null`
+ 文本字符串: one , sometext , main 

**字符串操作**

+ 字符串连接: `+`
+ 文字替换: The name is `${name}`

**数值型操作**

+ 运算符: `+` ,` -` ,` *` , `/` , `%`
+ 负号: `-`

**Boolean操作**

+ 运算符: `and` ,` or`
+ 非运算符: `! `, `not`

**比较相等算法**

+ 比较: `>` , `<` , `>=` , `<=` ( gt , lt , ge , le )

+ 相等算法: `==` , `!=` ( eq , ne )

**条件语句**

+ If-then: (if) ? (then)
+ If-then-else: (if) ? (then) : (else)
+ Default: (value) ?: (defaultvalue)