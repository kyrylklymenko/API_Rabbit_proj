package com.example.demo.api.dto.depositDTO;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class DepositDataFormat {
    @NotNull
    int balance;

    @NotNull
    String openingDate;

    @NotNull
    UUID ownerId;

    @NotNull
    UUID workerId;
}
