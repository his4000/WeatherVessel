package com.capstonedesign.kakaobot.service.machine_learning;

import org.openkoreantext.processor.OpenKoreanTextProcessorJava;
import org.openkoreantext.processor.phrase_extractor.KoreanPhraseExtractor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NaturalLanguageProcessing {

    public List<String> extractPhrase(String text){
        List<KoreanPhraseExtractor.KoreanPhrase> phrases
                = OpenKoreanTextProcessorJava.extractPhrases(
                        OpenKoreanTextProcessorJava.tokenize(
                                OpenKoreanTextProcessorJava.normalize(text)
                        ), true, true
        );

        List<String> extractPhrases = new ArrayList<String>();
        phrases.stream().forEach(phrase -> extractPhrases.add(phrase.text()));

        return extractPhrases;
    }
}
