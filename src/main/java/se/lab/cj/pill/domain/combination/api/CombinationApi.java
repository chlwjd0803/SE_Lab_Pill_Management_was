package se.lab.cj.pill.domain.combination.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "Combination API", description = "조합식 API입니다.")
public interface CombinationApi {

    @Operation(summary = "조합식 전체(트리) 조회", description = "조합식과 연관된 정보들을 가져옵니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "업로드 성공",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                    [
                                            {
                                                "combinationId": 1,
                                                "numberOfCappedImages": 1,
                                                "title": "p_2 p_5 p_17 p_19",
                                                "type": "DIRECTORY"
                                            },
                                            {
                                                "combinationId": 2,
                                                "numberOfCappedImages": 0,
                                                "title": "p_6",
                                                "type": "DIRECTORY"
                                            },
                                            {
                                                "combinationId": 3,
                                                "numberOfCappedImages": 0,
                                                "title": "p_7",
                                                "type": "DIRECTORY"
                                            }
                                    ]
                                    """)
                    })),
            @ApiResponse(responseCode = "500", description = "모든 오류(4XX에러 포함)")
    })
    @GetMapping("/tree")
    public ResponseEntity<?> getCombinationTree();


    @Operation(summary = "조합식 알약 샘플 정보", description = "조합식에 포함된 알약 샘플 정보들을 가져옵니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                    [
                                        {
                                            "firstName": "SK",
                                            "lastName": "J200",
                                            "pillNumber": 2,
                                            "sampleImageUrl": "/p_2.png"
                                        },
                                        {
                                            "firstName": "아서틸정4mg",
                                            "lastName": "한국세르비에",
                                            "pillNumber": 10,
                                            "sampleImageUrl": "/p_10.png"
                                        },
                                        {
                                            "firstName": "Bayer",
                                            "lastName": "",
                                            "pillNumber": 12,
                                            "sampleImageUrl": "/p_12.png"
                                        }
                                    ]
                                    
                                    """)
                    })),
            @ApiResponse(responseCode = "500", description = "모든 오류(4XX에러 포함)")
    })
    @GetMapping("/{combinationId}/pillSample")
    public ResponseEntity<?> getCombinationPillSamples(
            @PathVariable Long combinationId
    );
}
