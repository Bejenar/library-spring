package com.example.demo.service.impl;

import com.example.demo.config.LibraryProperties;
import com.example.demo.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final LibraryProperties properties;

    @Override
    public String convertFileToBase64(String fileName) {
        String filePath = properties.getStoragePath() + "/" + fileName;

        byte[] fileContent = new byte[0];
        try {
            fileContent = FileUtils.readFileToByteArray(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Can't convert file to byte array", e);
        }

        return Base64.getEncoder().encodeToString(fileContent);
    }

    @Override
    public void saveFile(MultipartFile file) {
        try {
            String filePath = properties.getStoragePath() + "/" + file.getOriginalFilename();
            File dest = new File(filePath);
            file.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Can't save multipart file", e);
        }

        log.info("File " + file.getOriginalFilename() + " saved to local storage");
    }
}
