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
package org.springframework.sbm.shell;

import org.springframework.sbm.engine.events.FinishedScanningProjectResourceSetEvent;
import org.springframework.sbm.engine.events.StartedScanningProjectResourceEvent;
import org.springframework.sbm.engine.events.StartedScanningProjectResourceSetEvent;
import org.springframework.sbm.engine.recipe.Recipe;
import lombok.RequiredArgsConstructor;
import me.tongfei.progressbar.ProgressBar;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ApplicableRecipeListRenderer {

    private final RecipeRenderer recipeRenderer;

    // TODO: The progress bar should be part of the scan command?!
    @Component
    public static class DisplayProgressBar {

        private ProgressBar pb;
        private static final int MAX_LENGTH = 12;

        @EventListener
        public void listenForStartedScanningEvent(StartedScanningProjectResourceSetEvent e) {
            // reset progressbar
            pb = null;
            long numJavaSources = e.getSize();
            if (numJavaSources != 0) {
                String message = e.getMessage();
                message = message.length() > MAX_LENGTH ? message.substring(0, MAX_LENGTH) : message + " ".repeat(MAX_LENGTH - message.length());
                pb = createProgressBar(message, numJavaSources);
            }
        }

        public ProgressBar createProgressBar(String message, long numJavaSources) {
            return new ProgressBar(message, numJavaSources);
        }

        @EventListener
        public void listenForFinishedScanningProjectResourceSetEvent(FinishedScanningProjectResourceSetEvent e) {
            if (pb != null) {
//                pb.stepTo(pb.getMax());
                pb.close();
            }
        }

        @EventListener
        // TODO: listen for finish instead of start here
        public void listenForStartedScanningEvent(StartedScanningProjectResourceEvent e) {
            if (pb != null) {
                pb.stepBy(1);
            }
        }

    }

    public AttributedString render(List<Recipe> applicableRecipes) {
        return recipeRenderer.renderRecipesList("No applicable recipes found.", "Applicable recipes:", applicableRecipes);
    }

    private void addTitle(AttributedStringBuilder builder, String title) {
        builder.style(AttributedStyle.DEFAULT.bold());
        builder.append(title);
    }
}
