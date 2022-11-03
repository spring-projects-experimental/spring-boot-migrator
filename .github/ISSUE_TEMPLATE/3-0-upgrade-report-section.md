---
name: 3.0 Upgrade Report Section
about: Template for Spring Boot 3 Upgrade Report Section
title: '3.0 Upgrade Report: '
labels: 3.0.0, good first issue, upgrade:boot-report
assignees: ''

---

## What needs to be done

A report section for [`{{Release Note title}}`]({{http://link-to-section.foo}}) should be added to the Spring Boot 3 Upgrade Report.

The section in the report for this change must be provided in `YAML` which will be used to render the asciidoctor markup which will finally be used to render the report.

A class implementing the interface `SpringBootUpgradeReportSection.Helper` must be provided.
````java
    public interface Helper<T> extends Condition {
        /**
         * @return {@code Map<String, T>} the model data for the template.
         */
        Map<String, T> getData(ProjectContext context);
    }
````

The fully qualified name of this class must be referenced in the `YAML` describing the report section (see below).
It will be used as `Condition` to define if the section should rendered and
as `DataProvider` providing data to the freemarker template markup.


### Condition
{{Describe when the section should be rendered}}


### DoD
- [ ] Created `Helper` and referenced the fully qualified name in the yaml [[example](https://github.com/spring-projects-experimental/spring-boot-migrator/blob/8cc692233e949fc152126633ad0f69d9cd6c08e4/components/sbm-recipes-boot-upgrade/src/main/java/org/springframework/sbm/boot/upgrade_27_30/report/helper/ConstructorBindingHelper.java#L36)]
- [ ] Created test for the `Helper` against single and multi-module code (where applicable) [[example](https://github.com/spring-projects-experimental/spring-boot-migrator/blob/59ab7720d0961ec22cda9ed0bc48c78dd91fd1cf/components/sbm-recipes-boot-upgrade/src/test/java/org/springframework/sbm/boot/upgrade_27_30/report/helper/ConstructorBindingHelperTest.java#L25)]
- [ ] Created a test for the rendered section in asciidoctor markup
- [ ] Create a negative test using `shouldNotRender`


## YAML Example

This provides an example with information about how a report section can be defined in `YAML`.
[Freemarker syntax](https://freemarker.apache.org/docs/ref.html) can be used to render the asciidoctor code.

<details>
<summary>Report Section YAML (example)</summary>

````yaml
- title: Add the title from Release Notes section
  helper: fully qualified name of the Helper class
  change: |-
    Add the description from the Relase Notes section
    This can be multiline, ident (two spaces) is important
  sources: 
    - http://some-link-to-the-relase-note.html
    - http://some-link-to-some-other-relase-note.html
  affected: |-
    Why is the scanned application affected?
    Describes the matches of the `Finder` that made the condition for this section apply
    <#list matches as match>
      * file://${match.absolutePath}[`${match.relativePath}`]<#lt>
      <#list match.propertiesFound as property>
      ** `${property}`<#lt>
      </#list>
    </#list>
  remediation:
    description: |-
      Describe what the user needs to do to remediate this change in the scanned application.
      This should be as descriptive as possible and can potentially serve as the requirement 
      for a later migration recipe that migrates the steps.
      It is possible that different ways exist, in this case use this format
    possibilities:
      - title: The title of this remediation 
        description:  |-
          Detailed description of this approach and what the implications are
          Use checkboxes if there's a sequence of steps
          - [ ] Step 1
          - [ ] Step 2
        recipe: Name of the migration recipe for this remediation, if any
        resources:
          - Optional List of further resources like
          - blog.spring.io/some-blog-article
      - title: Title of the next remediation
        description:
          Detailed description of this approach and what the implications are
          ````java
          codeblocks can be used here, even containing codeblocks with freemarker template code
          like ${className}
          ````
  gitHubIssue: add the id of this issue, like 123
  projects: 
    - spring-boot
    - [Some other project]http://link-to-some-other.project
  contributors:
    - Fabian Krüger[@fabapp2]
    - Displayed Name[@GitHubName]
````

</details>

## Additional Resources & Information
