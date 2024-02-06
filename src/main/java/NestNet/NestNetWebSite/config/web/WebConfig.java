package NestNet.NestNetWebSite.config.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Value("#{environment['filePath']}")
    private String filePath;

    /*
    multipart 데이터 파싱 및 처리
     */
    @Bean
    public MultipartResolver multipartResolver(){
        return new StandardServletMultipartResolver();
    }

    /*
   Cross-Origin Resource Sharing (CORS)
   보안상의 이유로 브라우저는 다른 도메인의 리소스에 접근하는 교차 출처 HTTP 요청을 제한한다. '동일 출처 정책' (Same Origin Policy, SOP)
   이를 위해서는 서버의 동의가 필요하며, 교차 출처 요청에 대해 동의하기 위해 Cross-Origin Resource Sharing를 구성해야 한다.
   이를 위해 커스텀 필터인 corsFilter를 생성하고 추가하는 방법으로 해결할 수 있다.
   */

    @Bean
    public CorsFilter corsFilter(){

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();     //특정 url 패턴에 대해 cors 설정 적용
        CorsConfiguration configuration = new CorsConfiguration();      //CORS 관련 설정 정의할 객체

        configuration.setAllowCredentials(true);         //자격 증명 허용 여부 설정. Authorization을 이용해 인증 서비스를 할 때 true로 세팅
        configuration.addAllowedOriginPattern("http://**");
        configuration.addAllowedHeader("*");             //모든 header에 대해 응답을 허용
        configuration.addAllowedMethod("*");             //모든 매서드(get, post, put, delete..)에 대해 응답을 허용

        source.registerCorsConfiguration("/**", configuration);     //configuration을 모든 경로에 적용

        return new CorsFilter(source);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://**")
                .allowCredentials(true)
                .allowedMethods("OPTIONS", "GET", "POST", "PUT", "DELETE");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/image/**")
                .addResourceLocations("file:///" + filePath);
    }


}
