package NestNet.NestNetWebSite.repository;

import NestNet.NestNetWebSite.domain.member.Member;
import NestNet.NestNetWebSite.domain.member.MemberAuthority;
import NestNet.NestNetWebSite.domain.post.exam.ExamCollectionPost;
import NestNet.NestNetWebSite.domain.post.exam.ExamType;
import NestNet.NestNetWebSite.repository.member.MemberRepository;
import NestNet.NestNetWebSite.repository.post.ExamCollectionPostRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
public class ExamCollectionPostRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    ExamCollectionPostRepository examCollectionPostRepository;

    @Test
    public void 필터에_따른_조회(){
        //given
        Member member = createMember();
        ExamCollectionPost examCollectionPost = createPost(member);

        //when
        memberRepository.save(member);
        examCollectionPostRepository.save(examCollectionPost);
        List<ExamCollectionPost> findPost = examCollectionPostRepository.findAllExamCollectionPostByFilter(examCollectionPost.getSubject(), null, null, null, null);

        //then
        Assertions.assertEquals(examCollectionPost, findPost.get(0));
        Assertions.assertEquals(examCollectionPost.getSubject(), findPost.get(0).getSubject());
        Assertions.assertEquals(member, findPost.get(0).getMember());
    }

    @Test
    public void 모두_조회_테스트(){
        //given
        Member member = createMember();
        ExamCollectionPost examCollectionPost = createPost(member);

        //when
        memberRepository.save(member);
        examCollectionPostRepository.save(examCollectionPost);
        List<ExamCollectionPost> resultList = examCollectionPostRepository.findAllExamCollectionPost();

        //then
        Assertions.assertEquals(resultList.get(0), examCollectionPost);
        Assertions.assertEquals(member, resultList.get(0).getMember());
    }

    private Member createMember(){

        return Member.builder()
                .loginId("test")
                .name("테스트")
                .memberAuthority(MemberAuthority.GENERAL_MEMBER)
                .build();
    }

    private ExamCollectionPost createPost(Member member){

        return ExamCollectionPost.builder()
                .title("2022년 1학기 운영체제 기말고사 조희승")
                .bodyContent("testtesttest")
                .member(member)
                .subject("운영체제")
                .examType(ExamType.FINAL)
                .build();
    }
}
