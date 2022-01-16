package com.slub.chamchimayo.repository;

import com.slub.chamchimayo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String mail);

    Optional<User> findById(Long userId);
}
