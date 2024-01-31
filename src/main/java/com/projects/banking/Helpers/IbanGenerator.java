package com.projects.banking.Helpers;

import java.util.Random;

public class IbanGenerator {

    public static String generateIban(String countryCode) {

        // Generate 18 random digits for the BBAN (Basic Bank Account Number)
        String bban = generateRandomDigits(18);

        // Combine all parts to form the complete IBAN
        String iban = countryCode + "00" + bban;

        // Calculate the check digits
        int checkDigits = calculateCheckDigits(iban);

        // Replace the placeholder "00" with the calculated check digits
        iban = countryCode + String.format("%02d", checkDigits) + bban;

        return iban;
    }

    private static String generateRandomDigits(int length) {
        Random random = new Random();
        StringBuilder digits = new StringBuilder();

        for (int i = 0; i < length; i++) {
            digits.append(random.nextInt(10));
        }

        return digits.toString();
    }

    private static int calculateCheckDigits(String iban) {
        // Convert letters to numbers (A=10, B=11, ..., Z=35)
        String numericIban = iban.substring(4) + iban.substring(0, 2) + "00";
        StringBuilder numericIbanWithCheck = new StringBuilder();
        for (char c : numericIban.toCharArray()) {
            if (Character.isLetter(c)) {
                numericIbanWithCheck.append(Character.getNumericValue(c - 55));
            } else {
                numericIbanWithCheck.append(c);
            }
        }

        // Calculate check digits
        int modResult = new java.math.BigInteger(numericIbanWithCheck.toString()).mod(new java.math.BigInteger("97")).intValue();
        return 98 - modResult;
    }

}

