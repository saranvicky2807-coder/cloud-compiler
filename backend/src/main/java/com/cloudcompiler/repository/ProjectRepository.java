package com.cloudcompiler.repository;

import com.cloudcompiler.entity.Project;
import com.cloudcompiler.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID> {
    List<Project> findByUserOrderByUpdatedAtDesc(User user);
    Optional<Project> findByIdAndUser(UUID id, User user);
    long countByUser(User user);
}
