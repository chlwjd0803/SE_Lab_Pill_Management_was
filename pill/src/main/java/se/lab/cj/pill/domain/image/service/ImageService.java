package se.lab.cj.pill.domain.image.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.lab.cj.pill.domain.image.repository.ImageRepository;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;

}
