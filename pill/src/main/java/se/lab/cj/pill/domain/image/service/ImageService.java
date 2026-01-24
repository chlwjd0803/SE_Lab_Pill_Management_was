package se.lab.cj.pill.domain.image.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import se.lab.cj.pill.domain.combination.entity.Combination;
import se.lab.cj.pill.domain.combination.repository.CombinationRepository;
import se.lab.cj.pill.domain.image.command.ImageTreeResDto;
import se.lab.cj.pill.domain.image.entity.Image;
import se.lab.cj.pill.domain.image.repository.ImageRepository;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    private final ImageRepository imageRepository;
    private final CombinationRepository combinationRepository;

    @Transactional
    public void uploadImageSet(
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

        String relativePath = "/" + comb.getName() + "/";

        // 3. 이미지 객체 생성
        Image image = Image.builder()
                .combination(comb)
                .imgMaskedUrl(relativePath + maskFilaName)
                .imgProcessedUrl(relativePath + processedFilaName)
                .imgOriginUrl(relativePath + originFilaName)
                .name(comb.getName() + "-" + nextSequence)
                .worker(worker)
                .createdAt(LocalDateTime.now())
                .isDeleted(false)
                .build();

        imageRepository.save(image);

        // 4. 파일 물리 저장 시도
        try {
            saveFile(relativePath, maskFilaName, mask);
            saveFile(relativePath, processedFilaName, processed);
            saveFile(relativePath, originFilaName, origin);
        } catch (IOException e) {
            // 파일 저장 실패 시 생성했던 파일들 삭제 (Cleanup)
            deletePhysicalFiles(relativePath, maskFilaName, processedFilaName, originFilaName);
            // 런타임 예외를 던져 DB 트랜잭션 롤백 유도
            throw new RuntimeException("파일 저장 중 오류가 발생하여 롤백합니다.", e);
        }

        // 5. 저장 검증
        if (!new File(uploadDir + relativePath + maskFilaName).exists() ||
                !new File(uploadDir + relativePath + processedFilaName).exists() ||
                !new File(uploadDir + relativePath + originFilaName).exists()) {
            throw new RuntimeException("물리 파일 저장 검증 실패");
        }

    }

    private void saveFile(String path, String fileName, MultipartFile file) throws IOException {
        File directory = new File(uploadDir + path);
        if (!directory.exists()) directory.mkdirs(); // 날짜별 폴더 자동 생성

        File targetFile = new File(uploadDir + path + fileName);
        file.transferTo(targetFile); // 물리적 쓰기
    }

    private void deletePhysicalFiles(String path, String... fileNames) {
        for (String name : fileNames) {
            File file = new File(uploadDir + path + name);
            if (file.exists()) file.delete();
        }
    }

    @Transactional
    public void deleteImageSet(Long imageId) {
        // 1. 이미지 불러오기
        Image image = imageRepository.findById(imageId).orElseThrow(
                () -> new IllegalArgumentException("이미지가 없습니다.")
        );

        // 2. Soft Delete
        image.delete();
        imageRepository.save(image);

        // 3. 물리적 삭제
        try{
            deletePhysicalFile(uploadDir + image.getImgMaskedUrl());
            deletePhysicalFile(uploadDir + image.getImgProcessedUrl());
            deletePhysicalFile(uploadDir + image.getImgOriginUrl());
        } catch (Exception e){
            log.error("파일 삭제 중 오류 발생: {}", e.getMessage());
        }
    }

    private void deletePhysicalFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            if (!file.delete()) {
                throw new RuntimeException("파일 삭제 실패: " + filePath);
            }
        }
    }

    // 특정 조합 클릭 시 이미지(파일) 조회
    public List<ImageTreeResDto> getImageNodesByCombination(Long combinationId) {
        Combination comb = combinationRepository.findById(combinationId)
                .orElseThrow(() -> new IllegalArgumentException("조합이 없습니다."));

        return imageRepository.findAllByCombinationAndIsDeleted(comb, false).stream()
                .map(img -> ImageTreeResDto.builder()
                        .imageId(img.getImageId())
                        .title(extractFileName(img.getImgOriginUrl())) // 파일명 추출
                        .type("FILE")
                        .build())
                .collect(Collectors.toList());
    }

    private String extractFileName(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return "";
        }
        // 경로 구분자(/)의 마지막 위치를 찾아 그 이후의 문자열만 반환
        return filePath.substring(filePath.lastIndexOf("/") + 1);
    }


    public Resource getImageById(Long imageId) {
        Image image = imageRepository.findByImageIdAndIsDeleted(imageId, false)
                .orElseThrow(() -> new IllegalArgumentException("이미지가 없습니다."));

        try {
            // DB에 저장된 전체 경로를 기반으로 리소스 생성
            Path filePath = Paths.get(image.getImgOriginUrl());
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                return resource;
            } else {
                // 경로 수정해야함
                throw new RuntimeException("파일을 찾을 수 없습니다: " + image.getImgOriginUrl());
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("파일 경로가 잘못되었습니다.", e);
        }
    }
}
