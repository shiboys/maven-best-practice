package com.swj.ics.web_api.config;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by swj on 2016/12/5.
 * * If the customization options of WebMvcConfigurer do not expose something you need to configure,
 * consider removing the @EnableWebMvc annotation and extending directly from WebMvcConfigurationSupport
 * overriding selected @Bean methods
 *
 *
 * Its better to extend WebMvcConfigurationSupport. It provides more customization options and also works fine with

 configureMessageConverters(List<HttpMessageConverter<?>> converters)
 cause you can add these convertors using

 addDefaultHttpMessageConverters(converters);
 that is not available with WebMvcConfigurerAdapter.

 Click [here] How to configure MappingJacksonHttpMessageConverter while using spring annotation-based configuration?

 If you extend WebMvcConfigurerAdapter, it behaves strangely with configuring Jackson and Jaxb. That happened with me !!!
 */
@EnableWebMvc
@Configuration
@ComponentScan("com.swj.ics.redis.spring_redis")
public class WebConfig extends WebMvcConfigurerAdapter {

    @Bean
    public RestTemplate template()
    {
        return new RestTemplate();
    }
}
