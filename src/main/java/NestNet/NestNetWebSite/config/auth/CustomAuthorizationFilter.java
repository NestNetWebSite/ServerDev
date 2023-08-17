package NestNet.NestNetWebSite.config.auth;

import NestNet.NestNetWebSite.config.jwt.TokenProvider;
import NestNet.NestNetWebSite.config.redis.RedisUtil;
import NestNet.NestNetWebSite.exception.CustomException;
import NestNet.NestNetWebSite.service.token.RefreshTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.ErrorResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

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
        if(servletPath.equals("/auth/signup") || servletPath.equals("/auth/login") || servletPath.equals("/auth/refresh")){
            log.info("CustomAuthorizationFilter / doFilterInternal :" + servletPath +  ": 엑세스 토큰을 검사하지 않음");
        }
        else{
            log.info("CustomAuthorizationFilter / doFilterInternal :" + servletPath +  ": 엑세스 토큰을 검사");
            try{
                String accessToken = tokenProvider.resolveToken(request);
                System.out.println("엑세스 토큰 : " + accessToken);

                // 블랙리스트에 있으면 401에러
                if(redisUtil.hasKey(accessToken)){
                    System.out.println("블랙리스트??");
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    return;
                }

                // access 토큰 검증
                if(StringUtils.hasText(accessToken) && tokenProvider.validateAccessToken(accessToken, request, response)){
                    Authentication authentication = tokenProvider.getAuthentication(accessToken);

                    //토큰을 통해 생성한 Authentication 객체 스프링 시큐리티 컨텍스트에 저장
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.debug("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);
                }

            } catch (CustomException e) {      //만료됐을때 처리. refresh 토큰으로부터 access토큰 발행받도록
                SecurityContextHolder.clearContext();
                log.debug("access 토큰 만료");

                return;
            }
        }
        filterChain.doFilter(request, response);        //다음 필터 실행
    }
}
