package NestNet.NestNetWebSite.config.auth;

import NestNet.NestNetWebSite.config.jwt.TokenProvider;
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

        String servletPath = request.getServletPath();
        String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);
        String requestURI = request.getRequestURI();

        //로그인 or 리프레시 요청이면 토큰을 검사하지 않음
        if(servletPath.equals("auth/login") || servletPath.equals("auth/refresh")){
            filterChain.doFilter(request, response);
        }
        else if(authorizationHeader == null ||!authorizationHeader.startsWith("Bearer ")){  //토큰 값이 없거나 정상적이지 않을 때
            log.info("CustomAuthorizationFilter: JWT 토큰이 존재하지 않거나 정상적이지 않습니다.");

            //=========추후 예외처리 관련하여 리펙토링 예정=========//

        }
        else{
            try{
                String accessToken = authorizationHeader.substring(("Bearer ").length());       //access토큰 꺼내옴

                // access 토큰 검증

                if(StringUtils.hasText(accessToken) && tokenProvider.validateToken(accessToken)){
                    Authentication authentication = tokenProvider.getAuthentication(accessToken);
                    SecurityContextHolder.getContext().setAuthentication(authentication);       //스프링 시큐리티에 Authetication 저장
                    log.debug("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);
                }
                filterChain.doFilter(request, response);        //인증 처리 후 다음 필터 수행

            } catch (Exception e) {      //만료됐을때 처리. refresh 토큰으로부터 access토큰 발행받도록

            }
        }

    }
}
