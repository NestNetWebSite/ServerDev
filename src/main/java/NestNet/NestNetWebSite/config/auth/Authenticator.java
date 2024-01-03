package NestNet.NestNetWebSite.config.auth;

import NestNet.NestNetWebSite.service.auth.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class Authenticator {

    private final CustomUserDetailsService customUserDetailsService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    /*
    로그인 아이디와 비밀번호를 이용해 Authentication 생성
     */
    public Authentication createAuthenticationByIdPassword(String loginId, String password){

        //인증 전의 UsernamePasswordAuthenticationToken 객체(Authentication의 구현체)
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginId, password);

        //인증 후의 Authentication 객체
        //authenticationManager가 UserDetailsService의 loadByUsername매서드를 호출하여 인증 수행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        return authentication;
    }

    /*
    로그인 아이디를 이용해 Authentication 생성
     */
    public Authentication createAuthentication(String loginId){

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(loginId);

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    /*
    스프링 시큐리티 컨텍스트에 Authentication 세팅
     */
    public void setAuthenticationInSecurityContext(Authentication authentication){

        SecurityContextHolder.getContext().setAuthentication(authentication);

        log.info("Authenticator.class / Security Context에 '{}' 인증 정보를 저장했습니다", authentication.getName());
    }
}
