package com.knowYourfan.backEnd.Controllers;

import com.knowYourfan.backEnd.DTOs.AuthenticationResponse;
import com.knowYourfan.backEnd.DTOs.SocialProfileDTO;
import com.knowYourfan.backEnd.Entities.SocialProfile;
import com.knowYourfan.backEnd.Services.SocialProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/profile") @RequiredArgsConstructor
public class SocialProfileController {
    private final SocialProfileService service;

    @PostMapping
    public SocialProfile create(@RequestBody SocialProfile s) { return service.create(s); }

    @GetMapping
    public List<SocialProfile> list() { return service.list(); }

}
