package org.springframework.sbm.boot.upgrade_27_30.checks;

import org.springframework.sbm.boot.asciidoctor.Section;
import org.springframework.sbm.boot.upgrade_27_30.Sbu30_PreconditionCheck;
import org.springframework.sbm.boot.upgrade_27_30.Sbu30_PreconditionCheckResult;
import org.springframework.sbm.boot.upgrade_27_30.Sbu30_UpgradeSectionBuilder;
import org.springframework.sbm.engine.context.ProjectContext;

public class RedeclaredDependenciesBuilder implements Sbu30_PreconditionCheck, Sbu30_UpgradeSectionBuilder {
    private final RedeclaredDependenciesFinder finder;

    public RedeclaredDependenciesBuilder(RedeclaredDependenciesFinder finder) {
        this.finder = finder;
    }

    @Override
    public boolean isApplicable(ProjectContext projectContext) {
        return false;
    }

    @Override
    public Section build(ProjectContext projectContext) {
        return null;
    }

    @Override
    public Sbu30_PreconditionCheckResult run(ProjectContext context) {
        return null;
    }
}
