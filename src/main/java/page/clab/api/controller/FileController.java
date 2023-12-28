package page.clab.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.File;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import page.clab.api.exception.FileUploadFailException;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.service.FileService;
import page.clab.api.type.dto.ResponseModel;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
@Tag(name = "UploadedFile", description = "파일 업로드 관련 API")
@Slf4j
public class FileController {

    private final FileService fileService;

    @Operation(summary = "[U] 게시글 사진 업로드", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @PostMapping(value ="/boards/{boardId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseModel boardUpload(
            @PathVariable("boardId") String boardId,
            @RequestParam("multipartFile") List<MultipartFile> multipartFiles,
            @RequestParam("storagePeriod") long storagePeriod
    ) throws FileUploadFailException {
        List<String> url = fileService.saveFiles(multipartFiles, "boards" + File.separator + boardId, storagePeriod);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(url);
        return responseModel;
    }

    @Operation(summary = "[U] 뉴스 사진 업로드", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @Secured({"ROLE_ADMIN", "ROLE_SUPER"})
    @PostMapping(value ="/news/{newsId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseModel newsUpload(
            @PathVariable("newsId") String newsId,
            @RequestParam("multipartFile") List<MultipartFile> multipartFiles,
            @RequestParam("storagePeriod") long storagePeriod
    ) throws FileUploadFailException {
        List<String> url = fileService.saveFiles(multipartFiles, "news" + File.separator + newsId, storagePeriod);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(url);
        return responseModel;
    }

    @Operation(summary = "[U] 도서 사진 업로드", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @Secured({"ROLE_ADMIN", "ROLE_SUPER"})
    @PostMapping(value ="/books/{bookId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseModel bookUpload(
            @PathVariable("bookId") String bookId,
            @RequestParam("multipartFile") List<MultipartFile> multipartFiles,
            @RequestParam("storagePeriod") long storagePeriod
    ) throws FileUploadFailException {
        List<String> url = fileService.saveFiles(multipartFiles, "books" + File.separator + bookId, storagePeriod);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(url);
        return responseModel;
    }

    @Operation(summary = "[U] 멤버 프로필 사진 업로드", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @PostMapping(value ="/profiles/{memberId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseModel profileUpload(
            @PathVariable("memberId") String memberId,
            @RequestParam("multipartFile") MultipartFile multipartFile,
            @RequestParam("storagePeriod") long storagePeriod
    ) throws FileUploadFailException {
        String url = fileService.saveFile(multipartFile, "profiles" + File.separator + memberId, storagePeriod);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(url);
        return responseModel;
    }

    @Operation(summary = "[U] 함께하는 활동 사진 업로드", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @PostMapping(value = "/activity-photos/{activityPhotoId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseModel activityUpload(
            @PathVariable("activityPhotoId") String activityPhotoId,
            @RequestParam("multipartFile") List<MultipartFile> multipartFiles,
            @RequestParam("storagePeriod") long storagePeriod
    ) throws FileUploadFailException {
        List<String> url = fileService.saveFiles(multipartFiles, "activity-photos" + File.separator + activityPhotoId, storagePeriod);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(url);
        return responseModel;
    }

    @Operation(summary = "[U] 멤버 클라우드 파일 업로드", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @PostMapping(value ="/members/{memberId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseModel memberCloudUpload(
            @PathVariable("memberId") String memberId,
            @RequestParam("multipartFile") List<MultipartFile> multipartFiles,
            @RequestParam("storagePeriod") long storagePeriod
    ) throws FileUploadFailException {
        List<String> url = fileService.saveFiles(multipartFiles, "members" + File.separator + memberId, storagePeriod);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(url);
        return responseModel;
    }

    @Operation(summary = "[U] 양식 업로드", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @PostMapping(value ="/forms", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseModel formUpload(
            @RequestParam("multipartFile") List<MultipartFile> multipartFiles,
            @RequestParam("storagePeriod") long storagePeriod
    ) throws FileUploadFailException {
        List<String> url = fileService.saveFiles(multipartFiles, "forms", storagePeriod);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(url);
        return responseModel;
    }

    @Operation(summary = "[U] 파일 삭제", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @DeleteMapping("/{saveFileName}")
    public ResponseModel deleteFile(
            @PathVariable String saveFileName
    ) throws PermissionDeniedException {
        String url = fileService.deleteFile(saveFileName);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(url);
        return responseModel;
    }

}
