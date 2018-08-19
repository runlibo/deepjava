package study.com.netty.started.heartbeat;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;

/**
 * Created by runlibo.li on 2018/8/19.
 *
 * @author runlibo.li
 */
@ChannelHandler.Sharable
public class ConnectorIdleStateTrigger extends ChannelInboundHandlerAdapter {

    private static final ByteBuf HEALTH_CHECK = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("healthcheck", CharsetUtil.UTF_8));

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState idleState = ((IdleStateEvent) evt).state();
            if (idleState == IdleState.WRITER_IDLE) {
                ctx.writeAndFlush(HEALTH_CHECK.duplicate());
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
