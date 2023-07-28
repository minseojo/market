package demo.demo.service;

import demo.demo.Form.ProductCreateForm;
import demo.demo.Form.ProductUpdateForm;
import demo.demo.domain.Product;
import demo.demo.domain.UploadFile;
import demo.demo.repository.ProductRepository;
import demo.demo.utility.Time;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.AttributedString;
import java.util.List;
import java.util.Optional;

import static demo.demo.defaultName.PRODUCT_IMAGE;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {
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
    public List<Product> findLimitTwenty() {return productRepository.findLimitTwenty();}
    public List<Product> findAllProduct() {
        return productRepository.findAll();
    }

    public Optional<Product> findById(Long id) {return productRepository.findById(id);}
    public List<Product> findByFilter(String name) {
        return productRepository.findByFilter(name);
    }

    public Product update(ProductUpdateForm form) {
        Product product = Product.builder()
                .id(form.getId())
                .name(form.getName())
                .price(form.getPrice())
                .category(form.getCategory())
                .build();

        return productRepository.update(product);
    }

    public ProductUpdateForm createProduct(Product product) {
        return ProductUpdateForm.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .category(product.getCategory())
                .createDate(product.getCreateDate())
                .build();
    }

    public List<String> getImageFileNames(Product product) {
        List<String> imageFileNames;
        if (product.getStringImageFiles() == null) {
            imageFileNames = List.of("default_product.jpeg");
        } else {
            imageFileNames = List.of(product.getStringImageFiles().split(","));
        }
        return imageFileNames;
    }
}
