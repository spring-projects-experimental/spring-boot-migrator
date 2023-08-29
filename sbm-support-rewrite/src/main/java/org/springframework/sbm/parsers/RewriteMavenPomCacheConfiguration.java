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
package org.springframework.sbm.parsers;

import jdk.jfr.Enabled;
import org.openrewrite.ExecutionContext;
import org.openrewrite.maven.MavenExecutionContextView;
import org.openrewrite.maven.cache.InMemoryMavenPomCache;
import org.openrewrite.maven.cache.MavenPomCache;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Fabian Krüger
 */
@Configuration
public class RewriteMavenPomCacheConfiguration {
    @Bean
    MavenPomCache mavenPomCache(ExecutionContext executionContext, ParserSettings parserSettings) {
        InMemoryMavenPomCache mavenPomCache = new InMemoryMavenPomCache();
        MavenExecutionContextView.view(executionContext).setPomCache(mavenPomCache);
        return mavenPomCache;
    }

    //        if (pomCache == null) {
//            if (isJvm64Bit()) {
//                try {
//                    if (pomCacheDirectory == null) {
//                        //Default directory in the RocksdbMavenPomCache is ".rewrite-cache"
//                        pomCache = new CompositeMavenPomCache(
//                                new InMemoryMavenPomCache(),
//                                new RocksdbMavenPomCache(Paths.get(System.getProperty("user.home")))
//                        );
//                    } else {
//                        pomCache = new CompositeMavenPomCache(
//                                new InMemoryMavenPomCache(),
//                                new RocksdbMavenPomCache(Paths.get(pomCacheDirectory))
//                        );
//                    }
//                } catch (Exception e) {
//                    logger.warn("Unable to initialize RocksdbMavenPomCache, falling back to InMemoryMavenPomCache");
//                    logger.debug(e);
//                }
//            } else {
//                logger.warn("RocksdbMavenPomCache is not supported on 32-bit JVM. falling back to InMemoryMavenPomCache");
//            }
//        }
//        if (pomCache == null) {
//    MavenPomCache pomCache = new InMemoryMavenPomCache();
//        }
}
