package com.nowcoder.controller;

import com.nowcoder.aspect.LogAspect;
import com.nowcoder.dao.UserDAO;
import com.nowcoder.service.ToutiaoService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;


/**
 * Created by Administrator on 2017/6/19.
 */
//@Controller
public class IndexController {

    private static final org.slf4j.Logger logger=LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private ToutiaoService toutiaoService;

    @RequestMapping("/")
    @ResponseBody
    public String index(HttpSession session){
        logger.info("Visit Index");
        return "Hello Nowcoder"+session.getAttribute("msg")+"<br>"+toutiaoService.say();
    }

    @RequestMapping(value = {"/vm"})
    @ResponseBody
    public String news(ModelMap map){
        map.addAttribute("name","guo");
        return "news.vm";
    }

    @RequestMapping(value = {"/request"})
    @ResponseBody
    public String request(HttpServletRequest request,
                          HttpServletResponse response,
                          HttpSession session){
        StringBuilder sb=new StringBuilder();
        Enumeration<String> headerNames=request.getHeaderNames();
        while (headerNames.hasMoreElements()){
            String name=headerNames.nextElement();
            sb.append(name+":"+request.getHeader(name)+"<br>");
        }

        for(Cookie cookie:request.getCookies()){
            sb.append("Cookie:");
            sb.append(cookie.getName());
            sb.append(":");
            sb.append(cookie.getValue());
            sb.append("<br>");
        }

        return sb.toString();
    }

    @RequestMapping("/redirect/{code}")
    public RedirectView redirect(@PathVariable("code") int code,
                                 HttpSession session){
        RedirectView red=new RedirectView("/",true);
        if(code==301){
            red.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        }
        session.setAttribute("msg","Jump from redirect");
        return red;
    }

    @RequestMapping("/admin")
    @ResponseBody
    public String admin(@RequestParam(value = "key",required = false) String key){
        if("admin".equals(key)){
            return "hello admin";
        }
       throw new IllegalArgumentException("Key cuowu");
    }

    @ExceptionHandler()
    @ResponseBody
    public String error(Exception e){
        return "error:"+e.getMessage();
    }



}

