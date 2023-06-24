package NestNet.NestNetWebSite.service;

import NestNet.NestNetWebSite.domain.board.AttachedFile;
import NestNet.NestNetWebSite.domain.board.Board;
import NestNet.NestNetWebSite.dto.AttachedFileDto;
import NestNet.NestNetWebSite.repository.AttachedFileRepository;
import NestNet.NestNetWebSite.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
    private final BoardRepository boardRepository;

    // Create
    @Transactional
    public void save(List<AttachedFileDto> attachedFileDtos, Long boardId){
        Board board = boardRepository.findById(boardId);
        saveFiles(attachedFileDtos);
        for(AttachedFileDto fileDto : attachedFileDtos){
            AttachedFile attachedFile = fileDto.toEntity(board);
            attachedFileRepository.save(attachedFile);
        }

    }

    //파일 저장 (물리적)
    public void saveFiles(List<AttachedFileDto> attachedFileDtos){
        for(AttachedFileDto fileDto : attachedFileDtos){
            MultipartFile multipartFile = fileDto.getFile();
            File file = new File(fileDto.getSaveFilePath(), fileDto.getSaveFileName());

            try {
                multipartFile.transferTo(file);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}
