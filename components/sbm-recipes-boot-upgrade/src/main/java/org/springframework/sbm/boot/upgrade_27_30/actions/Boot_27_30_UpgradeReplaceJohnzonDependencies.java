package org.springframework.sbm.boot.upgrade_27_30.actions;

import org.springframework.sbm.boot.upgrade_27_30.conditions.JohnzonDependencyCondition;
import org.springframework.sbm.build.api.BuildFile;
import org.springframework.sbm.build.api.Dependency;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.recipe.AbstractAction;

public class Boot_27_30_UpgradeReplaceJohnzonDependencies extends AbstractAction {

    private static final String JOHNZON_DEPENDENCY_PATTERN = "org\\.apache\\.johnzon\\:johnzon-core\\:.*";
    private static final String JOHNZON_DEPENDENCY = "org.apache.johnzon:johnzon-core:1.2.18-jakarta";

    @Override
    public void apply(ProjectContext context) {
        BuildFile buildFile = context.getBuildFile();
        buildFile.removeDependenciesMatchingRegex(JOHNZON_DEPENDENCY_PATTERN);
        buildFile.addDependency(Dependency.fromCoordinates(JOHNZON_DEPENDENCY));
    }

    @Override
    public boolean isApplicable(ProjectContext context) {
        return new JohnzonDependencyCondition().evaluate(context);
    }

}
