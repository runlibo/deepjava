package study.com.nio.socket;


import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.TimeUnit;

/**
 * Created by runlibo.li on 2018/1/31.
 *
 * @author runlibo.li
 */
@Slf4j
public class NonBlockingChannelServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(new InetSocketAddress(7777));

        SocketChannel cli = null;
        while ((cli = serverSocketChannel.accept()) == null) {
            TimeUnit.SECONDS.sleep(1);
            log.info("non-blocking model no connection");
        }
        log.info("accept connection from:{}", cli.getRemoteAddress());

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        while (cli.read(buffer) != -1) {
            buffer.flip();
            byte[] bytes = new byte[buffer.remaining()];
            buffer.get(bytes);
            log.info("{}", new String(bytes));
            buffer.clear();
        }
        cli.close();
        serverSocketChannel.close();
    }

}
