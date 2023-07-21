package NestNet.NestNetWebSite.domain.member;

import NestNet.NestNetWebSite.repository.MemberRepository;
import NestNet.NestNetWebSite.repository.MemberSignUpManagementRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class memberServiceTest {

    @Autowired MemberRepository memberRepository;
    @Autowired MemberSignUpManagementRepository memberSignUpManagementRepository;



}
