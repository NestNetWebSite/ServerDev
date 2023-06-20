package NestNet.NestNetWebSite.service;

import NestNet.NestNetWebSite.dto.FileDto;
import NestNet.NestNetWebSite.repository.AttachedFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AttachedFileService {

    private final AttachedFileRepository attachedFileRepository;
    private static String fileUploadPath = "C:" + File.separator + "nestnetFile" + File.separator;     //리눅스, 맥은 슬래시 어떻게 처리??

    @Transactional
    public Long saveOne(FileDto){
        attachedFileRepository.
    }

    @Transactional
    public Long saveAll(FileDto){

    }

    //파일 하나 저장
    public String saveOneFile(FileDto fileDto){
        String fileName = createFileName(fileDto);
        File file = new File(fileUploadPath + fileName);

        try {
            file.createNewFile();       //파일 저장
        } catch (IOException e){
            e.printStackTrace();
        }

        return file.getAbsolutePath();
    }

    //파일 여러개 저장
    public List<String> saveFiles(List<FileDto> files){
        List<String> filePathList = new ArrayList<>();
        String dirName = UUID.randomUUID().toString();
        File dir = new File(fileUploadPath + dirName);      //랜덤 이름의 디렉토리 생성

        try {
            dir.mkdir();            //디렉토리 생성
        } catch (Exception e){
            e.printStackTrace();
        }

        for(FileDto fileDto : files){
            String fileName = createFileName(fileDto);
            File file = new File(dir, fileName);

            try {
                file.createNewFile();       //파일 저장
            } catch (IOException e){
                e.printStackTrace();
            }
            filePathList.add(file.getAbsolutePath());
        }

        return filePathList;
    }

    //파일 이름 중복 방지를 위한 파일명 생성
    public String createFileName(FileDto fileDto){
        UUID uuid = UUID.randomUUID();

        return uuid.toString() + "_" + fileDto.getFile().getOriginalFilename();
    }
}
