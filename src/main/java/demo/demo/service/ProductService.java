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

import java.util.List;
import java.util.NoSuchElementException;

import static demo.demo.Config.FileConst.DEFAULT_IMAGE_PRODUCT;
@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {
    private final ProductRepository productRepository;
    private final FileService fileService;
    private final Time time;

    @Transactional
    public Product create(ProductCreateForm form, Long loginUserId) {
        List<UploadFile> storeImageFiles = fileService.saveFiles(form.getImageFiles());
        if (storeImageFiles.isEmpty()) {
            storeImageFiles.add(new UploadFile(DEFAULT_IMAGE_PRODUCT));
        }

        Product product = Product.builder()
                .name(form.getName())
                .price(form.getPrice())
                .category(form.getCategory())
                .createDate(time.getCurrentTime())
                .imageFiles(storeImageFiles)
                .ownerId(loginUserId)
                .build();
        Long generatedId = productRepository.sava(product);
        product.setId(generatedId);
        return product;
    }
    public List<Product> findLimitEight() {return productRepository.findLimitEight();}
    public List<Product> findAllProduct() {
        return productRepository.findAll();
    }

    public Product findById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new NoSuchElementException("상품이 존재하지 않습니다."));
    }


    public List<Product> findByFilter(String name){
        return productRepository.findByFilter(name);
    }

    public Long update(ProductUpdateForm form) {
        Product product = Product.builder()
                .id(form.getId())
                .name(form.getName())
                .price(form.getPrice())
                .category(form.getCategory())
                .build();
        return productRepository.update(product).getId();
    }
    public boolean delete(Long loginUserId, Long productId) {
        if (!isProductOwner(loginUserId, productId)) {
            throw new RuntimeException("비정상 접근입니다. 상품을 삭제할 수 없습니다.");
        }
        return productRepository.deleteById(productId);
    }
    public boolean isProductOwner(Long loginUserId, Long productId) {
        Product product = findById(productId);
        return product.isOwner(loginUserId);
    }

    public ProductUpdateForm updateProductCreate(Long productId) {
        Product product = findById(productId);
        return ProductUpdateForm.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .category(product.getCategory())
                .build();
    }

    public List<String> getImageFileNames(Long productId) {
        Product product = findById(productId);
        List<String> imageFileNames;
        if (product.getStringImageFiles() == null) {
            imageFileNames = List.of(DEFAULT_IMAGE_PRODUCT);
        } else {
            imageFileNames = List.of(product.getStringImageFiles().split(","));
        }
        return imageFileNames;
    }

    public String getCreateDate(Long productId) {
        Product product = findById(productId);
        return product.getCreateDate();
    }

}
