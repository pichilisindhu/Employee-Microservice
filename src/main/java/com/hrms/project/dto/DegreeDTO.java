package com.hrms.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DegreeDTO {


    private String id;
    private String degree;
    private String branchOrSpecialization;
    private String startMonth;
    private String endMonth;
    private String startYear;
    private String endYear;
    private String cgpaOrPercentage;
    private String universityOrCollege;
    private String addFiles;


}