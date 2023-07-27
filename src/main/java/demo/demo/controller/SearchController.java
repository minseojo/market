package demo.demo.controller;

import demo.demo.domain.Product;
import demo.demo.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class SearchController {
    private final ProductService productService;

    @GetMapping(value = "/search")
    public String searchList(Model model, @RequestParam String name) {
        List<Product> products = productService.findByFilter(name);
        model.addAttribute("products", products);
        return "products/productsList";
    }

}
