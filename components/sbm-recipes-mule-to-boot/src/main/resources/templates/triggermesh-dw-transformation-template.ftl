<#if packageName?has_content>
package ${packageName};

<#else>
package com.example.javadsl;

</#if>
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ${className} {
    public static class DataWeavePayload {
        public String input_data;
        public String spell;
        public String input_content_type;
        public String output_content_type;
    };

    public static String transform(TmDwPayload payload) {
        String uuid = payload.getId();
        String url = System.getenv("K_SINK");
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest.Builder requestBuilder;
        DataWeavePayload dwPayload = new DataWeavePayload();

        if (payload.getSourceType().contains(";")) {
            dwPayload.input_content_type = payload.getSourceType().split(";")[0];
        } else {
            dwPayload.input_content_type = payload.getSourceType();
        }
        dwPayload.output_content_type = "${outputContentType}";

        //TODO: Verify the spell conforms to Dataweave 2.x: https://docs.mulesoft.com/mule-runtime/4.4/migration-dataweave
        dwPayload.spell = "${dwSpell}";
        dwPayload.input_data = payload.getPayload();
        String body;

        try {
            requestBuilder = HttpRequest.newBuilder(new URI(url));
            ObjectMapper om = new ObjectMapper();
            body = om.writeValueAsString(dwPayload);
        } catch (Exception e) {
            System.out.println("Error sending request: " + e.toString());
            return null;
        }

        requestBuilder.setHeader("content-type", "application/json");
        requestBuilder.setHeader("ce-specversion", "1.0");
        requestBuilder.setHeader("ce-source", payload.getSource());
        requestBuilder.setHeader("ce-type", "io.triggermesh.dataweave.transform");
        requestBuilder.setHeader("ce-id", payload.getId());

        HttpRequest request = requestBuilder.POST(HttpRequest.BodyPublishers.ofString(body)).build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            // TODO: verify the response status and body
            return response.body();
        } catch (Exception e) {
            System.out.println("Error sending event: " + e.toString());
            return null;
        }
    }
}
