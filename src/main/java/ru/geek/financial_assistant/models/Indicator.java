package ru.geek.financial_assistant.models;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "indicators")
public class Indicator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "id_indices")
    private Index index;

    @Column(name = "RSI")
    private Float rsi;

    @Column(name = "MACD")
    private Float macd;

    @Column(name = "MACDG")
    private Float macdg;

    @Column(name = "GARCH")
    private Float garch;

    public Indicator() {
    }

    public Indicator(Index index, Float rsi, Float macd, Float macdg, Float garch) {
        this.index = index;
        this.rsi = rsi;
        this.macd = macd;
        this.macdg = macdg;
        this.garch = garch;
    }



}
