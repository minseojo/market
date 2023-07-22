package demo.demo.controller;

import demo.demo.domain.Product;
import demo.demo.domain.User;
import demo.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @GetMapping("/users/{id}")
    public String products(@PathVariable Long id, Model model){
        Optional<User> user = userService.findById(id);
        model.addAttribute("user", user);
        /*
        나중에 프로필 이미지 추가 시
        if (user.isPresent()) {
            List<String> imageFileNames = List.of(user.get().getStringImageFile().split(","));
            model.addAttribute("imageFileName", imageFileNames);
        }

        */
        return "users/user-view";
    }
}
