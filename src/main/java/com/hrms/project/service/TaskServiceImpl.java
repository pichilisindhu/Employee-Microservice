package com.hrms.project.service;

import com.hrms.project.configuration.TaskId;
import com.hrms.project.configuration.TaskUpdateId;
import com.hrms.project.dto.AllTaskDTO;
import com.hrms.project.dto.TaskDTO;
import com.hrms.project.dto.TaskUpdateDTO;
import com.hrms.project.entity.*;
import com.hrms.project.handlers.EmployeeNotFoundException;
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


    public String createAssignment(String employeeId,String tlId,String projectId,TaskDTO taskDTO) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id " + employeeId));


        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found"));

        Task task = modelMapper.map(taskDTO, Task.class);

        TaskId taskId = new TaskId(taskDTO.getId(), projectId);
        task.setCreatedBy(tlId);
        task.setId(taskId);
        task.setEmployee(employee);
        task.setProject(project);

        taskRepository.save(task);
        return "Assignment Created Successfully";
    }

    public List<AllTaskDTO> getAllTasks(String employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + employeeId));

        if (employee.getTasks() == null || employee.getTasks().isEmpty()) {
            throw new TaskNotFoundException("This employee has no tasks assigned");
        }

        return employee.getTasks().stream()
                .map(task -> {
                    AllTaskDTO dto = new AllTaskDTO();
                    dto.setId(task.getId().getTaskId());
                    dto.setStatus(task.getStatus());
                    dto.setTitle(task.getTitle());
                    dto.setPriority(task.getPriority());
                    dto.setStartDate(task.getCreatedDate());
                    dto.setDueDate(task.getDueDate());
                    dto.setProjectId(task.getProject().getProjectId());
                    dto.setAssignedTo(task.getAssignedTo());
                    dto.setCreatedBy(task.getCreatedBy());
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

        if (taskUpdateDTO.getId() == null) {
            throw new IllegalArgumentException("Task update ID must be provided");
        }

        TaskUpdateId compositeUpdateId = new TaskUpdateId(taskKey, taskUpdateDTO.getId());

        TaskUpdate taskUpdate = new TaskUpdate();
        taskUpdate.setTask(task);
        taskUpdate.setId(compositeUpdateId);

        taskUpdate.setUpdatedDate(LocalDateTime.now());
        taskUpdate.setChanges(taskUpdateDTO.getChanges());
        taskUpdate.setNote(taskUpdateDTO.getNote());
        taskUpdate.setRelatedLinks(taskUpdateDTO.getRelatedLinks());
        taskUpdate.setRelatedFileLinks(taskUpdateDTO.getRelatedFileLinks());
        taskUpdate.setReviewedBy(taskUpdateDTO.getReviewedBy());
        taskUpdate.setRemark(taskUpdateDTO.getRemark());

        taskUpdateRepository.save(taskUpdate);
        return "Task update saved successfully";
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
        taskUpdate.setRemark(taskUpdateDTO.getRemark());

        if (taskUpdate.getTask() == null) {
            Task task = taskRepository.findById(taskKey)
                    .orElseThrow(() -> new TaskNotFoundException("Task not found"));
            taskUpdate.setTask(task);
        }
        taskUpdateRepository.save(taskUpdate);
        return "Updated Successfully";
    }


    public TaskDTO getTask(String projectId, String taskId) {

        TaskId taskKey = new TaskId(taskId, projectId);

        Task task = taskRepository.findById(taskKey)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));

        TaskDTO dto = new TaskDTO();
        dto.setId(task.getId().getTaskId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setCreatedBy(task.getCreatedBy());
        dto.setAssignedTo(task.getAssignedTo());
        dto.setStatus(task.getStatus());
        dto.setPriority(task.getPriority());
        dto.setCreatedDate(task.getCreatedDate());
        dto.setCompletedDate(task.getCompletedDate());
        dto.setDueDate(task.getDueDate());
        dto.setRating(task.getRating());
        dto.setRemark(task.getRemark());
        dto.setCompletionNote(task.getCompletionNote());
        dto.setRelatedLinks(task.getRelatedLinks());
        dto.setAttachedFileLinks(task.getAttachedFileLinks());
        return dto;
    }
    public List<TaskUpdateDTO> getUpdateTasks(String projectId, String taskId) {
        return taskUpdateRepository.findAll()
                .stream()
                .filter(taskUpdate ->
                        taskUpdate.getTask().getId().getTaskId().equals(taskId) &&
                                taskUpdate.getTask().getId().getProjectId().equals(projectId)
                )
                .map(update -> {
                    TaskUpdateDTO dto = new TaskUpdateDTO();
                    dto.setId(update.getId().getUpdateNumber());
                    dto.setChanges(update.getChanges());
                    dto.setNote(update.getNote());
                    dto.setRelatedLinks(update.getRelatedLinks());
                    dto.setRelatedFileLinks(update.getRelatedFileLinks());
                    dto.setUpdatedDate(update.getUpdatedDate());
                    dto.setReviewedBy(update.getReviewedBy());
                    dto.setRemark(update.getRemark());
                    return dto;
                })
                .toList();
    }

    public String deleteTask(String projectId, String taskId) {

        TaskId taskKey = new TaskId(taskId, projectId);
        taskRepository.deleteById(taskKey);

        return "Task deleted successfully";
    }

    public List<Task> getTasks(String tlId) {
        return taskRepository.findByCreatedBy(tlId);

    }
}
