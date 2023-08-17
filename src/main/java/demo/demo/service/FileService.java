package demo.demo.service;

import demo.demo.domain.UploadFile;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static demo.demo.Config.FileConst.FILE_DIR;
@Service
@RequiredArgsConstructor
public class FileService {
    public String getFullPath(String filename) {
        return FILE_DIR + filename;
    }

    public List<UploadFile> saveFiles(List<MultipartFile> multipartFiles) {
        return multipartFiles.stream()
                .filter(multipartFile -> !multipartFile.isEmpty())
                .map(this::saveFile)
                .collect(Collectors.toList());
    }

    public UploadFile saveFile(MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            throw new IllegalArgumentException("Uploaded file is empty");
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFilename);
        try {
            // Create a File object for storing the uploaded file
            File file = new File(getFullPath(storeFileName));

            // Transfer the data from MultipartFile to the File object
            try (OutputStream outputStream = new FileOutputStream(file)) {
                IOUtils.copy(multipartFile.getInputStream(), outputStream);
            }

            return new UploadFile(storeFileName);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save file", e);
        }
    }

    private String createStoreFileName(String originalFilename) {
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }

}
