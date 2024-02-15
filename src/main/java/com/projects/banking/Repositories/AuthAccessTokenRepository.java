package com.projects.banking.Repositories;

import com.projects.banking.Entities.AuthAccessTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthAccessTokenRepository  extends JpaRepository<AuthAccessTokenEntity, Long> {

    public void deleteAllByUserId(Long userId);
    public AuthAccessTokenEntity findByUserId(Long userId);
}
