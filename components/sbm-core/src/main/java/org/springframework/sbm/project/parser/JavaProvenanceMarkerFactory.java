package org.springframework.sbm.project.parser;

import org.jetbrains.annotations.NotNull;
import org.openrewrite.ExecutionContext;
import org.openrewrite.java.marker.JavaProject;
import org.openrewrite.java.marker.JavaVersion;
import org.openrewrite.marker.BuildTool;
import org.openrewrite.marker.Marker;
import org.openrewrite.maven.tree.MavenResolutionResult;
import org.openrewrite.maven.tree.Pom;
import org.openrewrite.xml.tree.Xml;
import org.springframework.sbm.build.impl.MavenBuildFileUtil;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.openrewrite.Tree.randomId;

@Component
public class JavaProvenanceMarkerFactory {

    private static final Pattern mavenWrapperVersionPattern = Pattern.compile(".*apache-maven/(.*?)/.*");
    public List<Marker> getJavaProvenanceMarkers(Xml.Document maven, Path projectDirectory, ExecutionContext ctx) {
            MavenResolutionResult mavenResolution = MavenBuildFileUtil.findMavenResolution(maven).get();
            Pom mavenModel = mavenResolution.getPom().getRequested();
            String javaRuntimeVersion = System.getProperty("java.runtime.version");
            String javaVendor = System.getProperty("java.vm.vendor");
            String sourceCompatibility = javaRuntimeVersion;
            String targetCompatibility = javaRuntimeVersion;
            String propertiesSourceCompatibility = mavenModel.getProperties().get("maven.compiler.source");
            if (propertiesSourceCompatibility != null) {
                sourceCompatibility = propertiesSourceCompatibility;
            }
            String propertiesTargetCompatibility = mavenModel.getProperties().get("maven.compiler.target");
            if (propertiesTargetCompatibility != null) {
                targetCompatibility = propertiesTargetCompatibility;
            }

            Path wrapperPropertiesPath = projectDirectory.resolve(".mvn/wrapper/maven-wrapper.properties");
            String mavenVersion = "3.6";
            if (Files.exists(wrapperPropertiesPath)) {
                try {
                    Properties wrapperProperties = new Properties();
                    wrapperProperties.load(new FileReader(wrapperPropertiesPath.toFile()));
                    String distributionUrl = (String) wrapperProperties.get("distributionUrl");
                    if (distributionUrl != null) {
                        Matcher wrapperVersionMatcher = mavenWrapperVersionPattern.matcher(distributionUrl);
                        if (wrapperVersionMatcher.matches()) {
                            mavenVersion = wrapperVersionMatcher.group(1);
                        }
                    }
                } catch (IOException e) {
                    ctx.getOnError().accept(e);
                }
            }

            return Arrays.asList(
                    new BuildTool(randomId(), BuildTool.Type.Maven, mavenVersion),
                    new JavaVersion(randomId(), javaRuntimeVersion, javaVendor, sourceCompatibility, targetCompatibility),
                    new JavaProject(randomId(), mavenModel.getName(), new JavaProject.Publication(
                            mavenModel.getGroupId(),
                            mavenModel.getArtifactId(),
                            mavenModel.getVersion()
                    ))
            );
        }
}
