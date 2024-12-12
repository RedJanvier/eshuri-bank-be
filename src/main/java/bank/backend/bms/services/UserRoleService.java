package bank.backend.bms.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bank.backend.bms.models.UserRole;
import bank.backend.bms.repositories.UserRoleRepository;
import java.util.Optional;

@Service
public class UserRoleService {
    @Autowired
    private UserRoleRepository userRoleRepository;

    public List<UserRole> getAllUserRoles(){
        return userRoleRepository.findAll();
    }

    public UserRole getUserRoleById(Long id) {
        return userRoleRepository.findById(id).orElse(null);
    }

    public UserRole saveUserRole(UserRole userRole) {
        Optional<UserRole> existingRole = userRoleRepository.findByRoleName(userRole.getRoleName());
        if (existingRole.isPresent()) {
            throw new IllegalArgumentException("Role name '" + userRole.getRoleName() + "' already exists.");
        }
        return userRoleRepository.save(userRole);
    }

    public void deleteUserRole(Long id) {
        userRoleRepository.deleteById(id);
    }
}