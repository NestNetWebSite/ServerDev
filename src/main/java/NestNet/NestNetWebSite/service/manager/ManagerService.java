package NestNet.NestNetWebSite.service.manager;

import NestNet.NestNetWebSite.api.ApiResult;
import NestNet.NestNetWebSite.domain.manager.MemberSignUpManagement;
import NestNet.NestNetWebSite.domain.member.Member;
import NestNet.NestNetWebSite.domain.member.MemberAuthority;
import NestNet.NestNetWebSite.dto.request.MemberSignUpManagementRequest;
import NestNet.NestNetWebSite.dto.response.MemberInfoResponse;
import NestNet.NestNetWebSite.dto.response.manager.MemberSignUpManagementDto;
import NestNet.NestNetWebSite.dto.response.manager.MemberSignUpManagementResponse;
import NestNet.NestNetWebSite.repository.member.MemberRepository;
import NestNet.NestNetWebSite.repository.manager.MemberSignUpManagementRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ManagerService {

    private final MemberRepository memberRepository;
    private final MemberSignUpManagementRepository memberSignUpManagementRepository;

    /*
    회원 가입 요청 조회
     */
    public ApiResult<?> findAllRequests(){

        List<MemberSignUpManagement> list = memberSignUpManagementRepository.findAll();
        List<MemberSignUpManagementDto> dtoList = new ArrayList<>();

        for(MemberSignUpManagement request : list){
            dtoList.add(new MemberSignUpManagementDto(request.getMember().getName(), request.getMember().getLoginId(), request.getMember().getStudentId(),
                    request.getMember().getGrade(), request.getMember().getGraduateYear(), request.getRequestMemberAuthority()));
        }

        MemberSignUpManagementResponse memberSignUpManagementResponse = new MemberSignUpManagementResponse(dtoList);

        return ApiResult.success(memberSignUpManagementResponse);
    }

    /*
    회원 가입 요청 승인
     */
    @Transactional
    public ApiResult<?> approveSignUp(MemberSignUpManagementRequest dto, HttpServletResponse response){

        Member member = memberRepository.findByLoginId(dto.getLoginId());
        member.changeAuthority(dto.getMemberAuthority());         //권한 설정
        Optional<MemberSignUpManagement> memberSignUpManagement = memberSignUpManagementRepository.findByMember(member);

        if(memberSignUpManagement.isPresent()){
            memberSignUpManagement.get().setComplete(true);
        }
        else{
            return ApiResult.error(response, HttpStatus.INTERNAL_SERVER_ERROR, "회원가입 요청을 찾을 수 없습니다.");
        }

        memberRepository.save(member);

        return ApiResult.success(member.getLoginId() + " 님의 회원가입 요청 승인이 완료되었습니다.");
    }

    /*
    재학생 -> 졸업생으로 전환
    */
    @Transactional
    public ApiResult<?> changeAuthorityGraduate(Long id){

        Member member = memberRepository.findById(id);
        member.changeMemberToGraduate();

        return ApiResult.success(member.getName() + "님의 권한이 졸업생으로 변경되었습니다.");
    }

    /*
    권한 변경
     */
    @Transactional
    public ApiResult<?> changeAuthority(Long id, MemberAuthority authority){

        Member member = memberRepository.findById(id);
        member.changeAuthority(authority);

        return ApiResult.success(member.getName() + "님의 권한이 " + member.getMemberAuthority().toString() + "(으)로 변경되었습니다.");
    }

    /*
    전체 회원 정보 조회 (권한에 따른 필터링)
     */
    public ApiResult<?> findAllMemberInfo(String name, MemberAuthority memberAuthority){

        List<Member> memberList = memberRepository.findAllMember(name, memberAuthority);
        List<MemberInfoResponse> memberInfoResponseList = new ArrayList<>();
        for(Member member : memberList){
            memberInfoResponseList.add(new MemberInfoResponse(member.getMemberAuthority(), member.getName(), member.getLoginId(), member.getEmailAddress(),
                    member.getStudentId(), member.getGrade(), member.getGraduateYear()));
        }

        return ApiResult.success(memberInfoResponseList);
    }

    /*
    회원 탈퇴 -> 모든 정보 초기화 + 아이디, 비밀번호 랜덤 문자열(10자리)로 변경 + 이름 '알수없음'으로 변경
     */
    @Transactional
    public ApiResult<?> withDrawMember(Long id){

        Member member = memberRepository.findById(id);
        String memberName = member.getName();
        String memberLoginId = member.getLoginId();

        member.withdraw();

        return ApiResult.success(memberName + "(" + memberLoginId + ") 님 탈퇴 처리 되었습니다.");
    }
}
