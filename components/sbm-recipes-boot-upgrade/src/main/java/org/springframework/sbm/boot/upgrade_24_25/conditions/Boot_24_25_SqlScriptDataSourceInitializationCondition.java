package org.springframework.sbm.boot.upgrade_24_25.conditions;

import org.springframework.sbm.boot.properties.api.SpringBootApplicationProperties;
import org.springframework.sbm.boot.properties.search.SpringBootApplicationPropertiesResourceListFilter;
import org.springframework.sbm.boot.upgrade_24_25.filter.SqlScriptDataSourceInitializationPropertiesAnalyzer;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.recipe.Condition;

import java.util.List;

public class Boot_24_25_SqlScriptDataSourceInitializationCondition implements Condition {
    @Override
    public String getDescription() {
        return "Check if deprecated Datasource init properties exist.";
    }

    @Override
    public boolean evaluate(ProjectContext context) {
        List<SpringBootApplicationProperties> filteredResources = context.search(new SpringBootApplicationPropertiesResourceListFilter());
        List<SqlScriptDataSourceInitializationPropertiesAnalyzer.DeperecatedPropertyMatch> properties = new SqlScriptDataSourceInitializationPropertiesAnalyzer().findDeprecatedProperties(filteredResources);
        return !properties.isEmpty();
    }
}
