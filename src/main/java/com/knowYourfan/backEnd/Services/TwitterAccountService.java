package com.knowYourfan.backEnd.Services;

import com.knowYourfan.backEnd.Entities.TwitterAccount;
import com.knowYourfan.backEnd.Repositories.TwitterAccountRepository;
import com.knowYourfan.backEnd.Repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class TwitterAccountService {
    private final TwitterAccountRepository twitterAccountRepository;
    private final UserRepository userRepository;
    private final TwitterFactoryService twitterFactoryService;
    private static final String CALLBACK_URL = System.getenv("CALLBACK_URL");

    private final Map<String, RequestToken> requestTokenStorage = new ConcurrentHashMap<>();

    public URI getOAuthRedirectURL(Long userId) {
        try {
            Twitter twitter = twitterFactoryService.getTwitterInstance();
            RequestToken requestToken = twitter.getOAuthRequestToken(CALLBACK_URL + "?userId=" + userId);
            requestTokenStorage.put(requestToken.getToken(), requestToken);
            return URI.create(requestToken.getAuthorizationURL());
        } catch (TwitterException e) {
            throw new RuntimeException("Failed to get request token", e);
        }
    }

    @Transactional
    public void fetchAndSaveTwitterAccount(String oauthToken, String oauthVerifier, Long currentUserId) {
        com.knowYourfan.backEnd.Entities.User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não existe"));

        try {
            Twitter twitter = twitterFactoryService.getTwitterInstance();
            RequestToken requestToken = requestTokenStorage.get(oauthToken);
            AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, oauthVerifier);

            // Salvar tokens no TwitterAccount
            TwitterAccount account = twitterAccountRepository.findByUserId(currentUserId)
                    .orElseGet(TwitterAccount::new);

            account.setUsername(accessToken.getScreenName());
            account.setProfileUrl("https://twitter.com/" + accessToken.getScreenName());
            account.setAccessToken(accessToken.getToken());
            account.setAccessTokenSecret(accessToken.getTokenSecret());
            account.setUser(currentUser);
            currentUser.setTwitterAccount(account);

            twitterAccountRepository.save(account);
            updateTwitterAccount(account);

            currentUser.increaseXp(1000);

        } catch (TwitterException e) {
            throw new RuntimeException("Falha ao autenticar com o Twitter", e);
        }

    }

    @Transactional
    public void updateTwitterAccount(TwitterAccount account) {
//        try {
//            Twitter twitter = twitterFactoryService.getTwitterInstance(account.getAccessToken(), account.getAccessTokenSecret());
//            long furiaUserId = getUserIdByUsername(twitter, "FURIA");
//            boolean followsFuria = checkIfUserFollows(twitter, account.getUsername(), furiaUserId);
//            int interactions = countUserInteractions(twitter, account.getUsername());
//
//            account.setFollowsFuria(followsFuria);
//            account.setInteractions(interactions);
//            twitterAccountRepository.save(account);
//        } catch (TwitterException e) {
//            e.printStackTrace();
//        }

        boolean prevFollowsFuria = account.isFollowsFuria();
        int prevInteractions = account.getInteractions();

        boolean followsFuria = true;
        int interactions = 82;
        account.setFollowsFuria(followsFuria);
        account.setInteractions(interactions);
        System.out.println("acesso a API bloqueado devido ao plano gratuito - Usando valores padrão para follows e interações.");

        com.knowYourfan.backEnd.Entities.User u = account.getUser();
        if(followsFuria && !prevFollowsFuria) u.increaseXp(1000);
        u.increaseXp((interactions-prevInteractions)*10);
    }

    @Transactional
    public void updateAllTwitterAccounts() {
        List<TwitterAccount> accounts = twitterAccountRepository.findAll();

        for (TwitterAccount account : accounts) {
            updateTwitterAccount(account);
        }
    }

    private long getUserIdByUsername(Twitter twitter, String username) throws TwitterException {
        User user = twitter.showUser(username);
        return user.getId();
    }

    private boolean checkIfUserFollows(Twitter twitter, String sourceUsername, long targetUserId) throws TwitterException {
        long sourceUserId = getUserIdByUsername(twitter, sourceUsername);
        return twitter.showFriendship(sourceUserId, targetUserId).isSourceFollowingTarget();
    }

    private int countUserInteractions(Twitter twitter, String username) throws TwitterException {
        int interactionCount = 0;
        List<Status> statuses = twitter.getUserTimeline(username);
        for (Status status : statuses) {
            interactionCount += status.getRetweetCount();
            interactionCount += status.getFavoriteCount();
        }
        return interactionCount;
    }
}
