package com.knowYourfan.backEnd.DTOs;

import com.knowYourfan.backEnd.Entities.InstagramAccount;
import com.knowYourfan.backEnd.Entities.TwitchAccount;
import com.knowYourfan.backEnd.Entities.TwitterAccount;
import com.knowYourfan.backEnd.EsportsEnum;

import java.time.LocalDateTime;
import java.util.Set;

public record UserFullResponseDTO(Long id, String name, String nickname, String cpf, String email, EnderecoResponseDTO endereco,
                                  Set<String> interests, int xp, String level, float cash, LocalDateTime createdAt,
                                  boolean verifiedCpf, String twitter, boolean twitterFollowsFuria, int twitterInteractions,
                                  String twitch, String instagram, String events, String purchases, String outsideActivities) {
}
