package com.seatallocation.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupBookingRequest {

    @NotNull(message = "Seat count is required")
    @Min(value = 1, message = "At least 1 seat is required")
    @Max(value = 10, message = "Maximum 10 contiguous seats per row")
    private Integer count;
}
