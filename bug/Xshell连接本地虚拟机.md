



1. vmware配置
   + 配置vmnet8
     + ip
     + netmask
     + dns
2. centos配置
   + 安装正经的centos镜像
   + vim /etc/sysconfig/network-config/ifcfg-ens33

```
TYPE=Ethernet
PROXY_METHOD=none
BROWSER_ONLY=no
#
BOOTPROTO=static
#
IPADDR=192.168.103.100
NETMASK=255.255.255.0
GATEWAY=192.168.103.2
#
DNS=114.114.114.114
PREFIX=24
#
DEFROUTE=yes
IPV4_FAILURE_FATAL=no
IPV6INIT=yes
IPV6_AUTOCONF=yes
IPV6_DEFROUTE=yes
IPV6_FAILURE_FATAL=no
IPV6_ADDR_GEN_MODE=stable-privacy
NAME=ens33
UUID=f5410a91-7aec-4639-8bb4-377c33518a22
DEVICE=ens33
ONBOOT=yes
PREFIX=24
```







检查/`etc//etc/sysconfig/network-scripts`目录下配置文件

```
[loki@localhost network-scripts]$ ls
ifcfg-lo                  ifdown-isdn      ifdown-tunnel  ifup-isdn    ifup-Team
ifcfg-Wired_connection_1  ifdown-post      ifup           ifup-plip    ifup-TeamPort
ifdown                    ifdown-ppp       ifup-aliases   ifup-plusb   ifup-tunnel
ifdown-bnep               ifdown-routes    ifup-bnep      ifup-post    ifup-wireless
ifdown-eth                ifdown-sit       ifup-eth       ifup-ppp     init.ipv6-global
ifdown-ippp               ifdown-Team      ifup-ippp      ifup-routes  network-functions
ifdown-ipv6               ifdown-TeamPort  ifup-ipv6      ifup-sit     network-functions-ipv6
```

发现没有 ifcfg-【网卡名】，只有 ifcfg-Wired_connection_1

+ 解决：[centos7 网卡配置错误问题解决办法（ifcfg-Wired_connection_1）](https://blog.51cto.com/u_2221384/2543301)

  ```
  systemctl restart NetworkManager.service
  ```



+ ifconfig查看网卡信息

```
[loki@localhost network-scripts]$ ifconfig
eno16777736: flags=4163<UP,BROADCAST,RUNNING,MULTICAST>  mtu 1500
        inet 192.138.103.100  netmask 255.255.255.0  broadcast 192.138.103.255
        inet6 fe80::20c:29ff:fe25:f551  prefixlen 64  scopeid 0x20<link>
        ether 00:0c:29:25:f5:51  txqueuelen 1000  (Ethernet)
        RX packets 882651  bytes 1291690382 (1.2 GiB)
        RX errors 0  dropped 0  overruns 0  frame 0
        TX packets 164665  bytes 10348836 (9.8 MiB)
        TX errors 0  dropped 0 overruns 0  carrier 0  collisions 0

lo: flags=73<UP,LOOPBACK,RUNNING>  mtu 65536
        inet 127.0.0.1  netmask 255.0.0.0
        inet6 ::1  prefixlen 128  scopeid 0x10<host>
        loop  txqueuelen 0  (Local Loopback)
        RX packets 502  bytes 60904 (59.4 KiB)
        RX errors 0  dropped 0  overruns 0  frame 0
        TX packets 502  bytes 60904 (59.4 KiB)
        TX errors 0  dropped 0 overruns 0  carrier 0  collisions 0
```

+ 关于网卡 `eno16777736`

> Linux 操作系统的网卡设备的传统命名方式是 eth0、eth1、eth2等，而 CentOS7 提供了不同的命名规则，默认是基于固件、拓扑、位置信息来分配。这样做的优点是命名全自动的、可预知的，缺点是比 eth0、wlan0 更难读，比如 ens33 。
>
> 如果不习惯新的命令规则，可以恢复使用传统的方式命名。

