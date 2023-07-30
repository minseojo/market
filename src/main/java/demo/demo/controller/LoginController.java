package demo.demo.controller;

import demo.demo.Form.LoginForm;
import demo.demo.Form.SignupForm;
import demo.demo.SessionConst;
import demo.demo.domain.User;
import demo.demo.service.LoginService;
import demo.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {
    private final UserService userService;
    private final LoginService loginService;
    @GetMapping("/login")
    public String loginHome() {
        return "login/login-view";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute LoginForm form, BindingResult result,
                        @RequestParam(defaultValue = "/") String redirectURL,
                        HttpServletRequest request) {
        if (result.hasErrors()) {
            return "login/login-view";
        }
        log.info("{}", redirectURL);
        User user = userService.findByUserId(form.getUserId()).orElse(null);
        if (user == null || !loginService.login(user, form)) {
            result.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/login-view";
        }

        //세션이 있으면 있는 세션 반환, 없으면 신규 세션 생성
        HttpSession session = request.getSession();
        //세션에 로그인 회원 정보 보관
        session.setAttribute(SessionConst.LOGIN_USER, user);
        return "redirect:" + redirectURL;
    }

    @GetMapping("/signup")
    public String signupHome() {
        return "login/signup-view";
    }

    @PostMapping("/signup")
    public String signup(@Valid @ModelAttribute SignupForm form,
                         BindingResult result,
                         RedirectAttributes redirectAttributes)  {
        if (result.hasErrors()) {

            log.info("{}", result);
        }

        Long id = loginService.signup(form);

        redirectAttributes.addAttribute("id", id);
        return "redirect:/users/{id}";
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

}
