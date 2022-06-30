package com.company.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecuredFilterConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public FilterRegistrationBean<JwtFilter> filterRegistrationBeanRegion() {
        FilterRegistrationBean<JwtFilter> bean = new FilterRegistrationBean<JwtFilter>();
        bean.setFilter(jwtFilter);

      //  bean.addUrlPatterns("/profile/*");
        bean.addUrlPatterns("/attach/upload/*");
//        bean.addUrlPatterns("/attach/upload/factory/*");
//        bean.addUrlPatterns("/attach/upload/profile");
        bean.addUrlPatterns("/attach/factory");
        bean.addUrlPatterns("/attach/profile");
        bean.addUrlPatterns("/attach/product");
        bean.addUrlPatterns("/factory/adm/*");
        bean.addUrlPatterns("/product/adm/*");
      //  bean.addUrlPatterns("/sms/pagination");
      //  bean.addUrlPatterns("/email/pagination");
      //  bean.addUrlPatterns("/type/adm/*");
      //  bean.addUrlPatterns("/article_like/*");
      //  bean.addUrlPatterns("/comment_like/*");
      //  bean.addUrlPatterns("/article_save/*");
      //  bean.addUrlPatterns("/article/adm/*");

        return bean;
    }

}
