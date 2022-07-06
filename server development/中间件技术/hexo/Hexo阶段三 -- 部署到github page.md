## 将Hexo部署到github



### 1、在本地生成SSH公钥并绑定到github

> 务必确保在 **本地PC** 已经完成了Node.js、Git 和 Hexo 的安装，打开 Git Bash ，如果是第一次使使用 Git 的话

1. 注册自己的github账号

2. 生成本地的SSH公钥，并且绑定到远端的github，实现免密码登录（这一步很重要否则每次提交都需要输入密码）

   ```bash
   #进入C:\Users\yu'chun\.ssh目录
   #生成公钥
   ssh-keygen
   ```

![image-20210603163230769.png](https://img-blog.csdnimg.cn/img_convert/27d26e9e4216c2f6aa790b6bf98748f1.png)

3. 将密钥添加至github/jitee即可添加成功

   ![image-20210603190555558.png](https://img-blog.csdnimg.cn/img_convert/36d727ee9b499666a0e1cfbab44960ca.png)

### 2、新建一个仓库

![image-20210605221301099](https://i.loli.net/2021/06/05/ZthufrCBlF4N1qd.png)

修改hexo主配置文件

找到 hexo 的配置文件 `_config.yml` 。在最下面有个 `deploy` 的配置，注意

```yaml
# Deployment
## Docs: https://hexo.io/docs/one-command-deployment
deploy:
  type: git
  repo: https://github.com/OliverLoki/OliverLoki.github.io.git 
  branch: main
```

> repo的值复制仓库的HTTPS地址即可
>
> ![image-20210605220138020](https://i.loli.net/2021/06/05/tvmL8EIhxyfwFJV.png)

### 3、使用hexo -d向git提交部署

在博客根目录打开Git Bash，输入以下命令

```
hexo d
```

> 当执行 `hexo deploy` 时，Hexo 会将 `public` 目录中的文件和目录推送至 `_config.yml` 中指定的远端仓库和分支中，并且**完全覆盖**该分支下的已有内容。
>
> 简单来说该指令集成了git的一部分操作

可以看到目录下public文件夹中的内容已被上传至github

![image-20210605220800437](https://i.loli.net/2021/06/05/r5dgPsKGHWYpLaV.png)

**此时访问仓库名即可访问到你的个人博客**

我的博客地址 ：

![image-20210605221626581](https://i.loli.net/2021/06/05/6zFYseiRq3JmWvL.png)







> 完结撒花，路漫漫其修远兮