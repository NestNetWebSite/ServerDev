package NestNet.NestNetWebSite.controller;

import NestNet.NestNetWebSite.dto.AttachedFileDto;
import NestNet.NestNetWebSite.dto.ExamCollectionBoardDto;
import NestNet.NestNetWebSite.dto.UnifiedBoardDto;
import NestNet.NestNetWebSite.service.AttachedFileService;
import NestNet.NestNetWebSite.service.BoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final AttachedFileService attachedFileService;
    private final BoardService boardService;

    @PostMapping("/exam_collection_board/post")
    public void savePost(@RequestBody @Valid ExamCollectionBoardDto boardDto, @RequestBody @Valid List<AttachedFileDto> attachedFileDtos){
        attachedFileService.saveFiles(attachedFileDtos);
        boardService.save(boardDto);
    }

    @PostMapping("/unified_board/post")
    public void savePost(@RequestBody @Valid UnifiedBoardDto boardDto, @RequestBody @Valid List<AttachedFileDto> attachedFileDtos){
        attachedFileService.saveFiles(attachedFileDtos);
        boardService.save(boardDto);
    }

}
