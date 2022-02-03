package com.slub.chamchimayo.repository;

import com.slub.chamchimayo.entity.User;
import com.slub.chamchimayo.oauth.entity.ProviderType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    User findByEmailAndProviderType(String email, ProviderType providerType);
    Optional<User> findById(Long id);
    User findByUserId(String userId);

}