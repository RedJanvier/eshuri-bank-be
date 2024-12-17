package bank.backend.bms.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @GetMapping("/current-user")
    public Object getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getPrincipal(); // Return the authenticated user's details
    }

    @GetMapping("/check-role")
    public String checkUserRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
            return "You are an Admin!";
        } else if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("MANAGER"))) {
            return "You are a Manager!";
        } else {
            return "You are a User!";
        }
    }
}
