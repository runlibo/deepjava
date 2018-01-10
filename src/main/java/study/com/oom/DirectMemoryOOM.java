package study.com.oom;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * Created by runlibo.li on 2018/1/10.
 *
 * @author runlibo.li
 */
public class DirectMemoryOOM {
    private static final int MB = 1 * 1024 * 1024;

    /**
     * -XX:MaxDirectMemorySize=10M
     */
    public static void main(String[] args) throws IllegalAccessException {
        Field field = Unsafe.class.getDeclaredFields()[0];
        field.setAccessible(true);
        Unsafe unsafe = (Unsafe) field.get(null);
        while (true) {
            unsafe.allocateMemory(MB);
        }
    }
}
