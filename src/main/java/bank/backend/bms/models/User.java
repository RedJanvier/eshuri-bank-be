package bank.backend.bms.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @NotBlank(message = "Email is required.")
    @Email
    @Column(unique = true, nullable = false)
    private String email;

    private String address;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private UserRole role;

    private Date birthdate;

    @Column(nullable = false)
    @Pattern(regexp = "female|male", message = "Gender must be either 'female' or 'male'")
    private String gender;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    @Column(nullable = false)
    @Size(min = 5, message = "Password length must be at least 5 characters")
    private String password;
}
