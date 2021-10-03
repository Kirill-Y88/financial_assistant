package ru.geek.financial_assistant.controllers;

import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.geek.financial_assistant.models.Company;
import ru.geek.financial_assistant.models.IndexDTO;
import ru.geek.financial_assistant.services.CompanyService;
import ru.geek.financial_assistant.services.IndexService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/indices")
public class IndexController {


    private IndexService indexService;
    private CompanyService companyService;

    @Autowired
    public void setCompanyService(CompanyService companyService) {
        this.companyService = companyService;
    }

    @Autowired
    public void setIndexService(IndexService indexService) {
        this.indexService = indexService;
    }

    @GetMapping
    public List<IndexDTO> getAll (){
        return indexService.findAll();
    }

    @GetMapping("/getIndices")
    public void getIndices (){
        indexService.getIndices();
    }

    @GetMapping("/create")
    public void create (){
        //    indexService.create();
    }

    @PostMapping("/downloadIndicesForCompany")
    public void downloadIndicesForCompany(@RequestParam String name,
                                     @RequestParam String dateStart,
                                     @RequestParam String dateFinish){
        System.out.println("name = " + name + " dstart = " + dateStart + " dfinish = " + dateFinish);
        indexService.downloadIndicesForCompany(name,dateStart,dateFinish);
    }

    @GetMapping("/getIndicesForCompany/{name}/{dateStart}/{dateFinish}")
    public void getIndicesForCompany2(@PathVariable String name,
                                      @PathVariable String dateStart,
                                      @PathVariable String dateFinish){
        indexService.downloadIndicesForCompany(name,dateStart,dateFinish);
    }

    @PostMapping("/getLastIndex")
    public IndexDTO getLastIndex(@RequestParam String name){
        Company company = companyService.findCompanyByName(name);
       // Company company = new Company(371l,15544l,"КАМАЗ","KMAZ");
       return indexService.getLastIndex(company);
    }

    @PostMapping("/getFirstIndex")
    public IndexDTO getFirstIndex(@RequestParam String name){
        Company company = companyService.findCompanyByName(name);
        // Company company = new Company(371l,15544l,"КАМАЗ","KMAZ");
        return indexService.getFirstIndex(company);
    }

    @PostMapping("/getAllIndicesByCompany")
    public List<IndexDTO> getAllIndicesByCompany(@RequestParam String name){
        Company company = companyService.findCompanyByName(name);
        return indexService.getAllIndicesByCompany(company);
    }


}
