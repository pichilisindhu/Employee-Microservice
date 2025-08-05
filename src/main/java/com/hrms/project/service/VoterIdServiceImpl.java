package com.hrms.project.service;

import com.hrms.project.entity.Employee;
import com.hrms.project.entity.VoterDetails;
import com.hrms.project.handlers.APIException;
import com.hrms.project.handlers.EmployeeNotFoundException;
import com.hrms.project.dto.VoterDTO;
import com.hrms.project.repository.EmployeeRepository;
import com.hrms.project.repository.VoterIdRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class VoterIdServiceImpl {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private VoterIdRepository voterIdRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private FileService fileService;

    @Value("${project.image}")
    private String path;

    public VoterDetails createVoter(String employeeId, MultipartFile voterImage,
                                    VoterDTO voterDTO) throws IOException {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + employeeId));
        if (voterIdRepository.findByEmployee_EmployeeId(employeeId).isPresent()) {
            throw new APIException("This employee already has a Voter ID assigned");
        }
        Optional<VoterDetails> existingVoterOpt = voterIdRepository.findById(voterDTO.getVoterIdNumber());
        VoterDetails cardDetails;

        if (existingVoterOpt.isEmpty()) {

            cardDetails = new VoterDetails();
            modelMapper.map(voterDTO, cardDetails);

            if (voterImage != null && !voterImage.isEmpty()) {
                String fileName = fileService.uploadImage(path, voterImage);
                cardDetails.setUploadVoter(fileName);
            }

            cardDetails.setEmployee(employee);
            voterIdRepository.save(cardDetails);

        } else {

            VoterDetails existing = existingVoterOpt.get();

            if (existing.getEmployee() == null) {
                existing.setEmployee(employee);
                modelMapper.map(voterDTO, existing);
                cardDetails = voterIdRepository.save(existing);
            } else {
                throw new APIException("This Voter ID is already assigned to another employee");
            }
        }

        return cardDetails;
    }


    public VoterDTO getVoterByEmployee(String employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + employeeId));

        VoterDetails voterDetails = employee.getVoterDetails();

        if (voterDetails == null) {
            throw new APIException("Voter ID details not found for this employee");
        }

        return modelMapper.map(voterDetails, VoterDTO.class);
    }

    public VoterDetails updateVoter(String employeeId, MultipartFile voterImage, VoterDTO voterDTO) throws IOException {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + employeeId));

        VoterDetails existing = employee.getVoterDetails();

        if (existing == null) {
            throw new APIException("Voter ID details not found for this employee");
        }

        if (!existing.getVoterIdNumber().equals(voterDTO.getVoterIdNumber())) {
            throw new APIException("Voter ID number cannot be changed once submitted");
        }

        if (voterImage != null && !voterImage.isEmpty()) {
            String fileName = fileService.uploadImage(path, voterImage);
            existing.setUploadVoter(fileName);
        }

        existing.setFullName(voterDTO.getFullName());
        existing.setRelationName(voterDTO.getRelationName());
        existing.setGender(voterDTO.getGender());
        existing.setDateOfBirth(voterDTO.getDateOfBirth());
        existing.setAddress(voterDTO.getAddress());
        existing.setIssuedDate(voterDTO.getIssuedDate());

        return voterIdRepository.save(existing);
    }


    public VoterDetails deleteByEmployeeId(String employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + employeeId));

        VoterDetails voterDetails = employee.getVoterDetails();

        if (voterDetails == null) {
            throw new APIException("Voter ID details not found for this employee" + employeeId);
        }
        employee.setVoterDetails(null);
        employeeRepository.save(employee);
        voterIdRepository.deleteById(voterDetails.getVoterIdNumber());
        return voterDetails;
    }
}