/*
 * Copyright 2021 - 2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.sbm.openrewrite;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.openrewrite.ExecutionContext;
import org.openrewrite.InMemoryExecutionContext;
import org.openrewrite.Recipe;
import org.openrewrite.internal.lang.Nullable;

import java.util.Collection;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;



@Getter
@Slf4j
public class RewriteExecutionContext implements ExecutionContext {


    private ExecutionContext delegate;

    public RewriteExecutionContext(Consumer<Throwable> onError) {
        this(new InMemoryExecutionContext(onError));
    }

    public RewriteExecutionContext() {
        this(new InMemoryExecutionContext(new RewriteExecutionContextErrorHandler(new RewriteExecutionContextErrorHandler.ThrowExceptionSwitch())));
    }

    public RewriteExecutionContext(ExecutionContext delegate) {
        this.delegate = delegate;
    }

    @Override
    public void putMessage(String key, @Nullable Object value) {
        delegate.putMessage(key, value);
    }

    @Override
    public <T> @Nullable T getMessage(String key) {
        return delegate.getMessage(key);
    }

    @Override
    public <V, C extends Collection<V>> C putMessageInCollection(String key, V value, Supplier<C> newCollection) {
        return delegate.putMessageInCollection(key, value, newCollection);
    }

    @Override
    public <T> Set<T> putMessageInSet(String key, T value) {
        return delegate.putMessageInSet(key, value);
    }

    @Override
    public <T> @Nullable T pollMessage(String key) {
        return delegate.pollMessage(key);
    }

    @Override
    public <T> T pollMessage(String key, T defaultValue) {
        return delegate.pollMessage(key, defaultValue);
    }

    @Override
    public void putCurrentRecipe(Recipe recipe) {
        delegate.putCurrentRecipe(recipe);
    }

    @Override
    public Consumer<Throwable> getOnError() {
        return delegate.getOnError();
    }

    @Override
    public BiConsumer<Throwable, ExecutionContext> getOnTimeout() {
        return delegate.getOnTimeout();
    }

}


