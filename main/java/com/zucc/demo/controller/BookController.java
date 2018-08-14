package com.zucc.demo.controller;

import com.zucc.demo.dao.BookDAO;
import com.zucc.demo.model.BookVo;
import com.zucc.demo.model.RatingVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;


import javax.annotation.Resource;
import javax.naming.directory.SearchControls;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
public class BookController {

    private static final Logger logger = LoggerFactory.getLogger(BookController.class);

    @Autowired
    BookDAO bookDAO;

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String getSearch(HttpServletRequest request, HttpServletResponse response) {
        String user_id = request.getParameter("id");
        String searchWord = request.getParameter("searchword");

        List<RatingVo> result = new ArrayList<>();
        result = bookDAO.getSearch(Integer.parseInt(user_id),searchWord);
        request.getSession().setAttribute("searchWord",searchWord);
        request.getSession().setAttribute("slist",result);
        logger.info("getSearch {}", searchWord);

        return "search";
    }
}
