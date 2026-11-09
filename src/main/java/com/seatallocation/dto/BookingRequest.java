package com.seatallocation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingRequest {

    @NotBlank(message = "Seat label is required")
    @Pattern(regexp = "^[A-H]([1-9]|10)$", message = "Invalid seat format. Use format like A5 or H10")
    private String seat;
}
