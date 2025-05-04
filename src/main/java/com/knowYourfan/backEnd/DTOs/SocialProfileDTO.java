package com.knowYourfan.backEnd.DTOs;

import java.time.LocalDateTime;
import java.util.Set;

public record SocialProfileDTO(String nick, Set<String> interests, int xp, String level, LocalDateTime createdAt,
                               boolean cpfVerified, String twitter, String twitch, String instagram) {
}
