/*
 * Copyright 2021 - 2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.sbm.parsers.maven;

import lombok.RequiredArgsConstructor;
import org.openrewrite.ExecutionContext;
import org.openrewrite.internal.lang.Nullable;
import org.openrewrite.maven.MavenExecutionContextView;
import org.openrewrite.maven.MavenSettings;
import org.openrewrite.maven.internal.RawRepositories;
import org.openrewrite.maven.tree.MavenRepository;
import org.openrewrite.maven.tree.ProfileActivation;
import org.springframework.sbm.scopes.ProjectMetadata;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Initialize {@link MavenSettings} with information from {@code ~/.m2/settings.xml} and
 * {@code settings-security.xml}.
 *
 * @author Fabian Krüger
 */
@Component
@RequiredArgsConstructor
public class MavenSettingsInitializer {

	private final ExecutionContext executionContext;

	private final ProjectMetadata projectMetadata;

	private final MavenPasswordDecrypter passwordDecrypter;

	public MavenSettingsInitializer(ExecutionContext executionContext, ProjectMetadata projectMetadata) {
		this.executionContext = executionContext;
		this.projectMetadata = projectMetadata;
		passwordDecrypter = new MavenPasswordDecrypter();
	}

	public void initializeMavenSettings() {
		Path userHome = Path.of(System.getProperty("user.home"));
		String m2RepoPath = userHome.resolve(".m2/repository").toAbsolutePath().normalize() + "/";
		String repo = "file://" + m2RepoPath;
		Path mavenSettingsFile = userHome.resolve(".m2/settings.xml");
		Path mavenSecuritySettingsFile = userHome.resolve(".m2/settings-security.xml");

		MavenRepository mavenRepository = new MavenRepository("local", repo, null, null, true, null, null, null);
		MavenSettings.Profile defaultProfile = new MavenSettings.Profile("default", null, new RawRepositories());
		MavenSettings.@Nullable Profiles profiles = new MavenSettings.Profiles(List.of(defaultProfile));
		MavenSettings.@Nullable ActiveProfiles activeProfiles = new MavenSettings.ActiveProfiles(List.of("default"));
		MavenSettings.@Nullable Mirrors mirrors = new MavenSettings.Mirrors();
		MavenSettings.Servers servers = new MavenSettings.Servers();
		MavenSettings mavenSettings = new MavenSettings(m2RepoPath, mavenRepository, profiles, activeProfiles, mirrors,
				servers);

		// TODO: Add support for global Maven settings (${maven.home}/conf/settings.xml).
		MavenExecutionContextView mavenExecutionContextView = MavenExecutionContextView.view(executionContext);
		if (Files.exists(mavenSettingsFile)) {
			mavenSettings = mavenSettings.merge(MavenSettings.parse(mavenSettingsFile, mavenExecutionContextView));
			if (mavenSecuritySettingsFile.toFile().exists()) {
				passwordDecrypter.decryptMavenServerPasswords(mavenSettings, mavenSecuritySettingsFile);
			}
		}
		//
		// if(mavenSettings.getMavenLocal() == null) {
		// mavenSettings.setMavenLocal(new MavenRepository("local", repo, null, null,
		// true, null, null, null));
		// }
		//
		// if(mavenSettings.getActiveProfiles() == null) {
		// mavenSettings.set
		// }

		mavenExecutionContextView.setMavenSettings(mavenSettings);
		projectMetadata.setMavenSettings(mavenSettings);
	}

}
