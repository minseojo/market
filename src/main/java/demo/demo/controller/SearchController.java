package demo.demo.controller;

import demo.demo.domain.Product;
import demo.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

@Controller
public class SearchController {

    private ProductService productService;

    @Autowired
    public SearchController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping(value = "/search")
    public String searchList(Model model, @RequestParam String name) {
        List<Product> products = productService.findProduct(name);
        //List<Product> products1 = productService.findAllProduct();
        model.addAttribute("products", products);
        return "products/productsList";
    }

}
