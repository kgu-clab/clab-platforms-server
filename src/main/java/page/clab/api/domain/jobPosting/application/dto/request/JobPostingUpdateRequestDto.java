package page.clab.api.domain.jobPosting.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import page.clab.api.domain.jobPosting.domain.CareerLevel;
import page.clab.api.domain.jobPosting.domain.EmploymentType;

@Getter
@Setter
public class JobPostingUpdateRequestDto {

    @Schema(description = "공고명", example = "[네이버웹툰] Analytics Engineer(경력)")
    private String title;

    @Schema(description = "경력 수준", example = "EXPERIENCED")
    private CareerLevel careerLevel;

    @Schema(description = "고용 형태", example = "FULL_TIME")
    private EmploymentType employmentType;

    @Schema(description = "기업명", example = "네이버")
    private String companyName;

    @Schema(description = "채용 기간", example = "2024.01.11 ~ 2024.01.28")
    private String recruitmentPeriod;

    @Schema(description = "채용 공고 URL", example = "https://recruit.navercorp.com/rcrt/view.do?annoId=30001804&sw=&subJobCdArr=1010001%2C1010002%2C1010003%2C1010004%2C1010005%2C1010006%2C1010007%2C1010008%2C1010020%2C1020001%2C1030001%2C1030002%2C1040001%2C1050001%2C1050002%2C1060001&sysCompanyCdArr=&empTypeCdArr=&entTypeCdArr=&workAreaCdArr=")
    private String jobPostingUrl;
}
