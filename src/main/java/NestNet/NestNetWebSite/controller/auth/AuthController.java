package NestNet.NestNetWebSite.controller.auth;

import NestNet.NestNetWebSite.api.ApiResult;
import NestNet.NestNetWebSite.config.auth.CustomAuthorizationFilter;
import NestNet.NestNetWebSite.dto.request.LoginRequestDto;
import NestNet.NestNetWebSite.dto.request.SignUpRequestDto;
import NestNet.NestNetWebSite.dto.response.JwtAccessTokenDto;
import NestNet.NestNetWebSite.dto.response.TokenDto;
import NestNet.NestNetWebSite.service.member.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final MemberService memberService;

    @Value("#{environment['jwt.refresh-exp-time']}")
    private static int refreshTokenExpTime;              //리프레쉬 토큰 유효기간

    /*
    회원가입 post 요청
     */
    @PostMapping("/auth/signup")
    public ApiResult<?> signUp(@Valid @RequestBody SignUpRequestDto signUpRequestDto){

        log.info("회원가입 컨트롤러 동작");

        return memberService.sendSignUpRequest(signUpRequestDto);
    }

    /*
    로그인 post 요청
     */
    @PostMapping("/auth/login")
    public ResponseEntity<TokenDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto){

        log.info("로그인 컨트롤러 : " + loginRequestDto.getLoginId() + " " + loginRequestDto.getPassword());

        TokenDto tokenDto = memberService.login(loginRequestDto);

        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.add(CustomAuthorizationFilter.AUTHORIZATION_HEADER, "Bearer " + tokenDto.getAccessToken());

        //리프레시 토큰 담은 쿠키 생성
        Cookie cookie = new Cookie("refresh_token", tokenDto.getRefreshToken());
        cookie.setHttpOnly(true);       //클라이언트에서 쿠키에 접근하지 못함
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(refreshTokenExpTime);

        httpHeaders.add("Set-Cookie", cookie.toString());

        return new ResponseEntity<>(httpHeaders, HttpStatus.OK);
    }

    @GetMapping("auth/president")
    public void test(){
        log.info("회장 권한 수행 ");      // 인가 성공 시 로그
    }

}
