package study.com.nio.aio;

import lombok.extern.slf4j.Slf4j;

import java.nio.channels.CompletionHandler;

/**
 * Created by runlibo.li on 2018/4/26.
 *
 * @author runlibo.li
 */
@Slf4j
public class WriteCompletionHandler implements CompletionHandler<Integer, AioWriteSession> {
    @Override
    public void completed(Integer result, AioWriteSession writeSession) {
        log.debug("to {} write {} bytes", writeSession.serverChannel);
        writeSession.tryWrite();
    }

    @Override
    public void failed(Throwable exc, AioWriteSession writeSession) {
        log.error("write failed", exc);
        writeSession.close();
    }
}
