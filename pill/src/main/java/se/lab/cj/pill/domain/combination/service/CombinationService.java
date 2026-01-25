package se.lab.cj.pill.domain.combination.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.lab.cj.pill.domain.combination.repository.CombinationRepository;
import se.lab.cj.pill.domain.image.command.CombinationTreeResDto;
import se.lab.cj.pill.domain.image.repository.ImageRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CombinationService {

    private final CombinationRepository combinationRepository;
    private final ImageRepository imageRepository;

    // 조합 리스트(폴더) 조회
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
    @Transactional(readOnly = true)
    public Integer getCombinationCaptureCount(Long combinationId) {
        return imageRepository.countByCombinationAndIsDeleted(
                combinationRepository.findById(combinationId).orElseThrow(
                        () -> new IllegalArgumentException("조합이 없습니다.")
                ),
                false
        );
    }
}
