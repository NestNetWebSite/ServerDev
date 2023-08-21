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
    댓글 수정
     */
    @Transactional
    public void modifyComment(CommentRequest commentRequest, Long commentId){

        Comment comment = commentRepository.findById(commentId);

        commentRepository.modify(comment, commentRequest.getContent());
    }

    /*
    게시물에 따른 댓글 모두 조회
     */
    public List<CommentResponse> findCommentByPost(Long postId, String memberLoginId){

        Post post = findPost(postId);
        List<Comment> commentList = commentRepository.findCommentsByPost(post);

        List<CommentResponse> resultList = new ArrayList<>();
        for(Comment comment : commentList){
            if((comment.getMember().getLoginId()).equals(memberLoginId)){       //현재 로그인한 멤버가 작성한 댓글이면
                resultList.add(
                        CommentResponse.builder()
                                .id(comment.getId())
                                .username(comment.getMember().getName())
                                .content(comment.getContent())
                                .createdTime(comment.getCreatedTime())
                                .isMemberWitten(true)
                                .build());
            }
            else{
                resultList.add(
                        CommentResponse.builder()
                                .id(comment.getId())
                                .username(comment.getMember().getName())
                                .content(comment.getContent())
                                .createdTime(comment.getCreatedTime())
                                .isMemberWitten(false)
                                .build());
            }
        }

        return resultList;
    }

    /*
    댓글 삭제
     */
    @Transactional
    public void DeleteComment(Long commentId){

        Comment comment = commentRepository.findById(commentId);
        commentRepository.delete(comment);
    }
}
