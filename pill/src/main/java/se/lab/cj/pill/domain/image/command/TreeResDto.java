package se.lab.cj.pill.domain.image.command;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TreeResDto {
    private Long key;
    private String title;
    private boolean isLeaf;
    private String type;
}
