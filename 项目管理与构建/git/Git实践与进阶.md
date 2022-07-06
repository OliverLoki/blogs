## Git版本回退的一些问题

> 很多人对git不熟悉,对git clean,git checkout 有啥好怕?都是操作缓冲区的命令,又不影响仓库里面的东西,如果这些都不懂,那么用到git revert 和git reset 不是抓瞎了?git 是分部式的,就是本地完蛋了,还能从远程仓库拉,再说了,git 还有git log ,怕什么?就怕有些人乱用git reset再用上git checkout和git clean ,再说了,就是git reset,只要没有git push --force 没啥好怕的,就怕菜鸟每次不解冲突,直接git push --force 坑所有人
>
> 加分支权限，main分支没有权限push，只能request merge，审核通过才可以

## Git忽略规则.gitignore不生效

> **简介：** 在项目开发过程中个，一般都会添加 .gitignore 文件，规则很简单，但有时会发现，规则不生效。 原因是 .gitignore 只能忽略那些原来没有被track的文件，如果某些文件已经被纳入了版本管理中，则修改.gitignore是无效的。

在项目开发过程中个，一般都会添加 .gitignore 文件，规则很简单，但有时会发现，规则不生效。
原因是 .gitignore 只能忽略那些原来没有被track的文件，如果某些文件已经被纳入了版本管理中，则修改.gitignore是无效的。
那么解决方法就是先把本地缓存删除（改变成未track状态），然后再提交。

```
git rm -r --cached .

git add .

git commit -m 'update .gitignore'
```

## Git撤销提交



## GitHub提交代码无contributions记录解决

> 问题：最近在使用GigHub时，发现提交的记录并没有统计在GitHub首页的Contributions Graph里（即贡献图上没有绿块）
>

> 原因：经过查资料发现，是因为提交时填写的邮箱与GitHub账号里的邮箱不一致导致，而GitHub是以邮箱关联GitHub账号的

> 解决：

```
#查看全局用户配置信息
git config --global --list
```

如果和Github预留的用户名不同，则不记录贡献，需要修改

```
#用户名配置
git config --global user.name "你的用户名"
git config --global user.email "你的邮箱地址"
```

















