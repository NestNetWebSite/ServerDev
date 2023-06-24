package NestNet.NestNetWebSite.controller;

import NestNet.NestNetWebSite.domain.board.BoardType;
import NestNet.NestNetWebSite.dto.AttachedFileDto;
import NestNet.NestNetWebSite.dto.ExamCollectionBoardDto;
import NestNet.NestNetWebSite.dto.UnifiedBoardDto;
import NestNet.NestNetWebSite.service.AttachedFileService;
import NestNet.NestNetWebSite.service.BoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/unified_board/{board_type}")
    public void showPost(@PathVariable("board_type") BoardType board_type){
        boardService.findAllFromUnifiedBoard(board_type, )
    }

}
