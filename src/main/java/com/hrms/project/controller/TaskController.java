package com.hrms.project.controller;


import com.hrms.project.dto.AllTaskDTO;
import com.hrms.project.dto.TaskDTO;
import com.hrms.project.dto.TaskUpdateDTO;
import com.hrms.project.service.TaskServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")

public class TaskController {


    @Autowired
    private TaskServiceImpl taskService;


    @PostMapping("/{employeeId}/{projectId}/task")
    public ResponseEntity<String> createAssignment( @PathVariable String employeeId,
                                                    @PathVariable String projectId,
                                                    @RequestBody TaskDTO taskDTO){

        return new ResponseEntity<>(taskService.createAssignment(employeeId,projectId,taskDTO), HttpStatus.CREATED);

    }

    @GetMapping("/all/tasks/{employeeId}")

        public ResponseEntity<List<AllTaskDTO>> getALlTasks(@PathVariable String employeeId)
        {
            return new ResponseEntity<>(taskService.getAllTasks(employeeId),HttpStatus.OK);


    }

    @PutMapping("/{employeeId}/{projectId}/task")
    public ResponseEntity<String> updateTask(@RequestBody TaskDTO taskDTO,
                                             @PathVariable String employeeId,
                                             @PathVariable String projectId){
        return new ResponseEntity<>(taskService.updateTask(taskDTO,employeeId,projectId),HttpStatus.CREATED);
    }

    @PostMapping("/history/{projectId}/{taskId}")
    public ResponseEntity<String> createTaskHistory(@PathVariable String projectId,
                                                    @PathVariable String taskId,
                                                    @RequestBody TaskUpdateDTO taskUpdateDTO) {

        return new ResponseEntity<>(taskService.createStatus(taskId, projectId, taskUpdateDTO), HttpStatus.CREATED);
    }

    @GetMapping("/task/status/{projectId}/{taskId}")
    public ResponseEntity<List<TaskUpdateDTO>> getTaskStatus(@PathVariable String projectId,
                                                          @PathVariable String taskId) {
        return new ResponseEntity<>(taskService.getTaskStatus(taskId, projectId), HttpStatus.OK);
    }

    @PutMapping("/status/{projectId}/{taskId}")
    public ResponseEntity<String> updateTaskHistory(@PathVariable String taskId,
                                                        @PathVariable String projectId,
                                                        @RequestBody TaskUpdateDTO taskUpdateDTO) {

        return new ResponseEntity<>(taskService.updateStatus(taskId, projectId, taskUpdateDTO),
                HttpStatus.OK);
    }



}
