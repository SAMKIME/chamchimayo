package com.slub.chamchimayo.controller;

import com.slub.chamchimayo.dto.SessionUser;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
@RequiredArgsConstructor
public class IndexController {
    private final HttpSession httpSession;

    @GetMapping("/loginSuccess")
    public String index(Model model) {
        SessionUser user = (SessionUser) httpSession.getAttribute("user");

        if (user != null) {
            model.addAttribute("userName", user.getName());
            model.addAttribute("userEmail", user.getEmail());
        }

        log.info("[로그인 성공]");
        log.info("userName : " + user.getName());
        log.info("userEmail : " + user.getEmail());

        return "loginSuccess";
    }

    @GetMapping("/loginFailure")
    public ModelAndView loginFailure(ModelAndView md) {

        log.info("로그인 실패");

        md.setViewName("loginFailure");

        return md;
    }
}