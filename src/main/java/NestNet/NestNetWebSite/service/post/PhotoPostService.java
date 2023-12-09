package NestNet.NestNetWebSite.service.post;

import NestNet.NestNetWebSite.api.ApiResult;
import NestNet.NestNetWebSite.domain.attachedfile.AttachedFile;
import NestNet.NestNetWebSite.domain.member.Member;
import NestNet.NestNetWebSite.domain.post.Post;
import NestNet.NestNetWebSite.domain.post.photo.PhotoPost;
import NestNet.NestNetWebSite.domain.post.photo.ThumbNail;
import NestNet.NestNetWebSite.dto.request.PhotoPostModifyRequest;
import NestNet.NestNetWebSite.dto.request.PhotoPostRequest;
import NestNet.NestNetWebSite.dto.response.AttachedFileResponse;
import NestNet.NestNetWebSite.dto.response.CommentResponse;
import NestNet.NestNetWebSite.dto.response.photopost.PhotoPostDto;
import NestNet.NestNetWebSite.dto.response.photopost.PhotoPostResponse;
import NestNet.NestNetWebSite.dto.response.photopost.ThumbNailDto;
import NestNet.NestNetWebSite.dto.response.photopost.ThumbNailResponse;
import NestNet.NestNetWebSite.exception.CustomException;
import NestNet.NestNetWebSite.exception.ErrorCode;
import NestNet.NestNetWebSite.repository.member.MemberRepository;
import NestNet.NestNetWebSite.repository.attachedfile.AttachedFileRepository;
import NestNet.NestNetWebSite.repository.post.PhotoPostRepository;
import NestNet.NestNetWebSite.repository.post.ThumbNailRepository;
import NestNet.NestNetWebSite.service.attachedfile.AttachedFileService;
import NestNet.NestNetWebSite.service.comment.CommentService;
import NestNet.NestNetWebSite.service.like.PostLikeService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PhotoPostService {

    private final PhotoPostRepository photoPostRepository;
    private final MemberRepository memberRepository;
    private final AttachedFileService attachedFileService;
    private final CommentService commentService;
    private final PostLikeService postLikeService;
    private final ThumbNailService thumbNailService;
    private final PostService postService;

    /*
    사진 게시판에 게시물 저장
     */
    @Transactional
    public ApiResult<?> savePost(PhotoPostRequest photoPostRequest, List<MultipartFile> files, String memberLoginId){

        Member member = memberRepository.findByLoginId(memberLoginId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_LOGIN_ID_NOT_FOUND));

        PhotoPost post = photoPostRequest.toEntity(member);

        photoPostRepository.save(post);

        if(files != null){
            attachedFileService.save(post, files);

            MultipartFile thumbNailFile = files.get(0);         //썸네일 사진

            thumbNailService.saveThumbNail(post, thumbNailFile);
        }

        return ApiResult.success("게시물 저장 성공");
    }

    /*
    사진 게시물 단건 조회
     */
    @Transactional
    public PhotoPostResponse findPostById(Long id, String memberLoginId){

        PhotoPost post = photoPostRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        PhotoPostDto postDto = null;
        List<AttachedFileResponse> fileDtoList = attachedFileService.findAllFilesByPost(post);
        List<CommentResponse> commentResponseList = commentService.findCommentByPost(post, memberLoginId);
        boolean isMemberLiked = postLikeService.isMemberLikedByPost(post, memberLoginId);
        ThumbNail thumbNail = thumbNailService.findThumbNail(post);

        postService.addViewCount(post, memberLoginId);

        if(memberLoginId.equals(post.getMember().getLoginId())){
            postDto =  PhotoPostDto.builder()
                    .id(post.getId())
                    .title(post.getTitle())
                    .bodyContent(post.getBodyContent())
                    .viewCount(post.getViewCount())
                    .likeCount(post.getLikeCount())
                    .username(post.getMember().getName())
                    .createdTime(post.getCreatedTime())
                    .modifiedTime(post.getModifiedTime())
                    .isMemberWritten(true)
                    .build();
        }
        else{
            postDto =  PhotoPostDto.builder()
                    .id(post.getId())
                    .title(post.getTitle())
                    .bodyContent(post.getBodyContent())
                    .viewCount(post.getViewCount())
                    .likeCount(post.getLikeCount())
                    .username(post.getMember().getName())
                    .createdTime(post.getCreatedTime())
                    .modifiedTime(post.getModifiedTime())
                    .isMemberWritten(false)
                    .build();
        }

        return new PhotoPostResponse(postDto, fileDtoList, commentResponseList, thumbNail.getId(), isMemberLiked);

    }

    /*
    사진 게시물 수정
     */
    @Transactional
    public void modifyPost(PhotoPostModifyRequest photoPostModifyRequest,
                           List<Long> fileIdList, List<MultipartFile> files, Long thumbNailId){

        PhotoPost post = photoPostRepository.findById(photoPostModifyRequest.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        attachedFileService.modifyFiles(post, fileIdList, files);

        if(thumbNailId == null){

        }

        // 변경 감지 -> 자동 update
        post.modifyPost(photoPostModifyRequest.getTitle(), photoPostModifyRequest.getBodyContent());
    }
}
