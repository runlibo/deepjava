package study.com.nio.socket;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * Created by runlibo.li on 2018/2/1.
 *
 * @author runlibo.li
 */
@Slf4j
public class SelectorClient {

    public static void main(String[] args) throws IOException, InterruptedException {
        SocketChannel socketChannel = null;
        Scanner scanner = new Scanner(System.in);

        try {
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            socketChannel.connect(new InetSocketAddress(9999));
            while (!socketChannel.finishConnect()) {
                log.info("connect has not finished, wait...");
                TimeUnit.SECONDS.sleep(1);
            }

            ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
            ByteBuffer header = ByteBuffer.allocate(4);
            ByteBuffer body = ByteBuffer.allocate(1024);

            ByteBuffer[] readBuffer = new ByteBuffer[]{header, body};
            while (true) {
                String inputStr = scanner.nextLine();
                if (Strings.isNullOrEmpty(inputStr)) {
                    continue;
                }
                if (inputStr.startsWith("exit")) {
                    break;
                }

                byte[] bytes = inputStr.getBytes("UTF-8");
                writeBuffer.put(bytes).flip();
                while (writeBuffer.hasRemaining()) {
                    socketChannel.write(writeBuffer);
                }
                writeBuffer.clear();

                long res = 0;
                while ((res = socketChannel.read(readBuffer)) == 0) {
                }
                if (res == -1) {
                    break;
                }

                header.flip();
                body.flip();
                byte[] receiveBytes = new byte[body.remaining()];
                body.get(receiveBytes);
                log.info("client receive, header:{}, body:{}", header.getInt(), new String(receiveBytes, "UTF-8"));
                header.clear();
                body.clear();
            }
        } catch (Exception e) {
            log.error("", e);
            throw e;
        } finally {
            if (socketChannel != null) {
                socketChannel.close();
            }
            scanner.close();
        }
    }
}
