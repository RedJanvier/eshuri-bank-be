package bank.backend.bms.dtos;

import java.sql.Timestamp;

import bank.backend.bms.models.User;
import bank.backend.bms.models.UserRole;
import lombok.Data;

@Data
public class UserResponse {
    private Long id;
    private String fullName;
    private String email;
    private String address;
    private String gender;
    private UserRole role;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public static UserResponse fromUser(User user) {
        UserResponse response = new UserResponse();

        response.setId(user.getId());
        response.setAddress(user.getAddress());
        response.setEmail(user.getEmail());
        response.setFullName(user.getFullName());
        response.setGender(user.getGender());
        response.setRole(user.getRole());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());

        return response;
    }
}
