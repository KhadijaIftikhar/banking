package com.projects.banking;

import com.projects.banking.Entities.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserTest {
    UserEntity userEntity;
    @BeforeEach
    public void setUp(){
        userEntity=new UserEntity();
    }
    @Test
    void getId() {
        Long idValue=4L;
        userEntity.setId(idValue);

        assertEquals(userEntity.getId(), idValue);
    }

    @Test
    void setId() {
        Long idValue=4L;
        userEntity.setId(idValue);
        assertEquals(userEntity.getId(), idValue);
    }

    @Test
    void getName() {
        String name="test user";
        userEntity.setName(name);
        assertEquals(name,userEntity.getName());
    }

    @Test
    void setName() {
        String name="test user";
        userEntity.setName(name);
        assertEquals(name,userEntity.getName());
    }


}
