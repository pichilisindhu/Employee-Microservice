package com.hrms.project.repository;

import com.hrms.project.configuration.TaskId;
import com.hrms.project.entity.TaskUpdate;
import com.hrms.project.configuration.TaskUpdateId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskUpdateRepository extends JpaRepository<TaskUpdate, TaskUpdateId> {
   // List<TaskUpdate> findByTask_TaskId(TaskId taskId);
}

