package ru.geek.financial_assistant.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import ru.geek.financial_assistant.configs.Config;
import ru.geek.financial_assistant.library.FinancialAlgorithms;
import ru.geek.financial_assistant.library.GetRequestFinam;
import ru.geek.financial_assistant.models.Company;
import ru.geek.financial_assistant.models.IndexDTO;
import ru.geek.financial_assistant.repositories.IndexRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class FinancialAlgorithmsService {

    private IndexService indexService;
    private FinancialAlgorithms financialAlgorithms;
    private CompanyService companyService;

    public FinancialAlgorithmsService() {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(Config.class);
        financialAlgorithms = applicationContext.getBean(FinancialAlgorithms.class);
    }

    @Autowired
    public void setIndexService(IndexService indexService) {
        this.indexService = indexService;
    }

    @Autowired
    public void setCompanyService(CompanyService companyService) {
        this.companyService = companyService;
    }


    public List<IndexDTO> getIndicesForPeriod (String name, String dateFinish, int numberOfDay){

        LocalDate finalDate= LocalDate.of(Integer.parseInt(dateFinish.substring(0,4)),
                Integer.parseInt(dateFinish.substring(4,6)),
                Integer.parseInt(dateFinish.substring(6)));
        Company company = companyService.findCompanyByName(name);
        return indexService.getAllIndicesByCompanyForLastPeriod(company,finalDate,numberOfDay);
    }

    public float getRSI (String nameCompany, String dateFinish, int numberOfDay){
        return financialAlgorithms.getRSI(getIndicesForPeriod(nameCompany, dateFinish, numberOfDay));
    }






}
