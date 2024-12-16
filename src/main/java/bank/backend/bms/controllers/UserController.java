package bank.backend.bms.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import bank.backend.bms.services.UserRoleService;
import bank.backend.bms.services.UserService;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import bank.backend.bms.config.SecurityConfig;
import bank.backend.bms.config.Utils;
import bank.backend.bms.dtos.UserResponse;
import bank.backend.bms.models.User;
import bank.backend.bms.models.UserRole;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRoleService userRoleService;

    private JsonObjectBuilder jsonOB = Json.createObjectBuilder();
    
    @GetMapping
    public ResponseEntity<?> getAllUsers(@RequestParam(required = false) Long roleId){
        List<User> dbUsers;
        List<UserResponse> users = new ArrayList<>();
        try {
            if(roleId == null) {
                dbUsers = userService.getAllUsers();
            } else {
                dbUsers = userService.getAllUsers(roleId);
            }
            for(int i = 0; i < dbUsers.size(); i++) {
                users.add(UserResponse.fromUser(dbUsers.get(i)));
            }
            return ResponseEntity.ok().body(users);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            User user = userService.getUserById(id);
            return ResponseEntity.ok(UserResponse.fromUser(user));
        } catch (NoSuchElementException e) {
            JsonObject errorMessage = jsonOB
                .add("message", "Could not find the user with id: '" + id + "'")
                .add("defaultMessage", e.getMessage())
                .build();
            return ResponseEntity.status(404)
                .contentType(MediaType.APPLICATION_JSON).body(errorMessage.toString());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Server error: Unknown issue caused this.");
        }
    }

    @PostMapping
    public ResponseEntity<Object> createUser(@Valid @RequestBody User userRequest, BindingResult bindingResult) {

        // Validate input data
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest()
                .body(bindingResult.getAllErrors());
        }

        // Check if email already exists
        if (userService.getUserByEmail(userRequest.getEmail()).isPresent()) {
            return ResponseEntity.badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(jsonOB.add("message", "Email already exists!").build().toString());
        }

        // Retrieve and validate role
        UserRole role = userRequest.getRole();
        UserRole existingRole = userRoleService.getUserRoleById(role.getId());
        if (existingRole == null) {
            return ResponseEntity.badRequest().body("Invalid role ID: " + role.getId());
        }
        userRequest.setRole(existingRole); // Associate existing role with the user

        // Hash the password
        String hashedPassword = SecurityConfig.hashPassword(userRequest.getPassword());
        userRequest.setPassword(hashedPassword);

        try {
            userRequest.setCreatedAt(Utils.now());
            userRequest.setUpdatedAt(Utils.now());
            User savedUser = userService.saveUser(userRequest);
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(UserResponse.fromUser(savedUser));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error creating user: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @Valid @RequestBody User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        ResponseEntity<?> response = getUserById(id);
        HttpStatusCode status = response.getStatusCode();
        if(status != HttpStatus.OK) {
            return response;
        }

        User existing = userService.getUserById(id);
        if (existing != null) {
            if(existing.getRole().getId() != user.getRole().getId()) {
                UserRole r = userRoleService.getUserRoleById(user.getRole().getId());
                if(r != null) {
                    existing.setRole(r);
                } else {
                    return ResponseEntity.badRequest()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(jsonOB.add("message", "The role used does not exist.").build().toString());
                }
            }
            existing.setFullName(user.getFullName());
            existing.setEmail(user.getEmail());
            existing.setAddress(user.getAddress());
            existing.setBirthdate(user.getBirthdate());
            existing.setGender(user.getGender());
            existing.setUpdatedAt(Utils.now());
            try {
                User saved = userService.saveUser(existing);
                return ResponseEntity.ok(UserResponse.fromUser(saved));
            } catch (Exception e) { }
        }
        return ResponseEntity.internalServerError().body("Could not update user! Try again...");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        ResponseEntity<?> response = getUserById(id);
        HttpStatusCode status = response.getStatusCode();

        if(status == HttpStatus.OK) {
            userService.deleteUser(id);
            return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(jsonOB.add("message", "User with ID " + id + " has been deleted!").build().toString());
        }

        return response;
    }
}
