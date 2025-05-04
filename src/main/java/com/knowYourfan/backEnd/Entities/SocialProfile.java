package com.knowYourfan.backEnd.Entities;

import com.knowYourfan.backEnd.DTOs.SocialProfileDTO;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Data
@Table
public class SocialProfile {
    @Id
    private String url;

    @OneToOne(mappedBy = "socialProfile", cascade = CascadeType.ALL)
    @Getter
    private User user;

    public SocialProfile() {}
    public SocialProfile(User user) {
        this.user = user;
        user.setSocialProfile(this);
        url = "api/user/" + user.getId();
    }

}
