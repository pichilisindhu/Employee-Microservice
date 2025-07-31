package com.hrms.project.repository;

import com.hrms.project.configuration.TaskId;
import com.hrms.project.entity.TaskUpdate;
import com.hrms.project.configuration.TaskUpdateId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.lang.ScopedValue;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskUpdateRepository extends JpaRepository<TaskUpdate, TaskUpdateId> {
    @Query("SELECT MAX(t.id.updateNumber) FROM TaskUpdate t WHERE t.id.taskId.taskId = :taskId AND t.id.taskId.projectId = :projectId")
    Optional<Long> findMaxUpdateNumber(@Param("projectId") String projectId, @Param("taskId") String taskId);

}

