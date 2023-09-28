package page.clab.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public ResponseModel boardsUpload(
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
    public ResponseModel booksUpload(
            @RequestParam(value = "file", required = true) MultipartFile multipartFile
    ) throws FileUploadFailException {
        String url = fileUploadService.saveFile(multipartFile, "books");
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(url);
        return responseModel;
    }

}
