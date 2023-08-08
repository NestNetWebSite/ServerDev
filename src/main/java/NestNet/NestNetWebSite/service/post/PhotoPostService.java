package NestNet.NestNetWebSite.service.post;

import NestNet.NestNetWebSite.domain.attachedfile.AttachedFile;
import NestNet.NestNetWebSite.domain.member.Member;
import NestNet.NestNetWebSite.domain.post.exam.ExamCollectionPost;
import NestNet.NestNetWebSite.domain.post.photo.PhotoPost;
import NestNet.NestNetWebSite.domain.post.photo.ThumbNail;
import NestNet.NestNetWebSite.dto.request.PhotoPostRequestDto;
import NestNet.NestNetWebSite.dto.response.PhotoPostDto;
import NestNet.NestNetWebSite.repository.member.MemberRepository;
import NestNet.NestNetWebSite.repository.attachedfile.AttachedFileRepository;
import NestNet.NestNetWebSite.repository.post.PhotoPostRepository;
import NestNet.NestNetWebSite.repository.post.ThumbNailRepository;
import lombok.RequiredArgsConstructor;
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
    public void savePost(PhotoPostRequestDto photoPostRequestDto, List<MultipartFile> files, String memberLoginId){

        Member member = memberRepository.findByLoginId(memberLoginId);

        PhotoPost post = photoPostRequestDto.toEntity(member);

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
        thumbNailRepository.save(new ThumbNail(post, thumbNailFile), thumbNailFile);
        attachedFileRepository.saveAll(attachedFileList, files);
    }

    /*
    사진 게시물 단건 조회
     */
    public PhotoPostDto findPostById(Long id, String memberLoginId){

        PhotoPost post = photoPostRepository.findById(id);
        photoPostRepository.addViewCount(post, memberLoginId);

        return new PhotoPostDto(post.getId(), post.getTitle(),
                post.getBodyContent(), post.getViewCount(), post.getLikeCount(), post.getMember().getName());
    }

    /*
    좋아요
     */
    public void like(Long id){

        PhotoPost post = photoPostRepository.findById(id);
        photoPostRepository.like(post);
    }

    /*
    좋아요 취소
     */
    public void cancelLike(Long id){

        PhotoPost post = photoPostRepository.findById(id);
        photoPostRepository.cancelLike(post);
    }

    /*
    족보 게시물 삭제
     */
    public void deletePost(Long id){
        photoPostRepository.deletePost(id);
    }
}
