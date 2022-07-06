hexo阶段一---搭建一个博客并部署到本地

> 本文共有三个阶段
>
> ​	阶段一：搭建一个博客并部署到本地
>
> ​	阶段二:  Hexo的主题美化与文章发布 
>
> ​    阶段三：将Hexo部署到Github Page,通过域名访问你的博客



> 效果展示(个人blog)：https://oliverloki.github.io/ 	

## Hexo简介

+ Hexo是什么

> Hexo 是一个快速、简洁且高效的博客框架。Hexo 使用 [Markdown](http://daringfireball.net/projects/markdown/)（或其他渲染引擎）解析文章，在几秒内，即可利用靓丽的主题生成静态网页。
>

+ Hexo特性

![image-20210318144048982](https://i.loli.net/2021/04/22/4Aob1qKd6IhRgUs.png)

> 这里的一键部署相当于集成了一部分的Git操作
>
> 在安装Git的前提下，使用`hexo -d`命令（下文中详解）可以一键部署到GitHub，免去了自己配置忽略文件等繁琐的步骤

## Hexo的安装

> Hexo的安装方式是用户从 npm 服务器下载第三方包到本地使用。npm 是 Node.js 官方提供的包管理工具
>
> =>因此使用需要先下载Node.js

### 1、安装node.js

官网下载最新版本 ： https://nodejs.org/

下载完成后点击安装，注意安装路径即可

> 由于Github是使用git来提交的，而生成的博客需要部署到GIthub
>
> =>需要先下载GIt

### 2、安装git

同样在官网下载exe文件： https://git-scm.com/

下载完成后点击安装，注意安装路径即可

git提交到远端仓库比较繁琐，所以Hexo框架集成了所需要的git操作



**判断nodejs和git是否安装成功**

1. ​	**nodejs --> 在Windows Powershell中输入 node -v ，出现版本号则证明安装成功**
2.  ​    **git --> 在Windows Powershell中输入 git version ，出现版本号则证明安装成功**



### 3、安装hexo

> 以上准备工作完成后，可以在bash命令行输入以下命令

```bash
npm install hexo-cli -g
```

![image-20210605120524176](https://i.loli.net/2021/06/05/pFuHkPmhfy6QLqW.png)

> 如果有兴趣深挖，可以看一看readme.md和其目录结构，这里我先摸了，以后有机会可能会补充

## 初始化博客目录并部署到本地

> 这一步主要是在一个空白文件夹下执行 `hexo init` 命令，初始化文件夹，然后使用`hexo -g`加载资源，最后使用`hexo s`部署到本地服务器,在浏览器中访问`localhost:4000`即可访问到初始化的hexo界面

### 1、初始化博客目录

**新建一个空文件夹（这个就是你博客的本地目录），进入该文件夹  ,  鼠标右键Git Bash  =》执行以下命令**

```bash
hexo init 
```

**执行成功会出现以下文件**

![image-20210605122156715](E:\typoradata\img\image-20210605122156715.png)

文件夹目录结构如下

```
.
├── _config.yml
├── package.json
├── scaffolds
├── source
|   ├── _drafts
|   └── _posts
└── themes
```



### 2、部署之前预先生成静态文件

```bash
hexo g
```

![image-20210605153700820](https://i.loli.net/2021/06/05/oqLAyn1lcB4IxMG.png)

### 3、启动服务器

继续执行以下指令启动服务器

```
hexo s
```

![image-20210605153812452](https://i.loli.net/2021/06/05/BtZuQ36esyDJjfA.png)

> 默认情况下，访问网址为： `http://localhost:4000/`

![image-20210605155320353](https://i.loli.net/2021/06/05/XnlJHtKsAY5Vcij.png)

## 小结

到这一步，你已经成功的搭建了一个博客，本且成功的让它跑在了本地的服务器，但是现在还有几个问题



> 如何美化主题并上传自己的文章
>
> 如何将它部署到github通过域名来访问它



## hexo常用命令行操作

```bash
  clean     Remove generated files and cache.
  config    Get or set configurations.
  deploy    Deploy your website.
  generate  Generate static files.
  help      Get help on a command.
  init      Create a new Hexo folder.
  list      List the information of the site
  migrate   Migrate your site from other system to Hexo.
  new       Create a new post.
  publish   Moves a draft post from _drafts to _posts folder.
  render    Render files with renderer plugins.
  server    Start the server.
  version   Display version information.

Global Options:
  --config  Specify config file instead of using _config.yml
  --cwd     Specify the CWD
  --debug   Display all verbose messages in the terminal
  --draft   Display draft posts
  --safe    Disable all plugins and scripts
  --silent  Hide output on console
```


