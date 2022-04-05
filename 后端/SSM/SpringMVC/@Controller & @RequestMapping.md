# @Controller & @RequestMapping

> 
>

## 控制器Controller

Controller是一个接口，在`org.springframework.web.servlet.mvc`包下，接口中只有一个方法；

```java
public interface Controller {
   //处理请求且返回一个模型与视图对象
   ModelAndView handleRequest(HttpServletRequest var1, HttpServletResponse var2) throws Exception;
}
```

### 两种方式实现Controller

#### 配置文件实现

**测试demo**

1. 编写一个Controller类 

   > 注意：不要导错包,否则这个bug可能让你破防 :frowning_face:
   >
   > ![image-20210621161805127](https://i.loli.net/2021/06/21/Pgr2toIsSJD8CxT.png)

   ```java
   public class ControllerTest implements Controller {
       //该方法返回一个模型视图对象
      public ModelAndView handleRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
          ModelAndView mv = new ModelAndView();
          mv.addObject("msg","Test1Controller");
          mv.setViewName("test");//会拿这个拼接url
          return mv;
     }
   }
   ```
   
2. 编写完毕后，去Spring配置文件中注册请求的bean，name对应请求路径，class对应处理请求的类

   ```xml
   <!--配置到包名即可
   对应路径 ： localhost:8080/loki/test
   -->
       <bean name="/func" class="org.test.controller.ControllerTest"/>
   ```

3. 编写前端test.jsp，注意在WEB-INF/jsp目录下编写，对应我们的视图解析器

   ```jsp
   <%@ page contentType="text/html;charset=UTF-8" language="java" %>
   <html>
   <head>
      <title>Title</title>
   </head>
   <body>
   	${msg}
   </body>
   </html>
   ```

4. 配置Tomcat运行测试 url格式`http://localhost:8080/项目发布名/控制器名称`,得到结果

   ![image-20210621151925192](https://i.loli.net/2021/06/21/EQ5R1q7c8aYj9fw.png)

   

   

> 实际开发中都是使用注解的方式
>
> 因为这种方法一个控制器中只有一个方法，如果要多个方法则需要定义多个Controller类，定义的方式比较麻烦

#### 使用注解@Controller

> `applicationContext.xml`中需要开启扫描

> **有这个注解的类，其中所有的方法如果返回值是String，并且有具体页面可以跳转，就会被视图解析器解析**

- @Controller注解类型用于声明Spring类的实例是一个控制器

  + dao @Repository
  + service @Service
  + controller  @Controller

- Spring可以使用扫描机制来找到应用程序中所有基于注解的控制器类，为了保证Spring能找到你的控制器，需要在配置文件中声明组件扫描。

  ```xml
  <!-- 自动扫描指定的包，下面所有注解类交给IOC容器管理 -->
  <context:component-scan base-package="test.controller"/>
  ```

- 增加一个ControllerTest类，使用注解实现；

  ```java
  //@Controller注解的类会自动添加到Spring上下文中
  @Controller
  public class ControllerTest2{
     //映射访问路径 localhost:8080/loki/func
     @RequestMapping("/func")
     public String index(Model model){
         //Spring MVC会自动实例化一个Model对象用于向视图中传值
         model.addAttribute("msg", "ControllerTest2");
         //返回视图位置
         return "test";
    }
  }
  ```

  

**可以发现，我们的两个请求都可以指向一个视图，但是页面结果的结果是不一样的，从这里可以看出视图是被复用的，而控制器与视图之间是弱偶合关系。**

## RequestMapping

**@RequestMapping**

- @RequestMapping注解用于映射url到控制器类或一个特定的处理程序方法。

- 可用于类或方法上。用于类上，表示类中的所有响应请求的方法都是以该地址作为父路径。

  > 注解在方法上面

  ```java
  @Controller
  public class TestController {
     @RequestMapping("/func")
     public String test(){
         return "test";
    }
  }
  ```

  访问路径：http://localhost:8080 / 项目名 / func

  > 同时注解类与方法

  ```
  @Controller
  @RequestMapping("/admin")
  public class TestController {
     @RequestMapping("/func")
     public String test(){
         return "test";
    }
  }
  ```

  访问路径：http://localhost:8080 / 项目名/ admin /func  , 需要先指定类的路径再指定方法的路径；

> 习惯性将路径全部写在方法中

