package com.knowYourfan.backEnd.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

@Entity
@Data
@Table
public class TwitterAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @OneToOne(mappedBy = "twitterAccount", cascade = CascadeType.ALL)
    private User user;

    private String username;

    @Getter
    private String profileUrl;

    @Getter
    private boolean followsFuria = false;

    @Getter
    private int interactions = 0;

    private String accessToken;
    private String accessTokenSecret;
}
