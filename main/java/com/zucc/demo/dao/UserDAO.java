package com.zucc.demo.dao;

import com.zucc.demo.model.BookVo;
import com.zucc.demo.model.RatingVo;
import com.zucc.demo.model.UserVo;

import java.util.List;

public interface UserDAO {
    public UserVo login(int id) throws Exception;
    public UserVo register(String passwd,String location,String age) throws Exception;
    public List<BookVo> recommend(int id) throws Exception;
    public List<RatingVo> already(int id) throws Exception;
    public int value(int id,String isbn,int score) throws  Exception;
}
