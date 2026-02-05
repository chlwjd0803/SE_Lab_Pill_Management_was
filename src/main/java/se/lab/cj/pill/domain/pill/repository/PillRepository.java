package se.lab.cj.pill.domain.pill.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.lab.cj.pill.domain.pill.entity.Pill;

import java.util.Optional;

/**
 * 알약 JPA Repository
 *
 * 최초 작성자 : 최정
 */
@Repository
public interface PillRepository extends JpaRepository<Pill, Long> {

    // 알약번호 기반으로 찾는 메소드
    Optional<Pill> findByPillNumber(Integer pillNumber);
}
