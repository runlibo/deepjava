package study.com.nio.aio;

import lombok.Builder;
import lombok.Getter;

import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;

/**
 * Created by runlibo.li on 2018/4/26.
 *
 * @author runlibo.li
 */
@Getter
@Builder
public class SessionConfig {

    private AsynchronousServerSocketChannel serverSocketChannel;

    private AsynchronousSocketChannel clientChannel;

}
