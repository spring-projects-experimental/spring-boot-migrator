package org.springframework.sbm.boot.cleanup.actions;


import org.junit.jupiter.api.Test;

import org.springframework.sbm.boot.cleanup.actions.RemoveRedundantMavenCompilerPluginProperties;
import org.springframework.sbm.build.migration.actions.OpenRewriteMavenBuildFileTestSupport;

class RemoveRedundantMavenCompilerPluginPropertiesTest {

	@Test
	void mavenCompilerPluginNotDefined() {
		String pomXml =
				"""
      <?xml version="1.0" encoding="UTF-8"?>
      <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
               xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
          <modelVersion>4.0.0</modelVersion>
          <groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot-starter-parent</artifactId>
          <version>2.7.3</version>
          <name>clean-maven-properties</name>
          <description>Remove Redundant configuration</description>
          <build>
              <plugins>
                  <plugin>
                      <groupId>org.springframework.boot</groupId>
                                        <artifactId>spring-boot-maven-plugin</artifactId>
                  </plugin>
              </plugins>
          </build>
      </project>
                      """;

		String expected =
				"""
      <?xml version="1.0" encoding="UTF-8"?>
      <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
               xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
          <modelVersion>4.0.0</modelVersion>
          <groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot-starter-parent</artifactId>
          <version>2.7.3</version>
          <name>clean-maven-properties</name>
          <description>Remove Redundant configuration</description>
          <build>
              <plugins>
                  <plugin>
                      <groupId>org.springframework.boot</groupId>
                                        <artifactId>spring-boot-maven-plugin</artifactId>
                  </plugin>
              </plugins>
          </build>
      </project>
                      """;

		RemoveRedundantMavenCompilerPluginProperties sut = new RemoveRedundantMavenCompilerPluginProperties();
		OpenRewriteMavenBuildFileTestSupport.verifyRefactoring(pomXml, expected, sut);


	}
	@Test
	void sourceAndTargetVersionDifferent() {
		String pomXml = """
				<?xml version="1.0" encoding="UTF-8"?>
				<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
				         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
				    <modelVersion>4.0.0</modelVersion>
				    <groupId>org.springframework.boot</groupId>
				    <artifactId>spring-boot-starter-parent</artifactId>
				    <version>2.7.3</version>
				    <name>clean-maven-properties</name>
				    <description>Remove Redundant configuration</description>
				    <build>
				        <plugins>
				            <plugin>
				                <groupId>org.apache.maven.plugins</groupId>
				                <artifactId>maven-compiler-plugin</artifactId>
				                <configuration>
				                    <source>1.8</source>
				                    <target>1.6</target>
				                </configuration>
				            </plugin>
				        </plugins>
				    </build>
				</project>
				                """;

		String expected = """
				<?xml version="1.0" encoding="UTF-8"?>
				<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
				         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
				    <modelVersion>4.0.0</modelVersion>
				    <groupId>org.springframework.boot</groupId>
				    <artifactId>spring-boot-starter-parent</artifactId>
				    <version>2.7.3</version>
				    <name>clean-maven-properties</name>
				    <description>Remove Redundant configuration</description>
				    <build>
				        <plugins>
				            <plugin>
				                <groupId>org.apache.maven.plugins</groupId>
				                <artifactId>maven-compiler-plugin</artifactId>
				                <configuration>
				                    <source>1.8</source>
				                    <target>1.6</target>
				                </configuration>
				            </plugin>
				        </plugins>
				    </build>
				</project>
				                """;

		RemoveRedundantMavenCompilerPluginProperties sut = new RemoveRedundantMavenCompilerPluginProperties();
		OpenRewriteMavenBuildFileTestSupport.verifyRefactoring(pomXml, expected, sut);

	}

	@Test
	void mavenCompilerProperties() {
		String pomXml = """
				<?xml version="1.0" encoding="UTF-8"?>
				<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
				         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
				    <modelVersion>4.0.0</modelVersion>
				    <groupId>org.springframework.boot</groupId>
				    <artifactId>spring-boot-starter-parent</artifactId>
				    <version>2.7.3</version>
				    <name>clean-maven-properties</name>
				    <description>Remove Redundant configuration</description>
				    <properties>
				        <maven.compiler.source>17</maven.compiler.source>
				        <maven.compiler.target>17</maven.compiler.target>
				    </properties>
				    <build>
				        <plugins>
				            <plugin>
				                <groupId>org.apache.maven.plugins</groupId>
				                <artifactId>maven-compiler-plugin</artifactId>
				                <configuration>
				                    <source>${maven.compiler.source}</source>
				                    <target>${maven.compiler.target}</target>
				                </configuration>
				            </plugin>
				        </plugins>
				    </build>
				</project>
				                """;

		String expected = """
				<?xml version="1.0" encoding="UTF-8"?>
				<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
				         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
				    <modelVersion>4.0.0</modelVersion>
				    <groupId>org.springframework.boot</groupId>
				    <artifactId>spring-boot-starter-parent</artifactId>
				    <version>2.7.3</version>
				    <name>clean-maven-properties</name>
				    <description>Remove Redundant configuration</description>
				    <properties>
				        <java.version>17</java.version>
				    </properties>
				    <build>
				        <plugins>
				            <plugin>
				                <groupId>org.apache.maven.plugins</groupId>
				                <artifactId>maven-compiler-plugin</artifactId>
				                <configuration>
				                    <source>${maven.compiler.source}</source>
				                    <target>${maven.compiler.target}</target>
				                </configuration>
				            </plugin>
				        </plugins>
				    </build>
				</project>
				""";

		RemoveRedundantMavenCompilerPluginProperties sut = new RemoveRedundantMavenCompilerPluginProperties();
		OpenRewriteMavenBuildFileTestSupport.verifyRefactoring(pomXml, expected, sut);
	}

