package com.pet_clinic_end.filter;

import com.alibaba.fastjson.JSON;
import com.pet_clinic_end.common.BaseContext;
import com.pet_clinic_end.common.Result;
import com.pet_clinic_end.entity.User;
import com.pet_clinic_end.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Component
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
    @Autowired
    UserService userService;
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String requestURI = request.getRequestURI();

        log.info("拦截到请求 {}", requestURI);
        String[] urls = new String[] {
                "/user/login",
                "/user/register",
                "/user/code"
        };
        String[] adminUrls = new String[]{
                "/user/page",
                "/case/add",
                "/case/update",
                "/case/delete",
                "/item/add",
                "/item/delete",
                "/medicine/add",
                "/medicine/delete",
                "/question/**",
                "/paper/update",
                "/paper/page",
                "/paper/delete",
                "/paper/addQuestion",
                "/exam/update",
                "/exam/addUser"
        };

        boolean check = check(urls, requestURI);

        // 访问特定可以不登录的接口
        if (check) {
            log.info("该接口允许未登录用户访问");
            filterChain.doFilter(request,response);
            return;
        }
        // 访问特定接口之外的接口 未登录
        if (request.getSession().getAttribute("email")==null){
            log.info("未登录，本次请求{}不需要处理", requestURI);
            return;
        }

        // 访问特定接口之外的接口 已登陆
        boolean checkAdmin = check(adminUrls, requestURI);
        if (checkAdmin) {
            log.info("访问管理员接口");
            String email = request.getSession(false).getAttribute("email").toString();
            User loginUser = userService.getUserByEmail(email);
            if (loginUser.getRole()!=0) {
                log.info("该登录用户没有管理员权限，本次请求{}不需要处理", requestURI);
                return;
            }
        }

        filterChain.doFilter(request,response);
        return;

//        if(request.getSession().getAttribute("employee") != null) {
//            log.info("用户已登录, 用户id为: {}", request.getSession().getAttribute("employee"));
//            Long empId = (Long) request.getSession().getAttribute("employee");
//            BaseContext.setCurrentId(empId);
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        if(request.getSession().getAttribute("user") != null) {
//            log.info("用户已登录, 用户id为: {}", request.getSession().getAttribute("user"));
//            Long empId = (Long) request.getSession().getAttribute("user");
//            BaseContext.setCurrentId(empId);
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        log.info("用户未登录");
//
//        response.getWriter().write(JSON.toJSONString(Result.error("NOTLOGIN")));
//        return;
    }

    public boolean check(String[] urls, String requestURI) {
        for (String url : urls){
            boolean match = PATH_MATCHER.match(url, requestURI);
            if (match) {
                return true;
            }
        }
        return false;
    }
}
