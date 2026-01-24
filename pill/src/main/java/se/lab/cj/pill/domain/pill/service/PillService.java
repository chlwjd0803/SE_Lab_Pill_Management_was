package se.lab.cj.pill.domain.pill.service;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import se.lab.cj.pill.domain.combination.entity.Combination;
import se.lab.cj.pill.domain.combination.repository.CombinationRepository;
import se.lab.cj.pill.domain.pill.command.SamplePillResDto;
import se.lab.cj.pill.domain.pill.entity.Pill;
import se.lab.cj.pill.domain.pill.repository.PillRepository;

@Service
@RequiredArgsConstructor
public class PillService {

    @Value("${file.sample-dir}")
    private String sampleDir;

    private final PillRepository pillRepository;
    private final CombinationRepository combinationRepository;

    public List<SamplePillResDto> getCombinationPillSamples(Long combinationId) {
        // 1. 조합 검색
        Combination combination = combinationRepository.findById(combinationId).orElseThrow(
                () -> new IllegalArgumentException("조합이 없습니다.")
        );

        // 2. 조합 이름 추출
        String combinationName = combination.getName();
        StringTokenizer st = new StringTokenizer(combinationName);
        List<SamplePillResDto> dtos = new ArrayList<>();


        while(st.hasMoreTokens()){
            // 3. 샘플 이름에서 번호 추출
            String sampleName = st.nextToken();
            String sampleNumber = sampleName.replaceAll("\\D", "");

            // 4. 약 번호로 Pill검색
            Pill pill = pillRepository.findByPillNumber(Integer.parseInt(sampleNumber));

            if(pill != null) {
                dtos.add(SamplePillResDto.builder()
                        .pillNumber(pill.getPillNumber())
                        .firstName(pill.getFirstName())
                        .lastName(pill.getLastName())
                        .sampleImageUrl(pill.getSampleImageUrl())
                        .build());
            }
        }
        // 5. Pill 리스트 반환
        return dtos;
    }
}
