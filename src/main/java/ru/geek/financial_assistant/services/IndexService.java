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

import javax.persistence.NoResultException;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Service
public class IndexService {

    private IndexRepository indexRepository;
    private GetRequestFinam getRequestFinam;
    private CompanyService companyService;

    ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);

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

    //тестовый метод
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

    public void downloadIndicesForCompany(String name, String dateFrom, String dateTo){
        System.out.println("name = " + name + " dfromyear = " + Integer.parseInt(dateFrom.substring(0,4)) + " dfrommount = " + Integer.parseInt(dateFrom.substring(4,6)) + " dfromday = " + Integer.parseInt(dateFrom.substring(6)) );

            LocalDate dateStart = LocalDate.of(Integer.parseInt(dateFrom.substring(0,4)),
                    Integer.parseInt(dateFrom.substring(4,6)),
                    Integer.parseInt(dateFrom.substring(6)));
            LocalDate dateFinish= LocalDate.of(Integer.parseInt(dateTo.substring(0,4)),
                    Integer.parseInt(dateTo.substring(4,6)),
                    Integer.parseInt(dateTo.substring(6)));

            Company company = companyService.findCompanyByName(name);

            //крайние даты хранимых индексов
        LocalDate dateLast = null;
        LocalDate dateFirst = null;
        try {
            dateLast = indexRepository.findLastIndexByCompany(company).getDate();
        }catch (NoResultException e){
            e.printStackTrace();
        }
        try {
            dateFirst = indexRepository.findFirstIndexByCompany(company).getDate();
        }catch (NoResultException e){
            e.printStackTrace();
        }

        //тестовые логи
        try {
            System.out.println(" разница начальных дат = " + dateFirst.until(dateStart,ChronoUnit.DAYS)
                    + "  рфзница конечных дат =" + dateLast.until(dateFinish,ChronoUnit.DAYS));
        }catch (Exception e){
            e.printStackTrace();
        }


            //если данные еще не были загружены
            if(dateLast == null || dateFirst == null){
                startNewThreadForDownloadFromSite(company, dateStart, dateFinish);
                System.out.println("если данные еще не были загружены");
                return;
            }

            //если необходимо добавить только период до существующих данных в БД
            if(dateFirst.until(dateStart,ChronoUnit.DAYS)<0 && dateLast.until(dateFinish,ChronoUnit.DAYS)<=0){
                System.out.println("dataFirst = " + dateFirst + " data start = " + dateStart +
                        " dataLast = " + dateLast + " dataFinish = " + dateFinish);

                LocalDate finalDateFirst = dateFirst;
                startNewThreadForDownloadFromSite(company,dateStart,finalDateFirst);

                System.out.println("если необходимо добавить только период до существующих данных в БД");
                return;
            }

            //если необходимо добавить только период после существующих данных в БД
            if(dateFirst.until(dateStart,ChronoUnit.DAYS)>=0 && dateLast.until(dateFinish,ChronoUnit.DAYS)>0){
                System.out.println("dataFirst = " + dateFirst + " data start = " + dateStart +
                        " dataLast = " + dateLast + " dataFinish = " + dateFinish);

                LocalDate finalDateLast = dateLast;
                startNewThreadForDownloadFromSite(company,finalDateLast, dateFinish);

                System.out.println("если необходимо добавить только период после существующих данных в БД");
                return;
            }
            //если необходимо добавить периоды до и после существующих данных в БД
            if(dateFirst.until(dateStart,ChronoUnit.DAYS)<0 && dateLast.until(dateFinish,ChronoUnit.DAYS)>0){

                System.out.println("dataFirst = " + dateFirst + " data start = " + dateStart +
                        " dataLast = " + dateLast + " dataFinish = " + dateFinish);

                LocalDate finalDateFirst1 = dateFirst;
                startNewThreadForDownloadFromSite(company,dateStart, finalDateFirst1);

                LocalDate finalDateLast1 = dateLast;
                startNewThreadForDownloadFromSite(company,finalDateLast1,dateFinish);

                System.out.println("если необходимо добавить периоды до и после существующих данных в БД");
                return;
            }

            //если запрошенный период уже хранится в БД
            if(dateFirst.until(dateStart,ChronoUnit.DAYS)>=0 && dateLast.until(dateFinish,ChronoUnit.DAYS)<=0){
                System.out.println("Данные уже загружены ранее");
                return;
            }

    }

    public void addIndex (Index index){
        indexRepository.save(index);
    }

    private synchronized void  downloadFromSite(Company company, LocalDate dateStart, LocalDate dateFinish){
            getRequestFinam.downloadDataFromSite(company.getEm(), company.getCode(), dateStart, dateFinish);
            getRequestFinam.readDateFromFile();

            File download = getRequestFinam.getDownload();
            String row;
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(download.getPath()));
                bufferedReader.readLine();
                while ((row = bufferedReader.readLine()) != null) {
                    String[] data = row.split(",");
                    for (String s : data) {
                        System.out.print(s + " // ");
                    }
                    addIndex(new Index(company,
                            data[1],
                            LocalDate.of(Integer.parseInt(data[2].substring(0, 4)), Integer.parseInt(data[2].substring(4, 6)), Integer.parseInt(data[2].substring(6))),
                            LocalTime.of(Integer.parseInt(data[3].substring(0, 2)), Integer.parseInt(data[3].substring(2, 4)), Integer.parseInt(data[3].substring(4))),
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

    private synchronized void startNewThreadForDownloadFromSite(Company company, LocalDate dateStart, LocalDate dateFinish){
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<LocalDate[]> localDatesArray = getListOfPeriods(dateStart,dateFinish);
                for (LocalDate[] lDA: localDatesArray ) {
                    downloadFromSite(company, lDA[0], lDA[1]);
                }
            }
        }).start();
    }

    //в связи с тем, что с сайта за раз не дает загрузить данные за период более 5 лет - запрашиваемый период необходимо разбить на более мелкие
    private List<LocalDate[]> getListOfPeriods (LocalDate dateStart, LocalDate dateFinish){
        int n = 0;
        List<LocalDate[]> localDatesArray = new ArrayList<>();
        LocalDate dateStartTemp;

        if(dateStart.until(dateFinish, ChronoUnit.YEARS) > 4){

            if((dateStart.until(dateFinish, ChronoUnit.YEARS)) % 4 != 0){
                n = (int) ((dateStart.until(dateFinish, ChronoUnit.YEARS))/4) + 1;
            } else {
                n = (int) ((dateStart.until(dateFinish, ChronoUnit.YEARS))/4);
            }
        }
        dateStartTemp = dateStart;
        for (int i = 0; i < n-1 ; i++) {
            localDatesArray.add(new LocalDate[]{dateStartTemp,dateStartTemp.plusYears(4)});
            dateStartTemp = dateStartTemp.plusYears(4).plusDays(1);
        }
        localDatesArray.add(new LocalDate[]{dateStartTemp, dateFinish});

        return localDatesArray;
    }

    public IndexDTO getLastIndex(Company company){
        return new IndexDTO(indexRepository.findLastIndexByCompany(company));
    }

    public IndexDTO getFirstIndex(Company company){
        return new IndexDTO(indexRepository.findFirstIndexByCompany(company));
    }


    public List<IndexDTO> getAllIndicesByCompany (Company company){
        return indexRepository.getAllIndicesByCompany(company).stream().map(IndexDTO::new).collect(Collectors.toList());
    }

    public List<IndexDTO> getAllIndicesByCompanyForLastPeriod (Company company, LocalDate finalDate, int numberOfDay){
    return indexRepository.findIndicesByCompanyAndDate(company, finalDate.minusDays(numberOfDay) ,finalDate).stream().map(IndexDTO::new).collect(Collectors.toList());
    }



}
