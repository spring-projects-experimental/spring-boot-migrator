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
package org.springframework.sbm.boot.upgrade_27_30;

import org.jetbrains.annotations.NotNull;
import org.openrewrite.Tree;
import org.openrewrite.internal.ListUtils;
import org.openrewrite.internal.lang.Nullable;
import org.openrewrite.java.JavaIsoVisitor;
import org.openrewrite.java.tree.*;
import org.openrewrite.marker.Markers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ImplementTypedInterface<P> extends JavaIsoVisitor<P> {
    private final J.ClassDeclaration scope;
    private final JavaType.FullyQualified interfaceType;
    private final List<JavaType> typeParameters;

    public ImplementTypedInterface(J.ClassDeclaration scope, JavaType.FullyQualified interfaceType, List<JavaType> typeParameters) {
        this.scope = scope;
        this.interfaceType = interfaceType;
        this.typeParameters = typeParameters;
    }

    public ImplementTypedInterface(J.ClassDeclaration scope, String interfaze, List<JavaType> typeParameters) {
        this(scope, JavaType.ShallowClass.build(interfaze), typeParameters);
    }

    @NotNull
    public J.ClassDeclaration visitClassDeclaration(@NotNull J.ClassDeclaration classDecl, @NotNull P p) {
        J.ClassDeclaration c = super.visitClassDeclaration(classDecl, p);
        if (c.isScope(this.scope) && (c.getImplements() == null || c.getImplements().stream().noneMatch((f) -> TypeUtils.isAssignableTo(f.getType(), this.interfaceType)))) {
            if (!classDecl.getSimpleName().equals(this.interfaceType.getClassName())) {
                this.maybeAddImport(this.interfaceType);
            }

            TypeTree type = TypeTree.build(classDecl.getSimpleName().equals(this.interfaceType.getClassName()) ? this.interfaceType.getFullyQualifiedName() : this.interfaceType.getClassName()).withType(this.interfaceType).withPrefix(Space.format(" "));
            if (typeParameters != null && !typeParameters.isEmpty() && typeParameters.stream().noneMatch(tp -> tp instanceof JavaType.GenericTypeVariable)) {
                // FIX: OR8.1
                NameTree clazz = null;
                JavaType javaType = null;
                type = new J.ParameterizedType(UUID.randomUUID(), Space.EMPTY, Markers.EMPTY, clazz, buildTypeParameters(typeParameters), /*type*/ javaType);
            }
            c = c.withImplements(ListUtils.concat(c.getImplements(), type));
            JContainer<TypeTree> anImplements = c.getPadding().getImplements();

            assert anImplements != null;

            if (anImplements.getBefore().getWhitespace().isEmpty()) {
                c = c.getPadding().withImplements(anImplements.withBefore(Space.format(" ")));
            }
        }

        return c;
    }

    @Nullable
    private JContainer<Expression> buildTypeParameters(List<JavaType> typeParameters) {
        List<JRightPadded<Expression>> typeExpressions = new ArrayList<>();

        int index = 0;
        for (JavaType type : typeParameters) {
            Expression typeParameterExpression = (Expression) buildTypeTree(type, (index++ == 0) ? Space.EMPTY : Space.format(" "));
            if (typeParameterExpression == null) {
                return null;
            }
            typeExpressions.add(new JRightPadded<>(
                    typeParameterExpression,
                    Space.EMPTY,
                    Markers.EMPTY
            ));
        }
        return JContainer.build(Space.EMPTY, typeExpressions, Markers.EMPTY);
    }

    private TypeTree buildTypeTree(@Nullable JavaType type, Space space) {
        if (type == null || type instanceof JavaType.Unknown) {
            return null;
        } else if (type instanceof JavaType.FullyQualified fq) {

            J.Identifier identifier = new J.Identifier(Tree.randomId(),
                    space,
                    Markers.EMPTY,
                    fq.getClassName(),
                    type,
                    null
            );

            if (!fq.getTypeParameters().isEmpty()) {
                JContainer<Expression> typeParameters = buildTypeParameters(fq.getTypeParameters());
                if (typeParameters == null) {
                    //If there is a problem resolving one of the type parameters, then do not return a type
                    //expression for the fully-qualified type.
                    return null;
                }
                // FIXME: OR8.1
                NameTree clazz = null;
                return new J.ParameterizedType(
                        Tree.randomId(),
                        space,
                        Markers.EMPTY,
                        clazz,
                        typeParameters,
                        type
                        /*identifier*/
                );

            } else {
                maybeAddImport(fq);
                return identifier;
            }
        } else if (type instanceof JavaType.GenericTypeVariable genericType) {
            if (!genericType.getName().equals("?")) {
                return new J.Identifier(Tree.randomId(),
                        space,
                        Markers.EMPTY,
                        genericType.getName(),
                        type,
                        null
                );
            }
            JLeftPadded<J.Wildcard.Bound> bound = null;
            NameTree boundedType = null;
            if (genericType.getVariance() == JavaType.GenericTypeVariable.Variance.COVARIANT) {
                bound = new JLeftPadded<>(Space.format(" "), J.Wildcard.Bound.Extends, Markers.EMPTY);
            } else if (genericType.getVariance() == JavaType.GenericTypeVariable.Variance.CONTRAVARIANT) {
                bound = new JLeftPadded<>(Space.format(" "), J.Wildcard.Bound.Super, Markers.EMPTY);
            }

            if (!genericType.getBounds().isEmpty()) {
                boundedType = buildTypeTree(genericType.getBounds().get(0), Space.format(" "));
                if (boundedType == null) {
                    return null;
                }
            }

            return new J.Wildcard(
                    Tree.randomId(),
                    space,
                    Markers.EMPTY,
                    bound,
                    boundedType
            );
        }
        return null;

    }
}

