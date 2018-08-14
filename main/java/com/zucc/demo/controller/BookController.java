package com.zucc.demo.controller;

import com.zucc.demo.dao.BookDAO;
import com.zucc.demo.model.BookVo;
import com.zucc.demo.model.RatingVo;
import net.sf.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;


import javax.annotation.Resource;
import javax.naming.directory.SearchControls;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@RestController
public class BookController {

    private static final Logger logger = LoggerFactory.getLogger(BookController.class);

    @Autowired
    BookDAO bookDAO;

    @RequestMapping(value = "/book", method = RequestMethod.GET)
    public Map<String,Object> getSearch(String id, String searchWord) {
        logger.info("getSearch {}", searchWord);
        List<RatingVo> result = new ArrayList<>();
        Map<String,Object> map = new HashMap<String,Object>();
        result = bookDAO.getSearch(Integer.parseInt(id),searchWord);
        map.put("searchList",result);
//        JSONArray list = JSONArray.fromObject(result);
        return map;
    }
}
