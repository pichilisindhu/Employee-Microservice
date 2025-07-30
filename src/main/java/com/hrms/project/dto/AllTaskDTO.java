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
public class AllTaskDTO {

    private String status;
    private String title;
    private String priority;
    private LocalDate startDate;
}
