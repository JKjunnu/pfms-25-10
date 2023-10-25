package com.nal.pfms.backend.services;

import com.nal.pfms.backend.entity.FileData;
import com.nal.pfms.backend.repositories.FileDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

@Service
public class FileDataServiceImpl implements FileDataService{
    @Autowired
    private FileDataRepository fileDataRepository;

    private final String FOLDER_PATH="D:\\jatin\\pfmsCompleted-main\\backend\\uploads\\";

    public String uploadImageToFileSystem(MultipartFile file) throws IOException {
        String filePath=FOLDER_PATH+file.getOriginalFilename();

        FileData fileData=fileDataRepository.save(FileData.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .filePath(filePath).build());

        file.transferTo(new File(filePath));

        if (fileData != null) {
            return "file uploaded successfully : " + filePath;
        }
        return null;
    }

    public byte[] downloadImageFromFileSystem(String fileName) throws IOException {
        Optional<FileData> fileData = fileDataRepository.findByName(fileName);
        if(fileData.isPresent()) {
            String filePath = fileData.get().getFilePath();
            byte[] file = Files.readAllBytes(new File(filePath).toPath());
            return file;

        }
        else {
            return null;
        }
    }
}
