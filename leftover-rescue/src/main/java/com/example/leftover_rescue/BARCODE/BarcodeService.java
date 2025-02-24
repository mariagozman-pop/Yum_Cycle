/*
package com.example.leftover_rescue.BARCODE;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class BarcodeService {

    private final RestTemplate restTemplate;

    @Value("${national.aliments.api.url}")
    private String apiUrl; // External API URL

    public BarcodeService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // Method to fetch product details from the external barcode database
    public ScannedProduct getProductDetails(String barcode) throws Exception {
        String url = apiUrl + "/product/" + barcode; // Modify this URL based on the external API's format

        try {
            // Send a GET request to the external API
            ResponseEntity<ScannedProduct> response = restTemplate.exchange(url, HttpMethod.GET, null, ScannedProduct.class);

            // Return the response (the product details)
            return response.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new Exception("Error fetching product data from external API", e);
        }
    }
}
*/
