package NestNet.NestNetWebSite.service.life4cut;

import NestNet.NestNetWebSite.api.ApiResult;
import NestNet.NestNetWebSite.domain.life4cut.Life4Cut;
import NestNet.NestNetWebSite.dto.response.Life4CutResponse;
import NestNet.NestNetWebSite.repository.life4cut.Life4CutRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class Life4CutService {

    private final Life4CutRepository life4CutRepository;

    /*
    인생네컷 저장
     */
    @Transactional
    public ApiResult<?> saveFile(MultipartFile file, HttpServletResponse response){

        Life4Cut life4Cut = new Life4Cut(file);
        boolean isSaved = life4CutRepository.save(life4Cut, file);

        if(isSaved){
            return ApiResult.success("인생네컷 저장 성공");
        }
        else{
            return ApiResult.error(response, HttpStatus.INTERNAL_SERVER_ERROR, "인생네컷 저장 실패");
        }
    }

    /*
    인생네컷 조회 (랜덤)
     */
    public ApiResult<?> findFileByRandom(int limit){

        List<Life4Cut> life4CutList = life4CutRepository.findAllByPaging(limit);
        List<Life4CutResponse> responseList = new ArrayList<>();

        for(Life4Cut life4Cut : life4CutList){
            responseList.add(new Life4CutResponse(life4Cut.getSaveFilePath(), life4Cut.getSaveFileName()));
        }

        return ApiResult.success(responseList);
    }
}
