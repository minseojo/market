package demo.demo.service;

import demo.demo.domain.Product;
import demo.demo.domain.UploadFile;
import demo.demo.repository.FileRepository;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.ls.LSException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;

    private String fileDir = "/Users/minseojo/Desktop/demo/images/";
    public String getFullPath(String filename) {
        return fileDir + filename;
    }

    public UploadFile sava(Product product, UploadFile imageFile) {
        return fileRepository.sava(product, imageFile);
    }

    public List<UploadFile> getFiles(Long id) {

        return fileRepository.findByIdUploadFiles(id);
    }
    public List<UploadFile> storeFiles (Product product, List <MultipartFile> multipartFiles) throws IOException {
        List<UploadFile> storeFileResult = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                storeFileResult.add(storeFile(product, multipartFile));
            }
        }

        return storeFileResult;
    }


    public UploadFile storeFile (Product product, MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }
        String originalFilename = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFilename);
        multipartFile.transferTo(new File(getFullPath(storeFileName)));
        return new UploadFile(originalFilename, storeFileName, product.getId());
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
