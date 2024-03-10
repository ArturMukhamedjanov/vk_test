package application.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import application.models.User;

public interface UserRepo extends JpaRepository<User, Long> {
    User getUserByUsername(String username);

    boolean existsByUsername(String username);
}