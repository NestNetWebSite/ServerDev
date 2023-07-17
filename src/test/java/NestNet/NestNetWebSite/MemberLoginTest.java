package NestNet.NestNetWebSite;

import NestNet.NestNetWebSite.controller.auth.AuthController;
import NestNet.NestNetWebSite.dto.request.LoginRequestDto;
import NestNet.NestNetWebSite.dto.response.JwtAccessTokenDto;
import NestNet.NestNetWebSite.repository.MemberRepository;
import NestNet.NestNetWebSite.service.member.MemberService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class MemberLoginTest {

    @Autowired
    MemberService memberService;

    @Test
    public void 로그인() throws Exception{

        //given
        LoginRequestDto dto = new LoginRequestDto("kksshh0612", "kksshh1735");

        //when
        System.out.println(memberService.login(dto).getToken());

        //then
    }
}
