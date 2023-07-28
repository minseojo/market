package demo.demo.controller;

import demo.demo.Form.ProductCreateForm;
import demo.demo.Form.ProductUpdateForm;
import demo.demo.SessionConst;
import demo.demo.domain.Product;
import demo.demo.domain.User;
import demo.demo.service.FileService;
import demo.demo.service.ProductService;
import demo.demo.utility.Time;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.net.MalformedURLException;
import java.util.*;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final FileService fileService;


    private final Time time;

    @GetMapping("/products")
    public String products(Model model) {
        List<Product> products = productService.findAllProduct();
        model.addAttribute("products", products);
        return "products/productsList";
    }

    @GetMapping("/products/new")
    public String createForm(@SessionAttribute(name = SessionConst.LOGIN_USER) User loginMember) {
        if (loginMember == null) {
            return "login/login-view";
        }

        return "products/createProductForm";
    }

    @PostMapping("/products/new")
    public String create(@Valid @ModelAttribute ProductCreateForm form,
                         BindingResult result,
                         @SessionAttribute(SessionConst.LOGIN_USER) User loginUser,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            log.info("BindingResult = {}", result);
            return "products/createProductForm";
        }

        Product product = productService.create(form, loginUser.getId());

        redirectAttributes.addAttribute("productId", product.getId());
        return "redirect:/products/{productId}";
    }


    @GetMapping("/products/edit/{id}")
    public String getEditView(@PathVariable Long id,
                              @SessionAttribute(name = SessionConst.LOGIN_USER) User loginUser,
                              RedirectAttributes redirectAttributes,
                              HttpServletRequest request,
                              Model model) {
        if (loginUser == null) {
            log.info("getEditView 로그인 안함");
            return "redirect:login-view";
        }

        Product product = productService.findById(id).orElseThrow(NoSuchElementException::new);
        ProductUpdateForm productUpdateForm = productService.updateProductCreate(product);

        if (!productService.ownerCheck(loginUser.getId(), product.getOwnerId())) {
            log.info("[{}]: 상품 주인이 아닌 사용자 요청", loginUser.getId());
            redirectAttributes.addAttribute("id", product.getId());
            return "redirect:/products/{id}";
        }

        model.addAttribute("product", productUpdateForm);
        List<String> imageFileNames = productService.getImageFileNames(product);
        model.addAttribute("imageFileNames", imageFileNames);
        return "products/product-edit";
    }


    @PostMapping("/products/edit/{id}")
    public String update(@Valid @ModelAttribute("product") ProductUpdateForm form, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            log.info("BindingResult = {}", result);
        }

        productService.update(form);

        redirectAttributes.addAttribute("productId", form.getId());
        return "redirect:/products/{productId}";
    }

    @GetMapping("/products/{id}")
    public String product(@PathVariable Long id, Model model,
                          @SessionAttribute(name = SessionConst.LOGIN_USER, required = false)  User loginUser) {
        Product product = productService.findById(id).orElse(null);
        if (product == null) {
            return "errors/product-null";
        }
        model.addAttribute("product", product);

        List<String> imageFileNames = productService.getImageFileNames(product);
        model.addAttribute("imageFileNames", imageFileNames);

        if (loginUser != null) {
            model.addAttribute("loginUserId", loginUser.getId());
        }
        return "products/product-view";
    }

    @ResponseBody
    @GetMapping("/images/{filename}")
    public UrlResource downloadImage(@PathVariable String filename) throws MalformedURLException {
        return new UrlResource("file:" + fileService.getFullPath(filename));
    }

}
