package se.lab.cj.pill.domain.pill.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Pill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pillId;

    @Column(nullable = false)
    private Integer pillNumber;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private String sampleImageUrl;
}
