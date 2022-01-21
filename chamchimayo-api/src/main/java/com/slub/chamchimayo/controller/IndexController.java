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
    public ModelAndView index(ModelAndView md) {

        SessionUser user = (SessionUser) httpSession.getAttribute("user");

        log.info("로그인 성공");

        if (user != null) {
            md.addObject("userName", user.getName());
        }

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