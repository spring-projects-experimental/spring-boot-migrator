<#macro paragraph paragraph>
    <#list paragraph as p>
        <#if p.text?has_content>
${p.text}
        </#if>

        <#if p.table?has_content>
|===
<#if p.table.headerCols?has_content>
| ${p.table.headerCols?join(" | ")}

</#if>
<#if p.table.rows?has_content>
            <#list p.table.rows as row>
| ${row?join(" | ")}
            </#list>
</#if>
|===

        </#if>
    </#list>
</#macro>