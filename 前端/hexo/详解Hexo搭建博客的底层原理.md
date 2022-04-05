## 前言

在2021年初对照着攻略趟过无数坑终于通过Hexo把博客给整出来了

> Hexo官网-----快速、简洁且高效的博客框架
>
> https://hexo.io/zh-cn/

但是在不断学习的过程中对个人博客的技术选型又有了新的想法，想搭建一个自己喜欢的UI风格并且有后台的博客CMS



于是回过头来，开始对hexo原理好奇起来，本着知其然更要知其所以然的态度，搜了许多有关资料，现在对hexo有了些新的认识，就来梳理记录一下



> 梳理一下Hexo的优缺点之我见

**优点：**

+ 生态丰富，好看的主题多

+ 相关文档多，解决bug有优势
+ 通过修改yaml配置文件来修改样式比较简单

**缺点：**

+ 初次配置会遇到各种各样的bug，对新手和非专业人员不是很友好
+ 每次发布文章都需要重新部署，没有一个后台管理系统
+ 对于程序员来说，还是想拥有一个自己亲手撸的个人博客



首先需阐明的是本文用window系统为例，而有相当一部分Hexo是搭建在LNMP或LAMP环境下的，但这一切仅仅是物理存储位置发生了变化，原理依旧不变。 LNMP指的是 linux+nginx+mysql+php，这是一款功能非常强大的环境套件，nginx以其轻量而高效受到建站者的青睐

## Github page

Hexo搭建的博客项目源码托管在github，并享受github pages服务。

