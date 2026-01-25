package se.lab.cj.pill.domain.pill.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.lab.cj.pill.domain.pill.api.PillApi;
import se.lab.cj.pill.domain.pill.service.PillService;

@RestController
@RequestMapping("/api/pills")
@RequiredArgsConstructor
public class PillController implements PillApi {

    private final PillService pillService;

    @GetMapping("/{pillNo}")
    public ResponseEntity<?> getPillSampleImage(
            @PathVariable Integer pillNo
    ){
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG)
                .body(pillService.getSampleImage(pillNo));
    }
}
