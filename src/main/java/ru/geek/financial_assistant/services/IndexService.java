package ru.geek.financial_assistant.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.geek.financial_assistant.library.GetRequestFinam;
import ru.geek.financial_assistant.models.Company;
import ru.geek.financial_assistant.models.Index;
import ru.geek.financial_assistant.models.IndexDTO;
import ru.geek.financial_assistant.repositories.IndexRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IndexService {

    private IndexRepository indexRepository;
    private GetRequestFinam getRequestFinam;


    public IndexService() {
        getRequestFinam = new GetRequestFinam();
    }

    @Autowired
    public void setIndexRepository(IndexRepository indexRepository) {
        this.indexRepository = indexRepository;
    }

    public List<IndexDTO> findAll (){
        return (indexRepository.findAll().stream().map(IndexDTO::new)).collect(Collectors.toList());
    }

    public void getIndices (){
        boolean downloadOk;
        downloadOk = getRequestFinam.downloadDataFromSite(1,
                15544,
                "KMAZ",
                7,
                LocalDate.of(2013, 1, 1),
                LocalDate.of(2015, 2, 1),
                "csv" );
        System.out.println("dowloadOK = " + downloadOk);
        getRequestFinam.readDateFromFile();
    }


    public void create (){
        indexRepository.save(new Index(new Company(1l,15554l,"КАМАЗ","KMAZ"),60l,LocalDate.of(2021,1,5), LocalTime.of(14,00,00),72.4f,72.5f,72.9f,70f,1955l));

    }

}
