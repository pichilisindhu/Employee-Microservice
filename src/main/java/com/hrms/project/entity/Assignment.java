package com.hrms.project.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "employee")
@ToString(exclude = "employee")

public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long assignmentId;

    private String assignmentName;
    private String assignmentDescription;
    private LocalDate  startDate;
    private LocalDate  endDate;
    private Integer rating;
    private String remark;

    @ManyToOne
    @JoinColumn(name="employee_id")
     @JsonBackReference
    private Employee employee;

}
