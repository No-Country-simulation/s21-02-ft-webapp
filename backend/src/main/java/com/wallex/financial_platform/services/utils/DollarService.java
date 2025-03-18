package com.wallex.financial_platform.services.utils;

import com.wallex.financial_platform.dtos.responses.DollarResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Service
@Getter
public class DollarService {
    @Value("${dollar.api.base}")
    private String baseUrl;

    @Value("${dollar.api.dollar-oficial}")
    private String dollarOficialUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public BigDecimal getCurrentDollarValue() {
        String url = baseUrl + dollarOficialUrl;
        DollarResponse response = restTemplate.getForObject(url, DollarResponse.class);
        if (response != null) {
            return response.venta(); // Obtener el valor de venta del dólar oficial
        }
        throw new RuntimeException("No se pudo obtener el valor del dólar oficial");
    }
}