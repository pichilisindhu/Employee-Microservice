package com.hrms.project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskDTO {


    private String id;
    private String title;
    private String description;
    private String createdBy;
    private String assignedTo;
    private String status;
    private String priority;
    private LocalDate  createdDate;
    private LocalDate completedDate;
    private LocalDate  dueDate;
    private Integer rating;
    private String remark;
    private String completionNote;
    private List<String> relatedLinks;
    private List<String> attachedFileLinks;


}
