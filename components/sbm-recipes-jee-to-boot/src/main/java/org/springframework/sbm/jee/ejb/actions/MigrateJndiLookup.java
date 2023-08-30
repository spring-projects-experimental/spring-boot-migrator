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
package org.springframework.sbm.jee.ejb.actions;

import org.springframework.sbm.engine.recipe.AbstractAction;
import org.springframework.sbm.java.api.JavaSource;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.support.openrewrite.GenericOpenRewriteRecipe;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.openrewrite.ExecutionContext;
import org.openrewrite.Recipe;
import org.openrewrite.java.AddImport;
import org.openrewrite.java.JavaIsoVisitor;
import org.openrewrite.java.JavaTemplate;
import org.openrewrite.java.RemoveUnusedImports;
import org.openrewrite.java.cleanup.RemoveUnusedLocalVariables;
import org.openrewrite.java.format.AutoFormat;
import org.openrewrite.java.tree.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MigrateJndiLookup extends AbstractAction {

    @Override
    public void apply(ProjectContext context) {
        context.getProjectJavaSources().list().stream()
                .filter(js -> js.hasImportStartingWith("javax.naming.InitialContext"))
                .forEach(sourceWithLookup -> {
                    migrateJndiLookup(sourceWithLookup);
                });
    }

    private void migrateJndiLookup(JavaSource sourceWithLookup) {
        Recipe recipe = new GenericOpenRewriteRecipe<>(() -> new MigrateJndiLookupVisitor())
                .doNext(new RemoveUnusedLocalVariables(null))
                .doNext(new RemoveUnusedImports())
                .doNext(new GenericOpenRewriteRecipe<>(() -> new AddImport<>("org.springframework.beans.factory.annotation.Autowired", null, false)))
                .doNext(new AutoFormat());

        sourceWithLookup.apply(recipe);
    }

    class MigrateJndiLookupVisitor extends JavaIsoVisitor<ExecutionContext> {

        public static final String MATCHES_KEY = "matchingVariables";

        @Override
        public J.ClassDeclaration visitClassDeclaration(J.ClassDeclaration cd, ExecutionContext executionContext) {

            J.ClassDeclaration classDeclaration = super.visitClassDeclaration(cd, executionContext);

            Object matchingVariable = executionContext.getMessage(MATCHES_KEY);
            if (matchingVariable != null) {
                List<MatchFound> matches = (List) matchingVariable;
                Iterator<MatchFound> iterator = matches.iterator();
                while (iterator.hasNext()) {
                    MatchFound nextMatch = iterator.next();
                    classDeclaration = addInstanceAsAutowiredMember(classDeclaration, nextMatch);
                }
            }

            return classDeclaration;
        }

        @Override
        public J.VariableDeclarations visitVariableDeclarations(J.VariableDeclarations multiVariable, ExecutionContext executionContext) {
            if (isAssignedThroughStaticInitialContextLookup(multiVariable)) {
                J.Block containingBlock = findContainingBlock();
                addToExecutionContext(containingBlock, multiVariable, executionContext);
                return super.visitVariableDeclarations(multiVariable, executionContext);
            }
            return multiVariable;
        }

        @Override
        public J.Block visitBlock(J.Block b, ExecutionContext executionContext) {
            J.Block block = super.visitBlock(b, executionContext);

            if (executionContext.getMessage(MATCHES_KEY) != null) {
                List<MatchFound> matches = executionContext.getMessage(MATCHES_KEY);
                block = removeFromMethodBlock(matches, block);
            }

            return block;
        }

        private J.Block findContainingBlock() {
            return getCursor().firstEnclosing(J.Block.class);
        }

        private void addToExecutionContext(J.Block containingBlock, J.VariableDeclarations multiVariable, ExecutionContext executionContext) {
            MatchFound matchFound = new MatchFound(containingBlock, multiVariable);
            if (executionContext.getMessage(MATCHES_KEY) != null) {
                ((List) executionContext.getMessage(MATCHES_KEY)).add(matchFound);
            } else {
                List<MatchFound> matches = new ArrayList<>();
                matches.add(matchFound);
                executionContext.putMessage(MATCHES_KEY, matches);
            }
        }


        private J.Block removeFromMethodBlock(List<MatchFound> matches, J.Block block) {
            for (MatchFound match : matches) {
                if (match.getContainingBlock() != null && block.getId().equals(match.getContainingBlock().getId())) {
                    List<Statement> statements = block.getStatements();
                    Iterator<Statement> iterator = statements.iterator();
                    while (iterator.hasNext()) {
                        Statement statement = iterator.next();
                        if (statement == match.getMultiVariable()) {
                            iterator.remove();
                            block = block.withStatements(statements);
                        }
                    }
                } else if (match.getContainingBlock() == null) {

                }
            }
            return block;
        }

        private J.ClassDeclaration addInstanceAsAutowiredMember(J.ClassDeclaration classDecl, MatchFound matchFound) {
            J.Block body = classDecl.getBody();
            J.VariableDeclarations variable = matchFound.getMultiVariable();
            JavaType.Class type = (JavaType.Class) variable.getTypeExpression().getType();
            String variableName = variable.getVariables().get(0).getSimpleName();
            JavaTemplate javaTemplate = JavaTemplate.builder(() -> getCursor(), "@Autowired\nprivate " + type.getClassName() + " " + variableName).build();
            J.Block result = body.withTemplate(javaTemplate, body.getCoordinates().lastStatement());
            List<Statement> statements1 = result.getStatements();
            Statement statement = statements1.get(statements1.size() - 1);
            statements1.remove(statement);
            statements1.add(0, statement);
            result = body.withStatements(statements1);
            J.ClassDeclaration classDeclaration = classDecl.withBody(result);
            return classDeclaration;
        }

        private boolean isAssignedThroughStaticInitialContextLookup(J.VariableDeclarations multiVariable) {
            if (multiVariable.getVariables().size() != 1) {
                // TODO: handle multi variable -> maybe refactor multivariable to single variable decl first.
            } else {
                JRightPadded<J.VariableDeclarations.NamedVariable> namedVariable = multiVariable.getPadding().getVariables().get(0);
                JLeftPadded<Expression> variableInitializer = namedVariable.getElement().getPadding().getInitializer();
                if (variableInitializer.getClass().isAssignableFrom(JLeftPadded.class)) {
                    JLeftPadded jLeftPadded = variableInitializer;
                    Expression element = variableInitializer.getElement();
                    if (element.getClass().isAssignableFrom(J.TypeCast.class)) {
                        J.MethodInvocation methodInvocation = (J.MethodInvocation) ((J.TypeCast) element).getExpression();
                        if (methodInvocation.getSelect().getType().getClass().isAssignableFrom(JavaType.Class.class)) {
                            JavaType.Class type = (JavaType.Class) methodInvocation.getSelect().getType();
                            return type.getFullyQualifiedName().equals("javax.naming.InitialContext");
                        }
                    } else if (element.getClass().isAssignableFrom(J.MethodInvocation.class)) {
                        J.MethodInvocation methodInvocation = (J.MethodInvocation) element;
                        if (methodInvocation.getSelect().getType().getClass().isAssignableFrom(JavaType.Class.class)) {
                            JavaType.Class type = (JavaType.Class) methodInvocation.getSelect().getType();
                            return type.getFullyQualifiedName().equals("javax.naming.InitialContext");
                        }
                    }
                }

            }
            return false;
        }

        @RequiredArgsConstructor
        @Getter
        private class MatchFound {
            private final J.Block containingBlock;
            private final J.VariableDeclarations multiVariable;
        }
    }


}
