//package com.hrms.project.entity;
//
//
//import com.fasterxml.jackson.annotation.JsonBackReference;
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//@Entity
//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
//public class Skills {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long skillid;
//    private String skillname;
//    private String skillProficiency;
//
//    @ManyToOne
//    @JoinColumn(name="employee_id")
//    @JsonBackReference
//    private Employee employee;
//
//}
