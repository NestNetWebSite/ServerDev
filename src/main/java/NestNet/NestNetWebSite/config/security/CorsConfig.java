package NestNet.NestNetWebSite.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

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
        configuration.addAllowedOriginPattern("http://192.168.35.25:3000");      //모든 ip에 대해 응답을 허용
        configuration.addAllowedHeader("*");             //모든 header에 대해 응답을 허용
        configuration.addAllowedMethod("*");             //모든 매서드(get, post, put, delete..)에 대해 응답을 허용

        source.registerCorsConfiguration("/**", configuration);     //configuration을 모든 경로에 적용

        return new CorsFilter(source);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
//                .allowedOrigins("/**")
                .allowedOrigins("http://192.168.35.25:3000")
                .allowCredentials(true)
                .allowedMethods("OPTIONS", "GET", "POST", "PUT", "DELETE");
    }
}
