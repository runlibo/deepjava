package study.com.nio.socket;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.TimeUnit;

/**
 * Created by runlibo.li on 2018/1/31.
 *
 * @author runlibo.li
 */
@Slf4j
public class NonBlockingChannelClient {

    public static void main(String[] args) throws IOException, InterruptedException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        socketChannel.connect(new InetSocketAddress(7777));
        while (!socketChannel.finishConnect()) {
            log.info("connect has not finished, wait...");
            TimeUnit.SECONDS.sleep(1);
        }

        ReadableByteChannel inputChannel = Channels.newChannel(System.in);
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        while (inputChannel.read(buffer) != -1) {
            buffer.flip();
            while (buffer.hasRemaining()) {
                socketChannel.write(buffer);
            }
            buffer.clear();
        }

        inputChannel.close();
        socketChannel.close();
    }

}
