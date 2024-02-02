package com.projects.banking.Services;

import com.projects.banking.DTO.UserRequest;
import com.projects.banking.Entities.UserEntity;
import com.projects.banking.Helpers.IbanGenerator;
import com.projects.banking.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private static final String NL_IBAN_PREFIX = "NL";
    private static final String NL_BRANCH_CODE = "ABCD";
    private static final Random RANDOM = new Random(14);
    public String customerGenerateIban() {
        return IbanGenerator.generateIban(NL_IBAN_PREFIX, NL_BRANCH_CODE, RANDOM.toString());
    }

    /**
     *
     * @param userRequest userRequest
     * @return UserEntity
     */
    public UserEntity saveCustomer(UserRequest userRequest) {
        UserEntity userEntity = new UserEntity();
        System.out.println("customerGenerateIban:: "+customerGenerateIban());
        System.out.println("generateRandomOTP:: "+generateRandomOTP());
        userEntity.setIBAN(customerGenerateIban());
        if(!userRequest.getName().isEmpty()) {
            userEntity.setName(userRequest.getName());
        }
        userEntity.setUsername(userRequest.getUsername());
        userEntity.setPassword("123456");
        userEntity.setAddress(userRequest.getAddress());
        userEntity.setDateOfBirth(userRequest.getDateOfBirth());
        userEntity.setIdDocument(userRequest.getIdDocument());
        userEntity.setAccountType("Saving");
        userEntity.setOTP(generateRandomOTP());
        userEntity.setIsVerified(0);
        userEntity.setBalance(0.00);
        return userRepository.save(userEntity);
    }

    /**
     *
     * @return String
     */
    private static String generateRandomOTP() {
        Random RANDOM = new Random(6);
        return RANDOM.toString();
    }
}
