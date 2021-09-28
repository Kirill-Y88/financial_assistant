package ru.geek.financial_assistant.models;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class IndexDTO {

    private Long id;
    private CompanyDTO companyDTO;
    private Long per;
    private LocalDate data;
    private LocalTime time;
    private Float priceOpen;
    private Float priceHigh;
    private Float priceLow;
    private Float priceClose;
    private Long vol;

    public IndexDTO(Index index) {
        this.id = index.getId();
        this.companyDTO = new CompanyDTO(index.getCompany());
        this.per = index.getPer();
        this.data = index.getData();
        this.time = index.getTime();
        this.priceOpen = index.getPriceOpen();
        this.priceHigh = index.getPriceHigh();
        this.priceLow = index.getPriceLow();
        this.priceClose = index.getPriceClose();
        this.vol = index.getVol();
    }

    public IndexDTO() {
    }

    public Index convertToIndex(){
        Index index = new Index(id,companyDTO.convertToCompany(),per,data,time,priceOpen,priceHigh,priceLow,priceClose,vol);
        return index;
    }





}
