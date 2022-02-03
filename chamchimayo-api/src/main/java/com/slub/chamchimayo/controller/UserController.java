package com.slub.chamchimayo.controller;

import com.slub.chamchimayo.dto.ApiResponse;
import com.slub.chamchimayo.dto.request.UserUpdateRequest;
import com.slub.chamchimayo.dto.response.UserResponse;
import com.slub.chamchimayo.entity.User;
import com.slub.chamchimayo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    
    private final UserService userService;

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
    public ApiResponse getUser() {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = userService.getUser(principal.getUsername());

        return ApiResponse.success("user", user);
    }
}
