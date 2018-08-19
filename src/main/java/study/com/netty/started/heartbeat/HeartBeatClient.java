package study.com.netty.started.heartbeat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.HashedWheelTimer;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * Created by runlibo.li on 2018/8/19.
 *
 * @author runlibo.li
 */
@Slf4j
public class HeartBeatClient {

    private final HashedWheelTimer timer = new HashedWheelTimer();
    private final ConnectorIdleStateTrigger idleStateTrigger = new ConnectorIdleStateTrigger();

    public void connect(String host, int port) {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap().group(eventLoopGroup).channel(NioSocketChannel.class);
        ConnectionWatchdog watchdog = new ConnectionWatchdog(bootstrap, host, port, timer, true, 20) {
            @Override
            public ChannelHandler[] handlers() {
                return new ChannelHandler[]{
                        this,
                        new IdleStateHandler(0, 1, 0, TimeUnit.SECONDS),
                        idleStateTrigger
                };
            }
        };

        synchronized (bootstrap) {
            bootstrap.handler(new ChannelInitializer<Channel>() {
                @Override
                protected void initChannel(Channel ch) throws Exception {
                    ch.pipeline().addLast(watchdog.handlers());
                }
            });
        }
        try {
            Channel channel = bootstrap.connect(host, port).sync().channel();
            log.info("client:{} connect server:{}", channel.localAddress(), channel.remoteAddress());
        } catch (Exception e) {
            log.error("connect server error, ", e);
        }
    }


    public static void main(String[] args) {
        new HeartBeatClient().connect("127.0.0.1", 9998);
    }


}
