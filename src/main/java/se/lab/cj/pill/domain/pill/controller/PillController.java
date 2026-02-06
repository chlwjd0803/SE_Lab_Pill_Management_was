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

/**
 * @author 최정
 * @since 2026.01.24
 *     since    |    author    | description
 *  2026.01.24  |     최정      | 최초 등록
 *
 * 알약 컨트롤러
 *
 * 해당 클래스에 대한 명세서는 PillApi 인터페이스에 작성되어있으므로 생략합니다.
 */
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
