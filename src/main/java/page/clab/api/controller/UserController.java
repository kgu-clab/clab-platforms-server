package page.clab.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.service.UserService;
import page.clab.api.type.dto.ResponseModel;
import page.clab.api.type.dto.UserRequestDto;
import page.clab.api.type.dto.UserResponseDto;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User")
@Slf4j
public class UserController {

    private final UserService userService;

    @Operation(summary = "신규 유저 생성", description = "신규 유저 생성<br>" +
            "String id;<br>"+
            "String password;<br>" +
            "String name;<br>" +
            "String contact;<br>" +
            "String email;<br>" +
            "String department;<br>" +
            "Long grade;<br>" +
            "LocalDate birth; (ex: 2023-01-01)<br>" +
            "String address;<br>" +
            "Boolean isInSchool<br>")
    @PostMapping("")
    public ResponseModel createUser(
            @RequestBody UserRequestDto userRequestDto
    ) throws PermissionDeniedException {
        userService.createUser(userRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "유저 정보", description = "프로필 정보 조회")
    @GetMapping("")
    public ResponseModel getUsers() throws PermissionDeniedException {
        List<UserResponseDto> users = userService.getUsers();
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(users);
        return responseModel;
    }

    @Operation(summary = "유저 검색", description = "유저의 ID 또는 이름을 기반으로 검색")
    @GetMapping("/search")
    public ResponseModel searchUser(
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) String name
    ) throws PermissionDeniedException {
        UserResponseDto user = userService.searchUser(userId, name);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(user);
        return responseModel;
    }

    @Operation(summary = "유저 삭제(관리자 전용)", description = "관리자에 의한 유저 삭제(모든 계정 삭제 가능)")
    @DeleteMapping("/{userId}")
    public ResponseModel deleteUserByAdmin(
            @PathVariable("userId") String userId
    ) throws PermissionDeniedException {
        userService.deleteUserByAdmin(userId);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "유저 삭제(일반 유저 전용)", description = "본인 계정 삭제")
    @DeleteMapping("")
    public ResponseModel deleteUserByUser() {
        userService.deleteUserByUser();
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

}
