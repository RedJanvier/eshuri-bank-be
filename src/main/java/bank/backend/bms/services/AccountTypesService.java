package bank.backend.bms.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bank.backend.bms.models.AccountType;
import bank.backend.bms.repositories.AccountTypesRepository;

@Service
public class AccountTypesService {
    @Autowired
    private AccountTypesRepository accountTypesRepository;

    public List<AccountType> getAllAccountTypes(){
        return accountTypesRepository.findAll();
    }
    
    public Optional<AccountType> getAccountTypeById(Long id) {
        Optional<AccountType> existing = accountTypesRepository.findById(id);
        if (existing.isPresent()) {
            return accountTypesRepository.findById(id);
        } else {
            throw new IllegalArgumentException("Account type not found for ID: " + id);
        }
    }
    
    public AccountType createAccountType(AccountType accountType){
        return accountTypesRepository.save(accountType);
    }
    
    public AccountType updateAccountType(Long id, AccountType accountType) {
        Optional<AccountType> existing = accountTypesRepository.findById(id);
        Optional<AccountType> type = accountTypesRepository.findByName(accountType.getName());
        if (existing.isPresent()) {
            if(type.isPresent()){
                throw new IllegalArgumentException("Account type Already exists");
            }
            accountType.setId(id);
            return accountTypesRepository.save(accountType);
        } else {
            throw new IllegalArgumentException("Account type not found for ID: " + id);
        }
    }
    
    public boolean deleteAccountType(Long id) {
        try {
            Optional<AccountType> existingAccountType = accountTypesRepository.findById(id);
            if (existingAccountType.isPresent()) {
                accountTypesRepository.deleteById(id);
                return true;
            }
            return false;
        } catch (Exception e) {
            System.err.println("Error while deleting account type: " + e.getMessage());
            return false;
        }
    }

}