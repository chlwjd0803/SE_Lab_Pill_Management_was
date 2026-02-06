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

/**
 * @author 최정
 * @since 2026.01.24
 *     since    |    author    | description
 *  2026.01.24  |     최정      | 최초 등록
 *
 * 알약 샘플 서비스
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PillService {

    // 절대경로 값(
    @Value("${file.sample-dir}")
    private String sampleDir;

    private final PillRepository pillRepository;
    private final CombinationRepository combinationRepository;

    /**
     * 조합식에 대한 알약 샘플 정보 가져오기
     *
     * @param combinationId 알약 주 키
     * @return 알약 샘플 DTO 리스트
     */
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

            // 5. DTO 리스트에 추가
            dtos.add(SamplePillResDto.builder()
                    .pillNumber(pill.getPillNumber())
                    .firstName(pill.getFirstName())
                    .lastName(pill.getLastName())
                    .sampleImageUrl(pill.getSampleImageUrl())
                    .build());
        }
        // 6. Pill 리스트 반환
        return dtos;
    }

    /**
     * 알약 샘플 이미지 단일 조회
     *
     * @param pillNo 알약 고유 번호
     * @return 이미지 자원
     */
    @Transactional(readOnly = true)
    public Resource getSampleImage(Integer pillNo){
//        Pill pill = pillRepository.findById(pillId).orElseThrow(
//                () -> new IllegalArgumentException("해당 알약샘플이 존재하지 않습니다.")
//        );

        // 1. 알약 조회
        Pill pill = pillRepository.findByPillNumber(pillNo).orElseThrow(
                () -> new IllegalArgumentException("해당 알약샘플이 존재하지 않습니다.")
        );

        try{
            // 2. 이미지 자원 가져오기
            Path filePath = Paths.get(sampleDir + pill.getSampleImageUrl());
            Resource resource = new UrlResource(filePath.toUri());

            // 3. 파일이 실제로 존재한다면 반환, 아니면 예외처리
            if(resource.exists()){
                return resource;
            } else {
                throw new RuntimeException("파일을 찾을 수 없습니다: " + sampleDir + pill.getSampleImageUrl());
            }
        } catch (MalformedURLException e){
            // 절대경로나 상대경로가 꼬이면 해당 에러가 발생합니다.
            // 또는 임의로 파일을 삭제했을때도 발생하니 주의 바랍니다.
            throw new RuntimeException("파일 경로가 잘못되었습니다.", e);
        }
    }
}
