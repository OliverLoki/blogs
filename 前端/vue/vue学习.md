# 快速上手Vue2

## 特性

**vue是什么**

Vue是一套用于构建用户界面的**渐进式框架**。与其它大型框架不同的是，Vue 被设计为可以自底向上逐层应用。Vue 的核心库只关注视图层，不仅易于上手，还便于与第三方库或既有项目整合

**vue的优点**

+ 体积小

  压缩后33K

+ 更高的运行效率
  基于虚拟dom,一种可以预先通过JavaScript进行各种计算，把最终的DOM操作计算出来并优化的技术，由于这个DOM操作属于预处理操作，并没有真实的操作DOM，所以叫做虚拟DOM

+ 双向数据绑定
  即当数据发生变化的时候， 视图也就发生变化， 当视图发生变化的时候，数据也会跟着同步变化

+ 生态丰富、学习成本低
  市场上拥有大量成熟、稳定的基于vue.js的ui框架、常用组件!拿来即用实现快速开发!
  对初学者友好、入门容易、学习资料多;

## 使用 <script>标签快速引入vue

```
<!-- 对于制作原型或学习，你可以这样使用最新版本 -->
<script src="https://unpkg.com/vue@next"></script>
```

> 访问该网址可以看到vue的源码，可以jiang'ta



引入CDN后可以使用vue语法实现以下一个简单的小功能

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<!--view层，模板-->
<div id="app">
    {{message}}
</div>

<!--1.导入Vue.js-->
<script src="https://unpkg.com/vue@next"></script>
<script type="text/javascript">
    /*vue变量*/
    var vm = new Vue({
        /*与Html中的元素绑定*/
        el:"#app",
        /*Model：数据*/
        data:{
            message:"hello,vue!"
        }
    });
</script>
</body>
</html>
```

+ `el`:标记被挂载的元素

+ `data`：数据对象
  + Vue中用到的数据定义在data中
  + data中可以写复杂类型的数据
  + 渲染复杂类型数据时,遵守js的语法即可

Question1:Vue实例的作用范围是什么呢?

> Vue会管理el选项命中的元素及其内部的后代元素

Question2:是否可以使用其他的选择器?

> 可以使用其他的选择器,但是建议使用ID选择器

Question3:是否可以设置其他的dom元素呢?

> 可以使用其他的双标签,不能使用HTML和BODY



## Vue指令快速上手

### `v-text`

> 读取vue实例变量并显示

demo

```html
<div id="text">
    <!--这里还涉及到字符串拼接操作-->
    <h1 v-text="showtext+'我是被拼接的字符串'"></h1>
    <h2>show {{showtext}} </h2>
</div>
<script src="https://cdn.jsdelivr.net/npm/vue@2.5.21/dist/vue.min.js"></script>
<script type="text/javascript">
var vm = new Vue({
        el:"#text",
        data:{
            showtext:"Hello,Vue"
        }
    })
</script>
```

结果

![image-20211014013006865](https://i.loli.net/2021/10/14/2TmhEdl7B4H8q1w.png)

### `v-html`

> 设置元素的innerHTML,内部的标签会被解析

demo

```html
<div id="test">
    <p v-html="showtest"></p>
    <p v-text="showtest"></p>
</div>
<script src="https://cdn.jsdelivr.net/npm/vue@2.5.21/dist/vue.min.js"></script>
<script type="text/javascript">
var vm = new Vue({
        el:"#test",
        data:{
            showtest:"<a href=\"www.baidu.com\">百度一下</a>"
        }
    })
</script>
```

结果

![image-20211014013517082](https://i.loli.net/2021/10/14/cB7f3uObnDlVUiz.png)



### `v-on`

> 作用：
>
> 1、为元素绑定事件,绑定的方法定义在`methods`属性中
>
> 2、传递自定义参数，事件修饰符
>
> 事件

demo

```html
<div id="test">
    <input type="button" value="v-on指令" v-on:click="dolt">
    <input type="button" value="v-on指令简写" @click="dolt">
    <input type="button" value="双击事件" @dblclick="dolt">
    <!-- 事件绑定可以传形参 -->
    <input type="button" value="v-on指令简写" @click="doit(p1,p2)">
    <!--事件修饰符-->
    <!--按回车键时，触发该事件-->
    <input type="button" value="v-on指令简写" @keyup.enter="methods中的方法名，说明按回车时触发什么事件">
    <p @click="thisfunc">{{msg}}</p>
