package NestNet.NestNetWebSite.service;

import NestNet.NestNetWebSite.domain.post.*;
import NestNet.NestNetWebSite.domain.member.Member;
import NestNet.NestNetWebSite.dto.request.ExamCollectionPostRequestDto;
import NestNet.NestNetWebSite.dto.request.UnifiedPostRequestDto;
import NestNet.NestNetWebSite.dto.response.ExamCollectionPostDto;
import NestNet.NestNetWebSite.dto.response.ExamCollectionPostListDto;
import NestNet.NestNetWebSite.dto.response.UnifiedPostDto;
import NestNet.NestNetWebSite.repository.AttachedFileRepository;
import NestNet.NestNetWebSite.repository.PostRepository;
import NestNet.NestNetWebSite.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final AttachedFileRepository attachedFileRepository;

    // Create
    @Transactional
    public void save(ExamCollectionPostRequestDto PostDto) {

        Member findMember = memberRepository.findById(PostDto.getMemberId());
        Post post = PostDto.toEntity(findMember);

        postRepository.save(post);
    }

    // Create
    @Transactional
    public void save(UnifiedPostRequestDto postDto) {

        Member findMember = memberRepository.findById(postDto.getMemberId());
        Post Post = postDto.toEntity(findMember);

        postRepository.save(Post);
    }

    // Read ---> 족보 게시판 목록 조회
    public List<ExamCollectionPostListDto> findListFromExamCollectionPost(int offset, int limit){

        List<ExamCollectionPost> postList = new ArrayList<>();

        postList = postRepository.findAllExamCollectionPost(offset, limit);

        List<ExamCollectionPostListDto> postListDtos = new ArrayList<>();

        for(ExamCollectionPost Post : postList){
            postListDtos.add(new ExamCollectionPostListDto(Post.getId(), Post.getSubject(),
                    Post.getProfesssor(), Post.getYear(), Post.getSemester(), Post.getExamType()));
        }

        return postListDtos;
    }

    // Read ---> 족보 게시판 단건 조회
    public ExamCollectionPostDto findPostFromExamCollectionPost(Long postId){

        ExamCollectionPost post = postRepository.findExamCollectionPost(postId);

        return new ExamCollectionPostDto(postId, post.getTitle(), post.getBodyContent(), post.getSubject(),
                post.getProfesssor(), post.getYear(), post.getSemester(), post.getExamType(), post.getMember().getName());
    }


    // Read ---> 통합 게시판 조회
    public List<UnifiedPostDto> findAllFromUnifiedPost(PostType postType, int offset, int limit) {

        List<UnifiedPost> postList = new ArrayList<>();

        if (postType.equals("자유")) {
            postList = postRepository.findUnifiedFreePost(offset, limit);
        } else if (postType.equals("개발")) {
            postList = postRepository.findUnifiedDevPost(offset, limit);
        } else if (postType.equals("진로")) {
            postList = postRepository.findUnifiedCareerPost(offset, limit);
        }

        List<UnifiedPostDto> PostDtos = new ArrayList<>();

        for (UnifiedPost Post : postList) {
            PostDtos.add(new UnifiedPostDto(Post.getMember().getId(), Post.getTitle(), Post.getBodyContent(),
                    Post.getPostCategory(), Post.getPostType()));
        }

        return PostDtos;
    }
}