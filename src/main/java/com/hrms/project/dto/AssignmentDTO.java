package com.hrms.project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentDTO {


    private String assignmentName;
    private String assignmentDescription;
    private String assignedBy;
    private String status;
    private String priority;

    private LocalDate startDate;
    private LocalDate  dueDate;
    private Integer rating;
    private String remark;
    private String employeeId;
    private String projectId;

}
