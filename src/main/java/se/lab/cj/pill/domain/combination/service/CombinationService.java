package se.lab.cj.pill.domain.combination.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.lab.cj.pill.domain.combination.repository.CombinationRepository;
import se.lab.cj.pill.domain.combination.command.CombinationTreeResDto;
import se.lab.cj.pill.domain.image.repository.ImageRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 최정
 * @since 2026.01.24
 *     since    |    author    | description
 *  2026.01.24  |     최정      | 최초 등록
 *
 * 조합식 서비스
 */
@Service
@RequiredArgsConstructor
public class CombinationService {

    private final CombinationRepository combinationRepository;
    private final ImageRepository imageRepository;

    /**
     * @author 최정
     *
     * 조합식 트리(디렉토리 형태) 조회
     *
     * @return 모든 조합식 정보
     */
    @Transactional(readOnly = true)
    public List<CombinationTreeResDto> getRootCombinationNodes() {
        return combinationRepository.findAll().stream()
                .map(comb -> CombinationTreeResDto.builder()
                        .combinationId(comb.getCombinationId())
                        .title(comb.getName())
                        .type("DIRECTORY")
                        .numberOfCappedImages(
                                getCombinationCaptureCount(comb.getCombinationId())
                        )
                        .build())
                .collect(Collectors.toList());
    }

    // 조합에 찍힌 세트의 개수

    /**
     * @author 최정
     *
     * 촬영 개수 조회
     *
     * @param combinationId 조합식 주키
     * @return 촬영횟수
     */
    @Transactional(readOnly = true)
    public Integer getCombinationCaptureCount(Long combinationId) {

        // 1. 조합식을 찾는다
        // 2. 찾은 조합식으로 이미지 카운팅을 하고 응답.
        return imageRepository.countByCombinationAndIsDeleted(
                combinationRepository.findById(combinationId).orElseThrow(
                        () -> new IllegalArgumentException("조합이 없습니다.")
                ),
                false
        );
    }
}
