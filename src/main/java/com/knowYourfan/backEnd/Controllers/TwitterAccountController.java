package com.knowYourfan.backEnd.Controllers;

import com.knowYourfan.backEnd.Entities.User;
import com.knowYourfan.backEnd.Repositories.UserRepository;
import com.knowYourfan.backEnd.Security.JwtService;
import com.knowYourfan.backEnd.Services.TwitterAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/twitter")
public class TwitterAccountController {
    private final TwitterAccountService twitterAccountService;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @GetMapping("/auth")
    public ResponseEntity<Map<String, String>> getTwitterAuthUrl(@RequestParam Long userId) {
        URI twitterOAuthUrl = twitterAccountService.getOAuthRedirectURL(userId);
        return ResponseEntity.ok(Map.of("url", twitterOAuthUrl.toString()));
    }

    @GetMapping("/auth/callback")
    public ResponseEntity<?> twitterCallback(@RequestParam String oauth_token,
                                             @RequestParam String oauth_verifier,
                                             @RequestParam Long userId) {

        User u = userRepository.findById(userId).orElseThrow();
        String jwt = jwtService.generateToken(u);

        try {
            twitterAccountService.fetchAndSaveTwitterAccount(oauth_token, oauth_verifier, userId);

            URI redirect = URI.create("http://localhost:3000?tab=socials&jwt=" + jwt);
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(redirect);
            return new ResponseEntity<>(headers, HttpStatus.FOUND);

        } catch (Exception ex) {
            URI redirect = URI.create("http://localhost:3000?error=" + ex.getMessage());
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(redirect);
            return new ResponseEntity<>(headers, HttpStatus.FOUND);
        }
    }

}
