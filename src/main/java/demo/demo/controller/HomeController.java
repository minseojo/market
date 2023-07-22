package demo.demo.controller;

import demo.demo.domain.Product;
import demo.demo.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final ProductService productService;

    @GetMapping("/")
    private ModelAndView home() throws Exception {
        List<Product> products = productService.findLimitTwenty();
        ModelAndView mv = new ModelAndView("home");
        mv.addObject("products", products);
        return mv;
    }
}