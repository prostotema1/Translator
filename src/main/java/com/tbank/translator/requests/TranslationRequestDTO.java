package com.tbank.translator.requests;

import lombok.Data;

@Data
public class TranslationRequestDTO {

    private String inputText;
    private String sourceLanguage;
    private String targetLanguage;
}
