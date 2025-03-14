package org.example.authservice.repository;

import org.example.authservice.model.entity.PasswordResetToken;
import org.example.authservice.model.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;



public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken,Long>{

    @Transactional
    void removeByUser(User user);

    Optional<PasswordResetToken> findByToken(String token);


    Optional<List<PasswordResetToken>> findAllByActiveTrueAndUser(User user);

    Optional<PasswordResetToken> findByUser(User user);

    
}