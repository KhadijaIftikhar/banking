package com.projects.banking.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerOverviewResponse {
    private String accountNumber;
    private String accountType;
    private double balance;
    private String currency;
}
