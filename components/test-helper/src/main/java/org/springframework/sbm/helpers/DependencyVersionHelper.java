/*
 * Copyright 2021 - 2022 the original author or authors.
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
package org.springframework.sbm.helpers;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.Optional;


/**
 * Test helper that provides various information about the dependencies.
 *
 * @author Szymon Sadowski
 */
public class DependencyVersionHelper {
    public static final String MAVEN_DEPENDENCY_SEARCH_URL_TEMPLATE =
            "https://search.maven.org/solrsearch/select?q={0}&rows=1&wt=json";
    public static final String MAVEN_DEPENDENCY_QUERY_TEMPLATE = "g:\"{0}\" AND a:\"{1}\"";
    public static final String RESPONSE_JSON_KEY = "response";
    public static final String DOCS_JSON_KEY = "docs";
    public static final String LATEST_VERSION_JSON_KEY = "latestVersion";

    /**
     * Finds the latest release version for a given dependency.
     * @param groupId GroupId of a sought dependency.
     * @param artifactId ArtifactId of a sought dependency.
     * @return Optional string with the version number of the latest release for a given dependency
     * if the search was successful. Empty optional if the search was not successful.
     */
    public static Optional<String> getLatestReleaseVersion(String groupId, String artifactId) {
        String url = MessageFormat.format(
                MAVEN_DEPENDENCY_SEARCH_URL_TEMPLATE,
                URLEncoder.encode(
                        MessageFormat.format(MAVEN_DEPENDENCY_QUERY_TEMPLATE, groupId, artifactId),
                        StandardCharsets.UTF_8
                )
        );

        final HttpResponse<String> response;

        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request2 = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .GET()
                    .build();

            response = client.send(request2, HttpResponse.BodyHandlers.ofString());
        } catch (URISyntaxException | IOException | InterruptedException e) {
            return Optional.empty();
        }


        JSONObject json;

        try {
            json = new JSONParser(JSONParser.MODE_PERMISSIVE).parse(response.body(), JSONObject.class);
        } catch(ParseException jsonException) {
            return Optional.empty();
        }


        if (!json.containsKey(RESPONSE_JSON_KEY)) {
            return Optional.empty();
        }

        JSONObject responseSection = (JSONObject) json.get(RESPONSE_JSON_KEY);

        if(!responseSection.containsKey(DOCS_JSON_KEY)) {
            return Optional.empty();
        }

        JSONArray docs = (JSONArray) responseSection.get(DOCS_JSON_KEY);

        if (docs.size() == 0) {
            return Optional.empty();
        }

        JSONObject docEntry = (JSONObject) docs.get(0);

        if(!docEntry.containsKey(LATEST_VERSION_JSON_KEY)) {
            return Optional.empty();
        }

        return Optional.of(docEntry.getAsString(LATEST_VERSION_JSON_KEY));
    }
}
