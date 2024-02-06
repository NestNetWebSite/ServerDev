package NestNet.NestNetWebSite.controller.auth;

import NestNet.NestNetWebSite.api.ApiResult;
import NestNet.NestNetWebSite.config.cookie.CookieManager;
import NestNet.NestNetWebSite.dto.request.EmailAuthAnswerRequest;
import NestNet.NestNetWebSite.dto.request.EmailAuthRequest;
import NestNet.NestNetWebSite.dto.request.LoginRequest;
import NestNet.NestNetWebSite.dto.request.SignUpRequest;
import NestNet.NestNetWebSite.dto.response.TokenDto;
import NestNet.NestNetWebSite.exception.CustomException;
import NestNet.NestNetWebSite.exception.ErrorCode;
import NestNet.NestNetWebSite.service.auth.AuthService;
import NestNet.NestNetWebSite.service.mail.MailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "인증 API")
public class AuthController {

    private final AuthService authService;
    private final MailService mailService;

    /*
    회원 가입 시 인증 이메일 전송 Post 요청
     */
    @PostMapping("/auth/mail-auth")
    @Operation(summary = "인증 이메일 전송 요청", description = "이메일 전송에 실패했을 경우 500 에러를 반환한다.")
    public ApiResult<?> sendAuthEmail(@Valid @RequestBody EmailAuthRequest emailAuthRequest){

        return mailService.sendEmailAuthentication(emailAuthRequest.getEmail());
    }

    /*
    회원 가입 시 인증 이메일의 문제 정답 여부를 Post 요청
     */
    @PostMapping("/auth/mail-auth-answer")
    @Operation(summary = "인증 이메일 문제의 정답 여부 확인 요청", description = "")
    public ApiResult<?> sendAuthEmail(@Valid @RequestBody EmailAuthAnswerRequest emailAuthAnswerRequest){

        return authService.checkEmailAuth(emailAuthAnswerRequest.getAnswer());
    }

    /*
    회원가입 post 요청
     */
    @PostMapping("/auth/signup")
    @Operation(summary = "회원가입", description = "아이디 중복 요청의 경우 409 에러를 반환한다.")
    public ApiResult<?> signUp(@Valid @RequestBody SignUpRequest signUpRequest){

        return authService.sendSignUpRequest(signUpRequest);
    }

    /*
    로그인 post 요청
     */
    @PostMapping("/auth/login")
    @Operation(summary = "로그인", description = "Http 응답 헤더에 " +
            "(Authorization : 엑세스 토큰 / refresh-token : 리프레시 토큰 / refresh-token-exp-time : 리프레시 토큰 만료시간) 삽입")
    public ApiResult<?> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response){

        return authService.login(loginRequest, response);
    }

    /*
    로그아웃
     */
    @GetMapping("/auth/logout")
    @Operation(summary = "로그아웃", description = "로그아웃이 정상적으로 작동하지 않은 경우 500 에러를 반환한다.")
    public ApiResult<?> logout(HttpServletRequest request, HttpServletResponse response){

        return authService.logout(request, response);
    }

    @RequestMapping("/forbidden")
    public String forbidden(){
        throw new CustomException(ErrorCode.MEMBER_NO_PERMISSION);
    }

    @RequestMapping("/unauthorized")
    public String unauthorized(){
        throw new CustomException(ErrorCode.UNAUTHORIZED);
    }

}
