package NestNet.NestNetWebSite.config.servlet;

import NestNet.NestNetWebSite.config.auth.CustomAuthorizationFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class CustomFilterConfiguration {

    @Bean
    public FilterRegistrationBean<CustomAuthorizationFilter> registrationBean(CustomAuthorizationFilter filter){

        FilterRegistrationBean<CustomAuthorizationFilter> registration = new FilterRegistrationBean<>(filter);
        registration.setEnabled(false);

        return registration;
    }
}
