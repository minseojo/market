package demo.demo.controller;

import demo.demo.domain.Product;
import demo.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
//@RequestMapping("")
public class HomeController {
    private final ProductService productService;
    @Autowired
    public HomeController(ProductService productService) {
        this.productService = productService;
    }

    /*
    @GetMapping("/")
    public String home() {
        return "home";
    }
    */
    @GetMapping("/")
    private ModelAndView home() throws Exception {
        List<Product> products = productService.findAllProduct();
        ModelAndView mv = new ModelAndView("home");
        mv.addObject("products", products);
        return mv;
    }
}