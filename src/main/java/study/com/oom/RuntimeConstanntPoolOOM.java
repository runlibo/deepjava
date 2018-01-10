package study.com.oom;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by runlibo.li on 2018/1/10.
 *
 * @author runlibo.li
 */
public class RuntimeConstanntPoolOOM {

    /**
     * -XX:PermSize=10M  -XX:MaxPermSize=10M
     * 在jdk7之后这两个参数已经不再支持，没有了长久代
     * 对运行时期生成的常量，常量池中也不再存储对象实例，而是存储对象引用，对象实例存储在堆中
     * 可以通过String.intern()方法，JDK1.7考虑下面例子：
     * String str1 = new StringBuilder("计算机").append("软件").toString();
     * System.out.println(str1.intern() == str1);     true
     * String str2 = new String("a");
     * System.out.println(str2.intern() == str2);     false
     * String str3= new String("a") + "b";
     * System.out.println(str3.intern() == str3);     true
     * String str4= new String("ma") + "in";
     * System.out.println(str4.intern() == str4);     false
     * <p>
     * 下面方法当生成常量的大小超过堆的限制时会抛出oom的异常
     */
    public static void main(String[] args) {
        List<String> list = Lists.newArrayList();
        int i = 0;
        while (true) {
            list.add(String.valueOf(i++).intern());
        }
    }
}
