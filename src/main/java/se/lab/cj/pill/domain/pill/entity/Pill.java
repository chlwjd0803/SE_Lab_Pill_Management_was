package se.lab.cj.pill.domain.pill.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author 최정
 * @since 2026.01.24
 *     since    |    author    | description
 *  2026.01.24  |     최정      | 최초 등록
 *
 * 알약 엔티티
 */
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Pill {

    // 주키
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pillId;

    // 알약 고유 번호(정해진대로 가야함)
    @Column(nullable = false, unique = true)
    private Integer pillNumber;

    // 알약 앞 이름
    @Column
    private String firstName;

    // 알약 뒷 이름
    @Column
    private String lastName;

    // 알약 샘플 이미지 상대경로
    @Column
    private String sampleImageUrl;

    // Soft Delete를 위한 필드
    @Column
    private Boolean isDeleted;
}
