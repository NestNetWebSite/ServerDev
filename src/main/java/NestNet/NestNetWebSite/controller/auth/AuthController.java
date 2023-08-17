package NestNet.NestNetWebSite.controller.auth;

import NestNet.NestNetWebSite.api.ApiResult;
import NestNet.NestNetWebSite.api.LoginApiResult;
import NestNet.NestNetWebSite.config.auth.CustomAuthorizationFilter;
import NestNet.NestNetWebSite.config.jwt.TokenProvider;
import NestNet.NestNetWebSite.dto.request.LoginRequestDto;
import NestNet.NestNetWebSite.dto.request.RefreshtokenRequestDto;
import NestNet.NestNetWebSite.dto.request.SignUpRequestDto;
import NestNet.NestNetWebSite.dto.response.JwtAccessTokenDto;
import NestNet.NestNetWebSite.dto.response.TokenDto;
import NestNet.NestNetWebSite.service.member.MemberService;
import NestNet.NestNetWebSite.service.token.RefreshTokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.swagger.v3.oas.annotations.Operation;
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

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@RestController
@RequiredArgsConstructor
@Slf4j
//@RequestMapping("/auth")
public class AuthController {

    private final MemberService memberService;
    private final RefreshTokenService refreshTokenService;

    @Value("#{environment['jwt.refresh-exp-time']}")
    private long refreshTokenExpTime;              //리프레쉬 토큰 유효기간

    /*
    회원가입 post 요청
     */
    @Operation(summary = "회원가입 요청", description = "파라미터로 회원가입 폼 받음")
    @PostMapping("/auth/signup")
    public ApiResult<?> signUp(@Valid @RequestBody SignUpRequestDto signUpRequestDto){

        log.info("회원가입 컨트롤러 동작");

        return memberService.sendSignUpRequest(signUpRequestDto);
    }

    /*
    로그인 post 요청
     */
    @Operation(summary = "로그인 요청", description = "access 토큰은 헤더에 Authorization에, refresh 토큰은 헤더에 쿠키로 반환")
    @PostMapping("/auth/login")
    public ApiResult<?> login(@Valid @RequestBody LoginRequestDto loginRequestDto){

        log.info("로그인 컨트롤러 : " + loginRequestDto.getLoginId() + " " + loginRequestDto.getPassword());

        TokenDto tokenDto = memberService.login(loginRequestDto);

        // 현재 시간 + 만료 기간 == 만료 시간
        LocalDateTime expTime = Instant.now().plusMillis((long)refreshTokenExpTime).atZone(ZoneId.systemDefault()).toLocalDateTime();

        // 리프레시 토큰 저장
        refreshTokenService.save(new RefreshtokenRequestDto(tokenDto.getAccessToken(), tokenDto.getRefreshToken(), expTime));

        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.set(CustomAuthorizationFilter.AUTHORIZATION_HEADER, "Bearer " + tokenDto.getAccessToken());
        httpHeaders.set("refresh-token", tokenDto.getRefreshToken());
        httpHeaders.set("refresh-token-exp-time", Integer.toString((int)refreshTokenExpTime / 1000));

        System.out.println(" 로그인 완료 : " + tokenDto.getRefreshToken());

        return ApiResult.success(httpHeaders);
    }

    /*
    로그아웃
     */
    @DeleteMapping("/auth/logout")
    public ResponseEntity<?> logout(@Valid @RequestBody JwtAccessTokenDto accessTokenDto){

        log.info("로그아웃 컨트롤러 : JWT access 토큰 : " + accessTokenDto.getToken());

        boolean isLogoutComplete = memberService.logout(accessTokenDto);

        return ResponseEntity.ok(HttpStatus.OK);
    }



}
