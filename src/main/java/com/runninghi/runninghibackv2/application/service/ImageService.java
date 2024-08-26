package com.runninghi.runninghibackv2.application.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ImageService {

    List<String> uploadImageList(List<MultipartFile> fileList, Long memberNo, String dirName) throws IOException;
    String uploadImage(MultipartFile multipartFile, Long memberNo, String dirName) throws IOException;
    void saveImageList(List<String> imageUrlList);
    void saveImage(String imageUrl);
    byte[] resizeImage(MultipartFile multipartFile) throws IOException;
    void deleteImageList(List<String> imageUrlList);
    void deleteImageFromStorage(String imageUrl);
    void deleteImageFromDB(String imageUrl);

}
