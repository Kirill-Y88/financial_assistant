package ru.geek.financial_assistant.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import ru.geek.financial_assistant.configs.Config;
import ru.geek.financial_assistant.library.FinancialAlgorithms;
import ru.geek.financial_assistant.library.GetRequestFinam;
import ru.geek.financial_assistant.models.*;
import ru.geek.financial_assistant.repositories.IndexRepository;
import ru.geek.financial_assistant.repositories.IndicatorRepository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FinancialAlgorithmsService {

    private IndexService indexService;
    private FinancialAlgorithms financialAlgorithms;
    private CompanyService companyService;
    private IndicatorRepository indicatorRepository;

    public FinancialAlgorithmsService() {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(Config.class);
        financialAlgorithms = applicationContext.getBean(FinancialAlgorithms.class);
    }

    @Autowired
    public void setIndicatorRepository(IndicatorRepository indicatorRepository) {
        this.indicatorRepository = indicatorRepository;
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

    public float getEMA (String nameCompany, String dateFinish, int numberOfDay, float k){
        Company company = companyService.findCompanyByName(nameCompany);
        LocalDate finalDate= LocalDate.of(Integer.parseInt(dateFinish.substring(0,4)),
                Integer.parseInt(dateFinish.substring(4,6)),
                Integer.parseInt(dateFinish.substring(6)));
        LocalDate dateStart = finalDate.minusDays(numberOfDay);
        List<IndexDTO> indexDTOList = indexService.findIndicesByCompanyAndDateForCloseTime(company,dateStart,finalDate)
                .stream().map(IndexDTO::new).collect(Collectors.toList());
        float[] priceCloseArray = new float[indexDTOList.size()];

        for (int i = 0; i < indexDTOList.size(); i++) {
            priceCloseArray[i] = indexDTOList.get(i).getPriceClose();
        }
        return financialAlgorithms.getEMA(priceCloseArray, k);
    }

    public float getMACD (String nameCompany, String dateFinish, float k){
        return getEMA(nameCompany,dateFinish, 12, k) - getEMA(nameCompany,dateFinish, 26, k);
    }

    //сохранение индикатора с со сзначениями полей RSI и MACD   'к' - коэффициент сглаживания, чем больше 'к' - тем более влияние последних индексов
    public IndicatorDTO saveIndicator (String nameCompany, String dateFinish, float k){
        Company company = companyService.findCompanyByName(nameCompany);
        LocalDate finalDate= LocalDate.of(Integer.parseInt(dateFinish.substring(0,4)),
                Integer.parseInt(dateFinish.substring(4,6)),
                Integer.parseInt(dateFinish.substring(6)));
        Index index = indexService.findIndexByCompanyAndDate(company,finalDate);

        float rsi = getRSI(nameCompany,dateFinish, 7);
        float macd = getMACD(nameCompany, dateFinish, k);

       return  new IndicatorDTO (indicatorRepository.saveAndFlush(new Indicator(index, rsi, macd, 0f, 0f)));
    }

    //добавить MACDG в индикатор
    public IndicatorDTO addToIndicatorMACDG (String nameCompany, String dateFinish, float k){
        Company company = companyService.findCompanyByName(nameCompany);
        LocalDate finalDate= LocalDate.of(Integer.parseInt(dateFinish.substring(0,4)),
                Integer.parseInt(dateFinish.substring(4,6)),
                Integer.parseInt(dateFinish.substring(6)));
        Index index = indexService.findIndexByCompanyAndDate(company,finalDate);

        LocalDate dateStart = finalDate.minusDays(9);

        List<Index> indexList = indexService.findIndicesByCompanyAndDateForCloseTime(company,dateStart,finalDate);
        float[] macd = new float[indexList.size()];
        for (int i = 0; i < indexList.size(); i++) {
            macd[i] = indicatorRepository.findById_indices(indexList.get(i)).getMacd();
        }

        float signalLine = financialAlgorithms.getEMA(macd, k);
        Indicator indicator = indicatorRepository.findById_indices(index);

        float macdg = indicator.getMacd() - signalLine;
        indicator.setMacdg(macdg);
        return new IndicatorDTO (indicatorRepository.saveAndFlush(indicator));
    }

    //сохранение индикаторов за определенный период
    public List <IndicatorDTO> saveIndicatorForPeriod (String nameCompany,String dateStart, String dateFinish, float k){
        Company company = companyService.findCompanyByName(nameCompany);

        LocalDate localDateFinish = LocalDate.of(Integer.parseInt(dateFinish.substring(0,4)),
                Integer.parseInt(dateFinish.substring(4,6)),
                Integer.parseInt(dateFinish.substring(6)));
        LocalDate localDateStart = LocalDate.of(Integer.parseInt(dateStart.substring(0,4)),
                Integer.parseInt(dateStart.substring(4,6)),
                Integer.parseInt(dateStart.substring(6)));
        List<Index> indexList = indexService.findIndicesByCompanyAndDateForCloseTime(company,localDateStart,localDateFinish);

        List<IndicatorDTO> indicatorDTOList = new ArrayList<>();

        for (int i = 0; i <indexList.size(); i++) {
            indicatorDTOList.add(saveIndicator(nameCompany,
                    indexList.get(i).getDate().toString().replace("-",""),
                    k));
        }
        return indicatorDTOList;
    }

    public List<IndicatorDTO> addToIndicatorMACDGForPeriod (String nameCompany,String dateStart, String dateFinish, float k){
        Company company = companyService.findCompanyByName(nameCompany);
        LocalDate localDateFinish = LocalDate.of(Integer.parseInt(dateFinish.substring(0,4)),
                Integer.parseInt(dateFinish.substring(4,6)),
                Integer.parseInt(dateFinish.substring(6)));
        LocalDate localDateStart = LocalDate.of(Integer.parseInt(dateStart.substring(0,4)),
                Integer.parseInt(dateStart.substring(4,6)),
                Integer.parseInt(dateStart.substring(6)));
        List<Index> indexList = indexService.findIndicesByCompanyAndDateForCloseTime(company,localDateStart,localDateFinish);

        List<IndicatorDTO> indicatorDTOList = new ArrayList<>();
        for (int i = 0; i <indexList.size(); i++) {
            indicatorDTOList.add(addToIndicatorMACDG(nameCompany,
                    indexList.get(i).getDate().toString().replace("-",""),
                    k));
        }
        return indicatorDTOList;
    }














}
