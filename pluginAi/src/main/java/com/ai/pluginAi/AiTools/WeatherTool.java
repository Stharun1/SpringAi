package com.ai.pluginAi.AiTools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Component
public class WeatherTool {

    private final RestClient restClient;
    private final String apiKey;

    public WeatherTool(RestClient.Builder builder, @Value("${weather.api.key}") String apiKey) {
        this.restClient = builder.baseUrl("https://api.openweathermap.org").build();
        this.apiKey = apiKey;
    }

    @Tool(description = """
        Retrieve real-time weather details and temperature
        for a specified city name using OpenWeather current weather data.
        """)
    public String getWeather(
            @ToolParam(description = "The name of the city to look up weather for, e.g., 'Kansas City'.")
            String city) {

        try {
            // Step 1: geocode city name -> lat/lon
            List<Map<String, Object>> geoResults = restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/geo/1.0/direct")
                            .queryParam("q", city)
                            .queryParam("limit", 1)
                            .queryParam("appid", apiKey)
                            .build())
                    .retrieve()
                    .body(List.class);

            if (geoResults == null || geoResults.isEmpty()) {
                return "Could not find location: " + city;
            }

            double lat = ((Number) geoResults.get(0).get("lat")).doubleValue();
            double lon = ((Number) geoResults.get(0).get("lon")).doubleValue();

            // Step 2: fetch weather by coordinates
            return restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/data/2.5/weather")
                            .queryParam("lat", lat)
                            .queryParam("lon", lon)
                            .queryParam("units", "metric")
                            .queryParam("appid", apiKey)
                            .build())
                    .retrieve()
                    .body(String.class);

        } catch (Exception e) {
            return "Error retrieving weather data: " + e.getMessage();
        }
    }
}