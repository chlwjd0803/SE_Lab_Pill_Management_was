package se.lab.cj.pill.domain.pill.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "Pill API", description = "알약 정보 관련 API 입니다.")
public interface PillApi {

    @Operation(summary = "알약 이미지 조회", description = "정의한 고유 알약 번호로 알약 사진을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공(사진)"),
            @ApiResponse(responseCode = "500", description = "모든 오류(4XX에러 포함)")
    })
    @GetMapping("/{pillNo}")
    public ResponseEntity<?> getPillSampleImage(
            @PathVariable Integer pillNo
    );
}
