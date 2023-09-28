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
package org.springframework.sbm.parsers;

import org.apache.commons.cli.*;
import org.apache.maven.cli.CleanArgument;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Parse {@code .mvn/maven.config/} and provide access to relevant settings.
 * Code thankfully taken from <a href="https://github.com/apache/maven/blob/857a5e129ad8dad05495ba631818b55f1925d210/maven-embedder/src/main/java/org/apache/maven/cli/CLIManager.java">org.apache.maven.cli.CLIManager</a>.
 *
 * @author Fabian Krüger
 */
class MavenConfigFileParser {

    /** Constant <code>ALTERNATE_POM_FILE='f'</code> */
    private static final char ALTERNATE_POM_FILE = 'f';

    /** Constant <code>BATCH_MODE='B'</code> */
    private static final char BATCH_MODE = 'B';

    /** Constant <code>SET_USER_PROPERTY='D'</code> */
    private static final char SET_USER_PROPERTY = 'D';

    /**
     * @deprecated Use {@link #SET_USER_PROPERTY}
     */
    @Deprecated
    private static final char SET_SYSTEM_PROPERTY = SET_USER_PROPERTY;

    /** Constant <code>OFFLINE='o'</code> */
    private static final char OFFLINE = 'o';

    /** Constant <code>QUIET='q'</code> */
    private static final char QUIET = 'q';

    /** Constant <code>DEBUG='X'</code> */
    private static final char DEBUG = 'X';

    /** Constant <code>ERRORS='e'</code> */
    private static final char ERRORS = 'e';

    /** Constant <code>HELP='h'</code> */
    private static final char HELP = 'h';

    /** Constant <code>VERSION='v'</code> */
    private static final char VERSION = 'v';

    /** Constant <code>SHOW_VERSION='V'</code> */
    private static final char SHOW_VERSION = 'V';

    /** Constant <code>NON_RECURSIVE='N'</code> */
    private static final char NON_RECURSIVE = 'N';

    /** Constant <code>UPDATE_SNAPSHOTS='U'</code> */
    private static final char UPDATE_SNAPSHOTS = 'U';

    /** Constant <code>ACTIVATE_PROFILES='P'</code> */
    private static final char ACTIVATE_PROFILES = 'P';

    /** Constant <code>SUPRESS_SNAPSHOT_UPDATES="nsu"</code> */
    private static final String SUPRESS_SNAPSHOT_UPDATES = "nsu";

    /** Constant <code>CHECKSUM_FAILURE_POLICY='C'</code> */
    private static final char CHECKSUM_FAILURE_POLICY = 'C';

    /** Constant <code>CHECKSUM_WARNING_POLICY='c'</code> */
    private static final char CHECKSUM_WARNING_POLICY = 'c';

    /** Constant <code>ALTERNATE_USER_SETTINGS='s'</code> */
    private static final char ALTERNATE_USER_SETTINGS = 's';

    /** Constant <code>ALTERNATE_GLOBAL_SETTINGS="gs"</code> */
    private static final String ALTERNATE_GLOBAL_SETTINGS = "gs";

    /** Constant <code>ALTERNATE_USER_TOOLCHAINS='t'</code> */
    private static final char ALTERNATE_USER_TOOLCHAINS = 't';

    /** Constant <code>ALTERNATE_GLOBAL_TOOLCHAINS="gt"</code> */
    private static final String ALTERNATE_GLOBAL_TOOLCHAINS = "gt";

    /** Constant <code>FAIL_FAST="ff"</code> */
    private static final String FAIL_FAST = "ff";

    /** Constant <code>FAIL_AT_END="fae"</code> */
    private static final String FAIL_AT_END = "fae";

    /** Constant <code>FAIL_NEVER="fn"</code> */
    private static final String FAIL_NEVER = "fn";

    /** Constant <code>RESUME_FROM="rf"</code> */
    private static final String RESUME_FROM = "rf";

    /** Constant <code>PROJECT_LIST="pl"</code> */
    private static final String PROJECT_LIST = "pl";

    /** Constant <code>ALSO_MAKE="am"</code> */
    private static final String ALSO_MAKE = "am";

    /** Constant <code>ALSO_MAKE_DEPENDENTS="amd"</code> */
    private static final String ALSO_MAKE_DEPENDENTS = "amd";

    /** Constant <code>LOG_FILE="l"</code> */
    private static final String LOG_FILE = "l";

    /** Constant <code>ENCRYPT_MASTER_PASSWORD="emp"</code> */
    private static final String ENCRYPT_MASTER_PASSWORD = "emp";

    /** Constant <code>ENCRYPT_PASSWORD="ep"</code> */
    private static final String ENCRYPT_PASSWORD = "ep";

    /** Constant <code>THREADS="T"</code> */
    private static final String THREADS = "T";

