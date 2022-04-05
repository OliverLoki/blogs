# JavaSE之IO流

## File类

### File类的介绍

File:文件和目录路径名的抽象表示形式

+ 文件和目录是可以通过File封装成对象的
+ 对于File而言，其封装的并不是一个真正存在的文件，只是一个路径名而已。

### File类的构造方法

![image-20210823000339274](https://i.loli.net/2021/08/23/lDzfp8nsvXEYey6.png)

### File类创建文件的方法

```java
File file = new File("E://test.txt");
file.creatNewFile();
//在指定目录创建一个文件，创建成功返回true，若指定目录不存在，会抛出IO异常
```

```java
File file = new File("E://testDir");
file.mkdir();
//创建一个目录，若已存在，返回false，该方法不能自动创建多级目录
```

```java
File file = new File("E://testDir//sec");
file.mkdirs();
//若需要创建多级目录，则需要这个方法
```

### File类判断和获取功能

![image-20210823232823821](https://i.loli.net/2021/08/23/TOaJzub2cUgfQsm.png)

### File类的删除功能

```java
public boolean delete();
//删除此抽象路径名表示的文件或者目录
```



### 遍历目录（A subject）

> 需求:给定一个路径(E: //kitcat),请通过递归完成遍历该目录下的所有内容,并把所有文件的绝对路径输出在控制台



## IO流

`InputOutputStream`

**流的分类**

+ 字节流
  + 字节输入流
  + 字节输出流
+ 字符流
  + 字符输入流
  + 字符输出流

**如何判断使用哪种流**

+ 如果数据通过Window自带的记事本软件打开，我们还可以读懂里面的内容，就使用字符流，否则使用字节流。

+ 如果你不知道该使用哪种类型的流，就使用字节流

### 字节流

**字节流抽象基类**

+ InputStream 所有字节输入流类的超类
+ OutputStream 所有字节输出流类的超类

#### OutputStream--输出数据

`FileOutputStream`--文件输出流--将数据写入File

+ 构造方法

  ![image-20210824181536755](https://i.loli.net/2021/08/24/ez7xOMTiuoHAaqw.png)

  常用构造：`FileOutputStream(String name)`

```java
FileOutputStream fos = new FileOutputStream("code_practice//test.txt");
```

​		这行代码做了三件事：

​			1、调用系统功能创建了文件
​			2、创建了字节输出流对象
​			3、让字节输出流对象指向创建好的文件

> 注意：所有与IO有关的操作最后都要释放系统资源

+ 释放资源

  ```java
  void close(); 关闭流并释放与此流相关的所有系统资源
  ```

+ FileOutputStream写入数据的三种方法

```java
void write(int b) //将指定的字节写入此文件输入流
void write(byte[] b) //将b.length字节从指定的字节数组写入此文件输出流,一次写一个字节数组数据
void write(byte[] b,int off,int len) //将len字节从指定的字节数组开始，从偏移量off开始写入此文件输出流,一次写一个字节数组的部分数据
```

+ **字节流写数据实现换行** 

```java
@Test
    public void func2() throws IOException{
        FileOutputStream fos = new FileOutputStream("改为你的测试绝对路径");
        for (int i = 0; i < 10; i++) {
           fos.write("abcde".getBytes());
           fos.write("\r\n".getBytes());
        }
        fos.close();
    }
```



+ 字节流写数据实现追加写入

```java
FileOutputStream(Strign name,boolean append) 
    //如果第二个参数为true ，则字节将写入文件的末尾而不是开头
```

#### InputStream--获取数据

`FileInputStream`:从文件系统的文件中获取数据

构造方法

![image-20210830123800181](https://i.loli.net/2021/08/30/bBxuiPtoE6QGUAN.png)

构造方法做了三件事：

​			1、调用系统功能创建了文件
​			2、创建了字节输出流对象
​			3、让字节输出流对象指向创建好的文件

> 字节流读数据会读取到换行符
>
> 不同操作系统的换行符有所不同：
>
> windows: `/r/n`
>
> linux: `/n`
>
> mac: `/r`

**读取数据的方法**

```java
int read();
//从该输入流中读取一个字节的数据
```

```java
int read(Byte[] b);
//从输入流中获取字节数组的全部数据
```

```java
int read(Byte[] b,int off,int len);
//从数据流中读取len长度字节个数据
```



**字节流读数据参考代码**

> 需求：将test.txt中的内容的读取出来在控制台输出

```java
public class 字节流读数据 {
    public static void main(String[] args) throws IOException {
        File file = new File("E:\\学习代码\\java_study\\javaSE\\practicecode\\code_practice\\test.txt");
        FileInputStream fis = new FileInputStream(file);
        //字节流读取数据操作
        int by;
        while ((by=fis.read())!=-1){
            System.out.print((char)by);
        }
    }
}
```



### 字符流



### 特殊操作流