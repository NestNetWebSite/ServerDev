package NestNet.NestNetWebSite.config.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@Slf4j
@NoArgsConstructor @AllArgsConstructor
public class CustomJwtFilter extends GenericFilterBean {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    private TokenProvider tokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String jwt = resolveToken(httpServletRequest);              //jwt 토큰
        String requestURI = httpServletRequest.getRequestURI();     //요청한 URI

        log.info("requestURI: " + requestURI);

        //jwt 문자열이 null이 아니고 유효한 토큰이면 스프링 시큐리티 컨텍스트에 Authentication 객체 저장
        if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
            Authentication authentication = tokenProvider.getAuthentication(jwt);      //토큰에서 Authentication 정보 뽑아서 UsernamePasswordAuthenticationToken 객체 리턴

            SecurityContextHolder.getContext().setAuthentication(authentication);       //스프링 시큐리티에 Authetication 저장

            log.debug("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);
        } else {
            log.debug("유효한 JWT 토큰이 없습니다, uri: {}", requestURI);
        }

        chain.doFilter(request, response);
    }

    /*
    HttpServletRequest의 헤더에서 JWT 토큰 받아서 access 토큰 반환
    */
    private String resolveToken(HttpServletRequest request) {

        //bearer는 인증 타입중 하나로, jwt, oauth에 대한 토큰을 사용하는 인증을 뜻한다.
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        System.out.println(bearerToken);

        //StringUtils.hasText() : 공백이나 null이면 false
        //bearer 토큰은 'Bearer JWT 문자열' 이렇게 구성됨
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);       //JWT에서 "Bearer " 이부분 슬라이싱
        }

        return null;
    }

}
