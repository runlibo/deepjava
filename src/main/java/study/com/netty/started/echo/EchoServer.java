package study.com.netty.started.echo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by runlibo.li on 2018/6/24.
 *
 * @author runlibo.li
 */
@Slf4j
public class EchoServer {

    public static void main(String[] args) {
        NioEventLoopGroup group = new NioEventLoopGroup();
        EchoServerHandler serverHandler = new EchoServerHandler();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(group)
                .channel(NioServerSocketChannel.class)
                .localAddress(9999)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(serverHandler);
                    }
                });
        try {
            //sync方法阻塞等待直到操作完成，和await方法的区别是它会抛出异常
            ChannelFuture future = serverBootstrap.bind().sync();
            //获取到closeFuture对象，阻塞当前线程直到它完成
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error("", e);
        } finally {
            group.shutdownGracefully();
        }
    }
}
