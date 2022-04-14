package java.lang;

/**
 * @author oliverloki
 * @Description: TODO
 * @date 2022年04月14日 13:33
 */
public class String {
    static {
        System.out.println("这这个类会不会被创建");
    }

    public static void main(String[] args) {
        //报错
        //java: 程序包已存在于另一个模块中: java.base
    }
}
