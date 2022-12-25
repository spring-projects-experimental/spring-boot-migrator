package org.springframework.sbm.jee.jaxrs.recipes.visitors;

import lombok.EqualsAndHashCode;
import lombok.Value;
import org.jetbrains.annotations.NotNull;
import org.openrewrite.Cursor;
import org.openrewrite.ExecutionContext;
import org.openrewrite.java.AddOrUpdateAnnotationAttribute;
import org.openrewrite.java.JavaIsoVisitor;
import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.TypeUtils;
import org.springframework.sbm.jee.utils.AnnotationUtils;

import java.util.Optional;

@Value
@EqualsAndHashCode(callSuper = true)
public class CopyAnnotationAttributeVisitor extends JavaIsoVisitor<ExecutionContext> {
    String sourceAnnotationType;
    String sourceAttributeName;
    String targetAnnotationType;
    String targetAttributeName;

    @Override
    public J.Annotation visitAnnotation(@NotNull J.Annotation annotation, @NotNull ExecutionContext ctx) {
        J.Annotation a = super.visitAnnotation(annotation, ctx);

        if (!TypeUtils.isOfClassType(a.getType(), targetAnnotationType)) {
            return a;
        }

        Cursor parent = getCursor().getParent();
        if (parent == null) {
            return a;
        }
        J.VariableDeclarations variableDeclaration = parent.getValue();
        Optional<J.Literal> optionalSourceAnnotationAttributeValue = getSourceAnnotationAttributeValue(variableDeclaration);
        if (optionalSourceAnnotationAttributeValue.isEmpty()) {
            return a;
        }

        J.Literal sourceAnnotationAttributeValue = optionalSourceAnnotationAttributeValue.get();
        if (sourceAnnotationAttributeValue.getValue() != null) {
            JavaIsoVisitor<ExecutionContext> addOrUpdateAnnotationAttributeVisitor = new AddOrUpdateAnnotationAttribute(targetAnnotationType, targetAttributeName, sourceAnnotationAttributeValue.getValue().toString(), false)
                    .getVisitor();
            if (targetAnnotationOnlyHasOneLiteralArgument(a)) {
                a = (J.Annotation) addOrUpdateAnnotationAttributeVisitor.visit(a, ctx, getCursor());
            }
            return (J.Annotation) addOrUpdateAnnotationAttributeVisitor.visit(a, ctx, getCursor());
        }
        return a;
    }

    private Optional<J.Literal> getSourceAnnotationAttributeValue(J.VariableDeclarations methodParameterDeclaration) {
        return methodParameterDeclaration.getLeadingAnnotations().stream()
                .filter(annotation -> TypeUtils.isOfClassType(annotation.getType(), sourceAnnotationType))
                .flatMap(annotation -> AnnotationUtils.getAttributeValue(annotation, sourceAttributeName).stream())
                .findAny();
    }

    private boolean targetAnnotationOnlyHasOneLiteralArgument(@NotNull J.Annotation annotation) {
        return annotation.getArguments() != null && annotation.getArguments().size() == 1 && annotation.getArguments().get(0) instanceof J.Literal;
    }
}