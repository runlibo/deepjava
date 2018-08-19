package study.com.nio.aio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * Created by runlibo.li on 2018/4/24.
 *
 * @author runlibo.li
 */
@Slf4j
public final class AcceptCompletionHandler implements CompletionHandler<AsynchronousSocketChannel, AsynchronousServerSocketChannel> {

    @Override
    public void completed(AsynchronousSocketChannel client, AsynchronousServerSocketChannel serverSocketChannel) {
        try {
            log.info("Incoming connection from:{}", client.getRemoteAddress());
            SessionConfig sessionConfig = SessionConfig.builder()
                    .serverSocketChannel(serverSocketChannel)
                    .clientChannel(client).build();
            AioReadSession session = new AioReadSession(sessionConfig, 1024);
            session.start();
        } catch (IOException e) {
            log.error("Accept error", e);
        } finally {
            pendingAccept(serverSocketChannel);
        }
    }


    @Override
    public void failed(Throwable exc, AsynchronousServerSocketChannel attachment) {
        log.error("Accept fail", exc);
        pendingAccept(attachment);
    }


    private void pendingAccept(AsynchronousServerSocketChannel serverSocketChannel) {
        if (serverSocketChannel.isOpen()) {
            serverSocketChannel.accept(serverSocketChannel, new AcceptCompletionHandler());
        } else {
            throw new IllegalStateException("connection has been closed");
        }
    }
}
