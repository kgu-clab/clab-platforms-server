package page.clab.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import page.clab.api.exception.FileUploadFailException;
import page.clab.api.service.FileUploadService;
import page.clab.api.type.dto.ResponseModel;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
@Tag(name = "File", description = "파일 업로드 관련 API")
@Slf4j
public class FileController {

    private final FileUploadService fileUploadService;

    @Operation(summary = "[U] 게시글 사진 업로드", description = "ROLE_USER 이상의 권한이 필요함")
    @PostMapping("/boards/{boardId}")
    public ResponseModel boardUpload(
            @PathVariable("boardId") String boardId,
            @RequestParam(value = "file") MultipartFile multipartFile
    ) throws FileUploadFailException {
        String url = fileUploadService.saveFile(multipartFile, "boards/" + boardId);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(url);
        return responseModel;
    }

    @Operation(summary = "[U] 뉴스 사진 업로드", description = "ROLE_USER 이상의 권한이 필요함")
    @PostMapping("/news/{newsId}")
    public ResponseModel newsUpload(
            @PathVariable("newsId") String newsId,
            @RequestParam(value = "file") MultipartFile multipartFile
    ) throws FileUploadFailException {
        String url = fileUploadService.saveFile(multipartFile, "news/" + newsId);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(url);
        return responseModel;
    }

    @Operation(summary = "[U] 도서 사진 업로드", description = "ROLE_USER 이상의 권한이 필요함")
    @PostMapping("/books/{bookId}")
    public ResponseModel bookUpload(
            @PathVariable("bookId") String bookId,
            @RequestParam(value = "file") MultipartFile multipartFile
    ) throws FileUploadFailException {
        String url = fileUploadService.saveFile(multipartFile, "books/" + bookId);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(url);
        return responseModel;
    }

    @Operation(summary = "[U] 멤버 프로필 사진 업로드", description = "ROLE_USER 이상의 권한이 필요함")
    @PostMapping("/profiles/{memberId}")
    public ResponseModel profileUpload(
            @PathVariable("memberId") String memberId,
            @RequestParam(value = "file") MultipartFile multipartFile
    ) throws FileUploadFailException {
        String url = fileUploadService.saveFile(multipartFile, "profiles/" + memberId);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(url);
        return responseModel;
    }

    @Operation(summary = "[U] 함께하는 활동 사진 업로드", description = "ROLE_USER 이상의 권한이 필요함")
    @PostMapping("/activity-photos/{activityPhotoId}")
    public ResponseModel activityUpload(
            @PathVariable("activityPhotoId") String activityPhotoId,
            @RequestParam(value = "file") MultipartFile multipartFile
    ) throws FileUploadFailException {
        String url = fileUploadService.saveFile(multipartFile, "activity-photos/" + activityPhotoId);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(url);
        return responseModel;
    }

    @Operation(summary = "[U] 멤버 클라우드 파일 업로드", description = "ROLE_USER 이상의 권한이 필요함")
    @PostMapping("/members/{memberId}")
    public ResponseModel memberCloudUpload(
            @PathVariable("memberId") String memberId,
            @RequestParam(value = "file") MultipartFile multipartFile
    ) throws FileUploadFailException {
        String url = fileUploadService.saveFile(multipartFile, "members/" + memberId);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(url);
        return responseModel;
    }

    @Operation(summary = "[U] 양식 업로드", description = "ROLE_USER 이상의 권한이 필요함")
    @PostMapping("/forms")
    public ResponseModel formUpload(
            @RequestParam(value = "file") MultipartFile multipartFile
    ) throws FileUploadFailException {
        String url = fileUploadService.saveFile(multipartFile, "forms");
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(url);
        return responseModel;
    }

}
