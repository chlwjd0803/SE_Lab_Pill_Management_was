package se.lab.cj.pill.domain.pill.command;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SamplePillResDto {

    private Integer pillNumber;

    private String firstName;

    private String lastName;

    private String sampleImageUrl;
}
