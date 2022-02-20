package com.slub.chamchimayo.controller;

import com.slub.chamchimayo.config.AppProperties.Auth;
import com.slub.chamchimayo.dto.ApiResponse;
import com.slub.chamchimayo.dto.request.UserUpdateRequest;
import com.slub.chamchimayo.dto.response.UserResponse;
import com.slub.chamchimayo.entity.User;
import com.slub.chamchimayo.entity.UserRefreshToken;
import com.slub.chamchimayo.oauth.entity.CurrentUser;
import com.slub.chamchimayo.oauth.entity.RoleType;
import com.slub.chamchimayo.oauth.entity.UserPrincipal;
import com.slub.chamchimayo.oauth.token.AuthToken;
import com.slub.chamchimayo.oauth.token.AuthTokenProvider;
import com.slub.chamchimayo.repository.UserRefreshTokenRepository;
import com.slub.chamchimayo.service.UserService;
import com.slub.chamchimayo.utils.CookieUtil;
import com.slub.chamchimayo.utils.HeaderUtil;
import io.jsonwebtoken.Claims;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final static String REFRESH_TOKEN = "refresh_token";

    private final UserService userService;
    private final AuthTokenProvider tokenProvider;
    private final UserRefreshTokenRepository userRefreshTokenRepository;

    @PostMapping("/create")
    public ResponseEntity<UserResponse> create(@RequestBody UserUpdateRequest userUpdateRequest) {
        UserResponse userResponse = userService.create(userUpdateRequest);
        return ResponseEntity.ok().body(userResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> find(@PathVariable Long id) {
        UserResponse userResponse = userService.findById(id);
        return ResponseEntity.ok(userResponse);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<UserResponse> update(@PathVariable Long id, @ModelAttribute UserUpdateRequest userUpdateRequest) {
        userService.update(id, userUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/get")
    public ApiResponse getUser(@CurrentUser UserPrincipal userPrincipal) {

//        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        User user = userService.getUser(principal.getUsername());

        User user = userService.getUser(userPrincipal.getUserId());

        return ApiResponse.success("user", user);
    }
}
