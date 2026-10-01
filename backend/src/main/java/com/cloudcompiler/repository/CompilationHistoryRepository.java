package com.cloudcompiler.repository;

import com.cloudcompiler.entity.CompilationHistory;
import com.cloudcompiler.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CompilationHistoryRepository extends JpaRepository<CompilationHistory, UUID> {
    List<CompilationHistory> findByUserOrderByCreatedAtDesc(User user);
    Optional<CompilationHistory> findByIdAndUser(UUID id, User user);
    long countByUser(User user);
    long countByUserAndSuccess(User user, boolean success);
    void deleteByUser(User user);
}
