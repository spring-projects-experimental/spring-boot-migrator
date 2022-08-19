<#import "todo.ftl" as todo>
<#import "relevance.ftl" as relevance>
<#import "paragraph.ftl" as p/>
<#--<#macro section sections>-->
<#if sections?has_content>
    <#list sections as changeSection>
=== ${changeSection.title}
        <@p.paragraph changeSection.paragraphs/>
        <#if changeSection.relevanceSection?has_content>
            <@relevance.relevance changeSection.relevanceSection/>
        </#if>
        <#if changeSection.todoSection?has_content>
            <@todo.todos changeSection.todoSection/>
        </#if>
    </#list>
<#else>
No Changes found.
</#if>
<#--</#macro>-->