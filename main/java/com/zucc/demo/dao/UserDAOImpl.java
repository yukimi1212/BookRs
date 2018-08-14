package com.zucc.demo.dao;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import com.zucc.demo.model.BookVo;
import com.zucc.demo.model.RatingVo;
import com.zucc.demo.model.UserVo;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.model.jdbc.MySQLJDBCDataModel;
import org.apache.mahout.cf.taste.impl.model.jdbc.ReloadFromJDBCDataModel;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.EuclideanDistanceSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.JDBCDataModel;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserDAOImpl implements UserDAO{
    private static Recommender recommender = null;
    private MysqlDataSource dataSource = null;
    private JDBCDataModel dataModel = null;
    private ReloadFromJDBCDataModel model = null;
    @Override
    public UserVo login(int id) throws Exception{
        UserVo user = null;
        try {
            Connection con = getConnection();
            Statement stmt = null;
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM users where user_id ='"+id+"'");
            if(rs.next()){
                user = new UserVo();
                user.setUser_id(rs.getInt("user_id"));
                user.setPasswd(rs.getString("password"));
                user.setLocation(rs.getString("location"));
                user.setAge(rs.getString("age"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("数据库错误");
        }
        return user;
    }
    public UserVo register(String passwd,String location,String age) throws Exception{
        UserVo user = null;
        int id = 0;
        try {
            Connection con = getConnection();
            PreparedStatement stmt = null;
            stmt = con.prepareStatement("select max(user_id) from users");
            ResultSet rs  = stmt.executeQuery();
            if(rs.next())
                id = rs.getInt(1)+1;

            stmt = con.prepareStatement("insert into users (user_id,password,location,age) values(?,?,?,?)");
            stmt.setInt(1,id);
            stmt.setString(2,passwd);
            stmt.setString(3,location);
            stmt.setString(4,age);
            stmt.executeUpdate();

            user = login(id);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("数据库错误");
        }
        return  user;
    }
    public List<BookVo> recommend(int user_id) throws Exception{
        if(recommender==null){
            dataSource = new MysqlDataSource();
            dataSource.setServerName("localhost");
            dataSource.setUser("root");
            dataSource.setPassword("123456");
            dataSource.setDatabaseName("books_rs");
            dataModel = new MySQLJDBCDataModel(dataSource, "ratings", "user_id",
                    "book_id", "book_rating", null);
            model = new ReloadFromJDBCDataModel(dataModel);
            ItemSimilarity itemsimilarity =new EuclideanDistanceSimilarity(model);
            recommender= new GenericItemBasedRecommender(model,itemsimilarity);
        }
        List<BookVo> result = new ArrayList<BookVo>();
        try {
            Connection con = getConnection();
            Statement stmt = null;
            stmt = con.createStatement();

            int flag = 0;
            ResultSet rs = stmt.executeQuery("SELECT * FROM ratings where user_id = " + user_id);
            if(rs.next())
                flag = 1;
            if(flag==1){
                List<RecommendedItem> recommendations =recommender.recommend(user_id, 6);
                for(RecommendedItem recommendation :recommendations){
                    rs = stmt.executeQuery("SELECT * FROM books where id = "+recommendation.getItemID());
                    if(rs.next()){
                        BookVo book = new BookVo();
                        book.setIsbn(rs.getString("isbn"));
                        book.setBook_title(rs.getNString("book_title"));
                        book.setBook_Author(rs.getString("book_author"));
                        book.setYear_of_Publication(rs.getString("year_of_publication"));
                        book.setPublisher(rs.getString("publisher"));
                        book.setImage_URL_S(rs.getString("image_url_s"));
                        book.setImage_URL_M(rs.getString("image_url_m"));
                        book.setImage_URL_L(rs.getString("image_url_l"));
                        result.add(book);
//                        System.out.println(book.getBook_title());
                    }
                }
                if(result.size()==0)
                    flag = 0;
            }
            if(flag==0){
                rs = stmt.executeQuery("SELECT * FROM books order by rand() limit 6");
                while(rs.next()){
                    BookVo book = new BookVo();
                    book.setIsbn(rs.getString("isbn"));
                    book.setBook_title(rs.getNString("book_title"));
                    book.setBook_Author(rs.getString("book_author"));
                    book.setYear_of_Publication(rs.getString("year_of_publication"));
                    book.setPublisher(rs.getString("publisher"));
                    book.setImage_URL_S(rs.getString("image_url_s"));
                    book.setImage_URL_M(rs.getString("image_url_m"));
                    book.setImage_URL_L(rs.getString("image_url_l"));
                    result.add(book);
//                        System.out.println(book.getBook_title());
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("数据库错误");
        }
        return result;
    }
    public List<RatingVo> already(int id) throws Exception{
        List<RatingVo> result = new ArrayList<RatingVo>();
        try {
            Connection con = getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM books WHERE ISBN IN (SELECT ISBN FROM ratings WHERE user_id = "+id+")");
            while(rs.next()){
                RatingVo book = new RatingVo();
                book.setImg(rs.getString("image_url_m"));
                book.setIsbn(rs.getString("isbn"));
                book.setTitle(rs.getNString("book_title"));
                book.setAuthor(rs.getString("book_author"));
                book.setPublisher(rs.getString("publisher"));
                book.setYear(rs.getString("year_of_publication"));
                Statement stmt2 = con.createStatement();
                ResultSet rs2 = stmt2.executeQuery("SELECT book_rating FROM ratings WHERE user_id = "+id+" and isbn = '"+book.getIsbn()+"'");
                if(rs2.next())
                    book.setScore(rs2.getInt(1)+"");
                result.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("数据库错误");
        }
        return result;
    }
    public int value(int id,String isbn,int score) throws  Exception{
        int book_id = 0;
        try{
            Connection con = getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM ratings WHERE user_id = "+id+" and ISBN = '"+isbn+"'");
            if(rs.next())
                return 1;
            rs = stmt.executeQuery("SELECT id FROM books WHERE ISBN = '"+isbn+"'");
            if(rs.next())
                book_id = rs.getInt(1);
            PreparedStatement pstmt =  con.prepareStatement("insert into ratings(user_id,book_id,isbn,book_rating) values(?,?,?,?)");
            pstmt.setInt(1,id);
            pstmt.setInt(2,book_id);
            pstmt.setString(3,isbn);
            pstmt.setInt(4,score);
            pstmt.executeUpdate();
            model = new ReloadFromJDBCDataModel(dataModel);
            ItemSimilarity itemsimilarity =new EuclideanDistanceSimilarity(model);
            recommender= new GenericItemBasedRecommender(model,itemsimilarity);

        }catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("数据库错误");
        }
        return 0;
    }
    public static boolean isNum(String str){
        for(int i=0;i<str.length();i++)
            if(!Character.isDigit(str.charAt(i)))
                return false;
        return true;
    }
    private Connection getConnection() {
        try {
            //加载MySql的驱动类
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("找不到驱动程序类 ，加载驱动失败！");
            e.printStackTrace();
        }
        //连接MySql数据库，用户名和密码都是root
        String url = "jdbc:mysql://localhost:3306/books_rs";
        String username = "root";
        String password = "123456";
        try {
            Connection con =
                    DriverManager.getConnection(url, username, password);
            return con;
        } catch (SQLException se) {
            System.out.println("数据库连接失败！");
            se.printStackTrace();
        }
        return null;
    }
    public static void main(String[] args) throws Exception{
        UserDAOImpl u = new UserDAOImpl();
        List<RatingVo> result = u.already(14);
        for(RatingVo book:result)
            System.out.println(book.getScore());
    }
}
