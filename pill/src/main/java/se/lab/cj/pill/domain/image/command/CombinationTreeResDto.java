package se.lab.cj.pill.domain.image.command;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CombinationTreeResDto {
    private Long combinationId;
    private String title;
    private String type;
    private Integer numberOfCappedImages;
}
