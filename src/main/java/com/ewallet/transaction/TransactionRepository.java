package com.ewallet.transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByFromUserId(Long fromUserId);
    List<Transaction> findByToUserId(Long toUserId);

    // 1. Total Added Money nikalne ke liye
    @Query("SELECT COALESCE(SUM(t.amount), 0.0) FROM Transaction t WHERE t.type = 'ADD_MONEY' AND t.toUserId = :userId")
    Double sumTotalAddedByUserId(@Param("userId") Long userId);

    // 2. Total Sent Money nikalne ke liye
    @Query("SELECT COALESCE(SUM(t.amount), 0.0) FROM Transaction t WHERE t.type = 'TRANSFER' AND t.fromUserId = :userId")
    Double sumTotalSentByUserId(@Param("userId") Long userId);

    // 3. Total Received Money nikalne ke liye
    @Query("SELECT COALESCE(SUM(t.amount), 0.0) FROM Transaction t WHERE t.toUserId = :userId AND (t.type = 'TRANSFER' OR t.type = 'ADD_MONEY')")
    Double sumTotalReceivedByUserId(@Param("userId") Long userId);

    // 4. Total Transactions Count karne ke liye
    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.fromUserId = :userId OR t.toUserId = :userId")
    Long countTotalTransactionsByUserId(@Param("userId") Long userId);
}