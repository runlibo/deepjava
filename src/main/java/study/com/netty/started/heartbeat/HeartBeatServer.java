package study.com.netty.started.heartbeat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * Created by runlibo.li on 2018/8/16.
 *
 * @author runlibo.li
 */
@Slf4j
public class HeartBeatServer {

    public static void main(String[] args) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            AcceptorIdleStateTrigger idleStateTrigger = new AcceptorIdleStateTrigger();
            HeartBeatServerHandler serverHandler = new HeartBeatServerHandler();
            ServerBootstrap bootstrap = new ServerBootstrap()
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast("idle", new IdleStateHandler(2, 0, 0, TimeUnit.SECONDS));
                            pipeline.addLast("idleState", idleStateTrigger);
                            pipeline.addLast("decode", new StringDecoder());
                            pipeline.addLast("encode", new StringEncoder());
                            pipeline.addLast("heartbeat", serverHandler);
                        }
                    })
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            log.info("HeartBeatServer启动");
            Channel channel = bootstrap.bind(9998).sync().channel();
            channel.closeFuture().sync();
        } catch (Exception e) {
            log.error("", e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            log.info("HeartBeatServer关闭");
        }
    }


}
