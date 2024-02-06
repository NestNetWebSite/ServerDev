package NestNet.NestNetWebSite.service.life4cut;

import NestNet.NestNetWebSite.api.ApiResult;
import NestNet.NestNetWebSite.domain.life4cut.Life4Cut;
import NestNet.NestNetWebSite.dto.response.life4cut.Life4CutDto;
import NestNet.NestNetWebSite.dto.response.life4cut.Life4CutResponse;
import NestNet.NestNetWebSite.repository.life4cut.Life4CutRepository;
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
public class Life4CutService {

    private final Life4CutRepository life4CutRepository;

    private static String basePath = "C:" + File.separator + "nestnetFile" + File.separator;

    /*
    인생네컷 저장
     */
    @Transactional
    public ApiResult<?> saveFile(MultipartFile file){

        Life4Cut life4Cut = new Life4Cut(file);

        life4CutRepository.save(life4Cut);

        saveRealFile(life4Cut, file);

        return ApiResult.success("인생네컷 저장 성공");
    }

    /*
    인생네컷 조회 (최신순)
     */
    public ApiResult<?> findFileByPaging(int size){

        PageRequest pageRequest = PageRequest.of(0, size / 2, Sort.by(Sort.Direction.DESC, "id"));
        List<Life4Cut> recentLife4CutList = life4CutRepository.findAll(pageRequest).getContent();
        List<Life4Cut> randomLife4CutList = life4CutRepository.findByRandom(size / 2);


        List<Life4CutDto> dtoList = new ArrayList<>();
        for(Life4Cut life4Cut : recentLife4CutList){
            dtoList.add(new Life4CutDto(life4Cut.getSaveFilePath(), life4Cut.getSaveFileName()));
        }
        for(Life4Cut life4Cut : randomLife4CutList){
            dtoList.add(new Life4CutDto(life4Cut.getSaveFilePath(), life4Cut.getSaveFileName()));
        }

        return ApiResult.success(new Life4CutResponse(dtoList));
    }

    //==============================물리적인 파일 컨트롤==============================//

    // 실제 파일 저장
    private void saveRealFile(Life4Cut life4Cut, MultipartFile file){

        StringBuilder filePathBuilder = new StringBuilder(basePath)
                .append(life4Cut.getSaveFilePath())
                .append(File.separator)
                .append(life4Cut.getSaveFileName());

        Path saveFilePath = Paths.get(filePathBuilder.toString());

        try {
            file.transferTo(saveFilePath);
        } catch (IOException e){

        } catch (IllegalStateException e){

        }

    }
}
