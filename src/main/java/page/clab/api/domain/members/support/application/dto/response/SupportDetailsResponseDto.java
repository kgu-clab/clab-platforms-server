package page.clab.api.domain.members.support.application.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import page.clab.api.global.common.file.dto.response.UploadedFileResponseDto;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@Builder
public class SupportDetailsResponseDto {

    private Long id;
    private String writerId;
    private String name;
    private String title;
    private String content;
    private List<UploadedFileResponseDto> uploadedFiles;
    private String category;
    private String status;
    AnswerResponseDto answer;
    @JsonProperty("isOwner")
    private Boolean isOwner;
    private LocalDateTime createdAt;
}
