package com.runninghi.runninghibackv2.domain.repository;

import com.runninghi.runninghibackv2.domain.entity.Image;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {

    Optional<Image> findImageByImageUrl(String imageUrl);

    Optional<Image> findImageByPostNo(Long postNo);

    @Query("DELETE FROM Image i WHERE i.imageUrl IN :urls")
    @Modifying
    int deleteAllByImageUrlIn(@Param("urls") List<String> imageUrls);

    @Query("SELECT i.imageUrl FROM Image i WHERE i.imageUrl IN :urls")
    List<String> findImageUrlsIn(@Param("urls") List<String> imageUrls);

    @Query("SELECT i.imageUrl FROM Image i WHERE i.targetNo IS NULL AND i.createDate < :date")
    List<String> findUnassignedImagesBeforeDate(@Param("date") LocalDateTime date);
}
