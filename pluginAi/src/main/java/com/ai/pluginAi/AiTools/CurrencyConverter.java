package com.ai.pluginAi.AiTools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class CurrencyConverter {

    private final RestClient restClient;
    private final String apiKey;

    public CurrencyConverter(RestClient.Builder builder, @Value("${currency.api.key}") String apiKey) {
        // Set the FastForex base URL from the documentation
        this.restClient = builder.baseUrl("https://api.fastforex.io").build();
        this.apiKey = apiKey;
    }

    @Tool(description = "Convert an amount from one currency or cryptocurrency into another.")
    public String convert(
            @ToolParam(description = "Base currency symbol, e.g., 'USD'")
            String from,
            @ToolParam(description = "Target currency symbol, e.g., 'INR'")
            String to,
            @ToolParam(description = "Amount of source currency to convert")
            float amount) {

        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/convert")
                        .queryParam("from", from)
                        .queryParam("to", to)
                        .queryParam("amount", amount)   // Correct parameter key
                        .queryParam("api_key", apiKey)  // Correct parameter key
                        .build())
                .retrieve()
                .body(String.class);
    }
}