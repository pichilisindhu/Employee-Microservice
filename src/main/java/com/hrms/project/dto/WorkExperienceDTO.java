package com.hrms.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkExperienceDTO {

    private String id;
    private String companyName;
    private String jobTitle;
    private String location;
    private String description;
   private String startMonth;
   private String startYear;
   private String endMonth;
   private String endYear;

}

