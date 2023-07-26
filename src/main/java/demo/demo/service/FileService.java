package demo.demo.service;

import demo.demo.domain.UploadFile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FileService {
    private final String fileDir = "/Users/minseojo/Desktop/demo/images/";
    public String getFullPath(String filename) {
        return fileDir + filename;
    }

    public List<UploadFile> saveFiles(List<MultipartFile> multipartFiles) {
        return multipartFiles.stream()
                .filter(multipartFile -> !multipartFile.isEmpty())
                .map(this::saveFile)
                .collect(Collectors.toList());
    }

    public UploadFile saveFile (MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFilename);
        try {
            multipartFile.transferTo(new File(getFullPath(storeFileName)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new UploadFile(storeFileName);
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
