package com.company.config;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
//
//@Component
//public class JwtConfig extends GenericFilterBean {
//
//
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
//            throws IOException, ServletException {
//
//        final HttpServletRequest request = (HttpServletRequest) servletRequest;
//        final HttpServletResponse response = (HttpServletResponse) servletResponse;
//
//        final String authHeader = request.getHeader("Authorization");
////        if (authHeader == null || authHeader.isBlank() || authHeader.startsWith("Bearer ")){
////            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
////            response.setHeader("Message","Not fount Token!");
////            return;
////        }
//
//        String[] jwtArray = authHeader.split(" ");
//
////        if (jwtArray.length != 2) {
////            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
////            response.setHeader("Message", "Token Not Valid");
////            return;
////        }
//
//        String jwt = jwtArray[1];
//        //
//
//
//
//    }
//}
