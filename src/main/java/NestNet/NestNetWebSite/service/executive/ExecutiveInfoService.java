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
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ExecutiveInfoService {

    public ExecutiveInfoRepository executiveInfoRepository;

    /*
    임원 정보 저장
     */
    @Transactional
    public void saveExecutiveInfo(List<ExecutiveInfoRequest> executiveInfoRequestList){

        List<ExecutiveInfo> executiveInfoList = new ArrayList<>();

        for(ExecutiveInfoRequest executiveInfoRequest : executiveInfoRequestList){
            executiveInfoList.add(executiveInfoRequest.toEntity());
        }

        executiveInfoRepository.saveAll(executiveInfoList);
    }

    /*
    전 임원 정보 조회
     */
    public ApiResult<?> findPrevExecutiveList(){

        List<ExecutiveInfo> executiveInfoList = executiveInfoRepository.findPrevExecutiveInfo();

        List<ExecutiveInfoDto> dtoList = new ArrayList<>();

        for(ExecutiveInfo executiveInfo : executiveInfoList){
            dtoList.add(new ExecutiveInfoDto(executiveInfo.getId(), executiveInfo.getYear(),
                    executiveInfo.getName(), executiveInfo.getStudentId(), executiveInfo.getRole()));
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
                    executiveInfo.getName(), executiveInfo.getStudentId(), executiveInfo.getRole()));
        }

        return ApiResult.success(new ExecutiveInfoResponse(dtoList));
    }

    /*
    임원 정보 수정
     */
    public ApiResult<?> modifyExecutiveInfo(ExecutiveInfoModifyRequest executiveInfoRequest){

        ExecutiveInfo executiveInfo = executiveInfoRepository.findById(executiveInfoRequest.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.EXECUTIVE_INFO_NOT_FOUND));

        executiveInfo.modifyInfo(executiveInfoRequest.getYear(), executiveInfoRequest.getName(),
                executiveInfoRequest.getStudentId(), executiveInfoRequest.getRole());

        return ApiResult.success("임원 정보가 수정되었습니다.");
    }

    /*
    임원 정보 삭제
     */
    public ApiResult<?> deleteExecutiveInfo(Long id){

        ExecutiveInfo executiveInfo = executiveInfoRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.EXECUTIVE_INFO_NOT_FOUND));

        executiveInfoRepository.delete(executiveInfo);

        return ApiResult.success("임원 정보가 삭제되었습니다.");
    }


}
