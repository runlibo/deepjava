package study.com.nio.aio;

import com.google.common.base.Charsets;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.nio.channels.CompletionHandler;
/**
 * Created by runlibo.li on 2018/4/24.
 *
 * @author runlibo.li
 */
@Slf4j
public final class ReadCompletionHandler implements CompletionHandler<Integer, AioReadSession> {
    @Override
    public void completed(Integer result, AioReadSession readSession) {
        if (result < 0){
            log.info("Client closed");
            readSession.close();
            return;
        }
        if (result > 0){
            String receiveContent = readSession.getReadContent();
            log.info(receiveContent);
            readSession.pendingRead();

            SessionConfig sessionConfig = SessionConfig.builder()
                                             .clientChannel(readSession.getClientChannel())
                                             .serverSocketChannel(readSession.getServerChannel())
                                             .build();
            AioWriteSession writeSession = new AioWriteSession(sessionConfig);
            ByteBuffer writeBuffer = ByteBuffer.wrap("receive succ".getBytes(Charsets.UTF_8));
            writeSession.write(writeBuffer);
        }

    }

    @Override
    public void failed(Throwable exc, AioReadSession session) {
        log.error("read failed", exc);
        session.close();
    }
}
