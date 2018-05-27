package com.swj.ics.web_api.config;


import org.springframework.data.querydsl.QPageRequest;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

/**
 * Created by swj on 2016/12/6.
 */
    public class JavaConfigWebAppInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext container) throws ServletException {
        //create the root spring application context
        AnnotationConfigWebApplicationContext rootContext=new AnnotationConfigWebApplicationContext();
        rootContext.register(JavaConfigServiceLayerConfig.class);

        //manage the  lifecycle of the root application context
        container.addListener(new ContextLoaderListener(rootContext));

        //create the spring mvc's dispatcher servlet for the spring mvc application context
        AnnotationConfigWebApplicationContext dispatcherServlet=new AnnotationConfigWebApplicationContext();
        dispatcherServlet.register(JavaConfigMvcConfig.class);

        //register and map the dispatcher servlet.servlet 3.0的动态注入servlet
        ServletRegistration.Dynamic dispatcher=container.addServlet("dispatcher",new DispatcherServlet(dispatcherServlet));
        dispatcher.addMapping("/");
        dispatcher.setLoadOnStartup(1);
    }
}
