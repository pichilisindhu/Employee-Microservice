package com.hrms.project.service;

import com.hrms.project.entity.Archive;
import com.hrms.project.repository.ArchiveRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArchivedServiceImpl {
    @Autowired
    private ArchiveRepository archiveRepository;
    @Autowired
    private ModelMapper modelMapper;

    public List<Archive> getEmployee() {

        return archiveRepository.findAll();




    }
}
