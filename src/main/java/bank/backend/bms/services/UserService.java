package bank.backend.bms.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bank.backend.bms.models.User;
import bank.backend.bms.models.UserRole;
import bank.backend.bms.repositories.UserRepository;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRoleService userRoleRepository;

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public List<User> getAllUsers(Long role) throws Exception {
        UserRole r = userRoleRepository.getUserRoleById(role);
        if(r == null) throw new Exception("Role not found.");
        return userRepository.findAllByRole(r);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow();
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
