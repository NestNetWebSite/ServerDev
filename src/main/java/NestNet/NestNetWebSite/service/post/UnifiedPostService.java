package NestNet.NestNetWebSite.service.post;

import NestNet.NestNetWebSite.domain.member.Member;
import NestNet.NestNetWebSite.domain.post.unified.UnifiedPost;
import NestNet.NestNetWebSite.domain.post.unified.UnifiedPostType;
import NestNet.NestNetWebSite.dto.request.UnifiedPostRequestDto;
import NestNet.NestNetWebSite.dto.response.UnifiedPostDto;
import NestNet.NestNetWebSite.repository.post.AttachedFileRepository;
import NestNet.NestNetWebSite.repository.post.UnifiedPostRepository;
import NestNet.NestNetWebSite.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UnifiedPostService {

    private final UnifiedPostRepository unifiedPostRepository;
    private final MemberRepository memberRepository;
    private final AttachedFileRepository attachedFileRepository;

    // Create
    @Transactional
    public void save(UnifiedPostRequestDto postDto) {

        Member findMember = memberRepository.findById(postDto.getMemberId());
        UnifiedPost post = postDto.toEntity(findMember);

        unifiedPostRepository.save(post);
    }

//    // Read ---> 족보 게시판 목록 조회
//    public List<ExamCollectionPostListDto> findListFromExamCollectionPost(int offset, int limit){
//
//        List<UnifiedPost> postList = new ArrayList<>();
//
//        postList = unifiedPostRepository.findAllUnifiedPost(offset, limit);
//
//        List<ExamCollectionPostListDto> postListDtos = new ArrayList<>();
//
//        for(ExamCollectionPost Post : postList){
//            postListDtos.add(new ExamCollectionPostListDto(Post.getId(), Post.getSubject(),
//                    Post.getProfesssor(), Post.getYear(), Post.getSemester(), Post.getExamType()));
//        }
//
//        return postListDtos;
//    }

//    // Read ---> 족보 게시판 단건 조회
//    public ExamCollectionPostDto findPostFromExamCollectionPost(Long postId){
//
//        ExamCollectionPost post = unifiedPostRepository.findExamCollectionPost(postId);
//
//        return new ExamCollectionPostDto(postId, post.getTitle(), post.getBodyContent(), post.getSubject(),
//                post.getProfesssor(), post.getYear(), post.getSemester(), post.getExamType(), post.getMember().getName());
//    }


    // Read ---> 통합 게시판 조회
    public List<UnifiedPostDto> findAllFromUnifiedPost(UnifiedPostType unifiedPostType, int offset, int limit) {

        List<UnifiedPost> postList = new ArrayList<>();

        if (unifiedPostType.equals("자유")) {
            postList = unifiedPostRepository.findUnifiedFreePost(offset, limit);
        } else if (unifiedPostType.equals("개발")) {
            postList = unifiedPostRepository.findUnifiedDevPost(offset, limit);
        } else if (unifiedPostType.equals("진로")) {
            postList = unifiedPostRepository.findUnifiedCareerPost(offset, limit);
        }

        List<UnifiedPostDto> PostDtos = new ArrayList<>();

        for (UnifiedPost Post : postList) {
            PostDtos.add(new UnifiedPostDto(Post.getMember().getId(), Post.getTitle(), Post.getBodyContent(),
                    Post.getPostCategory(), Post.getUnifiedPostType()));
        }

        return PostDtos;
    }
}
