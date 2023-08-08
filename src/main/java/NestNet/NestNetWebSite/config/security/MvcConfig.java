package NestNet.NestNetWebSite.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

//    private final long MAX_AGE_SECS = 3600;

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {

        corsRegistry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true);
//                .maxAge(MAX_AGE_SECS);
    }


}
