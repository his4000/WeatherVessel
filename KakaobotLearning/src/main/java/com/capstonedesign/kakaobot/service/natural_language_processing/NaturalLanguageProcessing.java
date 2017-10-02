package com.capstonedesign.kakaobot.service.natural_language_processing;

import lombok.extern.slf4j.Slf4j;
import org.openkoreantext.processor.KoreanTokenJava;
import org.openkoreantext.processor.OpenKoreanTextProcessorJava;
import org.openkoreantext.processor.phrase_extractor.KoreanPhraseExtractor;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Slf4j
public class NaturalLanguageProcessing{

    public List<KoreanTokenJava> textTokenizing(String text){
        return OpenKoreanTextProcessorJava.tokensToJavaKoreanTokenList(
                OpenKoreanTextProcessorJava.tokenize(
                        OpenKoreanTextProcessorJava.normalize(text)
                )
        );
    }

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
