package com.stayfinder.backend.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.stayfinder.backend.User;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}