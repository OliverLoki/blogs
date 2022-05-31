package 类加载器;

/**
 * @author oliverloki
 * @Description: TODO
 * @date 2022年04月13日 17:50
 */
public class 获取ClassLoader示例 {
    public static void main(String[] args) {
        //获取系统类加载器
        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        //jdk.internal.loader.ClassLoaders$AppClassLoader@2437c6dc
        System.out.println(systemClassLoader);

        //获取拓展类加载器
        ClassLoader extClassLoader = systemClassLoader.getParent();
        //jdk.internal.loader.ClassLoaders$PlatformClassLoader@58ceff1
        System.out.println(extClassLoader);

        //获取启动类加载器
        ClassLoader bootStrapClassloader = extClassLoader.getParent();
        System.out.println(bootStrapClassloader);//null(Bootstrap ClassLoader)

        //获取自定义类加载器:默认使用系统类加载器进行加载
        ClassLoader classLoader = 获取ClassLoader示例.class.getClassLoader();
        //jdk.internal.loader.ClassLoaders$AppClassLoader@2437c6dc
        System.out.println(classLoader);

    }
}
