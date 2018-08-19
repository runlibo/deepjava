package study.com.netty.started.chat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by runlibo.li on 2018/8/14.
 *
 * @author runlibo.li
 */
@Slf4j
public class SimpleChatServer {

    public static void main(String[] args) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //接收到一个连接之后初始化pipeline  初始化完成之后会注册channel，然后变成active状态
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast("framer", new LineBasedFrameDecoder(8192));
                            pipeline.addLast("decode", new StringDecoder());
                            pipeline.addLast("encode", new StringEncoder());
                            pipeline.addLast("handler", new SimpleChatServerHandler());
                        }
                    })
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            log.info("SimpleChatServer启动");
            ChannelFuture future = bootstrap.bind(9999).sync();

            future.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error("", e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            log.info("SimpleChatServer关闭");
        }
    }
}
