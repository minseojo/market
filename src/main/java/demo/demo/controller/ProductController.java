package demo.demo.controller;

import demo.demo.Form.ProductCreateForm;
import demo.demo.Form.ProductUpdateForm;
import demo.demo.SessionConst;
import demo.demo.domain.Product;
import demo.demo.domain.User;
import demo.demo.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.w3c.dom.ranges.Range;

import javax.validation.Valid;
import java.util.*;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/products")
    public String getProducts(Model model) {
        List<Product> products = productService.findAllProduct();
        model.addAttribute("products", products);
        return "products/productsList";
    }


    //  이상하게 required 없애면 인터셉터가 정상 작동안함
    @GetMapping("/products/new")
    public String getCreateView(@SessionAttribute(name = SessionConst.LOGIN_USER,  required = false) User loginUser,
                                Model model) {
        if (loginUser == null) {
            return "redirect:/login";
        }
        model.addAttribute("product", new ProductCreateForm());
        return "products/product-new";
    }

    @PostMapping("/products/new")
    public String createProduct(@Validated @ModelAttribute("product") ProductCreateForm form,
                         BindingResult bindingResult,
                         @SessionAttribute(SessionConst.LOGIN_USER) User loginUser,
                         RedirectAttributes redirectAttributes) {

        Product product = productService.create(form, loginUser.getId());
        redirectAttributes.addAttribute("productId", product.getId());
        return "redirect:/products/{productId}";
    }


    @GetMapping("/products/edit/{productId}")
    public String getUpdateView(@PathVariable Long productId,
                                  @SessionAttribute(name = SessionConst.LOGIN_USER) User loginUser,
                              RedirectAttributes redirectAttributes,
                              Model model) {

        Product product = productService.findById(productId).orElseThrow(NoSuchElementException::new);


        // 상품 주인이 아닌 경우
        if (!productService.ownerCheck(loginUser.getId(), product.getOwnerId())) {
            redirectAttributes.addAttribute("productId", product.getId());
            return "redirect:/products/{productId}";
        }

        ProductUpdateForm productUpdateForm = productService.updateProductCreate(product);
        model.addAttribute("product", productUpdateForm);
        model.addAttribute("createDate", product.getCreateDate());
        List<String> imageFileNames = productService.getImageFileNames(product);
        model.addAttribute("imageFileNames", imageFileNames);
        return "products/product-edit";
    }
    @PostMapping("/products/edit/{id}")
    public String updateProduct(@Validated @ModelAttribute("product") ProductUpdateForm form,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes,
                                Model model) {
        if (bindingResult.hasErrors()) {
            Product product = productService.findById(form.getId()).orElseThrow(NoSuchElementException::new);
            model.addAttribute("createDate", product.getCreateDate());
            model.addAttribute("imageFileNames", productService.getImageFileNames(product));
            return "products/product-edit";
        }

        redirectAttributes.addAttribute("productId", productService.update(form));
        return "redirect:/products/{productId}";
    }


    @GetMapping("/products/{productId}")
    public String getProduct(@PathVariable Long productId,
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

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId) {
        if (!productService.delete(productId)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("상품 삭제에 실패했습니다.");
        }
        return ResponseEntity.ok("상품이 삭제되었습니다.");
    }

}
