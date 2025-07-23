package com.hrms.project.controller;


import com.hrms.project.entity.Assignment;
import com.hrms.project.service.AadhaarServiceImpl;
import com.hrms.project.service.AssignmentServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AssignmentController {


    @Autowired
    private AssignmentServiceImpl assignmentService;

    @PostMapping("/create/{employeeId}")
    public ResponseEntity<String> createAssignment(@PathVariable String employeeId, @RequestBody Assignment assignment){

        return new ResponseEntity<>(assignmentService.createAssignment(assignment,employeeId), HttpStatus.CREATED);

    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<List<Assignment>> getAllAssignments(@PathVariable String employeeId){

        return new ResponseEntity<>(assignmentService.getAllAssignments(employeeId),HttpStatus.OK);

    }
    @GetMapping("/{employeeId}/{assignmentId}")
    public ResponseEntity<Assignment> getAssignment(@PathVariable String employeeId, @PathVariable Long assignmentId){
        return new ResponseEntity<>(assignmentService.getAssignment(employeeId,assignmentId),HttpStatus.OK);
    }

    @PutMapping("/{employeeId}/{assignmentId}")
    public ResponseEntity<String> updateAssignment(@PathVariable String employeeId,
                                                   @PathVariable Long assignmentId,
                                                   @RequestBody Assignment assignment){
        return new ResponseEntity<>(assignmentService.updateAssignment(employeeId,assignmentId,assignment),HttpStatus.CREATED);
    }

}
