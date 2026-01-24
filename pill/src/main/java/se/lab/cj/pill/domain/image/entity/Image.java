package se.lab.cj.pill.domain.image.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import se.lab.cj.pill.domain.combination.entity.Combination;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageId;

    @ManyToOne
    @JoinColumn(name = "combination_id", nullable = false)
    private Combination combination;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private String worker;

    @Column(nullable = false)
    private String imgMaskedUrl;

    @Column(nullable = false)
    private String imgProcessedUrl;

    @Column(nullable = false)
    private String imgOriginUrl;

}
