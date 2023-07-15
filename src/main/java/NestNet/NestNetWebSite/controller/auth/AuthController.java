package NestNet.NestNetWebSite.controller.auth;

import NestNet.NestNetWebSite.api.ApiResult;
import NestNet.NestNetWebSite.config.jwt.CustomJwtFilter;
import NestNet.NestNetWebSite.dto.request.LoginRequestDto;
import NestNet.NestNetWebSite.dto.request.SignUpRequestDto;
import NestNet.NestNetWebSite.dto.response.JwtAccessTokenDto;
import NestNet.NestNetWebSite.service.member.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final MemberService memberService;

    /*
    회원가입 post 요청
     */
    @PostMapping("/auth/signup")
    public ApiResult<?> signUp(@Valid @RequestBody SignUpRequestDto signUpRequestDto){
        System.out.println("Sdfsdfsfsfdfdfdfffffffffff");
        return memberService.sendSignUpRequest(signUpRequestDto);
    }

    /*
    로그인 post 요청
     */
    @PostMapping("/auth/login")
    public ResponseEntity<JwtAccessTokenDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto){

        //loginRequestDto.getPassword() 가 null로 찍히는 문제 발생.
        System.out.println("로그인 컨트롤러 : " + loginRequestDto.getLoginId() + " " + loginRequestDto.getPassword());

        JwtAccessTokenDto jwt = memberService.login(loginRequestDto);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(CustomJwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        return new ResponseEntity<>(jwt, httpHeaders, HttpStatus.OK);
    }

}
