package com.seatallocation.config;

import com.seatallocation.entity.Seat;
import com.seatallocation.entity.SeatStatus;
import com.seatallocation.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private static final String[] ROWS = {"A", "B", "C", "D", "E", "F", "G", "H"};
    private static final int SEATS_PER_ROW = 10;

    private final SeatRepository seatRepository;

    @Override
    public void run(String... args) {
        if (seatRepository.count() > 0) {
            log.info("Seats already initialized. Skipping seed data.");
            return;
        }

        log.info("Initializing 80 seats (rows A-H, 10 seats each)...");

        for (String row : ROWS) {
            for (int seatNumber = 1; seatNumber <= SEATS_PER_ROW; seatNumber++) {
                Seat seat = Seat.builder()
                        .rowName(row)
                        .seatNumber(seatNumber)
                        .status(SeatStatus.FREE)
                        .build();
                seatRepository.save(seat);
            }
        }

        log.info("Seat initialization complete.");
    }
}
