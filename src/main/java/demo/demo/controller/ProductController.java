package demo.demo.controller;

import demo.demo.domain.Product;
import demo.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ProductController {

    private final ProductService productService;
    @Autowired
    public ProductController(ProductService productService, ProductService productService1) {
        this.productService = productService1;
    }

    @GetMapping("/products/new")
    public String createForm() {
        return "products/createProductForm";
    }
    @PostMapping("/products/new")
    public String create(ProductForm form) {
        Product product = new Product();
        product.setName(form.getName());
        productService.create(product);

        return "redirect:/";
    }

    @GetMapping("/products")
    public String list(Model model) {
        List<Product> products = productService.findProduct();
        model.addAttribute("products", products);
        return "products/productsList";
    }
}
