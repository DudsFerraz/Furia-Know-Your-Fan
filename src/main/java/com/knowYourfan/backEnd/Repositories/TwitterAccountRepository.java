package com.knowYourfan.backEnd.Repositories;

import com.knowYourfan.backEnd.Entities.TwitterAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TwitterAccountRepository extends JpaRepository<TwitterAccount, Long> {
    Optional<TwitterAccount> findByUserId(Long userId);

}
