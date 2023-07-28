package NestNet.NestNetWebSite.service;

import NestNet.NestNetWebSite.domain.attachedfile.AttachedFile;
import NestNet.NestNetWebSite.domain.post.Post;
import NestNet.NestNetWebSite.dto.request.AttachedFileRequestDto;
import NestNet.NestNetWebSite.dto.response.AttachedFileDto;
import NestNet.NestNetWebSite.repository.AttachedFileRepository;
import NestNet.NestNetWebSite.repository.UnifiedPostRepository;
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

    // 첨부파일은 게시판에 종속적인데, 굳이 서비스 레이어가 있을 필요가 있을까??

//    @Transactional
//    public void save()


}
