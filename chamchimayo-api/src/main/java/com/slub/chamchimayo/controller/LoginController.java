package com.slub.chamchimayo.controller;

import com.slub.chamchimayo.dto.response.UserResponse;
import com.slub.chamchimayo.oauth.entity.CurrentUser;
import com.slub.chamchimayo.oauth.entity.UserPrincipal;
import com.slub.chamchimayo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;

    @GetMapping("/account/login")
    public String loginPage() {

        return "/login";
    }
}
