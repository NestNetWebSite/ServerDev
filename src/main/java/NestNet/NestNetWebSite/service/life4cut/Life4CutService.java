package NestNet.NestNetWebSite.service.life4cut;

import NestNet.NestNetWebSite.api.ApiResult;
import NestNet.NestNetWebSite.domain.attachedfile.AttachedFile;
import NestNet.NestNetWebSite.domain.life4cut.Life4Cut;
import NestNet.NestNetWebSite.dto.response.Life4CutResponse;
import NestNet.NestNetWebSite.repository.life4cut.Life4CutRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
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
    public ApiResult<?> findFileByPaging(int page, int size){

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));

        Page<Life4Cut> life4CutPage = life4CutRepository.findAll(pageRequest);
        List<Life4Cut> life4CutList = life4CutPage.getContent();
        List<Life4CutResponse> responseList = new ArrayList<>();

        for(Life4Cut life4Cut : life4CutList){
            responseList.add(new Life4CutResponse(life4Cut.getSaveFilePath(), life4Cut.getSaveFileName()));
        }

        return ApiResult.success(responseList);
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
