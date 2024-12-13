package bank.backend.bms.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bank.backend.bms.models.AccountType;
import bank.backend.bms.services.AccountTypesService;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/api/account-type")
public class AccountTypesController {
    @Autowired
    private AccountTypesService accountTypesService;

    @GetMapping
    public List<AccountType> getAllAccountTypes(){
        return accountTypesService.getAllAccountTypes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAccountTypeById(@PathVariable Long id) {
        try {
            // Fetch the account type by ID
            Optional<AccountType> accountTypeOptional = accountTypesService.getAccountTypeById(id);
    
            // Check if the account type is present
            if (accountTypeOptional.isPresent()) {
                return ResponseEntity.ok(accountTypeOptional.get()); // Return the account type if found
            } else {
                return ResponseEntity.status(404).body("Account type not found for ID: " + id); // Return a 404 error message
            }
        } catch (Exception e) {
            // Handle unexpected exceptions
            return ResponseEntity.status(500).body("Error fetching account type: " + e.getMessage());
        }
    }
    
    @PostMapping
    public AccountType createAccountType(@RequestBody AccountType accountType){
        return accountTypesService.createAccountType(accountType);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAccountType(@PathVariable Long id, @RequestBody AccountType accountType) {
        try {
            AccountType updatedAccountType = accountTypesService.updateAccountType(id, accountType);
            return ResponseEntity.ok(updatedAccountType);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error updating account type: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAccountType(@PathVariable Long id) {
        try {
            boolean isDeleted = accountTypesService.deleteAccountType(id);
            if (isDeleted) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.status(404).body("Account type not found for deletion for Id: " + id);
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting account type: " + e.getMessage());
        }
    }

}

