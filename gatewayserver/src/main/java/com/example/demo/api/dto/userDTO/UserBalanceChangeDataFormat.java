package com.example.demo.api.dto.userDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class UserBalanceChangeDataFormat {
    UUID userId;
    int balanceChange;
}
