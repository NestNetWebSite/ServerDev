package NestNet.NestNetWebSite.config.jwt;

//import lombok.AllArgsConstructor;
//import lombok.NoArgsConstructor;
//import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.web.DefaultSecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//@NoArgsConstructor @AllArgsConstructor
//public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
//
//    private TokenProvider tokenProvider;
//
//    /*
//    UsernamePasswordAuthenticationFilter 앞에 커스텀 필터 등록
//     */
//    @Override
//    public void configure(HttpSecurity http) {
//        // UsernamePasswordAuthenticationFilter 앞에 JwtFilter 추가
//        http.addFilterBefore(new CustomJwtFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class);
//    }
//    /*
//    UsernamePasswordAuthenticationFilter는 request에서 username, password를 가져와서 UsernamePasswordAuthenticationToken 를 생성한 후
//    AuthenticationManager를 구현한 객체에 인증을 위임한다.
//     */
//
//}
