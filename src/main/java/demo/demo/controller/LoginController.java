package demo.demo.controller;

import demo.demo.form.LoginForm;
import demo.demo.form.SignupForm;
import demo.demo.config.SessionConst;
import demo.demo.domain.User;
import demo.demo.service.LoginService;
import demo.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {
    private final UserService userService;
    private final LoginService loginService;
    @GetMapping("/login")
    public String loginHome(Model model) {
        model.addAttribute("loginForm", new LoginForm("", ""));
        return "login/login-view";
    }

    @PostMapping("/login")
    public String login(@Validated @ModelAttribute("loginForm") LoginForm loginForm,
                        BindingResult bindingResult,
                        @RequestParam(defaultValue = "/") String redirectURL,
                        HttpServletRequest request) {

        if (bindingResult.hasErrors()) {
            return "login/login-view";
        }

        User user = userService.findByUserId(loginForm.getUserId()).orElse(null);
        if (user == null || !loginService.login(user, loginForm)) {
            bindingResult.reject("loginFail", " 아이디 또는 비밀번호를 잘못 입력했습니다.");
            return "login/login-view";
        }

        //세션이 있으면 있는 세션 반환, 없으면 신규 세션 생성
        HttpSession session = request.getSession();
        //세션에 로그인 회원 정보 보관
        session.setAttribute(SessionConst.LOGIN_USER, user);
        return "redirect:" + redirectURL;
    }

    @GetMapping("/signup")
    public String signupHome(Model model) {
        model.addAttribute("signupForm", new SignupForm());
        return "login/signup-view";
    }

    @PostMapping("/signup")
    public String signup(@Valid @ModelAttribute("signupForm") SignupForm form,
                         BindingResult result,
                         RedirectAttributes redirectAttributes)  {
        if (result.hasErrors()) {
            return "/login/signup-view";
        }

        Long id = loginService.signup(form);
        return "redirect:/login";
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        //세션을 삭제한다.
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }

    @GetMapping("/find-id")
    public String findId(){
        return "/login/findId-view";
    }

    @GetMapping("/find-password")
    public String findPassword(){
        return "/login/findPassword-view";
    }

}
