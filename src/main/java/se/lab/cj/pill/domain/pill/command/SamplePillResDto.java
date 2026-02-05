package se.lab.cj.pill.domain.pill.command;

import lombok.Builder;
import lombok.Data;

/**
 * 샘플 알약 정보 응답 DTO
 *
 * 최초 작성자 : 최정
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
