package se.lab.cj.pill.domain.combination.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.lab.cj.pill.domain.combination.repository.CombinationRepository;
import se.lab.cj.pill.domain.image.dto.TreeResDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CombinationService {

    private final CombinationRepository combinationRepository;

    // 조합 리스트(폴더) 조회
    public List<TreeResDto> getRootCombinationNodes() {
        return combinationRepository.findAll().stream()
                .map(comb -> TreeResDto.builder()
                        .key(comb.getCombinationId())
                        .title(comb.getName())
                        .isLeaf(false) // 자식이 0개여도 폴더이므로 false
                        .type("DIRECTORY")
                        .build())
                .collect(Collectors.toList());
    }
}
