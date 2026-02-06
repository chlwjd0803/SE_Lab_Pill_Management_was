package se.lab.cj.pill.domain.combination.command;

import lombok.Builder;
import lombok.Getter;

/**
 * @author 최정
 * @since 2026.01.24
 *     since    |    author    | description
 *  2026.01.24  |     최정      | 최초 등록
 *
 * 조합식 트리 응답 DTO
 */
@Getter
@Builder
public class CombinationTreeResDto {

    // 조합식 주키
    private Long combinationId;

    // 조합식 이름 : "p_1 p_3 p_5"
    private String title;

    // 타입 -> 큰 의미는 없는 필드이고 그냥 DIRECTORY만 반환되게 설정하였습니다. 필요없는 필드이면 삭제 가능.
    private String type;

    // 캡처된 이미지 개수
    private Integer numberOfCappedImages;
}
