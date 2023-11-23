/*
 * Copyright 2021 - 2023 the original author or authors.
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
package org.springframework.sbm.boot.autoconfigure;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.sbm.parsers.ParserProperties;
import org.springframework.sbm.parsers.RewriteParserConfiguration;
import org.springframework.sbm.recipes.RewriteRecipeDiscovery;

/**
 * @author Fabian Krüger
 */
@AutoConfiguration(after = RewriteParserConfiguration.class)
@Import(RewriteParserConfiguration.class)
@EnableConfigurationProperties(ParserProperties.class)
public class DiscoveryConfiguration {

	@Bean
	RewriteRecipeDiscovery rewriteRecipeDiscovery(ParserProperties parserProperties) {
		return new RewriteRecipeDiscovery(parserProperties);
	}

}
