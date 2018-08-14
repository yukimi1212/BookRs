package com.zucc.demo.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

public class RMSE {
    private static String TRAINPATH = "C:\\Users\\lenovo\\Desktop\\数据\\test.csv";
    private static String TESTPATH = "C:\\Users\\lenovo\\Desktop\\数据\\result1.csv";

    public static ArrayList<Double> getD(File m) {
        String id = m.getPath().split("_")[1];
        File test = new File(TRAINPATH);
        ArrayList<Double> list = new ArrayList<Double>();
        TreeMap<String,String> map = new TreeMap<String,String>();
        double n = 0;
        double mvalue = 0 ,tvalue = 0;
        try {
            BufferedReader brtest = new BufferedReader(new FileReader(test));
            BufferedReader brm = new BufferedReader(new FileReader(m));
            String linem = null;
            String linetest = null;
            while((linetest=brtest.readLine())!=null){
                if(linetest.split(",")[0].equals(id)) {
                    brm = new BufferedReader(new FileReader(m));
                    while ((linem=brm.readLine())!=null) {
                        String s = linem.split(",")[1];
                        if (s.equals(linetest.split(",")[1])) {
                            mvalue = Double.parseDouble(linem.split(",")[2]);
                            tvalue = Double.parseDouble(linetest.split(",")[2]);
                            n = Math.abs(mvalue - tvalue);
                            list.add(n);
                        }
                    }
                    brm.close();
                }
            }
            brtest.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return list;
    }

    public static double getRMSE(File m){
        ArrayList<Double> D_value = getD(m);
        double sum = 0;
        for(int i=0; i<D_value.size(); i++){
            sum += Math.pow(D_value.get(i),2);
        }
        sum = Math.sqrt(sum/D_value.size());
        return sum;
    }

    public static double getRMSE(ArrayList<Double> D_value){
        double sum = 0;
        for(int i=0; i<D_value.size(); i++){
            sum += Math.pow(D_value.get(i),2);
        }
        sum = Math.sqrt(sum/D_value.size());
        return sum;
    }


    public static void main(String[] args){
/*
        File m = new File("C:\\Users\\lenovo\\Desktop\\数据\\data\\user_3363_m2.csv");
        double result = getRMSE(m);
*/
        double[] x1 = new double[]{0.52702137, 0.28430519, 0.02832276};
//        double[] x2 = new double[]{0.63598067, 0.46696392, 0.5235221, 0.09552253};
        double[] x2 = new double[]{0.69951123, 0.41483209, 0.09193957};
        double r = 0.559371624492591;
        double result = getRMSE(x1,x2,r);
        System.out.println(result);
    }

    public static double getRMSE(double[] x1, double[] x2, double r){
        ArrayList<Double> D_value = getD(x1,x2,r);
        double sum = 0;
        for(int i=0; i<D_value.size(); i++){
            sum += Math.pow(D_value.get(i),2);
        }
        sum = Math.sqrt(sum/D_value.size());
        return sum;
    }

    public static ArrayList<Double> getD(double[] x1, double[] x2, double r) {
        File test = new File(TESTPATH);
        ArrayList<Double> list = new ArrayList<Double>();
        double n = 0;
        double mvalue = 0 ,tvalue = 0;
        try {
            BufferedReader brtest = new BufferedReader(new FileReader(test));
            String linetest = null;
            while((linetest=brtest.readLine())!=null){
                double real =0; double result1 = 0; double result2 = 0;
                real = Double.parseDouble(linetest.split(",")[4]);
                double m1 = Double.parseDouble(linetest.split(",")[0]);
                double m2 = Double.parseDouble(linetest.split(",")[1]);
                double m3 = Double.parseDouble(linetest.split(",")[2]);
                double m4 = Double.parseDouble(linetest.split(",")[3]);
                result1 = r + m1 * x1[0] + m2 * x1[1] + m3 * x1[2];
                result2 = r + m1 * x2[0] + m2 * x2[1] + m3 * x2[2];
                System.out.println("m2: " + m2 + "     result1: " + result1 + "     result2: " + result2);
//                System.out.println(result);
                n = Math.abs(result1 - real);
                list.add(n);
            }
            brtest.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return list;
    }

}
