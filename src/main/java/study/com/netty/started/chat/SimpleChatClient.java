package study.com.netty.started.chat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by runlibo.li on 2018/8/15.
 *
 * @author runlibo.li
 */
@Slf4j
public class SimpleChatClient {


    public static void main(String[] args) {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast("frame", new LineBasedFrameDecoder(8192));
                            pipeline.addLast("decoder", new StringDecoder());
                            pipeline.addLast("encoder", new StringEncoder());
                            pipeline.addLast("handler", new SimpleChatClientHandler());
                        }
                    });
            Channel channel = bootstrap.connect("127.0.0.1", 9999).sync().channel();
            log.info("client:{} connect server:{}", channel.localAddress(), channel.remoteAddress());
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                channel.writeAndFlush(reader.readLine() + "\n");
            }
        } catch (Exception e) {
            log.error("", e);
        } finally {
            eventLoopGroup.shutdownGracefully();
        }
    }


}
