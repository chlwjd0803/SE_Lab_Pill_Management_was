package se.lab.cj.pill.domain.image.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import se.lab.cj.pill.domain.combination.entity.Combination;
import java.time.LocalDateTime;


/**
 * @author 최정
 * @since 2026.01.24
 *     since    |    author    | description
 *  2026.01.24  |     최정      | 최초 등록
 *
 * 이미지 엔티티
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Image {

    // 주키
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageId;

    // 이미지를 촬영한 조합식
    @ManyToOne
    @JoinColumn(name = "combination_id", nullable = false)
    private Combination combination;

    // 이미지 이름
    @Column(nullable = false)
    private String name;

    // 생성일자
    @Column(nullable = false)
    private LocalDateTime createdAt;

    // 작성자(연관관계없이 바로 기입)
    @Column(nullable = false)
    private String worker;

    // 약봉지가 있는 이미지 상대경로
    @Column(nullable = false, unique = true)
    private String imgMaskedUrl;

    // 보정이 된 이미지 상대경로
    @Column(nullable = false, unique = true)
    private String imgProcessedUrl;

    // 처리가 전혀 되지않은 오리지널 이미지 상대경로
    @Column(nullable = false, unique = true)
    private String imgOriginUrl;

    // Soft Delete를 위한 필드
    @Column(nullable = false)
    private Boolean isDeleted;

    /**
     * 삭제 메소드
     *
     * isDeleted를 삭제처리한다.
     */
    public void delete(){
        isDeleted = true;
    }

}
