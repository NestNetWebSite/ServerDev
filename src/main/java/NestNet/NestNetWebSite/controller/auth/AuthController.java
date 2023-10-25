package NestNet.NestNetWebSite.controller.auth;

import NestNet.NestNetWebSite.api.ApiResult;
import NestNet.NestNetWebSite.config.auth.CustomAuthorizationFilter;
import NestNet.NestNetWebSite.dto.request.LoginRequest;
import NestNet.NestNetWebSite.dto.request.SignUpRequest;
import NestNet.NestNetWebSite.dto.response.TokenResponse;
import NestNet.NestNetWebSite.service.auth.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "인증 API")
public class AuthController {

    private final AuthService authService;

    @Value("#{environment['jwt.refresh-exp-time']}")
    private long refreshTokenExpTime;                           // refresh 토큰 유효 기간

    /*
    회원가입 post 요청
     */
    @PostMapping("/auth/signup")
    @Operation(summary = "회원가입", description = "")
    public ApiResult<?> signUp(@Valid @RequestBody SignUpRequest signUpRequest, HttpServletResponse response){

        return authService.sendSignUpRequest(signUpRequest, response);
    }

    /*
    로그인 post 요청
     */
    @PostMapping("/auth/login")
    @Operation(summary = "로그인", description = "Http 응답 헤더에 " +
            "(Authorization : 엑세스 토큰 / refresh-token : 리프레시 토큰 / refresh-token-exp-time : 리프레시 토큰 만료시간) 삽입")
    public ApiResult<?> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response){

        TokenResponse tokenResponse = authService.login(loginRequest);

        if(tokenResponse == null){
            return ApiResult.error(response, HttpStatus.BAD_REQUEST, "아이디 / 비밀번호 불일치");
        }

        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.set(CustomAuthorizationFilter.AUTHORIZATION_HEADER, "Bearer " + tokenResponse.getAccessToken());
        httpHeaders.set("refresh-token", tokenResponse.getRefreshToken());
        httpHeaders.set("refresh-token-exp-time", Integer.toString((int)refreshTokenExpTime / 1000));

        return ApiResult.success(httpHeaders);
    }

    /*
    로그아웃
     */
    @GetMapping("/auth/logout")
    @Operation(summary = "로그아웃", description = "")
    public ApiResult<?> logout(HttpServletRequest request, HttpServletResponse response){

        return authService.logout(request, response);
    }

}
