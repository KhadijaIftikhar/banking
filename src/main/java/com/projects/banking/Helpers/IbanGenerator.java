package com.projects.banking.Helpers;

import org.iban4j.CountryCode;
import org.iban4j.Iban;


public class IbanGenerator {

    public static String generateIban(String countryCode, String bankCode, String accountNumber) {
        Iban iban = new Iban.Builder()
                .countryCode(CountryCode.valueOf(countryCode))
                .bankCode(bankCode)
                .accountNumber(accountNumber)
                .build();
        return iban.toString();
    }

}

