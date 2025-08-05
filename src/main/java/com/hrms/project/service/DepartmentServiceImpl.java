package com.hrms.project.service;

import com.hrms.project.entity.Department;
import com.hrms.project.entity.Employee;
import com.hrms.project.handlers.DepartmentNotFoundException;
import com.hrms.project.handlers.EmployeeNotFoundException;
import com.hrms.project.dto.DepartmentDTO;
import com.hrms.project.dto.EmployeeDepartmentDTO;
import com.hrms.project.dto.EmployeeTeamResponse;
import com.hrms.project.repository.DepartmentRepository;
import com.hrms.project.repository.EmployeeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService{

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private ModelMapper modelMapper;


    @Override
    public DepartmentDTO saveDepartment(DepartmentDTO departmentDTO) {

        Department dept= modelMapper.map(departmentDTO,Department.class);
        departmentRepository.save(dept);
        return modelMapper.map(departmentDTO,DepartmentDTO.class);

    }


    @Override
    public EmployeeDepartmentDTO getEmployeesByDepartmentId(String departmentId) {
       Department dept=departmentRepository.findById(departmentId)
               .orElseThrow(()->new DepartmentNotFoundException(departmentId));
       EmployeeDepartmentDTO employeeDepartmentDTO=new EmployeeDepartmentDTO();
       employeeDepartmentDTO.setDepartmentId(dept.getDepartmentId());
       employeeDepartmentDTO.setDepartmentName(dept.getDepartmentName());


           List<EmployeeTeamResponse> employeeTeamResponses= dept.getEmployee().stream()
               .map(employee -> {
                   Employee empl = employeeRepository.findById(employee.getEmployeeId())
                           .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with employee id :"+employee.getEmployeeId()));
                   return modelMapper.map(employee,EmployeeTeamResponse.class);
               }).toList();

           employeeDepartmentDTO.setEmployeeList(employeeTeamResponses);
           return employeeDepartmentDTO;



    }

    @Override
    public DepartmentDTO updateDepartment(String departmentId, DepartmentDTO departmentDTO) {

        Department dept=departmentRepository.findById(departmentId)
                        .orElseThrow(() -> new DepartmentNotFoundException("Department not found with id: "+ departmentId));

        dept.setDepartmentName(departmentDTO.getDepartmentName());
        dept.setDepartmentDescription(departmentDTO.getDepartmentDescription());
        return modelMapper.map( departmentRepository.save(dept), DepartmentDTO.class);



    }

    @Override
    public List<DepartmentDTO> getAllDepartmentDetails() {
        List<Department> dept=departmentRepository.findAll();
        List<DepartmentDTO> departmentDTOS=new ArrayList<>();
        for(Department deptDTO:dept){
            DepartmentDTO departmentDTO=modelMapper.map(deptDTO,DepartmentDTO.class);
            departmentDTOS.add(departmentDTO);

        }
        return departmentDTOS;
    }

    @Override
    public DepartmentDTO getByDepartmentId(String departmentId) {

        Department dept=departmentRepository.findById(departmentId)
                .orElseThrow(() -> new DepartmentNotFoundException("Department not found with id: "+ departmentId));

        return modelMapper.map(dept ,DepartmentDTO.class);


    }

    @Override
    public EmployeeDepartmentDTO getEmployeeByEmployeeId(String employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + employeeId));

        Department dept = employee.getDepartment();

        EmployeeDepartmentDTO employeeDepartmentDTO = new EmployeeDepartmentDTO();
        employeeDepartmentDTO.setDepartmentId(dept.getDepartmentId());
        employeeDepartmentDTO.setDepartmentName(dept.getDepartmentName());

        List<EmployeeTeamResponse> employeeTeamResponses = dept.getEmployee().stream()
                .map(emp -> {
                    Employee empl = employeeRepository.findById(emp.getEmployeeId())
                            .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + emp.getEmployeeId()));

                    return modelMapper.map(empl,EmployeeTeamResponse.class);
                }).toList();

        employeeDepartmentDTO.setEmployeeList(employeeTeamResponses);

        return employeeDepartmentDTO;
    }

    @Override
    public String deleteDepartment(String departmentId) {

        Department dept=departmentRepository.findById(departmentId)
                .orElseThrow(() -> new DepartmentNotFoundException("Department not found with id: " + departmentId));
        List<Employee> employees = dept.getEmployee();
        for (Employee emp : employees) {
            emp.setDepartment(null);
            employeeRepository.save(emp);
        }

        departmentRepository.delete(dept);
        return "Department deleted successfully";

    }

}
