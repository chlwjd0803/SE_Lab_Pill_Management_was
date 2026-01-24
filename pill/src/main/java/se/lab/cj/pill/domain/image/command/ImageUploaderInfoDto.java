package se.lab.cj.pill.domain.image.command;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ImageUploaderInfoDto {

    private Long combinationId;

    private String worker;
}
