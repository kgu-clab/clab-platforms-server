package page.clab.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.service.DonationService;
import page.clab.api.type.dto.DonationRequestDto;
import page.clab.api.type.dto.DonationResponseDto;
import page.clab.api.type.dto.DonationUpdateRequestDto;
import page.clab.api.type.dto.ResponseModel;

import java.util.List;

@RestController
@RequestMapping("/donations")
@RequiredArgsConstructor
@Tag(name = "Donation")
@Slf4j
public class DonationController {

    private final DonationService donationService;

    @Operation(summary = "후원 생성", description = "후원 생성<br>" +
            "String donorId;<br>"+
            "Double amount;<br>" +
            "String message;")
    @PostMapping("")
    public ResponseModel createDonation(
            @RequestBody DonationRequestDto donationRequestDto
    ) throws PermissionDeniedException {
        donationService.createDonation(donationRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "후원 정보", description = "후원 정보")
    @GetMapping("")
    public ResponseModel getDonations() {
        List<DonationResponseDto> donations = donationService.getDonations();
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(donations);
        return responseModel;
    }

    @Operation(summary = "후원 검색", description = "유저의 ID 또는 이름을 기반으로 검색")
    @GetMapping("/search")
    public ResponseModel getDonation(
            @RequestParam(required = false) String memberId,
            @RequestParam(required = false) String name
    ) {
        List<DonationResponseDto> donations = donationService.searchDonation(memberId, name);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(donations);
        return responseModel;
    }

    @Operation(summary = "후원 정보 수정", description = "후원 정보 수정<br>" +
            "Long id;<br>" +
            "String donorId;<br>"+
            "Double amount;<br>" +
            "String message;")
    @PatchMapping("")
    public ResponseModel updateDonation(
            @RequestBody DonationUpdateRequestDto donationUpdateRequestDto
    ) throws PermissionDeniedException {
        donationService.updateDonation(donationUpdateRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "후원 삭제", description = "후원 삭제")
    @DeleteMapping("/{donationId}")
    public ResponseModel deleteDonation(
            @PathVariable Long donationId
    ) {
        donationService.deleteDonation(donationId);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

}
