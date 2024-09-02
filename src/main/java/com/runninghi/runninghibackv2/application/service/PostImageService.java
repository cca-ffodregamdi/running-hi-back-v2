package com.runninghi.runninghibackv2.application.service;

import com.runninghi.runninghibackv2.application.dto.image.response.ImageTarget;
import com.runninghi.runninghibackv2.domain.entity.Image;
import com.runninghi.runninghibackv2.domain.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PostImageService {

    private final ImageRepository imageRepository;
    private static final ImageTarget imageTarget = ImageTarget.POST;

    public void savePostNo(List<String> imageUrlList, Long postNo) {

        for (String imageUrl : imageUrlList) {
            savePostNo(imageUrl, postNo);
        }
    }

    public void savePostNo(String imageUrl, Long postNo) {
        log.info("이미지 저장을 시도합니다. imageUrl: {}, postNo: {}", imageUrl, postNo );
        Image image = imageRepository.findImageByImageUrl(imageUrl)
                        .orElse(Image.builder()
                                .imageUrl(imageUrl)
                                .build());

        image.updateImageTarget(imageTarget);
        image.updateTargetNo(postNo);
        log.info("{} 번의 이미지가 {} 의 {} 번의 엔테티로 할당되었습니다,", image.getId(), image.getImageTarget(), image.getTargetNo());
    }

}
