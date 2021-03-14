package com.example.demo.api.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class CardCloseDataFormat {
    UUID cardId;
}
