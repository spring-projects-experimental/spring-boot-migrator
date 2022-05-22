<#import "todo.ftl" as todo>
<#import "relevance.ftl" as relevance>
<#import "paragraph.ftl" as p/>

= Upgrade Spring Boot from 2.4 to 2.5
:toc:
:sectlinks:
:sectids:
:sectnums:
:sectnumlevels: 2
:source-highlighter: highlight.js
:highlightjs-languages: java

== Introduction

<#assign coordinates>${introductionSection.groupId}:${introductionSection.artifactId}:${introductionSection.version}</#assign>
[cols="1h,3"]
|===
| Datetime | ${introductionSection.datetime?datetime}
| Project dir | `${introductionSection.projectRoot.normalize().toAbsolutePath()}`
| Revision | <#if introductionSection.revision?has_content>`${introductionSection.revision}`<#else>No `.git` found in project dir</#if>
<#if introductionSection.projectName?has_content>
    | Project name |  ${introductionSection.projectName}
</#if>
| Coordinate | `${coordinates}`
| Boot version | `${introductionSection.bootVersion}`
<#if changeSections?has_content>
| Changes | ${changeSections?size}
</#if>
|===

The application was scanned and matched against the changes listed in the
https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-2.5-Release-Notes[Spring Boot 2.5 Release Notes]
as well as from https://github.com/spring-projects/spring-framework/wiki/Upgrading-to-Spring-Framework-5.x[Spring Framework 5.x Release Notes].

The Relevant Changes section lists all potentially required changes to upgrade the scanned application to Spring Boot 2.5.6.

=== Automated Migration
<#list changeSections as changeSection>
    <#list changeSection.todoSection.todoLists as todoList>
    <#if todoList.recipeName?has_content>
        <#assign recipesFound=true/>
    </#if>
    </#list>
</#list>
<#if recipesFound??>
Some changes listed in 'Relevant Changes' can be automated. +
It is advisable to read through 'Relevant Changes' to see if you want to apply all or just a subset of the applicable
recipes.

* Copy the following list of recipes to a file `recipes.txt`.
* Run Spring Boot Migrator and provide the list of recipes: +
`java -jar spring-boot-migrator.jar @recipes.txt`

[source,shell]
----
scan ${introductionSection.projectRoot.normalize().toAbsolutePath()}
<#list changeSections as changeSection>
    <#list changeSection.todoSection.todoLists as todoList>
    <#if todoList.recipeName?has_content>
apply ${todoList.recipeName}
    </#if>
    </#list>
</#list>
apply boot-2.4-2.5-upgrade-report
----
</#if>

== Relevant Changes

<#if changeSections?has_content>
<#list changeSections as changeSection>
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