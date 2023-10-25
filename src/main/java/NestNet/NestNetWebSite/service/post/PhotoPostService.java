package NestNet.NestNetWebSite.service.post;

import NestNet.NestNetWebSite.api.ApiResult;
import NestNet.NestNetWebSite.domain.attachedfile.AttachedFile;
import NestNet.NestNetWebSite.domain.member.Member;
import NestNet.NestNetWebSite.domain.post.Post;
import NestNet.NestNetWebSite.domain.post.photo.PhotoPost;
import NestNet.NestNetWebSite.domain.post.photo.ThumbNail;
import NestNet.NestNetWebSite.dto.request.PhotoPostModifyRequest;
import NestNet.NestNetWebSite.dto.request.PhotoPostRequest;
import NestNet.NestNetWebSite.dto.response.PhotoPostResponse;
import NestNet.NestNetWebSite.dto.response.ThumbNailResponse;
import NestNet.NestNetWebSite.repository.member.MemberRepository;
import NestNet.NestNetWebSite.repository.attachedfile.AttachedFileRepository;
import NestNet.NestNetWebSite.repository.post.PhotoPostRepository;
import NestNet.NestNetWebSite.repository.post.ThumbNailRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
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
    private final AttachedFileRepository attachedFileRepository;
    private final ThumbNailRepository thumbNailRepository;

    /*
    사진 게시판에 게시물 저장
     */
    @Transactional
    public ApiResult<?> savePost(PhotoPostRequest photoPostRequest, List<MultipartFile> files,
                                 String memberLoginId, HttpServletResponse response){

        Member member = memberRepository.findByLoginId(memberLoginId);

        PhotoPost post = photoPostRequest.toEntity(member);

        List<AttachedFile> attachedFileList = new ArrayList<>();

        MultipartFile thumbNailFile = null;         //썸네일 사진
        int curr = 0;
        for(MultipartFile file : files){
            if(curr++ == 0){
                thumbNailFile = file;               //첫번째 사진을 썸네일 사진으로 저장
            }
            AttachedFile attachedFile = new AttachedFile(post, file);
            attachedFileList.add(attachedFile);
            post.addAttachedFiles(attachedFile);
        }

        photoPostRepository.save(post);
        boolean isthumbNailSaved = thumbNailRepository.save(new ThumbNail(post, thumbNailFile), thumbNailFile);
        boolean isFileSaved = attachedFileRepository.saveAll(attachedFileList, files);

        if(isthumbNailSaved == false || isFileSaved == false){
            return ApiResult.error(response, HttpStatus.INTERNAL_SERVER_ERROR, "파일 저장 실패");
        }
        return ApiResult.success("게시물 저장 성공");
    }

    /*
    사진 게시판 썸네일 조회 (페이징)
     */
    public ApiResult<?> findThumbNails(int offset, int limit){

        List<ThumbNail> thumbNailList = thumbNailRepository.findAllPhotoThumbNailByPaging(offset, limit);

        List<ThumbNailResponse> thumbNailResponseList = new ArrayList<>();
        for(ThumbNail thumbNail : thumbNailList){
            thumbNailResponseList.add(
                    ThumbNailResponse.builder()
                            .postId(thumbNail.getPost().getId())
                            .title(thumbNail.getPost().getTitle())
                            .viewCount(thumbNail.getPost().getViewCount())
                            .likeCount(thumbNail.getPost().getLikeCount())
                            .saveFilePath(thumbNail.getSaveFilePath())
                            .saveFileName(thumbNail.getSaveFileName())
                            .build()
            );
        }

        return ApiResult.success(thumbNailResponseList);
    }

    /*
    사진 게시물 단건 조회
     */
    @Transactional
    public PhotoPostResponse findPostById(Long id, String memberLoginId){

        PhotoPost post = photoPostRepository.findById(id);
        photoPostRepository.addViewCount(post, memberLoginId);

        if(memberLoginId.equals(post.getMember().getLoginId())){
            return PhotoPostResponse.builder()
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
            return PhotoPostResponse.builder()
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
    }

    /*
    좋아요
     */
    @Transactional
    public void like(Long id){

        PhotoPost post = photoPostRepository.findById(id);
        photoPostRepository.like(post);
    }

    /*
    좋아요 취소
     */
    @Transactional
    public void cancelLike(Long id){

        PhotoPost post = photoPostRepository.findById(id);
        photoPostRepository.cancelLike(post);
    }

    /*
    사진 게시물 수정
     */
    @Transactional
    public void modifyPost(PhotoPostModifyRequest photoPostModifyRequest, Long postId){

        PhotoPost post = photoPostRepository.findById(postId);

        // 변경 감지 -> 자동 update
        post.modifyPost(photoPostModifyRequest.getTitle(), photoPostModifyRequest.getBodyContent());
    }

    /*
    사진 게시물 삭제
     */
    @Transactional
    public void deletePost(Long postId){

        Post post = photoPostRepository.findById(postId);

        photoPostRepository.deletePost(post);
    }
}
