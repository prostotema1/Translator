package com.tbank.translator;

import com.tbank.translator.requests.MyMemoryResponse;
import com.tbank.translator.service.TranslationService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class TranslationServiceTest {

    @Mock
    RestTemplate restTemplate;

    @InjectMocks
    private TranslationService translationService;

    public TranslationServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testTranslateWord() {
        String word = "Hello";
        String sourceLang = "en";
        String targetLang = "ru";
        String translatedText = "Здравствуйте";

        MyMemoryResponse mockResponse = new MyMemoryResponse();
        MyMemoryResponse.ResponseData translation = new MyMemoryResponse.ResponseData();
        translation.setTranslatedText(translatedText);
        mockResponse.setResponseData(translation);

        ResponseEntity<MyMemoryResponse> responseEntity = ResponseEntity.ok(mockResponse);
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), any(Class.class)))
                .thenReturn(responseEntity);

        String result = translationService.translateWords(List.of(word), sourceLang, targetLang).get(0);

        assertEquals(translatedText, result);
    }
}
