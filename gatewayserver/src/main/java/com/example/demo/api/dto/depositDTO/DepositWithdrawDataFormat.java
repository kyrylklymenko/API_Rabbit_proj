package com.example.demo.api.dto.depositDTO;


import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class DepositWithdrawDataFormat {
    UUID depositId;
    UUID workerId;
    String withdrawDate;
}
