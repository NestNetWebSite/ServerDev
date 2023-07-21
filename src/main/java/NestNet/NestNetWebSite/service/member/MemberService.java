package NestNet.NestNetWebSite.service.member;

import NestNet.NestNetWebSite.api.ApiResult;
import NestNet.NestNetWebSite.config.jwt.TokenProvider;
import NestNet.NestNetWebSite.domain.manager.MemberSignUpManagement;
import NestNet.NestNetWebSite.domain.member.Member;
import NestNet.NestNetWebSite.domain.member.MemberAuthority;
import NestNet.NestNetWebSite.dto.request.LoginRequestDto;
import NestNet.NestNetWebSite.dto.request.SignUpRequestDto;
import NestNet.NestNetWebSite.dto.response.JwtAccessTokenDto;
import NestNet.NestNetWebSite.dto.response.TokenDto;
import NestNet.NestNetWebSite.exception.CustomException;
import NestNet.NestNetWebSite.exception.DuplicateMemberException;
import NestNet.NestNetWebSite.repository.MemberRepository;
import NestNet.NestNetWebSite.repository.MemberSignUpManagementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberSignUpManagementRepository memberSignUpManagementRepository;
    private final PasswordEncoder passwordEncoder;
//    private final AuthenticationManager authenticationManager;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;


    /*
    관리자에게 회원가입 요청을 보냄
     */
    @Transactional
    public ApiResult<?> sendSignUpRequest(SignUpRequestDto signUpRequestDto){

        // 아이디 중복 확인
        if(memberRepository.findByLoginId(signUpRequestDto.getLoginId()) != null){
            return ApiResult.error(409, "이미 가입된 유저 아이디입니다.");
//            throw new DuplicateMemberException("이미 가입된 유저입니다.");
        }

        MemberAuthority enrollAuthority = signUpRequestDto.getMemberAuthority();        //사용자가 신청한 권한 (PRESIDENT, VICE_PRESIDENT, MANAGER, GENERAL_MEMBER, GRADUATED_MEMBER)

        signUpRequestDto.setMemberAuthority(MemberAuthority.WAITING_FOR_APPROVAL);                          //권한을 승인대기중으로 변경

        Member joinMember = signUpRequestDto.toEntity();
        joinMember.encodePassword(passwordEncoder);         //비밀번호 인코딩해서 저장

        MemberSignUpManagement signUpRequest = new MemberSignUpManagement(joinMember, enrollAuthority);

        memberRepository.save(joinMember);
        memberSignUpManagementRepository.save(signUpRequest);

        log.info("MemberService.class / sendSignUpRequest");

        return ApiResult.success("회원가입 신청이 성공적으로 처리되었습니다.");
    }

    /*
    로그인
     */
    public TokenDto login(LoginRequestDto loginRequestDto){

        try{
            //인증되기 전의 Authentication 객체
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginRequestDto.getLoginId(), loginRequestDto.getPassword());

            //인증 후의 객체
            //authenticationManager가 UserDetailsService의 loadByUsername매서드를 호출한 후 인증 수행
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

            log.info("MemberService / login 매서드 : 인증 후의 Authentication 객체 " + authentication);

            TokenDto tokenDto = new TokenDto(tokenProvider.createAccessToken(authentication), tokenProvider.createRefreshToken(authentication));

            return tokenDto;
        } catch (CustomException e){
            throw new CustomException("로그인 아이디 / 비밀번호 불일치", HttpStatus.BAD_REQUEST);
        }


    }

    /*
    로그아웃
     */
    @Transactional
    public void logout(JwtAccessTokenDto accessTokenDto){

        if(tokenProvider.validateToken(accessTokenDto.getToken())){
            throw new IllegalArgumentException("로그아웃 : 유효하지 않은 토큰입니다. ");
        }

        // 토큰에서 Authentication 객체 뽑아냄
        Authentication authentication = tokenProvider.getAuthentication(accessTokenDto.getToken());

    }

}
