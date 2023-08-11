package NestNet.NestNetWebSite.controller.auth;

import NestNet.NestNetWebSite.api.ApiResult;
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
//@CrossOrigin(origins = {"http://localhost:8080", "http://localhost:3000", "http://172.30.1.33:8080", "http://172.30.1.33:3000" })
public class AuthController {

    private final MemberService memberService;
    private final RefreshTokenService refreshTokenService;

    @Value("#{environment['jwt.refresh-exp-time']}")
    private long refreshTokenExpTime;              //리프레쉬 토큰 유효기간

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


        System.out.println("혹시 이게...?" + refreshTokenExpTime);
        // 현재 시간 + 만료 기간 == 만료 시간
        LocalDateTime expTime = Instant.now().plusMillis((long)refreshTokenExpTime).atZone(ZoneId.systemDefault()).toLocalDateTime();

        System.out.println("만료 시간 : " +expTime);

        // 리프레시 토큰 저장
        refreshTokenService.save(new RefreshtokenRequestDto(tokenDto.getAccessToken(), tokenDto.getRefreshToken(), expTime));

        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.add(CustomAuthorizationFilter.AUTHORIZATION_HEADER, "Bearer " + tokenDto.getAccessToken());

        //리프레시 토큰 담은 쿠키 생성
        Cookie cookie = new Cookie("refresh-token", tokenDto.getRefreshToken());
        cookie.setHttpOnly(true);       //클라이언트에서 쿠키에 접근하지 못함
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge((int)(refreshTokenExpTime / 1000));       //초단위. 음수가 되면 브라우저가 종료될 때 쿠키 자동 삭제

        httpHeaders.add("Set-Cookie", cookie.toString());

        return new ResponseEntity<>(httpHeaders, HttpStatus.OK);
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
