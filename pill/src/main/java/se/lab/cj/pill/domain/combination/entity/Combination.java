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

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Combination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long combinationId;

    @Column(nullable = false, unique = true)
    private String name;

    @Column
    private Integer numberOfPills;

    @Column
    private Integer captureCount;

    @Column(nullable = false)
    private Boolean isCompleted;

    @Column(nullable = false)
    private String worker;

    @Column(nullable = false)
    private Boolean isDeleted;

    @OneToMany(mappedBy = "combination")
    private List<Image> images = new ArrayList<>();
}
