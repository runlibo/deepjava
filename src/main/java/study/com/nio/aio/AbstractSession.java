package study.com.nio.aio;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;

/**
 * Created by runlibo.li on 2018/4/26.
 *
 * @author runlibo.li
 */
@Getter
@Slf4j
public class AbstractSession {

    protected AsynchronousSocketChannel clientChannel;

    protected AsynchronousServerSocketChannel serverChannel;

    AbstractSession(SessionConfig sessionConfig) {
        this.clientChannel = sessionConfig.getClientChannel();
        this.serverChannel = sessionConfig.getServerSocketChannel();
    }

    public void close() {
        try {
            clientChannel.close();
        } catch (IOException e) {
            log.error("ClientChannel close error", e);
        }
    }
}
