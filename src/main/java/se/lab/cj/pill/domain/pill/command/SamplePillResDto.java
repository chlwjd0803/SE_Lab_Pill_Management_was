package se.lab.cj.pill.domain.pill.command;

import lombok.Builder;
import lombok.Data;

/**
 * @author 최정
 * @since 2026.01.24
 *     since    |    author    | description
 *  2026.01.24  |     최정      | 최초 등록
 *
 * 알약 샘플 정보 응답 DTO
 */
@Data
@Builder
public class SamplePillResDto {

    // 알약 고유번호
    private Integer pillNumber;

    // 알약 앞이름
    private String firstName;

    // 알약 뒷이름
    private String lastName;

    // 알약 샘플 이미지
    private String sampleImageUrl;
}
