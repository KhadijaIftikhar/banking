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
    private static final String NL_BRANCH_CODE = "ABNA";
    private static final Random RANDOM = new Random();
    private String customerGenerateIban() {
        long randomNumber = generateRandomNumber(10, RANDOM);
        return IbanGenerator.generateIban(NL_IBAN_PREFIX, NL_BRANCH_CODE, Long.toString(randomNumber));
    }

    /**
     *
     * @param userRequest userRequest
     * @return UserEntity
     */
    public UserEntity saveCustomer(UserRequest userRequest) {
        UserEntity userEntity = new UserEntity();
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
        userEntity.setMobileNumber(userRequest.getMobileNumber());
        userEntity.setIsVerified(0);
        userEntity.setBalance(0.00);
        return userRepository.save(userEntity);
    }

    public UserEntity findCustomerByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     *
     * @return String
     */
    private static String generateRandomOTP() {
       long otp = generateRandomNumber(6, RANDOM);
       return Long.toString(otp);
    }

    private static long generateRandomNumber(int digits, Random random) {
        // Ensure the digits parameter is valid
        if (digits <= 0) {
            throw new IllegalArgumentException("Number of digits must be greater than 0");
        }

        // Calculate the minimum and maximum values for the specified number of digits
        long min = (long) Math.pow(10, digits - 1);
        long max = (long) Math.pow(10, digits) - 1;

        // Generate a positive random number within the specified range
        return min + Math.abs(random.nextLong()) % (max - min + 1);
    }

    public UserEntity updateCustomerVerification(UserEntity userEntity, int isVerified) {
        try {
            userEntity.setIsVerified(isVerified);
            return userRepository.save(userEntity);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
