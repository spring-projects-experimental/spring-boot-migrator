package org.springframework.sbm.openrewrite;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.openrewrite.java.JavaParsingException;
import org.openrewrite.maven.MavenDownloadingException;
import org.openrewrite.maven.internal.MavenParsingException;

import java.util.function.Consumer;

@Slf4j
public class RewriteExecutionContextErrorHandler implements Consumer<Throwable> {

    private final ThrowExceptionSwitch throwExceptionSwitch;

    RewriteExecutionContextErrorHandler(ThrowExceptionSwitch throwExceptionSwitch) {
        this.throwExceptionSwitch = throwExceptionSwitch;
    }

    @Override
    public void accept(Throwable t) {
        if (t instanceof MavenParsingException) {
            log.warn(t.getMessage());
        } else if(t instanceof MavenDownloadingException) {
            log.warn(t.getMessage());
        } else if(t instanceof JavaParsingException) {
            if(t.getMessage().equals("Failed symbol entering or attribution")) {
                throw new RuntimeException("This could be a broken jar. Activate logging on WARN level for 'org.openrewrite' might reveal more information.", t);
            }
        } else {
            throw new RuntimeException(t.getMessage(), t);
        }
    }

    @Getter
    @Setter
    public static class ThrowExceptionSwitch {
        private boolean throwExceptions = true;
    }
}