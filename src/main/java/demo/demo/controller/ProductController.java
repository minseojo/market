package demo.demo.controller;

import demo.demo.domain.Product;
import demo.demo.domain.UploadFile;
import demo.demo.service.FileService;
import demo.demo.service.ProductService;
import demo.demo.utility.Time;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.validation.Valid;
import java.io.IOException;
import java.net.MalformedURLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor

public class ProductController {

    private final ProductService productService;

    private final FileService fileService;

    private final Time time;

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

        List<UploadFile> storeImageFiles = fileService.storeFiles(form.getImageFiles());
        product.setImageFiles(storeImageFiles);
        String createTime = time.getTime();
        if(createTime != "") {
            product.setCreateDate(createTime);
        }
        productService.create(product);

        redirectAttributes.addAttribute("productId", product.getId());
       // return "redirect:/";
        return "redirect:/products/{productId}";
    }


    @GetMapping("/products/{id}")
    public String products(@PathVariable Long id, Model model){
        Optional<Product> product = productService.findById(id);
        model.addAttribute("product", product);

        if (product.isPresent() && product.get().getStringImageFiles() != "") {
            List<String> imageFileNames = List.of(product.get().getStringImageFiles().split(","));
            model.addAttribute("imageFileNames", imageFileNames);
        }
        return "products/product-view";
    }

    @ResponseBody
    @GetMapping("/images/{filename}")
    public UrlResource downloadImage(@PathVariable String filename) throws MalformedURLException {
        return new UrlResource("file:" + fileService.getFullPath(filename));
    }

}
