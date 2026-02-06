package se.lab.cj.pill.domain.pill.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.lab.cj.pill.domain.pill.entity.Pill;

import java.util.Optional;

/**
 * @author 최정
 * @since 2026.01.24
 *     since    |    author    | description
 *  2026.01.24  |     최정      | 최초 등록
 *
 * 알약 JPA Repository
 */
public interface PillRepository extends JpaRepository<Pill, Long> {

    // 알약번호 기반으로 찾는 메소드
    Optional<Pill> findByPillNumber(Integer pillNumber);
}
