package com.zucc.demo.dao;


import java.io.*;
import java.util.*;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.*;
import org.apache.mahout.cf.taste.impl.neighborhood.*;
import org.apache.mahout.cf.taste.impl.recommender.*;
import org.apache.mahout.cf.taste.impl.recommender.slopeone.SlopeOneRecommender;
import org.apache.mahout.cf.taste.impl.similarity.*;
import org.apache.mahout.cf.taste.model.*;
import org.apache.mahout.cf.taste.neighborhood.*;
import org.apache.mahout.cf.taste.recommender.*;
import org.apache.mahout.cf.taste.similarity.*;

import java.io.File;
import java.util.List;
public class Mahout {
    private static String TRAINPATH = "X:\\study\\大三\\短学期\\train.csv";
    private static String PATH = "X:\\study\\大三\\短学期\\data\\";

    public static void main(String args[]) throws Exception {

        Mahout testMahout = new Mahout();
        long id = 115150;
        int n = 100;
//        System.out.println("The baseItemCF Result:");
//        testMahout.baseItemCF(id,n);
        System.out.println("The baseUserCF Result:");
        testMahout.baseUserCF(id,n);
//        System.out.println("The baseSlopOne Result:");
//        testMahout.baseSlopOne(id);
    }


    //基于内容相似度的协同过滤推荐实现
    public void baseItemCF(long user_id,int n){
        DataModel model;
        try {
            model = new FileDataModel(new File(TRAINPATH));
            ItemSimilarity itemsimilarity =new EuclideanDistanceSimilarity(model);
            Recommender recommender= new GenericItemBasedRecommender(model,itemsimilarity);
            List<RecommendedItem> recommendations =recommender.recommend(user_id, n);
            List<String> m1 = new ArrayList<>();
            List<String> new_train = getByUser(user_id);
            for(RecommendedItem recommendation :recommendations){
                m1.add(user_id+","+recommendation.getItemID()+","+recommendation.getValue());
//                System.out.println(recommendation);
                new_train.addAll(getByItem(recommendation.getItemID()));
            }
            toCSV(m1,new File(PATH+"user_"+user_id+"_m1.csv"));
            toCSV(new_train,new File(PATH+"user_"+user_id+"_train.csv"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TasteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    //基于用户相似度的协同过滤推荐实现
    public void baseUserCF(long user_id,int n){
        try {
            // 1,构建模型
            DataModel dataModel = new FileDataModel(new File(TRAINPATH));
            //2,计算相似度
            UserSimilarity userSimilarity = new EuclideanDistanceSimilarity(dataModel);
            //3,查找K近邻
            UserNeighborhood userNeighborhood = new NearestNUserNeighborhood(80000, userSimilarity, dataModel);
            //4,构造推荐引擎
            Recommender recommender = new GenericUserBasedRecommender(dataModel, userNeighborhood, userSimilarity);
            //为用户i推荐item
            System.out.println("recommand for user:" + recommender.estimatePreference(user_id,Long.parseLong("69803")));
            List<String> m2 = new ArrayList<>();
            List<RecommendedItem>  recommendations = recommender.recommend(user_id, n);
            for (RecommendedItem recommendation:recommendations){
//                System.out.println(recommendation);
                m2.add(user_id+","+recommendation.getItemID()+","+recommendation.getValue());
            }
//            toCSV(m2,new File(PATH+"user_"+user_id+"_m2.csv"));
        }catch(IOException e){
            e.printStackTrace();
        }catch(TasteException e){
            e.printStackTrace();
        }
    }
    //基于SlopOne的推荐实现
    public void baseSlopOne(long user_id,int n){
        DataModel model;
        try {
            model = new FileDataModel(new File(PATH+"user_"+user_id+"_train.csv"));
            Recommender recommender= new SlopeOneRecommender(model);
            List<String> m3 = new ArrayList<>();
            List<RecommendedItem> recommendations =recommender.recommend(user_id, n);
            for(RecommendedItem recommendation :recommendations){
//                System.out.println(recommendation);
                m3.add(user_id+","+recommendation.getItemID()+","+recommendation.getValue());
            }
            toCSV(m3,new File(PATH+"user_"+user_id+"_m3.csv"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println("Io Error");
            e.printStackTrace();
        } catch (TasteException e) {
            // TODO Auto-generated catch block
            System.out.println("Taste Error");
            e.printStackTrace();
        }

    }
//    通过itemid从训练集中获取所有user对该item的评分
    public List<String> getByItem(long item){
        List<String> result = new ArrayList<>();
        File csv = new File(TRAINPATH);
        try {
            BufferedReader br = new BufferedReader(new FileReader(csv));
            String line = null;
            while((line=br.readLine())!=null){
                if(line.split(",")[1].equals(Long.toString(item))){
//                    System.out.println(line);
                    result.add(line);
                }

            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return result;
    }
    //    通过userid从训练集中获取user评价过书籍的所有评分
    public List<String> getByUser(long user){
        List<String> result = new ArrayList<>();
        File csv = new File(TRAINPATH);
        try {
            BufferedReader br = new BufferedReader(new FileReader(csv));
            String line = null;
            while((line=br.readLine())!=null){
                if(line.split(",")[0].equals(Long.toString(user))){
                    result.addAll(getByItem(Long.parseLong(line.split(",")[1])));
                }

            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return result;
    }
//    将list写入csv
    public void toCSV(List<String> list,File file){
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            for (String str:list){
//                System.out.println(str);
                bw.write(str);
                bw.newLine();
            }
            bw.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

}