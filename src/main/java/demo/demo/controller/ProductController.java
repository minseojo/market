package demo.demo.controller;

import demo.demo.domain.Product;
import demo.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class ProductController {

    private final ProductService productService;
    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products/new")
    public String createForm() {
        return "products/createProductForm";
    }
    @PostMapping("/products/new")
    public String create(ProductForm form) {
        Product product = new Product();
        product.setName(form.getName());
        product.setPrice(Integer.parseInt(form.getPrice()));
        product.setCategory(form.getCategory());
        productService.create(product);
        return "redirect:/";
    }

    @GetMapping("/products")
    public String getAllList(Model model) {
        List<Product> products = productService.findAllProduct();
        model.addAttribute("products", products);
        return "products/productsList";
    }



}
