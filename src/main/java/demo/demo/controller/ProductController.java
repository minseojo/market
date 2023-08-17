package demo.demo.controller;

import demo.demo.Form.ProductCreateForm;
import demo.demo.Form.ProductUpdateForm;
import demo.demo.Config.SessionConst;
import demo.demo.domain.Product;
import demo.demo.domain.User;
import demo.demo.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;
    @GetMapping
    public String getProducts(Model model) {
        //[성공]
        List<Product> products = productService.findAllProduct();
        model.addAttribute("products", products);
        return "products/productsList";
    }


    //  이상하게 required 없애면 인터셉터가 정상 작동안함
    @GetMapping("/new")
    public String getCreateView(@SessionAttribute(name = SessionConst.LOGIN_USER,  required = false) User loginUser,
                                Model model) {
        // 미인증 사용자는 상품생성 불가
        if (loginUser == null) {
            return "redirect:/login";
        }
        // [성공]
        // 상품생성 뷰 이동
        model.addAttribute("product", new ProductCreateForm());
        return "products/product-new";
    }

    @PostMapping("/new")
    public String createProduct(@Validated @ModelAttribute("product") ProductCreateForm form,
                         BindingResult bindingResult,
                         @SessionAttribute(SessionConst.LOGIN_USER) User loginUser,
                         RedirectAttributes redirectAttributes) {
        // 미인증 사용자 [예외]
        if (loginUser == null) {
            return "redirect:/";
        }
        // 검증실패
        if (bindingResult.hasErrors()) {
            log.info("{}", bindingResult);
            return "products/product-new";
        }

        // [성공]
        // 상품생성 성공 후, 상품상세 뷰로 이동
        Product product = productService.create(form, loginUser.getId());
        redirectAttributes.addAttribute("productId", product.getId());
        return "redirect:/products/{productId}";
    }


    @GetMapping("/edit/{productId}")
    public String getUpdateView(@PathVariable Long productId,
                                  @SessionAttribute(name = SessionConst.LOGIN_USER) User loginUser,
                              RedirectAttributes redirectAttributes,
                              Model model) {
        // 상품 주인이 아닌 경우 [예외]
        if (!productService.isProductOwner(loginUser.getId(), productId)) {
            redirectAttributes.addAttribute("productId",productId);
            return "redirect:/products/{productId}";
        }

        // [성공]
        // 상품 수정 폼 생성 후, 모델에 추가
        model.addAttribute("product", productService.updateProductCreate(productId));
        model.addAttribute("createDate", productService.getCreateDate(productId));
        // 상품 이미지들 모델에 추가
        List<String> imageFileNames = productService.getImageFileNames(productId);
        model.addAttribute("imageFileNames", imageFileNames);
        // 상품수정 뷰 이동
        return "products/product-edit";
    }
    @PostMapping("/edit/{id}")
    public String updateProduct(@Validated @ModelAttribute("product") ProductUpdateForm form,
                         BindingResult bindingResult,
                         @SessionAttribute(name = SessionConst.LOGIN_USER) User loginUser,
                         RedirectAttributes redirectAttributes,
                                Model model) {
        // 미인증 사용자 요청
        if (loginUser == null) {
            throw new IllegalStateException();
        }

        // 검증 실패 -> 상품이미지와, 상품수정일 리다이렉트
        if (bindingResult.hasErrors()) {
            model.addAttribute("createDate", productService.getCreateDate(form.getId()));
            model.addAttribute("imageFileNames", productService.getImageFileNames(form.getId()));
            return "products/product-edit";
        }

        // [성공]
        // 상품수정 성공
        redirectAttributes.addAttribute("productId", productService.update(form));
        return "redirect:/products/{productId}";
    }


    @GetMapping("/{productId}")
    public String getProduct(@PathVariable Long productId,
                          Model model,
                          @SessionAttribute(name = SessionConst.LOGIN_USER, required = false)  User loginUser) {

        model.addAttribute("product", productService.findById(productId));
        model.addAttribute("imageFileNames", productService.getImageFileNames(productId));

        // 로그인한 유저인 경우, [로그인유저 아이디][상품주인 아이디]를 비교해 상품 수정버튼 시각적 여부 확인
        if (loginUser != null) {
            model.addAttribute("loginUserId", loginUser.getId());
        }
        return "products/product-view";
    }

    @ExceptionHandler
    @DeleteMapping("/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId,
                                                @SessionAttribute(name = SessionConst.LOGIN_USER) User loginUser) {
        productService.delete(loginUser.getId(), productId);
        return ResponseEntity.ok("상품이 삭제되었습니다.");
    }

}
