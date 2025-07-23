package com.hrms.project.service;


import com.hrms.project.entity.Assignment;
import com.hrms.project.entity.Employee;
import com.hrms.project.handlers.AssignmentNotFound;
import com.hrms.project.repository.AssignmentRepository;

import com.hrms.project.repository.EmployeeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssignmentServiceImpl {

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    public String createAssignment(Assignment assignment, String employeeId) {

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() ->
                new EntityNotFoundException("Employee with id: " + employeeId + " not found"));

        assignment.setEmployee(employee);
        assignmentRepository.save(assignment);
        return "Assignment created successfully";
    }

    public List<Assignment> getAllAssignments(String employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() ->
                new EntityNotFoundException("Employee with id: " + employeeId + " not found"));

        List<Assignment> assignment = employee.getAssignments();

        return assignment;
    }


    public Assignment getAssignment(String employeeId, Long assignmentId) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() ->
                new EntityNotFoundException("Employee with id: " + employeeId + " not found"));

        List<Assignment> assignment = employee.getAssignments();
        return assignment.stream()
                .filter(a -> a.getAssignmentId().equals(assignmentId))
                .findFirst()
                .orElseThrow(() -> new AssignmentNotFound("Assignment Not Found with Id: " + assignmentId + " not found"));


    }

    public String updateAssignment(String employeeId, Long assignmentId, Assignment assignment) {

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() ->
                new EntityNotFoundException("Employee with id: " + employeeId + " not found"));

        List<Assignment> assignments = employee.getAssignments();
       Assignment assign= assignments.stream()
                .filter(a -> a.getAssignmentId().equals(assignmentId))
                .findFirst()
                .orElseThrow(() -> new AssignmentNotFound("Assignment Not Found with Id: " + assignmentId + " not found"));

       assign.setAssignmentName(assignment.getAssignmentName());
       assign.setAssignmentDescription(assignment.getAssignmentDescription());
       assign.setRating(assignment.getRating());
       assign.setStartDate(assignment.getStartDate());
       assign.setEndDate(assignment.getEndDate());
       assign.setRemark(assignment.getRemark());

       assignmentRepository.save(assign);


        return "Assignment updated successfully";
    }
}
