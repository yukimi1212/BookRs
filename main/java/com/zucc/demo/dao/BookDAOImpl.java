package com.zucc.demo.dao;

import com.zucc.demo.model.BookVo;
import com.zucc.demo.model.RatingVo;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


@Repository
public class BookDAOImpl implements BookDAO {
    private List<RatingVo> pages = new ArrayList<>();
    @Override
    public List<RatingVo> getAllPages() {
        List<RatingVo> result = new ArrayList<>();
        try {
            Connection con = getConnection();
            Statement stmt = null;

            stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT * FROM books");

            while(rs.next()){
                RatingVo book = new RatingVo();
                book.setIsbn(rs.getString("isbn"));
                book.setTitle(rs.getNString("book_title"));
                book.setAuthor(rs.getString("book_author"));
                book.setYear(rs.getString("year_of_publication"));
                book.setPublisher(rs.getString("publisher"));
                book.setImg(rs.getString("image_url_m"));
                result.add(book);
//                        System.out.println(book.getBook_title());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    public List<RatingVo> getSearch(int user_id,String searchWord){
        List<RatingVo> result = new ArrayList<>();
        if (pages.isEmpty()) {
            pages = getAllPages();
        }
        try{
            Connection con = getConnection();
            Statement stmt = null;
            stmt = con.createStatement();
            for (RatingVo page : pages) {
                if (page.getTitle().contains(searchWord)){
                    ResultSet rs = stmt.executeQuery("SELECT book_rating FROM ratings where user_id = "+user_id+" and isbn = '"+page.getIsbn()+"'");
                    if(rs.next())
                        page.setScore(rs.getInt(1)+"");
                    else
                        page.setScore("暂无评分");
                    result.add(page);
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }

        return  result;
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
}

