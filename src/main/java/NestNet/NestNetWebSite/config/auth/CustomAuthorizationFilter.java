package NestNet.NestNetWebSite.config.auth;

import NestNet.NestNetWebSite.config.jwt.TokenProvider;
import NestNet.NestNetWebSite.config.redis.RedisUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class CustomAuthorizationFilter extends OncePerRequestFilter {       //http 요청마다 처리하도록 하는 필터

    public static final String AUTHORIZATION_HEADER = "Authorization";
    private final TokenProvider tokenProvider;
    private final RedisUtil redisUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String servletPath = request.getServletPath();
        String requestURI = request.getRequestURI();

        //회원가입 or 로그인 or 리프레시 요청이면 토큰을 검사하지 않음
        if(servletPath.equals("/auth/signup") || servletPath.equals("/auth/login") ||
                servletPath.equals("/auth/refresh") || servletPath.equals("/member/find-id") || servletPath.equals("/member/get-temp-pw") || servletPath.equals("/file")){
            log.info("CustomAuthorizationFilter.class / doFilterInternal :" + servletPath +  ": 엑세스 토큰을 검사하지 않음");
        }
        else{
            log.info("CustomAuthorizationFilter.class / doFilterInternal :" + servletPath +  ": 엑세스 토큰을 검사");

            String accessToken = tokenProvider.resolveToken(request);
            System.out.println("엑세스 토큰 : " + accessToken);

            // 엑세스 토큰 없으면 -> 다시 로그인 해야함
            if(!StringUtils.hasText(accessToken)){
                log.info("CustomAuthorizationFilter.class / doFilterInternal : 엑세스 토큰 없음");
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setStatus(HttpStatus.FORBIDDEN.value());
            }

            // 블랙리스트에 있으면 401에러 -> 다시 로그인 해야함.
            if(redisUtil.hasKey(accessToken)){
                log.info("CustomAuthorizationFilter.class / doFilterInternal : 블랙리스트에 등록된 엑세스 토큰");
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                return;
            }

            System.out.println("필터 access : " + accessToken);

            // access 토큰 검증
            Map<String, Object> map = tokenProvider.validateAccessToken(accessToken, request, response);

            response = (HttpServletResponse) map.get("response");

            if(map.get("accessToken") != null){
                String validatedAccessToken = map.get("accessToken").toString();

                Authentication authentication = tokenProvider.getAuthentication(validatedAccessToken);

                //토큰을 통해 생성한 Authentication 객체 스프링 시큐리티 컨텍스트에 저장
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.debug("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);
            }
            else{
                log.info("CustomAuthorizationFilter.class / doFilterInternal : JWT access 토큰, refresh 토큰 모두 유효하지 않음");
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                return;
            }
        }
        filterChain.doFilter(request, response);        //다음 필터 실행
    }
}
