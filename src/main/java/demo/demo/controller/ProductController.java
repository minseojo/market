package demo.demo.controller;

import demo.demo.Form.ProductUpdateForm;
import demo.demo.Form.ProductSaveForm;
import demo.demo.domain.Product;
import demo.demo.domain.UploadFile;
import demo.demo.repository.ProductRepository;
import demo.demo.service.FileService;
import demo.demo.service.ProductService;
import demo.demo.utility.Time;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.validation.Valid;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor

public class ProductController {

    private final ProductService productService;
    private final FileService fileService;
    private final ProductRepository productRepository;

    private final Time time;

    @GetMapping("/products")
    public String products(Model model) {
        List<Product> products = productService.findAllProduct();
        model.addAttribute("products", products);
        return "products/productsList";
    }

    @GetMapping("/products/new")
    public String createForm() {
        return "products/createProductForm";
    }

    @PostMapping("/products/new")
    public String create(@Valid @ModelAttribute ProductSaveForm form, BindingResult result, RedirectAttributes redirectAttributes) throws Exception {
        if (result.hasErrors()) {
            log.info("BindingResult = {}", result);
            return "products/createProductForm";
        }

        String createTime = time.getTime();
        List<UploadFile> storeImageFiles = fileService.saveFiles(form.getImageFiles());

        Product product = Product.builder()
                .name(form.getName())
                .price(form.getPrice())
                .category(form.getCategory())
                .createDate(createTime)
                .imageFiles(storeImageFiles)
                .build();
        Long productId = productService.create(product);
        redirectAttributes.addAttribute("productId", productId);
        return "redirect:/products/{productId}";
    }

    @GetMapping("/products/edit/{id}")
    public String getEditView(@PathVariable Long id, Model model) {
        Optional<Product> product = productService.findById(id);
        model.addAttribute("product", product);

        if (product.isPresent()) {
            if (!product.get().getStringImageFiles().isEmpty()) {
                List<String> imageFileNames = List.of(product.get().getStringImageFiles().split(","));
                model.addAttribute("imageFileNames", imageFileNames);
            } else {
                List<String> defaultImage = List.of("default_product.jpeg");
                model.addAttribute("imageFileNames", defaultImage);
            }
        }

        return "products/product-edit";
    }

    @PostMapping("/products/edit/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("product") ProductUpdateForm form, BindingResult result, RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            log.info("BindingResult = {}", result);
        }

        // 나중에 이미지 변경도 추가할 예정
        Product updateProduct = Product.builder()
                .id(form.getId())
                .name(form.getName())
                .price(form.getPrice())
                .category(form.getCategory())
                .build();
        productService.update(updateProduct);

        redirectAttributes.addAttribute("productId", updateProduct.getId());
        return "redirect:/products/{productId}";
    }


    @GetMapping("/products/{id}")
    public String product(@PathVariable Long id, Model model){
        Optional<Product> product = productService.findById(id);
        model.addAttribute("product", product);

        if (product.isPresent()) {
            if (!product.get().getStringImageFiles().isEmpty()) {
                List<String> imageFileNames = List.of(product.get().getStringImageFiles().split(","));
                model.addAttribute("imageFileNames", imageFileNames);
            } else {
                List<String> defaultImage = List.of("default_product.jpeg");
                model.addAttribute("imageFileNames", defaultImage);
            }
        }
        return "products/product-view";
    }

    @ResponseBody
    @GetMapping("/images/{filename}")
    public UrlResource downloadImage(@PathVariable String filename) throws MalformedURLException {
        return new UrlResource("file:" + fileService.getFullPath(filename));
    }

}
