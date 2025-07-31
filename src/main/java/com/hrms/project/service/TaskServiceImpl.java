package com.hrms.project.service;

import com.hrms.project.configuration.TaskId;
import com.hrms.project.configuration.TaskUpdateId;

import com.hrms.project.dto.AllTaskDTO;
import com.hrms.project.dto.TaskDTO;
import com.hrms.project.dto.TaskUpdateDTO;
import com.hrms.project.entity.*;
import com.hrms.project.handlers.ProjectNotFoundException;
import com.hrms.project.handlers.TaskNotFoundException;
import com.hrms.project.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskServiceImpl {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ProjectRepository projectRepository;

   @Autowired
   private TaskUpdateRepository taskUpdateRepository;

    @Autowired
    private ModelMapper modelMapper;


    public String createAssignment(String employeeId,String projectId,TaskDTO taskDTO) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found"));

        Task task = modelMapper.map(taskDTO, Task.class);

        TaskId taskId = new TaskId(taskDTO.getId(), projectId);
        task.setId(taskId);
        task.setEmployee(employee);
        task.setProject(project);

        taskRepository.save(task);
        return "Assignment Created Successfully";
    }

    public List<AllTaskDTO> getAllTasks(String employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));

        return employee.getTasks().stream()
                .map(task -> {
                    AllTaskDTO dto = new AllTaskDTO();
                    dto.setId(task.getId().getTaskId()); // only taskId goes here
                    dto.setStatus(task.getStatus());
                    dto.setTitle(task.getTitle());
                    dto.setPriority(task.getPriority());
                    dto.setStartDate(task.getCreatedDate());
                    dto.setDueDate(task.getDueDate());
                    return dto;
                })
                .toList();
    }



    public String updateTask(TaskDTO taskDTO, String employeeId,String projectId) {
        TaskId compositeTaskId = new TaskId(taskDTO.getId(), projectId);

        Task task = taskRepository.findById(compositeTaskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found"));

        modelMapper.map(taskDTO, task);
        task.setEmployee(employee);
        task.setProject(project);

        taskRepository.save(task);
        return "Assignment Updated Successfully";
    }




    public String createStatus(String taskId, String projectId, TaskUpdateDTO taskUpdateDTO) {
        TaskId taskKey = new TaskId(taskId, projectId);

        Task task = taskRepository.findById(taskKey)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));


       Long lastUpdateNumber = taskUpdateRepository
                .findMaxUpdateNumber(projectId, taskId)
                .orElse(0L);

        Long newUpdateNumber = lastUpdateNumber + 1;

        TaskUpdateId updateId = new TaskUpdateId(taskKey, newUpdateNumber);


        TaskUpdate taskUpdate = modelMapper.map(taskUpdateDTO, TaskUpdate.class);
        taskUpdate.setId(updateId);
        taskUpdate.setUpdatedDate(LocalDateTime.now());
        taskUpdate.setTask(task);

        taskUpdateRepository.save(taskUpdate);

        return "Task Status Created Successfully";
    }



    public List<TaskUpdateDTO> getTaskStatus(String taskId, String projectId) {
        TaskId taskKey = new TaskId(taskId, projectId);

        Task task = taskRepository.findById(taskKey)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));

        return task.getUpdateHistory().stream()
                .map(update -> {
                    TaskUpdateDTO dto = new TaskUpdateDTO();
                    dto.setId(update.getId().getUpdateNumber()); // Manually set updateNumber
                    dto.setChanges(update.getChanges());
                    dto.setNote(update.getNote());
                    dto.setRelatedLinks(update.getRelatedLinks());
                    dto.setRelatedFileLinks(update.getRelatedFileLinks());
                    dto.setUpdatedDate(update.getUpdatedDate());
                    dto.setReviewedBy(update.getReviewedBy());
                    return dto;
                })
                .toList();
    }


    public String updateStatus(String taskId, String projectId, TaskUpdateDTO taskUpdateDTO) {
        TaskId taskKey = new TaskId(taskId, projectId);
        TaskUpdateId compositeUpdateId = new TaskUpdateId(taskKey, taskUpdateDTO.getId());

        TaskUpdate taskUpdate = taskUpdateRepository.findById(compositeUpdateId)
                .orElseThrow(() -> new TaskNotFoundException("Update not found"));


        taskUpdate.setUpdatedDate(taskUpdateDTO.getUpdatedDate());
        taskUpdate.setNote(taskUpdateDTO.getNote());
        taskUpdate.setChanges(taskUpdateDTO.getChanges());
        taskUpdate.setRelatedFileLinks(taskUpdateDTO.getRelatedFileLinks());
        taskUpdate.setRelatedLinks(taskUpdateDTO.getRelatedLinks());
        taskUpdate.setReviewedBy(taskUpdateDTO.getReviewedBy());

        if (taskUpdate.getTask() == null) {
            Task task = taskRepository.findById(taskKey)
                    .orElseThrow(() -> new TaskNotFoundException("Task not found"));
            taskUpdate.setTask(task);
        }

        taskUpdateRepository.save(taskUpdate);
        return "Updated Successfully";
    }


}
