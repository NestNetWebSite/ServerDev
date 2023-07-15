package NestNet.NestNetWebSite.config.jwt.errorHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    //권한에 맞지 않는 유저가 자원에 접근할 때 동작 ex) 관리자 페이지 접속 시도

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
            throws IOException, ServletException {

        response.sendError(HttpServletResponse.SC_FORBIDDEN);           //403 error
    }
}
