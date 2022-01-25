package com.slub.chamchimayo.controller;

import com.slub.chamchimayo.dto.SessionUser;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {
    private final HttpSession httpSession;

    @GetMapping("/loginSuccess")
    public ModelAndView index(ModelAndView md) {
        SessionUser user = (SessionUser) httpSession.getAttribute("user");

        if (user != null) {
            md.addObject("userName", user.getName());
            md.addObject("userEmail", user.getEmail());
        }

        log.info("[로그인 성공]");
        log.info("User Session : {}",user);

        md.setViewName("loginSuccess");
        return md;
    }

    @GetMapping("/loginFailure")
    public ModelAndView loginFailure(ModelAndView md) {

        log.info("로그인 실패");
        md.setViewName("loginFailure");
        return md;
    }
}