	@Test
	void customProperty() {
		String pomXml = """
				<?xml version="1.0" encoding="UTF-8"?>
				<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
				         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
				    <modelVersion>4.0.0</modelVersion>
				    <groupId>org.springframework.boot</groupId>
				    <artifactId>spring-boot-starter-parent</artifactId>
				    <version>2.7.3</version>
				    <name>clean-maven-properties</name>
				    <description>Remove Redundant configuration</description>
				    <properties>
				        <source>17</source>
				        <target>17</target>
				    </properties>
				    <build>
				        <plugins>
				            <plugin>
				                <groupId>org.apache.maven.plugins</groupId>
				                <artifactId>maven-compiler-plugin</artifactId>
				                <configuration>
				                    <source>${source}</source>
				                    <target>${target}</target>
				                </configuration>
				            </plugin>
				        </plugins>
				    </build>
				</project>
				                """;

		String expected = """
				<?xml version="1.0" encoding="UTF-8"?>
				<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
				         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
				    <modelVersion>4.0.0</modelVersion>
				    <groupId>org.springframework.boot</groupId>
				    <artifactId>spring-boot-starter-parent</artifactId>
				    <version>2.7.3</version>
				    <name>clean-maven-properties</name>
				    <description>Remove Redundant configuration</description>
				    <properties>
				        <java.version>17</java.version>
				    </properties>
				    <build>
				        <plugins>
				            <plugin>
				                <groupId>org.apache.maven.plugins</groupId>
				                <artifactId>maven-compiler-plugin</artifactId>
				                <configuration>
				                    <source>${maven.compiler.source}</source>
				                    <target>${maven.compiler.target}</target>
				                </configuration>
				            </plugin>
				        </plugins>
				    </build>
				</project>
				""";

		RemoveRedundantMavenCompilerPluginProperties sut = new RemoveRedundantMavenCompilerPluginProperties();
		OpenRewriteMavenBuildFileTestSupport.verifyRefactoring(pomXml, expected, sut);
	}

	@Test
	void fixedValueAndExistingJavaVersionProperty() {
		String pomXml = """
				<?xml version="1.0" encoding="UTF-8"?>
				<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
				         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
				    <modelVersion>4.0.0</modelVersion>
				    <groupId>org.springframework.boot</groupId>
				    <artifactId>spring-boot-starter-parent</artifactId>
				    <version>2.7.3</version>
				    <name>clean-maven-properties</name>
				    <description>Remove Redundant configuration</description>
				    <properties>
				        <java.version>15</java.version>				    
				    </properties>
				    <build>
				        <plugins>
				            <plugin>
				                <groupId>org.apache.maven.plugins</groupId>
				                <artifactId>maven-compiler-plugin</artifactId>
				                <configuration>
				                    <source>17</source>
				                    <target>17</target>
				                </configuration>
				            </plugin>
				        </plugins>
				    </build>
				</project>
				                """;

		String expected = """
				<?xml version="1.0" encoding="UTF-8"?>
				<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
				         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
				    <modelVersion>4.0.0</modelVersion>
				    <groupId>org.springframework.boot</groupId>
				    <artifactId>spring-boot-starter-parent</artifactId>
				    <version>2.7.3</version>
				    <name>clean-maven-properties</name>
				    <description>Remove Redundant configuration</description>
				    <properties>
				        <java.version>17</java.version>
				    </properties>
				    <build>
				        <plugins>
				            <plugin>
				                <groupId>org.apache.maven.plugins</groupId>
				                <artifactId>maven-compiler-plugin</artifactId>
				                <configuration>
				                    <source>${maven.compiler.source}</source>
				                    <target>${maven.compiler.target}</target>
				                </configuration>
				            </plugin>
				        </plugins>
				    </build>
				</project>
				""";

		RemoveRedundantMavenCompilerPluginProperties sut = new RemoveRedundantMavenCompilerPluginProperties();
		OpenRewriteMavenBuildFileTestSupport.verifyRefactoring(pomXml, expected, sut);
	}

	@Test
	void emptyConfiguration() {

		String pomXml = """
				<?xml version="1.0" encoding="UTF-8"?>
				<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
				         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
				    <modelVersion>4.0.0</modelVersion>
				    <groupId>org.springframework.boot</groupId>
				    <artifactId>spring-boot-starter-parent</artifactId>
				    <version>2.7.3</version>
				    <name>clean-maven-properties</name>
				    <description>Remove Redundant configuration</description>
				    <build>
				        <plugins>
				            <plugin>
				                <groupId>org.apache.maven.plugins</groupId>
				                <artifactId>maven-compiler-plugin</artifactId>
				            </plugin>
				        </plugins>
				    </build>
				</project>
				                """;
		String expected = """
				<?xml version="1.0" encoding="UTF-8"?>
				<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
				         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
				    <modelVersion>4.0.0</modelVersion>
				    <groupId>org.springframework.boot</groupId>
				    <artifactId>spring-boot-starter-parent</artifactId>
				    <version>2.7.3</version>
				    <name>clean-maven-properties</name>
				    <description>Remove Redundant configuration</description>
				    <build>
				        <plugins>
				            <plugin>
				                <groupId>org.apache.maven.plugins</groupId>
				                <artifactId>maven-compiler-plugin</artifactId>
				            </plugin>
				        </plugins>
				    </build>
				</project>
				""";

		RemoveRedundantMavenCompilerPluginProperties sut = new RemoveRedundantMavenCompilerPluginProperties();
		OpenRewriteMavenBuildFileTestSupport.verifyRefactoring(pomXml, expected, sut);
	}
}