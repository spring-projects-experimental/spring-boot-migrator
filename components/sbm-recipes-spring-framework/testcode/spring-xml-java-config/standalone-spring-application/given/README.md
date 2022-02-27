Standalone JDBC Template Spring Application
=============================

[![Build Status](https://drone.io/github.com/amusarra/standalone-spring-application/status.png)](https://drone.io/github.com/amusarra/standalone-spring-application/latest)

In this repository is available a sample project that shows how easy it is to implement a standalone application 
using the Spring Framework (v. 3.2.2) and is able to manipulate data in a database. 
Access to the database is done using the component's Spring JDBC Template.

This simple project shows how to implement simple CRUD operations (via JDBC Template) on an entity called Horse. The entity is defined as:
* Name: Name of the horse;
* Age: Age of horse;
* Type: Breed of the horse;
* Color Mantle
* Chip Id: ID chip of the horse.
	
The Spring JDBC Template has the following advantages compared with standard JDBC:
* The Spring JDBC template allows to clean-up the resources automatically, e.g. release the database connections;
* The Spring JDBC template converts the standard JDBC SQLExceptions into RuntimeExceptions. This allows the programmer to react more flexible to the errors.

For the configuration of the beans are used exclusively annotations.

Ready to get started? Very well. Follow these steps to run the sample application on the fly.

<pre>	
$ git clone git://github.com/amusarra/standalone-spring-application.git
$ cd standalone-spring-application/
$ mvn package
</pre>
List 1. Clone repository and build the package

<pre>
$ cd target/
$ java -jar standalonespringapplication-jar-with-dependencies.jar 
</pre>
List 2. Run the standalone application

<pre>
11-mar-2014 11.55.29 org.springframework.beans.factory.xml.XmlBeanDefinitionReader loadBeanDefinitions
INFO: Loading XML bean definitions from class path resource [applicationContext.xml]
11-mar-2014 11.55.30 org.springframework.context.support.AbstractApplicationContext prepareRefresh
INFO: Refreshing org.springframework.context.support.GenericXmlApplicationContext@789144: startup date [Tue Mar 11 11:55:30 CET 2014]; root of context hierarchy
11-mar-2014 11.55.30 org.springframework.core.io.support.PropertiesLoaderSupport loadProperties
INFO: Loading properties file from class path resource [database.properties]
11-mar-2014 11.55.30 org.springframework.beans.factory.support.DefaultListableBeanFactory preInstantiateSingletons
INFO: Pre-instantiating singletons in org.springframework.beans.factory.support.DefaultListableBeanFactory@da18ac: defining beans [org.springframework.context.annotation.internalConfigurationAnnotationProcessor,org.springframework.context.annotation.internalAutowiredAnnotationProcessor,org.springframework.context.annotation.internalRequiredAnnotationProcessor,org.springframework.context.annotation.internalCommonAnnotationProcessor,org.springframework.context.config.internalBeanConfigurerAspect,horseDAOImpl,org.springframework.beans.factory.config.PropertyPlaceholderConfigurer#0,dataSource,jdbcTemplate,org.springframework.context.annotation.ConfigurationClassPostProcessor.importAwareProcessor]; root of factory hierarchy
11-mar-2014 11.55.30 it.dontesta.spring.example.main.MinimalSpringApp main
INFO: Create a Horse table...
11-mar-2014 11.55.31 it.dontesta.spring.example.main.MinimalSpringApp main
INFO: Adding Horse data object...
11-mar-2014 11.55.31 it.dontesta.spring.example.main.MinimalSpringApp main
INFO: Retrieving data..
11-mar-2014 11.55.31 it.dontesta.spring.example.main.MinimalSpringApp main
INFO: From DB: Horse [age=9, chipID=742449734460126, colorMantle=Grigio, name=Shirus, type=Quarab]
11-mar-2014 11.55.31 it.dontesta.spring.example.main.MinimalSpringApp main
INFO: From DB: Horse [age=13, chipID=927720000545695, colorMantle=Pezzato con coperta, name=Eclisse, type=Appalousa]
11-mar-2014 11.55.31 it.dontesta.spring.example.main.MinimalSpringApp main
INFO: From DB: Horse [age=6, chipID=984976021983899, colorMantle=Baia, name=Morgana, type=Maremmana]
11-mar-2014 11.55.31 it.dontesta.spring.example.main.MinimalSpringApp main
INFO: From DB: Horse [age=12, chipID=301967237204226, colorMantle=Morello, name=Macchia, type=Appalousa]
11-mar-2014 11.55.31 it.dontesta.spring.example.main.MinimalSpringApp main
INFO: Deleting record macchia and viewing...
11-mar-2014 11.55.31 it.dontesta.spring.example.main.MinimalSpringApp main
INFO: From DB: Horse [age=9, chipID=742449734460126, colorMantle=Grigio, name=Shirus, type=Quarab]
11-mar-2014 11.55.31 it.dontesta.spring.example.main.MinimalSpringApp main
INFO: From DB: Horse [age=13, chipID=927720000545695, colorMantle=Pezzato con coperta, name=Eclisse, type=Appalousa]
11-mar-2014 11.55.31 it.dontesta.spring.example.main.MinimalSpringApp main
INFO: From DB: Horse [age=6, chipID=984976021983899, colorMantle=Baia, name=Morgana, type=Maremmana]
11-mar-2014 11.55.31 it.dontesta.spring.example.main.MinimalSpringApp main
INFO: From DB: Horse [age=12, chipID=301967237204226, colorMantle=Morello, name=Macchia, type=Appalousa]
11-mar-2014 11.55.31 it.dontesta.spring.example.main.MinimalSpringApp main
INFO: Adding a new record and viewing...
11-mar-2014 11.55.31 it.dontesta.spring.example.main.MinimalSpringApp main
INFO: From DB: Horse [age=9, chipID=742449734460126, colorMantle=Grigio, name=Shirus, type=Quarab]
11-mar-2014 11.55.31 it.dontesta.spring.example.main.MinimalSpringApp main
INFO: From DB: Horse [age=13, chipID=927720000545695, colorMantle=Pezzato con coperta, name=Eclisse, type=Appalousa]
11-mar-2014 11.55.31 it.dontesta.spring.example.main.MinimalSpringApp main
INFO: From DB: Horse [age=6, chipID=984976021983899, colorMantle=Baia, name=Morgana, type=Maremmana]
11-mar-2014 11.55.31 it.dontesta.spring.example.main.MinimalSpringApp main
INFO: From DB: Horse [age=12, chipID=301967237204226, colorMantle=Morello, name=Macchia, type=Appalousa]
11-mar-2014 11.55.31 it.dontesta.spring.example.main.MinimalSpringApp main
INFO: From DB: Horse [age=10, chipID=652694210034380, colorMantle=Morello, name=Furia, type=Appalousa]
11-mar-2014 11.55.31 it.dontesta.spring.example.main.MinimalSpringApp main
INFO: Deleting everything and viewing...
11-mar-2014 11.55.31 it.dontesta.spring.example.main.MinimalSpringApp main
INFO: Drop a Horse table...
</pre>
List 3. Output log

Enjoy!
