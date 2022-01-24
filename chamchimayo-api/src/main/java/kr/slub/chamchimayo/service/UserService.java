package kr.slub.chamchimayo.service;


import kr.slub.chamchimayo.dto.UserUpdateRequest;
import kr.slub.chamchimayo.dto.UserResponse;
import kr.slub.chamchimayo.entity.User;
import kr.slub.chamchimayo.exception.ExceptionWithCodeAndMessage;
import kr.slub.chamchimayo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;


    public UserResponse create(UserUpdateRequest userUpdateRequest) {
        Optional<User> findMembers = userRepository.findByEmail(userUpdateRequest.getEmail());
        findMembers.ifPresent(m -> {
            throw ExceptionWithCodeAndMessage.DUPLICATE_USER.getException();
        });
        User savedUser = userRepository.save(getMemberEntity(userUpdateRequest));
        return UserResponse.of(savedUser);
    }

    @Transactional(readOnly = true)
    public UserResponse findById(Long id) {
        Optional<User> possibleUser = userRepository.findById(id);
        User user = possibleUser.orElseThrow(ExceptionWithCodeAndMessage.NOT_FOUND_USER::getException);
        return UserResponse.of(user);
    }

    public UserResponse update(Long id, UserUpdateRequest userUpdateRequest) {
        Optional<User> possibleUser = userRepository.findById(id);
        User user = possibleUser.orElseThrow(ExceptionWithCodeAndMessage.NOT_FOUND_USER::getException);
        if (!Objects.isNull(userUpdateRequest.getName())) {
            user.changeName(userUpdateRequest.getName());
        }
        if (!Objects.isNull(userUpdateRequest.getEmail())) {
            user.changeEmail(userUpdateRequest.getEmail());
        }
        if (!Objects.isNull(userUpdateRequest.getMobile())) {
            user.changeMobile(userUpdateRequest.getMobile());
        }
        if (!Objects.isNull(userUpdateRequest.getArea())) {
            user.changeArea(userUpdateRequest.getArea());
        }
        return UserResponse.of(user);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    private User getMemberEntity(UserUpdateRequest userUpdateRequest) {
        return User.builder()
                .name(userUpdateRequest.getName())
                .gender(userUpdateRequest.getGender())
                .email(userUpdateRequest.getEmail())
                .mobile(userUpdateRequest.getMobile())
                .area(userUpdateRequest.getArea())
                .build();
    }

}
