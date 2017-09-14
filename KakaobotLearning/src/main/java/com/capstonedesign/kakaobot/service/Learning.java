package com.capstonedesign.kakaobot.service;

import com.capstonedesign.kakaobot.domain.Questions;
import com.capstonedesign.kakaobot.domain.QuestionsRepository;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import weka.core.converters.ArffSaver;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class Learning {

    private int count;
    private final long batchTime = 5;
    private Brain brain;
    private final String saveFilePath = "./DataSetBackUp.arff";

    @Autowired
    QuestionsRepository questionsRepository;

    public Learning(){
        brain = new Brain();
        this.count = 0;
    }


    @Scheduled(fixedRate = batchTime * 60 * 1000)
    public void executeLearning() {
        List<Questions> questions = questionsRepository.findByIdGreaterThan(count);
        ArffSaver saver = new ArffSaver();

        try {
            for(Questions question : questions){
                brain.refresh(question.getClassType(), question.getText());
                count++;
            }

            saver.setInstances(brain.getDataSet());
            saver.setFile(new File(this.saveFilePath));
            saver.writeBatch();

        }catch (Exception e) {
            log.info(LocalDateTime.now().toString() + " : Exception occurred when learning");
        }
    }
}
