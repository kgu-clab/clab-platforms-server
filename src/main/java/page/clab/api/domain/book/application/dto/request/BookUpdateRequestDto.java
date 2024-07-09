package page.clab.api.domain.book.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BookUpdateRequestDto {

    @Schema(description = "카테고리", example = "IT/개발")
    private String category;

    @Schema(description = "제목", example = "스프링 부트와 AWS로 혼자 구현하는 웹 서비스")
    private String title;

    @Schema(description = "저자", example = "이동욱")
    private String author;

    @Schema(description = "출판사", example = "프리렉")
    private String publisher;

    @Schema(description = "이미지 URL", example = "https://shopping-phinf.pstatic.net/main_3243625/32436253723.20230928091945.jpg?type=w300")
    private String imageUrl;

    @Schema(description = "리뷰 링크", example = "[\"https://www.yes24.com/Product/Goods/7516911\",\"https://www.aladin.co.kr/shop/wproduct.aspx?ISBN=8960773433&start=pnaver_02\"]")
    private List<String> reviewLinks;
}
