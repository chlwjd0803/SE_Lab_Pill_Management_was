package se.lab.cj.pill.domain.image.command;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ImageTreeResDto {
    private Long imageId;
    private String maskImageUrl;
    private String processedImageUrl;
    private String originImageUrl;
    private String type;
}
