> **Q:**      **git是什么呢？**
>
>
> **A:**    **Git是目前世界上最先进的分布式版本控制系统**
>
> 因此在学习git之前必须要了解的一个概念-------版本控制

@[toc]

## 一、理解版本控制系统

> 什么是版本控制

版本控制(Revision control)是一种在开发的过程中用于**管理我们对文件、目录或工程等内容的修改历史，方便查看更改历史记录，备份以便恢复以前的版本的软件工程技术**。

> 版本控制的具体功能

版本控制最主要的功能就是**追踪文件的变更**。它将什么时候、什么人更改了文件的什么内容等信息忠实地了记录下来。每一次文件的改变，文件的版本号都将增加。除了记录版本变更外，版本控制的另一个重要功能是并行开发。软件开发往往是多人协同作业，版本控制可以有效地解决版本的同步以及不同开发者之间的开发通信问题，提高协同开发的效率。并行开发中最常见的不同版本软件的错误(Bug)修正问题也可以通过版本控制中分支与合并的方法有效地解决

> 没有版本控制可能出现的问题

没有进行版本控制或者版本控制本身缺乏正确的流程管理，在软件开发过程中将会引入很多问题，如软件代码的一致性、软件内容的冗余、软件过程的事物性、软件开发过程中的并发性、软件源代码的安全性，以及软件的整合等问题

### 主流的版本控制系统

+ Git
  + 开源的分布式版本控制系统
  + 大牛`Linus`为了帮助管理 Linux 内核开发而开发的一个开放源码的版本控制软件
  + 不仅仅是个版本控制系统，它也是个内容管理系统(CMS)，工作管理系统等
