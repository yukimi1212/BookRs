package com.zucc.demo.dao;

import com.zucc.demo.model.Instance;
import com.zucc.demo.model.Logistic;

import java.util.ArrayList;
import java.util.List;

public class LogRegression {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
//      String fileName = "logisticaps.csv";
//      boolean isEnd = true;
//      int size = 12;
        String fileName = "C:\\Users\\lenovo\\Desktop\\数据\\result1.csv";
        boolean isEnd = true;
        int size = 3;
//      String fileName = "binary.csv"; logisticaps
//      boolean isEnd = false;
//      int size = 3;
        double rate = 0.001;
        int limit = 10000;
        boolean isGD = true;
        Logistic logistic = new Logistic(size,rate,limit);
        logistic.train(fileName, isEnd,isGD);
        ReadData readData = new ReadData();
        List<Instance> data = readData.readDataSet(fileName, isEnd);
/*
        double acc = logistic.accuracyRate(data);
        System.out.println("正确率："+acc);
*/
        ArrayList<Double> D_value = logistic.accuracyRate(data);
        double rmse = RMSE.getRMSE(D_value);
        System.out.println("RMSE: " + rmse);
        double w[] = logistic.getWeights();
        for(int i=0;i<w.length;i++){
            System.out.println(w[i]+"\t");
        }
    }

}

