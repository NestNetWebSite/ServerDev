package NestNet.NestNetWebSite.service.member;

import NestNet.NestNetWebSite.api.ApiResult;
import NestNet.NestNetWebSite.domain.member.Member;
import NestNet.NestNetWebSite.dto.request.MemberModifyInfoRequest;
import NestNet.NestNetWebSite.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

        Member member = memberRepository.findByLoginId(loginId);
        member.modifyInfo(dto.getLoginId(), dto.getName(), dto.getStudentId(), dto.getGrade(), dto.getEmailAddress());

        return ApiResult.success("회원 정보가 수정되었습니다.");
    }

    /*
    아이디 찾기
     */
    public String findMemberId(String name, String email){

        Optional<Member> member = memberRepository.findByNameAndEmail(name, email);

        if(member != null){
            return member.get().getLoginId();
        }

        return null;
    }

    /*
    임시 비밀번호 발급
     */
    public Map<String, String> createTemporaryPassword(String logindId){

        Map<String, String> result = new HashMap<>();

//        Optional<Member> member = memberRepository.findByLoginId(logindId);       //나중에 이렇게 고치기
        Member member = memberRepository.findByLoginId(logindId);
        if(member == null){
            return null;
        }

        String tempPassword = UUID.randomUUID().toString().replace("-", "");
        tempPassword = tempPassword.substring(0,15);

        result.put("email", member.getEmailAddress());
        result.put("tempPassword", tempPassword);

        return result;
    }

    /*
    회원 비밀번호 변경
     */
    @Transactional
    public ApiResult<?> changeMemberPassword(String password, String loginId){

        Member member = memberRepository.findByLoginId(loginId);
        member.changePassword(password, passwordEncoder);

        return ApiResult.success("비밀번호가 변경되었습니다.");
    }

    /*
    회원 탈퇴 -> 모든 정보 초기화 + 아이디, 비밀번호 랜덤 문자열(10자리)로 변경 + 이름 '알수없음'으로 변경
     */
    @Transactional
    public ApiResult<?> withDrawMember(String loginId){

        Member member = memberRepository.findByLoginId(loginId);
        String memberName = member.getName();

        member.withdraw();

        return ApiResult.success(memberName + "(" + loginId + ") 님 탈퇴 처리 되었습니다. 감사합니다.");
    }

}
