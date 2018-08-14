package com.zucc.demo.controller;

import com.zucc.demo.dao.UserDAOImpl;
import com.zucc.demo.model.BookVo;
import com.zucc.demo.model.RatingVo;
import com.zucc.demo.model.UserVo;
import org.jetbrains.annotations.NotNull;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private UserDAOImpl userDAO = new UserDAOImpl();

    @RequestMapping(value = "/individual", method = RequestMethod.GET)
    public ModelAndView indiPage() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("individual");
        return mav;
    }
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView loginPage() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("index");
        return mav;
    }

    /*@RequestMapping(value = "/Info", method = RequestMethod.GET)
    public ModelAndView infoPage() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("info");
        return mav;
    }*/

    @RequestMapping(value = "/check", method = RequestMethod.POST)
    private void check(HttpServletRequest request,HttpServletResponse response) throws IOException{
        String sid = request.getParameter("id");
        String pwd =request.getParameter("pwd");
        String msg = "{\"content\":\"正确\",\"flag\":true}";
        try{
            if(sid.equals(""))
                throw new Exception("用户id不能为空");
            if(!UserDAOImpl.isNum(sid))
                throw new Exception("用户id只能为数字");
            if(pwd.equals(""))
                throw new Exception("密码不能为空");
            int id = Integer.parseInt(sid);
            UserVo user = userDAO.login(id);
            if(user==null)
                throw new Exception("用户不存在");
            else
                if(!pwd.equals(user.getPasswd()))
                    throw new Exception("用户密码错误");
            List<BookVo> list = userDAO.recommend(id);
            request.getSession().setAttribute("user",user);
            request.getSession().setAttribute("list",list);
            logger.info("check{}---userid:"+sid);
        }
        catch(Exception ex){
            msg = "{\"content\":\""+ex.getMessage()+"\",\"flag\":false}";
        }
        finally {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write(msg);
        }
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    private void add(HttpServletRequest request,HttpServletResponse response) throws IOException{
        String location = new String(request.getParameter("location").getBytes("iso-8859-1"), "utf-8");
        String age =request.getParameter("age");
        String pwd =request.getParameter("pwd");
        String msg = "";
        try{
            if(location.equals(""))
                throw new Exception("地址不能为空");
            if(age.equals(""))
                throw new Exception("年龄不能为空");
            if(!UserDAOImpl.isNum(age))
                throw new Exception("年龄只能为数字");
            if(pwd.equals(""))
                throw new Exception("密码不能为空");
            UserVo user = userDAO.register(pwd,location,age);
            msg = "{\"content\":\""+user.getUser_id()+"\",\"flag\":true}";
            List<BookVo> list = userDAO.recommend(user.getUser_id());
            request.getSession().setAttribute("user",user);
            request.getSession().setAttribute("list",list);
            logger.info("add{}---userid:"+user.getUser_id());
        }
        catch(Exception ex){
            msg = "{\"content\":\""+ex.getMessage()+"\",\"flag\":false}";
        }
        finally {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write(msg);
        }
    }

    @RequestMapping(value = "/personal", method = RequestMethod.GET)
    private String personal(HttpServletRequest request,HttpServletResponse response){
        int id = Integer.parseInt(request.getParameter("id"));
        try{

            List<RatingVo> list = userDAO.already(id);
            request.getSession().setAttribute("alist",list);
            logger.info("personal{}---userid:"+id);
        }
        catch(Exception ex) {
            System.out.println(ex.getMessage());
        }
        return "info";
    }

    @RequestMapping(value = "/value", method = RequestMethod.POST)
    private void value(HttpServletRequest request,HttpServletResponse response) throws IOException{
        int id = Integer.parseInt(request.getParameter("id"));
        String isbn = request.getParameter("isbn");
        int score = Integer.parseInt(request.getParameter("score"));
        String msg = "";
        try{
            int flag = userDAO.value(id,isbn,score);
            List<BookVo> list = userDAO.recommend(id);
            request.getSession().setAttribute("list",list);
            if(flag==1)
                throw new Exception("您已评过分");
            msg = "{\"content\":\"评分成功\",\"flag\":true}";
            logger.info("value{}---score:"+ score);
        }
        catch(Exception ex){
            msg = "{\"content\":\""+ex.getMessage()+"\",\"flag\":false}";
        }
        finally {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write(msg);
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
