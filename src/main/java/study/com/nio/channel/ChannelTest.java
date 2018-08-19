package study.com.nio.channel;


import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.*;
/**
 * Created by runlibo.li on 2018/1/24.
 *
 * @author runlibo.li
 */
public class ChannelTest {

    private static void testNewChannel() throws IOException {
        ReadableByteChannel readableByteChannel = Channels.newChannel(System.in);
        WritableByteChannel writableByteChannel = Channels.newChannel(System.out);
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        //注意这里buffer的remaining为0的时候，read会返回0
        while (readableByteChannel.read(byteBuffer) != -1) {
            byteBuffer.flip();
            while (byteBuffer.hasRemaining()) {
                writableByteChannel.write(byteBuffer);
            }
            //在使用完buffer需要再次使用时应该清除读取的状态信息
            byteBuffer.clear();
        }
    }


    private static void testFileChannelCopy() throws IOException {
        String pathSrc = "./test.txt";
        String pathDes = "./copy.txt";

        RandomAccessFile source = new RandomAccessFile(pathSrc, "r");
        RandomAccessFile dest = new RandomAccessFile(pathDes, "rw");

        FileChannel srcChannel = source.getChannel();
        FileChannel desChannel = dest.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        while (srcChannel.read(buffer) != -1) {
            buffer.flip();
            while (buffer.hasRemaining()) {
                desChannel.write(buffer);
            }
            buffer.clear();
        }
        srcChannel.close();
        desChannel.close();
    }


    public static void main(String[] args) throws IOException {
        //testNewChannel();
        testFileChannelCopy();
    }
}
