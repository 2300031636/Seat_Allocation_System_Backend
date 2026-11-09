package com.seatallocation.dto;

import com.seatallocation.entity.SeatStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeatResponse {

    private Long id;
    private String rowName;
    private Integer seatNumber;
    private String seatLabel;
    private SeatStatus status;
}
