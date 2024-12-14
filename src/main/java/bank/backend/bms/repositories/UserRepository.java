package bank.backend.bms.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import bank.backend.bms.models.User;
import bank.backend.bms.models.UserRole;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserById(Long id);
    Optional<User> findUserByEmail(String email);
    List<User> findAllByRole(UserRole role);
}
