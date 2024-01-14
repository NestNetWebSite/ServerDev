package NestNet.NestNetWebSite.service.manager;

import NestNet.NestNetWebSite.api.ApiResult;
import NestNet.NestNetWebSite.domain.manager.MemberSignUpManagement;
import NestNet.NestNetWebSite.domain.member.Member;
import NestNet.NestNetWebSite.domain.member.MemberAuthority;
import NestNet.NestNetWebSite.dto.request.MemberSignUpManagementRequest;
import NestNet.NestNetWebSite.dto.response.manager.MemberInfoDto;
import NestNet.NestNetWebSite.dto.response.manager.MemberInfoResponse;
import NestNet.NestNetWebSite.dto.response.manager.MemberSignUpManagementDto;
import NestNet.NestNetWebSite.dto.response.manager.MemberSignUpManagementResponse;
import NestNet.NestNetWebSite.exception.CustomException;
import NestNet.NestNetWebSite.exception.ErrorCode;
import NestNet.NestNetWebSite.repository.member.MemberRepository;
import NestNet.NestNetWebSite.repository.manager.MemberSignUpManagementRepository;
import lombok.RequiredArgsConstructor;
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
    public ApiResult<?> approveSignUp(MemberSignUpManagementRequest dto){

        Member member = memberRepository.findByLoginId(dto.getLoginId())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_LOGIN_ID_NOT_FOUND));

        member.changeAuthority(dto.getMemberAuthority());         //권한 설정

        MemberSignUpManagement memberSignUpManagement = memberSignUpManagementRepository.findByMember(member)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_SIGNUP_REQUEST_NOT_FOUND));

        memberSignUpManagementRepository.delete(memberSignUpManagement);

        memberRepository.save(member);

        return ApiResult.success(member.getLoginId() + " 님의 회원가입 요청 승인이 완료되었습니다.");
    }

    /*
    회원 가입 요청 미승인
     */
    @Transactional
    public ApiResult<?> rejectSignUp(MemberSignUpManagementRequest dto){

        Member member = memberRepository.findByLoginId(dto.getLoginId())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_LOGIN_ID_NOT_FOUND));

        memberRepository.delete(member);

        MemberSignUpManagement memberSignUpManagement = memberSignUpManagementRepository.findByMember(member)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_SIGNUP_REQUEST_NOT_FOUND));

        memberSignUpManagementRepository.delete(memberSignUpManagement);

        return ApiResult.success(member.getLoginId() + " 님의 회원가입 요청 거절이 완료되었습니다.");
    }

    /*
    재학생 -> 졸업생으로 전환
    */
    @Transactional
    public ApiResult<?> changeAuthorityGraduate(Long id){

        Member member = memberRepository.findById(id)
                        .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        member.changeMemberToGraduate();

        return ApiResult.success(member.getName() + "님의 권한이 졸업생으로 변경되었습니다.");
    }

    /*
    권한 변경
     */
    @Transactional
    public ApiResult<?> changeAuthority(Long id, MemberAuthority authority){

        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        member.changeAuthority(authority);

        return ApiResult.success(member.getName() + "님의 권한이 " + member.getMemberAuthority().toString() + "(으)로 변경되었습니다.");
    }

    /*
    전체 회원 정보 조회 (권한에 따른 필터링)
     */
    public ApiResult<?> findAllMemberInfo(){

        // 승인 대기 제외
        List<Member> memberList = memberRepository.findAllByNameAndMemberAuthority(MemberAuthority.WAITING_FOR_APPROVAL);

        List<MemberInfoDto> memberInfoDtoList = new ArrayList<>();
        for(Member member : memberList){
            memberInfoDtoList.add(new MemberInfoDto(member.getId(), member.getMemberAuthority(), member.getName(), member.getLoginId(), member.getEmailAddress(),
                    member.getStudentId(), member.getGrade(), member.getGraduateYear()));
        }

        MemberInfoResponse result = new MemberInfoResponse(memberInfoDtoList);

        return ApiResult.success(result);
    }

    /*
    회원 탈퇴 -> 모든 정보 초기화 + 아이디, 비밀번호 랜덤 문자열(10자리)로 변경 + 이름 '알수없음'으로 변경
     */
    @Transactional
    public ApiResult<?> withDrawMember(Long id){

        System.out.println("여기");
        System.out.println(id);

        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        String memberName = member.getName();
        String memberLoginId = member.getLoginId();

        member.withdraw();

        return ApiResult.success(memberName + "(" + memberLoginId + ") 님 탈퇴 처리 되었습니다.");
    }
}
