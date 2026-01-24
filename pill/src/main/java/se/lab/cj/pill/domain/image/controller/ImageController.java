package se.lab.cj.pill.domain.image.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.lab.cj.pill.domain.image.service.ImageService;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;


    @GetMapping
    public ResponseEntity<?> getImageDirectory(){
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{imageId}")
    public ResponseEntity<?> getImageById(){
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<?> uploadImage(){
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<?> deleteImage(){
        return ResponseEntity.ok().build();
    }

}
