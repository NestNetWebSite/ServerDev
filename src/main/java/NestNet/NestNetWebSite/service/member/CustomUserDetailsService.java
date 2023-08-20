package NestNet.NestNetWebSite.service.member;

import NestNet.NestNetWebSite.domain.member.Member;
import NestNet.NestNetWebSite.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    /*
    사용자 아이디를 통해 UserDetails 객체를 반환
     */
    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {

        Member member = memberRepository.findByLoginId(loginId);

        log.info("CustomUserDetailsService.class / loadUserByUsername 매서드 : " + member.getLoginId() + " 유저 찾음");

        if (member == null) {
            throw new UsernameNotFoundException(loginId + " : 해당 유저를 데이터베이스에서 찾을 수 없습니다.");
        } else {
            return createUser(member);
        }
    }

    /*
    로그인 아이디와 member 객체를 이용해 UserDetails 인터페이스의 구현체인 User 객체를 생성하여 반환한다.
     */
    private User createUser(Member member){

        List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();

        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(member.getMemberAuthority().toString());

        grantedAuthorityList.add(simpleGrantedAuthority);

        return new User(member.getLoginId(), member.getLoginPassword(), grantedAuthorityList);
    }
}
