package NestNet.NestNetWebSite.controller.manager;

import NestNet.NestNetWebSite.api.ApiResult;
import NestNet.NestNetWebSite.dto.request.MemberSignUpManagementRequestDto;
import NestNet.NestNetWebSite.service.manager.ManagerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ManagerController {

    private final ManagerService managerService;

    @PostMapping("manager/approve-signup")
    public ApiResult<?> approveSignUpMember(@Valid @RequestBody MemberSignUpManagementRequestDto dto){
        return managerService.approveSignUp(dto);
    }

}
