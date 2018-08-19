package study.com.nio.socket;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * Created by runlibo.li on 2018/2/1.
 *
 * @author runlibo.li
 */
@Slf4j
public class SelectorServer {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = null;
        Selector selector = null;

        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(9999));
            serverSocketChannel.configureBlocking(false);

            selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            while (selector.select() != 0) {
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    if (selectionKey.isConnectable()) {
                        log.info("Connectable");
                    } else if (selectionKey.isAcceptable()) {
                        log.info("Acceptable");
                        handleAccept(selectionKey);
                    } else if (selectionKey.isReadable()) {
                        log.info("Readable");
                        handleRead(selectionKey);
                    } else if (selectionKey.isWritable()) {
                        log.info("Writable");
                        handleWrite(selectionKey);
                    }

                    iterator.remove();
                }
            }
        } catch (IOException e) {
            log.error("", e);
            throw e;
        } finally {
            if (serverSocketChannel != null) {
                serverSocketChannel.close();
            }

            if (selector != null) {
                selector.close();
            }
        }
    }


    private static void handleAccept(SelectionKey key) throws IOException {
        SocketChannel clientChannel = null;
        try {
            clientChannel = ((ServerSocketChannel) key.channel()).accept();
            clientChannel.configureBlocking(false);
            clientChannel.register(key.selector(), SelectionKey.OP_READ, ByteBuffer.allocate(1024));
        } catch (IOException e) {
            log.error("", e);
            key.cancel();
            if (clientChannel != null) {
                clientChannel.close();
            }
        }
    }

    private static void handleRead(SelectionKey key) throws IOException {
        SocketChannel clientChannel = (SocketChannel) key.channel();
        try {
            ByteBuffer buffer = (ByteBuffer) key.attachment();
            if (clientChannel.read(buffer) != -1) {
                buffer.flip();
                byte[] bytes = new byte[buffer.remaining()];
                buffer.get(bytes);

                log.info("server receive: {}", new String(bytes, "UTF-8"));
                buffer.rewind();
                key.interestOps(SelectionKey.OP_WRITE);
            } else {
                clientChannel.close();
                key.cancel();
            }
        } catch (IOException e) {
            log.error("", e);
            key.cancel();
            clientChannel.close();
        }
    }

    private static void handleWrite(SelectionKey key) throws IOException {
        SocketChannel clientChannel = (SocketChannel) key.channel();
        ByteBuffer header = ByteBuffer.allocate(4);
        header.putInt(0);
        header.flip();
        ByteBuffer body = (ByteBuffer) key.attachment();
        ByteBuffer[] content = new ByteBuffer[]{header, body};

        try {
            clientChannel.write(content);
        } catch (IOException e) {
            log.error("", e);
            key.cancel();
            clientChannel.close();
        }

        body.clear();
        key.interestOps(SelectionKey.OP_READ);
    }

}
