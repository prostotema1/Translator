package com.tbank.translator.repository;

import com.tbank.translator.requests.TranslationRequest;
import org.springframework.data.repository.CrudRepository;

public interface TranslationRequestRepository extends CrudRepository<TranslationRequest, Long> {

}
