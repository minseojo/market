package demo.demo.controller;

import demo.demo.domain.Product;
import demo.demo.domain.UploadFile;
import demo.demo.repository.FileRepository;
import demo.demo.service.FileService;
import demo.demo.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
    public String create(@Valid @ModelAttribute ProductForm form, BindingResult result, RedirectAttributes redirectAttributes) throws IOException {
        if (result.hasErrors()) {
            System.out.println("result = " + result);
            return "products/createProductForm";
        }

        Product product = new Product();
        product.setName(form.getName());
        product.setPrice(form.getPrice());
        product.setCategory(form.getCategory());

        List<UploadFile> storeImageFiles = fileService.storeFiles(product, form.getImageFiles());
        product.setImageFiles(storeImageFiles);
        productService.create(product);
        for (UploadFile storeImageFile : storeImageFiles) {
            fileService.sava(product, storeImageFile);
        }

        redirectAttributes.addAttribute("productId", product.getId());
       // return "redirect:/";
        return "redirect:/products/{productId}";
    }


    @GetMapping("/products/{id}")
    public String products(@PathVariable Long id, Model model) {
        List<UploadFile> uploadFiles = fileService.getFiles(id);
        Optional<Product> product = productService.findById(id);
        model.addAttribute("product", product);
        model.addAttribute("uploadFiles", uploadFiles);
        System.out.println(Optional.ofNullable(uploadFiles.get(0).getStoreFileName()));
        return "products/product-view";
    }

    @ResponseBody
    @GetMapping("/images/{filename}")
    public UrlResource downloadImage(@PathVariable String filename) throws MalformedURLException {
        return new UrlResource("file:" + fileService.getFullPath(filename));
    }

}
