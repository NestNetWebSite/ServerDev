package NestNet.NestNetWebSite.service;

import NestNet.NestNetWebSite.domain.post.AttachedFile;
import NestNet.NestNetWebSite.domain.post.Post;
import NestNet.NestNetWebSite.dto.request.AttachedFileRequestDto;
import NestNet.NestNetWebSite.dto.response.AttachedFileDto;
import NestNet.NestNetWebSite.repository.AttachedFileRepository;
import NestNet.NestNetWebSite.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AttachedFileService {

    private final AttachedFileRepository attachedFileRepository;
    private final PostRepository postRepository;

    // Create
    @Transactional
    public void save(List<AttachedFileRequestDto> attachedFileRequestDtos, Long PostId){
        Post post = postRepository.findById(PostId);
        saveFiles(attachedFileRequestDtos);
        for(AttachedFileRequestDto fileDto : attachedFileRequestDtos){
            AttachedFile attachedFile = fileDto.toEntity(post);
            attachedFileRepository.save(attachedFile);
        }

    }

    // 파일 저장 (물리적)
    public void saveFiles(List<AttachedFileRequestDto> attachedFileRequestDtos){
        for(AttachedFileRequestDto fileDto : attachedFileRequestDtos){
            MultipartFile multipartFile = fileDto.getFile();
            File file = new File(fileDto.getSaveFilePath(), fileDto.getSaveFileName());

            try {
                multipartFile.transferTo(file);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    // Read ---> 게시물에 해당하는 파일 조회
    public List<AttachedFileDto> findFilesByPost(Long postId){

        Post post = postRepository.findById(postId);
        List<AttachedFile> files = attachedFileRepository.findByPost(post);
        List<AttachedFileDto> fileDtos = new ArrayList<>();

        for(AttachedFile file : files){
            fileDtos.add(new AttachedFileDto(file.getOriginalFileName(), file.getSaveFileName(), file.getSaveFilePath()));
        }
        return fileDtos;
    }

}
