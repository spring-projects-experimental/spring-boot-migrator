<#import "paragraph.ftl" as p/>
<#macro relevance section>
==== Relevance

    <#if section.paragraphs?has_content>
<#--        <#list section.paragraphs as paragraph>-->
            <@p.paragraph section.paragraphs/>
<#--        </#list>-->
    </#if>
</#macro>