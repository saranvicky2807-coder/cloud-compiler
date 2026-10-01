package com.cloudcompiler.repository;

import com.cloudcompiler.entity.Project;
import com.cloudcompiler.entity.SourceFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SourceFileRepository extends JpaRepository<SourceFile, UUID> {
    List<SourceFile> findByProjectOrderByCreatedAtAsc(Project project);
    Optional<SourceFile> findByIdAndProject(UUID id, Project project);
    long countByProject_User(com.cloudcompiler.entity.User user);
}
