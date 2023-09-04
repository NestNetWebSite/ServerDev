package NestNet.NestNetWebSite.controller.auth;

import NestNet.NestNetWebSite.api.ApiResult;
import NestNet.NestNetWebSite.config.auth.CustomAuthorizationFilter;
import NestNet.NestNetWebSite.dto.request.LoginRequest;
import NestNet.NestNetWebSite.dto.request.SignUpRequest;
import NestNet.NestNetWebSite.dto.response.TokenResponse;
import NestNet.NestNetWebSite.service.auth.AuthService;
import io.swagger.v3.oas.annotations.Operation;
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
//@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @Value("#{environment['jwt.refresh-exp-time']}")
    private long refreshTokenExpTime;                           // refresh 토큰 유효 기간

    /*
    회원가입 post 요청
     */
    @Operation(summary = "회원가입 요청", description = "파라미터로 회원가입 폼 받음")
    @PostMapping("/auth/signup")
    public ApiResult<?> signUp(@Valid @RequestBody SignUpRequest signUpRequest, HttpServletResponse response){

        ApiResult<?> apiResult = authService.sendSignUpRequest(signUpRequest, response);

        return apiResult;
    }

    /*
    로그인 post 요청
     */
    @Operation(summary = "로그인 요청", description = "access 토큰은 헤더에 Authorization에, refresh 토큰은 헤더에 쿠키로 반환")
    @PostMapping("/auth/login")
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
    public ApiResult<?> logout(HttpServletRequest request, HttpServletResponse response){

        return authService.logout(request, response);
    }

}
