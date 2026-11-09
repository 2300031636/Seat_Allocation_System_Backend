package com.seatallocation.repository;

import com.seatallocation.entity.Seat;
import com.seatallocation.entity.SeatStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {

    Optional<Seat> findByRowNameAndSeatNumber(String rowName, Integer seatNumber);

    List<Seat> findAllByOrderByRowNameAscSeatNumberAsc();

    List<Seat> findByRowNameOrderBySeatNumberAsc(String rowName);

    long countByStatus(SeatStatus status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM Seat s WHERE s.rowName = :rowName AND s.seatNumber BETWEEN :start AND :end ORDER BY s.seatNumber")
    List<Seat> findSeatsInRowRangeWithLock(@Param("rowName") String rowName,
                                           @Param("start") int start,
                                           @Param("end") int end);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM Seat s WHERE s.rowName = :rowName AND s.seatNumber = :seatNumber")
    Optional<Seat> findByRowNameAndSeatNumberWithLock(@Param("rowName") String rowName,
                                                      @Param("seatNumber") Integer seatNumber);

    @Modifying
    @Query("UPDATE Seat s SET s.status = :status")
    int updateAllStatuses(@Param("status") SeatStatus status);
}
