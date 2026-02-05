package se.lab.cj.pill.domain.image.command;

import lombok.Builder;
import lombok.Getter;

/**
 * 조합식에 관련된 이미지 정보 응답 DTO
 *
 * 최초 작성자 : 최정
 */
@Getter
@Builder
public class CombinationTreeResDto {

    // 조합식 주 키
    private Long combinationId;
    private String title;
    private String type;
    private Integer numberOfCappedImages;
}
