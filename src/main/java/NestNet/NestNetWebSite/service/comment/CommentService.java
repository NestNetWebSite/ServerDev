package NestNet.NestNetWebSite.service.comment;

import NestNet.NestNetWebSite.domain.comment.Comment;
import NestNet.NestNetWebSite.domain.member.Member;
import NestNet.NestNetWebSite.domain.post.Post;
import NestNet.NestNetWebSite.dto.request.CommentRequest;
import NestNet.NestNetWebSite.dto.response.CommentResponse;
import NestNet.NestNetWebSite.exception.CustomException;
import NestNet.NestNetWebSite.exception.ErrorCode;
import NestNet.NestNetWebSite.repository.comment.CommentRepository;
import NestNet.NestNetWebSite.repository.member.MemberRepository;
import NestNet.NestNetWebSite.repository.post.PostRepository;
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
    private final PostRepository postRepository;

    /*
    댓글 저장
     */
    @Transactional
    public void saveComment(CommentRequest commentRequest, Long postId, String memberLoginId){

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        Member member = memberRepository.findByLoginId(memberLoginId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_LOGIN_ID_NOT_FOUND));

        Comment comment = commentRequest.toEntity(post, member);

        commentRepository.save(comment);
    }

    /*
    댓글 수정
     */
    @Transactional
    public void modifyComment(CommentRequest commentRequest, Long commentId){

        Comment comment = commentRepository.findById(commentId)
                        .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        comment.modifyContent(commentRequest.getContent());
    }

    /*
    게시물에 따른 댓글 모두 조회
     */
    public List<CommentResponse> findCommentByPost(Long postId, String memberLoginId){

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        List<Comment> commentList = commentRepository.findAllByPost(post);

        List<CommentResponse> resultList = new ArrayList<>();
        for(Comment comment : commentList){
            if((comment.getMember().getLoginId()).equals(memberLoginId)){       //현재 로그인한 멤버가 작성한 댓글이면
                resultList.add(
                        CommentResponse.builder()
                                .id(comment.getId())
                                .username(comment.getMember().getName())
                                .content(comment.getContent())
                                .createdTime(comment.getCreatedTime())
                                .modifiedTime(comment.getModifiedTime())
                                .isMemberWritten(true)
                                .build());
            }
            else{
                resultList.add(
                        CommentResponse.builder()
                                .id(comment.getId())
                                .username(comment.getMember().getName())
                                .content(comment.getContent())
                                .createdTime(comment.getCreatedTime())
                                .modifiedTime(comment.getModifiedTime())
                                .isMemberWritten(false)
                                .build());
            }
        }

        return resultList;
    }

    /*
    댓글 단건 삭제
     */
    @Transactional
    public void deleteComment(Long commentId){

        Comment comment = commentRepository.findById(commentId)
                        .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        commentRepository.delete(comment);
    }

    /*
    게시물에 관련된 댓글 모두 삭제
     */
    @Transactional
    public void deleteAllComments(Long postId){

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        commentRepository.deleteAllByPost(post);
    }
}
