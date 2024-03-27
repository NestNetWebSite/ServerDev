package NestNet.NestNetWebSite.member;

import NestNet.NestNetWebSite.domain.member.Member;
import NestNet.NestNetWebSite.domain.member.MemberAuthority;
import NestNet.NestNetWebSite.dto.request.MemberModifyInfoRequest;
import NestNet.NestNetWebSite.dto.response.MemberIdDto;
import NestNet.NestNetWebSite.dto.response.member.TemporaryInfoDto;
import NestNet.NestNetWebSite.repository.member.MemberRepository;
import NestNet.NestNetWebSite.service.member.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static reactor.core.publisher.Mono.when;

@ExtendWith(MockitoExtension.class)
@Transactional
public class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @DisplayName("회원의 정보를 변경할 수 있다")
    @Test
    void modifyMemberInfo(){

        // given
        final Member member = createMember();
        final MemberModifyInfoRequest dto = new MemberModifyInfoRequest("test2", "test2", "2018000001", 3, "test2");

//        BDDMockito.given(memberRepository.save(any(Member.class))).willReturn(member);
        BDDMockito.given(memberRepository.findByLoginId(any(String.class))).willReturn(Optional.of(member));

        // when
        MemberIdDto memberIdDto = memberService.modifyMemberInfo(dto, member.getLoginId());

        // then
        Assertions.assertThat(member.getLoginId()).isEqualTo(dto.getLoginId());
        Assertions.assertThat(member.getName()).isEqualTo(dto.getName());
        Assertions.assertThat(member.getStudentId()).isEqualTo(dto.getStudentId());
        Assertions.assertThat(member.getGrade()).isEqualTo(dto.getGrade());
        Assertions.assertThat(member.getEmailAddress()).isEqualTo(dto.getEmailAddress());
    }

    @DisplayName("사용자 이름과 이메일로 아이디를 찾을 수 있다")
    @Test
    void findMemberId(){

        // given
        final Member member = createMember();
        String name = member.getName();
        String email = member.getEmailAddress();

        BDDMockito.given(memberRepository.findByNameAndEmail(any(String.class), any(String.class))).willReturn(Optional.of(member));

        // when
        String loginId = memberService.findMemberId(name, email);

        // then
        Assertions.assertThat(member.getLoginId()).isEqualTo(loginId);
    }

//    @DisplayName("임시 비밀번호를 발급받고 비밀번호를 임시 비밀번호로 변경한다")
//    @Test
//    void createTemporaryPassword(){
//
//        // given
//        Member member = createMember();
//        String prevPassword = member.getLoginPassword();
//
//        BDDMockito.given(memberRepository.findByLoginId(any(String.class))).willReturn(Optional.of(member));
//
//        // when
//        TemporaryInfoDto temporaryInfoDto = memberService.createTemporaryPasswordAndChangePassword(member.getLoginId());
//
//        System.out.println(prevPassword);
//        System.out.println(temporaryInfoDto.getPassword());
//        System.out.println(member.getLoginPassword());
//        System.out.println(member.getLoginId());
//
//        // then
//        Assertions.assertThat(prevPassword).isNotEqualTo(member.getLoginPassword());
////        Assertions.assertThat(temporaryInfoDto.getPassword()).isEqualTo(member.getLoginPassword());
//    }

    Member createMember(){

        return Member.builder()
                .id(1L)
                .loginId("test")
                .loginPassword("test")
                .name("test")
                .graduated(false)
                .studentId("2018000000")
                .grade(4)
                .emailAddress("test")
                .memberAuthority(MemberAuthority.GENERAL_MEMBER)
                .joinDate(LocalDateTime.now())
                .build();
    }
}
