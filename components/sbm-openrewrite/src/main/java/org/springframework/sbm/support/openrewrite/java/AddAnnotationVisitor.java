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
package org.springframework.sbm.support.openrewrite.java;

import org.jetbrains.annotations.NotNull;
import org.openrewrite.ExecutionContext;
import org.openrewrite.java.JavaIsoVisitor;
import org.openrewrite.java.JavaParser;
import org.openrewrite.java.JavaTemplate;
import org.openrewrite.java.tree.J;

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
    private final Supplier<JavaParser> javaParserSupplier;

    public AddAnnotationVisitor(JavaParser javaParserSupplier, J target, String snippet, String annotationImport, String... otherImports) {
        this(() -> javaParserSupplier, target, snippet, annotationImport, otherImports);
    }

    public AddAnnotationVisitor(Supplier<JavaParser> javaParserSupplier, J target, String snippet, String annotationImport, String... otherImports) {
        this.target = target;
        this.snippet = snippet;
        this.imports = otherImports == null
                ? new String[]{annotationImport}
                : concat(annotationImport, otherImports);
        this.javaParserSupplier = javaParserSupplier;
    }

    public J.ClassDeclaration visitClassDeclaration(J.ClassDeclaration classDecl, ExecutionContext p) {
        J.ClassDeclaration cd = super.visitClassDeclaration(classDecl, p);
        if (target.getId().equals(cd.getId())) {
            JavaTemplate template = getJavaTemplate(p, snippet, imports);
            // FIXME: #7 Moving this line from above getTemaplet() fixed BootifyAnnotatedServletsIntegrationTest ?!
            Stream.of(imports).forEach(i -> maybeAddImport(i));
            cd = cd.withTemplate(template, cd.getCoordinates().addAnnotation((o1, o2) -> 0));
        }
        return cd;
    }


    public J.MethodDeclaration visitMethodDeclaration(J.MethodDeclaration methodDecl, ExecutionContext p) {
        J.MethodDeclaration md = super.visitMethodDeclaration(methodDecl, p);
        if (target == md) {
            Stream.of(imports).forEach(i -> maybeAddImport(i));
            JavaTemplate template = getJavaTemplate(p, snippet, imports);
            md = md.withTemplate(template, md.getCoordinates().addAnnotation(Comparator.comparing(J.Annotation::getSimpleName)));
        }
        return md;
    }

    @Override
    public J.VariableDeclarations visitVariableDeclarations(J.VariableDeclarations multiVariable, ExecutionContext p) {
        J.VariableDeclarations vd = super.visitVariableDeclarations(multiVariable, p);
        if (target == vd) {
            Stream.of(imports).forEach(i -> maybeAddImport(i));
            JavaTemplate template = getJavaTemplate(p, snippet, imports);
            vd = vd.withTemplate(template, vd.getCoordinates().addAnnotation(Comparator.comparing(J.Annotation::getSimpleName)));
        }
        return vd;
    }

    private String[] concat(String annotationImport, String[] otherImports) {
        String[] result = new String[otherImports.length + 1];
        result[0] = annotationImport;
        System.arraycopy(otherImports, 0, result, 1, otherImports.length);
        return result;
    }

    @NotNull
    private JavaTemplate getJavaTemplate(ExecutionContext p, String snippet, String... imports) {
        return JavaTemplate.builder(() -> getCursor(), snippet)
                .imports(imports)
                .javaParser(javaParserSupplier)
                .build();
    }

}