+ SVN
  + SVN是subversion的缩写，是一个[开放源代码](https://baike.baidu.com/item/开放源代码/114160)的版本控制系统
  + 设计目标就是取代CVS
+ CVS

> Git与SVN的区别

- 1、Git 是分布式的，SVN 不是：这是 Git 和其它非分布式的版本控制系统，例如 SVN，CVS 等，最核心的区别。
- 2、Git 把内容按元数据方式存储，而 SVN 是按文件：**所有的资源控制系统都是把文件的元信息隐藏在一个类似 .svn、.cvs 等的文件夹里。
- 3、Git 分支和 SVN 的分支不同：**分支在 SVN 中一点都不特别，其实它就是版本库中的另外一个目录。
- 4、Git 没有一个全局的版本号，而 SVN 有：**目前为止这是跟 SVN 相比 Git 缺少的最大的一个特征。
- 5、Git 的内容完整性要优于 SVN：**Git 的内容存储使用的是 SHA-1 哈希算法。这能确保代码内容的完整性，确保在遇到磁盘故障和网络问题时降低对版本库的破坏。

### 版本控制系统的工作模式

版本控制系统的工作模式主要有两种，集中式工作模式和分布式工作模式

> （一）集中式工作模式

		为了让不同系统上的开发者能够协同工作，集中化的版本控制系统应运而生。集中式版本控制系统，版本库是集中存放在中央服务器的，而干活的时候，用的都是自己的电脑，所以要先从中央服务器取得最新的版本，然后开始干活，干完活了，再把自己的活推送给中央服务器。中央服务器就好比是一个图书馆，你要改一本书，必须先从图书馆借出来，然后回到家自己改，改完了，再放回图书馆。

![image-20210602223109348](https://img-blog.csdnimg.cn/img_convert/4fd954035d52e8b13b33349afb8a7aff.png)

+ 存在的问题：
  1. 一旦服务器出现问题，所有的客户机将无法更新到最新版本，并且无法恢复到指定的版本。因为所有版本信息都在中央版本库中。
  2. 必须具有网络环境，单机无法实现版本控制。
  3. 客户机之间无法直接进行联系，无论中央版本服务器是否出现问题。CVS,SVN 都是集中式版本控制系统。

> （二）分布式工作模式

	分布式版本控制系统中无需中面版本库服务器，每台客户机都具有独立的版本控制功能，多台客户机之间相连就可以实现文件共享及版本管理。

![image-20210602224003509](https://img-blog.csdnimg.cn/img_convert/e5653483a0fd9c97b49a674f0215925e.png)

+ 分布式工作模式的好处

  无需网络环境也可以进行版本控制,在网络环境下可以实现协同工作

```
分布式版本系统的最大好处之一是在本地工作完全不需要考虑远程库的存在，也就是有没有联网都可以正常工作，而SVN在没有联网的时候是拒绝干活的
```

### Git的工作模式

**包含：集中式工作模式，开源社区工作模式**

+ 集中式工作模式

  Git为了便于客户机之间的协同工作,Git版本控制系统一般会设置一个中央版本库
  服务器，目的是让所有客户机都从该主机更新版本,提交最新版本。该工作模式下的客
  户机地位都平等

+ 开源社区工作模式

  + 对于开源软件开源社区的协作开发模式,不可能让所有人都具有修改中央版本库的权限，**让不同的客户机对中央版本库的不同操作权限**，将有利于维护开源软件的安全性
  + 你可以下载某一个仓库到你的本地仓库，但是你如果向原作者提交修改代码，需要原作者同意并且修改整合后才能提交成功



## 二、Windows环境下安装配置Git

### 安装

> 官网：https://git-scm.com/
>
> 如果下载比较慢的话可以用镜像下载：https://npm.taobao.org/mirrors/git-for-windows/

1. 下载后为可执行文件，直接双击安装

2. 点击下一步完成安装，注意安装目录即可

   ![image-20210603000348787](https://img-blog.csdnimg.cn/img_convert/06d0dcbab955bce8bf123b647296e133.png)

**安装成功后在开始菜单中会有Git项，菜单下有3个程序∶任意文件夹下右键也可以看到对应的程序!**

![image-20210603000808890](https://img-blog.csdnimg.cn/img_convert/f8dfe78549a0c2062d0f7351e6d753a7.png)

+ Git Bash : Unix与Linux风格的命令行，使用最多，推荐最多
+ Git CMD : Windows风格的命令行
+ Git GUI: 图形界面的Git，不建议初学者使用，尽量先熟悉常用命令

> **环境变量的说明**

在安装Git时，它会自动在系统环境变量里配置环境变量，我们鼠标右键就可以在任意一个文件夹下使用git，不需要手动配置了

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210603150931385.png)

### 卸载

1. 清理系统环境变量

   ![image-20210602235228422](https://img-blog.csdnimg.cn/img_convert/a02c74d8f19e6db91a20eb307344b869.png)

2. 在Windows设置中找到册程序卸载直接卸载Git即可

   ![image-20210602235423816](https://img-blog.csdnimg.cn/img_convert/36edbb4e0d116681958c6349cb52a07f.png)

两步之后即可将git卸载干净

### 如何验证Git安装成功

进入`Git bash`或者命令行

```bash
#输入以下命令
git --version
```

若出现对应版本号，说明git安装成功

![image-20210604004518446](https://img-blog.csdnimg.cn/img_convert/120276934e1cc4d1f65dd4c1b5f2e255.png)



### 更新

自从Git 2.16.1（2）你可以在命令行中使用以下命令来更新Git

```
git update-git-for-windows
```

## 三、初探Git底层结构

### Git工作区域

> Git本地有三个工作区域:文件在这四个区域之间的转换关系如下

![image-20210603151418488](https://img-blog.csdnimg.cn/img_convert/c2512dda5fc34c898b093311411f5b49.png)

+  **Workspace :工作区，就是你平时存放项目代码的地方**
+  **Index / Stage:暂存区，用于临时存放你的改动，事实上它只是一个文件，保存即将提交到文件列表信息**
+  **Repository :仓库区（或本地仓库），就是安全存放数据的位置，这里面有你提交到所有版本的数据。其中HEAD指向最新放入仓库的版本**
+  **Remote :远程仓库，托管代码的服务器**

> 注：暂存区是Git非常重要的概念，弄明白了暂存区，就弄明白了Git的很多操作到底干了什么

### Git管理的文件的三种状态

> **git管理的文件有三种状态︰已修改(modified),已暂存(staged),已提交(committed)**

+ 运行`git status`看看当前仓库的状态

- 如果`git status`告诉你有文件被修改过，用`git diff`可以查看修改内容。

> 我们把文件往Git版本库里添加的时候，是分两步执行的
>

第一步是用`git add`把文件添加进去，实际上就是把文件修改添加到暂存区；

第二步是用`git commit`提交更改，实际上就是把暂存区的所有内容提交到当前分支。

因为我们创建Git版本库时，Git自动为我们创建了唯一一个`master`分支，所以，现在，`git commit`就是往`master`分支上提交更改。

你可以简单理解为，需要提交的文件修改通通放到暂存区，然后，一次性提交暂存区的所有修改。



### 关于.gitignore文件

> 一般情况下工作区中的文件都是要交给Git管理的，但有些文件并不想交给Git管理，但是又由于该文件处理工作区，所有我们在执行git status 命令的时候会给出xx文件Untracked (未被跟踪)。

**解决办法：**

	<u>添加要忽略的文件名到.gitignore文件中，那Git就不会再检测该文件</u><u>，将忽略文件中的文件名删除之后，那么查看状态的时候该文件又被提示:未被跟踪。</u>

1. 我们通过一些设置让 Git 忽略这些文件，再运行git status命令时不对其进行检测
2. 只要在工作区创建一个文件，名称为`.gitignore`
3. 把要被忽略的文件名写入到其中
4. 然后将.gitignore文件add添加并提交commit到本地版本库即可

**忽略文件编写规则：**

1. 忽略文件中的空行或以井号(#）开始的行将会被忽略。
2. 可以使用Linux通配符。例如∶星号(*)代表任意多个字符，问号( ﹖)代表一个字符，方括号([abc] )代表可选字符范围，大括号( {string1,string2,…})代表可选的字符串等。
3. 如果名称的最前面有一个感叹号(!)，表示例外规则，将不被忽略。
4. 如果名称的最前面是一个路径分隔符(/ )，表示要忽略的文件在此目录下，而子目录中的文件不忽略。
5. 如果名称的最后面是一个路径分隔符（/ )，表示要忽略的是此目录下该名称的子目录，而非文件（默认文件或目录都忽略)。

```bash
*. txt 			#忽略所有. txt结尾的文件
! 1ib. txt		#但1ib. txt除外
/temp			#仅忽略项目根目录下的TOD0文件，不包括其它目录temp
bui1d/			#忽略bui1d/目录下的所有文件
doc/*. txt		#会忽略doc/notes. txt但不包括doc/server/arch. txt
```





## 四、快速开始：使用Git对你的本地仓库进行版本控制

1. 初始化一个本地工作区，每个版本库仅需要执行一次

   通俗一点就是命令行进入你想要进行版本管理的文件夹目录，执行 `git init` 或者 `git clone [url]` 命令

   ```bash
   #本地仓库的搭建有两种方式，初始化后文件夹多了一个目录 .git（隐藏目录）
   	# 1、初始化本地仓
   	git init #执行
   	# 2、从github / gitee 导入
   	git clone [url] 
   #这个目录下包含了当前版本库正常工作所需要的所有内容:暂存区文件，版本记录文件，配置文件等。
   #换句话，如果你想从项目中删除Git的版本控制，但又要保留项目原文件，那么只需要将这个.git目录删除即可。这样话，这个项目就与Git没有任何关系
   ```

2. 添加指定文件到版本控制管理(只是添加到暂存区)

   ```bash
   git add . #全部添加
   git add [文件名]
   ```

3. 将修改操作操作到本地版本库(将暂存区的内容提交到本地版本库)

   每当你觉得文件修改到一定程度的时候，就可以`保存一个快照`，这个快照在Git中被称为`commit`。一旦你把文件改乱了，或者误删了文件，还可以从最近的一个`commit`恢复，然后继续工作，而不是把几个月的工作成果全部丢失

   ```bash
   git commit
   ```

4. 将本地版本库中的修改内容`push`到中央版本库，将修改过的内容备份到中央版本库，方便协同开发

   这里涉及到绑定远端仓库，在第五小节详解，可以学习后再来看这一步

   ```
   git push origin main
   ```

5. 将中央版本库中的变化内容`pull`到本地版本库，这里涉及到冲突解决

   ```
   git pull origin main
   ```

## 五、协同开发第一步：连接远程仓库

### 使用Git远程仓库前要做的配置

由于版本库的所有提交操作都需要注明操作者身份，**Git 要求`用户名`和`Email`这两样信息是必不可少的**

所以客户机首先需要进行自我身份的注册，即创建用户

> **如何创建用户？**

Git具有三种不同的创建方式，会产生三种不同作用域的用户。这三种创建方式的用户信息会写到三个不同的配置文件中。这三种用户的创建均需要使用 git config 命令，只不过使用的选项不同。

```bash
#自己的电脑这样配置好就可以进行开发了，配置后这一部分可以跳过
git config --global user.name "你的用户名"
git config --global user.email "你的邮箱地址"
```

**`git config -l `显示当前配置**

+ 这三种创建方式的创建的用户作用域由大到小依次是:`系统用户`，`全局用户`与`本地库用户`。在多种用户都进行了创建的前提下，小范围用户会覆盖大范围用户，即默认会以小范围用户操作Git。

**查看用户配置信息**

```bash
#查看系统用户配置信息
git config --system --list
#查看全局用户配置信息
git config --global --list
#查看本地用户配置信息
git config  --list
```

**创建不同用户**

1. 系统用户:当前主机系统中所有用户均可以使用的Git用户

   ```bash
   git config --system user.name"创建的用户名"
   git config --system user.email"创建的用户邮箱"
   ```

2. 全局用户:当前主机系统的当前登录用户可以使用的Git用户

   ```bash
   git config --global  user.name"创建的用户名"
   git config --global  user.email"创建的用户邮箱"
   ```

3. 本地库用户:只能对当前的本地版本库进行提交的Git用户

   ```bash
   git config  user.name"创建的用户名"
   git config  user.email"创建的用户邮箱"
   ```

> 小范围的值一旦设定，值会将前面的值覆盖，即起作用的将是小范围用户那一组。

**所有的配置其实都以文件的形式保存在本地**

全局配置文件保存的位置

```
C:/user/[你的用户名]/.gitconfig
```

![](https://img-blog.csdnimg.cn/img_convert/129c9b0a7f2743d20531dd911c67b9ac.png)

系统配置文件保存的位置

```
Git工作目录/etc/gitconfig
```

![image-20210603150002089](https://img-blog.csdnimg.cn/img_convert/c93c3238d0941e9caa9324aa7dde649f.png)



### 绑定Github远程仓库

**使用SSH密钥绑定GitHub具体步骤**

1. 注册自己的[github](https://github.com/)账号

2. 生成本地的SSH公钥，并且绑定到远端的github，实现免密码登录（这一步很重要否则每次提交都需要输入密码）

   ```bash
   #进入C:\Users\yu'chun\.ssh目录
   #生成公钥
   ssh-keygen
   ```

   生成结果![image-20210603163230769](https://img-blog.csdnimg.cn/img_convert/fda21b3967ae0c2cb61621a48df98bf8.png)

3. 将密钥添加至github/jitee即可添加成功

   ![image-20210603190555558.png](https://img-blog.csdnimg.cn/img_convert/d80129683c5c76159c5072ed87e3b417.png)


> 为什么GitHub需要SSH Key呢？因为GitHub需要识别出你推送的提交确实是你推送的，而不是别人冒充的，而Git支持SSH协议，所以，GitHub只要知道了你的公钥，就可以确认只有你自己才能推送。
>
> 当然，GitHub允许你添加多个Key。假定你有若干电脑，你一会儿在公司提交，一会儿在家里提交，只要把每台电脑的Key都添加到GitHub，就可以在每台电脑上往GitHub推送了。
>
> 最后友情提示，在GitHub上免费托管的Git仓库，任何人都可以看到喔（但只有你自己才能改）。所以，不要把敏感信息放进去。
>
> 如果你不想让别人看到Git库，有两个办法，一个是交点保护费，让GitHub把公开的仓库变成私有的，这样别人就看不见了（不可读更不可写）。另一个办法是自己动手，搭一个Git服务器，因为是你自己的Git服务器，所以别人也是看不见的，相当简单，公司内部开发必备。
>
> 确保你拥有一个GitHub账号后，我们就即将开始远程仓库的学习。

**确保以上步骤结束以后，我们就即将开始远程仓库的学习**

### 远程库的相关操作

> 本地库与远程库相关联

```
git remote add origin [远程库url]
```

**添加后，远程库的名字就是`origin`，这是Git默认的叫法，也可以改成别的，但是`origin`这个名字一看就知道是远程库**

> 把本地库的所有内容推送到远程库

```
git push -u origin master
```

把本地库的内容推送到远程，用`git push`命令，实际上是把当前分支`master`推送到远程。

**注：**由于远程库是空的，我们第一次推送`master`分支时，加上了`-u`参数，Git不但会把本地的`master`分支内容推送的远程新的`master`分支，还会把本地的`master`分支和远程的`master`分支关联起来，在以后的推送或者拉取时就可以简化命令

```
git push origin master
```

> 删除与远程库的关联

如果添加的时候地址写错了，或者就是想删除远程库，可以用`git remote rm <name>`命令。使用前，建议先用`git remote -v`查看远程库信息：

```
git remote rm [仓库名称]
```

此处的“删除”其实是解除了本地和远程的绑定关系，并不是物理上删除了远程库。远程库本身并没有任何改动。要真正删除远程库，需要登录到GitHub，在后台页面找到删除按钮再删除。

> 从远程库克隆

```
git clone [仓库url]
```

如果有多个人协作开发，那么每个人各自从远程克隆一份就可以了

> 你也许还注意到，GitHub给出的地址不止一个，还可以用`https://github.com/michaelliao/gitskills.git`这样的地址。实际上，Git支持多种协议，默认的`git://`使用ssh，但也可以使用`https`等其他协议。
>
> 使用`https`除了速度慢以外，还有个最大的麻烦是每次推送都必须输入口令，但是在某些只开放http端口的公司内部就无法使用`ssh`协议而只能用`https`。



## 六、Git 常用命令

![image-20210603152410206](https://img-blog.csdnimg.cn/img_convert/751016559af149cb9221994b6facaa7c.png)

#### 文件操作

```bash
#查看文件状态
git status

#添加文件--该命令的作用是告诉Git系统，将指定文件的当前快照写入到版本库暂存区。即将文件交给Git进行版本管理。
    #添加全部文件
    git add .
    #添加指定文件
    git add [filename]

#提交到本地仓库--提交操作就通过命令将Git暂存区中的文件快照永久性地写入到本地仓库中  （提交时使用 -m 参数给出一个操作提示信息）
git commit -m"这次修改了什么东西的描述"

#查看区别--比较不同工作区域文件的异同
	# 1)比较工作区与暂存区
	git diff [filename]
	# 2）比较暂存区与本地库
	git diff --cached [filename]
#查看暂存区文件
git ls-files
```

#### 历史版本查看

>**注：git log 命令只可以查看到HEAD指针及其之前的版本信息,如果版本发生回退，则可能会出现HEAD指针之后仍存在版本的情况，而这些版本信息通过git log命令是看不到的.所以我们就得使用git reflog可查看到所有历史版本信息。**

> **HEAD指针**
>
> Git会默认创建一个master分支，即主分支。这是Git对版本进行管理的唯一的一条时间线。在这条master时间线上有很多版本的时间节点，而HEAD指针则指向的是当前刚刚提交的版本时间节点
> ==总结以下就是 git log 可以查看本地 `git commit` 提交记录==

```bash
#查看可引用历史版本
git log  #详细信息
git log --oneline  #简单显示
```

#### 版本回退

> 回退到指定版本。但仅仅修改分支中的HEAD指针的位置，不会改变工作区与暂存区中的文件的版本。实现上是改变了暂存区commit之前的状态

```bash
#git reset 命令用于回退版本
git reset [--soft | --mixed | --hard] [HEAD]
```

**`--mixed `为默认，可以不用带该参数，用于重置暂存区的文件与上一次的提交(commit)保持一致，工作区文件内容保持不变**

```bash
 git reset HEAD^            # 回退所有内容到上一个版本  
 git reset HEAD^ hello.php  # 回退 hello.php 文件的版本到上一个版本  
 git reset 052e             # 回退到指定版本
```

```
git reset --soft [HEAD]
```

**`--hard` 撤销工作区中所有未提交的修改内容，将暂存区与工作区都回到上一次版本，并删除之前的所有信息提交**

**注意：**谨慎使用 –hard 参数，它会删除回退点之前的所有信息

**`--soft` 用于回退到某个版本**

```bash
git reset –hard HEAD~3   # 回退上上上一个版本  
git reset –hard bae128   # 回退到某个版本回退点之前的所有信息。 
git reset --hard origin/master    # 将本地的状态回退到和远程的一样 
```

> **HEAD的说明**
>
> HEAD 表示当前版本，HEAD^ 上一个版本，HEAD^^ 上上一个版本，依次类推..

#### 文件删除

要删除文件，首先要清楚该文件所处的git状态  `git status`

若要是该文件未被Git管理，在工作区直接进行删除即可。但是若该文件已经经过多次add与commit操作后要文件要被删除，则我们就需要通过Git命令来操作。

```bash
	#删除暂存区指定文件
	git rm --cached [filename]
	#删除工作区指定文件
	rm [filename]
	#删除本地库指定文件
	git rm [filename]
	git commit
```

> `git checkout`其实是用版本库里的版本替换工作区的版本，无论工作区是修改还是删除，都可以“一键还原”。
>
> **注意：从来没有被添加到版本库就被删除的文件，是无法恢复的！**
>
> **小结**
>
> 命令`git rm`用于删除一个文件。如果一个文件已经被提交到版本库，那么你永远不用担心误删，但是要小心，你只能恢复文件到最新版本，你会丢失**最近一次提交后你修改的内容**

#### 撤销修改

撤销修改内容--对于己修改过的文件内容需要进行撒销，根据修改内容已经出现的位置可以分为三种情况:(相当于git的CTRL+Z)

```bash
	# 1）仅仅是工作区中内容进行了修改，还未添加add到暂存区
	git checkout -- [filename]
	# 2）已经add添加到暂存区，但是还未commit提交到本地版本库
	git checkout -- [filename]
	# 3）已经提交commit到本地版本库--无法撤销修改，可以回退到之前版本
	git reset [HEAD] [filename]
```

> 命令`git checkout -- readme.txt`意思就是，把`readme.txt`文件在工作区的修改全部撤销，这里有两种情况：
>
> 一种是`readme.txt`自修改后还没有被放到暂存区，现在，撤销修改就回到和版本库一模一样的状态；
>
> 一种是`readme.txt`已经添加到暂存区后，又作了修改，现在，撤销修改就回到添加到暂存区后的状态。
>
> 总之，就是让这个文件回到最近一次`git commit`或`git add`时的状态。

#### git log 命令中翻页与退出操作

```bash
#翻页与退出
	#当gitlog日志命令显示的内容太多，无法在一页内显示完毕所有内容时，其最后一行会出现一个冒号，让输入命令			
	回车:显示下一行
	空格:显示下一页
	q:退出git log命令
```



## 七、Git分支：协同开发的灵魂

### 理解分支和主干

> **主干：Git是以时间为主线对版本进行管理的，而这条时间主线就是Git主干**

> **分支：为了更加形象的描述指针的移动轨迹，我们称某一个指针的移动轨迹为一个分支**
>
> **这样的话，一个指针就代表了一个分支，可以使用不同的指针操作不同的分支。Master指针的移动轨迹与Git的主干是重合，所以称为master主分支**

> **`HEAD`严格来说不是指向提交，而是指向`master`，`master`才是指向提交的，所以，`HEAD`指向的就是当前分支**

**一开始的时候，`master`分支是一条线，Git用`master`指向最新的提交，再用`HEAD`指向`master`，就能确定当前分支，以及当前分支的提交点**

![image-20220113155344612](https://img-blog.csdnimg.cn/img_convert/0383f4cc925776ea57fdaf885bde72b7.png)

**每次提交，`master`分支都会向前移动一步，这样，随着你不断提交，`master`分支的线也越来越长。**

**当我们创建新的分支，例如`dev`时，Git新建了一个指针叫`dev`，指向`master`相同的提交，再把`HEAD`指向`dev`，就表示当前分支在`dev`上**

![image-20220113155527026](https://img-blog.csdnimg.cn/img_convert/4d5d29a175039290f0f45adab89d228e.png)



**Git创建一个分支很快，因为除了增加一个`dev`指针，改改`HEAD`的指向，工作区的文件都没有任何变化！**

**不过，从现在开始，对工作区的修改和提交就是针对`dev`分支了，比如新提交一次后，`dev`指针往前移动一步，而`master`指针不变：**

![image-20220113155615227](https://img-blog.csdnimg.cn/img_convert/3140a256e3331bb458fa0906e9448d21.png)



**假如我们在`dev`上的工作完成了，就可以把`dev`合并到`master`上。Git怎么合并呢？最简单的方法，就是直接把`master`指向`dev`的当前提交，就完成了合并**

![image-20220113155743765](https://img-blog.csdnimg.cn/img_convert/6d0ec6e746c532ae3b557398a4f37042.png)

**合并完分支后，甚至可以删除`dev`分支。删除`dev`分支就是把`dev`指针给删掉，删掉后，我们就剩下了一条`master`分支**

![image-20220113155753642](https://img-blog.csdnimg.cn/img_convert/6fec521e0015d2f0354e4de02964cd7a.png)

**注：**

+ 主干上的每一个节点就是一个版本，即一次commit提交
+ 在主干上可以定义多个指针，指向不同的节点
+ Git默认会创建一个名称为master的指针
+ 默认情况下用户操作的是master指针，但用户通过命令对操作的指针进行切换。用户每次提交一次，就会形成一个新的节点，当前操作的指针就会向前移动一次

**通过以上对Git分支操作的理解，下面来用命令行实战一下吧**

### 创建与合并分支

> 1、创建一个分支

我们创建`dev`分支，然后切换到`dev`分支

```
git checkout -b dev
```

`git checkout`命令加上`-b`参数表示创建并切换，相当于以下两条命令：

```
$ git branch dev
$ git checkout dev
```

查看当前分支

```bash
git branch
#git branch命令会列出所有分支，当前分支前面会标一个*号。
```

然后，我们就可以在`dev`分支上进行修改提交等操作

> 2、当dev分支上的工作完成后，需要将其合并到主分支master 上

合并过程其实很快，只需要将master指针指向dev指针指向的节点，然后再将HEAD指针指向master指针指向的节点即可。即分支的合并就是修改了两个指针的指向而已

```bash
#对于分支的合并需要注意，如果要将分支B合并到分支A上，首先要将当前分支切换色A分支上，然后再运行合并命令
git merge [dev]
```

**`git merge`命令用于合并指定分支到当前分支**

> 3、分支合并后的删除

**合并后的dev分支不仅无用还会带来版本不一致的麻烦，因此一般需要将dev分支删除**

```bash
#若要删除某一分支，必须要保证当前分支不能是被删除的分支
git branch -d [要删除的分支名]
```

**因为创建、合并和删除分支非常快，所以Git鼓励你使用分支完成某个任务，合并后再删掉分支，这和直接在`master`分支上工作效果是一样的，但过程更安全**

### 分支合并与冲突

> Git的冲突检测单位是文件，即当不同分支对同一个文件进行修改后进行合并，就会产生冲突
>
> 解决冲突就是把Git合并失败的文件手动编辑为我们希望的内容，再提交。

**冲突将导致 merge 指令失败，`git status`也可以告诉我们冲突的文件**

一、解决冲突

方法就是手工修改文件的冲突内容，然后add添加并commit 提交

```bash
vim [filename] #Linux下
```

二、查看冲突日志

```bash
git log --pretty=oneline --graph
git log --pretty=oneline --graph --abbrev-commit #缩写的commit-id
```

三、解决冲突并合并成功后删除分支

```bash
#先切换到master分支后再删除dev分支
git checkout master
git branch -d branchName
```

## 八、Git标签:方便人类记忆版本

> A: 请把上周一的那个版本打包发布，commit号是6a5819e...
> B: 一串乱七八糟的数字不好找！
> 如果换一个办法：
> A: 请把上周一的那个版本打包发布，版本号是v1.2
> B: 好的，按照tag v1.2查找commit就行！
>
> 
>
> **所以，tag就是一个让人容易记住的有意义的名字，它跟某个commit绑在一起**



**标签的定义**

发布一个版本时，我们通常先在版本库中打一个标签（tag），这样，就唯一确定了打标签时刻的版本。将来无论什么时候，取某个标签的版本，就是把那个打标签的时刻的历史版本取出来。所以，标签也是版本库的一个快照

Git的标签虽然是版本库的快照，但其实它就是指向某个commit的指针（跟分支很像对不对？但是分支可以移动，标签不能移动），所以，创建和删除标签都是瞬间完成的。

### 创建标签

在Git中打标签非常简单，首先，切换到需要打标签的分支上 

```
$ git branch
* dev
  master
$ git checkout master
Switched to branch 'master'
```

> 打标签

```
git tag v1.0
```

> 查看所有标签

```bash
git tag
#如果某个Commit忘了打标签，找到历史提交的commit id，然后打上就可以了
git log --pretty=oneline --abbrev-commit
$ git tag v0.9 [commitid]
```

> 创建带有说明的标签，用`-a`指定标签名，`-m`指定说明文字：

```
$ git tag -a [版本号] -m "说明文字" [commitId]
```

### 标签操作

- 命令`git push origin <tagname>`可以推送一个本地标签；
- 命令`git push origin --tags`可以推送全部未推送过的本地标签；
- 命令`git tag -d <tagname>`可以删除一个本地标签；
- 命令`git push origin :refs/tags/<tagname>`可以删除一个远程标签。

**注意：标签总是和某个commit挂钩。如果这个commit既出现在master分支，又出现在dev分支，那么在这两个分支上都可以看到这个标签**

## 附件：Git Cheat Sheet

[Git Cheat Sheet下载链接](https://education.github.com/git-cheat-sheet-education.pdf)

