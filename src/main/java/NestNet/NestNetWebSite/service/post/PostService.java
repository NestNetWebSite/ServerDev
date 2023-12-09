package NestNet.NestNetWebSite.service.post;

import NestNet.NestNetWebSite.config.redis.RedisUtil;
import NestNet.NestNetWebSite.domain.like.PostLike;
import NestNet.NestNetWebSite.domain.post.Post;
import NestNet.NestNetWebSite.domain.post.PostCategory;
import NestNet.NestNetWebSite.domain.post.exam.ExamCollectionPost;
import NestNet.NestNetWebSite.exception.CustomException;
import NestNet.NestNetWebSite.exception.ErrorCode;
import NestNet.NestNetWebSite.repository.post.PostRepository;
import NestNet.NestNetWebSite.service.attachedfile.AttachedFileService;
import NestNet.NestNetWebSite.service.comment.CommentService;
import NestNet.NestNetWebSite.service.like.PostLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final AttachedFileService attachedFileService;
    private final CommentService commentService;
    private final PostLikeService postLikeService;
    private final ThumbNailService thumbNailService;
    private final PostRepository postRepository;
    private final RedisUtil redisUtil;

    // 조회수 update
    @Transactional
    public void addViewCount(Post post, String memberLoginId){

        String viewRecordKey = memberLoginId + "_" +  post.getId().toString();     //사용자아이디 + 게시물 id

        //24시간 내에 다시 조회해도 조회수 올라가지 않음 (조회하지 않았으면 레디스에 없음 -> 조회수 + 1)
        if(!redisUtil.hasKey(viewRecordKey)){
            post.addViewCount();        //변경 감지에 의해 update
            redisUtil.setData(viewRecordKey, "v", 24, TimeUnit.HOURS);      //24시간 유지 -> 자동 삭제
        }
    }

    /*
    게시물 삭제
     */
    @Transactional
    public void deletePost(Long postId){

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        attachedFileService.deleteFiles(post);
        commentService.deleteAllComments(post);
        postLikeService.deleteLike(post);

        if(post.getPostCategory().equals(PostCategory.PHOTO)){
            thumbNailService.deleteThumbNail(post);
        }

        postRepository.delete(post);
    }

    /*
    좋아요
     */
    @Transactional
    public void like(Long id, String memberLoginId){

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        if(!postLikeService.isMemberLikedByPost(post, memberLoginId)) {
            postLikeService.saveLike(post, memberLoginId);

            post.like();
        }
    }

    /*
    좋아요 취소
     */
    @Transactional
    public void cancelLike(Long id, String memberLoginId){

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        if(postLikeService.isMemberLikedByPost(post, memberLoginId)) {
            postLikeService.cancelLike(post, memberLoginId);

            post.cancelLike();
        }
    }

}
