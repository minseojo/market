package demo.demo.controller;

import demo.demo.domain.Product;
import demo.demo.domain.UploadFile;
import demo.demo.repository.FileRepository;
import demo.demo.service.FileService;
import demo.demo.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor

public class ProductController {

    private final ProductService productService;

    private final FileService fileService;
    private final FileRepository fileRepository;

    @GetMapping("/products")
    public String getAllList(Model model) {
        List<Product> products = productService.findAllProduct();
        model.addAttribute("products", products);
        return "products/productsList";
    }

    @GetMapping("/products/new")
    public String createForm() {
        return "products/createProductForm";
    }

    @PostMapping("/products/new")
    public String create(@Valid @ModelAttribute ProductForm form, @RequestParam(value = "imageFiles", required = false) List<MultipartFile> imageFiles, BindingResult result, RedirectAttributes redirectAttributes) throws IOException {
        if (result.hasErrors()) {
            System.out.println("result = " + result);
            return "products/createProductForm";
        }

        Product product = new Product();
        product.setName(form.getName());
        product.setPrice(form.getPrice());
        product.setCategory(form.getCategory());

        List<UploadFile> storeImageFiles = fileService.storeFiles(imageFiles);
        product.setImageFiles(storeImageFiles);
        for (UploadFile imageFile : storeImageFiles) {
            fileRepository.sava(imageFile);
        }
        productService.create(product);

        //redirectAttributes.addAttribute("productId", product.getId());
        return "redirect:/";
        //return "redirect:/products/{productId}";
    }

    @GetMapping("/products/{productId}")
    public String getProduct() {
        return null;
    }

}
