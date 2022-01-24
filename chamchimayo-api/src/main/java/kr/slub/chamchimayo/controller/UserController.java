package kr.slub.chamchimayo.controller;

import kr.slub.chamchimayo.dto.UserUpdateRequest;
import kr.slub.chamchimayo.dto.UserResponse;
import kr.slub.chamchimayo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
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
}
