package application.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import application.models.Role;

public interface RoleRepo extends JpaRepository<Role, Long> {
    Role getRoleByName(String name);
}
