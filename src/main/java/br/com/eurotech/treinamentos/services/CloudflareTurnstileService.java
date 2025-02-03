package br.com.eurotech.treinamentos.services;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Service
public class CloudflareTurnstileService {

    @Value("${cloudflare.turnstile.secret}")
    private String secret;


    public CompletableFuture<Boolean> validaCaptchaAsync(String token) {
        String url = "https://challenges.cloudflare.com/turnstile/v0/siteverify";

        Map<String, String> formValues = new HashMap<>();
        formValues.put("secret", secret);
        formValues.put("response", token);

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(buildFormData(formValues))
                .build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        CaptchaModel captchaResult = objectMapper.readValue(response.body(), CaptchaModel.class);
                        return captchaResult.isSuccess();
                    } catch (IOException e) {
                        e.printStackTrace();
                        return false;
                    }
                });
    }

    private HttpRequest.BodyPublisher buildFormData(Map<String, String> formValues) {
        StringBuilder formData = new StringBuilder();
        for (Map.Entry<String, String> entry : formValues.entrySet()) {
            if (formData.length() != 0) {
                formData.append("&");
            }
            formData.append(entry.getKey())
                    .append("=")
                    .append(entry.getValue());
        }
        return HttpRequest.BodyPublishers.ofString(formData.toString());
    }

 
}

@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
class CaptchaModel {

    @JsonProperty("success")
    private boolean success;

    @JsonProperty("error-codes")
    private String[] errorCodes;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String[] getErrorCodes() {
        return errorCodes;
    }

    public void setErrorCodes(String[] errorCodes) {
        this.errorCodes = errorCodes;
    }
}