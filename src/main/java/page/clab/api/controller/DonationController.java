package page.clab.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.service.DonationService;
import page.clab.api.type.dto.DonationRequestDto;
import page.clab.api.type.dto.DonationResponseDto;
import page.clab.api.type.dto.PagedResponseDto;
import page.clab.api.type.dto.ResponseModel;

@RestController
@RequestMapping("/donations")
@RequiredArgsConstructor
@Tag(name = "Donation", description = "후원 관련 API")
@Slf4j
public class DonationController {

    private final DonationService donationService;

    @Operation(summary = "[U] 후원 생성", description = "ROLE_USER 이상의 권한이 필요함")
    @PostMapping("")
    public ResponseModel createDonation(
            @Valid @RequestBody DonationRequestDto donationRequestDto,
            BindingResult result
    ) throws MethodArgumentNotValidException {
        if (result.hasErrors()) {
            throw new MethodArgumentNotValidException(null, result);
        }
        Long id = donationService.createDonation(donationRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

    @Operation(summary = "[U] 후원 정보", description = "ROLE_USER 이상의 권한이 필요함")
    @GetMapping("")
    public ResponseModel getDonations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<DonationResponseDto> donations = donationService.getDonations(pageable);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(donations);
        return responseModel;
    }

    @Operation(summary = "[U] 나의 후원 정보", description = "ROLE_USER 이상의 권한이 필요함")
    @GetMapping("/my-donations")
    public ResponseModel getMyDonations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<DonationResponseDto> donations = donationService.getMyDonations(pageable);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(donations);
        return responseModel;
    }

    @Operation(summary = "[U] 후원 검색", description = "ROLE_USER 이상의 권한이 필요함<br>" +
            "멤버 ID, 이름을 기준으로 검색")
    @GetMapping("/search")
    public ResponseModel getDonation(
            @RequestParam(required = false) String memberId,
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<DonationResponseDto> donations = donationService.searchDonation(memberId, name, pageable);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(donations);
        return responseModel;
    }

    @Operation(summary = "[U] 후원 정보 수정", description = "ROLE_USER 이상의 권한이 필요함")
    @PatchMapping("/{donationId}")
    public ResponseModel updateDonation(
            @PathVariable Long donationId,
            @Valid @RequestBody DonationRequestDto donationRequestDto,
            BindingResult result
    ) throws MethodArgumentNotValidException, PermissionDeniedException {
        if (result.hasErrors()) {
            throw new MethodArgumentNotValidException(null, result);
        }
        Long id = donationService.updateDonation(donationId, donationRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

    @Operation(summary = "[U] 후원 삭제", description = "ROLE_USER 이상의 권한이 필요함")
    @DeleteMapping("/{donationId}")
    public ResponseModel deleteDonation(
            @PathVariable Long donationId
    ) throws PermissionDeniedException {
        Long id = donationService.deleteDonation(donationId);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

}