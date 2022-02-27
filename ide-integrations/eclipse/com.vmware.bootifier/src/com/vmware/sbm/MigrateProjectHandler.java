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

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

public class MigrateProjectHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
	    IProject p = projectFor(event);	    
//	    WizardDialog wizardDialog = new WizardDialog(HandlerUtil.getActiveShell(event), new MigrationWizard(p));
//	    wizardDialog.setPageSize(2000, 1000);
//        wizardDialog.open();
	    
	    MigrationDialog dialog = new MigrationDialog("Migrate Project", HandlerUtil.getActiveShell(event), p);
	    dialog.open();
		return null;
	}
	
    public static IProject projectFor(ExecutionEvent event) {
        IResource resource = null;
        IStructuredSelection selection = HandlerUtil.getCurrentStructuredSelection(event);
        if (selection.isEmpty() || !(selection.getFirstElement() instanceof IAdaptable)) {
            return null;
        }
        IAdaptable adaptable = (IAdaptable) selection.getFirstElement();
        resource = adaptable.getAdapter(IResource.class);
        if (resource != null) {
            return resource.getProject();
        }
        return null;
    }
    
    
}
