package com.knowYourfan.backEnd.Services;

import com.knowYourfan.backEnd.DTOs.*;
import com.knowYourfan.backEnd.Endereco;
import com.knowYourfan.backEnd.Entities.*;
import com.knowYourfan.backEnd.Repositories.SocialProfileRepository;
import com.knowYourfan.backEnd.Repositories.UserRepository;
import com.knowYourfan.backEnd.Security.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final SocialProfileRepository profileRepository;

    private UserResponseDTO generateResponseDTO(User u) {
        return new UserResponseDTO(u.getId(),u.getNickname(),u.getXp(),u.getLevel());
    }
    private List<UserResponseDTO> generateResponseDTOList(List<User> users) {
        List<UserResponseDTO> dtos = new ArrayList<>();
        for (User u : users) {
            dtos.add(generateResponseDTO(u));
        }
        return dtos;
    }
    private UserFullResponseDTO generateFullResponseDTO(User u) {
        Endereco e = u.getAddress();
        EnderecoResponseDTO enderecoDTO = new EnderecoResponseDTO(e.getPais(),e.getEstado(),e.getCidade(),e.getCEP());

        TwitterAccount x = u.getTwitterAccount();
        String xStr = "";
        boolean xFollows = false;
        int xInteractions = 0;
        if(x!=null){
            xStr = x.getProfileUrl();
            xFollows = x.isFollowsFuria();
            xInteractions = x.getInteractions();
        }

        TwitchAccount twitch = u.getTwitchAccount();
        String twitchStr = "";
        if(twitch!=null) twitchStr = twitch.getProfileUrl();

        InstagramAccount insta = u.getInstagramAccount();
        String instaStr = "";
        if(insta!=null) instaStr = insta.getProfileUrl();

        return new UserFullResponseDTO(u.getId(),u.getName(), u.getNickname(),u.getCpf().getCpf(), u.getEmail(), enderecoDTO,
                u.getInterests(), u.getXp(), u.getLevel(),u.getCash(),u.getCreatedAt(),u.isCpfVerified(),xStr,xFollows,
                xInteractions,twitchStr,instaStr,u.getEvents(),u.getPurchases(),u.getOutsideActivities());
    }




    public UserFullResponseDTO createUser(UserCreationDTO dto) {
        User newUser = userRepository.save(new User(dto,passwordEncoder));
        profileRepository.save(new SocialProfile(newUser));

        return generateFullResponseDTO(newUser);
    }

    public AuthenticationResponse login(AuthenticationRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não existe"));

        if (!user.matchesPassword(request.password(), passwordEncoder)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Senha incorreta");
        }

        String token = jwtService.generateToken(user);
        return new AuthenticationResponse(token);
    }

    public List<UserResponseDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return generateResponseDTOList(users);
    }

    public List<UserResponseDTO> getUsersByXp() {
        List<User> users = userRepository.findAll(Sort.by("xp").descending());
        return generateResponseDTOList(users);
    }

    public UserFullResponseDTO getUserInfo(@RequestParam Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não existe"));

        return generateFullResponseDTO(user);
    }

    @Transactional
    public void buy(@RequestParam Long id,@RequestParam Float price){
        User u = userRepository.findById(id).orElseThrow();
        u.decreaseCash(price);
    }

    @Transactional
    public void setEvents(Long id,String events){
        User u = userRepository.findById(id).orElseThrow();
        u.setEvents(events);
        u.increaseXp(500);
    }

    @Transactional
    public void setPurchases(Long id,String purchases){
        User u = userRepository.findById(id).orElseThrow();
        u.setPurchases(purchases);
        u.increaseXp(500);
    }

    @Transactional
    public void setOutsideActivities(Long id,String outsideActivities){
        User u = userRepository.findById(id).orElseThrow();
        u.setOutsideActivities(outsideActivities);
        u.increaseXp(500);
    }

}
