package com.knowYourfan.backEnd.Entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

@Entity
@Data
@Table
public class TwitchAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "twitchAccount", cascade = CascadeType.ALL)
    private User user;

    private String username;

    @Getter
    private String profileUrl;

    private boolean followsFuria;
}
