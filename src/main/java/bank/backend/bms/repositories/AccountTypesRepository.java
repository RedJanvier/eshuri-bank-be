package bank.backend.bms.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import bank.backend.bms.models.AccountType;


public interface AccountTypesRepository extends JpaRepository<AccountType, Long> {
    Optional<AccountType> findByName(String name);
}
