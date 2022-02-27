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
package org.springframework.sbm;

import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.springsource.ide.eclipse.commons.livexp.ui.ButtonSection;
import org.springsource.ide.eclipse.commons.livexp.ui.ChooseOneSection;
import org.springsource.ide.eclipse.commons.livexp.ui.DescriptionSection;
import org.springsource.ide.eclipse.commons.livexp.ui.DialogWithSections;
import org.springsource.ide.eclipse.commons.livexp.ui.OkButtonHandler;
import org.springsource.ide.eclipse.commons.livexp.ui.ValidatorSection;
import org.springsource.ide.eclipse.commons.livexp.ui.WizardPageSection;
import org.springsource.ide.eclipse.commons.livexp.util.Log;

public class MigrationDialog extends DialogWithSections {

    private MigrationWizardModel model;

    public MigrationDialog(String title, Shell shell, IProject project) {
        super(title, OkButtonHandler.DUMMY, shell);
        model = new MigrationWizardModel(project);
    }

    @SuppressWarnings("resource")
    @Override
    protected List<WizardPageSection> createSections() throws CoreException {
        ChooseOneSection<Recipe> recipeList = new ChooseOneSection<Recipe>(this, null, model.recipes, model.chosenRecipe, model.chosenRecipeValidator);
        recipeList.setHeightHint(300);
        return List.of(
                new ValidatorSection(model.loading, this),
                recipeList,
                new DescriptionSection(this, model.details) {

					@Override
					protected int preferredNumberOfLines() {
						return 20;
					}
                	
                },
                new ButtonSection(this, "Apply", () -> {
                    performWithProgress(this::applyRecipe);
                }).setEnabler(model.applyEnabler)
        );
    }
    
    private void performWithProgress(IRunnableWithProgress runnable) {
        dialogArea.getDisplay().asyncExec(() -> {
            try {
                getRunnableContext().run(true, false, runnable);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    
    private void loadRecipes(IProgressMonitor monitor) {
        monitor.beginTask("Computing applicable migration recipes...", IProgressMonitor.UNKNOWN);
        model.loadRecipes();
        monitor.done();
    }
    
    private void applyRecipe(IProgressMonitor monitor) {
        monitor.beginTask("Applying recipe '" + model.chosenRecipe.getValue().getName() + "'",
                IProgressMonitor.UNKNOWN);
        try {
            monitor.subTask("Modifying project...");
            model.applyRecipe();
            monitor.subTask("Refreshing project...");
            model.project.refreshLocal(IResource.DEPTH_INFINITE, monitor);
            monitor.subTask("Computing applicable migration recipes...");
            model.loadRecipes();
        } catch (CoreException e) {
            Log.log(e);
        } finally {
            monitor.done();
        }
    }

    @Override
    protected Control createContents(Composite parent) {
        Control c = super.createContents(parent);
        performWithProgress(this::loadRecipes);
        return c;
    }

    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
    }
    
    

}
