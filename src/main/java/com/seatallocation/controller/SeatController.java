package com.seatallocation.controller;

import com.seatallocation.dto.BookingRequest;
import com.seatallocation.dto.BookingResponse;
import com.seatallocation.dto.GroupBookingRequest;
import com.seatallocation.dto.OccupancyResponse;
import com.seatallocation.dto.SeatResponse;
import com.seatallocation.service.SeatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/seats")
@RequiredArgsConstructor
public class SeatController {

    private final SeatService seatService;

    @GetMapping
    public ResponseEntity<List<SeatResponse>> getAllSeats() {
        return ResponseEntity.ok(seatService.getAllSeats());
    }

    @PostMapping("/book")
    public ResponseEntity<BookingResponse> bookSeat(@Valid @RequestBody BookingRequest request) {
        BookingResponse response = seatService.bookSeat(request.getSeat());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/group-book")
    public ResponseEntity<BookingResponse> groupBook(@Valid @RequestBody GroupBookingRequest request) {
        BookingResponse response = seatService.bookGroupSeats(request.getCount());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/occupancy")
    public ResponseEntity<OccupancyResponse> getOccupancy() {
        return ResponseEntity.ok(seatService.getOccupancy());
    }

    @PostMapping("/reset")
    public ResponseEntity<BookingResponse> resetSeats() {
        return ResponseEntity.ok(seatService.resetAllSeats());
    }
}
