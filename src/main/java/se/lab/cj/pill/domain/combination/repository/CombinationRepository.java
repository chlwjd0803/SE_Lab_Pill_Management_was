package se.lab.cj.pill.domain.combination.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.lab.cj.pill.domain.combination.entity.Combination;

@Repository
public interface CombinationRepository extends JpaRepository<Combination, Long> {
}
