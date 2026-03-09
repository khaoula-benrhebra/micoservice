package com.microservice.userservice.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiErrorResponseDto {

    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
}
