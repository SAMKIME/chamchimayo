package com.slub.chamchimayo.oauth.service;

import com.slub.chamchimayo.entity.User;
import com.slub.chamchimayo.oauth.entity.UserPrincipal;
import com.slub.chamchimayo.repository.UserRepository;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = userRepository.findByUserId(userId).orElseThrow(
            () -> new UsernameNotFoundException("User not found with userId : " + userId)
        );

        return UserPrincipal.create(user);
    }
}

