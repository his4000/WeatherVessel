package com.capstonedesign.kakaobot.service.natural_language_processing;

import com.capstonedesign.kakaobot.domain.AddressRepository;
import lombok.extern.slf4j.Slf4j;
import org.openkoreantext.processor.KoreanTokenJava;
import org.openkoreantext.processor.OpenKoreanTextProcessorJava;
import org.openkoreantext.processor.phrase_extractor.KoreanPhraseExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Slf4j
public class NaturalLanguageProcessing{

    @Autowired
    AddressRepository addressIndexRepository;

    //@PostConstruct
    /*public void initNLP(){
        List<String> filePath = new ArrayList<>();
        filePath.add("./src/main/resources/static/address/Seoul.txt");
        List<String> addressList = new ArrayList<>();

        log.info(System.getProperty("user.dir"));

        try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath.get(0)), "UTF8"))){
            String line;

            while((line = br.readLine()) != null){
                String[] tokens = line.split(",|:");
                addressList.addAll(new ArrayList<>(Arrays.asList(tokens)));
            }
            br.close();
        }catch (Exception e){
            log.info("Init address Error");
            e.printStackTrace();
        }
*/
        /*KoreanDictionaryProvider.koreanDictionary().put(KoreanPos.Space(), KoreanConjugation.conjugatePredicatesToCharArraySet(KoreanDictionaryProvider.readWordsAsSet(
                JavaConverters.asScalaBuffer(filePath)
        ), false));*/

     /*   KoreanDictionaryProvider.koreanDictionary().put(KoreanPos.Space(), KoreanDictionaryProvider.readWords());

        OpenKoreanTextProcessorJava.addWordsToDictionary(KoreanPosJava.Space, addressList.stream().filter(address -> !address.equalsIgnoreCase("")).collect(toList()));
    }*/

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

    public String getAddressFromText(String text){
        List<KoreanTokenJava> tokens = textTokenizing(text);
        List<String> addressList = new ArrayList<>();
        String ret = "";

        for(KoreanTokenJava token : tokens){
            if(addressIndexRepository.findByAddress(token.getText()) != null)
                addressList.add(token.getText());
        }

        for(String address : addressList){
            ret = ret + address;
        }

        return ret;
    }
}
