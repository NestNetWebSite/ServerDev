package NestNet.NestNetWebSite.repository;

import NestNet.NestNetWebSite.domain.attachedfile.AttachedFile;
import NestNet.NestNetWebSite.domain.member.Member;
import NestNet.NestNetWebSite.domain.member.MemberAuthority;
import NestNet.NestNetWebSite.domain.post.exam.ExamCollectionPost;
import NestNet.NestNetWebSite.domain.post.exam.ExamType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@SpringBootTest
@Transactional
public class AttachedFileRepositoryTest {

    @Autowired AttachedFileRepository attachedFileRepository;
    @Autowired ExamCollectionPostRepository examCollectionPostRepository;
    @Autowired MemberRepository memberRepository;

//    @Test
//    public void 첨부파일_저장_테스트(){
//        Member member = createMember();
//
//        ExamCollectionPost examCollectionPost = createPost(member);
//    }
//
//    private Member createMember(){
//
//        return Member.builder()
//                .loginId("test")
//                .name("테스트")
//                .memberAuthority(MemberAuthority.GENERAL_MEMBER)
//                .build();
//    }
//
//    private AttachedFile createAttachedFile(){
//
//    }

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
