package study.com.netty.started.bytebuf;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.oio.OioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * Created by runlibo.li on 2018/4/4.
 *
 * @author runlibo.li
 */
public class ByteBufTest {

    public static void main(String[] args) {
        EventLoopGroup group = new OioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(group).channel(OioServerSocketChannel.class)
                .localAddress(new InetSocketAddress(8888));

    }
}















