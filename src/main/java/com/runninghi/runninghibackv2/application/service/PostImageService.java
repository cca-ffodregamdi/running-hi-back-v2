package com.runninghi.runninghibackv2.application.service;

import com.runninghi.runninghibackv2.application.dto.image.response.ImageTarget;
import com.runninghi.runninghibackv2.domain.entity.Image;
import com.runninghi.runninghibackv2.domain.repository.ImageRepository;
import jakarta.persistence.EntityNotFoundException;
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
    private final ImageTarget imageTarget = ImageTarget.POST;

    public void savePostNo(List<String> imageUrlList, Long postNo) {

        for (String imageUrl : imageUrlList) {
            savePostNo(imageUrl, postNo);
        }
    }

    public void savePostNo(String imageUrl, Long postNo) {
        Image image = imageRepository.findImageByImageUrl(imageUrl)
                .orElseThrow(EntityNotFoundException::new);

        image.updateImageTarget(imageTarget);
        image.updateTargetNo(postNo);
        log.info("{} 번의 이미지가 {} 의 {} 번의 엔테티로 할당되었습니다,", image.getId(), image.getImageTarget(), image.getTargetNo());
    }

    public void updateImage(Long postNo, String newImageUrl) {

        Image image = imageRepository.findImageByTargetNo(postNo)
                .orElseThrow(EntityNotFoundException::new);

        image.updateImageUrl(newImageUrl);
        log.info("{} post 의 이미지가 변경되었습니다.", postNo);
    }
}
