

## ARP协议简介

ARP协议是 `Address Resolution Protocol`（地址解析协议）的缩写，是一个位于TCP/IP协议栈中的底层协议，负责将某个IP地址解析成对应的MAC地址

> **作用:将已知IP地址转换为MAC地址**

+ 在以太网环境中，数据的传输所依懒的是MAC地址而非IP地址

+ 在局域网中，网络中实际传输的是`帧`，帧里面是有目标主机的MAC地址的。在以太网中，一个主机和另一个主机进行直接通信，必须要知道目标主机的MAC地址。但这个目标MAC地址是如何获得的呢？它就是通过地址解析协议获得的。所谓`地址解析`就是主机在发送帧前将目标IP地址转换成目标MAC地址的过程。**ARP协议的基本功能就是通过目标设备的IP地址，查询目标设备的MAC地址，以保证通信的顺利进行**

## ARP实现原理以及流程

在任何时候，一台主机有IP数据报文发送给另一台主机，它都要知道接收方的逻辑（IP）地址。但是IP地址必须封装成帧才能通过物理网络。这就意味着发送方必须有接收方的物理（MAC）地址，因此需要完成逻辑地址到物理地址的映射。而ARP协议可以接收来自IP协议的逻辑地址，将其映射为相应的物理地址，然后把物理地址递交给数据链路层

> ARP请求

任何时候，当主机需要找出这个网络中的另一个主机的物理地址时，它就可以发送一个ARP请求报文，这个报文包好了发送方的MAC地址和IP地址以及接收方的IP地址。因为发送方不知道接收方的物理地址，所以这个查询分组会在网络层中进行广播



> ARP响应

局域网中的每一台主机都会接受并处理这个ARP请求报文，然后进行验证，查看接收方的IP地址是不是自己的地址，只有验证成功的主机才会返回一个ARP响应报文，这个响应报文包含接收方的IP地址和物理地址。这个报文利用收到的ARP请求报文中的请求方物理地址以单播的方式直接发送给ARP请求报文的请求方

## ARP攻击的主要原理和过程

> 本实验的主要目的是在局域网内，实施ARP攻击，使局域网的网关失去作用，导致局域网内部主机无法与外网通信

准备工作

1、下载 WinArpAttacker



在[Hacking Software Download Center ](https://www.bookofnetwork.com/2576/download/Download-WinArpAttacker-software-for-PC-free)下载

这需要WinPacp的支持，如果你的系统是win10或者更高版本，会出现以下问题

```
--------------------------- WinPcap 4.1.3 Setup --------------------------- This version of Windows is not supported by WinPcap 4.1.3. The installation will be aborted. --------------------------- OK ---------------------------
```

解决方法: [下载Win10pcap](http://www.win10pcap.org/)

详情请参考issue：[installing-winpcap-on-windows-10](https://stackoverflow.com/questions/45502972/installing-winpcap-on-windows-10)






