package com.knowYourfan.backEnd.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.knowYourfan.backEnd.CPF;
import com.knowYourfan.backEnd.DTOs.UserCreationDTO;
import com.knowYourfan.backEnd.Endereco;
import com.knowYourfan.backEnd.EsportsEnum;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Column(nullable = false)
    private String name;

    @Getter
    @Column(nullable = false)
    private String nickname;

    @Getter
    @Column(nullable = false,unique = true)
    private CPF cpf;

    @Getter
    @Column(nullable = false, unique = true)
    private String email;

    @Getter
    @Column(nullable = false,length = 60)
    @JsonIgnore
    private String password;

    @Getter
    @Embedded
    @Column(nullable = false)
    private Endereco address;

    @ElementCollection(targetClass = EsportsEnum.class)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Set<EsportsEnum> interests;

    @Getter
    private int xp;

    @Getter
    private String level;

    @Getter
    private float cash;

    @Setter
    @Getter
    private boolean cpfVerified;

    private LocalDateTime createdAt;

    @Setter
    @OneToOne
    private SocialProfile socialProfile;

    @OneToOne
    private TwitterAccount twitterAccount;

    @OneToOne
    private InstagramAccount instagramAccount;

    @OneToOne
    private TwitchAccount twitchAccount;

    @Getter
    @Setter
    @Column(columnDefinition = "")
    private String events;

    @Getter
    @Setter
    @Column(columnDefinition = "")
    private String purchases;

    @Getter
    @Setter
    @Column(columnDefinition = "")
    private String outsideActivities;

    public User() {}
    public User(UserCreationDTO dto, PasswordEncoder encoder) {
        setName(dto.name());
        setNickname(dto.nickname());
        this.cpf = new CPF(dto.cpf());
        setEmail(dto.email());
        setPassword(dto.password(),encoder);
        this.address = new Endereco(dto.pais(), dto.estado(), dto.cidade(), dto.cep());
        this.interests = new HashSet<>();
        for (String interest : dto.interests()) {
            this.interests.add(EsportsEnum.valueOf(interest));
        }
        createdAt = LocalDateTime.now();
        xp = 100;
        level = "Filhote de Fera";
        this.cash = 0;
        cpfVerified = false;
        socialProfile = null;
    }

    public boolean matchesPassword(String rawPassword, PasswordEncoder encoder) {
        return encoder.matches(rawPassword, this.password);
    }
    private void increaseCash(float increment) {
        this.cash += increment;
    }
    public void increaseXp(int value) {
        this.xp += value;
        increaseCash((float) value /100);
        setLevel();
    }
    private void setLevel(){
        if(xp>1000 && xp<3000) this.level = "Pantera em treinamento";
        if(xp>3000) this.level = "Pantera Furiosa";
    }

    public void setPassword(String password, PasswordEncoder encoder) {
        if(password == null || password.length() < 5) throw new IllegalArgumentException("Senha invalida");
        this.password = encoder.encode(password);
    }
    public void setEmail(String email) {
        if(email == null || email.isBlank()) throw new IllegalArgumentException("Email invalida");
        this.email = email;
    }
    public Set<String> getInterests() {
        Set<String> interestsString = new HashSet<>();
        for (EsportsEnum interest : this.interests) {
            interestsString.add(interest.toString());
        }
        return interestsString;
    }
    public void setName(String name) {
        if(name == null || name.isBlank()) throw new IllegalArgumentException("Nome invalido");
        this.name = name;
    }
    public void setNickname(String nickname) {
        if(nickname == null || nickname.isBlank()) throw new IllegalArgumentException("Nickname invalido");
        this.nickname = nickname;
    }
    public float decreaseCash(float value) {
        return cash -= value;
    }
}
