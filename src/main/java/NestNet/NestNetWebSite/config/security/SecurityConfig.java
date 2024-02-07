package NestNet.NestNetWebSite.config.security;

import NestNet.NestNetWebSite.config.auth.Authenticator;
import NestNet.NestNetWebSite.config.auth.CustomAuthorizationFilter;
import NestNet.NestNetWebSite.config.jwt.TokenProvider;
import NestNet.NestNetWebSite.config.jwt.errorHandler.JwtAccessDeniedHandler;
import NestNet.NestNetWebSite.config.jwt.errorHandler.JwtAuthenticationEntryPoint;
import NestNet.NestNetWebSite.config.redis.RedisUtil;
import NestNet.NestNetWebSite.repository.member.MemberRepository;
import NestNet.NestNetWebSite.service.auth.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final TokenProvider tokenProvider;
    private final Authenticator authenticator;
    private final CorsFilter corsFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final MemberRepository memberRepository;
    private final RedisUtil redisUtil;

    /*
    비밀번호 암호화를 담당할 인코더 설정
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /*
    loadUserByUsername 을 사용하여 UserDetails 객체를 가져올 수 있도록 하는 설정
    UserDetails는 시큐리티 컨텍스트에 사용자 정보를 담는 인터페이스
     */
    @Bean
    public UserDetailsService userDetailsService(){
        return new CustomUserDetailsService(memberRepository);
    }

    /*
    커스텀 필터
     */
    @Bean
    public CustomAuthorizationFilter customAuthorizationFilter(){
        return new CustomAuthorizationFilter(tokenProvider, authenticator, redisUtil);
    }


    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());

        return provider;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return web -> web
                .ignoring()
                .requestMatchers("/swagger-ui/index.html", "/swagger-ui/**", "/v3/api-docs/**")
                .requestMatchers("/auth/signup", "/auth/login", "/auth/mail-auth", "/auth/mail-auth-answer")
                .requestMatchers("/member/find-id", "/member/get-temp-pw")
                .requestMatchers("/attendance/statistics")
                .requestMatchers("/life4cut")
                .requestMatchers("/post/recent-posts")
                .requestMatchers("/executive-info/prev", "/executive-info/current")
                .requestMatchers("/file/**")
                .requestMatchers("/forbidden", "/unauthorized")
                .requestMatchers("/image/**");
    }

    /*
    스프링 시큐리티 구성을 정의하는 필터체인 구성
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                //API 통신을 하는 애플리케이션의 경우 csrf 공격을 받을 가능성이 없기 때문에 @EnableWebSecurity의 csrf 보호 기능을 해제
                .csrf(csrf -> csrf.disable())

                //jwt를 사용하기 때문에 세션 사용하지 않음
                .sessionManagement((sessionManagement) -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                //각 예외 인터페이스를 커스텀한 두 예외 등록. 401, 403 에러
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(jwtAccessDeniedHandler)
                )

                //http 요청에 대한 접근 권한을 설정
                //로그인, 회원가입 api는 토큰이 없는 상태로 요청이 들어오기 때문에 permitAll()로 열어줌
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()        //html, css같은 정적 리소스에 대해 접근 허용
                        // 출석은 모든 권한 접근 가능 (승인대기, 탈퇴 제외)
                        .requestMatchers("/attendance").hasAnyAuthority("ADMIN", "PRESIDENT", "VICE_PRESIDENT", "MANAGER", "GENERAL_MEMBER", "ON_LEAVE_MEMBER", "GRADUATED_MEMBER")
                        // 댓글 관련은 모든 권한 접근 가능 (승인대기, 탈퇴 제외)
                        .requestMatchers("/comment/**").hasAnyAuthority("ADMIN", "PRESIDENT", "VICE_PRESIDENT", "MANAGER", "GENERAL_MEMBER", "ON_LEAVE_MEMBER", "GRADUATED_MEMBER")
                        // 임원 정보 저장/수정/삭제는 회장, 부회장, 관리자만 접근 가능
                        .requestMatchers("/executive-info/**").hasAnyAuthority("ADMIN", "PRESIDENT", "VICE_PRESIDENT", "MANAGER")
                        // 파일 저장은 모든 권한 접근 가능 (승인대기, 탈퇴 제외)
                        .requestMatchers("/file").hasAnyAuthority("ADMIN", "PRESIDENT", "VICE_PRESIDENT", "MANAGER", "GENERAL_MEMBER", "ON_LEAVE_MEMBER", "GRADUATED_MEMBER")
                        // 인생네컷 저장은 회장, 부회장, 관리자만 접근 가능
                        .requestMatchers("/life4cut/save").hasAnyAuthority("ADMIN", "PRESIDENT", "VICE_PRESIDENT", "MANAGER")
                        // 관리자 페이지는 관리자만 접근 가능
                        .requestMatchers("/manager/**").hasAnyAuthority("ADMIN", "MANAGER")
                        // 회원 정보 수정/비번 인증/비번 변경/탈퇴는 모든 권한 접근 가능 (승인대기, 탈퇴 제외)
                        .requestMatchers("/member/**").hasAnyAuthority("ADMIN", "PRESIDENT", "VICE_PRESIDENT", "MANAGER", "GENERAL_MEMBER", "ON_LEAVE_MEMBER", "GRADUATED_MEMBER")
                        // 회원 프로필 관련은 모든 회원 접근 가능 (승인대기, 탈퇴 제외)
                        .requestMatchers("/member-profile/**").hasAnyAuthority("ADMIN", "PRESIDENT", "VICE_PRESIDENT", "MANAGER", "GENERAL_MEMBER", "ON_LEAVE_MEMBER", "GRADUATED_MEMBER")
                        // 족보 게시판은 모든 회원 접근 가능 (승인대기, 탈퇴 제외)
                        .requestMatchers("/exam-collection-post/**").hasAnyAuthority("ADMIN", "PRESIDENT", "VICE_PRESIDENT", "MANAGER", "GENERAL_MEMBER", "ON_LEAVE_MEMBER", "GRADUATED_MEMBER")
                        // 자기 소개 게시판은 모든 회원 접근 가능 (승인대기, 탈퇴 제외)
                        .requestMatchers("/introduction-post/**").hasAnyAuthority("ADMIN", "PRESIDENT", "VICE_PRESIDENT", "MANAGER", "GENERAL_MEMBER", "ON_LEAVE_MEMBER", "GRADUATED_MEMBER")
                        // 공지 사항 게시판 작성은 회장, 부회장, 관리자만 접근 가능
                        .requestMatchers("/notice-post/post").hasAnyAuthority("ADMIN", "PRESIDENT", "VICE_PRESIDENT", "MANAGER")
                        // 공지 사항 게시판 수정은 회장, 부회장, 관리자만 접근 가능
                        .requestMatchers("/notice-post/modify").hasAnyAuthority("ADMIN", "PRESIDENT", "VICE_PRESIDENT", "MANAGER")
                        // 사진 게시판 작성은 회장, 부회장, 관리자만 접근 가능
                        .requestMatchers("/photo-post/post").hasAnyAuthority("ADMIN", "PRESIDENT", "VICE_PRESIDENT", "MANAGER")
                        // 사진 게시판 수정은 회장, 부회장, 관리자만 접근 가능
                        .requestMatchers("/photo-post/modify").hasAnyAuthority("ADMIN", "PRESIDENT", "VICE_PRESIDENT", "MANAGER")
                        // 사진 게시판 목록 조회는 모든 회원 접근 가능 (승인대기, 탈퇴 제외)
                        .requestMatchers("/photo-post").hasAnyAuthority("ADMIN", "PRESIDENT", "VICE_PRESIDENT", "MANAGER", "GENERAL_MEMBER", "ON_LEAVE_MEMBER", "GRADUATED_MEMBER")
                        // 게시판 통합 기능은 모든 회원 접근 가능
                        .requestMatchers("/post/**").hasAnyAuthority("ADMIN", "PRESIDENT", "VICE_PRESIDENT", "MANAGER", "GENERAL_MEMBER", "ON_LEAVE_MEMBER", "GRADUATED_MEMBER")
                        // 통합 게시판은 모든 회원 접근 가능
                        .requestMatchers("/unified-post/**").hasAnyAuthority("ADMIN", "PRESIDENT", "VICE_PRESIDENT", "MANAGER", "GENERAL_MEMBER", "ON_LEAVE_MEMBER", "GRADUATED_MEMBER")
                        .anyRequest().authenticated()       //나머지 요청은 모두 권한 필요함.
                )

                // 헤더 관련 설정
                .headers(headers ->
                        headers.frameOptions(options ->
                                options.sameOrigin()            //x-frame-option 설정

                        )
                );

        http
                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(customAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


}
