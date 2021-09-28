package ru.geek.financial_assistant.models;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
public class CompanyDTO {

    private Long id;
    private Long em;
    private String name;
    private String code;

    public CompanyDTO(){

    }

    public CompanyDTO(Company company) {
        this.id = company.getId();
        this.em = company.getEm();
        this.name = company.getName();
        this.code = company.getCode();
    }

    public Company convertToCompany(){
        Company company = new Company();
        company.setId(this.id);
        company.setEm(this.em);
        company.setName(this.name);
        company.setCode(this.code);
        return company;
    }




}
