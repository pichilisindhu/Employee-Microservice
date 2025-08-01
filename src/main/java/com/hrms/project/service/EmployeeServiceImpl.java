package com.hrms.project.service;
import com.hrms.project.entity.*;
import com.hrms.project.handlers.APIException;
import com.hrms.project.handlers.DepartmentNotFoundException;
import com.hrms.project.handlers.EmployeeNotFoundException;
import com.hrms.project.dto.*;
import com.hrms.project.repository.*;
import org.modelmapper.ModelMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.time.LocalDate;
import java.util.*;


@Service
public class EmployeeServiceImpl implements EmployeeService {


    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private ArchiveRepository archiveRepository;

    @Autowired
    private FileService fileService;

    @Value("${project.image}")
    private String path;


    @Override
    public EmployeeDTO createData(MultipartFile employeeImage,EmployeeDTO employeeDTO) throws IOException {


        if (employeeRepository.findById(employeeDTO.getEmployeeId()).isPresent()) {
            throw new APIException("Employee already exists");
        }

        Employee employee = modelMapper.map(employeeDTO, Employee.class);

        if (employeeImage != null && !employeeImage.isEmpty()) {
            String fileName = fileService.uploadImage(path, employeeImage);
            employee.setEmployeeImage(fileName);
        }

        if (employeeDTO.getDepartmentId() != null) {
            Department dept = departmentRepository.findById(employeeDTO.getDepartmentId())
                    .orElseThrow(() -> new DepartmentNotFoundException("Department not found with name: " + employeeDTO.getDepartmentId()));
            employee.setDepartment(dept);
        }

        employeeRepository.save(employee);

        return modelMapper.map(employee, EmployeeDTO.class);


    }


