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
package org.springframework.sbm.jee.ejb.actions;

import org.springframework.sbm.engine.recipe.AbstractAction;
import org.springframework.sbm.java.api.JavaSource;
import org.springframework.sbm.java.api.SuperTypeHierarchy;
import org.springframework.sbm.java.api.Type;
import org.springframework.sbm.engine.context.ProjectContext;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * Migrate local stateless Session Beans to Spring beans annotated with {@code @Service}.
 *
 * <p>
 *     <ul>
 *     <li>For NoInterfaceView Session Beans the {@code @Stateless} annotation is replaced with {@code @Service}.</li>
 *     <li>For all other Session Beans not implementing a {@code @Remote} business interface</li>
 *     <ul>
 *          <li>the {@code @Local} interface information is removed.</li>
 *          <li>the {@code @Stateless} annotation is replaced with {@code @Service}.</li>
 *     </ul>
 *     <li>Session Beans implementing a {@code @Remote} interface are ignored.</li>
 *     </ul>
 * </p>
 */
@RequiredArgsConstructor
public class MigrateLocalStatelessSessionBeans extends AbstractAction {

    private final MigrateLocalStatelessSessionBeansHelper helper;

    public MigrateLocalStatelessSessionBeans() {
        helper = new MigrateLocalStatelessSessionBeansHelper();
    }

    @Override
    public void apply(ProjectContext context) {
        migrateLocalStatelessSessionBeans(context);
        migrateSingletonSessionBeans(context);
        removeLocalBeanAnnotations(context);
    }

    private void removeLocalBeanAnnotations(ProjectContext context) {
        helper.removeLocalBeanAnnotations(context.getProjectJavaSources());
    }

    private void migrateSingletonSessionBeans(ProjectContext context) {
        List<TypeAndSourceFile> singletonBeans = helper.findTypesAnnotatedWithSingleton(context.getProjectJavaSources());
        singletonBeans.forEach(ssb -> {
            Type type = ssb.getType();
            helper.migrateSingletonAnnotation(type);
        });
    }

    private void migrateLocalStatelessSessionBeans(ProjectContext context) {
        List<TypeAndSourceFile> statelessSessionBeans = helper.findTypesAnnotatedWithStateless(context.getProjectJavaSources());

        statelessSessionBeans.forEach(ssb -> {
            Type type = ssb.getType();
            JavaSource javaSource = ssb.getJavaSource();
            SuperTypeHierarchy hierarchy = new SuperTypeHierarchy(type);
            if (helper.implementsNoRemoteInterface(hierarchy)) {
                helper.removeLocalAnnotations(hierarchy);
                helper.migrateStatelessAnnotation(type);
            }
        });
    }

}
