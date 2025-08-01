package com.hrms.project.controller;


import com.hrms.project.dto.DepartmentDTO;
import com.hrms.project.dto.EmployeeDepartmentDTO;
import com.hrms.project.service.DepartmentService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")

public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private ModelMapper modelMapper;



  //  @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    @PostMapping("/department")
    public ResponseEntity<DepartmentDTO> createDepartment(@Valid @RequestBody DepartmentDTO departmentDTO) {

        DepartmentDTO departmentList = departmentService.saveDepartment(departmentDTO);
        return new ResponseEntity<>(departmentList, HttpStatus.CREATED);
    }

   // @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER', 'TEAM_LEAD')")
    @GetMapping("/department/{departmentId}/employees")
    public ResponseEntity<EmployeeDepartmentDTO> getEmployeesByDepartmentId(@PathVariable String departmentId) {

        EmployeeDepartmentDTO employeeDepartmentDTO = departmentService.getEmployeesByDepartmentId(departmentId);
        return new ResponseEntity<>(employeeDepartmentDTO, HttpStatus.OK);
    }


  //  @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    @PutMapping("/department/{departmentId}")
    public ResponseEntity<DepartmentDTO> updateDepartment(@PathVariable String departmentId, @Valid @RequestBody DepartmentDTO departmentDTO) {
        return new ResponseEntity<>(departmentService.updateDepartment(departmentId, departmentDTO), HttpStatus.CREATED);
    }


//    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER', 'EMPLOYEE')")
    @GetMapping("/all/departments")
    public ResponseEntity<List<DepartmentDTO>> getAllDepartments() {
        return new ResponseEntity<>(departmentService.getAllDepartmentDetails(), HttpStatus.OK);
    }

//    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER', 'EMPLOYEE')")
    @GetMapping("/get/department/{departmentId}")
    public ResponseEntity<DepartmentDTO> getDepartmentById(@PathVariable String departmentId) {
        return new ResponseEntity<>(departmentService.getByDepartmentId(departmentId), HttpStatus.OK);
        }

//        @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'EMPLOYEE')")
        @GetMapping("/{employeeId}/department/employees")
        public ResponseEntity<EmployeeDepartmentDTO> getEmployeesByEmployeeId(@PathVariable String employeeId) {

        return new ResponseEntity<EmployeeDepartmentDTO>(departmentService.getEmployeeByEmployeeId(employeeId),HttpStatus.OK);


        }

        @DeleteMapping("/{departmentId}/department")
    public ResponseEntity<String> deleteDepartment(@PathVariable String departmentId) {
        return new ResponseEntity<>(departmentService.deleteDepartment(departmentId),HttpStatus.OK);
        }




}