</div>
<script src="https://cdn.jsdelivr.net/npm/vue@2.5.21/dist/vue.min.js"></script>
<script type="text/javascript">
var vm = new Vue({
        el: "#test",
        data: {
            msg: "this代表当前vm对象"
        },
        methods: {
            dolt: function () {
                alert("事件绑定出发");
            },
            thisfunc: function () {
                this.msg+="被调用"
            }，
        }
    })
</script>
```

结果

![image-20211014020634144](https://i.loli.net/2021/10/14/wao9mDzTr6XB15u.png)

### 简单的计数器demo

```html
<!DOCTYPE html>
<html lang="en" xmlns:v-on="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="UTF-8">
    <title>loki</title>
</head>
<body>
<div id="test">
    <button @click="sub">-</button>
    <span v-text="count"></span>
    <button @click="add">+</button>
</div>
<script src="https://cdn.jsdelivr.net/npm/vue@2.5.21/dist/vue.min.js"></script>
<script type="text/javascript">
    var vm = new Vue({
        el: "#test",
        data: {
            count: 1
        },
        methods: {
            add: function () {
                if (this.count < 5)
                    this.count++;
                else
                    alert("最小值")
            },
            sub: function () {
                if (this.count > 0)
                    this.count--;
                else
                    alert("最小值")
            }
    })
</script>
</body>
</html>
```

![image-20211014163122086](https://i.loli.net/2021/10/14/yN1UK4L6wsqYalZ.png)

### `v-show`

> 通过判断条件切换元素的显示状态
>
> 原理是修改元素的display属性
>
> 指令后面的内容最终都会被解析为布尔值，值为true显示,为false隐藏

demo

```html
<div id="test">
    <!--p标签中的内容会根据这个布尔值来判断是否显示-->
    <p v-show="true">正常显示</p>
    <p v-show="age<18">不显示</p>
</div>
<script src="https://cdn.jsdelivr.net/npm/vue@2.5.21/dist/vue.min.js"></script>
<script type="text/javascript">
 var vm = new Vue({
        el: "#test",
        data: {
            istrue: true,
            age:17
        }     
    })
</script>
```

### `v-if`& `v-else`

> 原理：与v-show修改元素的display属性不同，v-if直接操作dom
>
> 效果与v-if一致
>
> 频繁切换的元素用v-show指令，反之用v-if

### `v-bind`

> 设置HTML元素的属性

demo

```html
<!--V-bind的简写   :    -->
<img v-bind:class="isActive?'active':''"  :src="imgSrc"  :title="title">
<script src="https://cdn.jsdelivr.net/npm/vue@2.5.21/dist/vue.min.js"></script>
<script type="text/javascript">
   var vm = new Vue({
        el: "#test",
        data: {
            isActive: true,
            title: "标题传参",
            imgSrc: "../static/img/1.jpg"
        }
    })
</script>
```

### `v-for`

```html
	<ul>
    	/*index指下标*/
        <li v-for="(item,index) in arr">
             {{index+1}} 号： {{item}}
        </li>
    </ul>
```

### `v-model`

> 可以用`v-model`指令在表单<input>、<textarea>及<select>元素上创建双向数据绑定,实现数据与视图同步变化

demo

```html
<div id="test">
    <select v-model="selected">
        <option value="" disabled>请选择</option>
        <option>A</option>
        <option>B</option>
        <option>C</option>
    </select>
    <span >{{selected}}</span>
</div> 
<script src="https://cdn.jsdelivr.net/npm/vue@2.5.21/dist/vue.min.js"></script>
<script type="text/javascript">
var vm = new Vue({
        el: "#test",
        data: {
            selected:""
        }
    })
</script>
```

## Vue组件

组件是可复用的`Vue`实例， 说白了就是一组可以重复使用的模板， 跟`JSTL`的自定义标签、`Thymeleal`的`th:fragment`等框架有着异曲同工之妙，通常一个应用会以一棵嵌套的组件树的形式来组织：

例如，你可能会有页头、侧边栏、内容区等组件，每个组件又包含了其它的像导航链接、博文之类的组件。

**自定义组件**

> 使用`props`属性传递参数

```html
<div id="app">
    <!--组件：传递给组件中的值：props-->
  <loki v-for="item in items" v-bind:diyloki="item"></loki>
</div>

<script src="https://cdn.jsdelivr.net/npm/vue@2.5.21/dist/vue.min.js"></script>
<script type="text/javascript">
    //定义组件
    Vue.component("loki",{
        props:['diyloki'],
        template:'<li>{{diyloki}}</li>'
    });
    var vm = new Vue({
        el:"#app",
        data:{
            items:["java","Linux","前端"]
        }
    });
</script>
```

+ `v-for="item in items"`：遍历Vue实例中定义的名为items的数组，并创建同等数量的组件
+ `v-bind:diyloki="item"`：将遍历的item项绑定到组件中props定义名为item属性上；= 号左边的panh为props定义的属性名，右边的为item in items 中遍历的item项的值


## Axios异步通信

### 简介

Axios是一个开源的可以用在浏览器端和Node JS的异步通信框架， 主要作用就是实现AJAX异步通信，其功能特点如下

### 为什么需要Axios

由于`Vue.js`是一个视图层框架并且作者(尤雨溪) 严格准守SoC(关注度分离原则)所以`Vue.js`并不包含AJAX的通信功能，所以Axios的出现就是为了解决解决通信问题（也可以用jQuery.ajax()实现通信）

### 第一个Axios程序

```html
<!DOCTYPE html>
<html lang="en" xmlns:v-binf="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <!--v-cloak 解决闪烁问题-->
    <style>
        [v-cloak]{
            display: none;
        }
    </style>
</head>
<body>
<div id="vue">
    <div>地名：{{info.name}}</div>
    <div>地址：{{info.address.country}}--{{info.address.city}}--{{info.address.street}}</div>
    <div>链接：<a v-binf:href="info.url" target="_blank">{{info.url}}</a> </div>
</div>

<!--引入js文件-->
<script src="https://cdn.jsdelivr.net/npm/vue@2.5.21/dist/vue.min.js"></script>
<script src="https://unpkg.com/axios/dist/axios.min.js"></script>
<script type="text/javascript">
    var vm = new Vue({
        el:"#vue",
        //data：属性：vm
        data(){
            return{
                info:{
                    name:null,
                    address:{
                        country:null,
                        city:null,
                        street:null
                    },
                    url:null
                }
            }
        },
        mounted(){//钩子函数
            axios
                .get('data.json')
                .then(response=>(this.info=response.data));
        }
    });
</script>
</body>
</html>

```

测试数据

```json
{
  "name": "Loki",
  "url": "oliverloki.github.io",
  "page": 1,
  "isNonProfit": true,
  "address": {
    "street": "含光门",
    "city": "陕西西安",
    "country": "中国"
  },
  "links": [
    {
      "name": "bilibili",
      "url": "https://space.bilibili.com/95256449"
    },
    {
      "name": "vue",
      "url": "https://cn.vuejs.org/v2/guide/index.html"
    },
    {
      "name": "百度",
      "url": "https://www.baidu.com/"
    }
  ]
}

```

## 几个概念

### 计算属性

[计算属性](https://blog.csdn.net/qq_41257129/article/details/90257492?ops_request_misc=%257B%2522request%255Fid%2522%253A%2522163428726416780261995187%2522%252C%2522scm%2522%253A%252220140713.130102334.pc%255Fall.%2522%257D&request_id=163428726416780261995187&biz_id=0&utm_medium=distribute.pc_search_result.none-task-blog-2~all~first_rank_ecpm_v1~rank_v31_ecpm-1-90257492.pc_search_result_cache&utm_term=%E8%AE%A1%E7%AE%97%E5%B1%9E%E6%80%A7&spm=1018.2226.3001.4187)

### 内容分发

> 在`Vue.js`中我们使用`<slot>`元素作为承载分发内容的出口，作者称其为插槽，可以应用在组合组件的场景中



### 自定义事件



## Vue-cli

### Vue-cli简介

vue-cli官方提供的一个脚手架，用于快速生成一个vue的项目模板；
  预先定义好的目录结构及基础代码，就好比咱们在创建Maven项目时可以选择创建一个骨架项目，这个估计项目就是脚手架，我们的开发更加的快速；

### Feature

- 统一的目录结构
- 本地调试
- 热部署
- 单元测试
- 集成打包上线

### 安装教程

> 需要的基础环境

Node.js：http://nodejs.cn/download/
Git：https://git-scm.com/doenloads

> 安装教程

```
# -g 就是全局安装
npm instal1 vue-cli-g
```

ps:可能因为网络问题下载失败，可以寻找镜像下载

### 第一个vue-cli程序

1. 新建一个空文件夹，在当前目录下执行

   ```
   vue init webpack lokivue //这个是你的项目名称
   //这里会配置一大堆
   ```

2. 

## WebPack

### 简介

webpack是一个现代JavaScript应用程序的**静态模块打包器**(module bundler) 。当webpack处理应用程序时， 它会递归地构建一个依赖关系图(dependency graph) ， 其中包含应用程序需要的每个模块， 然后将所有这些模块打包成一个或多个bundle.

### 安装

```
//安装打包工具
npm install webpack -g 
//安装客户端
npm install webpack-cli -g
```

验证是否安装成功

```
webpack -v
```

### 怎么玩

> 用webpack打包自己的文件

**创建 `webpack.config.js`配置文件**

entry：入口文件， 指定Web Pack用哪个文件作为项目的入口
output：输出， 指定WebPack把处理完成的文件放置到指定路径
module：模块， 用于处理各种类型的文件
plugins：插件， 如：热更新、代码重用等
resolve：设置路径指向
watch：监听， 用于设置文件改动后直接打包

```js
module.exports = {
	entry:"",
	output:{
		path:"",
		filename:""
	},
	module:{
		loaders:[
			{test:/\.js$/,;\loade:""}
		]
	},
	plugins:{},
	resolve:{},
	watch:true
}
```

**webpack打包Demo**

> 项目结构

![image-20211015154709180](https://i.loli.net/2021/10/15/16lMACy497ioTXe.png)

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<!--第一步:暴露一个方法：sayHi

exports.sayHi = function () {
    document.write("<h1>我被main.js引入，然后被webpack打包了</h1>")
}

-->

<!--第二步:require 导入一个模块，就可以调用这个模块中的方法了

var hello = require("./hello");
hello.sayHi()

-->

<!--第三步:使用webpack打包，自动导出dist/js/bundle.js-->

<!--前端模块化开发-->
<script src="dist/js/bundle.js"></script>
</body>
</html>
```



## vue-router路由

### 简介

vue-router是一个插件包，是Vue.js官方的路由管理器。它和Vue.js的核心深度集成， 让构建单页面应用变得易如反掌

### 安装配置

vue-router是一个插件包， 所以还是需要用npm进行安装的

```
//当前项目目录下以管理员身份运行
npm install vue-router --save-dev
```

**如果在一个模块化工程中使用它，必须要通过Vue.use()明确地安装路由功能**

`main.js`

```
import Vue from 'vue'
import VueRouter from "vue-router"

//显示声明使vue-router
Vue.use(VueRouter)
```

### Demo

安装路由，在src目录下，新建一个文件夹：`router`，专门存放路由，配置路由index.js，如下

> 前端工程中，习惯性将当前项目的主配置文件命名为index

项目结构

![image-20211015163856527](https://i.loli.net/2021/10/15/iKX1JElSh5eowOk.png)

`index.js`

```js
//需要的组件需要在路由中配置
import Vue from 'vue'
//导入路由组件
import Router from "vue-router"
//导入自定义组件
import Content from "../components/Content";
import Main from "../components/Main";
//安装路由
Vue.use(Router);

//配置导出路由
export default new Router({
  routes:[
    {
      //路由路径 @RequestMapping
      path:'/content',
      //路由名称
      name:'content',
      //跳转的组件
      component:Content
    },
    {
      //路由路径
      path:'/Main',
      name:'Main',
      //跳转的组件
      component:Main
    }
  ]

});
```

`main.js`

```js
import Vue from 'vue'
import App from './App'
import VueRouter from "vue-router"
//导入上面创建的路由配置目录
import router from './router' //自动扫描里面的路由配置

//来关闭生产模式下给出的提示
Vue.config.productionTip = false;
//显示声明使vue-router
Vue.use(VueRouter)

new Vue({
  el: '#app',
  //配置路由
  router,
  components: { App },
  template: '<App/>'
})
```

`app.vue`

```vue
<template>
  <div id="app">
    <!--实现界面跳转-->
    <router-link to="/main">首页</router-link>
    <router-link to="/content">内容页</router-link>
    <!--这行代码是为了页面展示-->
    <router-view></router-view>
  </div>
</template>
<script>
export default {
  name: 'App'
}
</script>
<style>
</style>

```

### 路由嵌套

```js
import ...
export default new Router({
  routes: [
    {
      //登录页
      path: '/main',
      component: Main,
     /*子路由*/
     children: [
       {path:'/user/profile',component:UerProfile},
       {path:'/user/list',component:UerList}
     ]
    }
  ]
})
```



## 结合ElementUI组件库

### 创建工程

```
#进入工程目录
cd hello-vue
#安装vue-routern 
npm install vue-router --save-dev
#安装element-ui
npm i element-ui -S
#安装依赖
npm install
# 安装SASS加载器
cnpm install sass-loader node-sass --save-dev
#启功测试
npm run dev
```

### 传参

> template中的所有文本都需要在标签内

html

```
<router-link v-bind:to="{name: '/submit',params:{username:loki}">提交</router-link>
```

router--index.js

```
{
      path: '/submit/:username',
      component: Login
    }
```

