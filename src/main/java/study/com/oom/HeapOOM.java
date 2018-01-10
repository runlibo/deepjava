package study.com.oom;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by runlibo.li on 2018/1/9.
 *
 * @author runlibo.li
 */
public class HeapOOM {

    static class OOMObject {
    }

    /**
     * -Xms10M -Xmx10M -XX:+HeapDumpOnOutOfMemoryError
     */
    public static void main(String[] args) {
        List<OOMObject> list = Lists.newArrayList();
        while (true) {
            list.add(new OOMObject());
        }
    }
}