    /** Constant <code>BUILDER="b"</code> */
    private static final String BUILDER = "b";

    /** Constant <code>NO_TRANSFER_PROGRESS="ntp"</code> */
    private static final String NO_TRANSFER_PROGRESS = "ntp";

    /** Constant <code>COLOR="color"</code> */
    private static final String COLOR = "color";
    private static final String MVN_MAVEN_CONFIG = ".mvn/maven.config";
    public List<String> getActivatedProfiles(Path baseDir) {
        File configFile = baseDir.resolve(MVN_MAVEN_CONFIG).toFile();
        if (configFile.isFile()) {
            try (Stream<String> lines = Files.lines(configFile.toPath(), Charset.defaultCharset())) {
                String[] args = readFile(lines);
                return parse(args).stream()
                        .filter(o -> String.valueOf(ACTIVATE_PROFILES).equals(o.getOpt()))
                        .map(o -> o.getValue())
                        .map(v -> v.split(","))
                        .flatMap(Arrays::stream)
                        .map(String::trim)
                        .toList();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            return List.of();
        }
    }

    public Map<String, String> getUserProperties(Path baseDir) {
        File configFile = baseDir.resolve(MVN_MAVEN_CONFIG).toFile();
        if (configFile.isFile()) {
            try (Stream<String> lines = Files.lines(configFile.toPath(), Charset.defaultCharset())) {
                String[] args = readFile(lines);
                return parse(args).stream()
                        .filter(o -> String.valueOf(SET_USER_PROPERTY).equals(o.getOpt()))
                        .map(o -> o.getValue())
                        .filter(v -> v.contains("="))
                        .map(v -> v.split("="))
                        .collect(Collectors.toMap(a -> a[0], a -> a[1]));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            return Map.of();
        }
    }

    @NotNull
    private static String[] readFile(Stream<String> lines) {
        return lines.filter(arg -> !arg.isEmpty() && !arg.startsWith("#"))
                .toArray(String[]::new);
    }

    public List<Option> parse(String[] args) {
        Options options = new Options();
        options.addOption(Option.builder(Character.toString(HELP))
                .longOpt("help")
                .desc("Display help information")
                .build());
        options.addOption(Option.builder(Character.toString(ALTERNATE_POM_FILE))
                .longOpt("file")
                .hasArg()
                .desc("Force the use of an alternate POM file (or directory with pom.xml)")
                .build());
        options.addOption(Option.builder(Character.toString(SET_USER_PROPERTY))
                .longOpt("define")
                .hasArg()
                .desc("Define a user property")
                .build());
        options.addOption(Option.builder(Character.toString(OFFLINE))
                .longOpt("offline")
                .desc("Work offline")
                .build());
        options.addOption(Option.builder(Character.toString(VERSION))
                .longOpt("version")
                .desc("Display version information")
                .build());
        options.addOption(Option.builder(Character.toString(QUIET))
                .longOpt("quiet")
                .desc("Quiet output - only show errors")
                .build());
        options.addOption(Option.builder(Character.toString(DEBUG))
                .longOpt("debug")
                .desc("Produce execution debug output")
                .build());
        options.addOption(Option.builder(Character.toString(ERRORS))
                .longOpt("errors")
                .desc("Produce execution error messages")
                .build());
        options.addOption(Option.builder(Character.toString(NON_RECURSIVE))
                .longOpt("non-recursive")
                .desc("Do not recurse into sub-projects")
                .build());
        options.addOption(Option.builder(Character.toString(UPDATE_SNAPSHOTS))
                .longOpt("update-snapshots")
                .desc("Forces a check for missing releases and updated snapshots on remote repositories")
                .build());
        options.addOption(Option.builder(Character.toString(ACTIVATE_PROFILES))
                .longOpt("activate-profiles")
                .desc("Comma-delimited list of profiles to activate")
                .hasArg()
                .build());
        options.addOption(Option.builder(Character.toString(BATCH_MODE))
                .longOpt("batch-mode")
                .desc("Run in non-interactive (batch) mode (disables output color)")
                .build());
        options.addOption(Option.builder(SUPRESS_SNAPSHOT_UPDATES)
                .longOpt("no-snapshot-updates")
                .desc("Suppress SNAPSHOT updates")
                .build());
        options.addOption(Option.builder(Character.toString(CHECKSUM_FAILURE_POLICY))
                .longOpt("strict-checksums")
                .desc("Fail the build if checksums don't match")
                .build());
        options.addOption(Option.builder(Character.toString(CHECKSUM_WARNING_POLICY))
                .longOpt("lax-checksums")
                .desc("Warn if checksums don't match")
                .build());
        options.addOption(Option.builder(Character.toString(ALTERNATE_USER_SETTINGS))
                .longOpt("settings")
                .desc("Alternate path for the user settings file")
                .hasArg()
                .build());
        options.addOption(Option.builder(ALTERNATE_GLOBAL_SETTINGS)
                .longOpt("global-settings")
                .desc("Alternate path for the global settings file")
                .hasArg()
                .build());
        options.addOption(Option.builder(Character.toString(ALTERNATE_USER_TOOLCHAINS))
                .longOpt("toolchains")
                .desc("Alternate path for the user toolchains file")
                .hasArg()
                .build());
        options.addOption(Option.builder(ALTERNATE_GLOBAL_TOOLCHAINS)
                .longOpt("global-toolchains")
                .desc("Alternate path for the global toolchains file")
                .hasArg()
                .build());
        options.addOption(Option.builder(FAIL_FAST)
                .longOpt("fail-fast")
                .desc("Stop at first failure in reactorized builds")
                .build());
        options.addOption(Option.builder(FAIL_AT_END)
                .longOpt("fail-at-end")
                .desc("Only fail the build afterwards; allow all non-impacted builds to continue")
                .build());
        options.addOption(Option.builder(FAIL_NEVER)
                .longOpt("fail-never")
                .desc("NEVER fail the build, regardless of project result")
                .build());
        options.addOption(Option.builder(RESUME_FROM)
                .longOpt("resume-from")
                .hasArg()
                .desc("Resume reactor from specified project")
                .build());
        options.addOption(Option.builder(PROJECT_LIST)
                .longOpt("projects")
                .desc(
                        "Comma-delimited list of specified reactor projects to build instead of all projects. A project can be specified by [groupId]:artifactId or by its relative path")
                .hasArg()
                .build());
        options.addOption(Option.builder(ALSO_MAKE)
                .longOpt("also-make")
                .desc("If project list is specified, also build projects required by the list")
                .build());
        options.addOption(Option.builder(ALSO_MAKE_DEPENDENTS)
                .longOpt("also-make-dependents")
                .desc("If project list is specified, also build projects that depend on projects on the list")
                .build());
        options.addOption(Option.builder(LOG_FILE)
                .longOpt("log-file")
                .hasArg()
                .desc("Log file where all build output will go (disables output color)")
                .build());
        options.addOption(Option.builder(Character.toString(SHOW_VERSION))
                .longOpt("show-version")
                .desc("Display version information WITHOUT stopping build")
                .build());
        options.addOption(Option.builder(ENCRYPT_MASTER_PASSWORD)
                .longOpt("encrypt-master-password")
                .hasArg()
                .optionalArg(true)
                .desc("Encrypt master security password")
                .build());
        options.addOption(Option.builder(ENCRYPT_PASSWORD)
                .longOpt("encrypt-password")
                .hasArg()
                .optionalArg(true)
                .desc("Encrypt server password")
                .build());
        options.addOption(Option.builder(THREADS)
                .longOpt("threads")
                .hasArg()
                .desc("Thread count, for instance 4 (int) or 2C/2.5C (int/float) where C is core multiplied")
                .build());
        options.addOption(Option.builder(BUILDER)
                .longOpt("builder")
                .hasArg()
                .desc("The id of the build strategy to use")
                .build());
        options.addOption(Option.builder(NO_TRANSFER_PROGRESS)
                .longOpt("no-transfer-progress")
                .desc("Do not display transfer progress when downloading or uploading")
                .build());

        // Adding this back in for compatibility with the verifier that hard codes this option.
        options.addOption(Option.builder("npr")
                .longOpt("no-plugin-registry")
                .desc("Ineffective, only kept for backward compatibility")
                .build());
        options.addOption(Option.builder("cpu")
                .longOpt("check-plugin-updates")
                .desc("Ineffective, only kept for backward compatibility")
                .build());
        options.addOption(Option.builder("up")
                .longOpt("update-plugins")
                .desc("Ineffective, only kept for backward compatibility")
                .build());
        options.addOption(Option.builder("npu")
                .longOpt("no-plugin-updates")
                .desc("Ineffective, only kept for backward compatibility")
                .build());

        // Adding this back to make Maven fail if used
        options.addOption(Option.builder("llr")
                .longOpt("legacy-local-repository")
                .desc("UNSUPPORTED: Use of this option will make Maven invocation fail.")
                .build());

        options.addOption(Option.builder()
                .longOpt(COLOR)
                .hasArg()
                .optionalArg(true)
                .desc("Defines the color mode of the output. Supported are 'auto', 'always', 'never'.")
                .build());

        // We need to eat any quotes surrounding arguments...
        String[] cleanArgs = CleanArgument.cleanArgs(args);

        CommandLineParser parser = new GnuParser();

        try {
            CommandLine parsedOptions = parser.parse(options, cleanArgs);
            return Arrays.asList(parsedOptions.getOptions());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
