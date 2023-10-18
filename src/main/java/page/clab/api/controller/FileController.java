package page.clab.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
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
@Tag(name = "File")
@Slf4j
public class FileController {

    private final FileUploadService fileUploadService;

    @Operation(summary = "게시글 사진 업로드", description = "게시글 사진 업로드")
    @PostMapping("/boards")
    public ResponseModel boardUpload(
            @RequestParam(value = "file", required = true) MultipartFile multipartFile
    ) throws FileUploadFailException {
        String url = fileUploadService.saveFile(multipartFile, "boards");
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(url);
        return responseModel;
    }

    @Operation(summary = "뉴스 사진 업로드", description = "뉴스 사진 업로드")
    @PostMapping("/news")
    public ResponseModel newsUpload(
            @RequestParam(value = "file", required = true) MultipartFile multipartFile
    ) throws FileUploadFailException {
        String url = fileUploadService.saveFile(multipartFile, "news");
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(url);
        return responseModel;
    }

    @Operation(summary = "도서 사진 업로드", description = "도서 사진 업로드")
    @PostMapping("/books")
    public ResponseModel bookUpload(
            @RequestParam(value = "file", required = true) MultipartFile multipartFile
    ) throws FileUploadFailException {
        String url = fileUploadService.saveFile(multipartFile, "books");
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(url);
        return responseModel;
    }

    @Operation(summary = "멤버 프로필 사진 업로드", description = "유저 프로필 사진 업로드")
    @PostMapping("/profiles")
    public ResponseModel profileUpload(
            @RequestParam(value = "file", required = true) MultipartFile multipartFile
    ) throws FileUploadFailException {
        String url = fileUploadService.saveFile(multipartFile, "profiles");
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(url);
        return responseModel;
    }

    @Operation(summary = "멤버 클라우드 파일 업로드", description = "유저 클라우드 파일 업로드")
    @PostMapping("/members/{memberId}")
    public ResponseModel memberCloudUpload(
            @PathVariable("memberId") String memberId,
            @RequestParam(value = "file", required = true) MultipartFile multipartFile
    ) throws FileUploadFailException {
        String url = fileUploadService.saveFile(multipartFile, "members/" + memberId);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(url);
        return responseModel;
    }

    @Operation(summary = "양식 업로드", description = "양식 업로드")
    @PostMapping("/forms")
    public ResponseModel formUpload(
            @RequestParam(value = "file", required = true) MultipartFile multipartFile
    ) throws FileUploadFailException {
        String url = fileUploadService.saveFile(multipartFile, "forms");
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(url);
        return responseModel;
    }

    @Operation(summary = "클라우드 업로드 파일 다운로드", description = "클라우드 업로드 파일 다운로드<br>" +
            "Base64 디코딩 후 Blob으로 변환하여 다운로드할 것")
    @GetMapping("/download/{memberId}/{fileName:.+}")
    public ResponseModel downloadCloudFile (
            @PathVariable("memberId") String memberId,
            @PathVariable("fileName") String fileName
    ) {
        String downloadFile = fileUploadService.downloadCloudFile(memberId, fileName);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(downloadFile);
        return responseModel;
    }

}
