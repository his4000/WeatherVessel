package com.capstonedesign.kakaobot.service.machine_learning;

import lombok.Getter;

@Getter
public class Keyword {
    private String keyword;
    private double[] probDistribution;
    private final double defaultProb = 1.0;

    public Keyword(String keyword){
        this.keyword = keyword;
        this.probDistribution = new double[Learning.numAttr];

        probDistribution[Learning.GREETING] = 0.0;
        probDistribution[Learning.CURRENT] = 0.0;
        probDistribution[Learning.MONITOR] = 0.0;
    }

    public void addProb(int attrIndex){
        probDistribution[attrIndex] += defaultProb;
    }

    public int getMaxAttr(){
        int maxIndex = 0;
        double max = 0.0;

        for(int i=0;i<Learning.numAttr;i++){
            if(probDistribution[i] > max)
                maxIndex = i;
        }

        return maxIndex;
    }

    public double getMaxProb(){
        double max = 0.0;
        double sum = 0.0;

        for(int i=0;i<Learning.numAttr;i++){
            if(probDistribution[i] > max)
                max = probDistribution[i];
            sum += probDistribution[i];
        }

        return max / sum;
    }
}
