package se.lab.cj.pill.domain.image.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.lab.cj.pill.domain.image.entity.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

}
