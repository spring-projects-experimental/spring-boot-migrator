<#if packageName?has_content>
    package ${packageName};

</#if>
import org.springframework.ws.server.endpoint.annotation.Endpoint;

import ${serviceFqName};
<#list imports as imp>
    import ${imp};
</#list>

@Endpoint
public class ${className} {

private static final String ${nsUriConstantName} = "${nsUri}";

private ${serviceSimpleName} ${serviceFieldName};

${className}(${serviceSimpleName} ${serviceFieldName}) {
this.${serviceFieldName} = ${serviceFieldName};
}

<#list operations as op>
    ${op}
</#list>
}
