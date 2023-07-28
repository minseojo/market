package demo.demo.controller;

import demo.demo.SessionConst;
import demo.demo.domain.Product;
import demo.demo.domain.User;
import demo.demo.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Member;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final ProductService productService;

    @GetMapping("/")
    private String home(@SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginMember,
                        Model model) {
        List<Product> products = productService.findLimitTwenty();
        model.addAttribute("products", products);

        if (loginMember == null) {
            return "home";
        }

        //세션이 유지되면 로그인으로 이동
        model.addAttribute("member", loginMember);
        return "loginHome";
    }
}