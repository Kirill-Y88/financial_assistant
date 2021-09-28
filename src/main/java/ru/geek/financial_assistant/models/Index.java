package ru.geek.financial_assistant.models;


import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Entity
@Table(name = "indices")
public class Index {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_company")
    private Company company;

    @Column(name = "per")
    private Long per;

    @Column(name = "date")
    private LocalDate data;

    @Column(name = "time")
    private LocalTime time;

    @Column(name = "price_open")
    private Float priceOpen;

    @Column(name = "price_high")
    private Float priceHigh;

    @Column(name = "price_low")
    private Float priceLow;

    @Column(name = "price_close")
    private Float priceClose;

    @Column(name = "vol")
    private Long vol;

    public Index(){

    }

    public Index(Long id, Company company, Long per, LocalDate data, LocalTime time, Float priceOpen, Float priceHigh, Float priceLow, Float priceClose, Long vol) {
        this.id = id;
        this.company = company;
        this.per = per;
        this.data = data;
        this.time = time;
        this.priceOpen = priceOpen;
        this.priceHigh = priceHigh;
        this.priceLow = priceLow;
        this.priceClose = priceClose;
        this.vol = vol;
    }
}
