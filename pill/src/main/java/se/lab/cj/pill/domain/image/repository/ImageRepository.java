package se.lab.cj.pill.domain.image.repository;

import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.lab.cj.pill.domain.combination.entity.Combination;
import se.lab.cj.pill.domain.image.entity.Image;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    Optional<Image> findByImageIdAndIsDeleted(Long imageId, Boolean isDeleted);

    // 조합 쌍의 Auto Increment 처럼 카운트 하기 위함, false 카운팅 포함하
    Long countByCombination(Combination combination);

    // 삭제표식이 없는 모든 조합
    List<Image> findAllByCombinationAndIsDeleted(Combination combination, Boolean isDeleted);
}
