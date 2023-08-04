<#--Recuresiveky render Todos and children -->
<#macro todoListMacro list level=1>
    <#list list as todo>
${""?left_pad(level, "*")} [ ] ${todo.text}
<@todoListMacro todo.children level+1/>
    </#list>
</#macro>

<#macro todos section>
==== Todo

<#if section.paragraphs?has_content>
    <#list section.paragraphs as paragraph>
${paragraph.text}

    </#list>
</#if>
<#list section.todoLists as todoList>
    <#list todoList.paragraphs as paragraph>

${paragraph.text}

    </#list>

<@todoListMacro todoList.todos/>
<#if todoList.recipeName?has_content>

[TIP]
====
🤖 **Can be automated** 🤖

Apply the recipe `${todoList.recipeName}`.
====

</#if>
</#list>
</#macro>