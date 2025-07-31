package com.hrms.project.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskUpdateDTO {


    private Long id;
    private String changes;
    private String note;
    private List<String> relatedLinks;
    private List<String> relatedFileLinks;
    private LocalDateTime updatedDate;

    private String reviewedBy;

}

