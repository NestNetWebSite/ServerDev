package NestNet.NestNetWebSite.service.member;

import NestNet.NestNetWebSite.api.ApiResult;
import NestNet.NestNetWebSite.config.jwt.TokenProvider;
import NestNet.NestNetWebSite.domain.manager.MemberSignUpManagement;
import NestNet.NestNetWebSite.domain.member.Member;
import NestNet.NestNetWebSite.domain.member.MemberAuthority;
import NestNet.NestNetWebSite.dto.request.LoginRequestDto;
import NestNet.NestNetWebSite.dto.request.SignUpRequestDto;
import NestNet.NestNetWebSite.dto.response.JwtAccessTokenDto;
import NestNet.NestNetWebSite.exception.DuplicateMemberException;
import NestNet.NestNetWebSite.repository.MemberRepository;
import NestNet.NestNetWebSite.repository.MemberSignUpManagementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberSignUpManagementRepository memberSignUpManagementRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;


    /*
    관리자에게 회원가입 요청을 보냄
     */
    @Transactional
    public ApiResult<?> sendSignUpRequest(SignUpRequestDto signUpRequestDto){

        if(!memberRepository.findByLoginId(signUpRequestDto.getLoginId()).isEmpty()){
            throw new DuplicateMemberException("이미 가입된 유저입니다.");
        }

        MemberAuthority enrollAuthority = signUpRequestDto.getMemberAuthority();        //사용자가 신청한 권한 (PRESIDENT, VICE_PRESIDENT, MANAGER, GENERAL_MEMBER, GRADUATED_MEMBER)

        System.out.println(enrollAuthority);

        signUpRequestDto.setMemberAuthority(MemberAuthority.WAITING_FOR_APPROVAL);                          //권한을 승인대기중으로 변경
        signUpRequestDto.setLoginPassword(passwordEncoder.encode(signUpRequestDto.getLoginPassword()));     //비밀번호 인코딩해서 저장

        Member joinMember = signUpRequestDto.toEntity();
        MemberSignUpManagement signUpRequest = new MemberSignUpManagement(joinMember, enrollAuthority);

        System.out.println("MemberService 저장 전");

        memberRepository.save(joinMember);
        memberSignUpManagementRepository.save(signUpRequest);

        System.out.println("MemberService 저장 후 ");

        return ApiResult.success("회원가입 신청이 성공적으로 처리되었습니다.");
    }

    /*
    로그인
     */
    public JwtAccessTokenDto login(LoginRequestDto loginRequestDto){
        Member member = memberRepository.findByLoginId(loginRequestDto.getLoginId()).get(0);

//        System.out.println(member.getLoginId());

        //인증되기 전의 Authentication 객체
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequestDto.getLoginId(), loginRequestDto.getPassword());

        System.out.println("인증되기 전 : " + authenticationToken);

        //인증
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        System.out.println("인증 후 시큐리티 저장 전 : " + authentication);

        //스프링 시큐리티 컨텍스트에 인증 정보 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);

        System.out.println("저장 후");

        String jwt = tokenProvider.createJwtToken(authentication);

        System.out.println("jwt 토큰 : " + jwt);

        return new JwtAccessTokenDto(jwt);
    }
}
