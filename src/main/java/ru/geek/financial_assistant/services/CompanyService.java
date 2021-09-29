package ru.geek.financial_assistant.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.geek.financial_assistant.models.Company;
import ru.geek.financial_assistant.models.CompanyDTO;
import ru.geek.financial_assistant.repositories.CompanyRepository;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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

    public Company findCompanyByName(String name){
       return companyRepository.findCompanyByName(name);
    }

    public void loadCompanies(){
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader("src/main/resources/download/draft2.csv"))) {
            String row;
            String[] data;
            row = bufferedReader.readLine();
            while ( (row = bufferedReader.readLine())!=null){
                data = row.split(",");
                companyRepository.save(new Company(Long.valueOf(data[0]), Long.valueOf(data[1]), data[2], data[3]));
                System.out.println(" " + row + " save" );
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }



}
