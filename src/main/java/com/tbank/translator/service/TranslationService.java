package com.tbank.translator.service;

import com.tbank.translator.requests.YandexTranslateResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Service
public class TranslationService {
    private static final int MAX_THREADS = 10;
    private final RestTemplate restTemplate;
    private final ExecutorService executorService;
    private final String API_URL = "https://translate.api.cloud.yandex.net/translate/v2/translate";
    @Value("${yandex.api.token}")
    private String apiToken;
    @Value("${yandex.folderId}")
    private String folderId;

    public TranslationService() {
        this.restTemplate = new RestTemplate();
        this.executorService = Executors.newFixedThreadPool(MAX_THREADS);
    }

    public List<String> translateWords(List<String> words, String sourceLang, String targetLang) {
        List<Future<String>> futures = new ArrayList<>();
        for (String word : words) {
            futures.add(executorService.submit(() -> translateWord(word, sourceLang, targetLang)));
        }
        return futures.stream().map(f -> {
            try {
                return f.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                return null;
            }
        }).collect(Collectors.toList());
    }

    private String translateWord(String word, String sourceLang, String targetLang) {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("texts", word);
        requestBody.put("folderId", folderId);
        requestBody.put("targetLanguageCode", targetLang);
        requestBody.put("sourceLanguageCode", sourceLang);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiToken);

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<YandexTranslateResponse> response = restTemplate.postForEntity(API_URL, requestEntity, YandexTranslateResponse.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            YandexTranslateResponse body = response.getBody();
            if (body != null && body.getTranslations() != null) {
                return body.getTranslations().get(0).getText();
            }
        }
        return word;
    }
}



