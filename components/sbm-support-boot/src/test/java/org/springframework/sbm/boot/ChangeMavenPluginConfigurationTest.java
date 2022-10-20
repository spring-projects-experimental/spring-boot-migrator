package org.springframework.sbm.boot;

import org.junit.jupiter.api.Test;

import org.springframework.sbm.openrewrite.MavenRefactoringTestHelper;

class ChangeMavenPluginConfigurationTest {

	@Test
	void happyPath() {
		String given = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
				+ "            <project xmlns=\"http://maven.apache.org/POM/4.0.0\"\n"
				+ "                     xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
				+ "                     xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n"
				+ "                <modelVersion>4.0.0</modelVersion>\n"
				+ "                <parent>\n"
				+ "                    <groupId>org.springframework.boot</groupId>\n"
				+ "                    <artifactId>spring-boot-starter-parent</artifactId>\n"
				+ "                    <version>2.7.3</version>\n"
				+ "                </parent>\n"
				+ "                <artifactId>module1</artifactId>\n"
				+ "                <properties>\n"
				+ "                    <test1>17</test1>\n"
				+ "                    <test2>17</test2>\n"
				+ "                </properties>\n"
				+ "                <build>\n"
				+ "                    <plugins>\n"
				+ "                        <plugin>\n"
				+ "                            <groupId>org.apache.maven.plugins</groupId>\n"
				+ "                            <artifactId>maven-compiler-plugin</artifactId>\n"
				+ "                            <configuration>\n"
				+ "                                <source>${test1}</source>\n"
				+ "                                <target>${test2}</target>\n"
				+ "                            </configuration>\n"
				+ "                        </plugin>\n"
				+ "                    </plugins>\n"
				+ "                </build>\n"
				+ "            </project>";

		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
				+ "            <project xmlns=\"http://maven.apache.org/POM/4.0.0\"\n"
				+ "                     xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
				+ "                     xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n"
				+ "                <modelVersion>4.0.0</modelVersion>\n"
				+ "                <parent>\n"
				+ "                    <groupId>org.springframework.boot</groupId>\n"
				+ "                    <artifactId>spring-boot-starter-parent</artifactId>\n"
				+ "                    <version>2.7.3</version>\n"
				+ "                </parent>\n"
				+ "                <artifactId>module1</artifactId>\n"
				+ "                <properties>\n"
				+ "                    <java.version>17</java.version>\n"
				+ "                </properties>\n"
				+ "                <build>\n"
				+ "                    <plugins>\n"
				+ "                        <plugin>\n"
				+ "                            <groupId>org.apache.maven.plugins</groupId>\n"
				+ "                            <artifactId>maven-compiler-plugin</artifactId>\n"
				+ "                            <configuration>\n"
				+ "                                <source>${maven.compiler.source}</source>\n"
				+ "                                <target>${maven.compiler.target}</target>\n"
				+ "                            </configuration>\n"
				+ "                        </plugin>\n"
				+ "                    </plugins>\n"
				+ "                </build>\n"
				+ "            </project>";

		ChangeMavenCompilerPluginConfiguration sut = new ChangeMavenCompilerPluginConfiguration();
		MavenRefactoringTestHelper.verifyChange(given, expected, sut);
	}

	@Test
	void emptyMavenCompilerConfiguration() {
		String given = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
				+ "            <project xmlns=\"http://maven.apache.org/POM/4.0.0\"\n"
				+ "                     xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
				+ "                     xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n"
				+ "                <modelVersion>4.0.0</modelVersion>\n"
				+ "                <parent>\n"
				+ "                    <groupId>org.springframework.boot</groupId>\n"
				+ "                    <artifactId>spring-boot-starter-parent</artifactId>\n"
				+ "                    <version>2.7.3</version>\n"
				+ "                </parent>\n"
				+ "                <artifactId>module1</artifactId>\n"
				+ "                <properties>\n"
				+ "                </properties>\n"
				+ "                <build>\n"
				+ "                    <plugins>\n"
				+ "                        <plugin>\n"
				+ "                            <groupId>org.apache.maven.plugins</groupId>\n"
				+ "                            <artifactId>maven-compiler-plugin</artifactId>\n"
				+ "                        </plugin>\n"
				+ "                    </plugins>\n"
				+ "                </build>\n"
				+ "            </project>";

		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
				+ "            <project xmlns=\"http://maven.apache.org/POM/4.0.0\"\n"
				+ "                     xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
				+ "                     xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n"
				+ "                <modelVersion>4.0.0</modelVersion>\n"
				+ "                <parent>\n"
				+ "                    <groupId>org.springframework.boot</groupId>\n"
				+ "                    <artifactId>spring-boot-starter-parent</artifactId>\n"
				+ "                    <version>2.7.3</version>\n"
				+ "                </parent>\n"
				+ "                <artifactId>module1</artifactId>\n"
				+ "                <properties>\n"
				+ "                </properties>\n"
				+ "                <build>\n"
				+ "                    <plugins>\n"
				+ "                        <plugin>\n"
				+ "                            <groupId>org.apache.maven.plugins</groupId>\n"
				+ "                            <artifactId>maven-compiler-plugin</artifactId>\n"
				+ "                            <configuration>\n"
				+ "                                <source>${maven.compiler.source}</source>\n"
				+ "                                <target>${maven.compiler.target}</target>\n"
				+ "                            </configuration>\n"
				+ "                        </plugin>\n"
				+ "                    </plugins>\n"
				+ "                </build>\n"
				+ "            </project>";

		ChangeMavenCompilerPluginConfiguration sut = new ChangeMavenCompilerPluginConfiguration();
		MavenRefactoringTestHelper.verifyChange(given, expected, sut);
	}

}