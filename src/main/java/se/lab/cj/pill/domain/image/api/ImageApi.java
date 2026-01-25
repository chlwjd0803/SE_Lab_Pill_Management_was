package se.lab.cj.pill.domain.image.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Image API", description = "촬영 관련 주요 API 입니다.")
public interface ImageApi {

    @Operation(summary = "조합식 전체(트리) 조회", description = "조합식과 연관된 정보들을 가져옵니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "업로드 성공",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                    [
                                         {
                                             "imageId": 1,
                                             "title": "1-p_2 p_5 p_17 p_19-origin.jpg",
                                             "type": "FILE"
                                         }
                                    ]
                                    """)
                    })),
            @ApiResponse(responseCode = "500", description = "모든 오류(4XX에러 포함)")
    })
    @GetMapping("/tree/{combinationId}")
    public ResponseEntity<?> getImageTree(
            @PathVariable Long combinationId
    );


    @Operation(summary = "이미지 조회", description = "저장된 촬영 이미지를 조회합니다. " +
            "이미지 자체의 주키와 type에는 origin(원본), processed(보정처리), mask(약봉지)를 파라미터로 주입합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공 (사진정보)"),
            @ApiResponse(responseCode = "500", description = "모든 오류(4XX에러 포함)")
    })
    @GetMapping("/{imageId}")
    public ResponseEntity<?> getImageByIdAndType(
            @PathVariable Long imageId,
            @RequestParam String type
    );

    @Operation(summary = "이미지 업로드", description = "촬영된 3개의 이미지를 업로드 합니다. " +
            "이미지는 MediaType.IMAGE_JPEG 와 같이 jpg, jpeg 확장자로 전송하시면 됩니다. api 테스트 시 postman 사용 바랍니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "업로드 성공"),
            @ApiResponse(responseCode = "500", description = "모든 오류(4XX에러 포함)")
    })
    @PostMapping
    public ResponseEntity<?> uploadImageSet(
            @RequestParam Long combinationId,
            @RequestParam String worker,
            @RequestPart MultipartFile mask,
            @RequestPart MultipartFile processed,
            @RequestPart MultipartFile origin
    );

    @Operation(summary = "이미지 삭제", description = "촬영된 3개의 이미지를 삭제 합니다. " +
            "DB에서는 soft delete 처리되며 실제 파일 시스템에서 삭제하도록 구현되어있습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "삭제 성공"),
            @ApiResponse(responseCode = "500", description = "모든 오류(4XX에러 포함)")
    })
    @DeleteMapping("/{imageId}")
    public ResponseEntity<?> deleteImage(
            @PathVariable Long imageId
    );
}
