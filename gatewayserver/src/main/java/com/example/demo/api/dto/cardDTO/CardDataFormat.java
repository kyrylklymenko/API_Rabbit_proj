package com.example.demo.api.dto.cardDTO;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class CardDataFormat {
    String cardNum;
    int regMonth;
    int regYear;
    int CVC;
    UUID ownerId;
    UUID workerId;
}
