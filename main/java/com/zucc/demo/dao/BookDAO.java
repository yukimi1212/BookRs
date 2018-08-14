package com.zucc.demo.dao;

import com.zucc.demo.model.RatingVo;

import java.util.List;

public interface BookDAO
{
    public List<RatingVo> getAllPages();
    public List<RatingVo> getSearch(int user_id,String searchWord);
}
