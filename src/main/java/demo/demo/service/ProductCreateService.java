package demo.demo.service;

import demo.demo.Form.ProductCreateForm;
import demo.demo.domain.Product;
import demo.demo.domain.UploadFile;
import demo.demo.repository.ProductRepository;
import demo.demo.utility.Time;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static demo.demo.defaultName.PRODUCT_IMAGE;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductCreateService {
    private final ProductRepository productRepository;
    private final FileService fileService;
    private final Time time;

    public Long create(Product product) {
        productRepository.sava(product);
        return product.getId();
    }

    public Product createProduct(ProductCreateForm form) {
        List<UploadFile> storeImageFiles = fileService.saveFiles(form.getImageFiles());
        if (storeImageFiles.isEmpty()) {
            storeImageFiles.add(new UploadFile(PRODUCT_IMAGE));
        }

        Product product = Product.builder()
                .name(form.getName())
                .price(form.getPrice())
                .category(form.getCategory())
                .createDate(time.getTime())
                .imageFiles(storeImageFiles)
                .build();

        productRepository.sava(product);

        return product;
    }

    public List<UploadFile> uploadFilesAndSave(List<MultipartFile> multipartFiles) {
        return fileService.saveFiles(multipartFiles);
    }


}
