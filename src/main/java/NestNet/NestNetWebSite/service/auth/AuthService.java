package NestNet.NestNetWebSite.service.auth;

import NestNet.NestNetWebSite.api.ApiResult;
import NestNet.NestNetWebSite.config.jwt.TokenProvider;
import NestNet.NestNetWebSite.config.redis.RedisUtil;
import NestNet.NestNetWebSite.domain.manager.MemberSignUpManagement;
import NestNet.NestNetWebSite.domain.member.Member;
import NestNet.NestNetWebSite.domain.member.MemberAuthority;
import NestNet.NestNetWebSite.domain.token.RefreshToken;
import NestNet.NestNetWebSite.dto.request.LoginRequest;
import NestNet.NestNetWebSite.dto.request.SignUpRequest;
import NestNet.NestNetWebSite.dto.response.TokenResponse;
import NestNet.NestNetWebSite.exception.CustomException;
import NestNet.NestNetWebSite.repository.manager.MemberSignUpManagementRepository;
import NestNet.NestNetWebSite.repository.member.MemberRepository;
import NestNet.NestNetWebSite.repository.token.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final MemberRepository memberRepository;
    private final MemberSignUpManagementRepository memberSignUpManagementRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RedisUtil redisUtil;

    /*
    관리자에게 회원가입 요청을 보냄
     */
    @Transactional
    public ApiResult<?> sendSignUpRequest(SignUpRequest signUpRequestDto, HttpServletResponse response){

        // 아이디 중복 확인
        if(memberRepository.findByLoginId(signUpRequestDto.getLoginId()) != null){
            return ApiResult.error(response, HttpStatus.CONFLICT, "이미 가입된 유저 아이디입니다.");
        }

        MemberAuthority enrollAuthority = signUpRequestDto.getMemberAuthority();        //사용자가 신청한 권한

        signUpRequestDto.setMemberAuthority(MemberAuthority.WAITING_FOR_APPROVAL);                          //권한을 승인대기중으로 변경

        Member joinMember = signUpRequestDto.toEntity();
        joinMember.encodePassword(passwordEncoder);         //비밀번호 인코딩해서 저장

        MemberSignUpManagement signUpRequest = new MemberSignUpManagement(joinMember, enrollAuthority);

        memberRepository.save(joinMember);
        memberSignUpManagementRepository.save(signUpRequest);

        log.info("MemberService.class / sendSignUpRequest : 회원가입 신청");

        return ApiResult.success("회원가입 신청이 성공적으로 처리되었습니다. 관리자에게 문의하세요");
    }

    /*
    로그인
     */
    @Transactional
    public TokenResponse login(LoginRequest loginRequest){

        try{
            //인증 전의 UsernamePasswordAuthenticationToken 객체(Authentication의 구현체)
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginRequest.getLoginId(), loginRequest.getPassword());

            //인증 후의 Authentication 객체
            //authenticationManager가 UserDetailsService의 loadByUsername매서드를 호출하여 인증 수행
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

            TokenResponse tokenResponse = new TokenResponse(tokenProvider.createAccessToken(authentication), tokenProvider.createRefreshToken(authentication));

            return tokenResponse;
        } catch (Exception e){
            return null;
//            throw new CustomException("로그인 아이디 / 비밀번호 불일치", HttpStatus.BAD_REQUEST);
        }
    }

    /*
    로그아웃
     */
    @Transactional
    public ApiResult<?> logout(HttpServletRequest request, HttpServletResponse response){

        String accessToken = tokenProvider.resolveToken(request);

        Claims claims = tokenProvider.getTokenClaims(accessToken);

        long expTime = claims.getExpiration().getTime();     //토큰의 만료 시각
        long now = new Date().getTime();                     //현재 시각

        // 남은 엑세스토큰 유효 시간
        long remainTime = expTime - now;        // 테스트 사이트 : https://currentmillis.com/

        //기간이 만료된 엑세스 토큰인 경우는 필터에서 걸러지기 때문에 없음.

        //리프레시 토큰 삭제
        RefreshToken findRefreshToken = refreshTokenRepository.findByAccessToken(accessToken);
        int rows = refreshTokenRepository.delete(findRefreshToken.getId());

        //레디스에 블랙리스트 등록
        redisUtil.setData(accessToken, "logout", remainTime);

        //블랙리스트(key : 엑세스 토큰 / value : logout)
        if(redisUtil.hasKey(accessToken) && rows == 1){
            return ApiResult.success("로그아웃 되었습니다");
        }
        return ApiResult.error(response, HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러/ 관리자에게 문의하세요");
    }
}
