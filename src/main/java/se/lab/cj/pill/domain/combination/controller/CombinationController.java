package se.lab.cj.pill.domain.combination.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.lab.cj.pill.domain.combination.api.CombinationApi;
import se.lab.cj.pill.domain.combination.service.CombinationService;
import se.lab.cj.pill.domain.pill.service.PillService;

@RestController
@RequestMapping("/api/combinations")
@RequiredArgsConstructor
public class CombinationController implements CombinationApi {

    private final CombinationService combinationService;
    private final PillService pillService;

    @GetMapping("/tree")
    public ResponseEntity<?> getCombinationTree(){
        return ResponseEntity.ok().body(combinationService.getRootCombinationNodes());
    }

    @GetMapping("/{combinationId}/pillSample")
    public ResponseEntity<?> getCombinationPillSamples(
            @PathVariable Long combinationId
    ){
        return ResponseEntity.ok(pillService.getCombinationPillSamples(combinationId));
    }

//    @GetMapping("/{combinationId}/captureCount")
//    public ResponseEntity<?> getCombinationCaptureCount(
//            @PathVariable Long combinationId
//    ){
//        return ResponseEntity.ok()
//                .body(combinationService.getCombinationCaptureCount(combinationId));
//    }

}
