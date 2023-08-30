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
package org.springframework.sbm.java.impl;

import org.openrewrite.ExecutionContext;
import org.openrewrite.java.JavaIsoVisitor;
import org.openrewrite.java.tree.*;
import org.openrewrite.java.tree.J.FieldAccess;
import org.openrewrite.marker.Markers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ReplaceStaticFieldAccessVisitor extends JavaIsoVisitor<ExecutionContext> {

    //TODO: what if we have something like
    // import static javax.ws.rs.core.MediaType.APLICATION_XML;

    private final StaticFieldAccessTransformer transform;

    public ReplaceStaticFieldAccessVisitor(StaticFieldAccessTransformer transform) {
        this.transform = transform;
    }

    @Override
    public J.FieldAccess visitFieldAccess(FieldAccess fieldAccess, ExecutionContext p) {
        if (!isImport()) {
            Expression currentTarget = fieldAccess.getTarget();
            if (currentTarget instanceof J.Identifier) {
                J.Identifier currentIdentTarget = (J.Identifier) currentTarget;
                JavaType currentTargetType = currentIdentTarget.getType();
                if (currentTargetType instanceof JavaType.Class) {
                    JavaType.Class currentTargetClassType = (JavaType.Class) currentTargetType;

                    String currentFieldName = fieldAccess.getSimpleName();
                    Optional<StaticFieldAccessTransformer.StaticFieldRef> newStaticFieldAccess = transform.transform(StaticFieldAccessTransformer.StaticFieldRef.builder()
                            .field(currentFieldName)
                            .fqClassName(currentTargetClassType.getFullyQualifiedName())
                            .build()
                    );

                    if (newStaticFieldAccess.isPresent() && differ(newStaticFieldAccess.get(), fieldAccess)) {

                        JavaType.Class newClassType = JavaType.Class.build(newStaticFieldAccess.get().getFqClassName());
                        J.Identifier ident = new J.Identifier(UUID.randomUUID(), Space.EMPTY, Markers.EMPTY, newClassType.getClassName(), newClassType, null); // FIXME: #497 correct?!

                        String newFieldName = newStaticFieldAccess.get().getField();

                        J.Identifier identifier = new J.Identifier(
                                UUID.randomUUID(),
                                Space.EMPTY,
                                Markers.EMPTY,
                                newFieldName,
                                newClassType,
                                null
                        );
                        FieldAccess af = new J.FieldAccess(
                                UUID.randomUUID(),
                                Space.build(" ", List.of()),
                                Markers.EMPTY,
                                ident,
                                JLeftPadded.build(identifier),
                                null // FIXME: #497 correct?!
                        );

                        maybeRemoveImport(currentTargetClassType);
                        maybeAddImport(newClassType.getFullyQualifiedName());
                        return af;
                    }
                }
            }
        }
        return super.visitFieldAccess(fieldAccess, p);
    }

    private boolean differ(StaticFieldAccessTransformer.StaticFieldRef staticFieldRef, FieldAccess fieldAccess) {
        String fullyQualifiedName = TypeUtils.asFullyQualified(fieldAccess.getTarget().getType()).getFullyQualifiedName();
        staticFieldRef.getFqClassName();
        return !(fullyQualifiedName.equals(staticFieldRef.getFqClassName()) && staticFieldRef.getField().equals(fieldAccess.getSimpleName()));
    }

    private boolean isImport() {
        return getCursor().getPathAsStream().anyMatch(node -> node instanceof J.Import);
    }
}
