package com.slub.chamchimayo.repository;

import com.slub.chamchimayo.entity.UserRefreshToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRefreshTokenRepository extends JpaRepository<UserRefreshToken, Long> {
    UserRefreshToken findByUserId(String userId);
    Optional<UserRefreshToken> findByUserIdAndRefreshToken(String userId, String refreshToken);
}
