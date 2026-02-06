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
import se.lab.cj.pill.domain.image.api.ImageApi;
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

/**
 * @author 최정
 * @since 2026.01.24
 *     since    |    author    | description
 *  2026.01.24  |     최정      | 최초 등록
 *
 * 이미지 서비스
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ImageService {

    // 이미지파일 업로드 절대경로
    @Value("${file.upload-dir}")
    private String uploadDir;

    private final ImageRepository imageRepository;
    private final CombinationRepository combinationRepository;

    /**
     * @author 최정
     * 3개의 이미지 한 세트 업로드
     *
     * @param combinationId 조합식 주키
     * @param worker 작업자 이름
     * @param mask 약봉지 있는 사진
     * @param processed 보정된 사진
     * @param origin 보정되지 않은 사진
     */
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

        // 각각 파일 이름 지정
        String maskFilaName = nextSequence + "-" + comb.getName() + "-mask.jpg";
        String processedFilaName = nextSequence + "-" + comb.getName() + "-processed.jpg";
        String originFilaName = nextSequence + "-" + comb.getName() + "-origin.jpg";

        // 상대주소
        String relativePath = "/" + comb.getName() + "/";

        // 3. 이미지 객체 생성
        Image image = Image.builder()
                .combination(comb) // 조합식 등록
                .imgMaskedUrl(relativePath + maskFilaName) // 파일 상대경로 등록
                .imgProcessedUrl(relativePath + processedFilaName)
                .imgOriginUrl(relativePath + originFilaName)
                .name(comb.getName() + "-" + nextSequence) // 이미지 이름
                .worker(worker) // 작업자
                .createdAt(LocalDateTime.now()) // 촬영 시간
                .isDeleted(false) // Soft Delete 필드 false 세팅
                .build();

        imageRepository.save(image); // 이미지 엔티티 저장

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

    /**
     * @author 최정
     * 파일 물리적 저장
     *
     * @param path 상대 경로
     * @param fileName 파일 이름
     * @param file 저장할 파일
     * @throws IOException 파일시스템 접근에 대한 예외처리
     */
    private void saveFile(String path, String fileName, MultipartFile file) throws IOException {
        // 절대경로 + 상대경로로 지정
        File directory = new File(uploadDir + path);
        if (!directory.exists()) directory.mkdirs(); // 날짜별 폴더 자동 생성

        // 해당 경로의 파일
        File targetFile = new File(uploadDir + path + fileName);
        file.transferTo(targetFile); // 물리적 쓰기
    }

    /**
     * @author 최정
     * 파일 물리적 삭제, 수동 트랜잭션 전용
     *
     * @param path 상대경로
     * @param fileNames 파일 이름 배열
     */
    private void deletePhysicalFiles(String path, String... fileNames) {
        // 저장했던 파일 회수 -> 트랜잭션 범위에 포함되지 않아 반드시 삭제작업을 해줘야함
        for (String name : fileNames) {
            File file = new File(uploadDir + path + name);
            if (file.exists()) file.delete();
        }
    }

    /**
     * @author 최정
     * 이미지 세트 정상 삭제
     *
     * @param imageId 이미지 주키
     */
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

        // 4. 삭제 검증 후 롤백은 프로그램의 요구사항에 따라 추가 필요
    }

    /**
     * @author 최정
     * 단일 이미지 물리적 삭제
     *
     * @param filePath 이미지 상대경로
     */
    private void deletePhysicalFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            if (!file.delete()) {
                throw new RuntimeException("파일 삭제 실패: " + filePath);
            }
        }
    }


    /**
     * @author 최정
     * 특정 조합 클릭 시 이미지 정보 조회
     *
     * @param combinationId 조합식 주키
     * @return 이미지 정보 리스트
     */
    @Transactional(readOnly = true)
    public List<ImageTreeResDto> getImageNodesByCombination(Long combinationId) {

        // 1. 조합식 조회
        Combination comb = combinationRepository.findById(combinationId)
                .orElseThrow(() -> new IllegalArgumentException("조합이 없습니다."));

        // 2. 리스트 반환
        return imageRepository.findAllByCombinationAndIsDeleted(comb, false).stream()
                .map(img -> ImageTreeResDto.builder()
                        .imageId(img.getImageId())
                        // 실제 파일 이미지 이름과 확장자만 가져오도록, 모든경로가 포함되면 트리에서 표기가 힘들어짐을 고려함.
                        .maskImageUrl(img.getImgMaskedUrl().substring(img.getImgMaskedUrl().lastIndexOf("/") + 1))
                        .processedImageUrl(img.getImgProcessedUrl().substring(img.getImgProcessedUrl().lastIndexOf("/") + 1))
                        .originImageUrl(img.getImgOriginUrl().substring(img.getImgOriginUrl().lastIndexOf("/") + 1))
                        .type("FILE")
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * @author 최정
     * 이미지 이름 추출
     *
     * @param filePath 이미지 상대경로
     * @return
     */
    private String extractFileName(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return "";
        }
        // 경로 구분자(/)의 마지막 위치를 찾아 그 이후의 문자열만 반환
        return filePath.substring(filePath.lastIndexOf("/") + 1);
    }


    /**
     * @author 최정
     * 이미지 조회(시각적)
     *
     * @param imageId 이미지 주키
     * @param type 이미지 타입 - mask, processed, origin
     * @return 이미지
     */
    @Transactional(readOnly = true)
    public Resource getImageByIdAndType(Long imageId, String type) {

        // 1. 이미지 조회
        Image image = imageRepository.findByImageIdAndIsDeleted(imageId, false)
                .orElseThrow(() -> new IllegalArgumentException("이미지가 없습니다."));

        try {
            // DB에 저장된 전체 경로를 기반으로 리소스 생성
            Path filePath;

            // 약봉지를 조회한다면
            if(type.equals("mask"))
                filePath = Paths.get(uploadDir + image.getImgMaskedUrl());
            // 보정된 사진을 조회한다면
            else if(type.equals("processed"))
                filePath = Paths.get(uploadDir + image.getImgProcessedUrl());
            // 원본 사진을 조회한다면
            else if(type.equals("origin"))
                filePath = Paths.get(uploadDir + image.getImgOriginUrl());
            // 잘못된 파라미터
            else
                throw new IllegalArgumentException("유효하지 않은 유형 이미지 입니다. (not.. mask, processed, origin)");

            log.info(filePath.toString());

            // 이미지 소스 가져오기
            Resource resource = new UrlResource(filePath.toUri());

            // 이미지가 있다면 이미지 반환
            if (resource.exists()) {
                return resource;
            } else {
                throw new RuntimeException("파일을 찾을 수 없습니다: " + image.getImgOriginUrl());
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("파일 경로가 잘못되었습니다.", e);
        }
    }
}
