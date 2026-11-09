package com.seatallocation.service;

import com.seatallocation.dto.BookingResponse;
import com.seatallocation.dto.OccupancyResponse;
import com.seatallocation.dto.SeatResponse;
import com.seatallocation.entity.Seat;
import com.seatallocation.entity.SeatStatus;
import com.seatallocation.exception.ContiguousSeatsUnavailableException;
import com.seatallocation.exception.SeatAlreadyBookedException;
import com.seatallocation.exception.SeatNotFoundException;
import com.seatallocation.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SeatService {

    private static final String[] ROWS = {"A", "B", "C", "D", "E", "F", "G", "H"};
    private static final int SEATS_PER_ROW = 10;
    private static final long TOTAL_SEATS = 80L;

    private final SeatRepository seatRepository;

    @Transactional(readOnly = true)
    public List<SeatResponse> getAllSeats() {
        return seatRepository.findAllByOrderByRowNameAscSeatNumberAsc()
                .stream()
                .map(this::toSeatResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public BookingResponse bookSeat(String seatLabel) {
        SeatIdentifier identifier = parseSeatLabel(seatLabel);
        Seat seat = seatRepository.findByRowNameAndSeatNumberWithLock(identifier.rowName(), identifier.seatNumber())
                .orElseThrow(() -> new SeatNotFoundException("Seat not found: " + seatLabel.toUpperCase()));

        if (seat.getStatus() == SeatStatus.BOOKED) {
            throw new SeatAlreadyBookedException("Seat already booked.");
        }

        if (seat.getStatus() == SeatStatus.BLOCKED) {
            throw new SeatAlreadyBookedException("Seat is currently blocked.");
        }

        seat.setStatus(SeatStatus.BOOKED);
        seatRepository.save(seat);

        return BookingResponse.builder()
                .success(true)
                .message("Seat " + seat.getSeatLabel() + " booked successfully.")
                .seat(seat.getSeatLabel())
                .build();
    }

    @Transactional
    public BookingResponse bookGroupSeats(int count) {
        List<String> allocatedLabels = searchContiguousSeats(count);

        if (allocatedLabels.isEmpty()) {
            throw new ContiguousSeatsUnavailableException("Contiguous seats unavailable.");
        }

        SeatIdentifier first = parseSeatLabel(allocatedLabels.get(0));
        SeatIdentifier last = parseSeatLabel(allocatedLabels.get(allocatedLabels.size() - 1));

        List<Seat> seats = seatRepository.findSeatsInRowRangeWithLock(
                first.rowName(),
                first.seatNumber(),
                last.seatNumber()
        );

        if (seats.size() != count) {
            throw new ContiguousSeatsUnavailableException("Contiguous seats unavailable.");
        }

        for (Seat seat : seats) {
            if (seat.getStatus() != SeatStatus.FREE) {
                throw new ContiguousSeatsUnavailableException("Contiguous seats unavailable.");
            }
        }

        seats.forEach(seat -> seat.setStatus(SeatStatus.BOOKED));
        seatRepository.saveAll(seats);

        List<String> bookedLabels = seats.stream()
                .map(Seat::getSeatLabel)
                .collect(Collectors.toList());

        return BookingResponse.builder()
                .success(true)
                .message("Group booking successful for " + count + " seats.")
                .seats(bookedLabels)
                .build();
    }

    @Transactional(readOnly = true)
    public OccupancyResponse getOccupancy() {
        long booked = seatRepository.countByStatus(SeatStatus.BOOKED);
        long available = seatRepository.countByStatus(SeatStatus.FREE);
        double occupancy = (booked * 100.0) / TOTAL_SEATS;

        return OccupancyResponse.builder()
                .booked(booked)
                .available(available)
                .occupancy(Math.round(occupancy * 100.0) / 100.0)
                .build();
    }

    @Transactional
    public BookingResponse resetAllSeats() {
        List<Seat> seats = seatRepository.findAll();
        seats.forEach(seat -> seat.setStatus(SeatStatus.FREE));
        seatRepository.saveAll(seats);

        return BookingResponse.builder()
                .success(true)
                .message("All seats have been reset to FREE.")
                .build();
    }

    private List<String> searchContiguousSeats(int count) {
        if (count < 1 || count > SEATS_PER_ROW) {
            return List.of();
        }

        for (String row : ROWS) {
            List<Seat> rowSeats = seatRepository.findByRowNameOrderBySeatNumberAsc(row);

            for (int startIndex = 0; startIndex <= rowSeats.size() - count; startIndex++) {
                boolean allFree = true;
                List<String> candidateLabels = new ArrayList<>();

                for (int offset = 0; offset < count; offset++) {
                    Seat seat = rowSeats.get(startIndex + offset);

                    if (seat.getSeatNumber() != startIndex + offset + 1 || seat.getStatus() != SeatStatus.FREE) {
                        allFree = false;
                        break;
                    }

                    candidateLabels.add(seat.getSeatLabel());
                }

                if (allFree && candidateLabels.size() == count) {
                    return candidateLabels;
                }
            }
        }

        return List.of();
    }

    private SeatIdentifier parseSeatLabel(String seatLabel) {
        if (seatLabel == null || seatLabel.isBlank()) {
            throw new IllegalArgumentException("Seat label cannot be empty.");
        }

        String normalized = seatLabel.trim().toUpperCase();
        String rowName = normalized.substring(0, 1);
        int seatNumber;

        try {
            seatNumber = Integer.parseInt(normalized.substring(1));
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Invalid seat format. Use format like A5 or H10.");
        }

        if (!rowName.matches("[A-H]") || seatNumber < 1 || seatNumber > SEATS_PER_ROW) {
            throw new IllegalArgumentException("Invalid seat. Rows A-H with seats 1-10 are allowed.");
        }

        return new SeatIdentifier(rowName, seatNumber);
    }

    private SeatResponse toSeatResponse(Seat seat) {
        return SeatResponse.builder()
                .id(seat.getId())
                .rowName(seat.getRowName())
                .seatNumber(seat.getSeatNumber())
                .seatLabel(seat.getSeatLabel())
                .status(seat.getStatus())
                .build();
    }

    private record SeatIdentifier(String rowName, Integer seatNumber) {
    }
}
