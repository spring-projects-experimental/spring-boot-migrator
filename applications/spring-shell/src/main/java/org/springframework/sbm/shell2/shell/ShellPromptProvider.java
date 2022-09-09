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
package org.springframework.sbm.shell2.shell;

import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.context.ProjectContextHolder;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.jline.PromptProvider;
import org.springframework.stereotype.Component;

@Component
public class ShellPromptProvider implements PromptProvider {

	@Autowired
	ProjectContextHolder holder;
	
	@Override
	public AttributedString getPrompt() {
		return new AttributedString(promptString() + ":> ", AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW));
	}

	private String promptString() {
		ProjectContext pc = holder.getProjectContext();
		if (pc==null) {
			return "migrator";
		} else {
			return pc.getProjectRootDirectory().getFileName().toString();
		}
	}

}
