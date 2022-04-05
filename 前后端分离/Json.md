# Json

## Json简介

+ Json: `JavaScript Object Notation`(JavaScript 对象表示法)
+ 存储和交换文本信息的语法，类似 XML
  + XML 需要使用 XML 解析器来解析
  + JSON 文本格式在语法上与创建 JavaScript 对象的代码相同。由于这种相似性，无需解析器，JavaScript 程序能够使用内建的 eval() 函数，用 JSON 数据来生成原生的 JavaScript 对象
  + JSON 读写速度更快
  + JSON 可以使用数组
  + 针对 AJAX 应用，JSON 比 XML 数据加载更快，而且更简单

## Json实例

+ 数据在名称/值对中
+ 数据由逗号分隔
+ 大括号 **{}** 保存对象
+ 中括号 **[]** 保存数组，数组可以包含多个对象

```json
[
    { key1 : value1-1 , key2:value1-2 }, 
    { key1 : value2-1 , key2:value2-2 }, 
    { key1 : value3-1 , key2:value3-2 }, 
    ...
    { keyN : valueN-1 , keyN:valueN-2 }, 
]
```



## Json和Js对象的互转

- [JSON.parse()](https://www.runoob.com/js/javascript-json-parse.html): 将一个 JSON 字符串转换为 JavaScript 对象。
- [JSON.stringify()](https://www.runoob.com/js/javascript-json-stringify.html): 于将 JavaScript 值转换为 JSON 字符串。

```js
<script type="text/javascript">
    var user = {
        name:"loki",
        age:20,
        sex:"男"
    }
    console.log(user);
    //js对象转json字符串
    var s = JSON.stringify(user);
    console.log(s);//{"name":"王喆","agge":3,"sex":"男"}
    //json字符串转js对象
    let parse = JSON.parse(s);
    console.log(parse);
</script>
```

## Controller返回一个Json对象

以Maven项目为例

### pom.xml中导入jackson依赖

```xml
<dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
            <version>2.13.0-rc2</version>
</dependency>
```

### 使用@Responsebody注解

`@RequestMapping`正常返回，走视图解析器，**数据交互对前后端分离和很不友好**，所以需要一种更有效的数据交互方式，返回一个json字符串可以解决这个问题。很多jar包可以实现这个功能，最有名的是 `jackson`,`Fastjson`等

+ Jackson注解支持，需要在pom.xml中导入依赖

```xml
<dependency>
	<groupId>com.fasterxml.jackson.core</groupId>
	<artifactId>jackson-databind</artifactId>
	<version>2.13.0-rc2</version>
</dependency>
```

+ Controller代码

```java
@Controller
public class Controller写json {
    @RequestMapping(value = "/json1")
    @ResponseBody
    public String json1() throws JsonProcessingException {
        //需要一个jackson的对象映射器，就是一个类，使用它可将直接将对象转换为json字符串
        ObjectMapper mapper = new ObjectMapper();
        //创建一个对象
        User user = new User("王喆", 1, "男");
        System.out.println(user);
        //将java对象转换为json字符串
        String s = mapper.writeValueAsString(user);
        return s; //由于使用了Responsebody注解，这里会将
    }
    //优化代码
    @RequestMapping(value = "/json2",produces = "application/json;charest=utf-8")
    @ResponseBody
    public String json2() throws JsonProcessingException {  
        User user = new User("王喆", 1, "男");
        return new ObjectMapper().writeValueAsString(user);
    }
}

```

​		