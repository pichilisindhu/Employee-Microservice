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
        task.setTaskId(taskId);
        task.setEmployee(employee);
        task.setProject(project);

        taskRepository.save(task);
        return "Assignment Created Successfully";
    }

    public List<AllTaskDTO> getAllTasks(String employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));

        return employee.getTasks().stream()
                .map(task -> modelMapper.map(task, AllTaskDTO.class))
                .toList();
    }

    public String updateTask(TaskDTO taskDTO, String employeeId,String projectId,String taskId) {
        TaskId compositeTaskId = new TaskId(taskId, projectId);

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


        TaskUpdate taskUpdate = modelMapper.map(taskUpdateDTO, TaskUpdate.class);


        TaskUpdateId updateId = new TaskUpdateId(taskKey, taskUpdateDTO.getUpdateNumber());

        taskUpdate.setUpdateNumber(updateId);
        taskUpdate.setUpdatedDate(LocalDateTime.now());
        taskUpdate.setTask(task);

        taskUpdateRepository.save(taskUpdate);

        return "Task Status Created Successfully";
    }



    public List<TaskUpdateDTO> getTaskStatus(String taskId, String projectId) {
        TaskId taskKey = new TaskId(taskId, projectId);

        Task task = taskRepository.findById(taskKey)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));

        return task.getUpdates().stream()
                .map(update -> modelMapper.map(update, TaskUpdateDTO.class))
                .toList();
    }


    public String updateStatus(String taskId, String projectId, Long updateNumber, TaskUpdateDTO taskUpdateDTO) {

        TaskId taskKey = new TaskId(taskId, projectId);

        TaskUpdateId compositeUpdateId = new TaskUpdateId(taskKey, updateNumber);

        TaskUpdate taskUpdate = taskUpdateRepository.findById(compositeUpdateId)
                .orElseThrow(() -> new TaskNotFoundException("Update not found"));




       taskUpdate.setUpdatedDate(taskUpdateDTO.getUpdatedDate());
       taskUpdate.setNote(taskUpdateDTO.getNote());
       taskUpdate.setChanges(taskUpdateDTO.getChanges());
       taskUpdate.setRelatedFiles(taskUpdateDTO.getRelatedFiles());
       taskUpdate.setRelatedGitLinks(taskUpdateDTO.getRelatedGitLinks());

        if (taskUpdate.getTask() == null) {
            Task task = taskRepository.findById(taskKey)
                    .orElseThrow(() -> new TaskNotFoundException("Task not found"));
            taskUpdate.setTask(task);
        }

        taskUpdateRepository.save(taskUpdate);

        return "Updated Successfully";



    }

}
