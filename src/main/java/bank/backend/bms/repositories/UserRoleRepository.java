package bank.backend.bms.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import bank.backend.bms.models.UserRole;

import java.util.Optional;

public interface UserRoleRepository extends JpaRepository<UserRole, Long>{
    Optional<UserRole> findByRoleName(String name);
}
