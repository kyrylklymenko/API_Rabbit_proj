package com.example.demo.api.dto.workerDTO;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WorkerDataFormat {

    @NotNull
    String workerName;

    @NotNull
    String workerSurname;
}
