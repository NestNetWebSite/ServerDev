package NestNet.NestNetWebSite.service.post;

import NestNet.NestNetWebSite.api.ApiResult;
import NestNet.NestNetWebSite.domain.member.Member;
import NestNet.NestNetWebSite.domain.post.photo.PhotoPost;
import NestNet.NestNetWebSite.dto.request.PhotoPostModifyRequest;
import NestNet.NestNetWebSite.dto.request.PhotoPostRequest;
import NestNet.NestNetWebSite.dto.response.CommentResponse;
import NestNet.NestNetWebSite.dto.response.photopost.PhotoFileDto;
import NestNet.NestNetWebSite.dto.response.photopost.PhotoPostDto;
import NestNet.NestNetWebSite.dto.response.photopost.PhotoPostResponse;
import NestNet.NestNetWebSite.dto.response.photopost.ThumbNailDto;
import NestNet.NestNetWebSite.dto.response.photopost.ThumbNailResponse;
import NestNet.NestNetWebSite.exception.CustomException;
import NestNet.NestNetWebSite.exception.ErrorCode;
import NestNet.NestNetWebSite.repository.member.MemberRepository;
import NestNet.NestNetWebSite.repository.post.PhotoPostRepository;
import NestNet.NestNetWebSite.service.comment.CommentService;
import NestNet.NestNetWebSite.service.like.PostLikeService;
import NestNet.NestNetWebSite.service.photofile.PhotoFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    private final PhotoFileService photoFileService;
    private final CommentService commentService;
    private final PostLikeService postLikeService;
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
            photoFileService.save(post, files);
        }

        return ApiResult.success("게시물 저장 성공");
    }

    /*
    사진 게시판 썸네일 조회
     */
    public ApiResult<?> findThumbNail(int page, int size){

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));

        Page<PhotoPost> photoPostPage = photoPostRepository.findAll(pageRequest);

        List<PhotoPost> photoPostList = photoPostPage.getContent();

        List<PhotoFileDto> photoFileDtoList = photoFileService.findThumbNail(page, size);

        List<ThumbNailDto> thumbNailDtoList = new ArrayList<>();
        for(int i = 0; i < photoPostList.size(); i++){
            PhotoPost post = photoPostList.get(i);
            PhotoFileDto photoFileDto = photoFileDtoList.get(i);
            thumbNailDtoList.add(
                    new ThumbNailDto(post.getId(), post.getTitle(), post.getViewCount(),
                            post.getLikeCount(), photoFileDto.getSaveFilePath(), photoFileDto.getSaveFileName())
            );
        }

        Long totalSize = photoPostPage.getTotalElements();

        return ApiResult.success(new ThumbNailResponse(totalSize, thumbNailDtoList));
    }

    /*
    사진 게시물 단건 조회
     */
    @Transactional
    public PhotoPostResponse findPostById(Long id, String memberLoginId){

        PhotoPost post = photoPostRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        PhotoPostDto postDto = null;
        List<PhotoFileDto> fileDtoList = photoFileService.findAllFilesByPost(post);
        List<CommentResponse> commentResponseList = commentService.findCommentByPost(post, memberLoginId);
        boolean isMemberLiked = postLikeService.isMemberLikedByPost(post, memberLoginId);

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

        return new PhotoPostResponse(postDto, fileDtoList, commentResponseList, isMemberLiked);

    }

    /*
    사진 게시물 수정
     */
    @Transactional
    public void modifyPost(PhotoPostModifyRequest photoPostModifyRequest,
                           List<Long> fileIdList, List<MultipartFile> files, Long thumbNailId){

        PhotoPost post = photoPostRepository.findById(photoPostModifyRequest.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        photoFileService.modifyFiles(post, fileIdList, files);

        if(thumbNailId == null){

        }

        // 변경 감지 -> 자동 update
        post.modifyPost(photoPostModifyRequest.getTitle(), photoPostModifyRequest.getBodyContent());
    }
}
