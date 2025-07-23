package com.hrms.project.service;

import com.hrms.project.entity.Achievements;
import com.hrms.project.entity.Employee;
//import com.hrms.project.entity.Skills;
import com.hrms.project.handlers.EmployeeNotFoundException;
import com.hrms.project.dto.AchievementsDTO;
import com.hrms.project.repository.AchievementsRepository;
import com.hrms.project.repository.EmployeeRepository;
//import com.hrms.project.repository.SkillsRepository;
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

    public AchievementsDTO addAchievements(String employeeId,MultipartFile image, AchievementsDTO achievementsDTO) throws IOException {

        Employee employee=employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id " + employeeId));

        Achievements newAchievements = new Achievements();

        modelMapper.map(achievementsDTO,newAchievements);

        newAchievements.setEmployee(employee);


        if (image != null && !image.isEmpty()) {
            String img = fileService.uploadImage(path, image);
            newAchievements.setAchievementFile(img);
        }
        AchievementsDTO achievements=modelMapper.map(achievementsRepository.save(newAchievements),AchievementsDTO.class);
        return achievements;
    }

    public List<AchievementsDTO> getCertifications(String employeeId) {

        Employee employee=employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id " + employeeId));

        List<Achievements> achievements=employee.getAchievements();

        List<AchievementsDTO> details=achievements.stream().map(achieve->
        {
            return modelMapper.map(achieve,AchievementsDTO.class);
        }).toList();

        return details;



    }


    public AchievementsDTO updateAchievements(String employeeId, Long certificateId,
                                              MultipartFile image, AchievementsDTO achievementsDTO) throws IOException {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id " + employeeId));

        Achievements achievementToUpdate = employee.getAchievements().stream()
                .filter(achieve -> achieve.getId().equals(certificateId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Achievement with certificate ID " + certificateId + " not found."));

        modelMapper.map(achievementsDTO, achievementToUpdate);

        if (image != null && !image.isEmpty()) {
            String img = fileService.uploadImage(path, image);
            achievementToUpdate.setAchievementFile(img);
        }
       achievementsRepository.save(achievementToUpdate);

        return modelMapper.map(achievementToUpdate, AchievementsDTO.class);
    }

    public AchievementsDTO deleteAchievements(String employeeId, Long certificateId) {


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


//    public Skills addSkills(String employeeId, Skills skills) {
//        Employee employee = employeeRepository.findById(employeeId)
//                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id " + employeeId));
//
//        skills.setEmployee(employee);
//        return skillsRepository.save(skills);
//    }
//
//    public List<Skills> getSkills(String employeeId) {
//        Employee employee = employeeRepository.findById(employeeId)
//                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id " + employeeId));
//
//        return employee.getSkills(); // Assuming a OneToMany mapping in Employee entity
//    }
//
//    public Skills updateSkills(String employeeId, Long skillId, Skills updatedSkill) {
//        Employee employee = employeeRepository.findById(employeeId)
//                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id " + employeeId));
//
//        Skills skillToUpdate = employee.getSkills().stream()
//                .filter(skill -> skill.getSkillid().equals(skillId))
//                .findFirst()
//                .orElseThrow(() -> new RuntimeException("Skill with ID " + skillId + " not found."));
//
//        skillToUpdate.setSkillname(updatedSkill.getSkillname());
//        skillToUpdate.setSkillProficiency(updatedSkill.getSkillProficiency());
//
//        return skillsRepository.save(skillToUpdate);
//    }
//
//    public Skills deleteSkills(String employeeId, Long skillId) {
//        Employee employee = employeeRepository.findById(employeeId)
//                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id " + employeeId));
//
//        Skills skillToDelete = employee.getSkills().stream()
//                .filter(skill -> skill.getSkillid().equals(skillId))
//                .findFirst()
//                .orElseThrow(() -> new RuntimeException("Skill with ID " + skillId + " not found."));
//
//        skillsRepository.delete(skillToDelete);
//        return skillToDelete;
//    }


}
