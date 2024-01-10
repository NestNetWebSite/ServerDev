package NestNet.NestNetWebSite.service.photofile;

import NestNet.NestNetWebSite.domain.photofile.PhotoFile;
import NestNet.NestNetWebSite.domain.post.Post;
import NestNet.NestNetWebSite.dto.response.photopost.PhotoFileDto;
import NestNet.NestNetWebSite.exception.CustomException;
import NestNet.NestNetWebSite.exception.ErrorCode;
import NestNet.NestNetWebSite.repository.photofile.PhotoFileRepository;
import NestNet.NestNetWebSite.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PhotoFileService {

    private final PhotoFileRepository photoFileRepository;
    private final PostRepository postRepository;

    private static String basePath = "C:" + File.separator + "nestnetFile" + File.separator;

    /*
    사진 파일 저장
     */
    @Transactional
    public void save(Post post, List<MultipartFile> files){

        if(!files.isEmpty()){

            List<PhotoFile> photoFileList = new ArrayList<>();

            int idx = 0;
            for(MultipartFile file : files){
                if(idx++ == 0){
                    photoFileList.add(new PhotoFile(post, file, true));
                }
                else{
                    photoFileList.add(new PhotoFile(post, file, false));
                }
            }
            photoFileRepository.saveAll(photoFileList);
            saveRealFile(photoFileList, files);
        }
    }

    /*
    사진 게시판 썸네일 파일 정보 조회
     */
    public List<PhotoFileDto> findThumbNail(int page, int size){

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));

        List<PhotoFile> photoFileList = photoFileRepository.findThumbNail(pageRequest).getContent();

        List<PhotoFileDto> photoFileDtoList = new ArrayList<>();
        for(PhotoFile photoFile: photoFileList){
            photoFileDtoList.add(
                    new PhotoFileDto(photoFile.getId(), photoFile.getOriginalFileName(), photoFile.getSaveFilePath(), photoFile.getSaveFileName())
            );
        }

        return photoFileDtoList;
    }

    /*
    게시물에 해당된 모든 사진 파일 조회
     */
    public List<PhotoFileDto> findAllFilesByPost(Post post){

        List<PhotoFile> files = photoFileRepository.findAllByPost(post);

        List<PhotoFileDto> fileResponseList = new ArrayList<>();

        for(PhotoFile file : files){
            fileResponseList.add(new PhotoFileDto(file.getId(), file.getOriginalFileName(), file.getSaveFilePath(), file.getSaveFileName()));
        }

        return fileResponseList;
    }

    /*
    실제 파일 조회
     */
    public InputStreamResource findFile(Long postId, String saveFileName){

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        PhotoFile file = photoFileRepository.findByPostAndSaveFileName(post, saveFileName)
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
            throw new CustomException(ErrorCode.FILE_NOT_FOUND);
        }

        return resource;
    }

    /*
    사진 파일 수정
     */
    @Transactional
    public void modifyFiles(Post post, List<Long> existFileIdList, List<MultipartFile> fileList){

        List<PhotoFile> deleteFileList = photoFileRepository.findAllByPost(post);      //이전에 있던 파일 중 삭제할 파일

        boolean thumbNailExist = false;

        // 이전에 있던 파일 중 삭제될 파일만 남김
        for(int i = 0; i < deleteFileList.size(); i++){

            if(existFileIdList == null) break;

            for(Long existFileId : existFileIdList){
                if(existFileId == deleteFileList.get(i).getId()){

                    if(deleteFileList.get(i).isThumbNail()) thumbNailExist = true;

                    deleteFileList.remove(deleteFileList.get(i));
                }
            }
        }

        // Delete From DB
        photoFileRepository.deleteAll(deleteFileList);

        // Delete Real File
        deleteRealFile(deleteFileList);

        List<PhotoFile> newFileList = new ArrayList<>();     // 새로 입력된 파일

        if(fileList != null){

            int idx = 0;
            for(MultipartFile file : fileList){
                if(!thumbNailExist && idx++ == 0){
                    newFileList.add(new PhotoFile(post, file, true));
                }
                else{
                    newFileList.add(new PhotoFile(post, file, false));
                }

            }
        }

        // Save To DB
        photoFileRepository.saveAll(newFileList);

        // Save Real File
        saveRealFile(newFileList, fileList);
    }

    /*
    사진 파일 삭제
     */
    @Transactional
    public void deleteFiles(Post post){

        List<PhotoFile> photoFileList = photoFileRepository.findAllByPost(post);

        // Delete From DB
        photoFileRepository.deleteAll(photoFileList);

        // Delete Real File
        deleteRealFile(photoFileList);
    }

    //==============================물리적인 파일 컨트롤==============================//

    // 실제 파일 저장
    private void saveRealFile(List<PhotoFile> photoFileList, List<MultipartFile> files){

        for(int i = 0; i < photoFileList.size(); i++){

            PhotoFile photoFile = photoFileList.get(i);
            MultipartFile file = files.get(i);

            StringBuilder filePathBuilder = new StringBuilder(basePath)
                    .append(photoFile.getSaveFilePath())
                    .append(File.separator)
                    .append(photoFile.getSaveFileName());

            Path saveFilePath = Paths.get(filePathBuilder.toString());

            try {
                file.transferTo(saveFilePath);
            } catch (IOException | IllegalStateException e){
                throw new CustomException(ErrorCode.CANNOT_SAVE_FILE);
            }
        }
    }

    // 실제 파일 삭제
    private void deleteRealFile(List<PhotoFile> photoFileList){

        for(PhotoFile file : photoFileList){

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
