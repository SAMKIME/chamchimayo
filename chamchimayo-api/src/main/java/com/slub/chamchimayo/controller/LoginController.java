package com.slub.chamchimayo.controller;

import com.slub.chamchimayo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;

    @GetMapping("/account/login")
    public String loginPage() {

        return "/login";
    }

    @GetMapping("/hello")
    public String hello() {

        return "hello world";
    }
}
