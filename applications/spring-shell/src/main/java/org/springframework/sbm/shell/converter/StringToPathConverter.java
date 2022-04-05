package org.springframework.sbm.shell.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Component
public class StringToPathConverter implements Converter<String, Path> {
    @Override
    public Path convert(String source) {
        return Path.of(source);
    }
}
