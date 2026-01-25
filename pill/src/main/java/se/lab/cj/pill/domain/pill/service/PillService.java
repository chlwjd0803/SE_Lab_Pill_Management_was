package se.lab.cj.pill.domain.pill.service;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.lab.cj.pill.domain.combination.entity.Combination;
import se.lab.cj.pill.domain.combination.repository.CombinationRepository;
import se.lab.cj.pill.domain.pill.command.SamplePillResDto;
import se.lab.cj.pill.domain.pill.entity.Pill;
import se.lab.cj.pill.domain.pill.repository.PillRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class PillService {

    @Value("${file.sample-dir}")
    private String sampleDir;

    private final PillRepository pillRepository;
    private final CombinationRepository combinationRepository;

    @Transactional(readOnly = true)
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
            Pill pill = pillRepository.findByPillNumber(Integer.parseInt(sampleNumber)).orElseThrow(
                    () -> new IllegalArgumentException("해당 알약샘플이 존재하지 않습니다.")
            );

            dtos.add(SamplePillResDto.builder()
                    .pillNumber(pill.getPillNumber())
                    .firstName(pill.getFirstName())
                    .lastName(pill.getLastName())
                    .sampleImageUrl(pill.getSampleImageUrl())
                    .build());
        }
        // 5. Pill 리스트 반환
        return dtos;
    }

    @Transactional(readOnly = true)
    public Resource getSampleImage(Integer pillNo){
//        Pill pill = pillRepository.findById(pillId).orElseThrow(
//                () -> new IllegalArgumentException("해당 알약샘플이 존재하지 않습니다.")
//        );

        Pill pill = pillRepository.findByPillNumber(pillNo).orElseThrow(
                () -> new IllegalArgumentException("해당 알약샘플이 존재하지 않습니다.")
        );

        try{
            Path filePath = Paths.get(sampleDir + pill.getSampleImageUrl());
            Resource resource = new UrlResource(filePath.toUri());

            if(resource.exists()){
                return resource;
            } else {
                throw new RuntimeException("파일을 찾을 수 없습니다: " + sampleDir + pill.getSampleImageUrl());
            }
        } catch (MalformedURLException e){
            throw new RuntimeException("파일 경로가 잘못되었습니다.", e);
        }
    }
}
