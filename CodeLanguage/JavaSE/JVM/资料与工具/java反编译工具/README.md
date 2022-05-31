# JD-GUI

## 使用教程

https://blog.csdn.net/Jerry_1126/article/details/51419076

对于Java开发人员来说，提供了一大堆第三方jar包，class文件，而没有源代码的话是非常痛苦的，特别是debug调试的时候， 当然你可以通过jad命令来反编译class来获得源码，更简洁的方式是通过JD-GUI的方式来反编译，该工具可以反编译单个、多个，甚至整个jar包，而且是开源，免费的，可谓非常方便.


JD-GUI, a standalone graphical utility that displays Java sources from CLASS files.

![](https://raw.githubusercontent.com/java-decompiler/jd-gui/master/src/website/img/jd-gui.png)

- Java Decompiler projects home page: [http://java-decompiler.github.io](http://java-decompiler.github.io)
- JD-GUI source code: [https://github.com/java-decompiler/jd-gui](https://github.com/java-decompiler/jd-gui)

## Description
JD-GUI is a standalone graphical utility that displays Java source codes of 
".class" files. You can browse the reconstructed source code with the JD-GUI
for instant access to methods and fields.

## How to build JD-GUI ?

```
> git clone https://github.com/java-decompiler/jd-gui.git
> cd jd-gui
> ./gradlew build 
```
generate :
- _"build/libs/jd-gui-x.y.z.jar"_
- _"build/libs/jd-gui-x.y.z-min.jar"_
- _"build/distributions/jd-gui-windows-x.y.z.zip"_
- _"build/distributions/jd-gui-osx-x.y.z.tar"_
- _"build/distributions/jd-gui-x.y.z.deb"_
- _"build/distributions/jd-gui-x.y.z.rpm"_

## How to launch JD-GUI ?
- Double-click on _"jd-gui-x.y.z.jar"_
- Double-click on _"jd-gui.exe"_ application from Windows
- Double-click on _"JD-GUI"_ application from Mac OSX
- Execute _"java -jar jd-gui-x.y.z.jar"_ or _"java -classpath jd-gui-x.y.z.jar org.jd.gui.App"_

## How to use JD-GUI ?
- Open a file with menu "File > Open File..."
- Open recent files with menu "File > Recent Files"
- Drag and drop files from your file explorer

## How to extend JD-GUI ?
```
> ./gradlew idea 
```
generate Idea Intellij project
```
> ./gradlew eclipse
```
generate Eclipse project
```
> java -classpath jd-gui-x.y.z.jar;myextension1.jar;myextension2.jar org.jd.gui.App
```
launch JD-GUI with your extensions

## How to uninstall JD-GUI ?
- Java: Delete "jd-gui-x.y.z.jar" and "jd-gui.cfg".
- Mac OSX: Drag and drop "JD-GUI" application into the trash.
- Windows: Delete "jd-gui.exe" and "jd-gui.cfg".
- //所在路径：C:\Users\yu'chun\AppData\Roaming\jd-gui.cfg

## License
Released under the [GNU GPL v3](LICENSE).

## Donations
Did JD-GUI help you to solve a critical situation? Do you use JD-Eclipse daily? What about making a donation?
