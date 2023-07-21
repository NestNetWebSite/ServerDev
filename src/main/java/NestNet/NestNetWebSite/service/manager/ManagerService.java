package NestNet.NestNetWebSite.service.manager;

import NestNet.NestNetWebSite.api.ApiResult;
import NestNet.NestNetWebSite.domain.manager.MemberSignUpManagement;
import NestNet.NestNetWebSite.domain.member.Member;
import NestNet.NestNetWebSite.domain.member.MemberAuthority;
import NestNet.NestNetWebSite.dto.request.MemberSignUpManagementRequestDto;
import NestNet.NestNetWebSite.dto.response.MemberSignUpManagementDto;
import NestNet.NestNetWebSite.repository.MemberRepository;
import NestNet.NestNetWebSite.repository.MemberSignUpManagementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ManagerService {

    private final MemberRepository memberRepository;
    private final MemberSignUpManagementRepository memberSignUpManagementRepository;

    /*
    회원 가입 요청 조회
     */
    public List<MemberSignUpManagementDto> findAllRequests(){
        List<MemberSignUpManagementDto> resultList = new ArrayList<>();
        List<MemberSignUpManagement> list = memberSignUpManagementRepository.findAll();

        for(MemberSignUpManagement request : list){
            resultList.add(new MemberSignUpManagementDto(request.getMember().getLoginId(), request.getRequestMemberAuthority()));
        }

        return resultList;
    }

    /*
    회원 가입 요청 승인
     */
    @Transactional
    public ApiResult<?> approveSignUp(MemberSignUpManagementRequestDto dto){

        Member member = memberRepository.findByLoginId(dto.getLoginId());
        member.setMemberAuthority(dto.getMemberAuthority());         //권한 설정
        memberSignUpManagementRepository.findByMember(member).setComplete(true);

        System.out.println("여기여기여기여긱여기");

        memberRepository.save(member);

        return ApiResult.success(member.getLoginId() + " 님의 회원가입 요청 승인이 완료되었습니다.");
    }

    /*
    재학생 -> 졸업생으로 전환
    */
    @Transactional
    public void changeAuthorityGraduate(Long id){

        Member member = memberRepository.findById(id);
        member.changeMemberToGraduate();
    }
}
