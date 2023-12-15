package NestNet.NestNetWebSite.service.member;

import NestNet.NestNetWebSite.api.ApiResult;
import NestNet.NestNetWebSite.domain.member.Member;
import NestNet.NestNetWebSite.dto.request.MemberModifyInfoRequest;
import NestNet.NestNetWebSite.dto.response.member.TemporaryInfoDto;
import NestNet.NestNetWebSite.exception.CustomException;
import NestNet.NestNetWebSite.exception.ErrorCode;
import NestNet.NestNetWebSite.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    /*
    회원 정보 변경
     */
    @Transactional
    public ApiResult<?> modifyMemberInfo(MemberModifyInfoRequest dto, String loginId){

        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_LOGIN_ID_NOT_FOUND));

        member.modifyInfo(dto.getLoginId(), dto.getName(), dto.getStudentId(), dto.getGrade(), dto.getEmailAddress());

        return ApiResult.success("회원 정보가 수정되었습니다.");
    }

    /*
    아이디 찾기
     */
    public String findMemberId(String name, String email){

        Member member = memberRepository.findByNameAndEmail(name, email)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        return member.getLoginId();
    }

    /*
    임시 비밀번호 발급
     */
    public TemporaryInfoDto createTemporaryPassword(String loginId){

        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_LOGIN_ID_NOT_FOUND));

        String tempPassword = UUID.randomUUID().toString().replace("-", "");
        tempPassword = tempPassword.substring(0,15);

        return new TemporaryInfoDto(member.getEmailAddress(), tempPassword);
    }

    /*
    회원 비밀번호 인증
     */
    public ApiResult<?> checkMemberPassword(String loginId, String password){

        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_LOGIN_ID_NOT_FOUND));

//        if(!member.getLoginPassword().equals(passwordEncoder.encode(password))){
//            throw new CustomException(ErrorCode.ID_PASSWORD_NOT_MATCH);
//        }

        return ApiResult.success("인증에 성공했습니다.");
    }

    /*
    회원 비밀번호 변경
     */
    @Transactional
    public ApiResult<?> changeMemberPassword(String loginId, String password){

        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_LOGIN_ID_NOT_FOUND));

        member.changePassword(password, passwordEncoder);

        return ApiResult.success("비밀번호가 변경되었습니다.");
    }

    /*
    회원 탈퇴 -> 모든 정보 초기화 + 아이디, 비밀번호 랜덤 문자열(10자리)로 변경 + 이름 '알수없음'으로 변경
     */
    @Transactional
    public ApiResult<?> withDrawMember(String loginId){

        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_LOGIN_ID_NOT_FOUND));

        String memberName = member.getName();

        member.withdraw();

        return ApiResult.success(memberName + "(" + loginId + ") 님 탈퇴 처리 되었습니다. 감사합니다.");
    }

}
