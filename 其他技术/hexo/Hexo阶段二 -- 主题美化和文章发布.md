

## Hexo阶段二 -- 主题美化和文章发布



## Hexo的主题切换

创建 Hexo 主题非常容易，您只要在 `themes` 文件夹内，新增一个主题文件夹，并修改 `_config.yml` 内的 `theme` 设定，即可切换主题。

### 1、主题的选择

> 分享一个Hexo主题推荐的网址，当然你也可以去github或者其他渠道找到你喜欢的主题
>
> https://blog.csdn.net/zgd826237710/article/details/99671027

### 2、主题的下载

> 我以butterfly主题为例，可以在github中直接搜索找到这个主题

![image-20210605181735634](https://i.loli.net/2021/06/05/BRjcIrokPS9uxKa.png)

**下载并解压后，在themes文件夹下要有你的主题文件，这一步就完成了**

![image-20210605191934047](https://i.loli.net/2021/06/05/4p3rXoYNZ9DAyUc.png)

### 3、修改_config.yml文件中的theme值

![image-20210605192012098](https://i.loli.net/2021/06/05/tpFo1XZ7YxTlPQL.png)



### 4、重新部署到本地即可看到主题的改变

```bash
#依次使用Git Bash在当前文件夹下执行以下三个命令
hexo clean  #清除之前加载的资源--public文件夹（这个文件夹也是最后提交到github的文件夹）
hexo g      #重新部署，生成public文件夹
hexo s      #启动本地服务器
```

如果主题成功加载，界面会是这样子滴

![image-20210605192720936](https://i.loli.net/2021/06/05/dirYpbwKf1g8a3M.png)

如果页面根本无法正常渲染，只有一行字：`extends includes/layout.pug block content include ./includes/mixins/post-ui.pug #recent-posts.recent-posts +postUI include includes/pagination.pug`

**解决方法**：git bash 输入以下命令，然后重复第四步

```bash
npm install hexo-renderer-pug hexo-renderer-stylus --save
```

**到这，你的主题就算更换完成啦，但是还缺了点东西，比如**

1、自定义界面的内容

2、发布文章



## Hexo美化界面

> 这里有butterfly官方的美化教程，所以怎么自定义界面靠它就可以了
>
> https://butterfly.js.org/

注意两个`_config.yml`文件，一个在根目录，一个在themes文件夹下，然后自定义你的网站即可

> 配置文件带多数内容你应该都能看懂并修改，如果需要更详细的内容可以去hexo官网
>
> https://hexo.io/zh-cn/docs/configuration

```
.
├── _config.yml文件   hexo的配置文件，可以修改网站有哪些功能等配置
├── package.json
├── scaffolds
├── source 
|   ├── _drafts
|   └── _posts    你的博客地址，将你的md格式的博客粘贴在这即可被部署
├── source 
|   ├── butterfly(你的主题文件夹)  文件夹内有_config.yml文件，可以修改主题样式
|   └── .gitkeep    
```

**这里虽然写的少，虽然有摸鱼的成分，但其实两个配置文件的注释十分详细，你打开就知道了**

## Hexo发布文章

> 我这儿给出一种比较摸鱼的做法，容易理解

将写好的博客（markdown文档）粘贴在 `hexo-test\source\_posts`下即可被加载部署

但是还有一个点要注意，如果需要给文章分类打标签等操作，需要在文章最开头添加Front-matter

### Front-matter

这是hexo里的一个术语，Front-matter 是markdown文件最上方以 `---` 分隔的区域，用于指定个别文件的变量，举例来说：

```
---
title: SQL语句
date: 2013/7/13 20:46:25
categories: 数据库学习
tags: sql
---
```

![image-20210605195920330](https://i.loli.net/2021/06/05/67LWcADRpsiSlUr.png)

以上是一种比较简单的做法，复制写好的文章，然后给文章设置标签分类即可，足以满足基本的写博客需求，如果不能，可以在hexo官网api文档中学习进阶，将会详解每个值所定义的内容

https://hexo.io/zh-cn/docs/writing



**路漫漫其修远兮**

