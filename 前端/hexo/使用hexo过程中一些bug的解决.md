 **一、 执行 `hexo g -d` 操作时会在本地生成 `public` 静态代码和 `.deploy_git` 文件夹。`.deploy_git` 和 `public` 的内容几乎一致，但 `.deploy_git` 多了 GitHub 所需的仓库信息与提交信息。全部解析完后 hexo 会把 `.deploy_git` 文件夹内的全部内容推送到 GitHub 仓库中，再由 Github Pages 服务完成静态网站的解析**





访问路径导致的问题

![image-20210607123114819](D:\桌面\P_picture_cahe\image-20210607123114819.png)

![image-20210607123127432](D:\桌面\P_picture_cahe\image-20210607123127432.png)

![image-20210607123201292](D:\桌面\P_picture_cahe\image-20210607123201292.png)

_post文件夹下

文章名称后面不要有空格，否则会报错

**二、Font-matter属性中的type属性对应themes/layout下的 `.ejs` 文件**

这两个名字要相同，这个问题经常在创建新界面时碰到

