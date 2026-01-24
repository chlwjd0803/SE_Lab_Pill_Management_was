package se.lab.cj.pill.domain.image.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import se.lab.cj.pill.domain.combination.entity.Combination;
import se.lab.cj.pill.domain.combination.repository.CombinationRepository;
import se.lab.cj.pill.domain.image.command.ImageUploaderInfoDto;
import se.lab.cj.pill.domain.image.entity.Image;
import se.lab.cj.pill.domain.image.repository.ImageRepository;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ImageService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    private final ImageRepository imageRepository;
    private final CombinationRepository combinationRepository;

    @Transactional
    public Image uploadImageSet(
            Long combinationId,
            String worker,
            MultipartFile mask,
            MultipartFile processed,
            MultipartFile origin
    ){
        // 1. 조합 식 이름 불러오기
        Combination comb = combinationRepository.findById(combinationId).orElseThrow(
                () -> new IllegalArgumentException("요청한 조합식이 존재하지 않습니다")
        );

        // 2. 파일 명 생성
        Long currentCount = imageRepository.countByCombination(comb);
        Long nextSequence = currentCount + 1;

        String maskFilaName = nextSequence + "-" + comb.getName() + "-mask.jpg";
        String processedFilaName = nextSequence + "-" + comb.getName() + "-processed.jpg";
        String originFilaName = nextSequence + "-" + comb.getName() + "-origin.jpg";

        String fullPath = uploadDir + comb.getName() + "/";

        // 3. 이미지 객체 생성
        Image image = Image.builder()
                .combination(comb)
                .imgMaskedUrl(uploadDir + comb.getName() + "/" + maskFilaName)
                .imgProcessedUrl(uploadDir + comb.getName() + "/" + processedFilaName)
                .imgOriginUrl(uploadDir + comb.getName() + "/" + originFilaName)
                .name(comb.getName() + "-" + nextSequence)
                .worker(worker)
                .createdAt(LocalDateTime.now())
                .isDeleted(false)
                .build();

        imageRepository.save(image);

        // 4. 파일 물리 저장 시도
        try {
            saveFile(fullPath, maskFilaName, mask);
            saveFile(fullPath, processedFilaName, processed);
            saveFile(fullPath, originFilaName, origin);
        } catch (IOException e) {
            // 파일 저장 실패 시 생성했던 파일들 삭제 (Cleanup)
            deletePhysicalFiles(fullPath, maskFilaName, processedFilaName, originFilaName);
            // 런타임 예외를 던져 DB 트랜잭션 롤백 유도
            throw new RuntimeException("파일 저장 중 오류가 발생하여 롤백합니다.", e);
        }

        // 5. 저장 검증
        if (!new File(fullPath + maskFilaName).exists() ||
                !new File(fullPath + processedFilaName).exists() ||
                !new File(fullPath + originFilaName).exists()) {
            throw new RuntimeException("물리 파일 저장 검증 실패");
        }

        return image;
    }

    private void saveFile(String path, String fileName, MultipartFile file) throws IOException {
        File directory = new File(path);
        if (!directory.exists()) directory.mkdirs(); // 날짜별 폴더 자동 생성

        File targetFile = new File(path + fileName);
        file.transferTo(targetFile); // 물리적 쓰기
    }

    private void deletePhysicalFiles(String path, String... fileNames) {
        for (String name : fileNames) {
            File file = new File(path + name);
            if (file.exists()) file.delete();
        }
    }
}
