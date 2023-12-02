package NestNet.NestNetWebSite.service.attachedfile;

import NestNet.NestNetWebSite.domain.attachedfile.AttachedFile;
import NestNet.NestNetWebSite.domain.post.Post;
import NestNet.NestNetWebSite.dto.response.AttachedFileResponse;
import NestNet.NestNetWebSite.exception.CustomException;
import NestNet.NestNetWebSite.exception.ErrorCode;
import NestNet.NestNetWebSite.repository.attachedfile.AttachedFileRepository;
import NestNet.NestNetWebSite.repository.post.PostRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class AttachedFileService {

    private final AttachedFileRepository attachedFileRepository;
    private final PostRepository postRepository;
    private final EntityManager entityManager;

    private static String basePath = "C:" + File.separator + "nestnetFile" + File.separator;

    /*
    첨부파일 저장 -> 저장 로직은 게시물에 종속적. 게시물 저장하는 서비스 로직에서 수행
     */

    /*
    게시물에 해당된 첨부파일 모두 조회
     */
    public List<AttachedFileResponse> findAllFilesByPost(Long postId){

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        List<AttachedFile> files = attachedFileRepository.findAllByPost(post);

        List<AttachedFileResponse> fileResponseList = new ArrayList<>();

        for(AttachedFile file : files){
            fileResponseList.add(new AttachedFileResponse(file.getId(), file.getOriginalFileName(), file.getSaveFilePath(), file.getSaveFileName()));
        }

        return fileResponseList;
    }

    /*
    실제 파일 조회
     */
    public InputStreamResource findFile(Long postId, String saveFileName){

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        AttachedFile file = attachedFileRepository.findByPostAndFileName(post, saveFileName)
                .orElseThrow(() -> new CustomException(ErrorCode.FILE_NOT_FOUND));

        InputStreamResource resource = null;

        StringBuilder filePathBuilder = new StringBuilder(basePath)
                .append(file.getSaveFilePath())
                .append(File.separator)
                .append(file.getSaveFileName());

        Path filePath = Paths.get(filePathBuilder.toString());
        File realFile = new File(filePath.toString());

        try{
            resource = new InputStreamResource(new FileInputStream(realFile));
        } catch (FileNotFoundException e){

        }

        return resource;
    }

    /*
    첨부파일 수정
     */
    @Transactional
    public void modifyFiles(List<Long> existFileIdList, List<MultipartFile> fileList, Long postId){

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        List<AttachedFile> deleteFileList = attachedFileRepository.findAllByPost(post);      //이전에 있던 파일 중 삭제할 파일

        // 이전에 있던 파일 중 삭제될 파일만 남김
        for(int i = 0; i < deleteFileList.size(); i++){
            for(Long existFileId : existFileIdList){
                if(deleteFileList.get(i).getId() == existFileId){
                    deleteFileList.remove(deleteFileList.get(i));
                }
            }
        }

        // Delete From DB
        attachedFileRepository.deleteAll(deleteFileList);

        // Delete Real File
        deleteRealFile(deleteFileList);

        List<AttachedFile> newFileList = new ArrayList<>();     // 새로 입력된 파일
        for(MultipartFile file : fileList){
            newFileList.add(new AttachedFile(post, file));
        }

        // Save To DB
        attachedFileRepository.saveAll(newFileList);

        // Save Real File
        saveRealFile(newFileList, fileList);
    }

    /*
    첨부파일 삭제
     */
    @Transactional
    public void deleteFiles(Long postId){

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        List<AttachedFile> files = attachedFileRepository.findAllByPost(post);

        // Delete From DB
        attachedFileRepository.deleteAll(files);

        // Delete Real File
        deleteRealFile(files);
    }

    //==============================물리적인 파일 컨트롤==============================//

    // 실제 파일 저장
    private void saveRealFile(List<AttachedFile> attachedFiles, List<MultipartFile> files){

        for(int i = 0; i < attachedFiles.size(); i++){

            AttachedFile attachedFile = attachedFiles.get(i);
            MultipartFile file = files.get(i);

            StringBuilder filePathBuilder = new StringBuilder(basePath)
                    .append(attachedFile.getSaveFilePath())
                    .append(File.separator)
                    .append(attachedFile.getSaveFileName());

            Path saveFilePath = Paths.get(filePathBuilder.toString());

            try {
                file.transferTo(saveFilePath);
            } catch (IOException e){

            } catch (IllegalStateException e){

            }
        }
    }

    // 실제 파일 삭제
    private void deleteRealFile(List<AttachedFile> files){

        for(AttachedFile file : files){
            StringBuilder filePathBuilder = new StringBuilder(basePath)
                    .append(file.getSaveFilePath())
                    .append(File.separator)
                    .append(file.getSaveFileName());

            Path deleteFilePath = Paths.get(filePathBuilder.toString());
            File deleteFile = new File(deleteFilePath.toString());

            if(!deleteFile.exists()){
                throw new CustomException(ErrorCode.FILE_NOT_FOUND);
            }

            deleteFile.delete();
        }
    }

}
