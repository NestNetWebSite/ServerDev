package NestNet.NestNetWebSite.config.auth;

import NestNet.NestNetWebSite.config.jwt.TokenProvider;
import NestNet.NestNetWebSite.exception.CustomException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        System.out.println("CustomAuthorizationFilter ");

        String servletPath = request.getServletPath();
        String requestURI = request.getRequestURI();

        //회원가입 or 로그인 or 리프레시 요청이면 토큰을 검사하지 않음
        if(servletPath.equals("/auth/signup") || servletPath.equals("/auth/login") || servletPath.equals("/auth/refresh")){
            log.info(servletPath + " : 토큰을 검사하지 않음");
        }
        else{
            System.out.println("CustomAuthorizationFilter 여기 들어오나?");
            try{
                String accessToken = tokenProvider.resolveToken(request);

                System.out.println("여기 토큰 : " + accessToken);

                // access 토큰 검증
                if(StringUtils.hasText(accessToken) && tokenProvider.validateToken(accessToken)){
                    Authentication authentication = tokenProvider.getAuthentication(accessToken);
                    System.out.println("여기 authentication : " + authentication);
                    //토큰을 통해 생성한 Authentication 객체 스프링 시큐리티 컨텍스트에 저장
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.debug("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);
                }

            } catch (CustomException e) {      //만료됐을때 처리. refresh 토큰으로부터 access토큰 발행받도록
                SecurityContextHolder.clearContext();
                log.debug("access 토큰 만료");

                String refreshToken = tokenProvider.getRefreshToken(request);       //쿠키에서 리프레쉬 토큰 가져옴

                /*
                레디스랑 비교하는 로직 들어가야 함.
                 */

                return;
            }
        }
        filterChain.doFilter(request, response);        //다음 필터 실행
    }
}
