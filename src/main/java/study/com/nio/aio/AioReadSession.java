package study.com.nio.aio;

import java.nio.ByteBuffer;

/**
 * Created by runlibo.li on 2018/4/24.
 *
 * @author runlibo.li
 */
public class AioReadSession extends AbstractSession {

    private ByteBuffer readBuffer;

    private ReadCompletionHandler readCompletionHandler = new ReadCompletionHandler();

    public AioReadSession(SessionConfig sessionConfig, int bufSize) {
        super(sessionConfig);
        this.readBuffer = ByteBuffer.allocate(bufSize);
    }

    public void start() {
        pendingRead();
    }

    public void pendingRead() {
        if (clientChannel.isOpen()) {
            clientChannel.read(readBuffer, this, this.readCompletionHandler);
        } else {
            throw new IllegalStateException("ClientChannel has been closed");
        }
    }


    public String getReadContent() {
        readBuffer.flip();
        byte[] bytes = new byte[readBuffer.remaining()];
        readBuffer.get(bytes);
        readBuffer.clear();
        return new String(bytes);
    }
}
