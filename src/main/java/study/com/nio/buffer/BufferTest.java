package study.com.nio.buffer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.IntBuffer;

/**
 * Created by runlibo.li on 2018/1/30.
 *
 * @author runlibo.li
 */
public class BufferTest {

    private static void testProperties() {
        CharBuffer charBuffer = CharBuffer.allocate(10);
        showBuffer(charBuffer);

        charBuffer.put("abc");
        showBuffer(charBuffer);

        //flip在写入后读取buffer之前进行调用，它是将limit设成写状态下的position，position设为0
        charBuffer.flip();
        showBuffer(charBuffer);

        char c = charBuffer.get();
        showBuffer(charBuffer);

        //clear是将position设为0，limit设置成capacity的大小
        charBuffer.clear();
        showBuffer(charBuffer);

        charBuffer.position(1);
        showBuffer(charBuffer);
    }

    private static void testCompact() {
        CharBuffer charBuffer = CharBuffer.allocate(10);
        charBuffer.put("def");
        showBuffer(charBuffer);

        charBuffer.flip();
        showBuffer(charBuffer);

        char[] arr = new char[2];
        charBuffer.get(arr);
        showBuffer(charBuffer);

        //compact将读取过程中还没有读到的那部分内容挪到buffer的头部，这时position是没有读到内容的长度大小，
        // limit成了capacity的大小，便于后续的写入，可以认为buffer是写状态
        charBuffer.compact();
        charBuffer.put("ghlmn");
        charBuffer.flip();
        showBuffer(charBuffer);
    }

    private static void testDuplicate() {
        CharBuffer charBuffer = CharBuffer.allocate(10);
        charBuffer.put("abcde");
        //duplicate用于复制缓冲区，最终两个缓冲区实际上指向的是同一个内部数组，只是分别管理各自的属性
        CharBuffer charBuffer1 = charBuffer.duplicate();
        charBuffer1.clear();
        charBuffer1.put("other1");

        showBuffer(charBuffer);
        showBuffer(charBuffer1);
    }


    private static void testElementView() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(12);
        byteBuffer.put(new byte[]{0x00, 0x00, 0x00, 0x42});
        byteBuffer.position(0);
        //转换成视图缓冲区，是以position开头，limit结尾来创建新的视图缓冲区的
        IntBuffer intBuffer = byteBuffer.asIntBuffer();

        int i = intBuffer.get();
        System.out.println(i);
        System.out.println(Integer.toHexString(i));
    }


    private static void testPutAndGetElement() {
        ByteBuffer buffer = ByteBuffer.allocate(12);
        buffer.putInt(0x1234abcd);
        buffer.position(0);
        byte b1 = buffer.get();
        byte b2 = buffer.get();
        byte b3 = buffer.get();
        byte b4 = buffer.get();

        //java中默认是以大端的方式来存放元素的
        System.out.println(Integer.toHexString(b1 & 0xff));
        System.out.println(Integer.toHexString(b2 & 0xff));
        System.out.println(Integer.toHexString(b3 & 0xff));
        System.out.println(Integer.toHexString(b4 & 0xff));

        buffer.position(0);
        int bigEndian = buffer.getInt();
        System.out.println(Integer.toHexString(bigEndian));

        buffer.rewind();
        int littleEndian = buffer.order(ByteOrder.LITTLE_ENDIAN).getInt();
        System.out.println(Integer.toHexString(littleEndian));
    }


    private static void showBuffer(CharBuffer buffer) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < buffer.limit(); ++i) {
            char c = buffer.get(i);
            if (c == 0) {
                c = '.';
            }
            sb.append(c);
        }
        System.out.printf("position=%d, limit=%d, capacity=%d, content=%s\n",
                buffer.position(), buffer.limit(), buffer.capacity(), sb.toString());
    }


    public static void main(String[] args) {
        testProperties();
        testCompact();
        testDuplicate();
        testElementView();
        testPutAndGetElement();
    }

}
