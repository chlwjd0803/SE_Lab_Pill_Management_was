package se.lab.cj.pill.domain.image.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TreeResDto {
    private Long key;
    private String title;
    private boolean isLeaf; // true면 파일(이미지), false면 무조건 폴더(조합)
    private String type;    // "DIRECTORY" 또는 "FILE"로 명시적 구분 추가
}
