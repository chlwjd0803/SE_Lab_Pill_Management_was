package se.lab.cj.pill.domain.image.command;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ImageTreeResDto {
    private Long imageId;
    private String title;
    private String type;
}
