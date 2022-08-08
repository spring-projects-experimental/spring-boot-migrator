package org.springframework.sbm.engine.recipe;

import org.openrewrite.ExecutionContext;
import org.openrewrite.SourceFile;

import java.util.List;

public class ErrorClass extends org.openrewrite.Recipe {

    @Override
    public String getDisplayName() {
        return "NAME";
    }

    @Override
    protected List<SourceFile> visit(List<SourceFile> before, ExecutionContext ctx) {

        if (true) {
            throw new RuntimeException("A problem happened whilst visiting");
        }
        return super.visit(before, ctx);
    }
}
