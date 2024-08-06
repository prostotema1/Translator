package com.tbank.translator.service;

import com.tbank.translator.requests.MyMemoryResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
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

    private final String URL = "https://api.mymemory.translated.net/get";

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
        String url = String.format("%s?q=%s&langpair=%s|%s", URL, word, sourceLang, targetLang);


        ResponseEntity<MyMemoryResponse> response = restTemplate.getForEntity(url, MyMemoryResponse.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            MyMemoryResponse body = response.getBody();
            if (body != null && body.getResponseData() != null) {
                return body.getResponseData().getTranslatedText();
            }
        }
        return word;
    }
}



