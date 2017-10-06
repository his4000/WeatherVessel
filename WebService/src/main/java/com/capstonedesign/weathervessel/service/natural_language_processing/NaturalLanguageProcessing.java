package com.capstonedesign.weathervessel.service.natural_language_processing;

import lombok.extern.slf4j.Slf4j;
import org.openkoreantext.processor.KoreanTokenJava;
import org.openkoreantext.processor.OpenKoreanTextProcessorJava;
import org.openkoreantext.processor.phrase_extractor.KoreanPhraseExtractor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Slf4j
public class NaturalLanguageProcessing {

    private List<String> addresses;

    @PostConstruct
    public void initNLP(){
        addresses = new ArrayList<>();
        String line;

        try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("./BOOT-INF/classes/static/address/Seoul.txt"), "UTF-8"))){
            while((line = br.readLine()) != null)
                addresses.add(line);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public List<KoreanTokenJava> filterAddress(List<KoreanTokenJava> tokens){
        return tokens.stream().filter(token -> addresses.contains(token.getText())).collect(toList());
    }

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
