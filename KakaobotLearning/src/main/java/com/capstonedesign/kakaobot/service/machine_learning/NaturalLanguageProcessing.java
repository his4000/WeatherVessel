package com.capstonedesign.kakaobot.service.machine_learning;

import org.openkoreantext.processor.OpenKoreanTextProcessorJava;
import org.openkoreantext.processor.phrase_extractor.KoreanPhraseExtractor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class NaturalLanguageProcessing {

    public List<String> extractPhrase(String text){
        List<KoreanPhraseExtractor.KoreanPhrase> phrases
                = OpenKoreanTextProcessorJava.extractPhrases(
                        OpenKoreanTextProcessorJava.tokenize(
                                OpenKoreanTextProcessorJava.normalize(text)
                        ), true, true
        );

        return phrases.stream().map(KoreanPhraseExtractor.KoreanPhrase::text).collect(toList());
    }
}
