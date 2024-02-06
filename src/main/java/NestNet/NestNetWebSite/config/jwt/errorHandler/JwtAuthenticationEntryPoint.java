package NestNet.NestNetWebSite.config.jwt.errorHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    //인증되지 않은 유저가 자원에 접근할 때 동작. ex) 로그인하지 않은 사용자가 접속 시도

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {

        log.error("JwtAuthenticationEntryPoint.class / 인증정보 없는 유저의 접근");

//        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);        //401 error

        response.sendRedirect("/unauthorized");
    }
}
