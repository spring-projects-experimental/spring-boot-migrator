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

import org.openrewrite.ExecutionContext;
import org.openrewrite.java.JavaIsoVisitor;
import org.openrewrite.java.JavaParser;
import org.openrewrite.java.JavaTemplate;
import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.JavaCoordinates;

import java.util.Comparator;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Adds <code>annotation</code> to <code>target</code> if it is part of <code>scope</code>
 */
public class AddAnnotationVisitor extends JavaIsoVisitor<ExecutionContext> {
    private final J target;
    private final String snippet;
    private final String[] imports;
    private final Supplier<JavaParser.Builder> javaParserSupplier;
    // ugly, just because UUID of elemnts stay same now and can't be used as criteria leading to multiple visits of the same .
    private boolean targetVisited;

    public AddAnnotationVisitor(JavaParser.Builder javaParserSupplier, J target, String snippet, String annotationImport, String... otherImports) {
        this(() -> javaParserSupplier, target, snippet, annotationImport, otherImports);
    }

    public AddAnnotationVisitor(Supplier<JavaParser.Builder> javaParserSupplier, J target, String snippet, String annotationImport, String... otherImports) {
        this.target = target;
        this.snippet = snippet;
        this.imports = otherImports == null
                ? new String[]{annotationImport}
                : concat(annotationImport, otherImports);
        this.javaParserSupplier = javaParserSupplier;
    }

    public J.ClassDeclaration visitClassDeclaration(J.ClassDeclaration classDecl, ExecutionContext p) {
        J.ClassDeclaration cd = super.visitClassDeclaration(classDecl, p);
        if (target.getId().equals(cd.getId()) && !targetVisited) {
            JavaTemplate template = JavaTemplate.builder(() -> getCursor().getParent(), snippet)
                    .imports(imports)
                    .build();
            Stream.of(imports).forEach(i -> maybeAddImport(i, null, false));
            JavaCoordinates coordinates = cd.getCoordinates().addAnnotation((o1, o2) -> 0);
            cd = cd.withTemplate(template, coordinates);
            targetVisited = true;
        }
        return cd;
    }


    public J.MethodDeclaration visitMethodDeclaration(J.MethodDeclaration methodDecl, ExecutionContext p) {
        J.MethodDeclaration md = super.visitMethodDeclaration(methodDecl, p);
        if (target.getId().equals(md.getId()) && !targetVisited) {
            JavaTemplate template = JavaTemplate.builder(() -> getCursor().getParent(), snippet)
                            .imports(imports)
                            .build();
            Stream.of(imports).forEach(i -> {
                maybeAddImport(i, null, false);
            });
            md = md.withTemplate(template, md.getCoordinates().addAnnotation(Comparator.comparing(J.Annotation::getSimpleName)));
            targetVisited = true;
        }
        return md;
    }

    @Override
    public J.VariableDeclarations visitVariableDeclarations(J.VariableDeclarations multiVariable, ExecutionContext p) {
        J.VariableDeclarations vd = super.visitVariableDeclarations(multiVariable, p);
        if (target.getId().equals(vd.getId()) && !targetVisited) {
            JavaTemplate template = JavaTemplate.builder(() -> getCursor().getParent(), snippet).imports(imports).build();
            Stream.of(imports).forEach(i -> maybeAddImport(i, null, false));
            vd = vd.withTemplate(template, vd.getCoordinates().addAnnotation(Comparator.comparing(J.Annotation::getSimpleName)));
            targetVisited = true;
        }
        return vd;
    }

    private String[] concat(String annotationImport, String[] otherImports) {
        String[] result = new String[otherImports.length + 1];
        result[0] = annotationImport;
        System.arraycopy(otherImports, 0, result, 1, otherImports.length);
        return result;
    }

}
