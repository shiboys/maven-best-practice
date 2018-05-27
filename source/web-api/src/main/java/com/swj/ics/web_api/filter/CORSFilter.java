package com.swj.ics.web_api.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by swj on 2016/12/5.
 */
public class CORSFilter extends OncePerRequestFilter {

    private Logger logger= LoggerFactory.getLogger(CORSFilter.class);
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        logger.info("Adding CORS Header........");
        response.addHeader("Access-Control-Allow-Origin","*");
        response.addHeader("Access-Control-Allow-Methods","GET,POST,PUT,DELETE,OPTIONS");
        response.addHeader("Access-Control-Allow-Headers","*");
        response.addHeader("Access-Control-Allow-Max-Age","3600");

        filterChain.doFilter(request, response);
    }
}
