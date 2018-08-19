package study.com.netty.started.heartbeat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.util.Timeout;
import io.netty.util.Timer;
import io.netty.util.TimerTask;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * Created by runlibo.li on 2018/8/17.
 * <p>
 * 重连检测狗，当发现当前链路关闭之后，进行重连
 *
 * @author runlibo.li
 */
@Slf4j
@ChannelHandler.Sharable
public abstract class ConnectionWatchdog extends ChannelInboundHandlerAdapter implements TimerTask, ChannelHandlerHolder {

    private final Bootstrap bootstrap;
    private final String host;
    private final int port;
    private final Timer timer;
    private final int retryTimes;
    private volatile boolean reconnect = true;
    private int currentTimes = 0;


    public ConnectionWatchdog(Bootstrap bootstrap, String host, int port, Timer timer, boolean reconnect, int retryTimes) {
        this.bootstrap = bootstrap;
        this.host = host;
        this.port = port;
        this.timer = timer;
        this.reconnect = reconnect;
        this.retryTimes = retryTimes;
    }


    /**
     * channel链路active的时候，将其重试次数置为0
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        if (currentTimes != 0) {
            log.info("reconnect {} success, retryTimes:{}", ctx.channel().remoteAddress(), currentTimes);
        }
        currentTimes = 0;
        ctx.fireChannelActive();
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("server {} inactive", ctx.channel().remoteAddress());
        if (reconnect) {
            if (currentTimes < retryTimes) {
                currentTimes++;
                //重连的时间间隔会越来越长
                int timeout = 2 << currentTimes;
                timer.newTimeout(this, timeout, TimeUnit.MILLISECONDS);
            }
        }

        super.channelInactive(ctx);
    }

    @Override
    public void run(Timeout timeout) throws Exception {
        ChannelFuture future = null;
        //这里应该主要为了同步变量，并没有并发的问题
        //bootstrap已经初始化好了，只需要将handler填入
        synchronized (bootstrap) {
            bootstrap.handler(new ChannelInitializer<Channel>() {
                @Override
                protected void initChannel(Channel ch) throws Exception {
                    ch.pipeline().addLast(handlers());
                }
            });
            future = bootstrap.connect(host, port);
        }
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    log.info("reconnect {} success", future.channel().remoteAddress());
                } else {
                    log.info("reconnect {} fail", future.channel().remoteAddress());
                    future.channel().pipeline().fireChannelInactive();
                }
            }
        });
    }
}
