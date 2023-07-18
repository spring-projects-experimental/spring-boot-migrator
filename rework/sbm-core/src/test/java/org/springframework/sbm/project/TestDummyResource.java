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
package org.springframework.sbm.project;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

@RequiredArgsConstructor
public class TestDummyResource implements Resource {
    private final Path path;
    private final String content;

    @Override
    public boolean exists() {
        return false;
    }

    @Override
    public URL getURL() {
        throw new UnsupportedOperationException();
    }

    @Override
    public URI getURI() {
        throw new UnsupportedOperationException();
    }

    @Override
    public File getFile() {
        return new File(path.toString());
    }

    public boolean isFile() {
        return getFile(this).isFile();
    }

    private File getFile(TestDummyResource testDummyResource) {
        return getFile();
    }

    @Override
    public long contentLength() {
        throw new UnsupportedOperationException();
    }

    @Override
    public long lastModified() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Resource createRelative(String s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getFilename() {
        return path.getFileName().toString();
    }

    @Override
    public String getDescription() {
        throw new UnsupportedOperationException();
    }

    @Override
    public InputStream getInputStream() {
        return new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
    }
}
