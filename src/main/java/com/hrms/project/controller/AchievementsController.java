package com.hrms.project.controller;

import com.hrms.project.dto.AchievementsDTO;
import com.hrms.project.dto.SkillsDTO;
import com.hrms.project.entity.Achievements;
import com.hrms.project.service.AchievementsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api")
public class AchievementsController {

    @Autowired
    private AchievementsServiceImpl achievementsServiceImpl;

    @PostMapping("/achievements/{employeeId}")
    public ResponseEntity<AchievementsDTO> addAchievements(@PathVariable String employeeId,
                                                            @RequestPart(value="image",required = false) MultipartFile image,
                                                        @RequestPart(value="details") Achievements achievementsDTO) throws IOException {
        return new ResponseEntity<>(achievementsServiceImpl.addAchievements(employeeId,image,achievementsDTO), HttpStatus.CREATED);

    }

    @GetMapping("/{employeeId}/employee/achievements")
    public ResponseEntity<List<AchievementsDTO>>  getAchievements(@PathVariable String employeeId) {

        List<AchievementsDTO> certifications=achievementsServiceImpl.getCertifications(employeeId);
        return new ResponseEntity<>(certifications,HttpStatus.OK);
    }

    @PutMapping("/{employeeId}/{certificateId}/achievements")
    public ResponseEntity<AchievementsDTO> updateAchievements(
            @PathVariable String employeeId,
            @PathVariable String certificateId,
            @RequestPart("image") MultipartFile image,
            @RequestPart("details") Achievements achievement) throws IOException {

       return new ResponseEntity<>( achievementsServiceImpl.updateAchievements(employeeId, certificateId, image, achievement),HttpStatus.CREATED);

    }


    @GetMapping("/{employeeId}/{achievementId}/achievement")
    public ResponseEntity<AchievementsDTO> getAchievement(@PathVariable String employeeId,@PathVariable String achievementId) {
        return new ResponseEntity<>(achievementsServiceImpl.getAchievement(employeeId,achievementId),HttpStatus.OK);
    }

    @DeleteMapping("/{employeeId}/{certificateId}/achievements")
    public ResponseEntity<AchievementsDTO> deleteAchievements(@PathVariable String employeeId, @PathVariable  String achievementId) {
        return new ResponseEntity<>(achievementsServiceImpl.deleteAchievements(employeeId,achievementId),HttpStatus.OK);
    }




    @PostMapping("/{employeeId}/skills")
    public ResponseEntity<SkillsDTO> addSkills(@PathVariable String employeeId,
                                               @RequestBody SkillsDTO resumeDTO) {
        return new ResponseEntity<>(achievementsServiceImpl.addSkills(employeeId, resumeDTO), HttpStatus.CREATED);
    }
    @PutMapping("/{employeeId}/skills")
    public ResponseEntity<SkillsDTO> updateSkills(@PathVariable String employeeId, @RequestBody SkillsDTO resumeDTO) {
        return  new ResponseEntity<>(achievementsServiceImpl.updateSkills(employeeId, resumeDTO), HttpStatus.OK);
    }

    @GetMapping("/{employeeId}/skills")
    public ResponseEntity<SkillsDTO> getSkills(@PathVariable String employeeId) {
        return  new ResponseEntity<>(achievementsServiceImpl.getSkills(employeeId), HttpStatus.OK);

    }

    @DeleteMapping("/{employeeId}/skills")
    public ResponseEntity<SkillsDTO> deleteSkills(@PathVariable String employeeId) {
        return  new ResponseEntity<>(achievementsServiceImpl.deleteSkills(employeeId), HttpStatus.OK);
    }

    }









