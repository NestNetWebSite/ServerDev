package NestNet.NestNetWebSite.service.comment;

import NestNet.NestNetWebSite.domain.comment.Comment;
import NestNet.NestNetWebSite.domain.member.Member;
import NestNet.NestNetWebSite.domain.post.Post;
import NestNet.NestNetWebSite.dto.request.CommentRequest;
import NestNet.NestNetWebSite.dto.response.CommentResponse;
import NestNet.NestNetWebSite.repository.comment.CommentRepository;
import NestNet.NestNetWebSite.repository.member.MemberRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final EntityManager entityManager;

    // Post 자식 객체를 가져오기 위한 간단한 로직 수행
    public Post findPost(Long postId){
        return entityManager.find(Post.class, postId);
    }

    /*
    댓글 저장
     */
    @Transactional
    public void saveComment(CommentRequest commentRequest, Long postId, String memberLoginId){

        Post post = findPost(postId);
        Member member = memberRepository.findByLoginId(memberLoginId);
        Comment comment = commentRequest.toEntity(post, member);

        commentRepository.save(comment);
    }

    /*
    게시물에 따른 댓글 모두 조회
     */
    public List<CommentResponse> findCommentByPost(Long postId){

        Post post = findPost(postId);
        List<Comment> commentList = commentRepository.findCommentsByPost(post);

        List<CommentResponse> resultList = new ArrayList<>();
        for(Comment comment : commentList){
            resultList.add(
                    CommentResponse.builder()
                            .id(comment.getId())
                            .username(comment.getMember().getName())
                            .content(comment.getContent())
                            .createdTime(comment.getCreatedTime())
                            .build());
        }

        return resultList;
    }
}