    @Override
    public EmployeeDTO getEmployeeById(String id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + id));
        return modelMapper.map(employee, EmployeeDTO.class);



    }

    @Override
    public List<EmployeeDTO> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        List<EmployeeDTO> allEmployeeDTOs = new ArrayList<>();

        for (Employee employee : employees) {
            EmployeeDTO employeeDTO = modelMapper.map(employee, EmployeeDTO.class);

            allEmployeeDTOs.add(employeeDTO);
        }

        return allEmployeeDTOs;
    }


    @Override
    public EmployeeDTO updateEmployee(String id, MultipartFile employeeImage, EmployeeDTO employeeDTO) throws IOException {

        Employee updateEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + id));

        if (employeeDTO.getDepartmentId() != null) {

            updateEmployee.setDepartment(null);
            employeeRepository.save(updateEmployee);

            Department department = departmentRepository.findById(employeeDTO.getDepartmentId())
                    .orElseThrow(() -> new DepartmentNotFoundException("Department not found with id: " + employeeDTO.getDepartmentId()));
            updateEmployee.setDepartment(department);
        }

        String existingImage = updateEmployee.getEmployeeImage();
        modelMapper.map(employeeDTO, updateEmployee);
        if (employeeImage != null && !employeeImage.isEmpty()) {
            String fileName = fileService.uploadImage(path, employeeImage);
            updateEmployee.setEmployeeImage(fileName);
        } else {
            updateEmployee.setEmployeeImage(existingImage);
        }


        employeeRepository.save(updateEmployee);

        return modelMapper.map(updateEmployee, EmployeeDTO.class);
    }


    @Override
    public ContactDetailsDTO getEmployeeContactDetails(String employeeId) {

        Employee employeeDetails=employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + employeeId));
          return modelMapper.map(employeeDetails, ContactDetailsDTO.class);


    }

    @Override
    public List<ContactDetailsDTO> getAllEmployeeContactDetails()
    {
        List<Employee> allEmployeeDetails=employeeRepository.findAll();
        return allEmployeeDetails.stream()
                .map(employeeDetails-> modelMapper.map(employeeDetails,ContactDetailsDTO.class))
                .toList();
    }

    @Override
    public ContactDetailsDTO updateContactDetails(String employeeId,ContactDetailsDTO contactDetailsDTO) {
        Employee employee=employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + employeeId));

        modelMapper.map(contactDetailsDTO,employee);
        employeeRepository.save(employee);
        return modelMapper.map(employee, ContactDetailsDTO.class);
    }

    @Override
    public AddressDTO getAddress(String employeeId) {
       Employee employee=employeeRepository.findById(employeeId)
               .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + employeeId));
       return modelMapper.map(employee, AddressDTO.class);
    }

    @Override
    public List<AddressDTO> getAllAddress() {
        List<Employee> allEmployee=employeeRepository.findAll();
       return allEmployee.stream()
                .map(employeeDetails-> modelMapper.map(employeeDetails, AddressDTO.class))
                .toList();
    }

    @Override
    public AddressDTO updateEmployeeAddress(String employeeId, AddressDTO addressDTO) {
        Employee employee=employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + employeeId));

        modelMapper.map(addressDTO,employee);
        employeeRepository.save(employee);
        return modelMapper.map(employee, AddressDTO.class);
    }

    @Override
    public EmployeePrimaryDetailsDTO getEmployeePrimaryDetails(String employeeId) {
       Employee employeeDetails=employeeRepository.findById(employeeId)
               .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + employeeId));
       return modelMapper.map(employeeDetails, EmployeePrimaryDetailsDTO.class);
    }

    @Override
    public EmployeePrimaryDetailsDTO updateEmployeeDetails(String employeeId, EmployeePrimaryDetailsDTO employeePrimaryDetailsDTO) {
        System.out.println(employeePrimaryDetailsDTO);
        Employee employeeDetails=employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + employeeId));
        modelMapper.map(employeePrimaryDetailsDTO,employeeDetails);
        employeeRepository.save(employeeDetails);
        return modelMapper.map(employeeDetails, EmployeePrimaryDetailsDTO.class);

    }

    @Override
    public JobDetailsDTO getJobDetails(String employeeId) {
      Employee employee=employeeRepository.findById(employeeId)
              .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + employeeId));
       return modelMapper.map(employee, JobDetailsDTO.class);
    }

    @Override
    public JobDetailsDTO updateJobDetails(String employeeId, JobDetailsDTO jobDetailsDTO) {
       Employee employee=employeeRepository.findById(employeeId)
               .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + employeeId));
       modelMapper.map(employee,jobDetailsDTO);
        employeeRepository.save(employee);
        return modelMapper.map(employee, JobDetailsDTO.class);
    }


    @Override
    public EmployeeDTO deleteEmployee(String employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + employeeId));

        employee.setDateOfLeaving(LocalDate.now());
        employeeRepository.save(employee);


        Archive archive=  modelMapper.map(employee,Archive.class);

        if (employee.getAadhaarCardDetails() != null) {
            archive.setAadharNumber(employee.getAadhaarCardDetails().getAadhaarNumber());
            archive.setAadharImage(employee.getAadhaarCardDetails().getUploadAadhaar());
        }

        if (employee.getPanDetails() != null) {
            archive.setPanNumber(employee.getPanDetails().getPanNumber());
            archive.setPanImage(employee.getPanDetails().getPanImage());
        }

        if (employee.getPassportDetails() != null) {
            archive.setPassportNumber(employee.getPassportDetails().getPassportNumber());
            archive.setPassportImage(employee.getPassportDetails().getPassportImage());
        }

        if (employee.getDepartment() != null) {
            archive.setDepartmentId(employee.getDepartment().getDepartmentId());
        }

        List<DegreeCertificates> documents = employee.getDegreeCertificates();
        if (documents != null && !documents.isEmpty()) {
            archive.setDegreeDocuments(
                    documents.stream().map(DegreeCertificates::getAddFiles).toList()
            );
        }

        List<Project> projects = employee.getProjects();
        if (projects != null && !projects.isEmpty()) {
            archive.setProjectId(
                    projects.stream().map(Project::getProjectId).toList()
            );
        }

        List<Team> teams = employee.getTeams();
        if (teams != null && !teams.isEmpty()) {
            archive.setTeamId(
                    teams.stream().map(Team::getTeamId).toList()
            );
        }

        archiveRepository.save(archive);


        if (teams != null) {
            for (Team team : teams) {
                team.getEmployees().remove(employee);
            }
            employee.getTeams().clear();
        }

        if (projects != null) {
            for (Project project : projects) {
                project.getEmployees().remove(employee);
            }
            employee.getProjects().clear();
        }

        employeeRepository.delete(employee);

        return modelMapper.map(employee, EmployeeDTO.class);
    }
}


















