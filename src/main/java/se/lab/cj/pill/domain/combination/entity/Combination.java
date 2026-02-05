package se.lab.cj.pill.domain.combination.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import se.lab.cj.pill.domain.image.entity.Image;

import java.util.ArrayList;
import java.util.List;


/**
 * 조합식 엔티티
 *
 * 명목상 필드는 현재 실제로 값이 오가는 필드가 아니며 이후에 필요할 시 연산에 쓰시면 됩니다.
 *
 * 최초 생성자 : 최정
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Combination {

    // 주키
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long combinationId;

    // 조합식 이름
    @Column(nullable = false, unique = true)
    private String name;

    // 알약의 개수
    @Column
    private Integer numberOfPills;

    // 캡처횟수(명목상 필드)
    @Column
    private Integer captureCount;

    // 완료여부(명목상 필드)
    @Column(nullable = false)
    private Boolean isCompleted;

    // 작업자(명목상 필드)
    @Column(nullable = false)
    private String worker;

    // Soft Delete를 위한 필드
    @Column(nullable = false)
    private Boolean isDeleted;

    // 관련 이미지들 불러올때 쓰기 위함
    @OneToMany(mappedBy = "combination")
    private List<Image> images = new ArrayList<>();
}
