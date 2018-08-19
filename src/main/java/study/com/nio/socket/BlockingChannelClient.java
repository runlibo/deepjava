package study.com.nio.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SocketChannel;

/**
 * Created by runlibo.li on 2018/1/31.
 *
 * @author runlibo.li
 */
public class BlockingChannelClient {

    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress(6666));

        ReadableByteChannel inputChannel = Channels.newChannel(System.in);
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        while (inputChannel.read(byteBuffer) != -1) {
            byteBuffer.flip();
            byte[] bytes = new byte[byteBuffer.remaining()];
            byteBuffer.get(bytes);
            if (new String(bytes).startsWith("exit")){
                socketChannel.close();
                break;
            }

            byteBuffer.position(0);
            while (byteBuffer.hasRemaining()) {
                socketChannel.write(byteBuffer);
            }
            byteBuffer.clear();
        }
        inputChannel.close();
        socketChannel.close();
    }
}