![image-20220109002620342](https://s2.loli.net/2022/01/09/AbzXy3Hd6T8PRZY.png)



 GitHub pages简称pages服务，每个仓库都有一个pages服务，可用来展示项目，通过简单的设置项目的index.html，并以此做为入口供用户参观访问。可是一个相当实用的功能呀！大部分的 pages 服务都是用来搭建个人博客的。言外之意不仅能搭建博客，还能做些其他的事情~



> 为什么需要Pages技术

生成博客的页面有动、静态页面之分

著名的博客 wordpress ，既是[**动态**](https://baike.baidu.com/item/动态网页/6327050?fromtitle=动态页面&fromid=8586386)页面生成的博客，其思路是 php + MySql 。

博客hexo生成的是[**静态**](https://baike.baidu.com/item/动态网页/6327050?fromtitle=动态页面&fromid=8586386)页面，而Github pages 又支持静态页面的解析。因此二者一拍即合能够用来生成 html 拼合成博客。



> 静态页面意味着评论、分享等功能得依赖于第三方插件，但它所带来的轻量、高效是动态页面所不及的！！！

## Hexo



从上面我们了解到 Hexo 是用来生成 HTML 的，那么这章我们就主要来讲一下 Hexo 是怎样生成 HTML 的

### 什么是Hexo

在官网中Hexo被介绍为快速、简洁且高效的博客框架。

在官方文档中，我们可以找到他的定义：Hexo 是一个快速、简洁且高效的博客框架。Hexo 使用 [Markdown](http://daringfireball.net/projects/markdown/)（或其他渲染引擎）解析文章，在几秒内，即可利用靓丽的主题生成静态网页。

### 工作原理

hexo的文件架构

```
├── node_modules：             #依赖包-安装插件及所需nodejs模块。
├── public          #最终网页信息。即存放被解析markdown、html文件。
├── scaffolds         #模板文件夹。即当您新建文章时，根据 scaffold生成文件。
├── source          #资源文件夹。即存放用户资源。
|   └── _posts         #博客文章目录。
└── themes             #存放主题。Hexo根据主题生成静态页面。
├── _config.yml       #网站的配置信息。标题、网站名称等。
├── db.json：        #source解析所得到的缓存文件。
├── package.json      # 应用程序信息。即配置Hexo运行需要js包。
```



首先本地文件夹的`source`就是数据库，以.md（markdown）格式存储文章，`theme`文件夹是主题文件（决定页面模板）。

我们可以通过部署流程来逐步详尽分析。以下先来了解hexo有哪些部署命令，再分析部署的步骤流程

> hexo的命令总览

hexo可以粗略分为三个子项目，分别是

```
- hexo-cli
- hexo (下文中用hexo core来指代)
- hexo plugins
```

其中hexo plugins不是指某一个单独的项目，而是泛指所有的hexo plugin项目



请看下图：

![image-20220109003141314](https://s2.loli.net/2022/01/09/t76pKGU5g19PaxO.png)

> - **hexo-cli**：hexo命令行，作用是：
>
>   1.启动hexo命令进程和参数解析机制。每次我们输入`hexo xxx`命令后，都会通过node调用hexo-cli中的[entry函数](https://github.com/hexojs/hexo-cli/blob/5e0969ffa64dec427428a245ab2d65beaf23123b/lib/hexo.js#L13)(比如，可以把’hexo init’视为’node hexo-cli/entry.js init’)，`hexo init`命令仅仅在安装时调用
>
>   2.实现hexo命令的三个初始参数：init/version/plugins
>
>   3.加载hexo核心模块，并初始化
>
> - **hexo core**：hexo核心，作用是：
>
>   1.实现hexo的new、generate、publish等功能
>
> - **hexo plugins**: 指一些能够扩展hexo的插件。插件可以按功能分成两类:
>   1.扩展hexo命令的参数，如`hexo-server`(安装这个插件以后才能使用hexo server命令)
>   2.扩展hexo解析文件的”能力”，如增加jade模版解析功能的hexo-render-jade插件

### 每次部署的流程



1. `hexo g`：生成静态文件。将我们的数据和界面模板相结合生成静态文件的过程。Hexo（node.js程序）遍历主题文件中你的`source`目录（js、css、img等静态资源），建立索引，再根据索引生成由html、js、css、img建立的纯静态文件并放在public文件夹里。public就是你的博客了，而这些恰好能被gitpages识别。

1. `hexo d`：部署文件。主要是根据在_config.yml中配置的git仓库或者coding的地址，将public文件通过git方式push到上传到github或coding的指定分支，然后在根据pages服务呈现出页面。当然把public文件部署至你的服务器也是OK滴

![image-20220109003452317](https://s2.loli.net/2022/01/09/gBFpYTvn3d6c4Ss.png)

图为git主要命令



## 模板引擎--Hexo怎样生成HTML

模板引擎的作用，就是将界面与数据分离。最简单的原理是将模板内容中指定的地方替换成数据，实现业务代码与逻辑代码分离



1.请看Hexo文件结构，`source`文件夹与`themes`文件夹是同级的，进而我们就可以将`source`文件夹理解为数据库，而主题文件夹相当于界面。`hexo g`就将我们的数据和界面相结合生成静态文件`public`



2.Hexo模板引擎默认使用ejs编写（本文以ejs模板举例，其他的有swig、jade）。hexo首先会解析.md文件，然后根据layout变量判断布局类型，再调用相应布局文件，如此这番每一块内容都是独立的，提供代码的复用性。最终生成一个html页面

> 注意

3.布局模板文件位于`layout`文件夹下，以下将**布局模板**简称**模板**。`layout`文件结构在不同主题下文件排布不一，但整体框架是一样的，有：

- 公共模板。里面引入了head、footer等公共组件，在其他模板下会自动引入公共模板
- 首页模板
- 文章模板
- 分类模板
- 归档模板
- ……

> 每个模板都默认使用layout布局，您可在文章的前置申明中指定其他布局，比如“post”或者“page”或是设为false来关闭布局功能（如果不填默认是post，这个在_config.yml中可以设置默认值），您甚至可在布局中再使用其他布局来建立嵌套布局。

在我们新建页面或者新建文章的使用可以选定我们使用的模板。`hexo new [layout] <title>`就会使用对应的模板

4.模板是可以**自定义**的

## 数据填充

上面解释了界面的原理，此篇将分析数据及数据如何与界面结合。

数据的填充主要是 `hexo -g` 的时候将数据传递给 ejs 模板，然后再由 ejs 模板填充到 HTML 中。

### 配置文件中的数据

------

Hexo 的配置文件 `_config.yml` 使用 [yml语法](https://link.juejin.im/?target=http%3A%2F%2Fdocs.ansible.com%2Fansible%2Flatest%2FYAMLSyntax.html) 。例如博客的名字、副标题等等之类。这些数据项组织在 config 对象中。可以数字、字符串、对象、数组，

### 配置文件中数据的使用

------

如果要在模板中使用某个具体的值，比如字符串、数字、逻辑变量或者对象的某个成员，可以在主题的模板文件 ejs中直接使用：

```
{% block title %} {{ page.title }} | {{ config.title }} {% endblock %}

```

## 总结

本质而言Hexo是一款静态页面生成工具

基于Hexo搭建的博客原理简单的说，是一个数据和界面的结合体。数据以.md与.html等格式存储于数据库`source`文件夹中，界面存于`themes`文件夹中。每次运行hexo g命令，遍历数据建立索引，根据索引把主题界面文件生成至`public`文件夹中，即html文件。再运行hexo d 将文件托管，由pages服务显示。







