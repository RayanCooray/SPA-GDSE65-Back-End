package com.ijse.gdse.spagdse65backend.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter(urlPatterns = "/*")
public class CrossFilter extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        System.out.println("CROSFilter");
        String origin = req.getHeader("Origin");
        System.out.println(origin);
        if (origin != null && origin.contains(getServletContext().getInitParameter("origin"))){//context parameter ek check krl balanwa
            res.setHeader("Access-Control-Allow-Origin",origin);//domain ek allow krnw(fornt end eka)
            res.setHeader("Access-Control-Allow-Methods","GET,POST,PUT,DELETE,HEADER");//front end eken access krnn ona methods
            res.setHeader("Access-Control-Allow-Headers","Content-Type");//meka adala wenne json or xml req (content type ekt allow krnn)
            res.setHeader("Access-Control-Expose-Headers","Content-Type");//js engine ekt expose krnn ona dewal expose krnn wenw (javascript engine ekt expose krnn)
        }
        chain.doFilter(req,res);
    }
}