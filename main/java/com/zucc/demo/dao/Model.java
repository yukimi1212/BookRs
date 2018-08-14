package com.zucc.demo.dao;


import java.io.*;

import org.apache.mahout.cf.taste.impl.model.file.*;
import org.apache.mahout.cf.taste.impl.neighborhood.*;
import org.apache.mahout.cf.taste.impl.recommender.*;
import org.apache.mahout.cf.taste.impl.recommender.knn.KnnItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.knn.NonNegativeQuadraticOptimizer;
import org.apache.mahout.cf.taste.impl.recommender.slopeone.SlopeOneRecommender;
import org.apache.mahout.cf.taste.impl.recommender.svd.SVDRecommender;
import org.apache.mahout.cf.taste.impl.recommender.svd.ALSWRFactorizer;
import org.apache.mahout.cf.taste.impl.similarity.*;
import org.apache.mahout.cf.taste.model.*;
import org.apache.mahout.cf.taste.neighborhood.*;
import org.apache.mahout.cf.taste.recommender.*;
import org.apache.mahout.cf.taste.similarity.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Model {
    private static String TRAINPATH = "X:\\study\\大三\\短学期\\model\\ratings_train.csv";
    private static String TESTPATH = "X:\\study\\大三\\短学期\\model\\ratings_test.csv";
    private static String PATH = "X:\\study\\大三\\短学期\\model\\";

    public static void main(String args[]) throws Exception {
        File test = new File(TESTPATH);
        File result = new File(PATH+"result.csv");
        Model m = new Model();
        m.getResult(test,result);
        System.out.println(m.test(result,1));
    }
    public void getResult(File test,File result){
        try {
            BufferedReader br = new BufferedReader(new FileReader(test));
            BufferedWriter bw = new BufferedWriter(new FileWriter(result));
            Recommender recommender1 = null;
            Recommender recommender2 = null;
            Recommender recommender3 = null;
            String line = null;
            int first = 0;
            while((line=br.readLine())!=null){
                long user_id = Long.parseLong(line.split(",")[0]);
                long item_id = Long.parseLong(line.split(",")[1]);
                if(first==0){
                    DataModel dataModel = new FileDataModel(new File(TRAINPATH));

//                    UserSimilarity userSimilarity = new EuclideanDistanceSimilarity(dataModel);
//                    UserNeighborhood userNeighborhood = new NearestNUserNeighborhood(2, userSimilarity, dataModel);
//                    recommender1 = new GenericUserBasedRecommender(dataModel, userNeighborhood, userSimilarity);

                    ItemSimilarity itemsimilarity = new EuclideanDistanceSimilarity(dataModel);
                    recommender1 = new GenericItemBasedRecommender(dataModel,itemsimilarity);

                    recommender2 = new SVDRecommender(dataModel,new ALSWRFactorizer(dataModel,10,0.7,3));
//                    recommender3 = new SlopeOneRecommender(dataModel);
                    recommender3 = new KnnItemBasedRecommender(dataModel,itemsimilarity,new NonNegativeQuadraticOptimizer(),4);
//                    first = 1;
                }
                first++;
                float m1 = recommender1.estimatePreference(user_id,item_id);
                if(Float.isNaN(m1)) m1 = 0;
                float m2 = recommender2.estimatePreference(user_id,item_id);
                if(Float.isNaN(m2)) m2 = 0;
//                float m3 = recommender3.estimatePreference(user_id,item_id);
//                if(Float.isNaN(m3)) m3 = 0;
                String new_line = line+","+m1+","+m2;
                bw.write(new_line);
                bw.newLine();
                System.out.println(first);
            }
            br.close();
            bw.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    public double test(File file,int i){
        double result = 0;
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = null;
            int n = 0;
            while((line=br.readLine())!=null){
                double r_value = Double.parseDouble(line.split(",")[2]);
                double m = Double.parseDouble(line.split(",")[i+2]);
                if(Double.isNaN(m))
                    continue;
                result += Math.pow(Math.abs(r_value-m),2);
                n++;
            }
            System.out.println(n);
            result = Math.sqrt(result/n);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return result;
    }

//    public void getResult(File test,File result){
//        try {
//            BufferedReader br = new BufferedReader(new FileReader(test));
//            BufferedWriter bw = new BufferedWriter(new FileWriter(result));
//            String line = null,new_line = "",pre_user = "0";
//
//            Recommender recommender1 = null;
//            Recommender recommender2 = null;
//            Recommender recommender3 = null;
//
//            DataModel dataModel = new FileDataModel(new File(TRAINPATH));
//            ItemSimilarity itemsimilarity =new EuclideanDistanceSimilarity(dataModel);
//            recommender1 = new GenericItemBasedRecommender(dataModel,itemsimilarity);
//
//            List<RecommendedItem>  recommendations = null;
//            List<String> list = new ArrayList<>();
//            while((line=br.readLine())!=null){
//                long user_id = Long.parseLong(line.split(",")[0]);
//                long item_id = Long.parseLong(line.split(",")[1]);
//                float score = 0;
//                if(!String.valueOf(user_id).equals(pre_user)){
//                    list = getByUser(user_id);
//
//                    recommendations = recommender1.recommend(user_id, 100);
//                    for (RecommendedItem recommendation:recommendations){
//                        list.addAll(getByItem(recommendation.getItemID()));
//                    }
//                }
//                List<String> new_train = new ArrayList<>();
//                new_train.addAll(list);
//                int flag = 0;
//                for (RecommendedItem recommendation:recommendations){
//                    if(recommendation.getItemID()==item_id)
//                        flag = 1;
//                }
//                if(flag==0){
//                    new_train.addAll(getByItem(item_id));
//                    toCSV(new_train,new File(PATH+"new_train.csv"));
//                }
//                if(flag==1&&!String.valueOf(user_id).equals(pre_user))
//                    toCSV(new_train,new File(PATH+"new_train.csv"));
//
//                score = recommender1.estimatePreference(user_id,item_id);
//                new_line = line+","+score;
//
//                if(!String.valueOf(user_id).equals(pre_user)||flag==0){
//                    DataModel model = new FileDataModel(new File(PATH+"new_train.csv"));
//                    UserSimilarity userSimilarity = new EuclideanDistanceSimilarity(model);
//                    UserNeighborhood userNeighborhood = new NearestNUserNeighborhood(1000, userSimilarity, model);
//                    recommender2 = new GenericUserBasedRecommender(model, userNeighborhood, userSimilarity);
//                }
//                score = recommender2.estimatePreference(user_id,item_id);
//                new_line += ","+score;
//
//                if(!String.valueOf(user_id).equals(pre_user)||flag==0){
//                    DataModel model = new FileDataModel(new File(PATH+"new_train.csv"));
//                    recommender3= new SlopeOneRecommender(model);
//                }
//                score = recommender3.estimatePreference(user_id,item_id);
//                new_line += ","+score;
//
//                bw.write(new_line);
//                bw.newLine();
//                pre_user = String.valueOf(user_id);
//                System.out.println(pre_user);
//            }
//            br.close();
//            bw.close();
//        }catch (Exception ex){
//            ex.printStackTrace();
//        }
//
//    }
//    //    通过itemid从训练集中获取所有user对该item的评分
//    public List<String> getByItem(long item){
//        List<String> result = new ArrayList<>();
//        File csv = new File(TRAINPATH);
//        try {
//            BufferedReader br = new BufferedReader(new FileReader(csv));
//            String line = null;
//            while((line=br.readLine())!=null){
//                if(line.split(",")[1].equals(Long.toString(item))){
////                    System.out.println(line);
//                    result.add(line);
//                }
//
//            }
//
//        }catch (Exception ex){
//            ex.printStackTrace();
//        }
//        return result;
//    }
//    //    通过userid从训练集中获取user评价过书籍的所有评分
//    public List<String> getByUser(long user){
//        List<String> result = new ArrayList<>();
//        File csv = new File(TRAINPATH);
//        try {
//            BufferedReader br = new BufferedReader(new FileReader(csv));
//            String line = null;
//            while((line=br.readLine())!=null){
//                if(line.split(",")[0].equals(Long.toString(user))){
//                    result.addAll(getByItem(Long.parseLong(line.split(",")[1])));
//                }
//
//            }
//
//        }catch (Exception ex){
//            ex.printStackTrace();
//        }
//        return result;
//    }
//    //    将list写入csv
//    public void toCSV(List<String> list,File file){
//        try {
//            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
//            for (String str:list){
////                System.out.println(str);
//                bw.write(str);
//                bw.newLine();
//            }
//            bw.close();
//        }catch (Exception ex){
//            ex.printStackTrace();
//        }
//    }
}