package com.knowYourfan.backEnd.Services;

import org.springframework.stereotype.Service;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;

@Service
public class TwitterFactoryService {

    public Twitter getTwitterInstance() {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(System.getenv("TWITTER_CONSUMER_KEY"))
                .setOAuthConsumerSecret(System.getenv("TWITTER_CONSUMER_SECRET"));
        return new TwitterFactory(cb.build()).getInstance();
    }

    public Twitter getTwitterInstance(String accessToken, String accessTokenSecret) {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(System.getenv("TWITTER_CONSUMER_KEY"))
                .setOAuthConsumerSecret(System.getenv("TWITTER_CONSUMER_SECRET"))
                .setOAuthAccessToken(accessToken)
                .setOAuthAccessTokenSecret(accessTokenSecret);
        return new TwitterFactory(cb.build()).getInstance();
    }

    public void testConnection() {
        try {
            Twitter twitter = getTwitterInstance();
            User user = twitter.verifyCredentials(); // tenta obter dados do user
            System.out.println("Conexão bem-sucedida! Usuário autenticado: " + user.getScreenName());
        } catch (TwitterException e) {
            System.err.println("Erro Twitter: " + e.getErrorMessage());
            System.err.println("Código HTTP: " + e.getStatusCode());
            System.err.println("Stacktrace: ");
            e.printStackTrace();
        }
    }
}
