package demo.demo.controller;

import demo.demo.Form.ProductCreateForm;
import demo.demo.Form.ProductUpdateForm;
import demo.demo.domain.Product;
import demo.demo.domain.UploadFile;
import demo.demo.service.FileService;
import demo.demo.service.ProductCreateService;
import demo.demo.service.ProductService;
import demo.demo.service.ProductUpdateService;
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
import java.util.*;

import static demo.demo.defaultName.PRODUCT_IMAGE;


@Slf4j
@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final FileService fileService;
    private final ProductCreateService productCreateService;
    private final ProductUpdateService productUpdateService;

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
    public String create(@Valid @ModelAttribute ProductCreateForm form, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            log.info("BindingResult = {}", result);
            return "products/createProductForm";
        }

        Product product = productCreateService.createProduct(form);

        redirectAttributes.addAttribute("productId", product.getId());
        return "redirect:/products/{productId}";
    }

    @GetMapping("/products/edit/{id}")
    public String getEditView(@PathVariable Long id, Model model) {
        Product product = productService.findById(id).orElseThrow(NoSuchElementException::new);

        ProductUpdateForm productUpdateForm = productUpdateService.createProduct(product);
        model.addAttribute("product", productUpdateForm);

        List<String> imageFileNames = productUpdateService.getImageFileNames(product);
        model.addAttribute("imageFileNames", imageFileNames);

        return "products/product-edit";
    }


    @PostMapping("/products/edit/{id}")
    public String update(@Valid @ModelAttribute("product") ProductUpdateForm form, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            log.info("BindingResult = {}", result);
        }

        productUpdateService.update(form);

        redirectAttributes.addAttribute("productId", form.getId());
        return "redirect:/products/{productId}";
    }

    @GetMapping("/products/{id}")
    public String product(@PathVariable Long id, Model model) {
        Product product = productService.findById(id).orElse(null);
        if (product == null) {
            return "errors/product-null";
        }
        model.addAttribute("product", product);

        List<String> imageFileNames = productService.getImageFileNames(product);
        model.addAttribute("imageFileNames", imageFileNames);

        return "products/product-view";
    }

    @ResponseBody
    @GetMapping("/images/{filename}")
    public UrlResource downloadImage(@PathVariable String filename) throws MalformedURLException {
        return new UrlResource("file:" + fileService.getFullPath(filename));
    }

}
