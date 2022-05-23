## [0.11.1](https://github.com/spring-projects-experimental/spring-boot-migrator/releases/tag/0.11.1) - 2022-05-23

### Adds
- Migrate all properties to @Stateless when migrating ejb-jar.xml to annotation (#56). Thanks, @ravig-kant
- Support for <db:select /> in mule (#119). Thanks @sanagaraj-pivotal

### Fixes
- recipesFound null in upgrade-asciidoc.ftl:46 breaks HTML report generation (#124). Thanks, @timtebeek

## [0.11.0](https://github.com/spring-projects-experimental/spring-boot-migrator/releases/tag/0.11.0) -  

### Adds
- Unmarshalling ejb-jar.xml for EJB 2.1 (#62) 
- Demo for Mule to Boot migration (#80)
- Demo for Spring Boot 2.4 to 2.5 upgrade (#120)
- New Mule 3.9 components and schemas (#110, #95, #87)
- Bumped some dependency versions

### Fixes
- Paths and CLI rendering under Windows (#58)
- Fix SBM when using Windows (#58)
- Bump some dependency versions, removes CVEs
- SBM generates Asciidoc report when started as jar
- 2.4 to 2.5 upgrade does not randomly fail anymore
- Full Windows support (#91)
- Builds on Mac M1 (#114) 

## [0.10.0](https://github.com/spring-projects-experimental/spring-boot-migrator/releases/tag/0.10.0) -  2022-03-28

### Adds
- Support to migrate to Spring Cloud Config Server
- Support to migrate from Mule to Spring Boot
- More status information is displayed during scan
- Precondition check before scan (#15)
- Upgraded to OpenRewrite 7.16.3
