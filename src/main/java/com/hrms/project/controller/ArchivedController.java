package com.hrms.project.controller;

import com.hrms.project.entity.Archive;
import com.hrms.project.service.ArchivedServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController

@RequestMapping("/api")
public class ArchivedController {


    @Autowired
    private ArchivedServiceImpl archivedService;

    @GetMapping()
    public ResponseEntity<List<Archive>>  getTerminatedEmployees() {

        return new ResponseEntity<>(archivedService.getEmployee(),HttpStatus.OK);

    }
}
