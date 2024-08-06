package com.tbank.translator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tbank.translator.controller.TranslationController;
import com.tbank.translator.repository.TranslationRequestRepository;
import com.tbank.translator.requests.TranslationRequestDTO;
import com.tbank.translator.service.TranslationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TranslationController.class)
public class TranslationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TranslationService translationService;

    @MockBean
    private TranslationRequestRepository requestRepository;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
    }


    @Test
    public void testTranslate() throws Exception {
        TranslationRequestDTO translationRequestDTO = new TranslationRequestDTO();
        translationRequestDTO.setInputText("Hello world");
        translationRequestDTO.setSourceLanguage("en");
        translationRequestDTO.setTargetLanguage("ru");
        String request = objectMapper.writeValueAsString(translationRequestDTO);

        List<String> translatedWords = Arrays.asList("Здравствуйте", "мир");
        String translatedText = String.join(" ", translatedWords);

        when(translationService.translateWords(any(), any(), any())).thenReturn(translatedWords);

        mockMvc.perform(post("/api/translate")
                        .contentType(MediaType.APPLICATION_JSON).content(request))
                .andExpect(status().isOk())
                .andExpect(content().string(translatedText));
    }
}