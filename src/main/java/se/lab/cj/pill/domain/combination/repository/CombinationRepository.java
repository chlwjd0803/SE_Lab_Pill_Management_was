package se.lab.cj.pill.domain.combination.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.lab.cj.pill.domain.combination.entity.Combination;

/**
 * @author 최정
 * @since 2026.01.24
 *     since    |    author    | description
 *  2026.01.24  |     최정      | 최초 등록
 *
 * 조합식 JPA Repository
 */
public interface CombinationRepository extends JpaRepository<Combination, Long> {
}
