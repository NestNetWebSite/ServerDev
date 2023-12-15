package NestNet.NestNetWebSite.service.auth;

import NestNet.NestNetWebSite.api.ApiResult;
import NestNet.NestNetWebSite.config.jwt.TokenProvider;
import NestNet.NestNetWebSite.config.redis.RedisUtil;
import NestNet.NestNetWebSite.domain.manager.MemberSignUpManagement;
import NestNet.NestNetWebSite.domain.member.Member;
import NestNet.NestNetWebSite.domain.member.MemberAuthority;
import NestNet.NestNetWebSite.dto.request.LoginRequest;
import NestNet.NestNetWebSite.dto.request.SignUpRequest;
import NestNet.NestNetWebSite.dto.response.TokenResponse;
import NestNet.NestNetWebSite.exception.CustomException;
import NestNet.NestNetWebSite.exception.ErrorCode;
import NestNet.NestNetWebSite.repository.manager.MemberSignUpManagementRepository;
import NestNet.NestNetWebSite.repository.member.MemberRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

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
    private final RedisUtil redisUtil;

    @Value("#{environment['jwt.refresh-exp-time']}")
    private long refreshTokenExpTime;              //리프레쉬 토큰 유효기간

    @Value("#{environment['mail-secret-string']}")
    private String mailSecretString;                //회원가입 시 이메일 인증에서 사용되는 문자열

    /*
    회원 가입 시 이메일 인증 정답 여부 확인
     */
    public ApiResult<?> checkEmailAuth(String answer){

        if(answer.equals(mailSecretString)) return ApiResult.success("이메일 인증이 완료되었습니다.");
        else return ApiResult.success("틀렸습니다. 하지만, 이메일을 정상적으로 받으셨다면 정답에 nestnet을 입력하세요.");
    }

    /*
    관리자에게 회원가입 요청을 보냄
     */
    @Transactional
    public ApiResult<?> sendSignUpRequest(SignUpRequest signUpRequestDto){

        Optional<Member> member = memberRepository.findByLoginId(signUpRequestDto.getLoginId());

        if(member.isPresent()){
            throw new CustomException(ErrorCode.ALREADY_EXIST);
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

        TokenResponse tokenResponse;

        try{
            //인증 전의 UsernamePasswordAuthenticationToken 객체(Authentication의 구현체)
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginRequest.getLoginId(), loginRequest.getPassword());

            //인증 후의 Authentication 객체
            //authenticationManager가 UserDetailsService의 loadByUsername매서드를 호출하여 인증 수행
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

//            Member member = memberRepository.findByLoginId(loginRequest.getLoginId())
//                    .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_LOGIN_ID_NOT_FOUND));
//
//            if(member.getMemberAuthority().equals(MemberAuthority.WAITING_FOR_APPROVAL)){
//                throw new CustomException(ErrorCode.MEMBER_NOT_PERMISSION_YET);
//            }

            String accessToken = tokenProvider.createAccessToken(authentication);
            String refreshToken = tokenProvider.createRefreshToken(authentication);

            //레디스에 리프레시 토큰 저장
            redisUtil.setData(refreshToken, "refresh-token", refreshTokenExpTime);

            tokenResponse = new TokenResponse(accessToken, refreshToken);

        } catch (Exception e){
            throw new CustomException(ErrorCode.ID_PASSWORD_NOT_MATCH);
        }

        return tokenResponse;
    }

    /*
    로그아웃
     */
    @Transactional
    public ApiResult<?> logout(HttpServletRequest request){

        String accessToken = tokenProvider.resolveToken(request);

        Claims claims = tokenProvider.getTokenClaims(accessToken);

        long expTime = claims.getExpiration().getTime();     //토큰의 만료 시각
        long now = new Date().getTime();                     //현재 시각

        // 남은 엑세스토큰 유효 시간
        long remainTime = expTime - now;        // 테스트 사이트 : https://currentmillis.com/

        //기간이 만료된 엑세스 토큰인 경우는 필터에서 걸러지기 때문에 없음.

        //리프레시 토큰 삭제
        String refreshToken = tokenProvider.getRefreshToken(request);
        if(refreshToken != null){
            System.out.println("삭제");
            redisUtil.deleteData(refreshToken);
        }

        //레디스에 블랙리스트 등록
        redisUtil.setData(accessToken, "logout", remainTime);

        return ApiResult.success("로그아웃 되었습니다");
    }
}
