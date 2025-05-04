package com.knowYourfan.backEnd.config;

import com.knowYourfan.backEnd.Services.TwitterAccountService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TwitterAccountScheduler {

    private final TwitterAccountService twitterAccountService;

    public TwitterAccountScheduler(TwitterAccountService twitterAccountService) {
        this.twitterAccountService = twitterAccountService;
    }

    @Scheduled(cron = "0 0 9 * * ?", zone = "America/Sao_Paulo")
    public void refreshAllTwitterInteractions() {
        twitterAccountService.updateAllTwitterAccounts();
    }
}
