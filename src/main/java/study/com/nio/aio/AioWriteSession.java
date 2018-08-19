package study.com.nio.aio;

import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by runlibo.li on 2018/4/26.
 *
 * @author runlibo.li
 */
public class AioWriteSession extends AbstractSession {

    private final Queue<ByteBuffer> writeQueue = new LinkedList<>();

    private WriteCompletionHandler writeCompletionHandler = new WriteCompletionHandler();

    public AioWriteSession(SessionConfig sessionConfig) {
        super(sessionConfig);
    }

    public void write(ByteBuffer writeBuffer) {
        boolean needWrite = false;
        synchronized (writeQueue) {
            needWrite = writeQueue.isEmpty();
            writeQueue.offer(writeBuffer);
        }
        if (needWrite) {
            pendingWrite(writeBuffer);
        }
    }

    public void tryWrite(){
        ByteBuffer writeBuffer = null;
        synchronized (writeQueue) {
            writeBuffer = writeQueue.peek();
            if (writeBuffer == null || !writeBuffer.hasRemaining()){
                writeQueue.poll();
                writeBuffer = writeQueue.peek();
            }
        }

        if (writeBuffer != null) {
            pendingWrite(writeBuffer);
        }
    }

    private void pendingWrite(ByteBuffer writeBuffer) {
        if (clientChannel.isOpen()) {
            clientChannel.write(writeBuffer, this, this.writeCompletionHandler);
        } else {
            throw new IllegalStateException("ClientChannel has been closed");
        }
    }
}
