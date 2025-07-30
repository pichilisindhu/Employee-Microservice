package com.hrms.project.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskUpdateDTO {
    private Long updateNumber;
    private String changes;
    private String note;
    private String relatedGitLinks;
    private String relatedFiles;
    private LocalDateTime updatedDate;
}

