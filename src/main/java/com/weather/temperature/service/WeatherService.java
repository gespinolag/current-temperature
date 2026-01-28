package com.weather.temperature.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weather.temperature.model.Temperature;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class WeatherService {
    private static final String URL = "https://www.meteorologia.gov.py/emas/data.json";
    private final ConcurrentHashMap<String, Temperature> temperatureCache = new ConcurrentHashMap<>();

    public Temperature fetchTemperature(String codigo) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            String jsonResponse = restTemplate.getForObject(URL, String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonResponse);

            JsonNode stationNode = rootNode.path("estaciones").path(codigo);
            if (stationNode.isMissingNode() || stationNode.path("ultima_observacion").path("temperatura_aire").isMissingNode()) {
                return null;
            }
            //String valor = stationNode.path("ultima_observacion").path("temperatura_aire").path("valor").asText() + "°";
            double temperaturaAire = stationNode.path("ultima_observacion").path("temperatura_aire").path("valor").asDouble();
            //String valor = String.format(Locale.US,"%.1f", temperaturaAire) + "°";
            String valor;

            valor = String.format(Locale.US, "%.1f", temperaturaAire) + "°";

            /*if (temperaturaAire % 1 == 0) {
                valor = String.format(Locale.US, "%.0f", temperaturaAire) + "°";
            } else {
                valor = String.format(Locale.US, "%.1f", temperaturaAire) + "°";
            }*/

            Temperature temperature = new Temperature(valor);
            temperatureCache.put(codigo, temperature);
        } catch (IOException e) {
            return null;
        }
        return temperatureCache.get(codigo);
    }

    public Temperature getTemperatureByCode(String codigo) {
        //return temperatureCache.getOrDefault(codigo, fetchTemperature(codigo));
        Temperature temperature = temperatureCache.get(codigo);
        if (temperature == null) {
            return fetchTemperature(codigo);
        }
        return temperature;
    }

    public Iterable<String> getAllCachedCodigos() {
        return temperatureCache.keySet();
    }
}
