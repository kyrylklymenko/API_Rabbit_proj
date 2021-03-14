package com.example.demo.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class UserBalChangeDTO {
    UUID userId;
    int balanceChange;
}
