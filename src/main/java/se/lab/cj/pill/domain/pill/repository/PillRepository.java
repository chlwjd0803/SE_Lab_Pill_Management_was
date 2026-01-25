package se.lab.cj.pill.domain.pill.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.lab.cj.pill.domain.pill.entity.Pill;

import java.util.Optional;

@Repository
public interface PillRepository extends JpaRepository<Pill, Long> {

    Optional<Pill> findByPillNumber(Integer pillNumber);
}
