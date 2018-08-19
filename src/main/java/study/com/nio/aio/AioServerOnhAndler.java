package study.com.nio.aio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;

/**
 * Created by runlibo.li on 2018/4/23.
 *
 * @author runlibo.li
 */
@Slf4j
public class AioServerOnHandler {

    public static void main(String[] args) throws IOException, InterruptedException {
        try (AsynchronousServerSocketChannel serverSocketChannel = AsynchronousServerSocketChannel.open()) {
            serverSocketChannel.bind(new InetSocketAddress(9999));
            log.info("Waiting for connections");
            serverSocketChannel.accept(serverSocketChannel, new AcceptCompletionHandler());

            Thread.currentThread().join();
        }
    }
}


