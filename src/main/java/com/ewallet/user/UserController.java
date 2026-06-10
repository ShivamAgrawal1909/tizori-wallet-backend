package com.ewallet.user;

import com.ewallet.config.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    // Register API
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody User user) {

        Map<String, Object> response = new HashMap<>();

        try {
            User savedUser = userService.registerUser(user);

            response.put("message", "User registered successfully");
            response.put("userId", savedUser.getId());
            response.put("name", savedUser.getName());
            response.put("email", savedUser.getEmail());
            response.put("role", savedUser.getRole());

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Login Request
    static class LoginRequest {
        public String email;
        public String password;
    }

    // Login API
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(
            @RequestBody LoginRequest request) {

        User user = userService.loginUser(
                request.email,
                request.password
        );

        String token = jwtUtil.generateToken(
                user.getEmail(),
                user.getRole().name()
        );

        Map<String, Object> response = new HashMap<>();

        response.put("token", token);
        response.put("userId", user.getId());
        response.put("name", user.getName());
        response.put("email", user.getEmail());
        response.put("role", user.getRole());

        return ResponseEntity.ok(response);
    }

    // Get User by ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(
            @PathVariable Long id) {

        User user = userService.getUserById(id);

        return ResponseEntity.ok(user);
    }

    // Get User by Email for transfer lookup
    @GetMapping("/by-email")
    public ResponseEntity<Map<String, Object>> getUserByEmail(
            @RequestParam String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Receiver account not found!"));

        Map<String, Object> response = new HashMap<>();

        response.put("userId", user.getId());
        response.put("name", user.getName());
        response.put("email", user.getEmail());
        response.put("role", user.getRole());

        return ResponseEntity.ok(response);
    }
}