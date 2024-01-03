package NestNet.NestNetWebSite.config.jwt.errorHandler;

import NestNet.NestNetWebSite.exception.CustomException;
import NestNet.NestNetWebSite.exception.ErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    //권한에 맞지 않는 유저가 자원에 접근할 때 동작 ex) 관리자 페이지 접속 시도

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
            throws IOException, ServletException {

        log.error("JwtAccessDeniedHandler.class / 권한 없는 유저의 접근");

//        response.sendError(HttpServletResponse.SC_FORBIDDEN);           //403 error

        response.sendRedirect("/forbidden");
    }
}
