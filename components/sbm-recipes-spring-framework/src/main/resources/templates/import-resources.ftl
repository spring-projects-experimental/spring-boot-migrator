<#if packageName?has_content>
package ${packageName};

</#if>
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource({${commaSeparatedResources}})
public class SpringContextImportConfig {
}
