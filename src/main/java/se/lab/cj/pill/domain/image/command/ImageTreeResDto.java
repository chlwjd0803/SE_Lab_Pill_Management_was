package se.lab.cj.pill.domain.image.command;

import lombok.Builder;
import lombok.Getter;

/**
 * @author 최정
 * @since 2026.01.24
 *     since    |    author    | description
 *  2026.01.24  |     최정      | 최초 등록
 *
 * 이미지 트리 응답 DTO
 */
@Getter
@Builder
public class ImageTreeResDto {

    // 이미지 주키
    private Long imageId;

    // 약봉지 이미지 이름과 확장자 (예시 : maskedImage.jpg)
    private String maskImageUrl;

    // 보정된 이미지 이름과 확장자
    private String processedImageUrl;

    // 원본 이미지 이름과 확장자
    private String originImageUrl;

    // 타입 -> 큰 의미는 없는 필드이고 그냥 FILE로만 반환되게 설정하였습니다. 필요없는 필드이면 삭제 가능.
    private String type;
}
