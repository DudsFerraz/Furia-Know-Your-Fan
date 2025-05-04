package com.knowYourfan.backEnd.Services;

import com.knowYourfan.backEnd.DTOs.SocialProfileDTO;
import com.knowYourfan.backEnd.Entities.*;
import com.knowYourfan.backEnd.Repositories.SocialProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SocialProfileService {
    private final SocialProfileRepository repo;

    public SocialProfile create(SocialProfile s) { return repo.save(s); }

    public List<SocialProfile> list() { return repo.findAll(); }

    private SocialProfileDTO generateSocialProfileDTO(SocialProfile s) {
        User u = s.getUser();
        TwitterAccount x = u.getTwitterAccount();
        String xStr = "";
        if(x!=null) xStr = x.getProfileUrl();

        TwitchAccount twitch = u.getTwitchAccount();
        String twitchStr = "";
        if(twitch!=null) twitchStr = twitch.getProfileUrl();

        InstagramAccount insta = u.getInstagramAccount();
        String instaStr = "";
        if(insta!=null) instaStr = insta.getProfileUrl();

        return new SocialProfileDTO(u.getNickname(),u.getInterests(),u.getXp(),u.getLevel(),
                u.getCreatedAt(),u.isCpfVerified(),xStr,twitchStr,instaStr);
    }

    public SocialProfileDTO getProfile(String id){
        String url = "api/user/"+id;
        SocialProfile profile = repo.findById(url)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Perfil não existe"));

        return generateSocialProfileDTO(profile);
    }
}
