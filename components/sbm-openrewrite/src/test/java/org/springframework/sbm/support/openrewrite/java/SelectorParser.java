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
package org.springframework.sbm.support.openrewrite.java;

import org.openrewrite.Recipe;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class SelectorParser {
    public Recipe parse(String selector) {
        if (selector.startsWith("org.openrewrite")) {
            String openRewriteSearchClazz = selector.substring(0, selector.indexOf(":") + 1);
            try {
                Class<?> openRewriteSearch = Class.forName(openRewriteSearchClazz);

                if (openRewriteSearch.isAssignableFrom(Recipe.class)) {
                    Class<Recipe> recipeClazz = (Class<Recipe>) openRewriteSearch;
                    Recipe recipe = recipeClazz.getConstructor().newInstance();
                    String paramsString = selector.substring(selector.indexOf(":") + 1);
                    String[] params = paramsString.split(",");
                    Arrays.stream(params)
                            .map(p -> p.split("="))
                            .forEach(t -> setterCall(recipe, t));
                    return recipe;
                } else {
                    throw new RuntimeException("FIXME...");
                }

            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        } else {
            // Custom recipe
            return null;
        }
    }

    private void setterCall(Recipe recipe, String[] t) {
        try {
            Method method = recipe.getClass().getMethod("set" + uppercaseFirstChar(t[0]), t[1].getClass());
            method.invoke(t[1]);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    // TODO: provide in common util class
    String uppercaseFirstChar(String name) {
        if (name.isEmpty()) return name;
        return Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }
}
