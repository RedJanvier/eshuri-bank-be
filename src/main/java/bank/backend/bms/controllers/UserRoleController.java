package bank.backend.bms.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bank.backend.bms.services.UserRoleService;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import bank.backend.bms.models.UserRole;
import java.util.List;

@RestController
@RequestMapping("/api/userroles")
public class UserRoleController {
    @Autowired
    private UserRoleService userRoleService;

    @GetMapping
    public List<UserRole> getAllUserRoles(){
        return userRoleService.getAllUserRoles();
    }

    @GetMapping("/{id}")
    public UserRole getUserRoleById(@PathVariable Long id) {
        return userRoleService.getUserRoleById(id);
    }

    @PostMapping
    public ResponseEntity<Object> createUserRole(@Valid @RequestBody UserRole userRole, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        try {
            return ResponseEntity.ok(userRoleService.saveUserRole(userRole));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());  // Handle duplicate role name error
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateUserRole(@PathVariable Long id, @Valid @RequestBody UserRole userRole, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        UserRole existingRole = userRoleService.getUserRoleById(id);
        if (existingRole != null) {
            existingRole.setRoleName(userRole.getRoleName());
            try {
                return ResponseEntity.ok(userRoleService.saveUserRole(existingRole));
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body(e.getMessage());  // Handle duplicate role name error
            }
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public String deleteUserRole(@PathVariable Long id) {
        userRoleService.deleteUserRole(id);
        return "UserRole with ID " + id + " has been deleted!";
    }
    
    
}
