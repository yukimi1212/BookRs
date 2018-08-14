package com.zucc.demo.controller;

import com.alibaba.fastjson.JSON;
import com.zucc.demo.dao.BookDAO;
import com.zucc.demo.dao.UserDAOImpl;
import com.zucc.demo.dao.UserDAO;
import com.zucc.demo.model.BookVo;
import com.zucc.demo.model.RatingVo;
import com.zucc.demo.model.UserVo;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    BookDAO bookDAO;

    @Autowired
    UserDAO userDAO;

    @RequestMapping(value = "/individual", method = RequestMethod.GET)
    public ModelAndView indiPage() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("individual");
        return mav;
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ModelAndView loginPage() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("search");
        return mav;
    }

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public ModelAndView infoPage() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("info");
        return mav;
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    private Map<String,Object> check(String id, String pwd) throws IOException{
        logger.info("check{}---userid:"+id);
        Map<String,Object> map = new HashMap<String,Object>();
        String msg = "{\"content\":\"正确\",\"flag\":true}";
        try{
            if(id.equals(""))
                throw new Exception("用户id不能为空");
            if(!UserDAOImpl.isNum(id))
                throw new Exception("用户id只能为数字");
            if(pwd.equals(""))
                throw new Exception("密码不能为空");
            int uid = Integer.parseInt(id);
            UserVo user = userDAO.login(uid);
            if(user==null)
                throw new Exception("用户不存在");
            else
                if(!pwd.equals(user.getPasswd()))
                    throw new Exception("用户密码错误");
            List<BookVo> list = userDAO.recommend(uid);
            map.put("userid",user.getUser_id());
            map.put("list",list);
        }
        catch(Exception ex){
            msg = "{\"content\":\""+ex.getMessage()+"\",\"flag\":false}";
        }
        finally {
            map.put("msg",msg);
            return map;
        }
    }

    @RequestMapping(value = "/user", method = RequestMethod.POST)
    private Map<String,Object> add(String location,String pwd, String age) throws IOException{
        System.out.println("enter add......");
        String nlocation = new String(location.getBytes("iso-8859-1"), "utf-8");
        String msg = "";
        Map<String,Object> map = new HashMap<String,Object>();
        try{
            if(location.equals(""))
                throw new Exception("地址不能为空");
            if(age.equals(""))
                throw new Exception("年龄不能为空");
            if(!UserDAOImpl.isNum(age))
                throw new Exception("年龄只能为数字");
            if(pwd.equals(""))
                throw new Exception("密码不能为空");
            UserVo user = userDAO.register(pwd,nlocation,age);
            msg = "{\"content\":\""+user.getUser_id()+"\",\"flag\":true}";
            List<BookVo> list = userDAO.recommend(user.getUser_id());
            map.put("userid",user.getUser_id());
            map.put("list",list);
            logger.info("add{}---userid:"+user.getUser_id());
        }
        catch(Exception ex){
            msg = "{\"content\":\""+ex.getMessage()+"\",\"flag\":false}";
        }
        finally {
            map.put("msg",msg);
            return map;
        }
    }

    @RequestMapping(value = "/getInfo", method = RequestMethod.GET)
    private List<RatingVo> personal(Integer id){
        List<RatingVo> list = null;
        try{
             list = userDAO.already(id);
            logger.info("personal{}---userid:"+id);
        }
        catch(Exception ex) {
            System.out.println(ex.getMessage());
        }
        finally {
            return list;
        }
    }

    @RequestMapping(value = "/user", method = RequestMethod.PUT)
    private Map<String,Object> value(Integer id, String isbn, Integer score) throws IOException{
        System.out.println("enter value ... ");
        String msg = "";
        List<RatingVo> searchList = null;
        List<BookVo> list = null;
        Map<String,Object> map = new HashMap<String,Object>();
        try{
            int flag = userDAO.value(id,isbn,score);
            list = userDAO.recommend(id);
            if(flag==1)
                throw new Exception("您已评过分");
            msg = "{\"content\":\"评分成功\",\"flag\":true}";
            map.put("list",list);
            map.put("searchList",searchList);
            logger.info("value{}---score:"+ score);
        }
        catch(Exception ex){
            msg = "{\"content\":\""+ex.getMessage()+"\",\"flag\":false}";
        }
        finally {
            map.put("msg",msg);
            return map;
        }
    }
   /* @RequestMapping(value = "/personal", method = RequestMethod.POST)
    private void personal(HttpServletRequest request,HttpServletResponse response) throws IOException{
        int id = Integer.parseInt(request.getParameter("id"));
        String msg = "";
        try{
            msg = "{\"flag\":true}";
            List<RatingVo> list = userDAO.already(id);
            request.getSession().setAttribute("alist",list);
            logger.info("personal{}---userid:"+id);
        }
        catch(Exception ex) {
            System.out.println(ex.getMessage());
        }
        finally {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write(msg);
        }
    }*/
}
