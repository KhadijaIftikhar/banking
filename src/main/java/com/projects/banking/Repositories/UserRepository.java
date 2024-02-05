package com.projects.banking.Repositories;

import com.projects.banking.Entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    public UserEntity findByUsername(String username);
    public UserEntity findByUsernameAndPassword (String username, String password);

}
