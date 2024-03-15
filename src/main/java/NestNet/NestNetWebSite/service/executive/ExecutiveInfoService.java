package NestNet.NestNetWebSite.service.executive;

import NestNet.NestNetWebSite.api.ApiResult;
import NestNet.NestNetWebSite.domain.executive.ExecutiveInfo;
import NestNet.NestNetWebSite.dto.request.ExecutiveInfoModifyRequest;
import NestNet.NestNetWebSite.dto.request.ExecutiveInfoRequest;
import NestNet.NestNetWebSite.dto.response.ExecutiveInfoDto;
import NestNet.NestNetWebSite.dto.response.ExecutiveInfoResponse;
import NestNet.NestNetWebSite.exception.CustomException;
import NestNet.NestNetWebSite.exception.ErrorCode;
import NestNet.NestNetWebSite.repository.executive.ExecutiveInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ExecutiveInfoService {

    private final ExecutiveInfoRepository executiveInfoRepository;

    /*
    임원 정보 저장
     */
    @Transactional
    public ApiResult<?> saveExecutiveInfo(List<ExecutiveInfoRequest> executiveInfoRequestList){

        List<ExecutiveInfo> executiveInfoList = new ArrayList<>();

        Map<String, Integer> priorityMap = new HashMap<>();
        priorityMap.put("회장", 1);
        priorityMap.put("부회장", 2);
        priorityMap.put("총무", 3);
        priorityMap.put("서버", 4);
        priorityMap.put("기획", 5);
        priorityMap.put("홍보", 6);
        priorityMap.put("학술", 7);
        priorityMap.put("복지", 8);

        for(ExecutiveInfoRequest executiveInfoRequest : executiveInfoRequestList){
            ExecutiveInfo executiveInfo = executiveInfoRequest.toEntity();

            executiveInfo.setPriority(priorityMap.get(executiveInfo.getRole()));        // 직무에 맞는 우선순위 세팅
            executiveInfoList.add(executiveInfo);
        }

        executiveInfoRepository.saveAll(executiveInfoList);

        return ApiResult.success("임원 정보를 저장했습니다");
    }

    /*
    전 임원 정보 조회
     */
    public ApiResult<?> findPrevExecutiveList(){

        List<ExecutiveInfo> executiveInfoList = executiveInfoRepository.findPrevExecutiveInfo();

        List<ExecutiveInfoDto> dtoList = new ArrayList<>();

        for(ExecutiveInfo executiveInfo : executiveInfoList){
            dtoList.add(new ExecutiveInfoDto(executiveInfo.getId(), executiveInfo.getYear(),
                    executiveInfo.getName(), executiveInfo.getStudentId(), executiveInfo.getRole(), executiveInfo.getPriority()));
        }

        return ApiResult.success(new ExecutiveInfoResponse(dtoList));
    }

    /*
    현 임원 정보 조회
     */
    public ApiResult<?> findCurrentExecutiveList(){

        List<ExecutiveInfo> executiveInfoList = executiveInfoRepository.findCurrentExecutiveInfo();

        List<ExecutiveInfoDto> dtoList = new ArrayList<>();

        for(ExecutiveInfo executiveInfo : executiveInfoList){
            dtoList.add(new ExecutiveInfoDto(executiveInfo.getId(), executiveInfo.getYear(),
                    executiveInfo.getName(), executiveInfo.getStudentId(), executiveInfo.getRole(), executiveInfo.getPriority()));
        }

        return ApiResult.success(new ExecutiveInfoResponse(dtoList));
    }

    /*
    임원 정보 수정
     */
    @Transactional
    public ApiResult<?> modifyExecutiveInfo(ExecutiveInfoModifyRequest executiveInfoRequest){

        Map<String, Integer> priorityMap = new HashMap<>();
        priorityMap.put("회장", 1);
        priorityMap.put("부회장", 2);
        priorityMap.put("총무", 3);
        priorityMap.put("서버", 4);
        priorityMap.put("기획", 5);
        priorityMap.put("홍보", 6);
        priorityMap.put("학술", 7);
        priorityMap.put("복지", 8);

        ExecutiveInfo executiveInfo = executiveInfoRepository.findById(executiveInfoRequest.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.EXECUTIVE_INFO_NOT_FOUND));

        executiveInfo.modifyInfo(executiveInfoRequest.getYear(), executiveInfoRequest.getName(),
                executiveInfoRequest.getStudentId(), executiveInfoRequest.getRole(), priorityMap.get(executiveInfoRequest.getRole()));

        return ApiResult.success("임원 정보가 수정되었습니다.");
    }

    /*
    임원 정보 삭제
     */
    @Transactional
    public ApiResult<?> deleteExecutiveInfo(Long id){

        ExecutiveInfo executiveInfo = executiveInfoRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.EXECUTIVE_INFO_NOT_FOUND));

        executiveInfoRepository.delete(executiveInfo);

        return ApiResult.success("임원 정보가 삭제되었습니다.");
    }


}
