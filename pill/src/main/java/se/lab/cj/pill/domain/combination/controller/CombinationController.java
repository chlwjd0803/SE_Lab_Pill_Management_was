package se.lab.cj.pill.domain.combination.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.lab.cj.pill.domain.combination.service.CombinationService;

@RestController
@RequestMapping("/api/combinations")
@RequiredArgsConstructor
public class CombinationController {

    private final CombinationService combinationService;

    @GetMapping
    public ResponseEntity<?> getCombinationTree(){
        return ResponseEntity.ok().body(combinationService.getRootCombinationNodes());
    }

}
