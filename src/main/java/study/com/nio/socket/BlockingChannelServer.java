package study.com.nio.socket;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * Created by runlibo.li on 2018/1/31.
 *
 * @author runlibo.li
 */
@Slf4j
public class BlockingChannelServer {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(6666));
        SocketChannel cli = serverSocketChannel.accept();

        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        while (cli.read(byteBuffer) != -1) {
            byteBuffer.flip();
            byte[] bytes = new byte[byteBuffer.remaining()];
            byteBuffer.get(bytes);
            log.info(new String(bytes));
            byteBuffer.clear();
        }

        serverSocketChannel.close();
        cli.close();
    }
}
