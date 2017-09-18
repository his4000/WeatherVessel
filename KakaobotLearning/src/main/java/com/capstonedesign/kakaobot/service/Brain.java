package com.capstonedesign.kakaobot.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayesMultinomialText;
import weka.core.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

/*****
 * This is the most important part of Kakaobot learning
 * This part like a brain in machine learning
 * *****/
@Getter
@Slf4j
public class Brain {

    private final ArrayList<Attribute> allAttr;
    private final int numAttr = 2;
    private final int numClassType = 3;
    private final int maxDataSet = 10000;
    private Instances dataSet;
    private Vector<String> fvAttr;
    private NaiveBayesMultinomialText classifier;
    private Evaluation evaluation;

    public Brain() {
        this.allAttr = new ArrayList<>();
        this.classifier = new NaiveBayesMultinomialText();
        createAttributes();
        createDataSet();
    }

    private void createAttributes() {
        this.fvAttr = new Vector<>(this.numClassType);

        fvAttr.addElement("forecast");
        fvAttr.addElement("current");
        fvAttr.addElement("monitoring");

        allAttr.add(new Attribute("class", fvAttr));
        allAttr.add(new Attribute("text", (Vector<String>)null));
    }

    private void createDataSet() {
        this.dataSet = new Instances("KakaobotLearningDataSet", this.allAttr, this.maxDataSet);
        dataSet.setClassIndex(0);
    }

    private void addLearningData(String attr, String text) throws Exception {
        AbstractInstance instance = new DenseInstance(this.numAttr);

        if(!this.fvAttr.contains(attr))
            throw new Exception("Attr is not supported");

        instance.setValue(this.allAttr.get(0), attr);
        instance.setValue(this.allAttr.get(1), text);
        this.dataSet.add(instance);
    }

    private void refreshClassifier() throws Exception {
        this.classifier.buildClassifier(this.dataSet);
    }

    private void evaluateModel() throws Exception {
        evaluation = new Evaluation(this.dataSet);
        evaluation.evaluateModel(this.classifier, this.dataSet);
    }

    public void refresh(String attr, String text) throws Exception {
        addLearningData(attr, text);
        refreshClassifier();
        evaluateModel();
    }

    public String predictUnknownCase(String text, Boolean observe) throws Exception {
        AbstractInstance instance = new DenseInstance(this.numAttr);

        instance.setValue(this.allAttr.get(1), text);
        instance.setDataset(this.dataSet);
        this.classifier.buildClassifier(this.dataSet);

        if(observe)
            Arrays.stream(this.classifier.distributionForInstance(instance)).forEach(probablity -> log.info(String.valueOf(probablity)));

        return this.fvAttr.get((int)this.classifier.classifyInstance(instance));
    }
}

