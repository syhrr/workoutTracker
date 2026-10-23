package dev.rhys.workouttracker.repository;

import dev.rhys.workouttracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email); // SELECT ... WHERE email ?
    boolean existsByEmail(String email); // SELECT count(*) > 0 ... WHERE email = ?

}
