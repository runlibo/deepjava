package study.com.oom;


/**
 * Created by runlibo.li on 2018/1/10.
 *
 * @author runlibo.li
 */
public class StackOOM {

    private Long stackLength = 1L;

    public void stackLeak() {
        ++stackLength;
        stackLeak();
    }


    /**
     * -Xss128k
     */
    public static void main(String[] args) {
        StackOOM stackOOM = new StackOOM();
        stackOOM.stackLeak();
    }
}
