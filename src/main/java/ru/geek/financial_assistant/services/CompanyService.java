package ru.geek.financial_assistant.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.geek.financial_assistant.models.Company;
import ru.geek.financial_assistant.models.CompanyDTO;
import ru.geek.financial_assistant.repositories.CompanyRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompanyService {


   private CompanyRepository companyRepository;

   @Autowired
    public void setCompanyRepository(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public List<CompanyDTO> findAll(){
         return    (companyRepository.findAll().stream().map(CompanyDTO::new)).collect(Collectors.toList());
    }





}
