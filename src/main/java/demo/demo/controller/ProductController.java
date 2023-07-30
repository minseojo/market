package demo.demo.controller;

import demo.demo.Form.ProductCreateForm;
import demo.demo.Form.ProductUpdateForm;
import demo.demo.SessionConst;
import demo.demo.domain.Product;
import demo.demo.domain.User;
import demo.demo.service.FileService;
import demo.demo.service.ProductService;
import demo.demo.utility.Time;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/products")
    public String products(Model model) {
        List<Product> products = productService.findAllProduct();
        model.addAttribute("products", products);
        return "products/productsList";
    }


    //  이상하게 required 없애면 인터셉터가 정상 작동안함
    @GetMapping("/products/new")
    public String createForm(@SessionAttribute(name = SessionConst.LOGIN_USER,  required = false) User loginUser) {
        if (loginUser == null) {
            return "redirect:/login";
        }
        return "products/product-new";
    }

    @PostMapping("/products/new")
    public String create(@Valid @ModelAttribute("product") ProductCreateForm form,
                         BindingResult bindingResult,
                         @SessionAttribute(SessionConst.LOGIN_USER) User loginUser,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            log.info("BindingResult = {}", bindingResult);
            return "redirect:/products/new";
        }

        Product product = productService.create(form, loginUser.getId());

        redirectAttributes.addAttribute("productId", product.getId());
        return "redirect:/products/{productId}";
    }


    @GetMapping("/products/edit/{productId}")
    public String getEditView(@PathVariable Long productId,
                              @SessionAttribute(name = SessionConst.LOGIN_USER) User loginUser,
                              RedirectAttributes redirectAttributes,
                              Model model) {

        Product product = productService.findById(productId).orElseThrow(NoSuchElementException::new);
        ProductUpdateForm productUpdateForm = productService.updateProductCreate(product);

        if (!productService.ownerCheck(loginUser.getId(), product.getOwnerId())) {
            log.info("[{}]: 상품 주인이 아닌 사용자 요청", loginUser.getId());
            redirectAttributes.addAttribute("productId", product.getId());
            return "redirect:/products/{productId}";
        }

        model.addAttribute("product", productUpdateForm);
        List<String> imageFileNames = productService.getImageFileNames(product);
        model.addAttribute("imageFileNames", imageFileNames);
        return "products/product-edit";
    }
    @PostMapping("/products/edit/{id}")
    public String update(@Valid @ModelAttribute("product") ProductUpdateForm form,
                         BindingResult result,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            log.info("BindingResult = {}", result);
        }

        productService.update(form);

        redirectAttributes.addAttribute("productId", form.getId());
        return "redirect:/products/{productId}";
    }


    @GetMapping("/products/{productId}")
    public String product(@PathVariable Long productId,
                          Model model,
                          @SessionAttribute(name = SessionConst.LOGIN_USER, required = false)  User loginUser) {
        Product product = productService.findById(productId).orElse(null);
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

    @DeleteMapping
    @RequestMapping(value = "/products/{productId}")
    public ResponseEntity<String> delete(@PathVariable Long productId) {
        boolean deleted = productService.delete(productId);
        if (deleted) {
            return ResponseEntity.ok("상품이 삭제되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("상품 삭제에 실패했습니다.");
        }
    }

}
