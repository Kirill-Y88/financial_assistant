package ru.geek.financial_assistant.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;
import ru.geek.financial_assistant.configs.Config;
import ru.geek.financial_assistant.library.GetRequestFinam;
import ru.geek.financial_assistant.models.Company;
import ru.geek.financial_assistant.models.Index;
import ru.geek.financial_assistant.models.IndexDTO;
import ru.geek.financial_assistant.repositories.IndexRepository;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IndexService {

    private IndexRepository indexRepository;
    private GetRequestFinam getRequestFinam;
    private CompanyService companyService;

    ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);

    /*public IndexService() {
        getRequestFinam = new GetRequestFinam();
    }*/

    public IndexService() {
        ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
        getRequestFinam = context.getBean(GetRequestFinam.class);
    }

    @Autowired
    public void setCompanyService(CompanyService companyService) {
        this.companyService = companyService;
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
        downloadOk = getRequestFinam.downloadDataFromSite(
                15544,
                "KMAZ",
                LocalDate.of(2013, 1, 1),
                LocalDate.of(2015, 2, 1) );
        System.out.println("dowloadOK = " + downloadOk);
        getRequestFinam.readDateFromFile();
    }

    public void getIndicesForCompany(String name, String dateFrom, String dateTo){
        System.out.println("name = " + name + " dfromyear = " + Integer.parseInt(dateFrom.substring(0,4)) + " dfrommount = " + Integer.parseInt(dateFrom.substring(4,5)) + " dfromday = " + Integer.parseInt(dateFrom.substring(6,7)) );

            LocalDate dateStart = LocalDate.of(Integer.parseInt(dateFrom.substring(0,4)),
                    Integer.parseInt(dateFrom.substring(4,6)),
                    Integer.parseInt(dateFrom.substring(6)));
            LocalDate dateFinish= LocalDate.of(Integer.parseInt(dateTo.substring(0,4)),
                    Integer.parseInt(dateTo.substring(4,6)),
                    Integer.parseInt(dateTo.substring(6)));

            Company company = companyService.findCompanyByName(name);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    getRequestFinam.downloadDataFromSite(company.getEm(), company.getCode(), dateStart,dateFinish);
                    getRequestFinam.readDateFromFile();

                    File download = getRequestFinam.getDownload();
                    String row;
                    try {
                        BufferedReader bufferedReader = new BufferedReader(new FileReader(download.getPath()));
                        bufferedReader.readLine();
                        while ( (row = bufferedReader.readLine())!=null){
                            String[] data = row.split(",");
                            for (String s: data    ) {
                                System.out.print(s + " // ");
                            }
                            addIndex(new Index(company,
                                    data[1],
                                    LocalDate.of(Integer.parseInt(data[2].substring(0,4)) , Integer.parseInt(data[2].substring(4,6)), Integer.parseInt(data[2].substring(6)) ),
                                    LocalTime.of(Integer.parseInt(data[3].substring(0,2)) , Integer.parseInt(data[3].substring(2,4)), Integer.parseInt(data[3].substring(4)) ),
                                    Float.parseFloat(data[4]),
                                    Float.parseFloat(data[5]),
                                    Float.parseFloat(data[6]),
                                    Float.parseFloat(data[7]),
                                    Long.parseLong(data[8])));

                            System.out.println("add");
                        }
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    getRequestFinam.deleteFile();

                }
            }).start();
    }

    public void addIndex (Index index){
        indexRepository.save(index);
    }


    public void create (){
        indexRepository.save(new Index(new Company(1l,15554l,"КАМАЗ","KMAZ"),"60",LocalDate.of(2021,1,5), LocalTime.of(14,00,00),72.4f,72.5f,72.9f,70f,1955l));

    }

}
