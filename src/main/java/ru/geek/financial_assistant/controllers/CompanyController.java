package ru.geek.financial_assistant.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.geek.financial_assistant.models.CompanyDTO;
import ru.geek.financial_assistant.services.CompanyService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/companies")
public class CompanyController {

    private CompanyService companyService;

    @Autowired
    public void setCompanyService(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping
    public List<CompanyDTO> getAll(){
        return companyService.findAll();
    }


    //метод для единоразовой загрузки полного перечня компаний, которые торгуются на московской бирже
    @GetMapping("/loadCompanies")
    public void loadCompanies(){
        companyService.loadCompanies();
    }

    @GetMapping("/getCompaniesNames")
    public List<String>getCompaniesNames(){
        return companyService.findAll().stream().map(CompanyDTO::getName).collect(Collectors.toList());
    }

}


