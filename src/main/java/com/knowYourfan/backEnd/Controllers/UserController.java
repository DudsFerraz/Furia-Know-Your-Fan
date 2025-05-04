package com.knowYourfan.backEnd.Controllers;


import com.knowYourfan.backEnd.DTOs.*;
import com.knowYourfan.backEnd.Entities.User;
import com.knowYourfan.backEnd.Repositories.UserRepository;
import com.knowYourfan.backEnd.Security.JwtService;
import com.knowYourfan.backEnd.Services.SocialProfileService;
import com.knowYourfan.backEnd.Services.TwitterAccountService;
import com.knowYourfan.backEnd.Services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.view.RedirectView;

import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final SocialProfileService profileService;

    @PostMapping("/register")
    public UserFullResponseDTO createUser(@RequestBody UserCreationDTO dto) {
        return userService.createUser(dto);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest request) {
        try {
            AuthenticationResponse response = userService.login(request);
            return ResponseEntity.ok(response);
        } catch (ResponseStatusException ex) {
            return ResponseEntity
                    .status(ex.getStatusCode())
                    .body(Map.of(
                            "message", Objects.requireNonNull(ex.getReason()),
                            "status", "error"
                    ));
        } catch (Exception ex) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "message", "Erro interno do servidor",
                            "status", "error"
                    ));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProfile(@PathVariable String id){
        try {
            SocialProfileDTO response = profileService.getProfile(id);
            return ResponseEntity.ok(response);
        } catch (ResponseStatusException ex) {
            return ResponseEntity
                    .status(ex.getStatusCode())
                    .body(Map.of(
                            "message", Objects.requireNonNull(ex.getReason()),
                            "status", "error"
                    ));
        } catch (Exception ex) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "message", "Erro interno do servidor",
                            "status", "error"
                    ));
        }
    }

    @GetMapping("/get/all/xp")
    public List<UserResponseDTO> getUsersByXp() {
        return userService.getUsersByXp();
    }

    @GetMapping("/info")
    public ResponseEntity<?> getUserInfo(Long id) {
        try {
            UserFullResponseDTO response = userService.getUserInfo(id);
            return ResponseEntity.ok(response);
        } catch (ResponseStatusException ex) {
            return ResponseEntity
                    .status(ex.getStatusCode())
                    .body(Map.of(
                            "message", Objects.requireNonNull(ex.getReason()),
                            "status", "error"
                    ));
        } catch (Exception ex) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "message", "Erro interno do servidor",
                            "status", "error"
                    ));
        }
    }

    @PostMapping("/buy")
    public void buy(Long id,Float price){
        userService.buy(id, price);
    }

    @PostMapping("/events")
    public void setEvents(Long id,String events){
        userService.setEvents(id, events);
    }

    @PostMapping("/purchases")
    public void setPurchases(Long id,String purchases){
        userService.setPurchases(id, purchases);
    }

    @PostMapping("/outsideActivities")
    public void setOutsideActivities(Long id,String outsideActivities){
        userService.setOutsideActivities(id, outsideActivities);
    }

}
