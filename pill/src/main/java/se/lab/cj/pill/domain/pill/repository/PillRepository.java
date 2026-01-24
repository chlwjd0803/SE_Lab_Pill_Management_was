package se.lab.cj.pill.domain.pill.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.lab.cj.pill.domain.pill.entity.Pill;

@Repository
public interface PillRepository extends JpaRepository<Pill, Long> {

    Pill findByPillNumber(Integer pillNumber);
}
