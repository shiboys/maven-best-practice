package com.swj.ics.web_api.config;

import com.swj.ics.web_api.filter.CORSFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * Created by swj on 2016/12/5.
 * Remember that WebApplicationInitializer implementations are detected automatically
 * -- so you are free to package them within your application as you see fit.
 */
    public class ServletInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    protected Class<?>[] getRootConfigClasses() {
        return null;
    }

    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[]{WebConfig.class};
    }

    protected String[] getServletMappings() {
        return null;
    }

    @Override
    protected javax.servlet.Filter[] getServletFilters() {
        return new javax.servlet.Filter[]{new CORSFilter()};
    }
}
