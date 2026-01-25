package se.lab.cj.pill.domain.image.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import se.lab.cj.pill.domain.image.api.ImageApi;
import se.lab.cj.pill.domain.image.service.ImageService;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController implements ImageApi {

    private final ImageService imageService;

    @GetMapping("/tree/{combinationId}")
    public ResponseEntity<?> getImageTree(
            @PathVariable Long combinationId
    ){
        return ResponseEntity.ok(imageService.getImageNodesByCombination(combinationId));
    }

    @GetMapping("/{imageId}")
    public ResponseEntity<?> getImageByIdAndType(
            @PathVariable Long imageId,
            @RequestParam String type
    ){
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG)
                .body(imageService.getImageByIdAndType(imageId, type));
    }

    @PostMapping
    public ResponseEntity<?> uploadImageSet(
            @RequestParam Long combinationId,
            @RequestParam String worker,
            @RequestPart MultipartFile mask,
            @RequestPart MultipartFile processed,
            @RequestPart MultipartFile origin
    ){

        imageService.uploadImageSet(combinationId, worker, mask, processed, origin);
        // 조합식 메타데이터 업데이트
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{imageId}")
    public ResponseEntity<?> deleteImage(
            @PathVariable Long imageId
    ){
        imageService.deleteImageSet(imageId);
        // 조합식 메타데이터 업데이트
        return ResponseEntity.ok().build();
    }

}
