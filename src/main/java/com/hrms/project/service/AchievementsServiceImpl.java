package com.hrms.project.service;

import com.hrms.project.dto.SkillsDTO;
import com.hrms.project.entity.Achievements;

import com.hrms.project.entity.Employee;
import com.hrms.project.handlers.APIException;
import com.hrms.project.handlers.EmployeeNotFoundException;
import com.hrms.project.dto.AchievementsDTO;
import com.hrms.project.repository.AchievementsRepository;
import com.hrms.project.repository.EmployeeRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@Service
public class AchievementsServiceImpl {

    @Autowired
    private AchievementsRepository achievementsRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private FileService fileService;

    @Value("${project.image}")
    private String path;


    @Autowired
    private ModelMapper modelMapper;

    public  AchievementsDTO addAchievements(String employeeId,MultipartFile image, Achievements achievements) throws IOException {

        Employee employee=employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id " + employeeId));


        if (image != null && !image.isEmpty()) {
            String img = fileService.uploadImage(path, image);
            achievements.setAchievementFile(img);

        }
        achievements.setEmployee(employee);

        return modelMapper.map(achievementsRepository.save(achievements),AchievementsDTO.class);

    }

    public List<AchievementsDTO> getCertifications(String employeeId) {

        Employee employee=employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id " + employeeId));

        List<Achievements> achievements=employee.getAchievements();

     return achievements.stream().map(achieve->
        {
            return modelMapper.map(achieve,AchievementsDTO.class);
        }).toList();





    }

    public AchievementsDTO getAchievement(String employeeId,String achievementId) {

        Employee employee=employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id " + employeeId));

        Achievements achievements=achievementsRepository.findById(achievementId)
                .orElseThrow(()->new APIException("Achievement not found with id " + achievementId));

        return modelMapper.map(achievements,AchievementsDTO.class);



    }


    public AchievementsDTO updateAchievements(String employeeId, String certificateId,
                                     MultipartFile image, Achievements achievement) throws IOException {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id " + employeeId));

        if (employee.getAchievements() == null || employee.getAchievements().isEmpty()) {
            throw new RuntimeException("No achievements found for employee " + employeeId);
        }

        Achievements achievementToUpdate = employee.getAchievements().stream()
                .filter(achieve -> achieve.getId().equals(certificateId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Achievement with certificate ID " + certificateId + " not found."));

        if (image != null && !image.isEmpty()) {
            String img = fileService.uploadImage(path, image);
            achievementToUpdate.setAchievementFile(img);
        }

        achievementToUpdate.setCertificationName(achievement.getCertificationName());
        achievementToUpdate.setIssuingAuthorityName(achievement.getIssuingAuthorityName());
        achievementToUpdate.setCertificationURL(achievement.getCertificationURL());
        achievementToUpdate.setIssueMonth(achievement.getIssueMonth());
        achievementToUpdate.setIssueYear(achievement.getIssueYear());
        achievementToUpdate.setExpirationMonth(achievement.getExpirationMonth());
        achievementToUpdate.setExpirationYear(achievement.getExpirationYear());
        achievementToUpdate.setLicenseNumber(achievement.getLicenseNumber());

        achievementsRepository.save(achievementToUpdate);

        return modelMapper.map(achievementToUpdate,AchievementsDTO.class);
    }




    public AchievementsDTO deleteAchievements(String employeeId, String certificateId) {


        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id " + employeeId));

        List<Achievements> achievements=employee.getAchievements();

        Achievements achievementToDelete = achievements.stream()
                .filter(achieve -> achieve.getId().equals(certificateId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Achievement with certificate ID " + certificateId + " not found."));

        achievementsRepository.delete(achievementToDelete);

        return modelMapper.map(achievementToDelete, AchievementsDTO.class);

    }

    public SkillsDTO addSkills(String employeeId, SkillsDTO resumeDTO) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id " + employeeId));
        if (employee.getSkills() != null && !employee.getSkills().isEmpty()) {
            throw new APIException("Skills already exist for this employee cannot add again.");
        }
        modelMapper.map(resumeDTO,employee);
        employeeRepository.save(employee);
        return modelMapper.map(employee, SkillsDTO.class);

    }

    public SkillsDTO updateSkills(String employeeId, SkillsDTO resumeDTO) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id " + employeeId));
        modelMapper.map(resumeDTO,employee);
        employeeRepository.save(employee);
        return modelMapper.map(employee, SkillsDTO.class);

    }

    public SkillsDTO getSkills(String employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id " + employeeId));

        SkillsDTO resumeDTO = modelMapper.map(employee, SkillsDTO.class);
        resumeDTO.setSkills(employee.getSkills());

        return resumeDTO;
    }

    public SkillsDTO deleteSkills(String employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id " + employeeId));
        if (employee.getSkills() == null || employee.getSkills().isEmpty()) {
            throw new APIException("No skills found to delete for this employee.");
        }

        employee.setSkills(null);
        employeeRepository.save(employee);

        SkillsDTO resumeDTO = modelMapper.map(employee, SkillsDTO.class);
        resumeDTO.setSkills(employee.getSkills());

        return resumeDTO;
    }


}
