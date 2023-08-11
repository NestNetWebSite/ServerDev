//package NestNet.NestNetWebSite.config.security;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
///*
//   Cross-Origin Resource Sharing (CORS)
//   보안상의 이유로 브라우저는 다른 도메인의 리소스에 접근하는 교차 출처 HTTP 요청을 제한한다. '동일 출처 정책' (Same Origin Policy, SOP)
//   이를 위해서는 서버의 동의가 필요하며, 교차 출처 요청에 대해 동의하기 위해 Cross-Origin Resource Sharing를 구성해야 한다.
//   이를 위해 커스텀 필터인 corsFilter를 생성하고 추가하는 방법으로 해결할 수 있다.
//   */
//
//@Configuration
//public class MvcConfig implements WebMvcConfigurer {
//
////    private final long MAX_AGE_SECS = 3600;
//
//    @Override
//    public void addCorsMappings(CorsRegistry corsRegistry) {
//
//        corsRegistry.addMapping("/**")
//                .allowedOrigins("*")                // 모든 ip에 대해 응답 허용
//                .allowedMethods("*")                // 모든 매서드 (GET, POST, PUT, DELETE..)에 대해 응답 허용
//                .allowedHeaders("*")                // 모든 요청 헤더에 대해 응답 허용
//                .allowCredentials(true);            // 자격 증명 허용 여부 설정 / Authorization 이용한 인증 서비스에 사용
////                .maxAge(MAX_AGE_SECS);
//    }
//
//
//}
