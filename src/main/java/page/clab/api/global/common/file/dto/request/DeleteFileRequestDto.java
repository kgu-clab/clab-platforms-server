package page.clab.api.global.common.file.dto.request;

import io.netty.channel.ChannelHandler;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeleteFileRequestDto {

    @NotNull(message = "{notNull.file.url}")
    @Schema(description = "파일경로", example = "/resources/files/forms/123456.png", required = true)
    private String url;

}
