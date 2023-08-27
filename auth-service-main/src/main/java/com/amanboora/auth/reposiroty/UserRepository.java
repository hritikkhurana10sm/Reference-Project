package com.amanboora.auth.reposiroty;

import com.amanboora.auth.model.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<AuthUser, String> {

    Optional<AuthUser> findByUsername(String username);

    @Query("SELECT u from AuthUser u WHERE u.username = :usernameOrEmail OR u.email = :usernameOrEmail")
    Optional<AuthUser> findByUsernameOrEmail(@Param("usernameOrEmail") String usernameOrEmail);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    @Modifying
    @Query("DELETE from AuthUser u WHERE (u.activationId IS NOT NULL OR u.activationId != '') AND u.registerTIme < :currentTimeFifteenMinAgo")
    void deleteExpiredUsers(@Param("currentTimeFifteenMinAgo") LocalDateTime currentTimeFifteenMinAgo);
}
