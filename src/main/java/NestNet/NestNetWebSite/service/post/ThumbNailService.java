package NestNet.NestNetWebSite.service.post;

import NestNet.NestNetWebSite.api.ApiResult;
import NestNet.NestNetWebSite.domain.attachedfile.AttachedFile;
import NestNet.NestNetWebSite.domain.post.Post;
import NestNet.NestNetWebSite.domain.post.photo.ThumbNail;
import NestNet.NestNetWebSite.dto.response.photopost.ThumbNailDto;
import NestNet.NestNetWebSite.dto.response.photopost.ThumbNailResponse;
import NestNet.NestNetWebSite.exception.CustomException;
import NestNet.NestNetWebSite.exception.ErrorCode;
import NestNet.NestNetWebSite.repository.post.ThumbNailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ThumbNailService {

    private final ThumbNailRepository thumbNailRepository;

    private static String basePath = "C:" + File.separator + "nestnetFile" + File.separator;

    /*
    썸네일 사진 저장
     */
    @Transactional
    public void saveThumbNail(Post post, MultipartFile file){

        ThumbNail thumbNail = new ThumbNail(post, file);

        thumbNailRepository.save(thumbNail);

        StringBuilder filePathBuilder = new StringBuilder(basePath)
                .append(thumbNail.getSaveFilePath())
                .append(File.separator)
                .append(thumbNail.getSaveFileName());

        Path saveFilePath = Paths.get(filePathBuilder.toString());

        try {
            file.transferTo(saveFilePath);
        } catch (IOException e){

        } catch (IllegalStateException e){

        }
    }

    /*
    사진 게시판 썸네일 조회 (페이징)
     */
    public ApiResult<?> findThumbNails(int page, int size){

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));

        Page<ThumbNail> thumbNailPage = thumbNailRepository.findAllByPaging(pageRequest);

        List<ThumbNail> thumbNailList = thumbNailPage.getContent();

        List<ThumbNailDto> thumbNailDtoList = new ArrayList<>();
        for(ThumbNail thumbNail : thumbNailList){
            thumbNailDtoList.add(
                    ThumbNailDto.builder()
                            .postId(thumbNail.getPost().getId())
                            .title(thumbNail.getPost().getTitle())
                            .viewCount(thumbNail.getPost().getViewCount())
                            .likeCount(thumbNail.getPost().getLikeCount())
                            .saveFilePath(thumbNail.getSaveFilePath())
                            .saveFileName(thumbNail.getSaveFileName())
                            .build()
            );
        }

        ThumbNailResponse result = new ThumbNailResponse(thumbNailDtoList);

        return ApiResult.success(result);
    }

    /*
    썸네일 삭제
     */
    public void deleteThumbNail(Post post){

        ThumbNail thumbNail = thumbNailRepository.findByPost(post)
                .orElseThrow(() -> new CustomException(ErrorCode.THUMBNAIL_FILE_NOT_FOUND));

        thumbNailRepository.delete(thumbNail);

        StringBuilder filePathBuilder = new StringBuilder(basePath)
                .append(thumbNail.getSaveFilePath())
                .append(File.separator)
                .append(thumbNail.getSaveFileName());

        Path deleteFilePath = Paths.get(filePathBuilder.toString());

        File deleteFile = new File(deleteFilePath.toString());

        if(!deleteFile.exists()){
            throw new CustomException(ErrorCode.FILE_NOT_FOUND);
        }

        deleteFile.delete();
    }
}
