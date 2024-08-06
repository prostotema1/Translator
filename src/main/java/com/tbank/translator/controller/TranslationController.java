package com.tbank.translator.controller;

import com.tbank.translator.repository.TranslationRequestRepository;
import com.tbank.translator.requests.TranslationRequest;
import com.tbank.translator.requests.TranslationRequestDTO;
import com.tbank.translator.service.TranslationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/translate")
public class TranslationController {

    private final TranslationRequestRepository translationRequestRepository;
    private final TranslationService translationService;

    @Autowired
    public TranslationController(TranslationRequestRepository translationRequestRepository, TranslationService translationService) {
        this.translationRequestRepository = translationRequestRepository;
        this.translationService = translationService;
    }

    @ResponseBody
    @PostMapping
    public ResponseEntity<String> translate(@RequestBody TranslationRequestDTO translationRequestDTO,
                                            HttpServletRequest request) {
        String ipAdress = request.getRemoteAddr();
        String inputText = translationRequestDTO.getInputText();
        String targetLanguage = translationRequestDTO.getTargetLanguage();
        String sourceLanguage = translationRequestDTO.getSourceLanguage();

        List<String> text = Arrays.asList(inputText.split("\\s+"));
        List<String> translatedText = translationService.translateWords(text, sourceLanguage, targetLanguage);
        String result = String.join(" ", translatedText);

        TranslationRequest translationRequest = new TranslationRequest(null, ipAdress, inputText, result, sourceLanguage, targetLanguage, LocalDateTime.now());
        translationRequestRepository.save(translationRequest);

        return ResponseEntity.ok(result);
    }
}
