package ru.geek.financial_assistant.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.geek.financial_assistant.models.IndexDTO;
import ru.geek.financial_assistant.services.FinancialAlgorithmsService;

import java.util.List;


@RestController
@RequestMapping("/api/v1/fAlgorithms")
public class FinancialAlgorithmsController {

    private FinancialAlgorithmsService financialAlgorithmsService;

    @Autowired
    public void setFinancialAlgorithmsService(FinancialAlgorithmsService financialAlgorithmsService) {
        this.financialAlgorithmsService = financialAlgorithmsService;
    }

    @PostMapping("/getIndicesForPeriod")
    public List<IndexDTO> getIndicesForPeriod (@RequestParam String nameCompany,
                                               @RequestParam String dateFinish,
                                               @RequestParam int numberOfDay){

    return financialAlgorithmsService.getIndicesForPeriod(nameCompany, dateFinish, numberOfDay);
    }

    @PostMapping("/getRSIForPeriod")
    public float getRSI (@RequestParam String nameCompany,
                         @RequestParam String dateFinish,
                         @RequestParam int numberOfDay){
        return financialAlgorithmsService.getRSI(nameCompany, dateFinish, numberOfDay);
    }



}
